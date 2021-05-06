package com.zee.extendobject

import android.app.Activity
import android.content.Intent
import com.zee.utils.UIUtils

fun Activity.startActivityEx(paClass: Class<*>) {
    val loIntent = Intent(this, paClass)
    startActivity(loIntent)
}

