package com.zee.libs.example.ui.activitys

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import com.zee.utils.ZEventBusUtils
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
        lifecycle.addObserver(MainPresenter())
        viewPage.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return 5
            }

            override fun getItem(p0: Int): Fragment {
                return LifecycleFragment()
            }
        }
    }

    override fun onDestroy() {
        ZEventBusUtils.printCurAllSubscribers()
        super.onDestroy()
    }
}