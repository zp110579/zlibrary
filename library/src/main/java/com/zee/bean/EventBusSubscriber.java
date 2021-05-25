package com.zee.bean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.listener.EventBusPostListener;

/**
 * @author Zee
 */
public class EventBusSubscriber {
    private String mClassTag;
    private MethodTag mMethodTag;
    private long mDelayTime;

    public EventBusSubscriber(String object) {
        this.mClassTag = object;
    }

    public EventBusSubscriber(int subscribeTag) {
        this(String.valueOf(subscribeTag));
    }

    public EventBusSubscriber(String subscribeTag, long delayTime) {
        this.mClassTag = subscribeTag;
        this.mDelayTime = delayTime;
    }

    public EventBusSubscriber(int subscribeTag, long delayTime) {
        this(String.valueOf(subscribeTag), delayTime);
    }

    public MethodTag getMethodTag(String methodTag) {
        mMethodTag = new MethodTag(mClassTag, methodTag);
        return mMethodTag;
    }

    public void post(Object object) {
        post(object, EventBus.METHODTAGDEFAULT);
    }

    public void post(Object object, String methodTag) {
        EventBus.getDefault().post(object, methodTag, mClassTag);
    }

    public void post(Object object, String methodTag, EventBusPostListener paListener) {
        EventBus.getDefault().post(object, methodTag, mClassTag, paListener);
    }

    public void postTagNoParam(String tag) {
        EventBus.getDefault().postTagNoParam(tag,mClassTag);
    }
}
