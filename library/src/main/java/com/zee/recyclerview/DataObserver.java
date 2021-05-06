package com.zee.recyclerview;

import android.support.v7.widget.RecyclerView;

class DataObserver extends RecyclerView.AdapterDataObserver {
    private XRecyclerView mXRecyclerView;

    public DataObserver(XRecyclerView recyclerView) {
        mXRecyclerView = recyclerView;
    }

    @Override
    public void onChanged() {
        mXRecyclerView.mWrapAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        mXRecyclerView.mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        mXRecyclerView.mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        mXRecyclerView.mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        mXRecyclerView.mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        mXRecyclerView.mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
    }
}
