package com.zee.extendobject

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.annotation.StringRes
import com.zee.listener.OnActivityResultListener
import com.zee.route.ZRouter
import com.zee.utils.UIUtils
import com.zee.utils.ZScreenUtils
import com.zee.utils.ZStatusBarUtils
import java.lang.Exception
import java.math.BigDecimal


fun getString(@StringRes resid: Int): String {
    return UIUtils.getString(resid)
}

fun setLightMode() {
    ZStatusBarUtils.setLightMode(curActivityEx())
}

fun setDarkMode() {
    ZStatusBarUtils.setDarkMode(curActivityEx())
}

fun runOnIOThread(runnable: () -> Unit = {}) {
    UIUtils.runOnAsyncThread {
        runnable.invoke()
    }
}

fun runOnMainThread(runnable: () -> Unit = {}) {
    UIUtils.runOnMainThread {
        runnable.invoke()
    }
}

/**
 * 保持现在样式显示,不使用科学计数法显示
 */
fun String.keepMe(): String {
    return BigDecimal(this).toString()
}

/**
 * 只保留2位小数
 */
fun Any.keepDecimal(number: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String {
    if (this is Double) {
        return BigDecimal(this).setScale(number, roundingMode).toString()
    } else if (this is String) {
        return BigDecimal(this).setScale(number, roundingMode).toString()
    }
    return this.toString()
}

fun String.toColor(): Int {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        Color.parseColor("#ff0000")
    }
}

fun curActivityEx(): Activity {
    return UIUtils.getCurActivity()
}

fun startActivityForResultEx(paClass: Class<*>?, listener: OnActivityResultListener) {
    ZRouter.getInstance().startActivity(paClass).requestCodeCallBack(listener).letsGo()
}

/**
 * 打开Activity 并关闭当前Activity
 */
fun startActivityAndFinishCurActivity(paClass: Class<*>?) {
    UIUtils.startActivity(paClass)
    UIUtils.getCurActivity().finish()
}

fun startActivityAndFinishCurActivity(intent: Intent) {
    UIUtils.startActivity(intent)
    UIUtils.getCurActivity().finish()
}

fun startActivityEx(intent: Intent) {
    UIUtils.startActivity(intent)
}

fun startActivityEx(paClass: Class<*>?) {
    UIUtils.startActivity(paClass)
}

fun Int.px2dp(): Int {
    return UIUtils.pxToDp(this)
}

fun Int.dp2px(): Int {
    return UIUtils.dpToPx(this)
}

fun showToastShort(message: String) {
    UIUtils.showToastShort(message)
}

fun showToastShort(resID: Int) {
    UIUtils.showToastShort(resID)
}

fun postDelayed(delayMillis: Long, runnable: () -> Unit = {}) {
    UIUtils.postDelayed({ runnable.invoke() }, delayMillis)
}

