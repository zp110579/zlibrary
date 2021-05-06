package com.zee.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zee.view.ZxRecyclerView;

import org.jetbrains.annotations.NotNull;

public abstract class RecyclerViewViewAdapter {
    private ExRecyclerViewViewAdapter mExRecyclerViewViewAdapter;
    private int viewType;

    public RecyclerViewViewAdapter() {
    }

    public RecyclerViewViewAdapter(int viewType) {
        this.viewType = viewType;
    }

    public abstract @LayoutRes
    int getLayoutResID();

    void setExRecyclerViewViewAdapter(ExRecyclerViewViewAdapter paExRecyclerViewViewAdapter) {
        mExRecyclerViewViewAdapter = paExRecyclerViewViewAdapter;
    }

    public void initViews() {
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return mExRecyclerViewViewAdapter.findViewById(id);
    }

    @Deprecated
    protected final View findViewByIdK(@IdRes int id) {
        return findViewById(id);
    }

    @Deprecated
    protected final ImageView findImageViewById(@IdRes int id) {
        return findViewById(id);
    }

    @Deprecated
    protected final ZxRecyclerView findZxRecyclerViewById(@IdRes int id) {
        return findViewById(id);
    }

    @Deprecated
    protected final TextView findTextViewById(@IdRes int id) {
        return findViewById(id);
    }

    protected final View viewById(@IdRes int id) {
        return findViewById(id);
    }

    @NotNull
    protected final ImageView imageViewById(@IdRes int id) {
        return findViewById(id);
    }

    @NotNull
    protected final TextView textViewById(@IdRes int id) {
        return findViewById(id);
    }

    @NotNull
    protected final EditText editTextById(@IdRes int id) {
        return findViewById(id);
    }

    protected final ZxRecyclerView recyclerViewById(@IdRes int id) {
        return findViewById(id);
    }

    protected void setText(@IdRes int id, CharSequence character) {
        TextView textView = findViewById(id);
        textView.setText(character);
    }

    protected final void setVisible(@IdRes int... ids) {
        setVisibility(View.VISIBLE, ids);
    }

    protected final void setInVisible(@IdRes int... ids) {
        setVisibility(View.INVISIBLE, ids);
    }

    protected final void setGone(@IdRes int... ids) {
        setVisibility(View.GONE, ids);
    }

    private void setVisibility(int visible, @IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            view.setVisibility(visible);
        }
    }

    protected void setVisibleOrInVisible(boolean isVisible, @IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    protected void setVisibleOrGone(boolean isVisible, @IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    protected final void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
        findViewByIdK(id).setOnClickListener(onClickListener);
    }

    public void setViewType(int paViewType) {
        viewType = paViewType;
    }

    public int getViewType() {
        return viewType;
    }
}
