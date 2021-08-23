package com.zee.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.lzy.imagepicker.ImagePickerManager
import com.zee.activity.BaseZActivity
import com.zee.dialog.LoadingDialog
import com.zee.scan.zxing.android.CaptureActivity
import kotlinx.android.synthetic.main.activity_image_select_main.*
import java.io.File

class MyImageSelectAppActivity : BaseZActivity() {
    private val DECODED_CONTENT_KEY = "codedContent"
    private val DECODED_BITMAP_KEY = "codedBitmap"

    private val REQUEST_CODE_SCAN = 0x0000


    override fun getLayoutID(): Int {
        return R.layout.activity_image_select_main
    }

    override fun initViews() {

        tv_scan.setOnClickListener {
            ImagePickerManager.singleSelectImage().letsGo { imageItemArrayList ->
                if (imageItemArrayList.isNotEmpty()) {
                    val loadingDialog = LoadingDialog.newLoadDialog().show()
                    iv_imageView.setImageURI(Uri.fromFile(File(imageItemArrayList[0].path)))

                }
            }

//            //动态权限申请
//            if (ContextCompat.checkSelfPermission(this@MyImageSelectAppActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this@MyImageSelectAppActivity, arrayOf(Manifest.permission.CAMERA), 1)
//            } else {
//                goScan()
//            }
        }
    }

    /**
     * 跳转到扫码界面扫码
     */
    private fun goScan() {
        val intent = Intent(this@MyImageSelectAppActivity, CaptureActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SCAN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                val content = data.getStringExtra(DECODED_CONTENT_KEY)
                //返回的BitMap图像
                val bitmap = data.getParcelableExtra<Bitmap>(DECODED_BITMAP_KEY)
                tv_scanResult.setText("你扫描到的内容是：$content")
            }
        }
    }
}