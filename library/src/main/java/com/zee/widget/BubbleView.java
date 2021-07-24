package com.zee.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import static com.zee.widget.BubbleUtils.dp2px;

/**
 * created by zee on 2021/7/16.
 */
 class BubbleView extends View implements BubbViewInterFace {

    private Paint mBubblePaint;
    private Path mBubblePath;
    private RectF mBubbleRectF;
    private Rect mRect;
    private String mProgressText = "";

    private int mBubbleRadius = 0;
    private int mBubbleColor = 0;
    private int mBubbleTextSize = 0;
    private int mBubbleTextColor = 0;


    BubbleView(Context context) {
        this(context, null);
    }


    BubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBubblePaint = new Paint();
        mBubblePaint.setAntiAlias(true);
        mBubblePaint.setTextAlign(Paint.Align.CENTER);
        mBubblePath = new Path();
        mBubbleRectF = new RectF();
        mRect = new Rect();
    }

    @Override
    public void setValue(int bubbleRadius, int bubbleColor, int bubbleTextSize, int bubbleTextColor) {
        this.mBubbleRadius = bubbleRadius;
        this.mBubbleColor = bubbleColor;
        this.mBubbleTextSize = bubbleTextSize;
        this.mBubbleTextColor = bubbleTextColor;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(3 * mBubbleRadius, 3 * mBubbleRadius);

        mBubbleRectF.set(getMeasuredWidth() / 2f - mBubbleRadius, 0,
                getMeasuredWidth() / 2f + mBubbleRadius, 2 * mBubbleRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBubblePath.reset();
        float x0 = getMeasuredWidth() / 2f;
        float y0 = getMeasuredHeight() - mBubbleRadius / 3f;
        mBubblePath.moveTo(x0, y0);
        float x1 = (float) (getMeasuredWidth() / 2f - Math.sqrt(3) / 2f * mBubbleRadius);
        float y1 = 3 / 2f * mBubbleRadius;
        mBubblePath.quadTo(
                x1 - dp2px(2), y1 - dp2px(2),
                x1, y1
        );
        mBubblePath.arcTo(mBubbleRectF, 150, 240);

        float x2 = (float) (getMeasuredWidth() / 2f + Math.sqrt(3) / 2f * mBubbleRadius);
        mBubblePath.quadTo(
                x2 + dp2px(2), y1 - dp2px(2),
                x0, y0
        );
        mBubblePath.close();

        mBubblePaint.setColor(mBubbleColor);
        canvas.drawPath(mBubblePath, mBubblePaint);

        mBubblePaint.setTextSize(mBubbleTextSize);
        mBubblePaint.setColor(mBubbleTextColor);
        mBubblePaint.getTextBounds(mProgressText, 0, mProgressText.length(), mRect);
        Paint.FontMetrics fm = mBubblePaint.getFontMetrics();
        float baseline = mBubbleRadius + (fm.descent - fm.ascent) / 2f - fm.descent;
        canvas.drawText(mProgressText, getMeasuredWidth() / 2f, baseline, mBubblePaint);
    }


    public void setProgressText(String progressText) {
        if (progressText != null && !mProgressText.equals(progressText)) {
            mProgressText = progressText;
            invalidate();
        }
    }
}
