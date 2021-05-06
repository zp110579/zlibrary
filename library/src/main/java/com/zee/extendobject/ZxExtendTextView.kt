package com.zee.extendobject

import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.widget.TextView
import com.zee.utils.UIUtils

fun TextView.setTextExA(isRight: Boolean, text1: String, text2: String): TextView {
    text = if (isRight) {
        text1
    } else {
        text2
    }
    return this
}

fun TextView.setTextColorExB(isRight: Boolean, @ColorRes color1: Int, @ColorRes color2: Int): TextView {
    if (isRight) {
        setTextColorEx(color1)
    } else {
        setTextColorEx(color2)
    }
    return this
}

fun TextView.setTextColorExB(index: Int, vararg colors: Int): TextView {
    if (index < colors.size && index > -1) {
        setTextColorEx(colors[index])
    } else {
        setTextColorEx(colors[0])
    }
    return this
}

fun TextView.setTextColorExB(isRight: Boolean, color1: String, color2: String): TextView {
    if (isRight) {
        setTextColorEx(color1)
    } else {
        setTextColorEx(color2)
    }
    return this
}

fun TextView.setTextColorExB(index: Int, vararg colors: String):TextView {
    if (index < colors.size && index > -1) {
        setTextColorEx(colors[index])
    } else {
        setTextColorEx(colors[0])
    }
    return this
}

fun TextView.setTextForm100s(txt: String): TextView {
    if (!text.isNullOrEmpty() && text.contains("%s")) {
        val formatStr = text.toString().format(txt)
        setText(formatStr)
    } else {
        setText(txt)
    }
    return this
}

fun TextView.setAssetFontEx(path: String): TextView {
    typeface = Typeface.createFromAsset(context.assets, path)
    return this
}

fun TextView.setFontEx(path: String): TextView {
    typeface = Typeface.createFromAsset(context.assets, path)
    return this
}


fun TextView.setTextColorEx(@ColorRes colorID: Int): TextView {
    setTextColor(UIUtils.getColor(colorID))
    return this
}

fun TextView.setTextColorEx(color: String): TextView {
    setTextColor(Color.parseColor(color))
    return this
}

fun TextView.setDrawableLeftEx(@DrawableRes resID: Int): TextView {
    setCompoundDrawablesWithIntrinsicBounds(resID, 0, 0, 0)
    return this
}

fun TextView.setDrawableTopEx(@DrawableRes resID: Int): TextView {
    setCompoundDrawablesWithIntrinsicBounds(0, resID, 0, 0)
    return this
}

fun TextView.setDrawableRightEx(@DrawableRes resID: Int): TextView {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, resID, 0)
    return this
}

fun TextView.setDrawableBottomEx(@DrawableRes resID: Int): TextView {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resID)
    return this
}

