package com.zee.recyclerview;

import android.support.annotation.LayoutRes;
import android.widget.LinearLayout;


/**
 * @author Administrator
 */
public interface IRecyclerViewRefreshView {

    /**
     * 需要显示的LayoutID
     *
     * @return
     */
    @LayoutRes
    int getLayoutID();

    /**
     * 初始化组件
     *
     * @param view
     */
    void initViews(LinearLayout view);

    /**
     * 返回自己的高度
     *
     * @return
     */
    int getViewHeight();

    /**
     * 提示下拉
     *
     * @param oldState 上次的状态
     */
    void onHintPullDown(int oldState);

    /**
     * 提示松手
     */
    void onHintReleaseHand();

    /**
     * 开始刷新
     */
    void onRefreshStart();

    /**
     * 刷新结束
     */
    void onRefreshEnd();

    /**
     * 当前View的高度
     *
     * @param height
     */
    void onHeight(int height);

    /**
     * 释放资源
     */
    void destroy();

    void setHintText(HintText mHintText);
}
