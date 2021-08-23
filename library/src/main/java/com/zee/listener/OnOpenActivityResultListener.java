package com.zee.listener;

import android.content.Intent;

public interface OnOpenActivityResultListener {
    /**
     * Activity返回的值
     *
     * @param data
     */
    void onActivityResult(Intent data);
}
