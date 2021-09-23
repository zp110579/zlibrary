package com.zee.libs.example.ui.fragments

import com.zee.activity.BaseZFragment
import com.zee.libs.example.R
import com.zee.log.ZLog

/**
 *created by zee on 2021/6/29.
 *
 */
class FragmentB : BaseZFragment() {
    var mIndex = 0

    override fun getLayoutID(): Int {
        return R.layout.fragment_b
    }


    override fun initViews() {
    }

    override fun onShowToUser(isVisibleToUser: Boolean) {
        super.onShowToUser(isVisibleToUser)
        if (isVisibleToUser) {
            ZLog.i("苹果")
        }
    }
}