package com.zee.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 *created by zee on 2020/10/28.
 *SharedPreferences 简单实用
 */
object Z1PrefsUtil {
    private val preferenceManager: SharedPreferences = UIUtils.getSharedPreferences()

    fun getValue(name: String, defaultValue: String = ""): String {
        return preferenceManager.getString(name, defaultValue)
    }

    fun setValue(name: String, value: String?) {
        val editor = preferenceManager.edit()
        editor.putString(name, if (value == null || value.isEmpty()) "" else value)
        editor.apply()
    }

    fun setValueSync(name: String, value: String?): Boolean {
        val editor = preferenceManager.edit()
        editor.putString(name, if (value == null || value.isEmpty()) "" else value)
        return editor.commit()
    }

    fun getValue(name: String, defaultValue: Int): Int {
        return preferenceManager.getInt(name, defaultValue)
    }
    fun getValue(name: String, defaultValue: Float): Float {
        return preferenceManager.getFloat(name, defaultValue)
    }

    fun setValue(name: String, value: Int) {
        val editor = preferenceManager.edit()
        editor.putInt(name, value)
        editor.commit()
    }

    fun setValue(name: String, value: Long) {
        val editor = preferenceManager.edit()
        editor.putLong(name, if (value < 0L) 0L else value)
        editor.apply()
    }

    fun getValue(name: String, defaultValue: Long): Long {

        var result: Long
        try {
            result = preferenceManager.getLong(name, defaultValue)
        } catch (e: Exception) {
            result = preferenceManager.getInt(name, defaultValue.toInt()).toLong()
        }

        return result
    }

    fun getValue(name: String, defaultValue: Boolean): Boolean {
        return preferenceManager.getBoolean(name, defaultValue)
    }

    fun setValue(name: String, value: Boolean) {
        val editor = preferenceManager.edit()
        editor.putBoolean(name, value)
        editor.apply()
    }
    fun setValue(name: String, value: Float) {
        val editor = preferenceManager.edit()
        editor.putFloat(name, value)
        editor.apply()
    }

    fun has(name: String): Boolean {
        return preferenceManager.contains(name)
    }

    fun removeValue(name: String) {
        val editor = preferenceManager.edit()
        editor.remove(name)
        editor.apply()
    }

    fun removeValueSync(name: String): Boolean {
        val editor = preferenceManager.edit()
        editor.remove(name)
        return editor.commit()
    }

    fun clearALl() {
        val editor = preferenceManager.edit()
        editor.clear()
        editor.apply()
    }
}