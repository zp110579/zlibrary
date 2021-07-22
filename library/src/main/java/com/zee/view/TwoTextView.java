package com.zee.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zee.libs.R;

import java.util.ArrayList;
import java.util.List;

public class TwoTextView extends LinearLayout {
    private TextView mTextView;
    private TextView mRightTextView;


    public TwoTextView(Context context) {
        this(context, null);
    }

    public TwoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TwoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);


        mTextView = new TextView(getContext());
        mRightTextView = new TextView(getContext());
        LinearLayout.LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams rightTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TwoTextView);
        int gap = ta.getDimensionPixelSize(R.styleable.TwoTextView_zv_gap, 0);
        String text = ta.getString(R.styleable.TwoTextView_zv_text);
        int textColor = ta.getColor(R.styleable.TwoTextView_zv_textColor, 0);
        float textSize = ta.getDimension(R.styleable.TwoTextView_zv_textSize, 15);
        mTextView.setText(text);
        mTextView.setTextColor(textColor);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);


        String rightText = ta.getString(R.styleable.TwoTextView_zv_rightText);
        float rightTextSize = ta.getDimension(R.styleable.TwoTextView_zv_rightTextSize, textSize);
        if (rightTextSize == 0) {
            rightTextSize = textSize;
        }
        int rightTextColor = ta.getColor(R.styleable.TwoTextView_zv_rightTextColor, textColor);
        mRightTextView.setText(rightText);

        mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        mRightTextView.setTextColor(rightTextColor);
        if (getOrientation() == VERTICAL) {
            rightTextParams.setMargins(0, gap, 0, 0);
        } else {

            rightTextParams.setMargins(gap, 0, 0, 0);
        }

        mTextView.setLayoutParams(textParams);
        mRightTextView.setLayoutParams(rightTextParams);
        addView(mTextView);
        addView(mRightTextView);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setText(@StringRes int resid) {
        mTextView.setText(resid);
    }

    public float getTextSize() {
        return mTextView.getTextSize();
    }

    public void setRightText(String rightText) {
        mRightTextView.setText(rightText);
    }

    public void setRightText(@StringRes int resid) {
        mRightTextView.setText(resid);
    }

}
