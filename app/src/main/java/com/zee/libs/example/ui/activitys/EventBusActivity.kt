package com.zee.libs.example.ui.activitys

import com.zee.activity.BaseZActivity
import com.zee.extendobject.eventBusPost
import com.zee.extendobject.eventBusRegisterThis
import com.zee.extendobject.eventBusRegisterThisAndBindCurActivity
import com.zee.extendobject.showToastShort
import com.zee.libs.example.R
import com.zee.log.ZLog
import kotlinx.android.synthetic.main.activity_evenbus.*
import org.greenrobot.eventbus.SubscribeMainThread
import org.greenrobot.eventbus.SubscribeSimple
import org.greenrobot.eventbus.adapter.EventBusPostAdapter

/**
 * EventBus
 */
class EventBusActivity : BaseZActivity() {

    override fun getLayoutID(): Int {
        return R.layout.activity_evenbus
    }

    override fun initViews() {
        eventBusRegisterThisAndBindCurActivity()
        tv_eventBus_a.setOnClickListener {
            eventBusPost("消息A", "evenBus_message_a", object : EventBusPostAdapter() {
            })
        }
        tv_eventBus_b.setOnClickListener {
            eventBusPost("消息B", "evenBus_message_b", object : EventBusPostAdapter() {

            })
        }
        tv_eventBus_c.setOnClickListener {
            eventBusPost("ErrorC", "evenBus_message_c", object : EventBusPostAdapter() {
            })
        }
    }

    @SubscribeSimple("evenBus_message_a,evenBus_message_b")
    fun onEventBusMessage(message: String) {
        showToastShort(message)
    }

    @SubscribeMainThread(tag = "evenBus_message_a,evenBus_message_b")
    fun onEventBusMessageB(message: String) {
        ZLog.i("showMessageIndex:$message")
    }

    @SubscribeSimple("evenBus_message_c")
    fun onEventBusMessage_error(message: String) {
        val value = 1 / 0;
        showToastShort(message)
    }
}