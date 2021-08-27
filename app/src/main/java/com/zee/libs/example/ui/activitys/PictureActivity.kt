package com.zee.libs.example.ui.activitys

import com.lzy.imagepicker.ImagePickerManager
import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import com.zee.utils.ImageCompressUtils
import kotlinx.android.synthetic.main.activity_picture.*

/**
 *created by zee on 2021/7/16.
 *图片处理类
 */
class PictureActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_picture
    }

    override fun initViews() {
        tv_zip.setOnClickListener {
            openDialg()
        }
    }

    fun openDialg() {
        ImagePickerManager.cameraImage().letsGo { imageItemArrayList ->
            if (imageItemArrayList.isNotEmpty()) {
                ImageCompressUtils.compressImageBigSize(imageItemArrayList[0].path, 600)
            }
        }
    }
}