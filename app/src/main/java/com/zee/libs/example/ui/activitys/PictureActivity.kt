package com.zee.libs.example.ui.activitys

import android.Manifest
import com.bumptech.glide.Glide
import com.lzy.imagepicker.ImagePickerManager
import com.lzy.imagepicker.bean.CameraPickerImageSelectManager
import com.zee.activity.BaseZActivity
import com.zee.extendobject.setOnNoDoubleClickListener
import com.zee.libs.example.R
import com.zee.log.ZLog
import com.zee.utils.ImageCompressUtils
import com.zee.utils.SuperZPerMissionUtils
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
        tv_camer.setOnNoDoubleClickListener {
            val cameraPickerImageSelectManager = ImagePickerManager.cameraImage()
            if (cb_btn.isChecked) {
                cameraPickerImageSelectManager.setRectangleEdit()
            }
            cameraPickerImageSelectManager.letsGo { imageItemArrayList ->
                if (imageItemArrayList.isNotEmpty()) {
                    Glide.with(imageView).load(imageItemArrayList[0].path).into(imageView)
                }
            }
        }
        tv_single.setOnNoDoubleClickListener {
            val singleImageSelectManager = ImagePickerManager.singleSelectImage()
            if (cb_btn.isChecked) {
                singleImageSelectManager.setRectangleEdit()
            }
            singleImageSelectManager.letsGo { imageItemArrayList ->
                if (imageItemArrayList.isNotEmpty()) {
                    Glide.with(imageView).load(imageItemArrayList[0].path).into(imageView)
                }
            }
        }

        tv_many.setOnClickListener {//多图片选择
            openDialg()
        }
    }

    fun openDialg() {
        ImagePickerManager.manySelectImages(8).letsGo { imageItemArrayList ->
            if (imageItemArrayList.isNotEmpty()) {
                Glide.with(imageView).load(imageItemArrayList[0].path).into(imageView)
                ImageCompressUtils.compressImageBigSize(imageItemArrayList[0].path, 600)
            }
        }
    }
}