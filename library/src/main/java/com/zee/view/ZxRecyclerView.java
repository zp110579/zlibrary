package com.zee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zee.adapter.RecyclerViewViewAdapter;
import com.zee.adapter.ZxBannerAdapter;
import com.zee.base.BaseRVAdapter;
import com.zee.recyclerview.RefreshAndLoadMoreAdapter;
import com.zee.recyclerview.RefreshAndLoadMoreManager;
import com.zee.recyclerview.XRecyclerView;
import com.zee.utils.UIUtils;
import com.zee.utils.ZListUtils;

import java.util.List;


/**
 * @author Administrator
 */
public class ZxRecyclerView extends XRecyclerView {

    public ZxRecyclerView(Context context) {
        super(context);
    }

    public ZxRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZxRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addHeaderView(view);
        }
    }

    public void addHeaderView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addHeaderView(paRecyclerViewViewAdapter);
        }
    }

    public void removeAllHeadViews() {
        if (mWrapAdapter != null) {
            mWrapAdapter.removeAllHeadViews();
        }
    }

    public void addFooterView(View view) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addFooterView(view);
        }
    }

    public void addFooterView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addFooterView(paRecyclerViewViewAdapter);
        }
    }

    public void removeAllFooterViews() {
        if (mWrapAdapter != null) {
            mWrapAdapter.removeAllFooterViews();
        }
    }

    public View addFootEmptyView(int dpValue) {
        View view = getEmptyView(dpValue);
        addFooterView(view);
        return view;
    }

    public View addHeadEmptyView(int dpValue) {
        View view = getEmptyView(dpValue);
        addHeaderView(view);
        return view;
    }

    private View getEmptyView(int dpValue) {
        RefreshAndLoadMoreManager refreshAndLoadMoreManager = getRefreshAndLoadMoreManager();

        View textView = new View(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dpToPx(dpValue));
        if (refreshAndLoadMoreManager.getLinearLayoutManagerOrientation() == 1) {
            layoutParams = new ViewGroup.LayoutParams(UIUtils.dpToPx(dpValue), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    /**
     * 将BannView添加到头部
     *
     * @param dpValue
     * @param bannerAdapter
     * @return
     */
    private ZxBannerView addBannerViewToHead(int dpValue, ZxBannerAdapter bannerAdapter) {
        ZxBannerView bannerView = new ZxBannerView(getContext());
        RefreshAndLoadMoreManager refreshAndLoadMoreManager = getRefreshAndLoadMoreManager();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dpToPx(dpValue));
        if (refreshAndLoadMoreManager.getLinearLayoutManagerOrientation() == 1) {
            layoutParams = new ViewGroup.LayoutParams(UIUtils.dpToPx(dpValue), ViewGroup.LayoutParams.MATCH_PARENT);
        }

        bannerView.setLayoutParams(layoutParams);
        bannerView.setBannerAdapter(bannerAdapter);
        addHeaderView(bannerView);
        return bannerView;
    }

    public final void notifyItemChanged() {
        BaseRVAdapter adapter = getBaseRVAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 和刷新和加载绑定的List
     */
    public void setListRefreshAndLoadMore(List list) {
        setRefreshAndLoadMoreList(list, -1);
    }

    public void setRefreshAndLoadMoreList(List list) {
        setRefreshAndLoadMoreList(list, -1);
    }

    /**
     * @param list
     * @param pageSize 每页加载的最多的数据
     */
    public void setRefreshAndLoadMoreList(List list, int pageSize) {
        RefreshAndLoadMoreAdapter refreshAndLoadMoreAdapter = getRefreshAndLoadMoreManager().getRefreshAndLoadMoreAdapter();
        if (refreshAndLoadMoreAdapter != null) {
            if (refreshAndLoadMoreAdapter.isRefreshState()) {
                setList(list);
            } else {
                addAll(list);
            }
            if (pageSize > 0 && ZListUtils.isNoEmpty(list)) {
                if (list.size() < pageSize) {
                    setLoadMoreEnabled(false);
                }
            }
        } else {
            setList(list);
        }
    }

    public final void notifyItemChanged(int position) {
        BaseRVAdapter adapter = getBaseRVAdapter();
        if (adapter != null) {
            adapter.notifyItemChanged(position);
        }
    }
}
