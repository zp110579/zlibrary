package com.zee.toast;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zee.utils.UIUtils;

@SuppressWarnings(value = {"unchecked", "deprecation"})
public class CustomToastBeanSettings {
    private int mPaddingLeft = 15;
    private int mPaddingRight = 15;
    private int mPaddingTop = 5;
    private int mPaddingBottom = 5;
    private int mRadius = 4;
    private int mTextColor = 0XEEFFFFFF;
    private int mBackgroundColor = 0Xff3A3A3A;
    private int mTextSize = 14;


    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        mPaddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        mPaddingRight = paddingRight;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        mPaddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        mPaddingBottom = paddingBottom;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }


    /**
     * 生成默认的 TextView 对象
     */
    TextView createTextView() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getBackgroundColor());
        drawable.setCornerRadius(UIUtils.dpToPx(getRadius()));

        TextView textView = new TextView(UIUtils.getApplication());
        textView.setId(android.R.id.message);
        textView.setTextColor(getTextColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, UIUtils.spToPx(getTextSize()));
        textView.setPadding(UIUtils.dpToPx(getPaddingLeft()), UIUtils.dpToPx(getPaddingTop()), UIUtils.dpToPx(getPaddingRight()), UIUtils.dpToPx(getPaddingBottom()));
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 设置 Z 轴阴影
            textView.setZ(30);
        }
        return textView;
    }
}
