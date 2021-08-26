package com.zee.libs.example.ui.activitys

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.widget.Button
import com.zee.activity.BaseZActivity
import com.zee.dialog.MyDialogK
import com.zee.extendobject.runOnMainThread
import com.zee.extendobject.setOnClick
import com.zee.extendobject.setVisible
import com.zee.http.MyOk
import com.zee.http.request.DownloadFileCallBackListener
import com.zee.libs.example.R
import com.zee.listener.OnPermissionListener
import com.zee.utils.SuperZPerMissionUtils
import com.zee.utils.UIUtils
import com.zee.widget.NumberProgressBar
import kotlinx.android.synthetic.main.activity_app_update.*
import java.io.File

/**
 * APP升级
 */
class AppUpdateActivity : BaseZActivity() {
    val mUpdateUrl = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json.txt"
    override fun getLayoutID(): Int {
        return R.layout.activity_app_update
    }

    override fun initViews() {
        tv_app_upDate_a.setOnClick {
            MyDialogK.init(R.layout.zx_update_app_dialog) {
                setText(R.id.tv_title, "更新最新版本")
                setText(R.id.tv_update_info, "本次更新到最新版本提示本次更新到最新版本提示本次更新到最新版本提示本次更新到最新版本提示")
                val mNumberProgressBar = findViewById<NumberProgressBar>(R.id.progressBar_view)
                mNumberProgressBar.setProgressTextColor(Color.parseColor("#3C9698"))
                mNumberProgressBar.reachedBarColor = Color.parseColor("#3C9698")
                val btn = viewById(R.id.btn_ok) as Button
                btn.setBackgroundColor(Color.parseColor("#3C9698"))

                setOnClickListener(R.id.btn_ok) {
                    mNumberProgressBar.setVisible()
                    setGone(R.id.btn_ok)
                    SuperZPerMissionUtils.getInstance().add(Manifest.permission.WRITE_EXTERNAL_STORAGE).requestPermissions(object : OnPermissionListener {
                        override fun onPerMission(deniedPermissions: MutableList<String>, permissionExplain: MutableList<String>?) {
                            if (deniedPermissions.isEmpty()) {//已经有权限了
                                MyOk.downloadFile("https://brand1.oss-cn-beijing.aliyuncs.com/ibtc/ibtc.apk").execute(object : DownloadFileCallBackListener() {
                                    override fun onDownloadSuccess(file: File) {
                                        init(file)
                                    }

                                    override fun onError(e: Exception?) {
                                    }

                                    override fun onDownloadProgress(progress: Float, currentSize: Long, totalSize: Long, networkSpeed: Long) {
                                        mNumberProgressBar.progress = Math.round(progress);
                                        mNumberProgressBar.max = 100
                                    }
                                })
                            }
                        }
                    })
                }
            }.setOutCancel(false).show()
        }
    }

    private fun init(pakFile: File) {
        runOnMainThread {
            val context: Context = UIUtils.getApplication()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val contentUri = FileProvider.getUriForFile(
                        context, context.packageName + ".fileprovider", pakFile)
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            } else {
                intent.setDataAndType(Uri.fromFile(pakFile), "application/vnd.android.package-archive")
            }
            context.startActivity(intent)
        }

    }
}