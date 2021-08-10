package com.zee.adapter

import android.support.v4.app.*
import java.util.*

/**
 * created by zee on 2020/9/11
 */
class CustomViewPageAdapterManager {
    private var mSupportFragmentManager: FragmentManager
    private val mFragmentArrayList = ArrayList<Fragment>()

    constructor(fragmentActivity: FragmentActivity) {
        mSupportFragmentManager = fragmentActivity.supportFragmentManager
    }

    constructor(fragment: Fragment) {
        mSupportFragmentManager = fragment.childFragmentManager
    }

    constructor(fragmentManager: FragmentManager) {
        mSupportFragmentManager = fragmentManager
    }

    fun addFragment(vararg fragment: Fragment): CustomViewPageAdapterManager {
        mFragmentArrayList.addAll(fragment)
        return this
    }

    val fragmentStatePagerAdapter: FragmentStatePagerAdapter
        get() = object : FragmentStatePagerAdapter(mSupportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                return mFragmentArrayList[i]
            }

            override fun getCount(): Int {
                return mFragmentArrayList.size
            }
        }
    val fragmentPagerAdapter: FragmentPagerAdapter
        get() = object : FragmentPagerAdapter(mSupportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                return mFragmentArrayList[i]
            }

            override fun getCount(): Int {
                return mFragmentArrayList.size
            }
        }
}