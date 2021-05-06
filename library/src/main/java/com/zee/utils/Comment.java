package com.zee.utils;

import android.os.Environment;

import java.io.File;

/**
 *
 * @author Administrator
 */

public class Comment {
    public static String AppName;
    public static String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    public static final String line="-------------------------------------------------------\r\n";
    private static final String zLogPath = sdCardPath + "Zlog/";

    public static String getCrashExceptionFile() {//返回崩溃日志
        return zLogPath + AppName + "/crashException.txt";
    }
}
