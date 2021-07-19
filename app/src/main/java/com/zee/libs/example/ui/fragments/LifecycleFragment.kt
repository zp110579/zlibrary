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
class LifecycleFragment : BaseZFragment() {
    var mIndex = 0

    override fun getLayoutID(): Int {
        return R.layout.fragment_lifecyle
    }

    companion object {
        fun newInstance(index: Int): LifecycleFragment {
            val args = Bundle()
            args.putInt("index", index)
            val fragment = LifecycleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initViews() {
        val objects = TestEventBusA()
        lifecycle.addObserver(FragmentLifecycleManager)
         mIndex=arguments!!.getInt("index")
        eventBusRegisterBindFragment(this)
        objects.eventBusRegisterBindFragment(this)
        tv_curPage.text = "第${mIndex}页"
        tv_curPage.setOnClickListener {
            startActivityEx(EventBusActivity::class.java)
        }
        ZEventBusUtils.registerBindFragment(this, objects)
    }

    override fun onShowToUser(isVisibleToUser: Boolean?) {
        super.onShowToUser(isVisibleToUser)
        ZLog.i("$mIndex-->${this}--->>onShowToUser($isVisibleToUser)")
    }
}