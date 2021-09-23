package com.zee.libs.example.ui.activitys

import android.Manifest
import com.bumptech.glide.Glide
import com.lzy.imagepicker.ImagePickerManager
import com.zee.activity.BaseZActivity
import com.zee.extendobject.cameraScan
import com.zee.extendobject.requestPermissions
import com.zee.extendobject.setOnClick
import com.zee.libs.example.R
import com.zee.listener.OnPermissionListener
import com.zee.log.ZLog
import com.zee.utils.ImageCompressUtils
import com.zee.utils.SuperZPerMissionUtils
import com.zee.utils.ZScreenUtils
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
        tv_single.setOnClick {
            ImagePickerManager.singleSelectImage().letsGo { imageItemArrayList ->
                if (imageItemArrayList.isNotEmpty()) {
                    ZLog.i("数量")
                }
            }
        }

        tv_zip.setOnClickListener {
            openDialg()
        }
    }

    fun openDialg() {

        SuperZPerMissionUtils.getInstance().add(Manifest.permission.CAMERA).requestPermissions { deniedPermissions, permissionExplain ->

            ImagePickerManager.manySelectImages(8).letsGo { imageItemArrayList ->
                if (imageItemArrayList.isNotEmpty()) {
                    Glide.with(imageView).load(imageItemArrayList[0].path).into(imageView)
                    ImageCompressUtils.compressImageBigSize(imageItemArrayList[0].path, 600)
                }
            }
        }
    }
}