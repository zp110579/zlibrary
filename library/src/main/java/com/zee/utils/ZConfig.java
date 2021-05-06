package com.zee.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.lzf.easyfloat.EasyFloat;
import com.zee.annotation.RunApplicationOnCreate;
import com.zee.interf.IRunApplicationOnCreate;
import com.zee.log.ZCrashExceptionHandler;
import com.zee.log.ZLog;
import com.zee.route.PRouterManager;

import org.greenrobot.eventbus.EventBus;

import com.zee.interf.IRoute;

import org.greenrobot.eventbus.interfaces.SubscriberInfoIndex;

import java.util.ArrayList;

public final class ZConfig {
    public static String TAG = "ZConfig";

    public boolean isDebug() {
        return ZLog.DEBUG;//是否打印
    }

    private static Application mApplication;
    //默认打印一层
    private boolean isSaveExceptionLog;//是否保存异常日志
    private boolean isMeLoop;//拦截Loop.Loop()
    private boolean isAddViewServer = false;//可以使用hierarchyviewer
    private static ArrayList<Object> sObjectArrayList = new ArrayList<>();


    public boolean isAddViewServer() {
        return isAddViewServer;
    }

    public boolean isMeLoop() {
        return isMeLoop;
    }

    public boolean isSaveExceptionLog() {
        return isSaveExceptionLog;
    }

    protected ZConfig(Application application, boolean isDeBug) {
        if (mApplication == null && application != null) {
            mApplication = application;
            initThirdLib(application, isDeBug);
            try {
                PackageManager packageManager = mApplication.getApplicationContext().getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mApplication.getPackageName(), 0);
                Comment.AppName = (String) packageManager.getApplicationLabel(applicationInfo);
                if (!TextUtils.isEmpty(Comment.AppName) && Comment.AppName.length() > 2) {
                    TAG = Comment.AppName;
                } else {
                    TAG = Comment.AppName.substring(0, 2);
                }
                ZLog.DEBUG = isDeBug;

                if (isDeBug) {
                    ZCrashExceptionHandler.getInstance().init(mApplication);
                }

                sObjectArrayList.clear();



            } catch (PackageManager.NameNotFoundException e) {
                ZLog.exception(e);
            }
        }
    }


    public static void addRegisterData() {

    }

    private static void register(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object obj = clazz.getConstructor().newInstance();
            if (obj instanceof IRoute) {
                ZLog.i(TAG, "Find 【IRoute】:" + className);
                PRouterManager.addRoteBean((IRoute) obj);
            } else if (obj instanceof SubscriberInfoIndex) {
                ZLog.i(TAG, "Find 【SubscriberInfoIndex】:" + className);
                EventBus.getDefault().getEventBusBuilder().addIndex((SubscriberInfoIndex) obj);
            } else if (obj instanceof IRunApplicationOnCreate) {
                ZLog.i(TAG, "Find 【IRunApplicationOnCreate】:" + className);
                IRunApplicationOnCreate onCreate = (IRunApplicationOnCreate) obj;
                ArrayList<Object> objectList = onCreate.getObjectList();
                sObjectArrayList.addAll(onCreate.getObjectList());

                for (Object object : objectList) {
                    //将所有的对象自动注册
                    ZEventBusUtils.register(object);
                }
            } else {
                ZLog.e("no find type：" + className);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 初始化第三方库
     *
     * @param application
     * @param isDebug
     */
    private void initThirdLib(Application application, boolean isDebug) {
        //悬浮窗初始化
        EasyFloat.init(application, isDebug);
    }

    public ZConfig setTag(String tag) {
        TAG = tag;
        return this;
    }

    //保存崩溃日志并从新启动程序
    public ZConfig saveCrashLogAndRestartApp(Class reStartActivityClass) {
        if (mApplication != null) {
            ZCrashExceptionHandler crashExceptionHandler = ZCrashExceptionHandler.getInstance();
            crashExceptionHandler.setClass(reStartActivityClass);
        }
        return this;
    }

    public ZConfig setDebug(boolean debug) {
        ZLog.DEBUG = debug;
        return this;
    }


    //是否保存异常日志
    public ZConfig saveExceptionLog(boolean isSaveExceptionLog) {
        this.isSaveExceptionLog = isSaveExceptionLog;

        if (mApplication != null && isSaveExceptionLog) {
            ZCrashExceptionHandler.getInstance().init(mApplication);
        }
        return this;
    }
//
//        //EventBus注册类型
//        public Builder addDefaultEventBusIndex(SubscriberInfoIndex subscriberInfoIndex) {
//            EventBus.builder().addIndex(subscriberInfoIndex).installDefaultEventBus();
//            return this;
//        }

    //拦截Loop.loop
    public ZConfig setUseMeLoop(boolean isMeLoop) {
        this.isMeLoop = isMeLoop;
        return this;
    }

    public ZConfig setViewServer(boolean isAddViewServer) {
        this.isAddViewServer = isAddViewServer;
        return this;
    }

    public String getTag() {
        return TAG;
    }

//    }

}
