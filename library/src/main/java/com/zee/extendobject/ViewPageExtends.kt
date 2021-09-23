package com.zee.extendobject

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.zee.adapter.CustomViewPageAdapterManager
import com.zee.dialog.MyDialogK
import com.zee.scan.zxing.encode.QRCodeUtil

/**
 * 将内容换成二维码直接显示的图片上
 */
fun ViewPager.addPagerAdapter(fragment: Fragment, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(fragment).addFragment(*fragments).fragmentPagerAdapter
}

fun ViewPager.addStatePagerAdapter(fragment: Fragment, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(fragment).addFragment(*fragments).fragmentStatePagerAdapter
}

fun ViewPager.addPagerAdapter(fragment: FragmentActivity, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(fragment).addFragment(*fragments).fragmentPagerAdapter
}

fun ViewPager.addStatePagerAdapter(activity: FragmentActivity, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(activity).addFragment(*fragments).fragmentStatePagerAdapter
}


fun ViewPager.addPagerAdapter(fragment: MyDialogK, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(fragment).addFragment(*fragments).fragmentPagerAdapter
}

fun ViewPager.addStatePagerAdapter(activity: MyDialogK, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(activity).addFragment(*fragments).fragmentStatePagerAdapter
}


