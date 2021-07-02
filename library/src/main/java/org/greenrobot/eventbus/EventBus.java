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

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.zee.utils.ZListUtils;
import com.zee.manager.RegisterManager;

import org.greenrobot.eventbus.listener.EventBusPostListener;
import org.greenrobot.eventbus.listener.EventBusUnregisterListener;
import org.greenrobot.eventbus.meta.EmptyEventBusType;
import org.greenrobot.eventbus.meta.SubscriberMessage;
import org.greenrobot.eventbus.util.EvenBusLifeAutomationManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * EventBus is a central publish/subscribe event system for Android. Events are posted ({@link #post(Object)}) to the
 * bus, which delivers it to subscribers that have a matching handler method for the event type. To receive events,
 * subscribers must register themselves to the bus using {@link #register(Object)}. Once registered, subscribers
 * receive events until {@link #unregister(Object)} is called. Event handling methods must be annotated by
 * {@link Subscribe}, must be public, return nothing (void), and have exactly one parameter
 * (the event).
 *
 * @author Markus Junginger, greenrobot
 */
public class EventBus {

    /**
     * Log keyWords, apps may override it.
     */
    public static String TAG = "EventBus";
    static volatile EventBus defaultInstance;
    private static final EventBusBuilder DEFAULT_BUILDER = new EventBusBuilder();
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap<>();

    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final Map<Object, List<Class<?>>> typesBySubscriber;


    private final Map<String, SubscribeType> stickyEvents;
    /**
     * record  class Type of key with sticky
     */
    private final Map<String, Class<?>> stickykeyClassType;

    private final ThreadLocal<PostingThreadState> currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
        @Override
        protected PostingThreadState initialValue() {
            return new PostingThreadState();
        }
    };

    private final HandlerPoster mainThreadPoster;
    private final BackgroundPoster backgroundPoster;
    private final AsyncPoster asyncPoster;
    private final SubscriberMethodFinder subscriberMethodFinder;
    private final ExecutorService executorService;

    private final boolean throwSubscriberException;
    private final boolean logSubscriberExceptions;
    private final boolean logNoSubscriberMessages;
    private final boolean sendSubscriberExceptionEvent;
    private final boolean sendNoSubscriberEvent;
    private final boolean eventInheritance;
    private final EmptyEventBusType mEmptyEventBusType = new EmptyEventBusType();

    private final int indexCount;
    /**
     * method keyWords
     */
    public static final String METHODTAGDEFAULT = "";

    /**
     * subscriber keyWords
     */
    public static final String MSUBSCRIBERTAGDefault = "";
    private EventBusBuilder mEventBusBuilder;
    private final Map<Object, String> subscriberMeTag;
    private final Map<String, List<Object>> typesBySubscriberTag;
    private ArrayList<Object> allSubscribers = new ArrayList<>();

    /**
     * Convenience singleton for apps using a process-wide EventBus instance.
     */
    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }


    public static EventBusBuilder builder() {
        return new EventBusBuilder();
    }

    public EventBusBuilder getEventBusBuilder() {
        return mEventBusBuilder;
    }

    /**
     * For unit test primarily.
     */
    public static void clearCaches() {
        SubscriberMethodFinder.clearCaches();
        eventTypesCache.clear();
    }

    /**
     * Creates a new EventBus instance; each instance is a separate scope in which events are delivered. To use a
     * central bus, consider {@link #getDefault()}.
     */
    public EventBus() {
        this(DEFAULT_BUILDER);
    }

    EventBus(EventBusBuilder builder) {
        mEventBusBuilder = builder;
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
        typesBySubscriberTag = new HashMap<>();
        subscriberMeTag = new HashMap<>();
        stickyEvents = new ConcurrentHashMap<>();
        stickykeyClassType = new ConcurrentHashMap<>();
        mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
        backgroundPoster = new BackgroundPoster(this);
        asyncPoster = new AsyncPoster(this);
        indexCount = builder.subscriberInfoIndexes != null ? builder.subscriberInfoIndexes.size() : 0;
        subscriberMethodFinder = new SubscriberMethodFinder(builder.subscriberInfoIndexes, builder.strictMethodVerification, builder.ignoreGeneratedIndex);
        logSubscriberExceptions = builder.logSubscriberExceptions;
        logNoSubscriberMessages = builder.logNoSubscriberMessages;
        sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        throwSubscriberException = builder.throwSubscriberException;
        eventInheritance = builder.eventInheritance;
        executorService = builder.executorService;
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link #unregister(Object)} once they
     * are no longer interested in receiving events.
     * <p>
     * Subscribers have event handling methods that must be annotated by {@link Subscribe}.
     * The {@link Subscribe} annotation also allows configuration like {@link
     * ThreadMode} and priority.
     */
    public void register(Object subscriber) {
        register(subscriber, "");
    }

    public void register(Object subscriber, Object subscriberTag) {
        Class<?> subscriberClass = subscriber.getClass();

        EvenBusLifeAutomationManager.isAutomationManager(subscriber);

        SubscriberMessage subscriberMessage = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
        if (subscriberMessage == null) {//如果没有发现注解方法，那就放弃
            return;
        }

        List<SubscriberMethod> subscriberMethods = subscriberMessage.getMethodList();

        if (!allSubscribers.contains(subscriber)) {
            allSubscribers.add(subscriber);

            String tempSubscriberTag = subscriberMessage.getTag();
            if (!TextUtils.isEmpty(tempSubscriberTag)) {
                List<Object> objectList = typesBySubscriberTag.get(tempSubscriberTag);
                if (objectList == null) {
                    objectList = new ArrayList<>();
                    typesBySubscriberTag.put(tempSubscriberTag, objectList);
                }
                objectList.add(subscriber);
                subscriberMeTag.put(subscriber, tempSubscriberTag);
            }
        }
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                subscribe(subscriber, subscriberMethod, subscriberTag);
            }
        }
        RegisterManager.register(subscriber);
    }


    // Must be called in synchronized block
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod, Object subscriberTag) {
        Class<?> eventType = subscriberMethod.eventType;

        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod, subscriberTag);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            if (subscriptions.contains(newSubscription)) {
                throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event " + eventType);
            }
        }

        int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size || subscriberMethod.priority > subscriptions.get(i).subscriberMethod.priority || subscriberMethod.mlifo) {
                subscriptions.add(i, newSubscription);
                break;
            }
        }
        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);
        if (subscriberMethod.sticky) {
            if (eventInheritance) {
                Set<Map.Entry<String, SubscribeType>> entries = stickyEvents.entrySet();
                for (Map.Entry<String, SubscribeType> entry : entries) {
                    Class<?> candidateEventType = stickykeyClassType.get(entry.getKey());

                    if (eventType.isAssignableFrom(candidateEventType)) {
                        SubscribeType stickyEvent = entry.getValue();
                        checkPostStickyEventToSubscription(newSubscription, stickyEvent);
                    }
                }
            } else {
                SubscribeType stickyEvent = stickyEvents.get(eventType);
                checkPostStickyEventToSubscription(newSubscription, stickyEvent);
            }
        }
    }

    private void checkPostStickyEventToSubscription(Subscription newSubscription, SubscribeType stickyEvent) {
        if (stickyEvent != null && newSubscription.subscriberTag == stickyEvent.getSubscriberTag() && newSubscription.subscriberMethod.equalTag(stickyEvent.getMethodTag())) {
            postToSubscription(newSubscription, stickyEvent, Looper.getMainLooper() == Looper.myLooper());
        }
    }

    public synchronized boolean isRegistered(Object subscriber) {
        return typesBySubscriber.containsKey(subscriber);
    }

    /**
     * Only updates subscriptionsByEventType, not typesBySubscriber! Caller must update typesBySubscriber.
     */
    private void unSubscribeByEventType(Object subscriber, Class<?> eventType) {
        List<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);

                if (subscription.subscriber == subscriber) {
                    subscription.active = false;
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    public synchronized void unregister(Object subscriber) {
        unregister(subscriber, false);
    }


    /**
     * Unregisters the given subscriber from all event classes.
     */
    private synchronized void unregister(Object subscriber, boolean isTag) {
        List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);
        boolean isRemove = allSubscribers.remove(subscriber);
        if (isRemove) {
            if (subscribedTypes != null) {
                for (Class<?> eventType : subscribedTypes) {
                    unSubscribeByEventType(subscriber, eventType);
                }
                RegisterManager.unregister(subscriber);
                if (subscriber != null) {
                    if (subscriber instanceof EventBusUnregisterListener) {
                        EventBusUnregisterListener listener = (EventBusUnregisterListener) subscriber;
                        listener.onEventBusUnRegister();
                    }
                }
            } else {
                Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
            }

            typesBySubscriber.remove(subscriber);

            String subscriberTag = subscriberMeTag.remove(subscriber);
            if (!isTag) {
                if (!TextUtils.isEmpty(subscriberTag)) {
                    List<Object> objectList = typesBySubscriberTag.get(subscriberTag);
                    objectList.remove(subscriber);
                    if (objectList.size() < 1) {
                        typesBySubscriberTag.remove(subscriberTag);
                    }
                }
            }

//            if (ZLog.DEBUG) {
//                Log.i(TAG, "subscriber to unregister:" + subscriber.getClass());
//                printCurAllSubscribers();
//            }
        }
    }

    /**
     * Unregisters the given subscriber by keyWords
     */
    public synchronized void unregisterWithSubscriberTag(String subscriberTag) {
        if (!TextUtils.isEmpty(subscriberTag)) {
            List<Object> objectList = typesBySubscriberTag.get(subscriberTag);
            if (ZListUtils.isNoEmpty(objectList)) {
                for (int i = 0; i < objectList.size(); i++) {
                    unregister(objectList.get(i), true);

                }
                typesBySubscriberTag.remove(subscriberTag);
            }
        }
    }

    /**
     * print all subscribers
     */
    public void printCurAllSubscribers() {
        Log.d(TAG, "EvenBus 内存中注册对象(开始)------");
        for (int i = 0; i < allSubscribers.size(); i++) {
            Object object = allSubscribers.get(i);
            Log.d(TAG, i + "--->" + object.toString());
        }
        Log.d(TAG, "EvenBus 内存中注册对象(结束)------");
    }

    public ArrayList<Object> getAllSubscribers() {
        return allSubscribers;
    }

    public void post(Object event) {
        post(event, METHODTAGDEFAULT);
    }

    public void post(Object event, String methodTag) {
        post(event, methodTag, MSUBSCRIBERTAGDefault);
    }

    public void post(Object event, String methodTag, EventBusPostListener paListener) {
        post(event, methodTag, MSUBSCRIBERTAGDefault, paListener);
    }

    public void post(Object event, String methodTag, String subscribeTag) {
        post(event, methodTag, subscribeTag, null);
    }

    public void post(Object event, String methodTag, String subscribeTag, EventBusPostListener paListener) {
        SubscribeType subscribeType = new SubscribeType(event, methodTag, subscribeTag, paListener);
        PostingThreadState postingState = currentPostingThreadState.get();
        List<SubscribeType> eventQueue = postingState.eventQueue;
        eventQueue.add(subscribeType);

        if (!postingState.isPosting) {
            postingState.isMainThread = Looper.getMainLooper() == Looper.myLooper();
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            try {
                while (!eventQueue.isEmpty()) {
                    postSingleEvent(eventQueue.remove(0), postingState);
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
        subscribeType.getSubScribeAdapter().onPostEnd();
    }

    /**
     * 直接前往无参数的方法
     *
     * @param methodTag
     */
    public void postTagNoParam(String methodTag) {
        post(mEmptyEventBusType, methodTag);
    }

    public void postTagNoParam(String... methodTagList) {
        for (String methodTag : methodTagList) {
            post(mEmptyEventBusType, methodTag);
        }
    }

    public void postTagNoParam(String methodTag, String subscribeTag) {
        post(mEmptyEventBusType, methodTag, subscribeTag);
    }

    /**
     * 直接前往无参数的方法
     *
     * @param methodTag
     */
    public void postTagNoParam(String methodTag, EventBusPostListener paListener) {
        post(mEmptyEventBusType, methodTag, paListener);
    }

//    public void post(int event, int keyWords) {
//        post(event,keyWords);
//    }

    /**
     * Called from a subscriber's event handling method, further event delivery will be canceled. Subsequent
     * subscribers
     * won't receive the event. Events are usually canceled by higher priority subscribers
     */
    public void cancelEventDelivery(Object event) {
        PostingThreadState postingState = currentPostingThreadState.get();
        if (!postingState.isPosting) {
            throw new EventBusException("This method may only be called from inside event handling methods on the posting thread");
        } else if (event == null) {
            throw new EventBusException("Event may not be null");
        } else if (postingState.event != event) {
            throw new EventBusException("Only the currently handled event may be aborted");
        } else if (postingState.subscription.subscriberMethod.threadMode != ThreadMode.POSTING) {
            throw new EventBusException(" event handlers may only abort the incoming event");
        }

        postingState.canceled = true;
    }

    /**
     * Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky
     * event of an event's type is kept in memory for future access by subscribers using {@link Subscribe#sticky()}.
     */
    public void postSticky(Object event) {
        // Should be posted after it is putted, in case the subscriber wants to remove immediately
        postSticky(event, METHODTAGDEFAULT);
    }


    /**
     * Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky
     * event of an event's type is kept in memory for future access by subscribers using {@link Subscribe#sticky()}.
     */
    public void postSticky(Object event, String methodTag) {
        synchronized (stickyEvents) {
            Class<?> tagClass = event.getClass();
            String tag = tagClass.toString() + methodTag;
            stickykeyClassType.put(tag, tagClass);
            stickyEvents.put(tag, new SubscribeType(event, methodTag, MSUBSCRIBERTAGDefault, null));
        }
        // Should be posted after it is putted, in case the subscriber wants to remove immediately
        post(event, methodTag);
    }

    /**
     * Gets the most recent sticky event for the given type.
     *
     * @see #postSticky(Object)
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (stickyEvents) {
            return eventType.cast(stickyEvents.get(eventType));
        }
    }

    /**
     * Remove and gets the recent sticky event for the given event type.
     *
     * @see #postSticky(Object)
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEvents) {
            return eventType.cast(stickyEvents.remove(eventType));
        }
    }

    /**
     * Removes the sticky event if it equals to the given event.
     *
     * @return true if the events matched and the sticky event was removed.
     */
    public boolean removeStickyEvent(Object event) {
        synchronized (stickyEvents) {
            Class<?> eventType = event.getClass();
            Object existingEvent = stickyEvents.get(eventType);
            if (event.equals(existingEvent)) {
                stickykeyClassType.remove(eventType);
                stickyEvents.remove(eventType);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Removes all sticky events.
     */
    public void removeAllStickyEvents() {
        synchronized (stickyEvents) {
            stickyEvents.clear();
            stickykeyClassType.clear();
        }
    }

    public boolean hasSubscriberForEvent(Class<?> eventClass) {
        List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
        if (eventTypes != null) {
            int countTypes = eventTypes.size();
            for (int h = 0; h < countTypes; h++) {
                Class<?> clazz = eventTypes.get(h);
                CopyOnWriteArrayList<Subscription> subscriptions;
                synchronized (this) {
                    subscriptions = subscriptionsByEventType.get(clazz);
                }
                if (subscriptions != null && !subscriptions.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void postSingleEvent(SubscribeType event, PostingThreadState postingState) throws Error {

        Class<?> eventClass = event.getEventObject().getClass();
        boolean subscriptionFound = false;
        if (eventInheritance) {
            List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
            int countTypes = eventTypes.size();
            for (int h = 0; h < countTypes; h++) {
                Class<?> clazz = eventTypes.get(h);
                subscriptionFound |= postSingleEventForEventType(event, postingState, clazz);
            }
        } else {
            subscriptionFound = postSingleEventForEventType(event, postingState, eventClass);
        }
        if (!subscriptionFound) {
            SubScribeAdapter loSubScribeAdapter = event.getSubScribeAdapter();
            loSubScribeAdapter.onPostNoFound();

            if (logNoSubscriberMessages) {
                String info = "Post Error for event " + event;
                Log.e(TAG, info);
            }
            if (sendNoSubscriberEvent && eventClass != NoSubscriberEvent.class && eventClass != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, event));
            }
        }
    }

    private boolean postSingleEventForEventType(SubscribeType event, PostingThreadState postingState, Class<?> eventClass) {
        String eventPostTag = event.getMethodTag();
        CopyOnWriteArrayList<Subscription> subscriptions;
        CopyOnWriteArrayList<Subscription> list = new CopyOnWriteArrayList<>();
        synchronized (this) {
            subscriptions = subscriptionsByEventType.get(eventClass);
        }

        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (int i = 0; i < subscriptions.size(); i++) {
                Subscription subscription = subscriptions.get(i);
                //比较ClassTag是否一样
                SubscriberMethod subscriberMethod = subscription.getSubscriberMethod();
                boolean isTrue = subscription.subscriberTag.equals(event.getSubscriberTag()) || subscriberMethod.ignoredSubscriberTag;
                if (isTrue && subscriberMethod.equalTag(eventPostTag)) {
                    list.add(subscription);
                    if (subscriberMethod.isFinish) {
                        break;
                    }
                }
            }
        }

        if (!list.isEmpty()) {
            for (Subscription subscription : list) {
                postingState.event = event;
                postingState.subscription = subscription;
                boolean aborted = false;
                try {
                    postToSubscription(subscription, event, postingState.isMainThread);
                    aborted = postingState.canceled;
                } finally {
                    postingState.event = null;
                    postingState.subscription = null;
                    postingState.canceled = false;
                }
                if (aborted) {
                    break;
                }
            }
            return true;
        }

        return false;
    }

    private void postToSubscription(Subscription subscription, SubscribeType event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING://同一个线程
                invokeSubscriber(subscription, event);
                break;
            case MAIN://主线程
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case BACKGROUND://如果在子线程那就在当前直接执行，如果在主线程，那就开启一个线程来执行
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case ASYNC://无论怎么弄，都开启一个新的线程来执行
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    /**
     * Looks up all Class objects including super classes and interfaces. Should also work for interfaces.
     */
    private static List<Class<?>> lookupAllEventTypes(Class<?> eventClass) {
        synchronized (eventTypesCache) {
            List<Class<?>> eventTypes = eventTypesCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList<>();
                Class<?> clazz = eventClass;
                while (clazz != null) {
                    eventTypes.add(clazz);
                    addInterfaces(eventTypes, clazz.getInterfaces());
                    clazz = clazz.getSuperclass();
                }
                eventTypesCache.put(eventClass, eventTypes);
            }
            return eventTypes;
        }
    }

    /**
     * Recurses through super interfaces.
     */
    static void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces) {
        for (Class<?> interfaceClass : interfaces) {
            if (!eventTypes.contains(interfaceClass)) {
                eventTypes.add(interfaceClass);
                addInterfaces(eventTypes, interfaceClass.getInterfaces());
            }
        }
    }

    /**
     * Invokes the subscriber if the subscriptions is still active. Skipping subscriptions prevents race conditions
     * between {@link #unregister(Object)} and event delivery. Otherwise the event might be delivered after the
     * subscriber unregistered. This is particularly important for main thread delivery and registrations bound to the
     * live cycle of an Activity or Fragment.
     */
    void invokeSubscriber(PendingPost pendingPost) {
        SubscribeType event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            invokeSubscriber(subscription, event);
        }
    }

    void invokeSubscriber(Subscription subscription, SubscribeType event) {
        try {

            if (event.getEventObject() instanceof EmptyEventBusType) {//没有参数类型的反馈
                subscription.subscriberMethod.invokeNoPararm(subscription);
            } else {
                subscription.subscriberMethod.invoke(subscription, event);
            }
        } catch (InvocationTargetException e) {
            event.getSubScribeAdapter().onPostError(e);
            handleSubscriberException(subscription, event, e.getCause());
        } catch (IllegalAccessException e) {
            event.getSubScribeAdapter().onPostError(e);
            throw new IllegalStateException("Unexpected exception", e);
        }
    }

    private void handleSubscriberException(Subscription subscription, Object event, Throwable cause) {
        if (event instanceof SubscriberExceptionEvent) {
            if (logSubscriberExceptions) {
                // Don't send another SubscriberExceptionEvent to avoid infinite event recursion, just log
                Log.e(TAG, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass() + " threw an exception", cause);
                SubscriberExceptionEvent exEvent = (SubscriberExceptionEvent) event;
                Log.e(TAG, "Initial event " + exEvent.causingEvent + " caused exception in " + exEvent.causingSubscriber, exEvent.throwable);
            }
        } else {
            if (throwSubscriberException) {
                throw new EventBusException("Invoking subscriber failed", cause);
            }
            if (logSubscriberExceptions) {
                Log.e(TAG, "Could not dispatch event: 【" + event.getClass() + "】 to subscribing class 【" + subscription.subscriber.getClass() + "】", cause);
            }
            if (sendSubscriberExceptionEvent) {
                SubscriberExceptionEvent exEvent = new SubscriberExceptionEvent(this, cause, event, subscription.subscriber);
                post(exEvent);
            }
        }
    }

    /**
     * For ThreadLocal, much faster to set (and get multiple values).
     */
    final static class PostingThreadState {
        final List<SubscribeType> eventQueue = new ArrayList<SubscribeType>();
        boolean isPosting;
        boolean isMainThread;
        Subscription subscription;
        Object event;
        boolean canceled;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    // Just an idea: we could provide a callback to post() to be notified, an alternative would be events, of course...
    /* public */interface PostCallback {
        void onPostCompleted(List<SubscriberExceptionEvent> exceptionEvents);
    }

    @Override
    public String toString() {
        return "EventBus[indexCount=" + indexCount + ", eventInheritance=" + eventInheritance + "]";
    }
}
