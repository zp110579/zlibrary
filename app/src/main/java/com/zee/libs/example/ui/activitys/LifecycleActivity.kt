package com.zee.libs.example.ui.activitys

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.zee.activity.BaseZActivity
import com.zee.adapter.ZxBottomBarAdapter
import com.zee.libs.example.R
import com.zee.libs.example.ui.fragments.LifecycleFragment
import com.zee.utils.ZEventBusUtils
import com.zee.utils.ZFragmentUtils
import kotlinx.android.synthetic.main.activity_lifecyle.*

/**
 *created by zee on 2021/6/29.
 *
 */
class LifecycleActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_lifecyle
    }

    override fun initViews() {
//        viewPage.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
//            override fun getCount(): Int {
//                return 5
//            }
//
//            override fun getItem(p0: Int): Fragment {
//                return LifecycleFragment.newInstance(p0)
//            }
//        }
//        initBottom()
//        initBottom2()
    }

    fun initBottom2() {
        ZFragmentUtils(this, R.id.layout_relative).show({ viewID ->
            LifecycleFragment.newInstance(viewID)
        }, tv_a, tv_b)
    }

    fun initBottom() {
        barLayout.setZxBottomBarAdapter(object : ZxBottomBarAdapter(this, R.id.tv_a, R.id.layout_relative) {
            override fun getFragment(viewID: Int): Fragment {
                if (viewID == R.id.tv_a) {
                    return LifecycleFragment.newInstance(1)
                } else {
                    return LifecycleFragment.newInstance(2)
                }
            }

        })
    }

    override fun onDestroy() {
        ZEventBusUtils.printCurAllSubscribers()
        super.onDestroy()
    }
}