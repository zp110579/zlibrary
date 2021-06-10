package com.zee.extendobject

import MarginBean
import PaddingBean
import android.graphics.Color
import android.support.annotation.ColorRes
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.zee.base.OnNoDoubleClickListener
import com.zee.utils.UIUtils
import com.zee.utils.ViewSaveImageUtils
import org.jetbrains.annotations.Nullable

/**
 * 将View保存成本地图片，并刷新图库
 */

/**
 * 防止快速点击运行2次
 */
fun View.setOnNoDoubleClickListener(unit: () -> Unit): View {
    setOnClickListener(object : OnNoDoubleClickListener(2000) {
        override fun onNoDoubleClick(v: View) {
            unit.invoke()
        }
    })
    return this
}

fun View.saveImage(fileName: String, result: (code: Int) -> Unit = {}): View {
    ViewSaveImageUtils.saveImg(this, fileName, result)
    return this
}

fun View.setGone(isGone: Boolean): View {
    if (isGone) {
        setGone()
    }
    return this
}

fun View.setInVisible(isInVisible: Boolean): View {
    if (isInVisible) {
        setInVisible()
    }
    return this
}

fun View.setVisible(isVisible: Boolean): View {
    if (isVisible) {
        setVisible()
    }
    return this
}

fun View.setVisibleOrInVisible(isVisible: Boolean): View {
    if (isVisible) {
        setVisible()
    } else {
        setInVisible()
    }
    return this
}

fun View.setVisibleOrGone(isVisible: Boolean): View {
    if (isVisible) {
        setVisible()
    } else {
        setGone()
    }
    return this
}

fun View.removeFromParentView(): View {
    UIUtils.removeFromParentView(this)
    return this
}

fun View.isGoneRun(run: () -> Unit): View {
    if (isGone()) {
        run.invoke()
    }
    return this
}

fun View.isVisibleRun(run: () -> Unit): View {
    if (isVisible()) {
        run.invoke()
    }
    return this
}

fun View.setPadding(init: PaddingBean.() -> Unit): View {
    PaddingBean().apply {
        init()

        val left = (if (leftPadding > 0) leftPadding else padding).dp2px()
        val top = (if (topPadding > 0) topPadding else padding).dp2px()
        val right = (if (rightPadding > 0) rightPadding else padding).dp2px()
        val bottom = (if (bottomPadding > 0) bottomPadding else padding).dp2px()
        setPadding(left, top, right, bottom)
    }
    return this
}

fun View.setMargin(init: MarginBean.() -> Unit): View {

    when (layoutParams) {
        is LinearLayout.LayoutParams -> {
            (layoutParams as LinearLayout.LayoutParams).apply {
                MarginBean().apply {
                    init()
                    setMargins(leftMargin.dp2px(), topMargin.dp2px(), rightMargin.dp2px(), bottomMargin.dp2px())
                }
            }
        }
        is RelativeLayout.LayoutParams -> {
            (layoutParams as RelativeLayout.LayoutParams).apply {
                MarginBean().apply {
                    init()
                    setMargins(leftMargin.dp2px(), topMargin.dp2px(), rightMargin.dp2px(), bottomMargin.dp2px())
                }
            }
        }
        is FrameLayout.LayoutParams -> {
            (layoutParams as FrameLayout.LayoutParams).apply {
                MarginBean().apply {
                    init()
                    setMargins(leftMargin.dp2px(), topMargin.dp2px(), rightMargin.dp2px(), bottomMargin.dp2px())
                }
            }
        }
    }
    return this
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isInVisible(): Boolean {
    return visibility == View.INVISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}

fun View.setVisible(): View {
    visibility = View.VISIBLE;
    return this
}

fun View.setInVisible(): View {
    visibility = View.INVISIBLE;
    return this
}

fun View.setGone(): View {
    visibility = View.GONE;
    return this
}

fun View.setBackgroundColorEx(@ColorRes colorId: Int): View {
    setBackgroundColor(UIUtils.getColor(colorId))
    return this
}

fun View.setBackgroundColorEx(colorId: String): View {
    setBackgroundColor(Color.parseColor(colorId))
    return this
}

fun View.setOnClickListenerEx(run: () -> Unit = {}, time: Int = 2000): View {
    setOnClickListener(object : OnNoDoubleClickListener(time) {
        override fun onNoDoubleClick(v: View) {
            run.invoke()
        }
    })
    return this
}

fun View.setOnClickListenerEx(@Nullable onClickListener: View.OnClickListener, value: Int = 2000): View {
    setOnClickListener(object : OnNoDoubleClickListener(value) {
        override fun onNoDoubleClick(v: View) {
            onClickListener.onClick(v)
        }
    })
    return this
}

