package com.zee.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;

import com.zee.toast.ToastUtils;

import java.util.concurrent.ExecutorService;


/**
 * @author Administrator
 */
@SuppressWarnings(value = {"unchecked", "deprecation"})
public final class UIUtils {

    public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
        LayoutInflater layoutInflater = getCurActivity().getWindow().getLayoutInflater();
        return layoutInflater.inflate(resource, root);
    }

    public static String getString(@StringRes int stringId) {
        return getCurActivity().getString(stringId);
    }

    public static int getDimens(@DimenRes int dimensId) {
        return getResources().getDimensionPixelSize(dimensId);
    }

    public static TypedArray getTypedArray(@ArrayRes int id) {
        return getResources().obtainTypedArray(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return getResources().getStringArray(id);
    }

    @SuppressWarnings("unchecked")
    public static int getColor(@ColorRes int colorId) {
        return getResources().getColor(colorId);
    }

    @SuppressWarnings("unchecked")
    public static Drawable getDrawable(@DrawableRes int resId) {
        return getResources().getDrawable(resId);
    }

    //异步运行起来
    public static void runOnAsyncThread(Runnable runnable) {
        ExecutorService executorService = ZLibrary.getInstance().getExecutorService();
        executorService.execute(runnable);
    }

    //异步运行起来
    public static void runOnAsyncThread(final Runnable runnable, long delayMillis) {
        UIUtilsAgent.runOnAsyncThread(runnable, delayMillis);
    }

    //是否在主线程
    public static boolean isOnMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void removeFromParentView(View view) {
        UIUtilsAgent.removeFromParentView(view);
    }

    //主线程运行
    public static boolean runOnMainThread(Runnable runnable) {
        return UIUtilsAgent.runOnMainThread(runnable);
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        Handler uiHandler = ZLibrary.getInstance().getMainUIHandler();
        return uiHandler.postDelayed(runnable, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        Handler uiHandler = ZLibrary.getInstance().getMainUIHandler();
        uiHandler.removeCallbacks(runnable);
    }

    public static void showToastShort(String message) {
        ToastUtils.showToastShort(message);
    }

    public static void showToastShort(int resID) {
        ToastUtils.showToastShort(resID);
    }

    public static void showToastShort(String message, final int gravity) {
        ToastUtils.showToastShort(message, gravity);
    }

    public static void showToastLong(String message) {
        ToastUtils.showToastLong(message);
    }

    public static void showToastLong(int resID) {
        ToastUtils.showToastLong(resID);
    }

    public static void showToastLong(String message, final int gravity) {
        ToastUtils.showToastLong(message, gravity);
    }

    public static void showToast(final String str, final int showTime, final int gravity, int xOffset, int yOffset) {
        ToastUtils.showToast(str, showTime, gravity, xOffset, yOffset);
    }

    public static Activity getCurActivity() {
        return ZLibrary.getInstance().getCurrentActivity();
    }

    public static Object getSystemService(String name) {
        return getCurActivity().getSystemService(name);
    }

    public static void startActivity(final Intent paIntent) {
        UIUtilsAgent.startActivity(paIntent);
    }

    public static void startActivity(Class<?> paClass) {
        UIUtilsAgent.startActivity(paClass);
    }

    public static void showDialog(final DialogFragment dialogFragment) {
        UIUtilsAgent.showDialogFragment(dialogFragment);
    }

    /**
     * 使用android默认的浏览器打开网址
     *
     * @param url
     */
    public static void openHttp(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public static int dpToPx(int dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dpToPx(double dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxToDp(int pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int spToPx(final float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static Resources getResources() {
        return getCurActivity().getResources();
    }

    public static Context getContext() {
        return getApplication();
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        UIUtilsAgent.showKeyboard(view);
    }

    public static void showKeyboard(View view, long delayMillis) {
        UIUtilsAgent.showKeyboard(view, delayMillis);
    }

    public static Application getApplication() {
        return ZLibrary.getInstance().getApplicationContext();
    }

    public static void addTouchRectView(View view, int dpValue) {
        UIUtils.addTouchRectView(view, dpValue);
    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplication());
    }

    public static void removeCallbacksAndMessages() {
        Handler mainHand = ZLibrary.getInstance().getMainUIHandler();
        if (mainHand != null) {
            mainHand.removeCallbacksAndMessages(null);
        }
    }

    public static boolean isDebugMode() {
        return ZLibrary.getInstance().isDebug();
    }

    public static void restartApp() {
        ZSystemInfoUtils.restartApp();
    }
}
