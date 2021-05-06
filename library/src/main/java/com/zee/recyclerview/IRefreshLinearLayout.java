package com.zee.recyclerview;

import android.view.MotionEvent;
import android.view.View;

interface IRefreshLinearLayout {
    void setRecyclerView(IRecyclerView xRecyclerView);

    void setDefaultRefreshView(IRecyclerViewRefreshView iRecyclerViewVerticalRefreshView);

    void refreshFinish();

    void startRefresh();

    void setRefreshListener(RefreshListener listener);

    void setRefreshEnabled(boolean isEnable);

    boolean isWaitRefresh();

    boolean onMove(MotionEvent ev);

    void setAppbarState(AppBarStateChangeListener.State state);

    void destroy();

    View getRefreshView();
}
