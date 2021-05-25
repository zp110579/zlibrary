package com.example.eventexample

import android.content.Intent
import android.provider.Settings
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zee.utils.UIUtils

/**
 * created by zee on 2020/12/28.
 * 需要完成2个功能，一个是提示的功能，一个是提示并能打开看问题的功能
 */
object SnackBarUtils {

    fun showMessage(message: String, btnTile: String? = null) {
        val activity = UIUtils.getCurActivity()
        val rootView = findSuitableParent(activity.window.decorView)
        val networkStatusSnackBar = Snackbar.make(rootView!!.getChildAt(0),
                message, Snackbar.LENGTH_INDEFINITE);
        if (networkStatusSnackBar != null && !btnTile.isNullOrEmpty()) {
            networkStatusSnackBar.setAction(btnTile, View.OnClickListener { // 开启设置界面
                activity.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
            })
        }
        networkStatusSnackBar.show()
    }


    private fun findSuitableParent(paView: View): ViewGroup? {
        var view: View? = paView
        var fallback: ViewGroup? = null
        do {
            if (view is CoordinatorLayout) {
                return view
            } else if (view is FrameLayout) {
                fallback = if (view.getId() == android.R.id.content) {
                    return view
                } else {
                    view
                }
            }
            if (view != null) {
                val parent = view.parent
                view = if (parent is View) parent else null
            }
        } while (view != null)
        return fallback
    }
}