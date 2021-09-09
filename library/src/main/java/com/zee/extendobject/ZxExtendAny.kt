package com.zee.extendobject

import android.Manifest
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.zee.libs.R
import com.zee.listener.OnOpenActivityResultListener
import com.zee.listener.OnPermissionListener
import com.zee.route.ZRouter
import com.zee.scan.zxing.android.CaptureActivity
import com.zee.utils.SuperZPerMissionUtils
import com.zee.utils.UIUtils
import com.zee.utils.ZStatusBarUtils
import java.lang.Exception
import java.math.BigDecimal


fun requestPermissions(permission: String, result: OnPermissionListener) {
    SuperZPerMissionUtils.getInstance().add(Manifest.permission.CAMERA).requestPermissions(result)
}

fun requestPermissions(permission: String, result: () -> Unit) {
    SuperZPerMissionUtils.getInstance().add(Manifest.permission.CAMERA).requestPermissions { deniedPermissions, _ ->
        if (deniedPermissions.isEmpty()) {
            result.invoke()
        }
    }
}

fun openActivityEx(intent: Intent) {
    UIUtils.startActivity(intent)
}

fun openActivityEx(paClass: Class<*>?) {
    UIUtils.startActivity(paClass)
}

fun openActivityAndFinishCurActivityEx(paClass: Class<*>?) {
    val tempActivity = UIUtils.getCurActivity()
    UIUtils.startActivity(paClass)
    tempActivity.finish()
}

fun openActivityAndFinishCurActivityEx(intent: Intent) {
    val tempActivity = UIUtils.getCurActivity()
    UIUtils.startActivity(intent)
    tempActivity.finish()
}

fun openActivityForResult(paClass: Class<*>?, listenerOpen: OnOpenActivityResultListener) {
    ZRouter.getInstance().startActivity(paClass).requestCodeCallBack(listenerOpen).letsGo()
}


/**
 * 将内容拷贝到粘贴板里
 */
fun copyTxtToClipboard(content: String): Boolean {
    val cmb = UIUtils.getCurActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cmb.text = content.trim()
    return true
}

/**
 * 相机扫描
 */
fun cameraScan(result: (txt: String) -> Unit) {
    SuperZPerMissionUtils.getInstance().add(Manifest.permission.CAMERA).requestPermissions { deniedPermissions, permissionExplain ->
        if (deniedPermissions.isEmpty()) {
            startActivityForResultEx(CaptureActivity::class.java, OnOpenActivityResultListener { data ->
                data?.apply {
                    val codedContent = getStringExtra("codedContent") //获得扫描内容
                    result.invoke(codedContent)
                }
            })
        } else {
            showToastShort(R.string.zee_str_no_permission)
        }
    }
}

fun getColor(@ColorRes color: Int): Int {
    return UIUtils.getColor(color)
}

fun getString(@StringRes resId: Int): String {
    return UIUtils.getString(resId)
}

fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
    return UIUtils.getString(resId, formatArgs)
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

fun startActivityForResultEx(paClass: Class<*>?, listenerOpen: OnOpenActivityResultListener) {
    ZRouter.getInstance().startActivity(paClass).requestCodeCallBack(listenerOpen).letsGo()
}

/**
 * 打开Activity 并关闭当前Activity
 */
fun startActivityAndFinishCurActivity(paClass: Class<*>?) {
    val tempActivity = UIUtils.getCurActivity()
    UIUtils.startActivity(paClass)
    tempActivity.finish()
}

fun startActivityAndFinishCurActivity(intent: Intent) {
    val tempActivity = UIUtils.getCurActivity()
    UIUtils.startActivity(intent)
    tempActivity.finish()
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

