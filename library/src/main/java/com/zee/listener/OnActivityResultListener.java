package com.zee.listener;

import android.content.Intent;

public interface OnActivityResultListener {
    /**
     * Activity返回的值
     *
     * @param data
     */
    void onActivityResult(Intent data);
}
