package com.zee.log;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/2/18 0018.
 */

 abstract class ZLogPrintMessage {

    public ZLogPrintMessage(StackTraceElement[] trace, Object message, boolean isPrintStackTraceInfo) {
        List<StackTraceElement> stringList = new ArrayList<>();

        for (int i = 3; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (stringList.size() == 0) {
                if (!name.equals(ZLog.class.getName()) && !name.equals(ZLogEngine.class.getName())) {
                    stringList.add(e);
                    if (!isPrintStackTraceInfo) {
                        break;
                    }
                }
            } else if (!name.contains("android") &&
                    !name.contains("java") &&
                    !name.contains("org.greenrobot.eventbus.") &&
                    !name.contains("com.zee.utils.")) {
                stringList.add(e);
            }

        }
        println(stringList, message.toString());
    }

    public void println(List<StackTraceElement> list, String message) {
        String threadName = Thread.currentThread().getName();
        boolean isMainThread = false;
        if (threadName.equals("main")) {
            isMainThread = true;
        }

        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (StackTraceElement stackTraceElement : list) {
            builder.append(getString(stackTraceElement, isMainThread));
            if (isFirst) {
                builder.append("|");
                builder.append(message);
                isFirst = false;
            }
            builder.append("\n");
        }

//        Log.i("TAG", builder.toString());
        printMessage(builder.toString());
    }

    private String getString(StackTraceElement stackTraceElement, boolean isMainThread) {
        StringBuilder result = new StringBuilder();
        if (isMainThread) {
            result.append("[主]");
        } else {
            result.append("[子]");
        }
        result.append(".").append(stackTraceElement.getMethodName());
        result.append("(").append(stackTraceElement.getFileName()).append(":").append(stackTraceElement.getLineNumber()).append(")");

        return result.toString();
    }

    public abstract void printMessage(String message);

}
