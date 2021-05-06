package com.zee.utils;

import com.zee.adapter.EventBusBindCurActivityAdapter;
import com.zee.bean.DelayTag;
import com.zee.bean.EventBusSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.listener.EventBusPostListener;


public class ZEventBusUtils {

    /**
     * 随着Activity的生命周注销而注销
     *
     * @param subscriber
     */
    public static void registerBindCurActivity(Object subscriber) {
        EventBusBindCurActivityAdapter adapter = ZLibrary.getInstance().getEventBusBindCurActivityAdapter();
        adapter.onRegister(subscriber,"");
    }

    /**
     * 随着Activity的生命周注销而注销
     *
     * @param subscriber
     */
    public static void registerBindCurActivity(Object subscriber,Object subscriberTag) {
        EventBusBindCurActivityAdapter adapter = ZLibrary.getInstance().getEventBusBindCurActivityAdapter();
        adapter.onRegister(subscriber,subscriberTag);
    }

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void register(Object subscriber, int subscriberTag) {
        register(subscriber, String.valueOf(subscriberTag));
    }

    public static void register(Object subscriber, Object subscriberTag) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber, subscriberTag);
        }
    }

    public static void postTagNoParam(String methodTag) {
        EventBus.getDefault().postTagNoParam(methodTag);
    }

    public static void postTagNoParam(String... methodTag) {
        EventBus.getDefault().postTagNoParam(methodTag);
    }

    public static void postTagNoParam(String methodTag, EventBusPostListener paListener) {
        EventBus.getDefault().postTagNoParam(methodTag, paListener);
    }

    public static EventBusSubscriber getEventBusSubscriber(String subscriberTag) {//获得
        return new EventBusSubscriber(subscriberTag);
    }

    public static EventBusSubscriber getEventBusSubscriber(int subscriberTag) {//获得
        return new EventBusSubscriber(subscriberTag);
    }

    public static DelayTag delay(long time) {
        return new DelayTag(time);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }

    public static void post(Object object, String methodTag) {
        EventBus.getDefault().post(object, methodTag);
    }

    public static void post(Object object, String methodTag, EventBusPostListener paListener) {
        EventBus.getDefault().post(object, methodTag, paListener);
    }

    public static void postSticky(Object object) {
        EventBus.getDefault().postSticky(object);
    }

    public static void postSticky(Object object, String methodTag) {
        EventBus.getDefault().postSticky(object, methodTag);
    }

    public static void unregisterWithSubscriberTag(String subscriberTag) {
        EventBus.getDefault().unregisterWithSubscriberTag(subscriberTag);
    }

    public static void printCurAllSubscribers() {
        EventBus.getDefault().printCurAllSubscribers();
    }
}
