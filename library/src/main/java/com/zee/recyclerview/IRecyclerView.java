package com.zee.recyclerview;

import com.zee.listener.ScrollAlphaChangeListener;

public interface IRecyclerView {

    void refresh();

    void loadFinish();

//    void addHeaderView(View view);

    void setNoMore(boolean noMore);

    void setRefreshEnabled(boolean isEnable);

    void setRefreshListener(RefreshListener listener);

    void setLoadMoreEnabled(boolean isEnable);

    void setLoadMoreListener(LoadMoreListener listener);

    void setRefreshAndLoadMordListener(RefreshAndLoadMoreAdapter listener);

    void setScrollAlphaChangeListener(ScrollAlphaChangeListener scrollAlphaChangeListener);

}
