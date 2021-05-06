package com.zee.log;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.zee.activity.CrashActivity;
import com.zee.utils.Comment;
import com.zee.utils.FileUtil;
import com.zee.utils.UIUtils;
import com.zee.utils.ZConfig;
import com.zee.utils.ZLibrary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Administrator on 2017-04-20.
 */

public class ZCrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static ZCrashExceptionHandler instances;
    private Application context;
    private Class<?> mClass;

    public static ZCrashExceptionHandler getInstance() {
        if (instances == null) {
            instances = new ZCrashExceptionHandler();
        }
        return instances;
    }

    public void setClass(Class aClass) {
        mClass = aClass;
    }

    public void init(Application context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        handleException(arg1);
        if (null != mClass) {//是否从新启动
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ZLog.exception(e);
            }
            restartApp();
        }
    }

    private void handleException(final Throwable ex) {
        if (ex == null) {
            return;
        }

        boolean isSave = false;
        ZLibrary zLibrary = ZLibrary.getInstance();
        ZConfig config = zLibrary.getZConfig();

        if (config != null) {
            isSave = config.isSaveExceptionLog() || config.isDebug();
        }

        StringBuilder stringBuffer = new StringBuilder();
        if (isSave) {
//            UIUtils.runOnAsyncThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
            String errorInfo = getErrorInfo(ex);
//            String[] info = errorInfo.split("\n\r");
            stringBuffer.append("-------------------------------------------------------\r\n");
            stringBuffer.append(ExceptionFileUtil.getBaseInfor());
            stringBuffer.append(errorInfo);
            String path = Comment.getCrashExceptionFile();//记录Exception日志
            FileUtil.writeFileAdd(path, stringBuffer.toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }


        if (config.isDebug()) {
            startCrashListPage(stringBuffer.toString());
        }
    }

    public static void startCrashListPage(String message) {
        Context context = UIUtils.getApplication();
        Intent intent = new Intent(context, CrashActivity.class);
        intent.putExtra("txt",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    //重新启动程序
    private void restartApp() {
        Intent intent = new Intent(context, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    /**
     * 获取 错误的信息
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }
}