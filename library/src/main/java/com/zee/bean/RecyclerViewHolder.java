package com.zee.bean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private SparseArray<View> views = new SparseArray<View>();
    private MultiItemAdapter mMultiItemAdapter;

    public RecyclerViewHolder(View view, MultiItemAdapter multiItemAdapter) {
        super(view);
        mConvertView = view;
        this.mMultiItemAdapter = multiItemAdapter;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public MultiItemAdapter getMultiAdapterBean() {
        return mMultiItemAdapter;
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T findViewById(int id) {
        View view = views.get(id);
        if (view == null) {
            view = mConvertView.findViewById(id);
        }
        if (view == null) {
            return null;
        }
        return (T) view;
    }

    public Context getContext() {
        return mConvertView.getContext();
    }
}
