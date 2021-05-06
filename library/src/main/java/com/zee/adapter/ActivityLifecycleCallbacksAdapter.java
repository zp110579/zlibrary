package com.zee.adapter;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.zee.listener.BackOrLeaveAppListener;
import com.zee.log.ZLog;
import com.zee.utils.ZEventBusUtils;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityLifecycleCallbacksAdapter implements Application.ActivityLifecycleCallbacks {
    int appCount = 0;
    boolean isRunInBackground;

    private BackOrLeaveAppListener mBackOrLeaveAppListener;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        appCount++;
        if (isRunInBackground) {
            //应用从后台回到前台 需要做的操作
            if (mBackOrLeaveAppListener != null) {
                mBackOrLeaveAppListener.back2App(activity);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        appCount--;
        if (appCount == 0) {
            //应用进入后台 需要做的操作
            if (mBackOrLeaveAppListener != null) {
                mBackOrLeaveAppListener.leaveApp(activity);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void setBackOrLeaveAppListener(BackOrLeaveAppListener backOrLeaveAppListener) {
        mBackOrLeaveAppListener = backOrLeaveAppListener;
    }
}
