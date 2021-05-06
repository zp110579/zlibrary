package com.zee.toast;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zee.log.ZLog;
import com.zee.utils.UIUtils;
import com.zee.utils.ZLibrary;


class CustomToast extends Toast implements Runnable {
    private boolean isShow;
    static final int SHORT_DURATION_TIMEOUT = 2000; // 短吐司显示的时长
    static final int LONG_DURATION_TIMEOUT = 3500; // 长吐司显示的时长
    // 吐司消息 View
    private TextView mMessageView;
    private CharSequence curShowText;

    /**
     * 正常显示
     */
    CustomToast() {
        super(ZLibrary.getInstance().getApplicationContext());
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        if (view instanceof TextView) {
            mMessageView = (TextView) view;
            return;
        } else if (view.findViewById(android.R.id.message) instanceof TextView) {
            mMessageView = (view.findViewById(android.R.id.message));
            return;
        } else if (view instanceof ViewGroup) {
            if ((mMessageView = findTextView((ViewGroup) view)) != null) return;
        }
        // 如果设置的布局没有包含一个 TextView 则抛出异常，必须要包含一个 TextView 作为 Message View
        throw new IllegalArgumentException("The layout must contain a TextView");
    }

    @Override
    public void setText(CharSequence s) {
        curShowText = s;
        mMessageView.setText(s);
    }


    @Override
    public void show() {
        if (!isShow) {
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = android.R.style.Animation_Toast;
            params.setTitle(Toast.class.getSimpleName());
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.packageName = UIUtils.getApplication().getPackageName();
            // 重新初始化位置
            params.gravity = getGravity();
            params.x = getXOffset();
            params.y = getYOffset();
            try {
                // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
                // java.lang.IllegalStateException: View android.widget.TextView{3d2cee7 V.ED..... ......ID 0,0-312,153} has already been added to the window manager.
                Activity activity = ZLibrary.getInstance().getCurrentActivity();

                 try {
                     activity.getWindowManager().addView(getView(), params);
                 }catch (IllegalStateException e){
                     activity.getWindowManager().removeView(getView());
                     activity.getWindowManager().addView(getView(), params);
                 }
                String text = curShowText.toString();
                int time = ToastHandler.getToastDuration(text);
                setDuration(time);
                // 当前已经显示
                isShow = true;
                // 添加一个移除吐司的任务

                UIUtils.postDelayed(this, ToastHandler.getToastDuration(curShowText));
            } catch (Exception ignored) {
                Log.i("CustomToast","错误信息" + ignored.getMessage());
            }
        }
    }


    /**
     * 递归获取ViewGroup中的TextView对象
     */
    private static TextView findTextView(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if ((view instanceof TextView)) {
                return (TextView) view;
            } else if (view instanceof ViewGroup) {
                TextView textView = findTextView((ViewGroup) view);
                if (textView != null) return textView;
            }
        }
        return null;
    }

    @Override
    public void run() {
        cancel();
    }

    /**
     * 取消吐司弹窗
     */
    public void cancel() {
        UIUtils.removeCallbacks(this);
        if (isShow) {
            try {
                // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
                Activity activity = ZLibrary.getInstance().getCurrentActivity();
                if (activity != null) {
                    activity.getWindowManager().removeView(getView());
                }
            } catch (Exception ignored) {
            }
            // 当前没有显示
            isShow = false;
        }
    }
}