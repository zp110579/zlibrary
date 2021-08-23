package com.zee.example

import android.Manifest
import android.content.Intent
import com.zee.extendobject.startActivityForResultEx
import com.zee.listener.OnActivityResultListener
import com.zee.scan.zxing.android.CaptureActivity
import com.zee.utils.SuperZPerMissionUtils

/**
 * 打开二维码扫描
 */
fun Any.startScanActivity() {
    SuperZPerMissionUtils.getInstance().add(Manifest.permission.CAMERA).requestPermissions { deniedPermissions, permissionExplain ->

        if (deniedPermissions.isEmpty()) {
            startActivityForResultEx(CaptureActivity::class.java, OnActivityResultListener { data ->
                data?.apply {
                    val codedContent = getStringExtra("codedContent") //获得扫描内容
                }
            })
        }
    }
}