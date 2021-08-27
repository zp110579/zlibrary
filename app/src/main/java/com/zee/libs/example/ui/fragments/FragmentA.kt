package com.zee.libs.example.ui.fragments

import android.os.Bundle
import com.zee.activity.BaseZFragment
import com.zee.extendobject.eventBusRegisterBindFragment
import com.zee.extendobject.startActivityEx
import com.zee.libs.example.R
import com.zee.libs.example.eventbus.TestEventBusA
import com.zee.libs.example.ui.activitys.EventBusActivity
import com.zee.log.ZLog
import com.zee.manager.FragmentLifecycleManager
import com.zee.utils.ZEventBusUtils
import kotlinx.android.synthetic.main.fragment_lifecyle.*

/**
 *created by zee on 2021/6/29.
 *
 */
class FragmentA : BaseZFragment() {
    var mIndex = 0

    override fun getLayoutID(): Int {
        return R.layout.fragment_a
    }


    override fun initViews() {
    }

    override fun onShowToUser(isVisibleToUser: Boolean) {
        super.onShowToUser(isVisibleToUser)
        if (isVisibleToUser) {
            ZLog.i("香蕉")
        }
    }
}