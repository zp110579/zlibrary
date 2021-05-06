package com.zee.listener;

public interface OnTabSelectListener {

    void onTabSelect(int position);

    /**
     * 重新点击一次，可以做刷新使用
     *
     * @param position
     */
    void onTabReselect(int position);
}