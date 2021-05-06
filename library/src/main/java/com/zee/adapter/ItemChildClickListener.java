package com.zee.adapter;

import android.view.View;

public interface ItemChildClickListener<T> {

    void onItemChildClick(View view, T bean, int position);

}
