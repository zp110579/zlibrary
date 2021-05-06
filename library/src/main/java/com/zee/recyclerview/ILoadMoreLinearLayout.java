package com.zee.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface ILoadMoreLinearLayout {

    void setHintText(HintText hintText);

    void setDefaultLoadMoreView(IRecyclerViewLoadMoreView iRecyclerViewVerticalLoadMoreView);

    void loadMoreFinish();

    void setNoMore(boolean noMore);

    void setLoadMoreListener(LoadMoreListener listener);

    void setLoadMoreEnabled(boolean isEnable);

    boolean isCanDrag();

    void dragLoadMoreView(RecyclerView.LayoutManager layoutManager, int i);

    void destroy();

    boolean loadingMoreEnabled();

    View getLoadMoreView();
}
