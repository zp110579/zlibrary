package com.zee.extendobject

import com.zee.log.ZLog

fun Any.logI(message: Any?) {
    ZLog.i(message)
}

fun Any.logI(message: Any?, isShowStack: Boolean) {
    ZLog.i(message, isShowStack)
}

fun Any.logI(tag: String?, message: String?) {
    ZLog.i(tag, message)
}

fun Any.logE(message: String) {
    ZLog.e(message)
}

fun Any.logE(message: Any?, isShowStack: Boolean) {
    ZLog.e(message, isShowStack)
}

fun Any.logE(tag: String?, message: Any?) {
    ZLog.e(tag, message)
}


