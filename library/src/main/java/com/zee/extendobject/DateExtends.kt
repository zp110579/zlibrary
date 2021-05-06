package com.zee.extendobject

import com.zee.bean.DateStyle
import com.zee.utils.ZDateUtil
import java.util.*

/**
 *created by zee on 2021/1/13.
 *日期分割
 */
fun Date.dateToString(newDateStyle: DateStyle): String {
    return ZDateUtil.DateToString(this, newDateStyle)
}

fun String.toString(newDateStyle: DateStyle): String {
    return ZDateUtil.StringToString(this, newDateStyle)
}