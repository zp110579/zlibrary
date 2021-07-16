package com.zee.manager

import android.annotation.SuppressLint
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import com.zee.extendobject.eventBusRegisterThis
import com.zee.extendobject.eventBusUnRegisterThis
import com.zee.log.ZLog
import com.zee.utils.ZEventBusUtils
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 *created by zee on 2021/6/29.
 *Fragment生命周期监视
 */
@SuppressLint("RestrictedApi")
object FragmentLifecycleManager : GenericLifecycleObserver {
    var mLifecycleOwnerHashMap = HashMap<LifecycleOwner, ArrayList<Any>>()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event?) {
//        if (source is Fragment) {
//            when (event) {
//                Lifecycle.Event.ON_RESUME -> source.onResume()
//                Lifecycle.Event.ON_PAUSE -> source.onPause()
//                Lifecycle.Event.ON_STOP -> source.onStop()
//            }
//        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            ZEventBusUtils.unregister(source)//将自己取消注册
            if (mLifecycleOwnerHashMap.containsKey(source)) {
                val list = mLifecycleOwnerHashMap[source]!!
                list.forEach {//注销所有跟source绑定的对象
                    it.eventBusUnRegisterThis()
                }
                source.lifecycle.removeObserver(this)
                mLifecycleOwnerHashMap.remove(source)
            }
        }
    }

    fun registerBindFragment(fragment: Fragment, any: Any) {
        if (!mLifecycleOwnerHashMap.containsKey(fragment)) {
            fragment.lifecycle.addObserver(this)
            mLifecycleOwnerHashMap[fragment] = ArrayList()
        }

        val tempList = mLifecycleOwnerHashMap[fragment]!!
        if (!tempList.contains(any)) {//如果没有，才会注册
            any.eventBusRegisterThis()
            tempList.add(any)
        }
    }

}