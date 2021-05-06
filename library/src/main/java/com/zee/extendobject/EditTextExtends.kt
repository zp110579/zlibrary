package com.zee.extendobject

import android.widget.EditText
import com.zee.utils.UIUtils

fun EditText.showKeyboard(delayMillis: Long = 200): EditText {
    postDelayed({
        performClick()
        UIUtils.showKeyboard(this)
    }, delayMillis)
    return this
}

