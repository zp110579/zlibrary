package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zee.libs.R;

public class LRTextView extends ZxRelativeLayout {
    private TextView mTextView;

    private TextView mRightTextView;

    public LRTextView(Context context) {
        this(context, null);
    }

    public LRTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mTextView = new TextView(getContext());
        mRightTextView = new TextView(getContext());
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rightTextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LRTextView);
        String text = ta.getString(R.styleable.LRTextView_zv_text);

        int textStyle = ta.getInt(R.styleable.LRTextView_zv_textStyle, 0);
        if (textStyle > 0) {
            if (textStyle == 1) {
                mTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (textStyle == 2) {
                mTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else {
                mTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            }
        }

        int textColor = ta.getColor(R.styleable.LRTextView_zv_textColor, 0);
        float textSize = ta.getDimension(R.styleable.LRTextView_zv_textSize, 0);
        mTextView.setText(text);
        if (textColor != 0) {
            mTextView.setTextColor(textColor);
        }
        if (textSize > 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        String rightText = ta.getString(R.styleable.LRTextView_zv_rightText);
        float rightTextSize = ta.getDimension(R.styleable.LRTextView_zv_rightTextSize, 0);
        if (rightTextSize == 0) {
            rightTextSize = textSize;
        }
        int rightTextColor = ta.getColor(R.styleable.LRTextView_zv_rightTextColor, textColor);
        mRightTextView.setText(rightText);

        int rightTextStyle = ta.getInt(R.styleable.LRTextView_zv_rightTextStyle, 0);

        if (rightTextStyle > 0) {
            if (rightTextStyle == 1) {
                mRightTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (textStyle == 2) {
                mRightTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else {
                mRightTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            }
        }

        if (rightTextSize > 0) {
            mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        }
        if (rightTextColor != 0) {
            mRightTextView.setTextColor(rightTextColor);
        }

        textParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mTextView.setLayoutParams(textParams);
        rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mRightTextView.setLayoutParams(rightTextParams);
        addView(mTextView);
        addView(mRightTextView);
    }

    public TextView setText(String text) {
        mTextView.setText(text);
        return mTextView;
    }

    public TextView setText(@StringRes int resid) {
        mTextView.setText(resid);
        return mTextView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public TextView getRightTextView() {
        return mRightTextView;
    }

    public float getTextSize() {
        return mTextView.getTextSize();
    }

    public TextView setRightText(String rightText) {
        mRightTextView.setText(rightText);
        return mRightTextView;
    }

    public TextView setRightText(@StringRes int resid) {
        mRightTextView.setText(resid);
        return mRightTextView;
    }

}

