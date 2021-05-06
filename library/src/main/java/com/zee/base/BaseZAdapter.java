package com.zee.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zee.adapter.OnItemClickListener;
import com.zee.adapter.OnItemViewClickListener;
import com.zee.bean.RecyclerViewHolder;
import com.zee.utils.UIUtils;

import java.util.List;

@SuppressWarnings(value = {"unchecked", "deprecation"})
public abstract class BaseZAdapter<BT> extends BaseRVAdapter<BT> {

    private OnItemClickListener mItemClickListener;
    private OnItemViewClickListener mOnItemViewClickListener;


    public BaseZAdapter() {
        setLayoutResID(getLayoutResID());
    }

    public BaseZAdapter(List list) {
        mList = list;
        setLayoutResID(getLayoutResID());
    }


    public abstract @LayoutRes
    int getLayoutResID();

    @SuppressWarnings("unchecked")
    public final BT getBean() {
        final BT bean = get(curIndex);
        return bean;
    }

    protected void setTextColor(@IdRes int id, @ColorRes int textColor) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setTextColor(UIUtils.getColor(textColor));
        }
    }

    protected void setTextColor(@IdRes int id, String textColor) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setTextColor(Color.parseColor(textColor));
        }
    }

    protected final void setVisible(@IdRes int id) {
        setVisibility(id, View.VISIBLE);
    }

    protected final void setInVisible(@IdRes int id) {
        setVisibility(id, View.INVISIBLE);
    }

    protected final void setGone(@IdRes int id) {
        setVisibility(id, View.GONE);
    }

    protected void setVisibility(@IdRes int id, int visible) {
        View view = findViewById(id);
        view.setVisibility(visible);
    }


    protected void setItemBackgroundColor(@ColorRes int id) {
        mViewHolder.getConvertView().setBackgroundColor(getColor(id));
    }

    protected void setItemBackgroundColor(String color) {
        mViewHolder.getConvertView().setBackgroundColor(Color.parseColor(color));
    }

    protected final int getColor(@ColorRes int id) throws Resources.NotFoundException {
        return UIUtils.getColor(id);
    }

    protected final void setRootViewOnClickListener(final View.OnClickListener onClickListener) {
        mViewHolder.getConvertView().setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onClickListener.onClick(v);
            }
        });
    }

    protected final void setOnClickListenerAndNotifyDataSetChanged(@IdRes int id, View.OnClickListener onClickListener) {
        setOnClickListener(id, onClickListener);
        notifyDataSetChanged();
    }

    public final void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    protected final void addOnItemViewClickListener(@IdRes int id) {
        View loView = findViewById(id);
        setOnItemClickListener(loView);
    }

    protected final void setOnItemClickListener(View view) {
        final BT bean = getBean();
        final int index = curIndex;
        view.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOnItemViewClickListener != null) {
                    mOnItemViewClickListener.onItemClick(bean, v, index);
                }
            }
        });
    }

    public final void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        mOnItemViewClickListener = onItemViewClickListener;
    }

    protected final void startActivity(Class<?> paClass) {
        UIUtils.startActivity(paClass);
    }

    protected final Context getContext() {
        return mViewHolder.getContext();
    }

    protected final void startActivity(Intent paIntent) {
        UIUtils.startActivity(paIntent);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        initViews(viewHolder.getConvertView(), position);
        if (mItemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    BT t = mList.get(position);
                    mItemClickListener.onItemClick(t, position);
                }
            });
        }
    }

    protected abstract void initViews(View parentView, int location);

}
