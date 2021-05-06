package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zee.libs.R;


/**
 * @author Administrator
 */
public class ZxRoundImageView extends AppCompatImageView {
    private float leftTopRadius;
    private float rightTopRadius;
    private float rightBottomRadius;
    private float leftBottomRadius;
    protected Path borderShapePath;
    private boolean isRadio = true;

    private static final int DEFAULT_BORDER_COLOR = Color.GRAY;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMEN = 2;

    private boolean mIsSelected = false;
//    private boolean mIsOval = false;
//    private boolean mIsCircle = false;

    private int mBorderWidth;
    private int mBorderColor;

    private int mSelectedBorderWidth;
    private int mSelectedBorderColor;
    private int mSelectedMaskColor;
    private boolean mIsTouchSelectModeEnabled = true;

    private int mCornerRadius;

    private Paint mBitmapPaint;
    private Paint mBorderPaint;
    private ColorFilter mColorFilter;
    private ColorFilter mSelectedColorFilter;
    private BitmapShader mBitmapShader;
    private boolean mNeedResetShader = false;
    private RectF mRectF = new RectF();
    private Bitmap mBitmap;

    private Matrix mMatrix;
    private int mWidth;
    private int mHeight;
    private int mType = 0;//0:默认圆形,1:4个角统一圆角2:其他的圆形
    private float mRateValue = 0;//宽度和高度的比例

    public ZxRoundImageView(Context context) {
        this(context, null, 0);
    }

