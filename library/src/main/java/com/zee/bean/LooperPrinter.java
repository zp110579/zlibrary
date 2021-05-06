package com.zee.bean;

import android.text.TextUtils;
import android.util.Printer;

import com.zee.log.ZLog;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class LooperPrinter implements Printer {
    public static final String TAG = "LooperPrinter";
    private long mFinishTimeMillis;
    private long mStartTimeMillis;
    private static final int START = 0;
    private static final int FINISH = 1;
    private static final int UNKONW = 2;

    @Override
    public void println(String x) {
        switch (isStart(x)) {
            case START:
                mStartTimeMillis = System.currentTimeMillis();
                break;
            case FINISH:
                mFinishTimeMillis = System.currentTimeMillis();
                long duration = mFinishTimeMillis - mStartTimeMillis;
                if (isBlock(duration)) {
                    ZLog.e(TAG, "block time:" + duration);
                }
                break;
        }
    }

    public int isStart(String x) {
        if (!TextUtils.isEmpty(x)) {
            if (x.startsWith(">>>>> Dispatching to Handler")) {
                return START;
            } else if (x.startsWith("<<<<< Finished to Handler")) {
                return FINISH;
            }
        }
        return UNKONW;
    }

    private boolean isBlock(long duration) {
        return duration > 16;
    }
}
