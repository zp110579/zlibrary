package com.zee.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.zee.adapter.RecyclerViewViewAdapter
import com.zee.adapter.ZxBannerAdapter
import com.zee.base.Z1RecyclerAdapter
import com.zee.recyclerview.XRecyclerView
import com.zee.utils.UIUtils
import com.zee.utils.ZListUtils

/**
 * @author Administrator
 */
class Zx1RecyclerView : XRecyclerView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    fun addHeaderView(view: View) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addHeaderView(view)
        }
    }

    fun addHeaderView(paRecyclerViewViewAdapter: RecyclerViewViewAdapter) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addHeaderView(paRecyclerViewViewAdapter)
        }
    }

    fun removeAllHeadViews() {
        if (mWrapAdapter != null) {
            mWrapAdapter.removeAllHeadViews()
        }
    }

    fun addFooterView(view: View) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addFooterView(view)
        }
    }

    fun addFooterView(paRecyclerViewViewAdapter: RecyclerViewViewAdapter) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addFooterView(paRecyclerViewViewAdapter)
        }
    }

    fun removeAllFooterViews() {
        if (mWrapAdapter != null) {
            mWrapAdapter.removeAllFooterViews()
        }
    }

    fun addFootEmptyView(dpValue: Int): View {
        val view = getEmptyView(dpValue)
        addFooterView(view)
        return view
    }

    fun addHeadEmptyView(dpValue: Int): View {
        val view = getEmptyView(dpValue)
        addHeaderView(view)
        return view
    }

    private fun getEmptyView(dpValue: Int): View {
        val refreshAndLoadMoreManager = refreshAndLoadMoreManager
        val textView = View(context)
        var layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dpToPx(dpValue))
        if (refreshAndLoadMoreManager.linearLayoutManagerOrientation == 1) {
            layoutParams = ViewGroup.LayoutParams(UIUtils.dpToPx(dpValue), ViewGroup.LayoutParams.MATCH_PARENT)
        }
        textView.layoutParams = layoutParams
        return textView
    }

    /**
     * 将BannView添加到头部
     *
     * @param dpValue
     * @param bannerAdapter
     * @return
     */
    private fun addBannerViewToHead(dpValue: Int, bannerAdapter: ZxBannerAdapter<*>): ZxBannerView {
        val bannerView = ZxBannerView(context)
        val refreshAndLoadMoreManager = refreshAndLoadMoreManager
        var layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dpToPx(dpValue))
        if (refreshAndLoadMoreManager.linearLayoutManagerOrientation == 1) {
            layoutParams = ViewGroup.LayoutParams(UIUtils.dpToPx(dpValue), ViewGroup.LayoutParams.MATCH_PARENT)
        }
        bannerView.layoutParams = layoutParams
        bannerView.setBannerAdapter(bannerAdapter)
        addHeaderView(bannerView)
        return bannerView
    }

    fun notifyItemChanged() {
        val adapter = baseRVAdapter
        adapter?.notifyDataSetChanged()
    }

    /**
     * @param list
     * @param pageSize 每页加载的最多的数据
     */
    fun setRefreshAndLoadMoreList(list: List<*>, pageSize: Int = -1) {
        val refreshAndLoadMoreAdapter = refreshAndLoadMoreManager.refreshAndLoadMoreAdapter
        if (refreshAndLoadMoreAdapter != null) {
            if (refreshAndLoadMoreAdapter.isRefreshState) {
                setList(list)
            } else {
                addAll(list)
            }
            if (pageSize > 0 && ZListUtils.isNoEmpty(list)) {
                if (list.size < pageSize) {
                    setNoMore(true)
                }
            }
        } else {
            setList(list)
        }
    }


    fun notifyItemChanged(position: Int) {
        val adapter = baseRVAdapter
        adapter?.notifyItemChanged(position)
    }
}