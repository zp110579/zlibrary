package com.zee.adapter;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.zee.utils.ZEventBusUtils;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * created by zee on 2020/12/22.
 */
public class EventBusBindCurActivityAdapter implements Application.ActivityLifecycleCallbacks {
    private HashMap<String, ArrayList<Object>> mEventBursRegister = new HashMap<>();

    private Activity mActivity;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String activityHashCode = activity.toString();
        ArrayList<Object> list = mEventBursRegister.get(activityHashCode);
        if (ZListUtils.isNoEmpty(list)) {
            //将绑定当前Activity的对象全部注销掉
            for (Object subscriber : list) {
                ZEventBusUtils.unregister(subscriber);
            }
            mEventBursRegister.remove(activityHashCode);
            ZEventBusUtils.printCurAllSubscribers();
        }
    }

    public void onRegister(Object subscriber,Object subscriberTag) {
        ArrayList<Object> list = mEventBursRegister.get(mActivity.toString());
        if (ZListUtils.isEmpty(list)) {
            list = new ArrayList<>();
            mEventBursRegister.put(mActivity.toString(), list);
        }
        ZEventBusUtils.register(subscriber,subscriberTag);
        list.add(subscriber);
    }

}
