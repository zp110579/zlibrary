package com.zee.recyclerview;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;

import com.zee.libs.R;

class XRecyclerViewParamManager {
    private int linearLayoutManager_orientation = 0;////默认0：:竖列显示，1:是横列显示

    XRecyclerViewParamManager(TypedArray ta, XRecyclerView recyclerView) {

        HintText hintText = recyclerView.getHintText();
        //设置基本的字体颜色
        hintText.setTextColor(ta.getColor(R.styleable.ZxRecyclerView_zv_textColor, Color.TRANSPARENT));
        //设置刷新的字体颜色
        hintText.setRefresh_textColor(ta.getColor(R.styleable.ZxRecyclerView_zv_refresh_textColor, Color.TRANSPARENT));
        //设置加载更多的字体颜色
        hintText.setLoadMore_textColor(ta.getColor(R.styleable.ZxRecyclerView_zv_loadMore_textColor, Color.TRANSPARENT));
        //设置刷新的背景颜色
        hintText.setRefreshBackgroundColor(ta.getColor(R.styleable.ZxRecyclerView_zv_refresh_background, Color.TRANSPARENT));
        //设置基本的字体大小
        hintText.setTextSize(ta.getDimension(R.styleable.ZxRecyclerView_zv_textSize, -1));
        //设置刷新的字体大小
        hintText.setRefresh_textSize(ta.getDimension(R.styleable.ZxRecyclerView_zv_refresh_textSize, -1));
        //设置加载更多的字体大小
        hintText.setLoadMore_textSize(ta.getDimension(R.styleable.ZxRecyclerView_zv_loadMore_textSize, -1));

        //设置最少多少开始加载更多
        hintText.setLimitNumber(ta.getInt(R.styleable.ZxRecyclerView_zv_limitNumber, 1));
        //刷新的松开手的提示
        String hintReleaseHand = ta.getString(R.styleable.ZxRecyclerView_zv_refresh_txt_hintReleaseHand);
        if (!TextUtils.isEmpty(hintReleaseHand)) {
            hintText.setRefresh_hintReleaseHandText(hintReleaseHand);
        }
        //提示下拉刷新
        String zv_refresh_hintPullDown = ta.getString(R.styleable.ZxRecyclerView_zv_refresh_txt_hintPullDown);
        if (!TextUtils.isEmpty(zv_refresh_hintPullDown)) {
            hintText.setRefresh_hintPullDownText(zv_refresh_hintPullDown);
        }
        //刷新开始
        String zv_refresh_refreshStart = ta.getString(R.styleable.ZxRecyclerView_zv_refresh_txt_refreshStart);
        if (!TextUtils.isEmpty(zv_refresh_refreshStart)) {
            hintText.setRefresh_refreshStartText(zv_refresh_refreshStart);
        }
        //刷新结束
        String zv_refresh_refreshEnd = ta.getString(R.styleable.ZxRecyclerView_zv_refresh_txt_refreshEnd);
        if (!TextUtils.isEmpty(zv_refresh_refreshEnd)) {
            hintText.setRefresh_refreshEndText(zv_refresh_refreshEnd);
        }

        //没有更多数据提示
        String zv_loadMore_noMore = ta.getString(R.styleable.ZxRecyclerView_zv_loadMore_txt_noMore);
        if (!TextUtils.isEmpty(zv_loadMore_noMore)) {
            hintText.setLoadMore_noMoreText(zv_loadMore_noMore);
        }
        //加载更多结束提示
        String zv_loadMore_loadEnd = ta.getString(R.styleable.ZxRecyclerView_zv_loadMore_txt_loadEnd);
        if (!TextUtils.isEmpty(zv_loadMore_loadEnd)) {
            hintText.setLoadMore_loadEndText(zv_loadMore_loadEnd);
        }
        //开始加载更多提示
        String zv_loadMore_loadStart = ta.getString(R.styleable.ZxRecyclerView_zv_loadMore_txt_loadStart);
        if (!TextUtils.isEmpty(zv_loadMore_loadStart)) {
            hintText.setLoadMore_loadStartText(zv_loadMore_loadStart);
        }
        //默认0：:竖列显示，1:是横列显示
        linearLayoutManager_orientation = ta.getInt(R.styleable.ZxRecyclerView_zv_linearLayoutManager, 0);

        ta.recycle();
    }

    int getLinearLayoutManagerOrientation() {
        return linearLayoutManager_orientation;
    }
}
