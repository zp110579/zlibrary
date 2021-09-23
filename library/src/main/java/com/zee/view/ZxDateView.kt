package com.zee.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bigkoo.pickerview.view.WheelTime
import com.contrarywind.view.WheelView
import com.zee.extendobject.setVisibleOrGone
import com.zee.libs.R
import java.util.*
import kotlin.collections.ArrayList

/**
 *created by zee on 2021/3/26.
 *
 */
class ZxDateView : LinearLayout {
    val mWheelViewList = ArrayList<WheelView>()

    constructor(ctx: Context) : this(ContextThemeWrapper(ctx, -1), null)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
//        attrs.
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ZxDateView)
        val textSize = ta.getDimension(R.styleable.ZxDateView_zv_textSize, 14f)//获得字体的大小
        val textColor = ta.getColor(R.styleable.ZxDateView_zv_textColor, 0)//获得字体颜色
        val textSelectColor = ta.getColor(R.styleable.ZxDateView_zv_textSelectColor, 0)//获得选中字体颜色
        val dividerSelectColor = ta.getColor(R.styleable.ZxDateView_zv_divider_color, 0)//分隔条的颜色
        val dividerWidth = ta.getDimensionPixelSize(R.styleable.ZxDateView_zv_divider_width, 0)//分隔条的宽度
        val showYear = ta.getBoolean(R.styleable.ZxDateView_zv_show_year, false)//是否显示年轮
        val showMonth = ta.getBoolean(R.styleable.ZxDateView_z_show_month, false)//是否显示月
        val showDay = ta.getBoolean(R.styleable.ZxDateView_zv_show_day, false)//是否显示天
        val showHour = ta.getBoolean(R.styleable.ZxDateView_zv_show_hour, false)//是否显示时
        val showMin = ta.getBoolean(R.styleable.ZxDateView_zv_show_min, false)//是否显示分
        val showSecond = ta.getBoolean(R.styleable.ZxDateView_zv_show_second, false)//是否显示分
        val startTime = ta.getInt(R.styleable.ZxDateView_zv_startYear, 1900)
        val endTime = ta.getInt(R.styleable.ZxDateView_zv_endYear, 1900)
        var textGravity = 1;
        val startCalendar = Calendar.getInstance()
        startCalendar.set(startTime, 0, 0, 0, 0, 0)
        val endCalendar = Calendar.getInstance()
        endCalendar.set(startTime, 0, 0, 0, 0, 0)
        LayoutInflater.from(context).inflate(R.layout.lib_zx_dateview, this)
        val show = booleanArrayOf(showYear, showMonth, showDay, showHour, showMin, showSecond)//显示类型，默认显示： 年月日

        val wheelTime = WheelTime(this, show, textGravity, textSize.toInt())
        wheelTime.apply {
            setDividerColor(dividerSelectColor)
            setTextColorCenter(textSelectColor)
            setTextColorOut(textColor)
            setRangDate(startCalendar, endCalendar)
        }
        ta.recycle()
    }

    fun initViews(list: Array<Boolean>) {
        mWheelViewList.forEachIndexed { index, wheelView ->
            wheelView.setVisibleOrGone(list[index])
        }
    }

    fun setDividerColor(color: Int, height: Int) {
        mWheelViewList.forEach {
            it.setDividerColor(color)
            it.setDividerWidth(height)
        }
    }

    fun setTextColor(color: Int, colorSelect: Int) {
        mWheelViewList.forEach {
            it.setTextColorOut(color)
            it.setTextColorCenter(colorSelect)
        }
    }

    fun setTextSize(textSize: Float) {
        mWheelViewList.forEach {
            it.setTextSize(textSize)
        }
    }

}