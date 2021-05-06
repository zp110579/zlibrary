package com.zee.log;

import com.zee.utils.Comment;
import com.zee.utils.ZConfig;

/**
 * Created by Administrator on 2017-04-19.
 */

public final class ZLog {
    protected static ZLogEngine sLogEngine = new ZLogEngine();

    public static boolean DEBUG = false;

//    public static void setConfig(ZConfig config) {
//        sLogEngine.setConfiguration(config);
//    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void printThis() {
        sLogEngine.printThis();
    }

    public static void json(String json) {
        sLogEngine.json(json);
    }

    public static void v(Object message) {
        sLogEngine.v(message);
    }

    public static void v(String tag, Object message) {
        sLogEngine.v(tag, message);
    }


    public static void w(Object message) {
        sLogEngine.w(message);
    }

    public static void w(String tag, Object message) {
        sLogEngine.w(tag, message);
    }


    public static void i(Object message) {
        sLogEngine.i(message);
    }

    public static void i(Object message, boolean isShowStack) {
        sLogEngine.i(message, isShowStack);
    }


    public static void i(String tag, String message) {
        sLogEngine.i(tag, message);
    }


    public static void d(Object message) {
        sLogEngine.d(message);
    }


    public static void d(String tag, Object message) {
        sLogEngine.d(tag, message);
    }

    public static void e(Object message) {
        sLogEngine.e(message);
    }

    public static void e(Object message, boolean isShowStack) {
        sLogEngine.e(message, isShowStack);
    }

    public static void e(String tag, Object message) {
        sLogEngine.e(tag, message);
    }

    public static void exception(Exception e) {
        sLogEngine.exception(e, null);
    }

    public static void exception(Exception e, String message) {
        sLogEngine.exception(e, message);
    }


    //获得崩溃日志
    public static String getCrashExceptionFilePath() {
        return Comment.getCrashExceptionFile();
    }

    public static ZLogEngine getLogEngine() {
        return sLogEngine;
    }
}
