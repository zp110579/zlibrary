package com.zee.extendobject

import com.zee.bean.EventBusSubscriber
import com.zee.utils.ZEventBusUtils
import com.zee.utils.ZEventBusUtils.postTagNoParam

/**
 * EventBus
 */

fun Any.eventBus_RegisterThis(subscriberTag: String = "") {
    eventBusRegister(this, subscriberTag)
}

fun Any.eventBus_UnRegisterThis() {
    eventBusUnRegister(this)
}

/**
 * 生命周期自动跟当前的Activity生命周期绑定在一起，自动注销
 */
fun Any.eventBus_RegisterBindCurActivity(subscriberTag: String = "") {
    ZEventBusUtils.registerBindCurActivity(this, subscriberTag)
}

fun eventBusPostTagNoParam(vararg value: String) {
    postTagNoParam(*value)
}

fun eventBusRegister(any: Any) {
    ZEventBusUtils.register(any)
}

fun eventBusRegister(any: Any, subscriberTag: Int) {
    ZEventBusUtils.register(any, subscriberTag)
}

fun eventBusRegister(any: Any, subscriberTag: String) {
    ZEventBusUtils.register(any, subscriberTag)
}

fun getEventBusSubscriber(subscriberTag: Int): EventBusSubscriber {
    return ZEventBusUtils.getEventBusSubscriber(subscriberTag)
}

fun getEventBusSubscriber(subscriberTag: String): EventBusSubscriber {
    return ZEventBusUtils.getEventBusSubscriber(subscriberTag)
}

fun eventBusUnRegister(any: Any) {
    ZEventBusUtils.unregister(any)
}

fun eventBusPost(any: Any) {
    ZEventBusUtils.post(any)
}

fun eventBusPost(any: Any, methodTag: String) {
    ZEventBusUtils.post(any, methodTag)
}

fun eventBusPost(any: Any, methodTag: String, subscriberTag: String) {
    getEventBusSubscriber(subscriberTag).post(any, methodTag)
}

fun eventBusPost(any: Any, methodTag: String, subscriberTag: Int) {
    getEventBusSubscriber(subscriberTag).post(any, methodTag)
}

fun eventBusPostTagNoParam(methodTag: String) {
    ZEventBusUtils.postTagNoParam(methodTag)
}