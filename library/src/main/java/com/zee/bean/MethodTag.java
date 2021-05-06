package com.zee.bean;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/3/2 0002.
 */

public class MethodTag {
    private String mMethodTag = "";
    private String mSubscriberTag;

    public MethodTag(String subscriberTag, String methodTag) {
        mMethodTag = methodTag;
        this.mSubscriberTag = subscriberTag;
    }

    public String getMethodTag() {
        return mMethodTag;
    }

    public void post(Object object) {
        EventBus.getDefault().post(object, mMethodTag, mSubscriberTag);
    }

}