    public ZxRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();
        borderShapePath = new Path();


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZxRoundImageView, defStyleAttr, 0);

        mBorderWidth = array.getDimensionPixelSize(R.styleable.ZxRoundImageView_zv_borderWidth, 0);
        mBorderColor = array.getColor(R.styleable.ZxRoundImageView_zv_borderColor, DEFAULT_BORDER_COLOR);
        mSelectedBorderWidth = array.getDimensionPixelSize(R.styleable.ZxRoundImageView_zv_press_bordWidth, mBorderWidth);
        mSelectedBorderColor = array.getColor(R.styleable.ZxRoundImageView_zv_press_bordColor, mBorderColor);
        mSelectedMaskColor = array.getColor(R.styleable.ZxRoundImageView_zv_press_maskColor, Color.TRANSPARENT);
        leftTopRadius = array.getDimension(R.styleable.ZxRoundImageView_zv_leftTopRadius, 0);
        rightTopRadius = array.getDimension(R.styleable.ZxRoundImageView_zv_rightTopRadius, 0);
        rightBottomRadius = array.getDimension(R.styleable.ZxRoundImageView_zv_rightBottomRadius, 0);
        leftBottomRadius = array.getDimension(R.styleable.ZxRoundImageView_zv_leftBottomRadius, 0);
        mCornerRadius = array.getDimensionPixelSize(R.styleable.ZxRoundImageView_zv_radius, 0);

        int rateWidth = array.getDimensionPixelSize(R.styleable.ZxRoundImageView_zv_rate_width, 0);
        int rateHeight = array.getDimensionPixelSize(R.styleable.ZxRoundImageView_zv_rate_height, 0);
        if (rateWidth != 0 && rateHeight != 0) {
            mRateValue = rateWidth * 1.0f / rateHeight;
            mType = 1;
        }

        if (mSelectedMaskColor != Color.TRANSPARENT) {
            mSelectedColorFilter = new PorterDuffColorFilter(mSelectedMaskColor, PorterDuff.Mode.DARKEN);
        }

        mIsTouchSelectModeEnabled = true;
        if (leftTopRadius + leftBottomRadius + rightTopRadius + rightBottomRadius != 0) {
            isRadio = false;
        }

        if (mCornerRadius < 0) {//说明是圆形
            setScaleType(ScaleType.CENTER_CROP);
        }
        float vlaueA = leftBottomRadius + leftTopRadius + rightBottomRadius + rightTopRadius;
        if (vlaueA > 0) {//只要有一个单独定义
            mType = 2;
        } else {
            if (mCornerRadius > 0) {//圆角
                mType = 1;
            }
        }
        array.recycle();
    }

    protected void initBorderPath() {
        if (mType == 2) {
            borderShapePath.reset();
            final float halfBorderWidth = mBorderWidth * 0.35f;
            final int width = getWidth();
            final int height = getHeight();
            float minWidth = Math.min(width, height) * 0.5f;
            leftTopRadius = Math.min(leftTopRadius, minWidth);
            rightTopRadius = Math.min(rightTopRadius, minWidth);
            rightBottomRadius = Math.min(rightBottomRadius, minWidth);
            leftBottomRadius = Math.min(leftBottomRadius, minWidth);

            if (isRadio) {
                if (leftTopRadius < 1) {
                    leftTopRadius = minWidth;
                }

                if (rightTopRadius < 1) {
                    rightTopRadius = minWidth;
                }
                if (rightBottomRadius < 1) {
                    rightBottomRadius = minWidth;
                }
                if (leftBottomRadius < 1) {
                    leftBottomRadius = minWidth;
                }
            }

            RectF rect = new RectF(halfBorderWidth, halfBorderWidth, width - halfBorderWidth, height - halfBorderWidth);
            borderShapePath.addRoundRect(rect,
                    new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
                            rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius},
                    Path.Direction.CW);
        }
    }

    public void setBorderWidth(int borderWidth) {
        if (mBorderWidth != borderWidth) {
            mBorderWidth = borderWidth;
            invalidate();
        }
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor;
            invalidate();
        }
    }


    public void setSelectedBorderColor(@ColorInt int selectedBorderColor) {
        if (mSelectedBorderColor != selectedBorderColor) {
            mSelectedBorderColor = selectedBorderColor;
            if (mIsSelected) {
                invalidate();
            }
        }

    }

    public void setSelectedBorderWidth(int selectedBorderWidth) {
        if (mSelectedBorderWidth != selectedBorderWidth) {
            mSelectedBorderWidth = selectedBorderWidth;
            if (mIsSelected) {
                invalidate();
            }
        }
    }

    public void setSelectedMaskColor(@ColorInt int selectedMaskColor) {
        if (mSelectedMaskColor != selectedMaskColor) {
            mSelectedMaskColor = selectedMaskColor;
            if (mSelectedMaskColor != Color.TRANSPARENT) {
                mSelectedColorFilter = new PorterDuffColorFilter(mSelectedMaskColor, PorterDuff.Mode.DARKEN);
            } else {
                mSelectedColorFilter = null;
            }
            if (mIsSelected) {
                invalidate();
            }
        }
        mSelectedMaskColor = selectedMaskColor;
    }


    public int getBorderColor() {
        return mBorderColor;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public int getSelectedBorderColor() {
        return mSelectedBorderColor;
    }

    public int getSelectedBorderWidth() {
        return mSelectedBorderWidth;
    }

    public int getSelectedMaskColor() {
        return mSelectedMaskColor;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean isSelected) {
        if (mIsSelected != isSelected) {
            mIsSelected = isSelected;
            invalidate();
        }
    }

    public void setTouchSelectModeEnabled(boolean touchSelectModeEnabled) {
        mIsTouchSelectModeEnabled = touchSelectModeEnabled;
    }

    public boolean isTouchSelectModeEnabled() {
        return mIsTouchSelectModeEnabled;
    }

    public void setSelectedColorFilter(ColorFilter cf) {
        if (mSelectedColorFilter == cf) {
            return;
        }
        mSelectedColorFilter = cf;
        if (mIsSelected) {
            invalidate();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mColorFilter == cf) {
            return;
        }
        mColorFilter = cf;
        if (!mIsSelected) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth(), viewHeight = getMeasuredHeight();
        int widthPadding = getPaddingLeft() + getPaddingRight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightPadding = getPaddingTop() + getPaddingBottom();
        int width = MeasureSpec.getSize(widthMeasureSpec) - widthPadding;
        int height = MeasureSpec.getSize(heightMeasureSpec) - heightPadding;

        if (mRateValue > 0) {
            if (widthMode == MeasureSpec.EXACTLY) {
                // 宽度确定时计算新高度
                height = (int) (width / mRateValue + 0.5f) + heightPadding;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            } else if (heightMode == MeasureSpec.EXACTLY) {
                // 高度确定时计算新宽度
                width = (int) (height * mRateValue + 0.5f) + widthPadding;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {

            if (mType == 0) {
                //圆角矩形
                int size = Math.min(viewWidth, viewHeight);
                setMeasuredDimension(size, size);
            } else {
                if (mBitmap == null) {
                    return;
                }
                if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED ||
                        heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
                    // 保证长宽比
                    float imageWidth = mBitmap.getWidth();
                    float imageHeight = mBitmap.getHeight();

                    float scaleX = viewWidth / imageWidth, scaleY = viewHeight / imageHeight;
                    if (scaleX == scaleY) {
                        return;
                    }
                    if (scaleX < scaleY) {
                        setMeasuredDimension(viewWidth, (int) (imageHeight * scaleX));
                    } else {
                        setMeasuredDimension((int) (imageWidth * scaleY), viewHeight);
                    }
                }
            }
        }
        initBorderPath();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setupBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setupBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        setupBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setupBitmap();
    }

    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null || getWidth() < 1) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMEN, COLOR_DRAWABLE_DIMEN, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getViewHeight());
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void setupBitmap() {
        Bitmap bm = getBitmap();
        if (bm == mBitmap) {
            return;
        }
        mBitmap = bm;
        if (mBitmap == null) {
            mBitmapShader = null;
            invalidate();
            return;
        }
        mNeedResetShader = true;
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint();
            mBitmapPaint.setAntiAlias(true);
        }
        mBitmapPaint.setShader(mBitmapShader);
        invalidate();
    }

    private void updateBitmapShader() {
        mMatrix.reset();
        mNeedResetShader = false;
        if (mBitmapShader == null || mBitmap == null) {
            return;
        }
        final float bmWidth = mBitmap.getWidth();
        final float bmHeight = mBitmap.getHeight();
        final float scaleX = mWidth / bmWidth;
        final float scaleY = mHeight / bmHeight;
        mMatrix.setScale(scaleX, scaleY);
        mMatrix.postTranslate(-(scaleX * bmWidth - mWidth) / 2, -(scaleY * bmHeight - mHeight) / 2);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth(), height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        Bitmap bm = getBitmap();
        if (bm != mBitmap) {
            setupBitmap();
        }

        if (mWidth != width || mHeight != height || mNeedResetShader) {
            mWidth = width;
            mHeight = height;
            updateBitmapShader();
        }
        if (mBitmap == null) {
            return;
        }

        mBorderPaint.setColor(mIsSelected ? mSelectedBorderColor : mBorderColor);
        mBitmapPaint.setColorFilter(mIsSelected ? mSelectedColorFilter : mColorFilter);
        int borderWidth = mIsSelected ? mSelectedBorderWidth : mBorderWidth;
        mBorderPaint.setStrokeWidth(borderWidth);
        final float halfBorderWidth = borderWidth * 1.0f / 2;

        switch (mType) {
            case 0:
                int radius = getWidth() / 2;
                canvas.drawCircle(radius, radius, radius, mBitmapPaint);
                if (borderWidth > 0) {
                    canvas.drawCircle(radius, radius, radius - halfBorderWidth, mBorderPaint);
                }
                break;
            case 1:
                mRectF.left = halfBorderWidth;
                mRectF.top = halfBorderWidth;
                mRectF.right = width - halfBorderWidth;
                mRectF.bottom = height - halfBorderWidth;

                canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mBitmapPaint);
                if (borderWidth > 0) {
                    canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mBorderPaint);
                }
                break;
            case 2:
                canvas.drawPath(borderShapePath, mBitmapPaint);
                if (borderWidth > 0) {
                    canvas.drawPath(borderShapePath, mBorderPaint);
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isClickable()) {
            this.setSelected(false);
            return super.onTouchEvent(event);
        }

        if (!mIsTouchSelectModeEnabled) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_SCROLL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                this.setSelected(false);
                break;
        }
        return super.onTouchEvent(event);
    }
}
