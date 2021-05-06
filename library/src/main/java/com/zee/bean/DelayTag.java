package com.zee.bean;

import com.zee.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Administrator
 */
public class DelayTag {
    private long time;

    public DelayTag(long time) {
        this.time = time;
    }

    public EventBusSubscriber getSubscriberTag(String subscriberTag) {
        return new EventBusSubscriber(subscriberTag, time);
    }

    public EventBusSubscriber getSubscriberTag(int subscriberTag) {
        return new EventBusSubscriber(subscriberTag, time);
    }

    public void post(final Object object) {
        if (time < 1) {
            EventBus.getDefault().post(object);
        } else {
            UIUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(object);
                }
            }, time);
        }
    }

    public void post(final Object object, final String methodTag) {
        if (time < 1) {
            EventBus.getDefault().post(object);
        } else {
            UIUtils.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(object, methodTag);
                                    }
                                }
                    , time);
        }
    }

}
