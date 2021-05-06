package com.zee.toast;

import android.view.Gravity;
import android.widget.Toast;

import com.zee.utils.UIUtils;
import com.zee.utils.ZSystemInfoUtils;

public final class ToastUtils {
    private static IBaseToast mIToast;
    private static boolean sShowCustomToast = false;

    public static void init() {
        setShowCustomToast(false);
    }

    /**
     * 显示自定义的Toast
     *
     * @param showCustomToast
     */
    public static void setShowCustomToast(boolean showCustomToast) {
        sShowCustomToast = showCustomToast;
        mIToast = null;
        if (!ZSystemInfoUtils.isNotificationEnabled() || sShowCustomToast) {
            mIToast = new NoHaveNotificationToast();
        } else {
            mIToast = new HaveNotificationToast();
        }
    }

    public static void setCustomToastBeanSettings(CustomToastBeanSettings customToastBeanSettings) {
        if (customToastBeanSettings == null) {
            throw new IllegalArgumentException("CustomToastBeanSettings can not null");
        }
        if (mIToast == null) {
            throw new IllegalStateException("first init ZLibrary.setConfig");
        }
        mIToast.setCustomToastBeanSettings(customToastBeanSettings);
    }

    public static void showToastShort(final String message) {
        if (UIUtils.isOnMainThread()) {
            mIToast.showBaseToast(message, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIToast.showBaseToast(message, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0);
                }
            });
        }
    }

    public static void showToastShort(final String message, final int gravity) {
        if (UIUtils.isOnMainThread()) {
            mIToast.showBaseToast(message, Toast.LENGTH_SHORT, gravity, 0, 0);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIToast.showBaseToast(message, Toast.LENGTH_SHORT, gravity, 0, 0);
                }
            });
        }
    }

    public static void showToastShort(final int resId) {
        if (UIUtils.isOnMainThread()) {
            showToastShort(UIUtils.getString(resId));
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    showToastShort(UIUtils.getString(resId));
                }
            });
        }
    }

    public static void showToastLong(final String message) {
        if (UIUtils.isOnMainThread()) {
            mIToast.showBaseToast(message, Toast.LENGTH_LONG, Gravity.CENTER, 0, 0);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIToast.showBaseToast(message, Toast.LENGTH_LONG, Gravity.CENTER, 0, 0);

                }
            });
        }
    }

    public static void showToastLong(final String message, final int gravity) {
        if (UIUtils.isOnMainThread()) {
            mIToast.showBaseToast(message, Toast.LENGTH_LONG, gravity, 0, 0);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIToast.showBaseToast(message, Toast.LENGTH_LONG, gravity, 0, 0);
                }
            });
        }
    }

    public static void showToastLong(final int resId) {
        if (UIUtils.isOnMainThread()) {
            showToastLong(UIUtils.getString(resId));
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    showToastLong(UIUtils.getString(resId));
                }
            });
        }
    }

    public static void showToast(final String str, final int showTime, final int gravity, final int xOffset, final int yOffset) {
        if (UIUtils.isOnMainThread()) {
            mIToast.showBaseToast(str, showTime, gravity, xOffset, yOffset);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIToast.showBaseToast(str, showTime, gravity, xOffset, yOffset);
                }
            });
        }
    }

}