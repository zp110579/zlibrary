package com.zee.adapter;

import android.view.View;

public interface OnItemViewClickListener<T> {
    void onItemClick(T bean, View view, int position);
}
