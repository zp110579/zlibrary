package com.zee.log;

import android.util.Log;

import com.zee.utils.UIUtils;
import com.zee.utils.ZConfig;
import com.zee.utils.ZLibrary;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-04-19.
 */

final class ZLogEngine implements ILog {
    private static final int STATE_VERBOSE = 0;
    private static final int STATE_DEBUG = 1;
    private static final int STATE_INFO = 2;
    private static final int STATE_WARN = 3;
    private static final int STATE_ERROR = 4;
    private static final int JSON_INDENT = 2;
    private static final String lineStr = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
    //默认配置
//    private ZConfig mConfiguration = ZConfig.createDefault();


    public void json(String json) {
        if (json == null || json.length() < 1) {
            d("Empty/Null json content");
            return;
        }
        try {

            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                i(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                i(message);
                return;
            }
        } catch (JSONException e) {
            exception(e, "");
        }
        e("Invalid Json");
    }

    public void printThis() {
        printInfo(null, "", STATE_VERBOSE);
    }

    @Override
    public void v(Object message) {
        printInfo(null, message, STATE_VERBOSE);
    }

    @Override
    public void v(String tag, Object message) {
        printInfo(tag, message, STATE_VERBOSE);
    }

    @Override
    public void w(Object message) {
        printInfo(null, message, STATE_WARN);
    }

    @Override
    public void w(String tag, Object message) {
        printInfo(tag, message, STATE_WARN);
    }

    @Override
    public void i(Object message) {
        printInfo(null, message, STATE_INFO);
    }

    public void i(Object message, boolean isPrintStackTraceInfo) {
        printInfo(null, message, STATE_INFO, isPrintStackTraceInfo);
    }

    @Override
    public void i(String tag, Object message) {
        printInfo(tag, message, STATE_INFO);
    }

    @Override
    public void d(Object message) {
        printInfo(null, message, STATE_DEBUG);
    }

    @Override
    public void d(String tag, Object message) {
        printInfo(tag, message, STATE_DEBUG);
    }

    @Override
    public void e(Object message) {
        printInfo(null, message, STATE_ERROR);
    }

    public void e(Object message, boolean isPrintStackTraceInfo) {
        printInfo(null, message, STATE_ERROR, isPrintStackTraceInfo);
    }

    @Override
    public void e(String tag, Object message) {
        printInfo(tag, message, STATE_ERROR);
    }


    private final void printInfo(String tag, Object message, int type) {
        if (message == null) {
            message = "";
        }

        String loMessage = message.toString();
        final int fixLength = 3 * 1024;
        if (loMessage.length() > fixLength) {
            for (int i = 0; i < loMessage.length(); i += fixLength) {
                if (i + fixLength < loMessage.length()) {
                    printInfo(tag, loMessage.substring(i, i + fixLength), type, false);
                } else {
                    printInfo(tag, loMessage.substring(i), type, false);
                }
            }
        } else {
            printInfo(tag, message, type, false);
        }
    }

    private final void printInfo(final String tag2, Object message, final int type, boolean isPrintStackTraceInfo) {
        if (ZLog.isDebug()) {
            final String tag = getTag(tag2);
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            new ZLogPrintMessage(trace, message, isPrintStackTraceInfo) {
                @Override
                public void printMessage(String message) {
                    print(tag, message, type);
                }
            };

//            int start = getStackOffset(trace, showStack);
//            int methodCount = getEndStackOffset(trace, showStack);
//            if (!showStack) {
//                methodCount = start + 1;
//            } else {
//                print(tag, lineStr, type);
//            }
//            if (methodCount < start) {
//                methodCount += 1;
//            }
//
//            StringBuffer builderA = new StringBuffer();
////            if (mConfiguration != null) {//打印当前线程
//            String threadName = Thread.currentThread().getName();
//            String name = "子";
//            if (threadName.equals("main")) {
//                name = "主";
//            }
//            builderA.append("[").append(name).append("]");
////            }
//
//            int index = 1;
//            int stackIndex = methodCount;
//
//            for (int i = methodCount; i > start; i--) {
//                if (stackIndex >= trace.length) {
//                    continue;
//                }
//                stackIndex = i;
//                StringBuilder builder = new StringBuilder();
//
//                builder.append(builderA);
////                if (mConfiguration.isShowStackTrace()) {
////                    builder.append(index);
////                    builder.append("->");
////                }
//                builder.append(trace[stackIndex].getMethodName())
//                        .append(" (")
//                        .append(trace[stackIndex].getFileName())
//                        .append(":")
//                        .append(trace[stackIndex].getLineNumber())
//                        .append(")");
//                if (i == start + 1) {
//                    builder.append("|");
//                    builder.append(message);
//                }
//
//                print(tag, builder.toString(), type);
////                print(tag, message.toString(), builder, type);
//                index++;
//            }
//            if (showStack) {
//                print(tag, lineStr, type);
//            }
            return;
        }
    }

