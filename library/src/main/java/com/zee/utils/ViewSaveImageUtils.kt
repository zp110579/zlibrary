package com.zee.utils

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ScrollView
import com.zee.extendobject.showToastShort
import com.zee.listener.OnPermissionListener
import java.io.File
import java.io.FileOutputStream

/**
 *created by zee on 2021/4/20.
 *将View保存成View保存在本地
 */
object ViewSaveImageUtils {

    /**
     * 将View保存为本地图片
     */
    fun saveImg(view: View, file: File, result: () -> Unit = {}) {
        val bitmap: Bitmap = getViewBitmap(view)
        if (!file.exists()) {
            file.createNewFile()
        }
        try {
            val fileOutputStream = FileOutputStream(file)
            //这个100表示压缩比,100说明不压缩,90说明压缩到原来的90%
            //注意:这是对于占用存储空间而言,不是说占用内存的大小
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //通知图库即使更新,否则不能看到图片
        UIUtils.getApplication().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())))
        result.invoke()
    }

    /**
     *view:View
     * fileName:文件的名字
     * result: -1:没有权限保存到本地 0:保存成功
     */
    fun saveImg(view: View, fileName: String, result: (code: Int) -> Unit = {}) {
        val bitmap: Bitmap = getViewBitmap(view)
        SuperZPerMissionUtils.getInstance().add(Manifest.permission.WRITE_EXTERNAL_STORAGE).requestPermissions(object : OnPermissionListener {
            override fun onPerMission(deniedPermissions: MutableList<String>, permissionExplain: MutableList<String>?) {
                if (deniedPermissions.isNotEmpty()) {
                    //没有权限
                    result.invoke(-1)
                    return
                }
                val dirFile = File(Environment.getExternalStorageDirectory(), "picture")
                if (!dirFile.exists()) {
                    dirFile.mkdir()
                }
                val file = File(dirFile, fileName)
                if (!file.exists()) {
                    file.createNewFile()
                }
                try {
                    val fileOutputStream = FileOutputStream(file)
                    //这个100表示压缩比,100说明不压缩,90说明压缩到原来的90%
                    //注意:这是对于占用存储空间而言,不是说占用内存的大小
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //通知图库即使更新,否则不能看到图片
                UIUtils.getApplication().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())))
                result.invoke(0) //成功
            }
        })
    }

    fun getViewBitmap(view: View): Bitmap {
        var w = 0
        var h = 0
        when (view) {
            is ScrollView -> {
                for (i in 0 until (view).childCount) {
                    h += (view).getChildAt(i).height
                }
                w = view.width
            }
            else -> {
                w = view.width
                h = view.height
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap
    }
}