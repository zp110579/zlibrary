import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.zee.extendobject.toColor
import com.zee.utils.UIUtils

/**
 *created by zee on 2020/10/20.
 * 代码生成Shape
 */

inline fun View.setBackgroundEx(fill: GradientDrawable.() -> Unit) {
    val mBackground = GradientDrawable().apply {
        gradientType = GradientDrawable.LINEAR_GRADIENT
        fill()
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
        background = mBackground
    } else {
        setBackgroundDrawable(mBackground)
    }
}


/**
 * 设置边框
 */
inline fun GradientDrawable.setStroke(fill: ShapeStroke.() -> Unit) {
    ShapeStroke().also {
        it.fill()
        setStroke(it.mWidth, it.mColor, it.mDashWidth, it.mDashGap)
    }
}

/**
 * 设置圆角
 */
inline fun GradientDrawable.setRadius(fill: ShapeRadius.() -> Unit) {
    ShapeRadius().also {
        it.fill()
        cornerRadii = it.render()
    }
}

/**
 * 背景颜色填充
 */
var GradientDrawable.solidColor: Int
    set(value) = setColor(value)
    @Deprecated(message = "NO_GETTER", level = DeprecationLevel.HIDDEN) get() = error("NO_GETTER")

/**
 * 背景渐变色
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
fun GradientDrawable.setGradient(fill: ShapeGradient.() -> Unit) {
    ShapeGradient().also {
        it.fill()
        orientation = it.mOrientation
        colors = intArrayOf(it.mStartColor, it.mEndColor)
    }
}

/**
 * 设置圆角的背景
 */
inline fun View.setBackgroundEx(
        solidColor1: Int,
        radius1: Float? = null,
        strokeColor: Int? = null,
        strokeWidth: Int? = null
) = setBackgroundEx {
    solidColor = solidColor1

    radius1?.apply {
        cornerRadius = radius1
    }
    strokeColor?.apply {
        strokeWidth?.apply {
            setStroke(strokeWidth, strokeColor)
        }
    }
}

/**
 * 设置圆角的背景
 */
inline fun View.setBackgroundEx2(
        solidColor1: Int,
        radiusDpValue: Int = 0,
        strokeColor: Int? = null,
        strokeWidth: Int? = null
) = setBackgroundEx {
    solidColor = solidColor1
    if (radiusDpValue > 0) {
        cornerRadius = UIUtils.dpToPx(radiusDpValue).toFloat()
    }
    strokeColor?.apply {
        strokeWidth?.apply {
            setStroke(strokeWidth, strokeColor)
        }
    }
}

/**
 * 设置圆角的背景
 */
inline fun View.setBackgroundEx(
        solidColor1: String,
        radius1: Float? = null,
        strokeColor: Int? = null,
        strokeWidth: Int = 1
) = setBackgroundEx {
    solidColor = solidColor1.toColor()
    radius1?.apply {
        cornerRadius = radius1
    }
    strokeColor?.apply {
        setStroke(strokeWidth, strokeColor)
    }
}

