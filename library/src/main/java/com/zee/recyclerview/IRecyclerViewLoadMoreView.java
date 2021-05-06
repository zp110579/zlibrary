package com.zee.recyclerview;

import android.support.annotation.LayoutRes;
import android.widget.LinearLayout;

/**
 * @author Administrator
 * @date 2018/11/21 0021
 */

public interface IRecyclerViewLoadMoreView {

    /**
     * 需要显示的LayoutID
     *
     * @return
     */
    @LayoutRes
    int getLayoutID();

    /**
     * 初始化控件
     *
     * @param linearLayout
     */
    void initViews(LinearLayout linearLayout);

    /**
     * 开始加载
     */
    void onLoadStart();

    /**
     * 加载结束
     */
    void onLoadEnd();

    /**
     * 没有数据
     */
    void onNoData();

    /**
     * 销毁
     */
    void destroy();

    void setHintText(HintText mHintText);
}
