package com.zee.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.zee.utils.ZEventBusUtils;


public abstract class ExActivity extends AppCompatActivity implements IBindView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        initViews(savedInstanceState);
        initViews();
    }

    void initViews(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        /** 主动调用gc回收 */
        System.gc();
        super.onDestroy();
        ZEventBusUtils.unregister(this);
    }

    /**
     * 启动activity
     */
    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