    private void print(String tag, String message, StringBuilder builder, int type) {
        if (message.length() > 3000) {
            print(tag, "start:" + lineStr, type);
            for (int i = 0; i < message.length(); i += 3000) {
                StringBuilder builder1 = new StringBuilder(builder);
                if (i + 3000 < message.length()) {
                    builder1.append(message.substring(i, i + 3000));
                    print(tag, builder1.toString(), type);
                } else {
                    builder1.append(message.substring(i, message.length()));
                    print(tag, builder1.toString(), type);
                }
            }
            print(tag, "end:" + lineStr, type);
        } else {
            builder.append(message);
            print(tag, builder, type);
        }
    }


    public void printStartAndEnd(Throwable cause) {
        StackTraceElement[] trace = cause.getStackTrace();
        int start = getStackOffset(trace, true);
        int methodCount = getEndStackOffset(trace, true);
        Log.i("", "printStartAndEnd: " + trace[start].toString());
        Log.i("", "printStartAndEnd: " + trace[start + 1]);
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private void print(String tag, Object message, int type) {
        if (message == null) {
            message = "**NULL**";
        }

        switch (type) {
            case STATE_VERBOSE:
                Log.v(tag, message.toString());
                break;
            case STATE_DEBUG:
                Log.d(tag, message.toString());
                break;
            case STATE_INFO:
                Log.i(tag, message.toString());
                break;
            case STATE_WARN:
                Log.w(tag, message.toString());
                break;
            case STATE_ERROR:
                Log.e(tag, message.toString());
                break;
            default:
                Log.i("**" + tag, message.toString());
                break;
        }
    }

    //返回tag
    private String getTag(String tag) {
        StringBuilder builder = new StringBuilder();
        builder.append(ZConfig.TAG);
        if (tag != null) {
            builder.append("-");
            builder.append(tag);
        }
        return builder.toString();
    }

    private String getMessage(Object infor, StackTraceElement stackTraceElement) {
        StringBuilder builder = new StringBuilder();

        String threadName = Thread.currentThread().getName();
        String name = "子";
        if (threadName.equals("main")) {
            name = "主";
        }
        builder.append("[").append(name).append("]");


        boolean isF = true;

        StringBuilder stringBuilder = new StringBuilder();
        if (!isF) {
            stringBuilder.append("\n\t");
            stringBuilder.append("[方法]");
            stringBuilder.append(stackTraceElement.getMethodName());
        }
//            stringBuilder.append("[ ");
        stringBuilder.append("(")
                .append(stackTraceElement.getFileName())
                .append(":")
                .append(stackTraceElement.getLineNumber())
                .append(")");
        stringBuilder.append("| ");
        builder.append(stringBuilder);
        return builder.toString();
    }

    protected void exception(Exception e, String message) {
        ExceptionFileUtil.dealNativeWithException(e, message);
    }


    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(StackTraceElement[] trace, boolean isEvent) {
        if (isEvent) {
            for (int i = 5; i < trace.length; i++) {
                StackTraceElement e = trace[i];
                String name = e.getClassName();
                if (!name.equals(ZLog.class.getName()) && !name.equals(ZLogEngine.class.getName()) && !name.equals(EventBus.class.getName())) {
                    return i;
                }
            }

        } else {
            for (int i = 3; i < trace.length; i++) {
                StackTraceElement e = trace[i];
                String name = e.getClassName();
                if (!name.equals(ZLog.class.getName()) && !name.equals(ZLogEngine.class.getName())) {
                    return --i;
                }
            }
        }
        return -1;
    }

    private int getEndStackOffset(StackTraceElement[] trace, boolean isEvent) {
        if (isEvent) {
            for (int i = 5; i < trace.length; i++) {
                StackTraceElement e = trace[i];
                String name = e.getClassName();
                if (name.equals(Object.class.getName()) || ((name.indexOf("android") == 0 || name.indexOf("java") == 0))) {
                    return --i;
                }
            }
        } else {
            for (int i = 3; i < trace.length; i++) {
                StackTraceElement e = trace[i];
                String name = e.getClassName();
                if (!name.equals(Object.class.getName()) && ((name.indexOf("android") == 0 || name.indexOf("java") == 0))) {
                    return --i;
                }
            }
        }
        return -1;
    }

}
