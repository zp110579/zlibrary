package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.SubscriberMethod;

import java.util.ArrayList;
import java.util.List;

public class SubscriberMessage {
    String tag = "";
    List<SubscriberMethod> mMethodList = new ArrayList<SubscriberMethod>();


    public void setMethodList(List<SubscriberMethod> methodList) {
        this.mMethodList = methodList;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public List<SubscriberMethod> getMethodList() {
        return mMethodList;
    }

    public void clear() {
        mMethodList.clear();
        tag = "";
    }

    public void add(SubscriberMethod subscriberMethod) {
        mMethodList.add(subscriberMethod);
    }

    public boolean isEmpty() {
        return mMethodList.isEmpty();
    }

    public void addAll(List<SubscriberMethod> methodList) {
        mMethodList.addAll(methodList);
    }
}
