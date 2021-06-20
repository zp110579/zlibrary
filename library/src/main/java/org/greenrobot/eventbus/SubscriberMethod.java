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

import org.greenrobot.eventbus.meta.SubscriberMethodInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.greenrobot.eventbus.SubscriberMethodFinder.eventBusHashMap;

/**
 * Used internally by EventBus and generated subscriber indexes.
 */
public class SubscriberMethod {
    private final Method method;//方法
    final ThreadMode threadMode;//线程类型
    final Class<?> eventType;//参数类型，只有一个参数，事件类
    final int priority;//优先级，越大越先执行
    final boolean sticky;//是否粘性

    final String tag;//标记
    final boolean isFinish;//是否终止，不在继续往下，和priority配合使用
    final boolean mlifo;
    final boolean ignoredSubscriberTag;//是否忽略SubscriberTag
    final String[] tags;


    /**
     * 比较tag
     *
     * @param postMethodTag
     * @return
     */
    public boolean equalTag(String postMethodTag) {
        if (tags.length > 1) {
            for (String s : tags) {
                if (s.equals(postMethodTag)) {
                    return true;
                }
            }
        }
        return tag.equals(postMethodTag);
    }

    /**
     * Used for efficient comparison
     */
    /**
     * method keyWords
     */
    String methodTag;

    public SubscriberMethod(Method method, Class<?> eventType, ThreadMode threadMode, Subscribe subscribe) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = subscribe.priority();
        this.sticky = subscribe.sticky();
        this.tag = subscribe.tag();
        this.isFinish = false;
        this.mlifo = false;
        this.ignoredSubscriberTag = false;
        this.tags = tag.split(",");
    }

    /**
     * subscribeSimple
     *
     * @param method
     * @param eventType
     * @param subscribeSimple
     */
    public SubscriberMethod(Method method, Class<?> eventType, SubscribeSimple subscribeSimple) {
        this.method = method;
        this.threadMode = ThreadMode.MAIN;
        this.eventType = eventType;
        this.priority = 0;
        this.sticky = false;
        this.tag = subscribeSimple.value();
        this.isFinish = false;
        this.mlifo = false;
        this.ignoredSubscriberTag = false;
        this.tags = tag.split(",");
    }

    /**
     * SubscribeMainThread
     *
     * @param method
     * @param eventType
     * @param mainThread
     */
    public SubscriberMethod(Method method, Class<?> eventType, SubscribeMainThread mainThread) {
        this.method = method;
        this.threadMode = ThreadMode.MAIN;
        this.eventType = eventType;
        this.priority = mainThread.priority();
        this.sticky = mainThread.sticky();
        this.tag = mainThread.tag();
        this.isFinish = mainThread.finish();
        this.mlifo = mainThread.lifo();
        this.ignoredSubscriberTag = mainThread.ignoredSubscriberTag();
        this.tags = tag.split(",");
    }

    /**
     * SubscribeRunOnlyTop
     *
     * @param method
     * @param eventType
     * @param mainThread
     */
    public SubscriberMethod(Method method, Class<?> eventType, SubscribeRunOnlyTop mainThread) {
        this.method = method;
        this.threadMode = mainThread.threadMode();
        this.eventType = eventType;
        this.priority = 0;
        this.sticky = false;
        this.tag = mainThread.tag();
        this.isFinish = true;
        this.mlifo = true;
        this.ignoredSubscriberTag = false;
        this.tags = tag.split(",");
    }

    public SubscriberMethod(Method method, SubscriberMethodInfo methodInfo) {//注解处理器使用
        this.method = method;
        this.threadMode = methodInfo.getThreadMode();
        Class<?> value = eventBusHashMap.get(methodInfo.getEventType().getName());
        this.eventType = value != null ? value : methodInfo.getEventType();
        this.priority = methodInfo.getPriority();
        this.sticky = methodInfo.isSticky();
        this.tag = methodInfo.getTag();
        this.isFinish = methodInfo.isFinish();
        this.mlifo = methodInfo.getMlifo();
        this.ignoredSubscriberTag = methodInfo.isIgnoredClassTag();
        this.tags = tag.split(",");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SubscriberMethod) {
            checkMethodString();
            SubscriberMethod otherSubscriberMethod = (SubscriberMethod) other;
            otherSubscriberMethod.checkMethodString();
            // Don't use method.equals because of http://code.google.com/p/android/issues/detail?id=7811#c6
            return methodTag.equals(otherSubscriberMethod.methodTag);//方法名字和参数类名是不是一样
        } else {
            return false;
        }
    }

    private synchronized void checkMethodString() {
        if (methodTag == null) {
            // Method.toString has more overhead, just take relevant parts of the method
            StringBuilder builder = new StringBuilder(64);

            builder.append(method.getDeclaringClass().getName());//方法所在的类的名字
            builder.append('#').append(method.getName());//方法名字
            builder.append('(').append(eventType.getName());//参数类的名字
            methodTag = builder.toString();
        }
    }


    public void invokeNoPararm(Subscription subscription) throws InvocationTargetException, IllegalAccessException {
        method.invoke(subscription.subscriber);
    }

    public void invoke(Subscription subscription, SubscribeType event) throws InvocationTargetException, IllegalAccessException {
        SubScribeAdapter loEventBusPostListener = event.getSubScribeAdapter();
        if (loEventBusPostListener != null) {
            loEventBusPostListener.onProcess(subscription.subscriber, subscription.subscriberMethod.method);
        }
        subscription.subscriberMethod.method.invoke(subscription.subscriber, event.getEventObject());
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "{" +
                "keyWords='" + tag + '\'' +
                ", " + method.getName() +
                ", threadMode=" + threadMode +
                ", isFinish=" + isFinish +
                ", mlifo=" + mlifo +
                '}';
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}