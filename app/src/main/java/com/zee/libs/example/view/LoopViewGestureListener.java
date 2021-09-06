package com.zee.libs.example.view;


import android.view.MotionEvent;

import com.contrarywind.view.WheelView;

/**
 * 手势监听
 */
public final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final ZxWheelView wheelView;


    public LoopViewGestureListener(ZxWheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
