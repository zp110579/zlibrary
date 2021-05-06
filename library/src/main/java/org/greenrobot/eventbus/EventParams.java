package org.greenrobot.eventbus;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.zee.utils.UIUtils;
import com.zee.utils.ZEventBusUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class EventParams {
    private HashMap<String, Object> params = new HashMap<>();

    public void eventBusPost(String methodTag) {
        ZEventBusUtils.post(this, methodTag);
    }

    public void eventBusPost() {
        ZEventBusUtils.post(this);
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    public void put(String key, boolean value) {
        params.put(key, value);
    }

    public void put(String key, Object value) {
        params.put(key, value);
    }

    public void put(String key, double value) {
        params.put(key, value);
    }

    public void put(String key, int value) {
        params.put(key, value);
    }


    //*****************************************************************************************************************
    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int var2) {
        Object var3;
        return (var3 = this.getValue(key)) != null && var3 instanceof Integer ? ((Integer) var3).intValue() : var2;
    }


    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String var2) {
        Object var3;
        return (var3 = this.getValue(key)) != null && var3 instanceof String ? (String) var3 : var2;
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double var2) {
        Object var3;
        return (var3 = this.getValue(key)) != null && var3 instanceof Double ? (double) var3 : var2;
    }

    @Override
    public String toString() {
        return params.toString();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean var2) {
        Object var3;
        return (var3 = this.getValue(key)) != null && var3 instanceof Boolean ? ((Boolean) var3).booleanValue() : var2;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getObject(String key) {
        Object object = getValue(key);
        return object == null ? null : (T) getValue(key);
    }

    private Object getValue(String key) {
        return params.get(key);
    }

    public int size() {
        return params.size();
    }
}
