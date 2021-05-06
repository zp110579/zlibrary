package org.greenrobot.eventbus;

import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class EventIntentParams extends Intent {

    public void put(String key, String value) {
        putExtra(key, value);
    }

    public void put(String key, boolean value) {
        putExtra(key, value);
    }

    public void put(String key, Serializable value) {
        putExtra(key, value);
    }

    public void put(String key, double value) {
        putExtra(key, value);
    }

    public void put(String key, int value) {
        putExtra(key, value);
    }


    //*****************************************************************************************************************
    public int getInt(String key) {
        return super.getIntExtra(key, 0);
    }

    public int getInt(String key, int var2) {
        return getIntExtra(key, var2);
    }

    public String getString(String key) {
        return getStringExtra(key);
    }

    public String getString(String key, String var2) {
        String var3 = getStringExtra(key);
        if (null == var3) {
            return var2;
        }
        return var3;
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double var2) {
        double var3 = getDoubleExtra(key, var2);
        return var3;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean var2) {
        return getBooleanExtra(key, var2);
    }
}
