package com.zee.log;

import android.os.Build;

import com.zee.libs.BuildConfig;
import com.zee.utils.Comment;
import com.zee.utils.FileUtil;
import com.zee.utils.UIUtils;
import com.zee.utils.ZSystemInfoUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本地异常处理类
 */
class ExceptionFileUtil {

    public static String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }

    public static void dealNativeWithException(final String detailMessage) {
        UIUtils.runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                writeException(detailMessage);
            }
        });
    }

    public static void dealNativeWithException(final Exception e, final String message) {
        UIUtils.runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder builder = new StringBuilder();
                if (message != null && message.length() > 0) {
                    builder.append(message);
                    builder.append("\n");
                }
                builder.append(getErrorInfo(e));
                writeException(builder.toString());
            }
        });
    }

    public static void writeException(String content) {
        try {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(Comment.line);
            stringBuffer.append(getBaseInfor());
            stringBuffer.append(content);
            FileUtil.writeFileAdd(Comment.getCrashExceptionFile(), stringBuffer.toString());
        } catch (ZException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseInfor() {
        StringBuilder stringBuffer = new StringBuilder();
        String appVersion = ZSystemInfoUtils.getVersionName();//版本名
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        stringBuffer.append(dataFormat.format(new Date()));
        stringBuffer.append("[");
        stringBuffer.append(appVersion);
        stringBuffer.append("][");
        stringBuffer.append(Build.VERSION.RELEASE);
        stringBuffer.append("][");
        stringBuffer.append(android.os.Build.BRAND);
        stringBuffer.append("][");
        stringBuffer.append(android.os.Build.DISPLAY);
        stringBuffer.append("]");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();

    }
}
