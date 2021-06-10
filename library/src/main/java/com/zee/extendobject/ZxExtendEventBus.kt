package com.zee.extendobject

import com.zee.bean.EventBusSubscriber
import com.zee.utils.ZEventBusUtils
import com.zee.utils.ZEventBusUtils.postTagNoParam


fun Any.eventBusRegisterThis() {
    ZEventBusUtils.register(this, "")
}

fun Any.eventBusRegisterThis(subscriberTag: String = "") {
    ZEventBusUtils.register(this, subscriberTag)
}

fun Any.eventBusRegisterThis(subscriberTag: Int = 0) {
    ZEventBusUtils.register(this, subscriberTag)
}

fun Any.eventBusUnRegisterThis() {
    eventBusUnRegister(this)
}

/**
 * 生命周期自动跟当前的Activity生命周期绑定在一起，自动注销
 */
fun Any.eventBusRegisterThisAndBindCurActivity(subscriberTag: String = "") {
    ZEventBusUtils.registerBindCurActivity(this,subscriberTag)
}

fun Any.unRegisterEventBus() {
    eventBusUnRegister(this)
}

fun eventBusPostTagNoParam(vararg value: String) {
    postTagNoParam(*value)
}

@Deprecated("", ReplaceWith("any.eventBusRegister()"))
fun eventBusRegister(any: Any) {
    any.eventBusRegisterThis("")
}

@Deprecated("", ReplaceWith("any.eventBusRegister(subscriberTag)"))
fun eventBusRegister(any: Any, subscriberTag: Int) {
    any.eventBusRegisterThis(subscriberTag)
}

@Deprecated("", ReplaceWith("any.eventBusRegister(subscriberTag)"))
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

fun eventBusPost(any: Any?) {
    ZEventBusUtils.post(any)
}

fun eventBusPost(any: Any?, methodTag: String) {
    ZEventBusUtils.post(any, methodTag)
}

fun eventBusPost(any: Any?, methodTag: String, subscriberTag: String) {
    getEventBusSubscriber(subscriberTag).post(any, methodTag)
}

fun eventBusPost(any: Any?, methodTag: String, subscriberTag: Int) {
    getEventBusSubscriber(subscriberTag).post(any, methodTag)
}

fun eventBusPostTagNoParam(methodTag: String) {
    ZEventBusUtils.postTagNoParam(methodTag)
}