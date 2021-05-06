package com.zee.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

public class ZScreenUtils {

    /**
     * 屏幕宽度
     *
     * @return
     */
    public final static int getScreenWidth() {
        return ZLibrary.getInstance().getScreenWith();
    }

    /**
     * 屏幕高度
     *
     * @return
     */
    public final static int getScreenHeight() {
        return ZLibrary.getInstance().getScreenHeight();
    }

    /**
     * 获得密度
     *
     * @return
     */
    public final static float getDensity() {
        return ZLibrary.getInstance().getDensity();
    }


    /**
     * 去掉Bar的屏幕高度
     *
     * @return
     */
    public final static int getScreenWithNoBar() {
        return ZLibrary.getInstance().getScreenWith() - ZStatusBarUtils.getStatusBarHeight(UIUtils.getApplication());
    }

    //获取当前屏幕截图，包含状态栏
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    public static void setFullScreen(Activity activity) {//全屏
        if (activity != null) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    //获取当前屏幕截图，不包含状态栏*
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
