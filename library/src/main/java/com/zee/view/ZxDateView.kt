package com.zee.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.contrarywind.view.WheelView
import com.zee.libs.R

/**
 *created by zee on 2021/3/26.
 *
 */
class ZxDateView : LinearLayout {


    constructor(ctx: Context) : this(ContextThemeWrapper(ctx, -1), null)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        LayoutInflater.from(context).inflate(R.layout.lib_zx_dateview, this)
        val yearView = findViewById<WheelView>(R.id.wl_year)
        val monthView = findViewById<WheelView>(R.id.wl_month)
        val dayView = findViewById<WheelView>(R.id.wl_day)
        val hourView = findViewById<WheelView>(R.id.wl_hour)
        val minView = findViewById<WheelView>(R.id.wl_min)
        val secondView = findViewById<WheelView>(R.id.wl_second)
    }

}