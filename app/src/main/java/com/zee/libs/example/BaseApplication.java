package com.zee.libs.example;

import android.app.Application;

import com.zee.utils.ZLibrary;

/**
 * created by zee on 2021/6/6.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZLibrary.init(this,true);
    }
}
