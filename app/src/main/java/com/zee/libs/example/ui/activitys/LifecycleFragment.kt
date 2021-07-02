package com.zee.libs.example.ui.activitys

import com.zee.activity.BaseZFragment
import com.zee.extendobject.eventBusRegisterBindFragment
import com.zee.libs.example.R
import com.zee.libs.example.eventbus.TestEventBusA
import com.zee.utils.ZEventBusUtils
import org.greenrobot.eventbus.SubscribeSimple

/**
 *created by zee on 2021/6/29.
 *
 */
class LifecycleFragment : BaseZFragment() {
    override fun getLayoutID(): Int {
        return R.layout.fragment_lifecyle
    }

    override fun initViews() {
        val objects= TestEventBusA()
        eventBusRegisterBindFragment(this)
        objects.eventBusRegisterBindFragment(this)
        ZEventBusUtils.registerBindFragment(this,objects)
    }

    @SubscribeSimple("Test")
    fun onDes() {

    }
}