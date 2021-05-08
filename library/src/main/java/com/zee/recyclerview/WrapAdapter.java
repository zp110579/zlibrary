package com.zee.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zee.adapter.ExRecyclerViewViewAdapter;
import com.zee.adapter.RecyclerViewViewAdapter;
import com.zee.log.ZLog;
import com.zee.utils.ZLibrary;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public RecyclerView.Adapter adapter;
    private XRecyclerView mXRecyclerView;
    private RefreshAndLoadMoreManager mRefreshAndLoadMoreManager;

    static final int TYPE_REFRESH_HEADER = 110000000;//顶部，刷新
    private static final int TYPE_EMPTY_VIEW = 120000000;//空数据
    static final int TYPE_FOOTER = 130000000;//底部，加载更多
    static final int HEADER_INIT_INDEX = 140000000;//在顶部增加的View
    static final int FOOTER_INIT_INDEX = 150000000;//在底部增加的View

    private RecyclerViewAddViewManager mRecyclerViewAddViewManager;
    private HashMap<Integer, View> mCenterViewHashMap = new HashMap<>();
    //没有数据显示的View资源LayoutID
    private int noDateResID = -1;

    WrapAdapter(XRecyclerView recyclerView) {
        this.mXRecyclerView = recyclerView;
        mRecyclerViewAddViewManager = new RecyclerViewAddViewManager(recyclerView.getContext(), this);
        mRefreshAndLoadMoreManager = recyclerView.getRefreshAndLoadMoreManager();
    }

    XRecyclerView getXRecyclerView() {
        return mXRecyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    RecyclerView.Adapter getOriginalAdapter() {
        return this.adapter;
    }

    void setLayoutManager(RecyclerView.LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layout);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mRecyclerViewAddViewManager.isHeader(position) || isLoadMore(position) || isRefreshHeader(position)) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isLoadMore(int position) {
        if (mRefreshAndLoadMoreManager.loadingMoreEnabled()) {
            return position == getItemCount() - 1;
        } else {
            return false;
        }
    }

    private boolean isRefreshHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REFRESH_HEADER) {
            return new SimpleViewHolder(mRefreshAndLoadMoreManager.getRefreshView());
        } else if (mRecyclerViewAddViewManager.isHeaderType(viewType)) {
            return new SimpleViewHolder(mRecyclerViewAddViewManager.getHeaderLinearLayout());
        } else if (viewType == TYPE_EMPTY_VIEW) {
            return new SimpleViewHolder(mRecyclerViewAddViewManager.getCenterLayout());
        } else if (viewType == FOOTER_INIT_INDEX) {//底部增加的View
            return new SimpleViewHolder(mRecyclerViewAddViewManager.getFooterLinearLayout());
        } else if (viewType == TYPE_FOOTER) {
            return new SimpleViewHolder(mRefreshAndLoadMoreManager.getLoadMoreView());
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mRecyclerViewAddViewManager.isHeader(position) || isRefreshHeader(position)) {
            return;
        }
        int adjPosition = position - (mRecyclerViewAddViewManager.getHeadersCount() + 1);
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                adapter.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    /**
     * some times we need to override this
     *
     * @param holder
     * @param position
     * @param payloads
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (mRecyclerViewAddViewManager.isHeader(position) || isRefreshHeader(position)) {
            return;
        }

        int adjPosition = position - (mRecyclerViewAddViewManager.getHeadersCount() + 1);
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                if (payloads.isEmpty()) {
                    if (ZLog.DEBUG) {
                        adapter.onBindViewHolder(holder, adjPosition);
                    } else {
                        try {
                            adapter.onBindViewHolder(holder, adjPosition);
                        } catch (Exception e) {
                            ZLog.e(e, true);
                        }
                    }
                } else {
                    adapter.onBindViewHolder(holder, adjPosition, payloads);
                }
            }
        }
    }


    public int getHeadersCount() {
        return mRecyclerViewAddViewManager.getHeadersCount();
    }

    public void addFooterView(View view) {
        mRecyclerViewAddViewManager.addFooterView(view);
        notifyDataSetChanged();
    }

    public void addFooterView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (paRecyclerViewViewAdapter != null) {
            View loView = LayoutInflater.from(mXRecyclerView.getContext()).inflate(paRecyclerViewViewAdapter.getLayoutResID(), null);
            new ExRecyclerViewViewAdapter(paRecyclerViewViewAdapter, loView);
            addFooterView(loView);
        }
    }

    public void addHeaderView(View view) {
        mRecyclerViewAddViewManager.addHeaderView(view);
        notifyDataSetChanged();
    }

    public void addHeaderView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (paRecyclerViewViewAdapter != null) {
            View loView = LayoutInflater.from(mXRecyclerView.getContext()).inflate(paRecyclerViewViewAdapter.getLayoutResID(), null);
            new ExRecyclerViewViewAdapter(paRecyclerViewViewAdapter, loView);
            addHeaderView(loView);
        }
    }

    public void removeAllHeadViews() {
        mRecyclerViewAddViewManager.removeAllHeadViews();
    }

    public void removeAllFooterViews() {
        mRecyclerViewAddViewManager.removeAllFooterViews();
    }

    @Override
    public int getItemCount() {
        if (mRecyclerViewAddViewManager.isHaveEmptyView()) {//如果有空数据
            return 2;
        }

        int count = (mRefreshAndLoadMoreManager.loadingMoreEnabled() ? 2 : 1);
        int totalCount = mRecyclerViewAddViewManager.getHeadersCount() + count + mRecyclerViewAddViewManager.getFooterCount();
        if (adapter != null) {
            totalCount += adapter.getItemCount();
        }
        return totalCount;
    }

    /**
     * 返回类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if (isRefreshHeader(position)) {//下拉刷新
            return TYPE_REFRESH_HEADER;
        }

        if (mRecyclerViewAddViewManager.isHaveEmptyView()) {//空View类型
            return TYPE_EMPTY_VIEW;
        }

        if (mRecyclerViewAddViewManager.isHeader(position)) {
            position = position - 1;
            return mRecyclerViewAddViewManager.sHeaderTypes.get(position);
        }

        if (mRecyclerViewAddViewManager.isFooter(position) && !mRefreshAndLoadMoreManager.isCanDrag()) {//底部是否增加了View,并且不能不能拖拽才显示空的View
            return FOOTER_INIT_INDEX;
        }

        if (isLoadMore(position)) {//是否有下拉的加载更多
            return TYPE_FOOTER;
        }

        if (adapter != null) {
            int adjPosition = position - (mRecyclerViewAddViewManager.getHeadersCount() + 1);
            int adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                int type = adapter.getItemViewType(adjPosition);
                if (mRecyclerViewAddViewManager.isReservedItemViewType(type)) {
                    throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                }
                return type;
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (adapter != null && position >= mRecyclerViewAddViewManager.getHeadersCount() + 1) {
            int adjPosition = position - (mRecyclerViewAddViewManager.getHeadersCount() + 1);
            if (adjPosition < adapter.getItemCount()) {
                return adapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mRecyclerViewAddViewManager.isHeader(position) || isLoadMore(position) || isRefreshHeader(position)) ? gridManager.getSpanCount() : 1;
                }
            });
        }
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    public void destroy() {
        mRecyclerViewAddViewManager.destroy();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (mRecyclerViewAddViewManager.isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isLoadMore(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        adapter.onViewRecycled(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return adapter.onFailedToRecycleView(holder);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void showCenterView(View emptyView) {
        mRecyclerViewAddViewManager.showCenterView(emptyView);
    }

    public void addCenterView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (paRecyclerViewViewAdapter != null) {
            View loView = LayoutInflater.from(mXRecyclerView.getContext()).inflate(paRecyclerViewViewAdapter.getLayoutResID(), null);
            new ExRecyclerViewViewAdapter(paRecyclerViewViewAdapter, loView);
            mCenterViewHashMap.put(paRecyclerViewViewAdapter.getViewType(), loView);
        }
    }

    /**
     * 显示指定类型的View
     *
     * @param ViewType
     */
    public void setShowCenterView(int ViewType) {
        View loView = mCenterViewHashMap.get(ViewType);
        if (loView == null) {//如果没有找到对应类型的View,那就显示null
            showCenterView(null);
        } else {
            showCenterView(loView);
        }
    }

    public void setNoDateResID(int noDateResID1) {
        noDateResID = noDateResID1;
    }

    /**
     * 如果检测有没有数据，如果没有数据就显示没有数据的View在中间
     */
    public void noDataShowNoDataView() {

        RecyclerView.Adapter originalAdapter = getOriginalAdapter();
        if (originalAdapter != null) {
            if (mRecyclerViewAddViewManager.getHeadersCount() > 0 || originalAdapter.getItemCount() > 0) {
                showCenterView(null);
            } else {
                View loView = mCenterViewHashMap.get(XRecyclerView.CENTERVIEW_TYPE_NO_DATA);//获得空View

                if (loView == null && noDateResID != -1) {
                    try {
                        loView = LayoutInflater.from(mXRecyclerView.getContext()).inflate(noDateResID, null);
                        mCenterViewHashMap.put(XRecyclerView.CENTERVIEW_TYPE_NO_DATA, loView);
                    } catch (Exception e) {
                        ZLog.e(e);
                    }
                }

                if (loView != null) {
                    showCenterView(loView);
                }
            }
        }

    }
}