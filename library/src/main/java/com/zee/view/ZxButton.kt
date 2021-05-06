package com.zee.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import com.zee.libs.R

/**
 *created by zee on 2021/3/26.
 *
 */
class ZxButton : android.support.v7.widget.AppCompatButton {


    private var startColor: Int

    //默认的背景颜色
    private var mDefaultBackgroundColor: Int = 0
    private var endColor: Int

    private var radius: Float

    constructor(ctx: Context) : this(ContextThemeWrapper(ctx, R.style.button_no_shadow_style), null)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        gravity = Gravity.CENTER//默认居中显示
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZxButton)
        startColor = typedArray.getColor(R.styleable.ZxButton_zv_startColor, Color.TRANSPARENT)
        endColor = typedArray.getColor(R.styleable.ZxButton_zv_endColor, Color.TRANSPARENT)
        radius = typedArray.getDimension(R.styleable.ZxButton_zv_radius, 0f)
        typedArray.recycle()
        val backgroundA: Drawable = background
        if (backgroundA is ColorDrawable) {
            mDefaultBackgroundColor = backgroundA.color
        }

        if (isEnabled) {
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(startColor, endColor)).apply {
                cornerRadius = radius
            }
        }

        //设置区分大小写
        setAllCaps(false)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(startColor, endColor)).apply {
                cornerRadius = radius
            }
        } else {
            background = GradientDrawable().apply {
                cornerRadius = radius
                setColor(mDefaultBackgroundColor)
            }
        }
    }
}