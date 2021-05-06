package com.zee.toast;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zee.utils.UIUtils;

/**
 * 通知栏没有权限的
 * 这里设置时间长度没有效果，是根据字的长度自动调整显示的长度
 */
@SuppressWarnings(value = {"unchecked", "deprecation"})
public class NoHaveNotificationToast implements IBaseToast {
    private static ToastHandler sToastHandler;
    private static CustomToast sToast;
    private static TextView oldView;
    private static CustomToastBeanSettings sCustomToastBeanSettings = new CustomToastBeanSettings();
    /**
     * 设置离底部的高度默认未20dp
     */
    private static int marginBottomDefaultValue = 20;

    public NoHaveNotificationToast() {
        sToast = new CustomToast();
        oldView = sCustomToastBeanSettings.createTextView();
        sToast.setView(oldView);
        // 创建一个吐司处理类
        sToastHandler = new ToastHandler(sToast);
    }

    public void setCustomToastBeanSettings(CustomToastBeanSettings customToastBeanSettings) {
        if (customToastBeanSettings == null) {
            throw new IllegalArgumentException("CustomToastBeanSettings can not null");
        }

        sCustomToastBeanSettings = customToastBeanSettings;
        oldView = sCustomToastBeanSettings.createTextView();
        sToast.setView(oldView);
    }

    @Override
    public void onShowToast(String message) {
        showBaseToast(message, 0, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onShowToastWithBottom(String message) {
        showBaseToast(message, 0, Gravity.BOTTOM, 0, UIUtils.dpToPx(marginBottomDefaultValue));
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("Views cannot be null");
        }

        if (sToast != null) {
            sToast.cancel();
            sToast.setView(view);
        }
    }

    public void showBaseToast(final String str, final int showTime, final int gravity, int xOffset, int yOffset) {
        sToast.setGravity(gravity, xOffset, yOffset);
        showCustomView(str, oldView);
    }

    public void showCustomView(CharSequence text, View view) {
        setView(view);
        if (text == null || text.equals("")) return;
        sToastHandler.showText(text);
    }

    public static void setMarginBottomDefaultValue(int marginBottomDefaultValue) {
        NoHaveNotificationToast.marginBottomDefaultValue = marginBottomDefaultValue;
    }

    private void setView(int layoutId) {
        setView(View.inflate(UIUtils.getApplication(), layoutId, null));
    }


//    private void setGravity(int gravity) {
//        if (sToast != null) {
//            if (gravity == Gravity.CENTER) {
//                sToast.setGravity(gravity, 0, 0);
//            } else {
//                sToast.setGravity(gravity, 0, UIUtils.dpToPx(marginBottom));
//            }
//        }
//    }
}
