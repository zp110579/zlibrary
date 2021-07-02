package com.zee.manager

import android.annotation.SuppressLint
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import com.zee.extendobject.eventBusRegisterThis
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
        if (event == Lifecycle.Event.ON_DESTROY) {
            ZEventBusUtils.unregister(source)//将自己取消注册
            val list = mLifecycleOwnerHashMap[source]!!
            list.forEach {//注销所有跟source绑定的对象
                ZEventBusUtils.unregister(it)
            }
            source.lifecycle.removeObserver(this)
            mLifecycleOwnerHashMap.remove(source)
        }
    }

    fun registerBindFragment(fragment: Fragment, any: Any) {
        val list = mLifecycleOwnerHashMap[fragment]
        if (list.isNullOrEmpty()) {
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