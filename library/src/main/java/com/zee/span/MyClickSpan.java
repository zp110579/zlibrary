package com.zee.span;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class MyClickSpan extends ClickableSpan {
    private int defaultValue = -666666;
    private int color;
    private boolean useUnderLine;
    private View.OnClickListener mOnClickListener;

    public MyClickSpan(int color, boolean useUnderLine, View.OnClickListener onClickListener) {
        this.color = color;
        this.useUnderLine = useUnderLine;
        mOnClickListener=onClickListener;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (widget instanceof TextView) {
            ((TextView) widget).setHighlightColor(widget.getResources().getColor(android.R.color.transparent));
            if (mOnClickListener != null) {
                mOnClickListener.onClick(widget);
            }
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        if (color != defaultValue) {
            ds.setColor(color);
        }
        ds.setUnderlineText(useUnderLine);
    }
}