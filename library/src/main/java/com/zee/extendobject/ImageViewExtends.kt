package com.zee.extendobject

import android.widget.ImageView
import com.zee.scan.zxing.encode.QRCodeUtil

/**
 * 将内容换成二维码直接显示的图片上
 */
fun ImageView.exMakerQRandShow(content: String) {
    setImageBitmap(QRCodeUtil.createQRCode(content, width, height))
}

fun ImageView.exMakerQRandShow(content: String, resID: Int) {
    setImageBitmap(QRCodeUtil.createQRCode(content, width, height, resID))
}


