import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorRes
import com.zee.extendobject.toColor
import com.zee.utils.UIUtils

/**
 *created by zee on 2020/10/20.
 *
 */

class ShapeStroke {
    var mWidth: Int = -1
    var mColor: Int = -1
    var mDashWidth: Float = 0F
    var mDashGap: Float = 0F

    fun setColor(@ColorRes color1: Int) {
        mColor = UIUtils.getColor(color1)
    }

    fun setColor(color1: String) {
        mColor = color1.toColor()
    }
}


class ShapeRadius {
    var mRadius: Float = 0F

    var mTopLeft: Float = Float.NaN
    var mTopRight: Float = Float.NaN
    var mBottomLeft: Float = Float.NaN
    var mBottomRight: Float = Float.NaN

    internal fun Float.orRadius(): Float =
            takeIf { it >= 0 } ?: mRadius
}

fun ShapeRadius.render(): FloatArray =
        floatArrayOf(
                mTopLeft.orRadius(),
                mTopLeft.orRadius(),

                mTopRight.orRadius(),
                mTopRight.orRadius(),

                mBottomRight.orRadius(),
                mBottomRight.orRadius(),

                mBottomLeft.orRadius(),
                mBottomLeft.orRadius()
        )

class ShapeGradient {
    var mStartColor: Int = -1
    var mEndColor: Int = -1

    var mOrientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT

    fun setGradientColor(startColor: String, endColor: String) {
        mStartColor = startColor.toColor()
        mEndColor = endColor.toColor()
    }

    fun setGradientColor(@ColorRes startColor: Int, @ColorRes endColor: Int) {
        mStartColor = UIUtils.getColor(startColor)
        mEndColor = UIUtils.getColor(endColor)
    }
}

typealias OrientationObject = GradientDrawable.Orientation