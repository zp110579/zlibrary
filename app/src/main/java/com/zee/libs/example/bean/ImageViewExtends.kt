package com.zee.libs.example.bean

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

fun ImageView.showUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

/**
 * 显示圆角
 */
fun ImageView.showCircle(url: String) {
    Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(CircleCrop())).into(this)
}

/**
 * 显示圆角的ImageView
 */
fun ImageView.showCircle(url: String, radius: Int) {
    Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(RoundedCorners(radius))).into(this)
}




