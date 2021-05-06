package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.zee.libs.R;
import com.zee.utils.SpanUtils;

@SuppressWarnings(value = {"unchecked", "deprecation"})
public class ZxFoldTextView extends AppCompatTextView {
    private static final String ELLIPSIZE_END = "...";
    private static final int MAX_LINE = 4;
    private int mShowMaxLine;
    /**
     * 折叠文本
     */
    private String mFoldText = "  ";

    /**
     * 原始文本
     */
    private CharSequence mOriginalText;
    /**
     * 是否展开
     */
    private boolean isExpand;

    private boolean flag;

    private int addEndWidth = 40;//尾部增加透明的宽度
    private OnFoldChangeListener mOnFoldChangeListener;

    public ZxFoldTextView(Context context) {
        this(context, null);
    }


    public ZxFoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    public ZxFoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShowMaxLine = MAX_LINE;
        if (attrs != null) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ZxFoldTextView);
            mShowMaxLine = arr.getInt(R.styleable.ZxFoldTextView_zv_showMaxLine, MAX_LINE);
            addEndWidth = arr.getInt(R.styleable.ZxFoldTextView_zv_addEndWidth, 40);
            isExpand = arr.getBoolean(R.styleable.ZxFoldTextView_zv_expand, false);
            arr.recycle();
        }

        String curText = getText().toString();
        if (!TextUtils.isEmpty(curText)) {
            setText(curText);
        }
    }

    private void addTip(SpannableStringBuilder span, BufferType type) {
        super.setText(span, type);
    }

    @Override
    public void setText(final CharSequence text, final BufferType type) {
        if (TextUtils.isEmpty(text) || mShowMaxLine == 0) {
            super.setText(text, type);
            if (mOnFoldChangeListener != null) {
                mOnFoldChangeListener.onViewVisible(View.GONE);
            }
        } else if (isExpand) {
            //文字展开
            super.setText(text, type);
        } else {
            if (!flag) {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        flag = true;
                        formatText(text, type);
                        return true;
                    }
                });
            } else {
                formatText(text, type);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void formatText(CharSequence text, final BufferType type) {
        if (TextUtils.isEmpty(mOriginalText)) {
            mOriginalText = text;
        }
        Layout layout = getLayout();
        if (layout == null || !layout.getText().equals(mOriginalText)) {
            super.setText(mOriginalText, type);
            layout = getLayout();
        }
        if (layout == null) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    translateText(getLayout(), type);
                }
            });
        } else {
            translateText(layout, type);
        }
    }


    private void translateText(Layout layout, BufferType type) {
        if (layout != null && layout.getLineCount() > mShowMaxLine) {//如果当前显示的内容的行数大于设置显示的行数
            SpannableStringBuilder span = new SpannableStringBuilder();
            int start = layout.getLineStart(mShowMaxLine - 1);
            int end = layout.getLineVisibleEnd(mShowMaxLine - 1);
            TextPaint paint = getPaint();
            StringBuilder builder = new StringBuilder(ELLIPSIZE_END);
//            if (mTipGravity == END) {
            builder.append(mFoldText);
            end -= paint.breakText(mOriginalText, start, end, false, paint.measureText(builder.toString()), null) + 1;
            float x = getWidth() - getPaddingLeft() - getPaddingRight() - getTextWidth(ELLIPSIZE_END.concat(mFoldText)) - getTextWidth("宽度");

            while (layout.getPrimaryHorizontal(end - 1) + getTextWidth(mOriginalText.subSequence(end - 1, end).toString()) < x) {
                end++;
                if (end > mOriginalText.length()) {
                    break;
                }
            }
//            end -= 2;
//            } else {
//                end -= paint.breakText(mOriginalText, start, end, false, paint.measureText(builder.toString()), null) + 1;
//            }
            if (end > mOriginalText.length()) {
                end = mOriginalText.length();
            }
            CharSequence ellipsize = mOriginalText.subSequence(0, end);
            span.append(ellipsize);
            span.append(ELLIPSIZE_END);
            addTip(span, type);
            if (mOnFoldChangeListener != null) {
                mOnFoldChangeListener.onViewVisible(View.VISIBLE);
            }
        } else if (mOnFoldChangeListener != null) {
            SpannableStringBuilder span = new SpannableStringBuilder(mOriginalText);
            addTip(span, type);
            if (mOnFoldChangeListener != null) {
                mOnFoldChangeListener.onViewVisible(View.GONE);
            }
        }
    }

    public void setOnFoldChangeListener(OnFoldChangeListener onFoldChangeListener) {
        mOnFoldChangeListener = onFoldChangeListener;
        if (!TextUtils.isEmpty(mOriginalText)) {
            if (getLineCount() < mShowMaxLine) {//如果当前显示的行数小于设置的最大显示行数，那就不显示
                mOnFoldChangeListener.onViewVisible(View.GONE);
            } else {
                mOnFoldChangeListener.onViewVisible(View.VISIBLE);
            }
        } else {
            mOnFoldChangeListener.onViewVisible(View.GONE);
        }
    }

    public void setShowMaxLine(int mShowMaxLine) {
        this.mShowMaxLine = mShowMaxLine;
    }


    public void foldChangText() {
        isExpand = !isExpand;
        if (!TextUtils.isEmpty(mOriginalText)) {
            setText(new SpanUtils().append(mOriginalText).appendSpace(addEndWidth).create());
        }
        if (mOnFoldChangeListener != null) {
            mOnFoldChangeListener.onFoldChange(isExpand);
        }
    }

    private float getTextWidth(String text) {
        Paint paint = getPaint();
        return paint.measureText(text);
    }

    public interface OnFoldChangeListener {
        /**
         * 展开的按钮是否显示
         *
         * @param visibility
         */
        void onViewVisible(int visibility);

        /**
         * 当前状态
         *
         * @param isFold
         */
        void onFoldChange(boolean isFold);
    }
}
