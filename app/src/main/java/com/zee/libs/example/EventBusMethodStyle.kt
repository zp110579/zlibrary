package com.zee.libs.example

import org.greenrobot.eventbus.SubscribeSimple

/**
 *created by zee on 2021/5/25.
 *
 */
class EventBusMethodStyle {


    fun onEventBusSettingAddressList() {

    }

    @SubscribeSimple("main_http_getCoint")
    fun onHttpBackSetAddressList() {

    }

    @SubscribeSimple("main_post_value")
    fun onSetNameTVValue() {

    }

}