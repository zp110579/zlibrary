package com.zee.bean;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zee.adapter.BaseMultiZAdapter;
import com.zee.adapter.ItemChildClickListener;
import com.zee.adapter.OnItemClickListener;
import com.zee.adapter.OnItemViewClickListener;
import com.zee.base.OnNoDoubleClickListener;
import com.zee.utils.UIUtils;
import com.zee.view.ZxRecyclerView;

@SuppressWarnings(value = {"unchecked", "deprecation"})
public abstract class MultiItemAdapter {
    private Object mObject;
    private RecyclerViewHolder mViewHolder;
    private BaseMultiZAdapter mBaseMultiZAdapter;
    private int curIndex;
    private OnItemViewClickListener mOnItemViewClickListener;

    private OnItemClickListener mItemClickListener;


    public abstract @LayoutRes
    int getLayoutResID();

    public final void bindObject(BaseMultiZAdapter adapter, RecyclerViewHolder viewHolder, int location, Object object) {
        this.mBaseMultiZAdapter = adapter;
        mObject = object;
        curIndex = location;
        mViewHolder = viewHolder;
        initViews(viewHolder.getConvertView(), location);
        checkItemClickListener(object, location);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getBean() {
        return (T) mObject;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public abstract void initViews(View parentView, int location);

    protected boolean isFirstItem() {
        return curIndex == 0;
    }

    public boolean isLastItem() {
        return (curIndex == getAdapter().getList().size() - 1);
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    protected final ImageView findImageViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    protected final TextView findTextViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    protected final ZxRecyclerView findZxRecyclerViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    protected final View findViewByIdK(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    public final void setText(@IdRes int id, CharSequence text) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public final void setText(@IdRes int id, @StringRes int resid) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setText(resid);
        }
    }

    public void setTextColor(@IdRes int resID, @ColorRes int textColor) {
        TextView textView = findViewById(resID);
        if (textView != null) {
            textView.setTextColor(UIUtils.getColor(textColor));
        }
    }

    public void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
        findViewByIdK(id).setOnClickListener(onClickListener);
    }

    public final void setGone(@IdRes int id) {
        setVisibility(id, View.GONE);
    }

    public final void setInVisible(@IdRes int id) {
        setVisibility(id, View.INVISIBLE);
    }

    public void setVisibleOrInVisible(boolean isVisible, @IdRes int id) {
        View view = findViewById(id);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void setVisibleOrGone(boolean isVisible, @IdRes int id) {
        View view = findViewById(id);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public final void setVisible(@IdRes int id) {
        setVisibility(id, View.VISIBLE);
    }

    private void setVisibility(@IdRes int id, int visible) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    public final void startActivity(Class<?> paClass) {
        UIUtils.startActivity(paClass);
    }

    public final void startActivity(Intent paIntent) {
        UIUtils.startActivity(paIntent);
    }


    public void setOnItemClickListener(final View view) {
        final int index = mBaseMultiZAdapter.getCurIndex();
        final Object mObject = mBaseMultiZAdapter.getBean();
        view.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOnItemViewClickListener != null) {
                    mOnItemViewClickListener.onItemClick(mObject, view, index);
                }
            }
        });
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        mOnItemViewClickListener = onItemViewClickListener;
    }

    public void setOnItemClickListener(@IdRes int viewId) {
        final View view = findViewById(viewId);
        if (view != null) {
            setOnItemClickListener(view);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    //检测是否item监听
    private void checkItemClickListener(final Object object, final int location) {
        final OnItemClickListener listener = mItemClickListener;
        if (mViewHolder != null) {
            mViewHolder.getConvertView().setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(object, location);
                    }
                }
            });
        }
    }


    protected Context getContext() {
        return mViewHolder.getContext();
    }

    public final BaseMultiZAdapter getAdapter() {
        return mBaseMultiZAdapter;
    }

    public final void notifyDataSetChanged() {
        mBaseMultiZAdapter.notifyDataSetChanged();
    }

    public final void notifyDataSetChanged(int position) {
        mBaseMultiZAdapter.notifyItemChanged(position);
    }
}
