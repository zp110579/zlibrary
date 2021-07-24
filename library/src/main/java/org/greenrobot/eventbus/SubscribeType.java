package org.greenrobot.eventbus;


import android.text.TextUtils;

import org.greenrobot.eventbus.listener.EventBusPostListener;

public class SubscribeType {
    private Object mEventObject;
    private String mMethodTag;
    private String mSubscriberTag;
    private SubScribeAdapter mSubScribeAdapter;


    public SubscribeType(Object object, String methodTag, String subscriberTag, EventBusPostListener paListener) {
        this.mEventObject = object;
        this.mMethodTag = methodTag;
        this.mSubscriberTag = subscriberTag;
        mSubScribeAdapter = new SubScribeAdapter(paListener);
        mSubScribeAdapter.onPostStart();
    }

    public Object getEventObject() {
        return mEventObject;
    }

    public SubScribeAdapter getSubScribeAdapter() {
        return mSubScribeAdapter;
    }

    public String getSubscriberTag() {
        return mSubscriberTag;
    }

    public String getMethodTag() {
        return mMethodTag;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SubscribeType{");
        if (!TextUtils.isEmpty(mMethodTag)) {
            builder.append(", MethodTag='");
            builder.append(mMethodTag);
            builder.append('\'');
        }
        builder.append("EventObject=" + mEventObject);
        if (!TextUtils.isEmpty(mSubscriberTag)) {
            builder.append(", SubscriberTag='");
            builder.append(mSubscriberTag + '\'');
            builder.append('\'');
        }
        builder.append('}');
        return builder.toString();
    }
}
