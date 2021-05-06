package com.zee.adapter;

import android.text.TextUtils;

import com.zee.listener.LetsGoListener;
import com.zee.log.ZLog;

public class LetsGoAdapter implements LetsGoListener {
    final static String TAG = "LetsGoAdapter";


    @Override
    public void onStart() {
        ZLog.i(TAG, "onStart()");
    }

    @Override
    public void onFound(String className) {
        ZLog.i(TAG, "onFound:" + className);
    }

    @Override
    public void onError(String msg) {
        ZLog.i(TAG, "onError()" + msg);
    }

    @Override
    public void onIntercept(String className, String name, String module, String keyWord) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n请求被拦截");
        stringBuilder.append("\n拦截器:" + className);
        if (!TextUtils.isEmpty(name)) {
            stringBuilder.append("\nname" + name);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            stringBuilder.append("\nkeyWord：" + keyWord);
        }

        ZLog.e(TAG, stringBuilder.toString());
    }

    @Override
    public void onEnd() {
        ZLog.i(TAG, "onEnd()");
    }
}
