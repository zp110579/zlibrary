package com.zee.base;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zee.bean.RecyclerViewHolder;
import com.zee.recyclerview.XRecyclerView;
import com.zee.utils.ZListUtils;
import com.zee.view.ZxRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zee on 2021/3/4.
 */
public abstract class BaseRVAdapter<BT> extends RecyclerView.Adapter<RecyclerViewHolder> {
    List<BT> mList = new ArrayList();
    RecyclerViewHolder mViewHolder;
    XRecyclerView mXRecyclerView;
    //设置选择的行
    private int mSelectItemIndex = 0;

    int curIndex;
    private @LayoutRes
    int layoutResID;

    public void setSelectItem(int selectItemIndex) {
        this.mSelectItemIndex = selectItemIndex;
    }

    public int getSelectItemIndex() {
        return mSelectItemIndex;
    }

    public List<BT> getList() {
        return mList;
    }

    public BT get(int index) {
        return mList.get(index);
    }

    public void remove(int index) {
        if (ZListUtils.isNoEmpty(mList)) {
            mList.remove(index);
            notifyDataSetChanged();
        }
    }

    public void remove(BT bean) {
        if (ZListUtils.isNoEmpty(mList)) {
            mList.remove(bean);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<BT> paList) {
        if (ZListUtils.isEmpty(paList)) {
            if (mXRecyclerView != null) {
                mXRecyclerView.setLoadMoreEnabled(true);
                mXRecyclerView.loadFinish();
            }
            return;
        }
        if (mList == null) {
            mList = new ArrayList();
        }
        mList.addAll(paList);
        if (mXRecyclerView != null) {
            mXRecyclerView.loadFinish();
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    protected boolean isFirstItem() {
        return curIndex == 0;
    }

    public boolean isLastItem() {
        return (curIndex == mList.size() - 1);
    }

    public int size() {
        if (ZListUtils.isNoEmpty(mList)) {
            return mList.size();
        }
        return 0;
    }

    public void setList(List<BT> list) {
        setList(list, true);
    }

    /**
     * 仅仅设置List，单不检测是否有数据，显示没有数据界面
     *
     * @param list
     * @param isCheckNoData
     */
    public void setList(List<BT> list, boolean isCheckNoData) {
        if (list != null) {
            mList = list;
        }else {
            mList.clear();
        }
        if (isCheckNoData) {
            if (mXRecyclerView != null) {
                mXRecyclerView.loadFinish();
            }
            notifyDataSetChanged();
        }
    }

    protected View setVisibleOrInVisible(boolean isVisible, @IdRes int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids[i]);
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        }
        return findViewById(ids[0]); //只返回第一个View
    }

    protected View setVisibleOrGone(boolean isVisible, @IdRes int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids[i]);
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
        return findViewById(ids[0]);
    }

    public final ImageView setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = imageViewById(viewId);
        view.setImageResource(imageResId);
        return view;
    }

    protected TextView setText(@IdRes int id, @StringRes int resId) {
        TextView textView = findViewById(id);

        if (textView != null) {
            textView.setText(resId);
        }
        return textView;
    }

    protected TextView setText(@IdRes int id, CharSequence character) {
        TextView textView = findViewById(id);
        textView.setText(character);
        return textView;
    }

    protected TextView setText(@IdRes int id, double character) {
        TextView textView = findViewById(id);
        textView.setText("" + character);
        return textView;
    }

    @NotNull
    protected final <T extends View> T findViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @NotNull
    protected final View findViewByIdK(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @Deprecated
    protected final ImageView findImageViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @Deprecated
    protected final ZxRecyclerView findZxRecyclerViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @Deprecated
    protected final TextView findTextViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @NotNull
    protected final View viewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @NotNull
    protected final TextView textViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @NotNull
    protected final ImageView imageViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    protected final ZxRecyclerView recyclerViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    protected final void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
        findViewByIdK(id).setOnClickListener(onClickListener);
    }

    void setLayoutResID(int layoutResID) {
        this.layoutResID = layoutResID;
    }

    public void setXRecyclerView(XRecyclerView XRecyclerView) {
        mXRecyclerView = XRecyclerView;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false), null);
    }

    protected int getItemLayoutId(int viewType) {
        return layoutResID;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int position) {
        curIndex = position;
        mViewHolder = viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

}
