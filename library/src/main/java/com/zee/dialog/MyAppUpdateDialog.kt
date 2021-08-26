package com.zee.dialog

import android.Manifest
import android.view.View
import com.zee.libs.R
import com.zee.listener.OnPermissionListener
import com.zee.utils.SuperZPerMissionUtils

/**
 * APP更新弹窗
 */
abstract class MyAppUpdateDialog {
    private var mContext = ""
    private var mTitle = "" //设置标题
    private var isMustUpdate = false //必须更新
    /**
     * 更新内容
     */
    fun setContentText(text: String) {
        mContext = text
    }

    /**
     * 是否升级到%s版本？
     *
     * @param text
     */
    fun setTitleText(text: String) {
        mTitle = text
    }

    /**
     * 设置必须更新
     */
    fun setMustUpdate() {
        isMustUpdate = true
    }

    abstract fun startDownApp()

    fun show() {
        MyDialog.init(object : BindViewAdapter(R.layout.zx_update_app_dialog) {
            override fun initViews(paView: View) {


            }
        }).show()
    }
}