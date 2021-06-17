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
    private var leftTopRadius = 0f
    private var rightTopRadius = 0f
    private var rightBottomRadius = 0f
    private var leftBottomRadius = 0f
    private var radius: Float

    constructor(ctx: Context) : this(ContextThemeWrapper(ctx, R.style.button_no_shadow_style), null)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        gravity = Gravity.CENTER//默认居中显示
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ZxButton)
        startColor = ta.getColor(R.styleable.ZxButton_zv_startColor, Color.TRANSPARENT)
        endColor = ta.getColor(R.styleable.ZxButton_zv_endColor, Color.TRANSPARENT)
        radius = ta.getDimension(R.styleable.ZxButton_zv_radius, 0f)
        leftTopRadius = ta.getDimension(R.styleable.ZxButton_zv_leftTopRadius, 0f)
        rightTopRadius = ta.getDimension(R.styleable.ZxButton_zv_rightTopRadius, 0f)
        leftBottomRadius = ta.getDimension(R.styleable.ZxButton_zv_leftBottomRadius, 0f)
        rightBottomRadius = ta.getDimension(R.styleable.ZxButton_zv_rightBottomRadius, 0f)

        if (radius > 0) {
            if (leftTopRadius == 0f) {
                leftTopRadius = radius
            }
            if (leftBottomRadius == 0f) {
                leftBottomRadius = radius
            }
            if (rightTopRadius == 0f) {
                rightTopRadius = radius
            }
            if (rightBottomRadius == 0f) {
                rightBottomRadius = radius
            }
        }

        ta.recycle()
        val backgroundA: Drawable = background
        if (backgroundA is ColorDrawable) {
            mDefaultBackgroundColor = backgroundA.color
        }

        if (isEnabled) {
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(startColor, endColor)).apply {
                if (leftTopRadius > 0 || rightTopRadius > 0 || rightBottomRadius > 0 || leftBottomRadius > 0) {
                    val radiusArr = floatArrayOf(leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
                            rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius)
                    cornerRadii = radiusArr
                } else {
                    cornerRadius = radius
                }
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