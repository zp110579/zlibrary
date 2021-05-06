package com.zee.base;

import android.view.View;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public abstract class OnNoDoubleClickListener implements View.OnClickListener {
    private static long mDefaultSleepTime = 2000l;
    private long lastClickTime = 0;

    public OnNoDoubleClickListener() {
    }

    public OnNoDoubleClickListener(int sleepTime) {
        mDefaultSleepTime = sleepTime;
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > mDefaultSleepTime) {
            onNoDoubleClick(v);
            lastClickTime = currentTime;
        }
    }

    public abstract void onNoDoubleClick(View v);


}
