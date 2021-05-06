package com.zee.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.zee.manager.LayoutBackgroundManager;

/**
 * @author Administrator
 */

public class ZxConstraintLayout extends ConstraintLayout {
    private LayoutBackgroundManager mBackgroundManager;
    private boolean first = true;

    public ZxConstraintLayout(Context context) {
        this(context, null);
    }

    public ZxConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundManager = new LayoutBackgroundManager(context, attrs, this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (first) {
            mBackgroundManager.setBgSelector();
            first = false;
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mBackgroundManager.setSelected(selected);
    }
}
