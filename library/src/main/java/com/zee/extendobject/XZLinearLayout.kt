import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.annotation.Size
import android.text.TextUtils
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.TextView
import com.zee.utils.UIUtils

/**
 *created by zee on 2020/12/6.
 *
 */


fun VerticalLayout(layout: LinearLayout.() -> Unit): LinearLayout {
    val linearLayout = LinearLayout(UIUtils.getCurActivity())
    linearLayout.orientation = LinearLayout.VERTICAL
    layout.invoke(linearLayout);
    return linearLayout
}

fun ViewGroup.HorizontalLayout(layout: LinearLayout.() -> Unit): LinearLayout {
    val linearLayout = LinearLayout(UIUtils.getCurActivity())
    linearLayout.orientation = LinearLayout.HORIZONTAL
    addView(linearLayout)
    layout.invoke(linearLayout)

    return linearLayout;
}

inline fun ViewGroup.layoutParamsEx(
        width: Int = WRAP_CONTENT,
        height: Int = WRAP_CONTENT
): ViewGroup {
    this.layoutParams = LinearLayout.LayoutParams(width, height)
    return this
}

inline fun ViewGroup.text(
        content: String,
        textSize: Int,
        colorString: String = "#333333",
        isBOLD: Boolean = false
): TextView {
    return TextView(context).apply {
        content?.let {
            text = content
        }
        setTextSize(UIUtils.pxToDp(textSize).toFloat())
        setTextColor(
                try {
                    Color.parseColor(colorString)
                } catch (e: Throwable) {
                    Color.parseColor("#333333")
                }
        )
        if (isBOLD) {
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }


        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        }
        this@text.addView(this)
    }
}


