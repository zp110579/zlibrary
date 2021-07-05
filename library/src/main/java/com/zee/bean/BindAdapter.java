package com.zee.bean;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zee.base.OnNoDoubleClickListener;
import com.zee.utils.UIUtils;
import com.zee.view.ZxRecyclerView;

public abstract class BindAdapter {
    private transient View mView;

    public final void setView(View paView) {
        this.mView = paView;
        initViews(mView);
    }

    public abstract void initViews(View paView);

    public final <T extends View> T findViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Deprecated
    public final View findViewByIdK(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Deprecated
    public final ImageView findImageViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Deprecated
    public final ZxRecyclerView findZxRecyclerView(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Deprecated
    public final TextView findTextViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public final TextView textViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public final ViewPager viewPagerById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public final EditText editTextById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public final ZxRecyclerView recyclerViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public final ImageView imageViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public final View viewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    public void setText(@IdRes int resID, @StringRes int resId) {
        TextView textView = findViewById(resID);

        if (textView != null) {
            String text = textView.getContext().getString(resId);
            textView.setText(text);
        }
    }

    public void setText(@IdRes int resID, CharSequence character) {
        TextView textView = findViewById(resID);
        if (textView != null) {
            textView.setText(character);
        }
    }

    public void setText(@IdRes int resID, Object character) {
        TextView textView = findViewById(resID);
        if (textView != null) {
            textView.setText(character.toString());
        }
    }

//    public final void setVisible(@IdRes int id) {
//        setVisibility(id, View.VISIBLE);
//    }

    public final void setVisible(@IdRes int... ids) {
        setVisibility(View.VISIBLE, ids);
    }

    public final void setInVisible(@IdRes int... ids) {
        setVisibility(View.INVISIBLE, ids);
    }

    public final void setGone(@IdRes int... ids) {
        setVisibility(View.GONE, ids);
    }

    private void setVisibility(int visible, @IdRes int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids[i]);
            view.setVisibility(visible);
        }
    }

    public void setVisibleOrInVisible(boolean isVisible, @IdRes int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids[i]);
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setVisibleOrGone(boolean isVisible, @IdRes int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids.length);
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public void setTextColor(@IdRes int id, @ColorRes int textColor) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setTextColor(UIUtils.getColor(textColor));
        }
    }

    public void setTextColor(@IdRes int id, String textColor) {
        TextView textView = findViewById(id);
        if (textView != null) {
            textView.setTextColor(Color.parseColor(textColor));
        }
    }

    public final Context getContext() {
        return mView.getContext();
    }

    public final void startActivity(Class<?> cls) {
        UIUtils.startActivity(cls);
    }

    public final void startActivity(@IdRes int id, final Class<?> cls) {
        findViewByIdK(id).setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                UIUtils.startActivity(cls);
            }
        });
    }

    public void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
        findViewByIdK(id).setOnClickListener(onClickListener);
    }

    public final void startActivity(Intent paIntent) {
        UIUtils.startActivity(paIntent);
    }

    public void onDestroy() {
    }
}
