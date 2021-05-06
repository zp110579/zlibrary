package com.zee.toast;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.zee.utils.UIUtils;
import com.zee.utils.ZLibrary;

/**
 * 正常显示Toast
 */
public class HaveNotificationToast implements IBaseToast {
    CustomToastBeanSettings mCustomToastBeanSettings;

    public HaveNotificationToast() {

    }

    @Override
    public void onShowToast(String message) {
        showToast(message, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    @Override
    public void onShowToastWithBottom(String message) {
        showToast(message, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    private void showToast(final String str, final int showTime, final int gravity) {
        if (UIUtils.isOnMainThread()) {
            showBaseToast(str, showTime, gravity, 0, 0);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showBaseToast(str, showTime, gravity, 0, 0);
                                        }
                                    }
            );
        }
    }

    @Override
    public void setCustomToastBeanSettings(CustomToastBeanSettings customToastBeanSettings) {
        mCustomToastBeanSettings = customToastBeanSettings;
    }

    public void showBaseToast(final String str, final int showTime, final int gravity, int xOffset, int yOffset) {
        Toast toast = Toast.makeText(getCurActivity(), str, showTime);
        if (mCustomToastBeanSettings != null) {
            TextView textView = mCustomToastBeanSettings.createTextView();
            toast.setView(textView);
            textView.setText(str);
        }
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();

    }

    /**
     * 获得当前的正在显示Activity
     *
     * @return
     */
    public static Activity getCurActivity() {
        return ZLibrary.getInstance().getCurrentActivity();
    }

}
