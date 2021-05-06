package com.zee.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.zee.log.ZLog;


/**
 * @author Administrator
 */
class LoadMoreVerticalLinearLayout extends LinearLayout implements ILoadMoreLinearLayout {
    private IRecyclerViewLoadMoreView mDefaultFooterView;
    /**
     * 开始加载
     */
    private final static int STATE_LOAD_START = 0;
    /**
     * 加载结束
     */
    private final static int STATE_LOAD_END = 1;
    /**
     * 没有数据
     */
    private final static int STATE_NO_DATA = 2;
    /**
     * 是否不能下拉加载更多
     */
    public boolean loadingMoreEnabled = false;
    /**
     * 是否没有数据了
     */
    private boolean isNoMore = false;
    /**
     * 当前拉拽装填
     */
    private int mState;
    /**
     * 加载更多监听器
     */
    private LoadMoreListener mLoadMoreListener;

    private HintText mHintText;

    public LoadMoreVerticalLinearLayout(Context context) {
        super(context);
    }

    public LoadMoreVerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initView() {
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(mDefaultFooterView.getLayoutID(), this);
        mDefaultFooterView.initViews(this);
        setLoadMoreEnabled(false);
        setVisibility(GONE);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
        if (mLoadMoreListener != null) {
            setLoadMoreEnabled(true);
        } else {
            setLoadMoreEnabled(false);
        }
    }

    public void setDefaultLoadMoreView(IRecyclerViewLoadMoreView defaultFooterView) {
        if (mDefaultFooterView != null) {
            removeAllViews();
        }
        mDefaultFooterView = defaultFooterView;
        initView();
    }

    public void setHintText(HintText hintText) {
        mHintText = hintText;
    }


    /**
     * 是否可以下拉加载更多
     */
    public boolean isCanDrag() {
        return mLoadMoreListener != null && mState != STATE_LOAD_START && loadingMoreEnabled;
    }

    /**
     * 开始加载更多
     */
    public void startLoadMore() {
        setState(STATE_LOAD_START);
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onLoadMore();
        }
    }

    public boolean isNoMore() {
        return isNoMore;
    }

    /**
     * 加载结束
     */
    public void loadMoreFinish() {
        if (mState != STATE_LOAD_END) {
            setState(STATE_LOAD_END);
        }
    }

    public void setNoMore(boolean noMore) {
        isNoMore = noMore;
        setState(isNoMore ? STATE_NO_DATA : STATE_LOAD_END);
    }

    /**
     * 设置能不能下拉
     *
     * @param enabled
     */
    public void setLoadMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            setState(STATE_LOAD_END);
        }
    }

    /**
     * 是否可以下来判断
     *
     * @param layoutManager 当前RecyclerView设置的Layout
     * @param headViewSize  头部有几个View
     */
    public void dragLoadMoreView(RecyclerView.LayoutManager layoutManager, int headViewSize) {
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = findMax(into);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        int adjAdapterItemCount = layoutManager.getItemCount() + headViewSize;

        boolean isTrue = layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= adjAdapterItemCount - mHintText.getLimitNumber()
                && adjAdapterItemCount >= layoutManager.getChildCount() && !isNoMore();

        if (isTrue) {
            startLoadMore();
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public void setState(int state) {
        if (mDefaultFooterView == null) {
            return;
        }
        mState = state;
        switch (state) {
            case STATE_LOAD_START:
                //正在加载...
                mDefaultFooterView.onLoadStart();
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_LOAD_END:
                //加载完成
                mDefaultFooterView.onLoadEnd();
                this.setVisibility(View.GONE);
                break;
            case STATE_NO_DATA:
                //没有数据了
                mDefaultFooterView.onNoData();
                this.setVisibility(View.VISIBLE);
                break;
            default:
                ZLog.i("其他状态");
                break;
        }
    }

    public void destroy() {

    }

    @Override
    public boolean loadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    @Override
    public View getLoadMoreView() {
        return this;
    }
}
