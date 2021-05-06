package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zee.libs.R;

import java.lang.reflect.Field;


/**
 *
 * @author KathLine
 * @date 2017/1/8
 */
public class ZxStrokeTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean m_bDrawSideLine = true; // 默认采用描边

    private TextPaint m_TextPaint;
    private int mInnerColor;
    private int mOuterColor;
    private float outStrokeWidth = 1;

    public ZxStrokeTextView(Context context, int outerColor, int innnerColor) {
        super(context);
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }

    public ZxStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_TextPaint = this.getPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZxStrokeTextView);
        this.mInnerColor = a.getColor(R.styleable.ZxStrokeTextView_zv_innerColor, 0xffffff);
        this.mOuterColor = a.getColor(R.styleable.ZxStrokeTextView_zv_outerColor, 0xffffff);
        this.outStrokeWidth = a.getDimension(R.styleable.ZxStrokeTextView_zv_outerStrokeWidth, 1);
    }

    public ZxStrokeTextView(Context context, AttributeSet attrs, int defStyle, int outerColor, int innnerColor) {
        super(context, attrs, defStyle);
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (m_bDrawSideLine) {
            // 描外层
            setTextColorUseReflection(mOuterColor);
            m_TextPaint.setStrokeWidth(outStrokeWidth);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            super.onDraw(canvas);

            // 描内层，恢复原先的画笔
            setTextColorUseReflection(mInnerColor);
            m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        super.onDraw(canvas);
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     *
     * @param color
     */
    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }
}
