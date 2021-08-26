package com.zee.libs.example

import android.app.Application
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.view.CropImageView
import com.zee.utils.ZLibrary

/**
 * created by zee on 2021/6/6.
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ZLibrary.init(this, true)
        initSelectPic()

    }


    private fun initSelectPic() {
        //设置头像相关
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = PhotoPickerImageLoader() //设置图片加载器
        imagePicker.isShowCamera = true //显示拍照按钮(相机选择的时候才会有用)
        imagePicker.isMultiMode = false //为真的时候每个图片上显示选择的框，选中的也会有遮盖
        imagePicker.isCrop = true //允许裁剪（单选才有效）
        imagePicker.isSaveRectangle = true //是否按矩形区域保存
        imagePicker.selectLimit = 1 //选中数量限制
        imagePicker.style = CropImageView.Style.RECTANGLE //裁剪框的形状
        imagePicker.setFocusSize(800, 800) //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutSize(1000, 1000) //保存文件的高度。单位像素
    }

}