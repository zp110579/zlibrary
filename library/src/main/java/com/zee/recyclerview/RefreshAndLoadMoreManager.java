package com.zee.recyclerview;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * 管理刷新和加载更多
 */
public class RefreshAndLoadMoreManager {
    private XRecyclerView mXRecyclerView;

    private IRefreshLinearLayout mRefreshVewLayout;
    private ILoadMoreLinearLayout mLoadMoreViewLayout;
    private int type;
    private RefreshAndLoadMoreAdapter mRefreshAndLoadMoreAdapter;

    /**
     * 顶部刷新View(默认Vertical)
     */
    private IRecyclerViewRefreshView mIRecyclerViewVerticalRefreshView;

    /**
     * 底部加载更多的View
     */
    private IRecyclerViewLoadMoreView mIRecyclerViewVerticalLoadMoreView;

    RefreshAndLoadMoreManager(XRecyclerView xRecyclerView, int type) {
        this.type = type;
        mXRecyclerView = xRecyclerView;
        HintText mHintText = mXRecyclerView.getHintText();
        if (type == 0) {
            mRefreshVewLayout = new RefreshVerticalLinearLayout(getContext());
            //底部加载更多的view
            mLoadMoreViewLayout = new LoadMoreVerticalLinearLayout(getContext());
            //设置默认的LayoutManager
            xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRefreshVewLayout = new RefreshHorizontalLinearLayout(getContext());
            //底部加载更多的view
            mLoadMoreViewLayout = new LoadMoreHorizontialLinearLayout(getContext());
            //设置默认的LayoutManager
            xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        mRefreshVewLayout.setRecyclerView(mXRecyclerView);
        mLoadMoreViewLayout.setHintText(mHintText);

        mIRecyclerViewVerticalRefreshView = new DefaultRefreshView();
        mIRecyclerViewVerticalRefreshView.setHintText(mHintText);
        setRefreshView(mIRecyclerViewVerticalRefreshView);

        mIRecyclerViewVerticalLoadMoreView = new DefaultLoadMoreView();
        mIRecyclerViewVerticalLoadMoreView.setHintText(mHintText);
        setLoadMoreView(mIRecyclerViewVerticalLoadMoreView);
    }

    void dragLoadMoreView(RecyclerView.LayoutManager layoutManager, int i) {
        mLoadMoreViewLayout.dragLoadMoreView(layoutManager, i);
    }

    void setAppbarState(AppBarStateChangeListener.State state) {
        mRefreshVewLayout.setAppbarState(state);
    }


    boolean loadingMoreEnabled() {
        if (mLoadMoreViewLayout == null) {
            return false;
        }
        return mLoadMoreViewLayout.loadingMoreEnabled();
    }

    View getRefreshView() {
        if (mRefreshVewLayout != null) {
            return mRefreshVewLayout.getRefreshView();
        }
        return null;
    }

    View getLoadMoreView() {
        return mLoadMoreViewLayout.getLoadMoreView();
    }


    /**
     * 设置顶部
     *
     * @param refreshView
     */
    void setRefreshView(IRecyclerViewRefreshView refreshView) {
        mIRecyclerViewVerticalRefreshView = refreshView;
        mRefreshVewLayout.setDefaultRefreshView(mIRecyclerViewVerticalRefreshView);
    }

    void setLoadMoreView(IRecyclerViewLoadMoreView IRecyclerViewLoadMoreView) {
        mIRecyclerViewVerticalLoadMoreView = IRecyclerViewLoadMoreView;
        mLoadMoreViewLayout.setDefaultLoadMoreView(mIRecyclerViewVerticalLoadMoreView);
    }

    boolean isCanDrag() {
        if (mLoadMoreViewLayout == null) {
            return false;
        }
        return mLoadMoreViewLayout.isCanDrag() && mRefreshVewLayout.isWaitRefresh();
    }

    /**
     * 加载结束
     * 包括刷新结束，加载更多结束
     */
    void loadFinish() {
        if (mRefreshVewLayout != null) {
            mRefreshVewLayout.refreshFinish();
        }
        if (mLoadMoreViewLayout != null) {
            mLoadMoreViewLayout.loadMoreFinish();
        }
    }

    /**
     * 设置是否可以加载更多
     *
     * @param noMore
     */
    void setNoMore(boolean noMore) {
        if (mLoadMoreViewLayout != null) {
            mLoadMoreViewLayout.setNoMore(noMore);
        }
    }

    void refresh() {
        if (mRefreshVewLayout != null) {
            mRefreshVewLayout.startRefresh();
        }
    }

    void setRefreshListener(RefreshListener listener) {
        if (mRefreshVewLayout != null) {
            mRefreshVewLayout.setRefreshListener(listener);
        }
    }

    void setLoadMoreListener(LoadMoreListener listener) {
        if (mLoadMoreViewLayout != null) {
            mLoadMoreViewLayout.setLoadMoreListener(listener);
        }
    }

    void setRefreshAndLoadMordListener(RefreshAndLoadMoreAdapter listener) {
        if (listener != null) {
            mRefreshAndLoadMoreAdapter = listener;
            setRefreshListener(mRefreshAndLoadMoreAdapter);
            setLoadMoreListener(mRefreshAndLoadMoreAdapter);
        }
    }

    public RefreshAndLoadMoreAdapter getRefreshAndLoadMoreAdapter() {
        return mRefreshAndLoadMoreAdapter;
    }

    void setRefreshEnabled(boolean isEnable) {
        if (mRefreshVewLayout != null) {
            mRefreshVewLayout.setRefreshEnabled(isEnable);
        }
    }

    void setLoadMoreEnabled(boolean isEnable) {
        if (mLoadMoreViewLayout != null) {
            mLoadMoreViewLayout.setLoadMoreEnabled(isEnable);
        }
    }

    private Context getContext() {
        return mXRecyclerView.getContext();
    }

    boolean onMove(MotionEvent ev) {
        if (mRefreshVewLayout != null) {
            return mRefreshVewLayout.onMove(ev);
        }
        return true;
    }

    void onDestroy() {
        if (mLoadMoreViewLayout != null) {
            mLoadMoreViewLayout.destroy();
        }
        mLoadMoreViewLayout = null;
        if (mRefreshVewLayout != null) {
            mRefreshVewLayout.destroy();
            mRefreshVewLayout = null;
        }
    }

    public int getLinearLayoutManagerOrientation() {
        return type;
    }


    void onAttachedToWindow(ViewParent viewParent) {
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        while (viewParent != null) {
            if (viewParent instanceof CoordinatorLayout) {
                break;
            }
            viewParent = viewParent.getParent();
        }
        if (viewParent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) viewParent;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        setAppbarState(state);
                    }
                });
            }
        }
    }
}
