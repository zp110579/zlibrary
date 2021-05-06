package com.zee.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zee.manager.LayoutBackgroundManager;

/**
 * @author Administrator
 */

public class ZxRelativeLayout extends RelativeLayout {
    private LayoutBackgroundManager mBackgroundManager;
    private boolean first = true;

    public ZxRelativeLayout(Context context) {
        this(context, null);
    }

    public ZxRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBackgroundManager.getUnline_height() > 0) {
            float startX = mBackgroundManager.getUnline_marginLeft();
            float startY = getHeight() - mBackgroundManager.getUnline_height() / 2;
            float startEndX = getWidth() - mBackgroundManager.getUnline_marginRight();
            canvas.drawLine(startX, startY, startEndX, startY, mBackgroundManager.getUnLinePaint());
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mBackgroundManager.setSelected(selected);
    }
}
