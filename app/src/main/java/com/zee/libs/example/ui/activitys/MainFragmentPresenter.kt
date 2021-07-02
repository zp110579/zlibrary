package com.zee.libs.example.ui.activitys

import android.annotation.SuppressLint
import android.arch.lifecycle.*
import com.zee.log.ZLog

/**
 *created by zee on 2021/6/29.
 *
 */
@SuppressLint("RestrictedApi")
class MainFragmentPresenter : GenericLifecycleObserver {
    lateinit var mSource: LifecycleOwner
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event?) {
        mSource = source
        ZLog.i("------------->>$source")
        if (event == Lifecycle.Event.ON_CREATE) {
            ZLog.i("$source---$event")
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            ZLog.i("$source---$event")
        }
    }


}