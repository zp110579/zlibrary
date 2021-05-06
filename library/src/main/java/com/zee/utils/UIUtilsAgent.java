package com.zee.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.ExecutorService;

/**
 * created by zee on 2020/12/28.
 */
class UIUtilsAgent {


    protected static void startActivity(Class<?> paClass) {
        Context loContext = getCurActivity();
        if (loContext == null) {
            loContext = getApplication();
        }
        Intent loIntent = new Intent(loContext, paClass);
        startActivity(loIntent);
    }

    protected static void startActivity(final Intent paIntent) {
        if (isOnMainThread()) {
            startActivityEx(paIntent);
        } else {
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    startActivityEx(paIntent);
                }
            });
        }
    }

    private static void startActivityEx(Intent paIntent) {
        Context loContext = getCurActivity();
        if (loContext == null) {
            loContext = getApplication();
            paIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        loContext.startActivity(paIntent);
    }


    /**
     * 显示dialog
     *
     * @param dialogFragment
     */
    protected static void showDialogFragment(final DialogFragment dialogFragment) {
        boolean isDestroy = false;
        Activity activity = getCurActivity();
        if (activity != null) {
            if (Build.VERSION.SDK_INT > 16) {
                isDestroy = activity.isDestroyed() && activity.isFinishing();
            }

            if (!isDestroy) {
                if (activity instanceof FragmentActivity) {
                    final FragmentActivity activity1 = (FragmentActivity) activity;
                    final FragmentManager supportFragmentManager = activity1.getSupportFragmentManager();

                    if (isOnMainThread()) {
                        if (!supportFragmentManager.isStateSaved()) {
                            dialogFragment.show(activity1.getSupportFragmentManager(), "dialogFragment");
                        }
                    } else {
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!supportFragmentManager.isStateSaved()) {
                                    dialogFragment.show(activity1.getSupportFragmentManager(), "dialogFragment");
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    protected static void showKeyboard(View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    protected static void showKeyboard(final View view, long delayMillis) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                }
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, delayMillis);
    }

    protected static void addTouchRectView(View view, int dpValue) {
        int spValue = UIUtils.dpToPx(dpValue);
        Rect delegateArea = new Rect();
        view.getHitRect(delegateArea);

        delegateArea.right += spValue;
        delegateArea.bottom += spValue;
        delegateArea.left -= spValue;
        delegateArea.top -= spValue;

        TouchDelegate touchDelegate = new TouchDelegate(delegateArea, view);

        if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
    }

    protected static void removeFromParentView(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    protected static boolean postDelayed(Runnable runnable, long delayMillis) {
        Handler uiHandler = ZLibrary.getInstance().getMainUIHandler();
        return uiHandler.postDelayed(runnable, delayMillis);
    }


    //异步运行起来
    public static void runOnAsyncThread(final Runnable runnable, long delayMillis) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ExecutorService executorService = ZLibrary.getInstance().getExecutorService();
                executorService.execute(runnable);
            }
        }, delayMillis);
    }

    //主线程运行
    protected static boolean runOnMainThread(Runnable runnable) {
        if (isOnMainThread()) {
            runnable.run();
            return true;
        }
        Handler uiHandler = ZLibrary.getInstance().getMainUIHandler();
        return uiHandler.post(runnable);
    }

    //是否在主线程
    protected static boolean isOnMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private static Context getApplication() {
        return UIUtils.getApplication();
    }

    private static Activity getCurActivity() {
        return UIUtils.getCurActivity();
    }
}
