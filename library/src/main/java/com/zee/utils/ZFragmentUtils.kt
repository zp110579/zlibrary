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
    private var mRecordProFragment: Fragment? = null
    private var mRecordProView: View? = null
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


    fun show(fragment: (viewID: Int) -> Fragment, vararg views: View) {
        views.forEach { it2 ->
            it2.setOnClickListener {
                mRecordProView?.apply {
                    isSelected = false
                }
                it2.isSelected = true
                val tempFragmentManager = mFragmentManager
                val loFragmentByTag: Fragment? = tempFragmentManager.findFragmentByTag(it.id.toString())
                val fragmentTransaction = mFragmentManager.beginTransaction()

                if (loFragmentByTag != null) {
                    if (mRecordProFragment != null) {
                        fragmentTransaction.hide(mRecordProFragment!!);
                    }
                    fragmentTransaction.show(loFragmentByTag)
                    mRecordProFragment = loFragmentByTag
                } else {
                    val fragment = fragment.invoke(it2.id)
                    fragmentTransaction.add(mLayoutID, fragment, it2.id.toString())
                    mRecordProFragment = fragment
                }

                fragmentTransaction.commitAllowingStateLoss()
                mRecordProView = it2
            }
        }
        //默认选择第一个
        views[0].performClick()
    }
}