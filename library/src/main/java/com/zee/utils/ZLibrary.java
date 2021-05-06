package com.zee.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zee.adapter.ActivityLifecycleCallbacksAdapter;
import com.zee.adapter.EventBusBindCurActivityAdapter;
import com.zee.listener.BackOrLeaveAppListener;
import com.zee.log.ZCrashExceptionHandler;
import com.zee.log.ZLog;
import com.zee.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author Administrator
 */
public class ZLibrary {
    private static volatile ZLibrary sInstance;
    private Application mContext;
    private Handler mMainUIHandler = new Handler(Looper.getMainLooper());
    private int screenWith, screenHeight;
    private float density;
    private ZConfig mZConfig;
    private Activity mActivity;
    private boolean isDebug = false;
    private ActivityLifecycleCallbacksAdapter mActivityLifecycleCallbacksAdapter;
    private EventBusBindCurActivityAdapter mEventBusBindCurActivityAdapter;

    public static void init(Application context, boolean isDeBug) {
        if (sInstance == null) {
            synchronized (ZLibrary.class) {
                if (sInstance == null) {
                    sInstance = new ZLibrary(context, isDeBug);
                    ToastUtils.init();
                }
            }
        }
    }

    private ZLibrary(Application context, boolean isDeBug) {
        this.mContext = context;// config.getApplication();
        boolean tempIsDeBug = isApkInDebug(context) && isDeBug;

        mZConfig = new ZConfig(context, tempIsDeBug);
        initApplication(context);
        mMainUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ZConfig.addRegisterData();
            }
            //为什么用100，有些类有可能直接用到了UIUtils类中的Application，这时候ZConfig还没有初始化完,所以延时100毫秒，这时候ZConfig就初始化完了。
        }, 100);
    }

    public ZConfig getZConfig() {
        return mZConfig;
    }

    private void initApplication(Application application) {
//        mZConfig = config;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWith = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        density = displayMetrics.density;

        if (mZConfig.isMeLoop()) {
            mMainUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Looper.loop();
                        } catch (Exception e) {//捕获主线程异常
                            ZCrashExceptionHandler.getInstance().uncaughtException(Looper.myLooper().getThread(), e);
                        }
                    }
                }
            });
        }
        mActivityLifecycleCallbacksAdapter = new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                mActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
                mActivity = activity;
            }
        };

        mEventBusBindCurActivityAdapter = new EventBusBindCurActivityAdapter();
        mContext.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacksAdapter);
        mContext.registerActivityLifecycleCallbacks(mEventBusBindCurActivityAdapter);
    }

    public Activity getCurrentActivity() {
        return mActivity;
    }

    public void setBackOrLeaveAppListener(BackOrLeaveAppListener backOrLeaveAppListener) {
        if (mActivityLifecycleCallbacksAdapter != null) {
            mActivityLifecycleCallbacksAdapter.setBackOrLeaveAppListener(backOrLeaveAppListener);
        }
    }

    public EventBusBindCurActivityAdapter getEventBusBindCurActivityAdapter() {
        return mEventBusBindCurActivityAdapter;
    }

    public static ZLibrary getInstance() {
        return sInstance;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public Application getApplicationContext() {
        return mContext;
    }

    protected Handler getMainUIHandler() {
        return mMainUIHandler;
    }

    protected int getScreenWith() {
        return screenWith;
    }

    protected int getScreenHeight() {
        return screenHeight;
    }

    public float getDensity() {
        return density;
    }

    protected ExecutorService getExecutorService() {
        return EventBus.getDefault().getExecutorService();
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
