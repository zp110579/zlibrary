package com.zee.libs.example

import com.zee.extendobject.*
import org.greenrobot.eventbus.SubscribeSimple

/**
 *created by zee on 2021/5/25.
 *
 */
class EventBusMethodStyle {

    //注册
    fun onEventBusRegisterMethod() {
        eventBusRegisterThis()
        eventBusRegister(this)
        eventBusRegisterThis("1")
        eventBusRegisterThis(0)
        eventBusRegisterThisAndBindCurActivity()
    }

    //注销
    fun onEventBusUnRegisterMethod() {
        eventBusUnRegisterThis()
        eventBusUnRegister(this)
    }

    //Post
    fun onEventBusPost() {
        eventBusPost(Any())
        eventBusPost(Any(), "methodTag")

        getEventBusSubscriber(1).post(Any())
        getEventBusSubscriber("1").post(Any(), "methodTag")

        eventBusPostTagNoParam("a")
        eventBusPostTagNoParam("a", "b", "c")
    }


    @SubscribeSimple("main_http_getCoint")
    fun onHttpBackSetAddressList() {

    }

    @SubscribeSimple("main_post_value")
    fun onSetNameTVValue() {

    }

}