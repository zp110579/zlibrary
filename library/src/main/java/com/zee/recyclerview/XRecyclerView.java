package com.zee.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zee.adapter.RecyclerViewViewAdapter;
import com.zee.base.BaseRVAdapter;
import com.zee.libs.R;
import com.zee.listener.ScrollAlphaChangeListener;
import com.zee.log.ZLog;

import java.util.HashMap;
import java.util.List;


/**
 * @author Administrator
 */
public class XRecyclerView extends RecyclerView implements IRecyclerView {
    protected WrapAdapter mWrapAdapter;
    public static final int CENTERVIEW_TYPE_NO_DATA = 10000000;//没有数据
    public static final int CENTERVIEW_TYPE_NO_NET = 10000001;//没有网络
    private RefreshAndLoadMoreManager mRefreshAndLoadMoreManager;
    private boolean isCanRefresh = true;

    private HashMap<Integer, View> mCenterViewHashMap = new HashMap<>();

    private HintText mHintText = new HintText();

    private ScrollAlphaChangeListener scrollAlphaChangeListener;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver(this);
    private XRecyclerViewParamManager mXRecyclerViewParamManager;


    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxRecyclerView);
        int noDateResID = ta.getResourceId(R.styleable.ZxRecyclerView_zv_no_date_layout, R.layout.zv_recyclerview_nodata);
        boolean isShowNoData = ta.getBoolean(R.styleable.ZxRecyclerView_zv_is_show_no_date, true);
        mXRecyclerViewParamManager = new XRecyclerViewParamManager(ta, this);
        mRefreshAndLoadMoreManager = new RefreshAndLoadMoreManager(this, mXRecyclerViewParamManager.getLinearLayoutManagerOrientation());
        mWrapAdapter = new WrapAdapter(this);
        if (isShowNoData) {
            mWrapAdapter.setNoDateResID(noDateResID);
        }
        setItemViewCacheSize(5);//设置5缓存,默认2个
    }

    public RefreshAndLoadMoreManager getRefreshAndLoadMoreManager() {
        return mRefreshAndLoadMoreManager;
    }

    /**
     * 获得提示信息
     */
    public HintText getHintText() {
        return mHintText;
    }

    /**
     * 设置顶部
     */
    public void setRefreshView(IRecyclerViewRefreshView refreshView) {
        mRefreshAndLoadMoreManager.setRefreshView(refreshView);
    }

    public void setLoadMoreView(IRecyclerViewLoadMoreView iRecyclerViewLoadMoreView) {
        mRefreshAndLoadMoreManager.setLoadMoreView(iRecyclerViewLoadMoreView);
    }

    public void setList(List list) {
        BaseRVAdapter baseRVAdapter = getBaseRVAdapter();
        baseRVAdapter.setList(list);
    }

    public void addAll(List paList) {
        BaseRVAdapter baseRVAdapter = getBaseRVAdapter();
        baseRVAdapter.addAll(paList);
    }

    public void addCenterView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addCenterView(paRecyclerViewViewAdapter);
        }
    }

    /**
     * 显示指定类型的View
     *
     * @param ViewType
     */
    public void setShowCenterView(int ViewType) {
        if (mWrapAdapter != null) {
            mWrapAdapter.setShowCenterView(ViewType);
        }
    }

    public void setEmptyView(RecyclerViewViewAdapter paRecyclerViewViewAdapter) {
        if (paRecyclerViewViewAdapter != null) {
            paRecyclerViewViewAdapter.setViewType(CENTERVIEW_TYPE_NO_DATA);//强制改成成空ViewType
            addCenterView(paRecyclerViewViewAdapter);
        }
    }

    /**
     * 加载结束
     * 包括刷新结束，加载更多结束
     */
    public void loadFinish() {

        mRefreshAndLoadMoreManager.loadFinish();
        noDataShowNoDataView();//检测是不是没有数据。
    }

    /**
     * 设置是否可以加载更多
     *
     * @param noMore
     */
    public void setNoMore(boolean noMore) {
        mRefreshAndLoadMoreManager.setNoMore(noMore);
    }

    public void refresh() {
        checkAdapter();
        mRefreshAndLoadMoreManager.refresh();
    }

    /**
     * 监听刷新
     *
     * @param listener
     */
    public void setRefreshListener(RefreshListener listener) {
        isCanRefresh = false;
        if (listener != null) {
            isCanRefresh = true;
            mRefreshAndLoadMoreManager.setRefreshListener(listener);
        }
    }

    /**
     * 加载更多
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mRefreshAndLoadMoreManager.setLoadMoreListener(listener);
    }

    public void setRefreshAndLoadMordListener(RefreshAndLoadMoreAdapter listener) {
        isCanRefresh = false;
        if (listener != null) {
            isCanRefresh = true;
            mRefreshAndLoadMoreManager.setRefreshAndLoadMordListener(listener);
        }
    }

    public void setRefreshEnabled(boolean isEnable) {
        mRefreshAndLoadMoreManager.setRefreshEnabled(isEnable);
    }

    public void setLoadMoreEnabled(boolean isEnable) {
        mRefreshAndLoadMoreManager.setLoadMoreEnabled(isEnable);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter tempAdapter = getAdapter();
        if (tempAdapter == null) {
            mWrapAdapter.setAdapter(adapter);
            super.setAdapter(mWrapAdapter);
            adapter.registerAdapterDataObserver(mDataObserver);
            if (adapter instanceof BaseRVAdapter) {
                BaseRVAdapter adapter1 = (BaseRVAdapter) adapter;
                adapter1.setXRecyclerView(this);
            }
            mDataObserver.onChanged();
        } else if (adapter != null && (adapter instanceof BaseRVAdapter) && (tempAdapter instanceof BaseRVAdapter)) {
            BaseRVAdapter baseRVAdapter = (BaseRVAdapter) tempAdapter;
            baseRVAdapter.setList(((BaseRVAdapter) adapter).getList());
        } else {
            try {
                throw new Exception("需要自己更新数据");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null) {
            return mWrapAdapter.getOriginalAdapter();
        }
        return null;
    }

    public BaseRVAdapter getBaseRVAdapter() {
        Adapter adapter = getAdapter();
        if (adapter == null) {
            ZLog.e("adapter is Null");
        }
        if (adapter instanceof BaseRVAdapter) {
            return (BaseRVAdapter) adapter;
        } else {
            ZLog.e("adapter is not BaseRVAdapter");
        }
        return null;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            mWrapAdapter.setLayoutManager(layout);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mRefreshAndLoadMoreManager.isCanDrag()) {
            LayoutManager layoutManager = getLayoutManager();
            mRefreshAndLoadMoreManager.dragLoadMoreView(layoutManager, mWrapAdapter.getHeadersCount() + 1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanRefresh) {
            boolean isCustom = mRefreshAndLoadMoreManager.onMove(ev);
            if (!isCustom) {
                return false;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 如果检测有没有数据，如果没有数据就显示没有数据的View在中间
     */
    public void noDataShowNoDataView() {
        if (mWrapAdapter != null) {
            mWrapAdapter.noDataShowNoDataView();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRefreshAndLoadMoreManager != null) {
            mRefreshAndLoadMoreManager.onAttachedToWindow(getParent());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    /**
     * add by LinGuanHong below
     */
    private int scrollDyCounter = 0;

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        if (position == 0) {
            scrollDyCounter = 0;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (scrollAlphaChangeListener == null) {
            return;
        }
        int height = scrollAlphaChangeListener.setLimitHeight();
        scrollDyCounter = scrollDyCounter + dy;
        if (scrollDyCounter <= 0) {
            scrollAlphaChangeListener.onAlphaChange(0);
        } else if (scrollDyCounter <= height && scrollDyCounter > 0) {
            /** 255/height = x/255 */
            float scale = (float) scrollDyCounter / height;
            float alpha = (255 * scale);
            scrollAlphaChangeListener.onAlphaChange((int) alpha);
        } else {
            scrollAlphaChangeListener.onAlphaChange(255);
        }
    }

    public void setScrollAlphaChangeListener(ScrollAlphaChangeListener scrollAlphaChangeListener) {
        this.scrollAlphaChangeListener = scrollAlphaChangeListener;
    }

    public void destroy() {
        mWrapAdapter.destroy();
//        mRefreshAndLoadMoreManager.onDestroy();
    }

    private void checkAdapter() {
        Adapter adapter = getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("please setAdapter first");
        }
    }
}