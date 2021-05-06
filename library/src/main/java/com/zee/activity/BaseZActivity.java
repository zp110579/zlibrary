package com.zee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.zee.utils.ZEventBusUtils;

import java.lang.reflect.Field;

public abstract class BaseZActivity extends AppCompatActivity implements IBase {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        initViews();
    }

    public TextView findTextViewByID(@IdRes int id) {
        return findViewById(id);
    }

    public void startActivity(Class<?> paClass) {
        Intent loIntent = new Intent(this, paClass);
        startActivity(loIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZEventBusUtils.unregister(this);
    }
}
