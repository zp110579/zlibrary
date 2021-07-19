package com.zee.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View

/**
 *created by zee on 2021/7/19.
 *
 */
class ZFragmentUtils {
    private var mFragmentManager: FragmentManager
    private var mLayoutID = 0

    constructor(activity: FragmentActivity, paLayout: Int) {
        mFragmentManager = activity.supportFragmentManager
        mLayoutID = paLayout
    }

    constructor(fragment: Fragment, paLayout: Int) {
        mFragmentManager = fragment.childFragmentManager
        mLayoutID = paLayout
    }

    var sFragment: Fragment? = null

    fun show(fragment: (viewID: Int) -> Fragment, vararg textView: View) {
        textView.forEach { it2 ->
            it2.setOnClickListener {
                val tempFragmentManager = mFragmentManager
                val loFragmentByTag: Fragment? = tempFragmentManager.findFragmentByTag(it.id.toString())
                val fragmentTransaction = mFragmentManager.beginTransaction()

                if (loFragmentByTag != null) {
                    if (sFragment != null) {
                        fragmentTransaction.hide(sFragment!!);
                    }
                    fragmentTransaction.show(loFragmentByTag)
                    sFragment = loFragmentByTag
                } else {
                    val fragment = fragment.invoke(it2.id)
                    fragmentTransaction.add(mLayoutID, fragment, it2.id.toString())
                    sFragment = fragment
                }

                fragmentTransaction.commitAllowingStateLoss()
            }
        }
        //默认选择第一个
        textView[0].performClick()
    }
}