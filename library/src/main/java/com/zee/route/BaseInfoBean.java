package com.zee.route;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class BaseInfoBean<T extends BaseInfoBean> {

    private String mName;
    private String mModule;
    protected Class<?> mGoalClass;
    protected Bundle mBundle = new Bundle();
    private int type;

    public BaseInfoBean(String name, String mModule, int type) {
        this.mName = name;
        this.mModule = mModule;
        this.type = type;
    }

    public BaseInfoBean(Class<?> classZ, int type) {
        mGoalClass = classZ;
        this.type = type;
    }

    public Class<?> getGoalClass() {
        return mGoalClass;
    }

    public int getType() {
        return type;
    }

    public T withBundle(Bundle bundle) {
        this.mBundle = bundle;
        return (T) this;
    }

    public T putString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return (T) this;
    }

    public T putInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return (T) this;
    }

    public T putDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return (T) this;
    }

    public T putBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return (T) this;
    }

    public T putShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return (T) this;
    }

    public T putLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return (T) this;
    }

    public T putFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return (T) this;
    }

    public T putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        mBundle.putCharSequence(key, value);
        return (T) this;
    }

    public T putParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return (T) this;
    }

    public T putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return (T) this;
    }

    public T putParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return (T) this;
    }

    public T putSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        mBundle.putSparseParcelableArray(key, value);
        return (T) this;
    }

    public T putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return (T) this;
    }

    public T putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return (T) this;
    }

    public T putCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mBundle.putCharSequenceArrayList(key, value);
        return (T) this;
    }

    public T putSerializable(@Nullable String key, @Nullable Serializable value) {
        mBundle.putSerializable(key, value);
        return (T) this;
    }

    public T putByteArray(@Nullable String key, @Nullable byte[] value) {
        mBundle.putByteArray(key, value);
        return (T) this;
    }

    public T putShortArray(@Nullable String key, @Nullable short[] value) {
        mBundle.putShortArray(key, value);
        return (T) this;
    }

    public T putCharArray(@Nullable String key, @Nullable char[] value) {
        mBundle.putCharArray(key, value);
        return (T) this;
    }

    public T putFloatArray(@Nullable String key, @Nullable float[] value) {
        mBundle.putFloatArray(key, value);
        return (T) this;
    }

    public T putCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        mBundle.putCharSequenceArray(key, value);
        return (T) this;
    }

    public T putBundle(@Nullable String key, @Nullable Bundle value) {
        mBundle.putBundle(key, value);
        return (T) this;
    }

    public T putBundle(@Nullable Bundle value) {
        if (value != null) {
            mBundle.putAll(value);
        }
        return (T) this;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public String getModule() {
        return mModule;
    }

    public String getName() {
        return mName;
    }

    String getInfo() {
        return mName + "$$" + mModule;
    }

}
