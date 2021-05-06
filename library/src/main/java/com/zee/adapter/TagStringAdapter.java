package com.zee.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.zee.libs.R;
import com.zee.view.ZxTagLayout;
import com.zee.view.ZxTextView;

import java.util.List;

public abstract class TagStringAdapter extends TagAdapter<String> {
    private OnDataChangedListener mOnDataChangedListener;
    private View sView;

    public TagStringAdapter(List<String> datas) {
        super(datas);
    }

    public interface OnDataChangedListener {
        void onChanged();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public void notifyDataChanged() {
        if (mOnDataChangedListener != null)
            mOnDataChangedListener.onChanged();
    }

    public abstract View getView(ZxTagLayout parent, int position, String str);

    public void onSelected(int position, View view) {
        Log.d("zhy", "onSelected " + position);
    }

    public void unSelected(int position, View view) {
        Log.d("zhy", "unSelected " + position);
    }

    public boolean setSelected(int position, String t) {
        return false;
    }


}
