package com.zee.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理头部，中间，底部增加View
 */
class RecyclerViewAddViewManager {
    //空数据
    private FrameLayout mCenterLayout;//空

    private LinearLayout mHeaderLinearLayout;//顶部View父容器
    private LinearLayout mFooterLinearLayout;//底部View父容器
    static List<Integer> sHeaderTypes = new ArrayList<>();
    private WrapAdapter mWrapAdapter;
    private XRecyclerView mXRecyclerView;

    RecyclerViewAddViewManager(Context context, WrapAdapter adapter) {
        //顶部增加View
        this.mWrapAdapter = adapter;
        mXRecyclerView = adapter.getXRecyclerView();
        initHeadViewFather(context);
        //底部增加View
        initFootViewFather(context);
    }

    private void initHeadViewFather(Context paContext) {
        mHeaderLinearLayout = new LinearLayout(paContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        RefreshAndLoadMoreManager refreshAndLoadMoreManager = mXRecyclerView.getRefreshAndLoadMoreManager();
        mHeaderLinearLayout.setOrientation(LinearLayout.VERTICAL);
        if (refreshAndLoadMoreManager.getLinearLayoutManagerOrientation() == 1) {
            layoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
            mHeaderLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
        mHeaderLinearLayout.setLayoutParams(layoutParams);
        mHeaderLinearLayout.setGravity(Gravity.CENTER);
    }

    private void initFootViewFather(Context paContext) {
        mFooterLinearLayout = new LinearLayout(paContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        RefreshAndLoadMoreManager refreshAndLoadMoreManager = mXRecyclerView.getRefreshAndLoadMoreManager();
        mFooterLinearLayout.setOrientation(LinearLayout.VERTICAL);
        if (refreshAndLoadMoreManager.getLinearLayoutManagerOrientation() == 1) {
            layoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
            mFooterLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
        mFooterLinearLayout.setLayoutParams(layoutParams);
        mFooterLinearLayout.setGravity(Gravity.CENTER);
    }

    /**
     * 顶部是否有添加的View
     *
     * @param position
     * @return
     */
    boolean isHeader(int position) {
        if (mHeaderLinearLayout == null) {
            return false;
        }
        int headViewSize = 0;
        if (mHeaderLinearLayout.getChildCount() > 0) {
            headViewSize = 1;
        }
        return position >= 1 && position < headViewSize + 1;
    }

    boolean isFooter(int position) {
        if (mFooterLinearLayout.getChildCount() > 0) {
            return position == mWrapAdapter.getItemCount() - 1;
        }
        return false;
    }

    int getHeadersCount() {
        if (mHeaderLinearLayout == null) {
            return 0;
        }

        int headViewSize = 0;
        if (mHeaderLinearLayout.getChildCount() > 0) {
            headViewSize = 1;
        }
        return headViewSize;
    }

    int getFooterCount() {
        int size = 0;
        if (mFooterLinearLayout.getChildCount() > 0) {
            size = 1;
        }
        return size;
    }

    /**
     * 判断一个type是否为HeaderType
     *
     * @param itemViewType
     * @return
     */
    boolean isHeaderType(int itemViewType) {
        return sHeaderTypes.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    /**
     * 判断是否是XRecyclerView保留的itemViewType
     *
     * @param itemViewType
     * @return
     */
    boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == WrapAdapter.TYPE_REFRESH_HEADER || itemViewType == WrapAdapter.TYPE_FOOTER || sHeaderTypes.contains(itemViewType)) {
            return true;
        }
        return false;
    }

    public LinearLayout getFooterLinearLayout() {
        return mFooterLinearLayout;
    }

    public LinearLayout getHeaderLinearLayout() {
        return mHeaderLinearLayout;
    }

    public FrameLayout getCenterLayout() {
        return mCenterLayout;
    }

    public void addHeaderView(View view) {
        mHeaderLinearLayout.addView(view);
        sHeaderTypes.add(WrapAdapter.HEADER_INIT_INDEX + 1);
    }

    public void addFooterView(View view) {
        mFooterLinearLayout.addView(view);
        sHeaderTypes.add(WrapAdapter.FOOTER_INIT_INDEX + 1);
    }

    /**
     * 是否有空View需要显示
     *
     * @return
     */
    boolean isHaveEmptyView() {
        if (mCenterLayout != null && mCenterLayout.getChildCount() > 0) {//如果显示空白的View，其他的都不需要在显示
            return true;
        }
        return false;
    }

    /**
     * 如果RecyclerView有数据显示，那中间的书
     *
     * @param emptyView
     */
    public void showCenterView(View emptyView) {
        if (mCenterLayout == null && emptyView != null) {
            mCenterLayout = new FrameLayout(emptyView.getContext());
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mCenterLayout.setLayoutParams(layoutParams);
        }
        if (mCenterLayout != null) {
            mCenterLayout.removeAllViews();
            if (emptyView != null) {//只有不为空的时候才会显示出来
                mCenterLayout.addView(emptyView);
            }
        }
    }


    public void destroy() {
//        if (mHeaderLinearLayout != null) {
//            mHeaderLinearLayout.removeAllViews();
//            mHeaderLinearLayout = null;
//        }
    }

    public void removeAllHeadViews() {
        if (mHeaderLinearLayout != null) {
            mHeaderLinearLayout.removeAllViews();
        }
    }

    public void removeAllFooterViews() {
        if (mFooterLinearLayout != null) {
            mFooterLinearLayout.removeAllViews();
        }
    }

}
