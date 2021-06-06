package com.zee.extendobject

import com.zee.utils.Z1PrefsUtil

/**
 *created by zee on 2021/4/30.
 * SharedPreferences 保存数据及读取数据
 */

fun spGetValue(key: String, defValue: String = ""): String {
    return Z1PrefsUtil.getValue(key, defValue)
}

fun spGetIntValue(key: String, defValue: Int = 0): Int {
    return Z1PrefsUtil.getValue(key, defValue)
}

fun spGetFloatValue(key: String, defValue: Float = 0f): Float {
    return Z1PrefsUtil.getValue(key, defValue)
}

fun spGetBooleanValue(key: String, defValue: Boolean = false): Boolean {
    return Z1PrefsUtil.getValue(key, defValue)
}

/**
 * SharedPreferences 保存String
 */
fun spSaveValue(key: String, defValue: String?) {
    Z1PrefsUtil.setValue(key, defValue)
}

/**
 * SharedPreferences 保存Int
 */
fun spSaveValue(key: String, defValue: Int) {
    Z1PrefsUtil.setValue(key, defValue)
}

/**
 * SharedPreferences 保存Float
 */
fun spSaveValue(key: String, defValue: Float) {
    Z1PrefsUtil.setValue(key, defValue)
}

/**
 * SharedPreferences 保存Boolean
 */
fun spSaveValue(key: String, defValue: Boolean) {
    Z1PrefsUtil.setValue(key, defValue)
}

fun spContains(key: String): Boolean {
    return Z1PrefsUtil.has(key)
}

fun spRemove(key: String) {
    Z1PrefsUtil.removeValue(key)
}