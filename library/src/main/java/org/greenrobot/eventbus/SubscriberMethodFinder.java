/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greenrobot.eventbus;

import android.util.Log;

import org.greenrobot.eventbus.meta.EmptyEventBusType;
import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.interfaces.SubscriberInfoIndex;
import org.greenrobot.eventbus.meta.SubscriberMessage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SubscriberMethodFinder {
    /*
     * In newer class files, compilers may add methods. Those are called bridge or synthetic methods.
     * EventBus must ignore both. There modifiers are not public but defined in the Java class file format:
     * http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.6-200-A.1
     */
    private static final int BRIDGE = 0x40;
    private static final int SYNTHETIC = 0x1000;

    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC | BRIDGE | SYNTHETIC;

    private static final Map<Class<?>, SubscriberMessage> METHOD_CACHE = new ConcurrentHashMap<>();


    private List<SubscriberInfoIndex> subscriberInfoIndexes;
    private final boolean strictMethodVerification;
    private final boolean ignoreGeneratedIndex;

    private static final int POOL_SIZE = 4;
    private static final FindState[] FIND_STATE_POOL = new FindState[POOL_SIZE];
    public static final HashMap<String, Class<?>> eventBusHashMap = new HashMap<>();

    static {
        eventBusHashMap.put("int", Integer.class);
        eventBusHashMap.put("boolean", Boolean.class);
        eventBusHashMap.put("float", Float.class);
        eventBusHashMap.put("double", Double.class);
    }

    SubscriberMethodFinder(List<SubscriberInfoIndex> subscriberInfoIndexes, boolean strictMethodVerification,
                           boolean ignoreGeneratedIndex) {
        this.subscriberInfoIndexes = subscriberInfoIndexes;
        this.strictMethodVerification = strictMethodVerification;
        this.ignoreGeneratedIndex = ignoreGeneratedIndex;
    }

    SubscriberMessage findSubscriberMethods(Class<?> subscriberClass) {
        SubscriberMessage subscriberMethods = METHOD_CACHE.get(subscriberClass);//缓存中获得该类的所有方法
        if (subscriberMethods != null) {
            return subscriberMethods;
        }

        if (ignoreGeneratedIndex) {
            subscriberMethods = findUsingReflection(subscriberClass);
        } else {
            subscriberMethods = findUsingInfo(subscriberClass);
        }
        if (subscriberMethods.isEmpty()) {
            Log.e(EventBus.TAG, subscriberClass.getName() + ":没有发现注解方法");
            return null;
//            throw new EventBusException("Subscriber " + subscriberClass + " and its super classes have no public methods with the @Subscribe annotation");
        } else {
            METHOD_CACHE.put(subscriberClass, subscriberMethods);//将key为订阅者Class,value为subscriberMethods 放入缓存，并返回
            return subscriberMethods;
        }
    }

    private SubscriberMessage findUsingInfo(Class<?> subscriberClass) {
        FindState findState = prepareFindState();

        findState.initForSubscriber(subscriberClass);
        while (findState.clazz != null) {
            findState.subscriberInfo = getSubscriberInfo(findState);
            if (findState.subscriberInfo != null) {
                //从SubscriberInfo中查询
                findState.subscriberMessage.setTag(findState.subscriberInfo.getClassTag());
                SubscriberMethod[] array = findState.subscriberInfo.getSubscriberMethods();
                for (SubscriberMethod subscriberMethod : array) {
                    if (findState.checkAdd(subscriberMethod.getMethod(), subscriberMethod.eventType)) {
                        findState.add(subscriberMethod);
                    }
                }
            } else {//直接反射类的所有注册方法
                findUsingReflectionInSingleClass(findState);
            }
            findState.moveToSuperclass();
        }
        return getMethodsAndRelease(findState);
    }

    private SubscriberMessage getMethodsAndRelease(FindState findState) {
        SubscriberMessage subscriberMethods = new SubscriberMessage();

        subscriberMethods.setTag(findState.subscriberMessage.getTag());
        subscriberMethods.addAll(findState.subscriberMessage.getMethodList());

        findState.recycle();
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                if (FIND_STATE_POOL[i] == null) {
                    FIND_STATE_POOL[i] = findState;
                    break;
                }
            }
        }
        return subscriberMethods;
    }

    private List<SubscriberMethod> getMethodsAndRelease34(FindState findState) {
        List<SubscriberMethod> subscriberMethods = new ArrayList<>(findState.subscriberMessage.getMethodList());
        findState.recycle();//清除以前的数据
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                if (FIND_STATE_POOL[i] == null) {
                    FIND_STATE_POOL[i] = findState;
                    break;
                }
            }
        }
        return subscriberMethods;
    }

    private FindState prepareFindState() {
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                FindState state = FIND_STATE_POOL[i];
                if (state != null) {
                    FIND_STATE_POOL[i] = null;
                    return state;
                }
            }
        }
        //如果没有就返回一个新的
        return new FindState();
    }

    private SubscriberInfo getSubscriberInfo(FindState findState) {
        if (findState.subscriberInfo != null && findState.subscriberInfo.getSuperSubscriberInfo() != null) {
            SubscriberInfo superclassInfo = findState.subscriberInfo.getSuperSubscriberInfo();
            if (findState.clazz == superclassInfo.getSubscriberClass()) {
                return superclassInfo;
            }
        }
        if (subscriberInfoIndexes != null) {
            for (SubscriberInfoIndex index : subscriberInfoIndexes) {
                SubscriberInfo info = index.getSubscriberInfo(findState.clazz);
                if (info != null) {
                    return info;
                }
            }
        }
        return null;
    }

    private SubscriberMessage findUsingReflection(Class<?> subscriberClass) {
        FindState findState = prepareFindState();
        findState.initForSubscriber(subscriberClass);
        while (findState.clazz != null) {
            findUsingReflectionInSingleClass(findState);
            findState.moveToSuperclass();
        }
        return getMethodsAndRelease(findState);
    }


    private void findUsingReflectionInSingleClass(FindState findState) {
        Method[] methods;
        try {
            methods = findState.clazz.getDeclaredMethods();

            SubscribeTag subscribeTag = findState.clazz.getAnnotation(SubscribeTag.class);
            if (subscribeTag != null) {
                String tag = subscribeTag.tag();
                findState.subscriberMessage.setTag(tag);
            }
        } catch (Throwable th) {
            methods = findState.clazz.getMethods();
            findState.skipSuperClasses = true;
        }
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                //获得方法的参数个数
                Class<?>[] parameterTypes = method.getParameterTypes();
                Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                SubscribeMainThread mainSubscribeAnnotation = method.getAnnotation(SubscribeMainThread.class);
                SubscribeRunOnlyTop runOnlyTop = method.getAnnotation(SubscribeRunOnlyTop.class);
                SubscribeSimple simpleAnnotation = method.getAnnotation(SubscribeSimple.class);

                boolean isTrue = (subscribeAnnotation == null) && (mainSubscribeAnnotation != null || runOnlyTop != null || simpleAnnotation != null);
                //只有mainThread,runOnlyTop注解永许没有参数
                if (parameterTypes.length == 0 && isTrue) {
                    findCustomPrarmMethod(method, findState, EmptyEventBusType.class);
                } else if (parameterTypes.length == 1) {
                    if (subscribeAnnotation != null) {
                        Class<?> eventType = parameterTypes[0];
                        Class<?> tempEventType = eventBusHashMap.get(eventType.getName());
                        if (tempEventType != null) {
                            eventType = tempEventType;
                        }
                        if (findState.checkAdd(method, eventType)) {
                            ThreadMode threadMode = subscribeAnnotation.threadMode();
                            findState.add(new SubscriberMethod(method, eventType, threadMode, subscribeAnnotation));
                        }
                    } else if (mainSubscribeAnnotation != null) {
                        findMainThreadParamMethod(findState, method, parameterTypes[0], mainSubscribeAnnotation);
                    } else if (runOnlyTop != null) {
                        findSubscribeRunOnlyTopMethod(findState, method, parameterTypes[0], runOnlyTop);
                    } else if (simpleAnnotation != null) {
                        findHitParamMethod(findState, method, parameterTypes[0], simpleAnnotation);
                    }
                } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
                    String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                    throw new EventBusException("@Subscribe method " + methodName + "must have exactly 1 parameter but has " + parameterTypes.length);
                }
            } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
                String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                throw new EventBusException(methodName + " is a illegal @Subscribe method: must be public, non-static, and non-abstract");
            }
        }
    }

    private void findCustomPrarmMethod(Method method, FindState findState, Class<?> parameterType) {
        SubscribeMainThread mainSubscribeAnnotation = method.getAnnotation(SubscribeMainThread.class);
        SubscribeRunOnlyTop runOnlyTop = method.getAnnotation(SubscribeRunOnlyTop.class);
        SubscribeSimple subscribeSimple = method.getAnnotation(SubscribeSimple.class);
        if (mainSubscribeAnnotation != null) {
            findMainThreadParamMethod(findState, method, parameterType, mainSubscribeAnnotation);
        } else if (runOnlyTop != null) {
            findSubscribeRunOnlyTopMethod(findState, method, parameterType, runOnlyTop);
        } else if (subscribeSimple != null) {
            findHitParamMethod(findState, method, parameterType, subscribeSimple);
        }
    }

    private void findHitParamMethod(FindState findState, Method method, Class<?> parameterType, SubscribeSimple subscribeSimple) {
        Class<?> eventType = parameterType;

        Class<?> tempEventType = eventBusHashMap.get(eventType.getName());
        if (tempEventType != null) {
            eventType = tempEventType;
        }

        if (findState.checkAdd(method, eventType)) {
            findState.add(new SubscriberMethod(method, eventType, subscribeSimple));
        }
    }

    private void findMainThreadParamMethod(FindState findState, Method method, Class<?> parameterType, SubscribeMainThread subscribeAnnotation) {
        Class<?> eventType = parameterType;

        Class<?> tempEventType = eventBusHashMap.get(eventType.getName());
        if (tempEventType != null) {
            eventType = tempEventType;
        }

        if (findState.checkAdd(method, eventType)) {
            findState.add(new SubscriberMethod(method, eventType, subscribeAnnotation));
        }
    }

    private void findSubscribeRunOnlyTopMethod(FindState findState, Method method, Class<?> parameterType, SubscribeRunOnlyTop subscribeAnnotation) {
        Class<?> eventType = parameterType;

        Class<?> tempEventType = eventBusHashMap.get(eventType.getName());
        if (tempEventType != null) {
            eventType = tempEventType;
        }

        if (findState.checkAdd(method, eventType)) {
            findState.add(new SubscriberMethod(method, eventType, subscribeAnnotation));
        }
    }

    static void clearCaches() {
        METHOD_CACHE.clear();
    }

    static class FindState {
        //        final List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        private SubscriberMessage subscriberMessage = new SubscriberMessage();
        final Map<Class, Object> anyMethodByEventType = new HashMap<>();
        final Map<String, Class> subscriberClassByMethodKey = new HashMap<>();
        final StringBuilder methodKeyBuilder = new StringBuilder(128);

        Class<?> subscriberClass;
        Class<?> clazz;
        boolean skipSuperClasses;
        SubscriberInfo subscriberInfo;

        void initForSubscriber(Class<?> subscriberClass) {
            this.subscriberClass = clazz = subscriberClass;
            skipSuperClasses = false;
            subscriberInfo = null;
        }

        void recycle() {
            subscriberMessage.clear();

            anyMethodByEventType.clear();
            subscriberClassByMethodKey.clear();
            methodKeyBuilder.setLength(0);
            subscriberClass = null;
            clazz = null;
            skipSuperClasses = false;
            subscriberInfo = null;
        }

        public void add(SubscriberMethod subscriberMethod) {
            subscriberMessage.add(subscriberMethod);
        }

        boolean checkAdd(Method method, Class<?> eventType) {
            // 2 level check: 1st level with event type only (fast), 2nd level with complete signature when required.
            // Usually a subscriber doesn't have methods listening to the same event type.
            Object existing = anyMethodByEventType.put(eventType, method);//将参数和方法保存在map中
            if (existing == null) {
                return true;
            } else {
                if (existing instanceof Method) {
                    if (!checkAddWithMethodSignature((Method) existing, eventType)) {
                        // Paranoia check
                        throw new IllegalStateException();
                    }
                    // Put any non-Method object to "consume" the existing Method
                    anyMethodByEventType.put(eventType, this);
                }
                return checkAddWithMethodSignature(method, eventType);
            }
        }

        private boolean checkAddWithMethodSignature(Method method, Class<?> eventType) {
            methodKeyBuilder.setLength(0);
            methodKeyBuilder.append(method.getName());
            methodKeyBuilder.append('>').append(eventType.getName());

            String methodKey = methodKeyBuilder.toString();
            Class<?> methodClass = method.getDeclaringClass();
            Class<?> methodClassOld = subscriberClassByMethodKey.put(methodKey, methodClass);
            if (methodClassOld == null || methodClassOld.isAssignableFrom(methodClass)) {
                // Only add if not already found in a sub class
                return true;
            } else {
                // Revert the put, old class is further down the class hierarchy
                subscriberClassByMethodKey.put(methodKey, methodClassOld);
                return false;
            }
        }

        void moveToSuperclass() {
            if (skipSuperClasses) {
                clazz = null;
            } else {
                clazz = clazz.getSuperclass();
                String clazzName = clazz.getName();
                /** Skip system classes, this just degrades performance. */
                if (clazzName.startsWith("java.") || clazzName.startsWith("javax.") || clazzName.startsWith("android.")) {
                    clazz = null;
                }
            }
        }
    }

}
