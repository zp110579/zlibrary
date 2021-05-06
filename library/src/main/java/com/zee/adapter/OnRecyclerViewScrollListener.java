package com.zee.adapter;

import android.support.v7.widget.RecyclerView;

public abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int curHeight;
    private int curWidth;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        curHeight += dy;
        curWidth += dx;
        onScrolled(curWidth, curHeight);
    }

    abstract void onScrolled(int dx, int dy);
}
