package com.zee.recyclerview;

public abstract class RefreshAndLoadMoreAdapter implements RefreshListener, LoadMoreListener {
    private int defaultStart = 0;//默认从0开始
    private int curPage = 0;

    public RefreshAndLoadMoreAdapter() {
    }

    public RefreshAndLoadMoreAdapter(int startPage) {
        this.defaultStart = startPage;
        curPage = defaultStart;
    }

    @Override
    public void onRefresh() {
        curPage = defaultStart;
        onStartLoad(curPage);
    }

    @Override
    public void onLoadMore() {
        curPage++;
        onStartLoad(curPage);
    }

    public abstract void onStartLoad(int curPage);

    /**
     * 判断刚刚进行的是刷新还是加载更多
     *
     * @return
     */
    public boolean isRefreshState() {
        return curPage == defaultStart;
    }
}
