package com.zee.extendobject

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager

import com.zee.adapter.CustomViewPageAdapterManager
import com.zee.dialog.MyDialog
import com.zee.dialog.MyDialogK
import com.zee.scan.zxing.encode.QRCodeUtil

/**
 * 将内容换成二维码直接显示的图片上
 */
fun ViewPager.addPagerAdapter(root: Fragment, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(root).addFragment(*fragments).fragmentPagerAdapter
}

fun ViewPager.addStatePagerAdapter(root: Fragment, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(root).addFragment(*fragments).fragmentStatePagerAdapter
}

fun ViewPager.addPagerAdapter(root: FragmentActivity, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(root).addFragment(*fragments).fragmentPagerAdapter
}

fun ViewPager.addStatePagerAdapter(root: FragmentActivity, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(root).addFragment(*fragments).fragmentStatePagerAdapter
}


fun ViewPager.addPagerAdapter(root: MyDialog, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(root).addFragment(*fragments).fragmentPagerAdapter
}

fun ViewPager.addStatePagerAdapter(root: MyDialog, vararg fragments: Fragment) {
    adapter = CustomViewPageAdapterManager(root).addFragment(*fragments).fragmentStatePagerAdapter
}


