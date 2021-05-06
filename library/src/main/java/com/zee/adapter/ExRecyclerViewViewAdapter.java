package com.zee.adapter;

import android.support.annotation.IdRes;
import android.view.View;

public class ExRecyclerViewViewAdapter {
    private View mView;

    public ExRecyclerViewViewAdapter(RecyclerViewViewAdapter paViewAdapater, View paEmptyView) {
        mView = paEmptyView;
        paViewAdapater.setExRecyclerViewViewAdapter(this);
        paViewAdapater.initViews();
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

}
