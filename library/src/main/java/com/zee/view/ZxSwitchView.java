package com.zee.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;

import com.zee.libs.R;


/**
 * @author Administrator
 */
public class ZxSwitchView extends CompoundButton {
    /**
     * 已经打开
     */
    private static final int STATE_SWITCH_ON = 4;

    /**
     * 准备关闭
     */
    private static final int STATE_SWITCH_ON2 = 3;
    /**
     * 准备打开
     */
    private static final int STATE_SWITCH_OFF2 = 2;

    /**
     * 已经关闭
     */
    private static final int STATE_SWITCH_OFF = 1;

    private final AccelerateInterpolator interpolator = new AccelerateInterpolator(2);
    private final Paint paint = new Paint();
    //背景填充的颜色
//    private final Path backGroundPath = new Path();
    private RectF backGroundRectF;
    private RectF backGroundStrockRectF;

    //球的阴影
    private final Path ballShadowPath = new Path();

    private final RectF bRectF = new RectF();
    private float sAnim, bAnim;

    private RadialGradient shadowGradient;

    // (0,1]
    protected float ratioAspect = 0.50f;
    // (0,1]
    protected float animationSpeed = 0.1f;

    private int state;
    private int lastState;
    private boolean isCanVisibleDrawing = false;
    private OnClickListener mOnClickListener;
    protected int mOnColor, mOffColor;
    protected int mOnBorderColor, mOffBorderColor, mBallBorderColor;
    protected int mBallColor;
    protected int shadowColor;

    protected boolean isShowShadow;
    protected boolean isOpened;

    private float sRight;

    private float bOffset;
    private float bRadius, bStrokeWidth;
    private float bWidth;
    private float bLeft, bRight;
    private float bOnLeftX, bOn2LeftX, bOff2LeftX, bOffLeftX;
    //设置球和边的间隔
    private float gap = 0;

    private float shadowReservedHeight;//阴影高度
    private OnChangedListener mOnChangedListener;

    //描边颜色
    private int mStrokeWidth = 1;

    public ZxSwitchView(Context context) {
        this(context, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ZxSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        final int DEFAULT_COLOR_PRIMARY = 0xFF4BD763;
        final int DEFAULT_COLOR_PRIMARY_DARK = 0xFFcecfce;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZxSwitchView);
        mOnColor = a.getColor(R.styleable.ZxSwitchView_zv_onColor, DEFAULT_COLOR_PRIMARY);
        mOnBorderColor = a.getColor(R.styleable.ZxSwitchView_zv_onBorderColor, mOnColor);
        mOffColor = a.getColor(R.styleable.ZxSwitchView_zv_offColor, DEFAULT_COLOR_PRIMARY_DARK);
        mOffBorderColor = a.getColor(R.styleable.ZxSwitchView_zv_offBorderColor, mOffColor);
        mBallColor = a.getColor(R.styleable.ZxSwitchView_zv_ballColor, 0xFFFFFFFF);
        mBallBorderColor = a.getColor(R.styleable.ZxSwitchView_zv_ballBorderColor, mBallColor);

        shadowColor = a.getColor(R.styleable.ZxSwitchView_zv_shadowColor, 0xFF333333);
        ratioAspect = a.getFloat(R.styleable.ZxSwitchView_zv_ratioAspect, 0.55f);
        gap = a.getDimension(R.styleable.ZxSwitchView_zv_gap, 0f);
        isShowShadow = a.getBoolean(R.styleable.ZxSwitchView_zv_showShadow, true);
        isOpened = a.getBoolean(R.styleable.ZxSwitchView_zv_opened, false);
        state = isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        lastState = state;
        a.recycle();

        if (mOnColor == DEFAULT_COLOR_PRIMARY && mOnBorderColor == DEFAULT_COLOR_PRIMARY_DARK) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    TypedValue primaryColorTypedValue = new TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.colorPrimary, primaryColorTypedValue, true);
                    if (primaryColorTypedValue.data > 0) {
                        mOnColor = primaryColorTypedValue.data;
                    }
                    TypedValue primaryColorDarkTypedValue = new TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.colorPrimaryDark, primaryColorDarkTypedValue, true);
                    if (primaryColorDarkTypedValue.data > 0) {
                        mOnBorderColor = primaryColorDarkTypedValue.data;
                    }
                }
            } catch (Exception ignore) {
            }
        }
    }

    public void setColor(int newColorPrimary, int newColorPrimaryDark) {
        setColor(newColorPrimary, newColorPrimaryDark, mOffBorderColor, mBallBorderColor);
    }

    public void setColor(int newColorPrimary, int newColorPrimaryDark, int newColorOff, int newColorOffDark) {
        setColor(newColorPrimary, newColorPrimaryDark, newColorOff, newColorOffDark, shadowColor);
    }

    public void setColor(int onColor, int onBorderColor, int offBorderColor, int ballBorderColor, int shadowColcor) {
        this.mOnColor = onColor;
        this.mOnBorderColor = onBorderColor;
        this.mOffBorderColor = offBorderColor;
        this.mBallBorderColor = ballBorderColor;
        this.shadowColor = shadowColcor;
        this.invalidate();
    }

    public void setShadow(boolean shadow) {
        isShowShadow = shadow;
        invalidate();
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean isOpened) {
        int wishState = isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        if (wishState == state) {
            return;
        }
        refreshState(wishState);
    }

    public void toggleSwitch(boolean isOpened) {
        int wishState = isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        if (wishState == state) {
            return;
        }
        if ((wishState == STATE_SWITCH_ON && (state == STATE_SWITCH_OFF || state == STATE_SWITCH_OFF2))
                || (wishState == STATE_SWITCH_OFF && (state == STATE_SWITCH_ON || state == STATE_SWITCH_ON2))) {
            sAnim = 1;
        }
        bAnim = 1;
        if (mOnChangedListener != null) {
            mOnChangedListener.onChange(isOpened);
        }
        refreshState(wishState);
    }

    private void refreshState(int newState) {
        if (!isOpened && newState == STATE_SWITCH_ON) {
            isOpened = true;
        } else if (isOpened && newState == STATE_SWITCH_OFF) {
            isOpened = false;
        }
        lastState = state;
        state = newState;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int resultWidth;

        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSize;
        } else {
            resultWidth = (int) (65 * getResources().getDisplayMetrics().density + 0.5f)
                    + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                resultWidth = Math.min(resultWidth, widthSize);
            }
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int resultHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeight = heightSize;
        } else {
            resultHeight = (int) (resultWidth * ratioAspect) + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                resultHeight = Math.min(resultHeight, heightSize);
            }
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        isCanVisibleDrawing = w > getPaddingLeft() + getPaddingRight() && h > getPaddingTop() + getPaddingBottom();

        if (isCanVisibleDrawing) {
            //获得按钮的宽度
            int actuallyDrawingAreaWidth = w - getPaddingLeft() - getPaddingRight();
            //获得按钮的高度
            int actuallyDrawingAreaHeight = h - getPaddingTop() - getPaddingBottom();

            int actuallyDrawingAreaLeft;
            int actuallyDrawingAreaRight;
            int actuallyDrawingAreaTop;
            int actuallyDrawingAreaBottom;
            if (actuallyDrawingAreaWidth * ratioAspect < actuallyDrawingAreaHeight) {
                actuallyDrawingAreaLeft = getPaddingLeft();
                actuallyDrawingAreaRight = w - getPaddingRight();
                int heightExtraSize = (int) (actuallyDrawingAreaHeight - actuallyDrawingAreaWidth * ratioAspect);
                actuallyDrawingAreaTop = getPaddingTop() + heightExtraSize / 2;
                actuallyDrawingAreaBottom = getHeight() - getPaddingBottom() - heightExtraSize / 2;
            } else {
                int widthExtraSize = (int) (actuallyDrawingAreaWidth - actuallyDrawingAreaHeight / ratioAspect);
                actuallyDrawingAreaLeft = getPaddingLeft() + widthExtraSize / 2;
                actuallyDrawingAreaRight = getWidth() - getPaddingRight() - widthExtraSize / 2;
                actuallyDrawingAreaTop = getPaddingTop();
                actuallyDrawingAreaBottom = getHeight() - getPaddingBottom();
            }

            shadowReservedHeight = (int) ((actuallyDrawingAreaBottom - actuallyDrawingAreaTop) * 0.07f);
            bLeft = actuallyDrawingAreaLeft;
            float sTop = actuallyDrawingAreaTop + shadowReservedHeight;
            sRight = actuallyDrawingAreaRight;
            float sBottom = actuallyDrawingAreaBottom - shadowReservedHeight;

            float sHeight = sBottom - sTop;


            bWidth = sBottom - sTop;
            bRight = bLeft + bWidth;
            final float halfHeightOfS = bWidth / 2; // OfB
            bRadius = halfHeightOfS * 0.95f;
            bOffset = bRadius * 0.2f; // offset of switching
            bStrokeWidth = (halfHeightOfS - bRadius) * 2;
            bOnLeftX = sRight - bWidth;
            bOn2LeftX = bOnLeftX - bOffset;
            bOffLeftX = bLeft;
            bOff2LeftX = bOffLeftX + bOffset;

            backGroundRectF = new RectF(bLeft, sTop, sRight, sBottom);
            backGroundStrockRectF = new RectF(bLeft + mStrokeWidth, sTop + mStrokeWidth, sRight - 2 * mStrokeWidth, sBottom - 2 * mStrokeWidth);
            bRectF.left = bLeft;
            bRectF.right = bRight;
            bRectF.top = sTop + bStrokeWidth / 2+gap;  // bTop = sTop
            bRectF.bottom = sBottom - bStrokeWidth / 2-gap; // bBottom = sBottom
            float bCenterX = (bRight + bLeft) / 2;
            float bCenterY = (sBottom + sTop) / 2;

            int red = shadowColor >> 16 & 0xFF;
            int green = shadowColor >> 8 & 0xFF;
            int blue = shadowColor & 0xFF;
            shadowGradient = new RadialGradient(bCenterX, bCenterY, bRadius, Color.argb(200, red, green, blue),
                    Color.argb(25, red, green, blue), Shader.TileMode.CLAMP);
        }
    }

    private void calcBPath(float percent) {
        ballShadowPath.reset();
        bRectF.left = bLeft + bStrokeWidth / 2+gap;
        bRectF.right = bRight - bStrokeWidth / 2-gap;
        ballShadowPath.arcTo(bRectF, 90, 180);
        bRectF.left = bLeft + percent * bOffset + bStrokeWidth / 2+gap;
        bRectF.right = bRight + percent * bOffset - bStrokeWidth / 2-gap;
        ballShadowPath.arcTo(bRectF, 270, 180);
        ballShadowPath.close();

    }

    private float calcBTranslate(float percent) {
        float result = 0;
        switch (state - lastState) {
            case 1:
                if (state == STATE_SWITCH_OFF2) {
                    // off -> off2
                    result = bOffLeftX;
                } else if (state == STATE_SWITCH_ON) {
                    // on2 -> on
                    result = bOnLeftX - (bOnLeftX - bOn2LeftX) * percent;
                }
                break;
            case 2:
                if (state == STATE_SWITCH_ON) {
                    // off2 -> on
                    result = bOnLeftX - (bOnLeftX - bOffLeftX) * percent;
                } else if (state == STATE_SWITCH_ON2) {
                    // off -> on2
                    result = bOn2LeftX - (bOn2LeftX - bOffLeftX) * percent;
                }
                break;
            case 3:
                // off -> on
                result = bOnLeftX - (bOnLeftX - bOffLeftX) * percent;
                break;
            case -1:
                if (state == STATE_SWITCH_ON2) {
                    // on -> on2
                    result = bOn2LeftX + (bOnLeftX - bOn2LeftX) * percent;
                } else if (state == STATE_SWITCH_OFF) {
                    // off2 -> off
                    result = bOffLeftX;
                }
                break;
            case -2:
                if (state == STATE_SWITCH_OFF) {
                    // on2 -> off
                    result = bOffLeftX + (bOn2LeftX - bOffLeftX) * percent;
                } else if (state == STATE_SWITCH_OFF2) {
                    // on -> off2
                    result = bOff2LeftX + (bOnLeftX - bOff2LeftX) * percent;
                }
                break;
            case -3:
                // on -> off
                result = bOffLeftX + (bOnLeftX - bOffLeftX) * percent;
                break;
            default: // init
            case 0:
                if (state == STATE_SWITCH_OFF) {
                    //  off -> off
                    result = bOffLeftX;
                } else if (state == STATE_SWITCH_ON) {
                    // on -> on
                    result = bOnLeftX;
                }
                break;
        }
        return result - bOffLeftX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isCanVisibleDrawing) {
            return;
        }

        paint.setAntiAlias(true);
        final boolean isOn = (state == STATE_SWITCH_ON || state == STATE_SWITCH_ON2);
        // Draw background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isOn ? mOnColor : mOffColor);
        canvas.drawRoundRect(backGroundRectF, backGroundRectF.height() / 2, backGroundRectF.height() / 2, paint);
        //描边
        paint.setColor(isOn ? mOnBorderColor : mOffBorderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        canvas.drawRoundRect(backGroundStrockRectF, backGroundRectF.height() / 2, backGroundRectF.height() / 2, paint);

        sAnim = sAnim - animationSpeed > 0 ? sAnim - animationSpeed : 0;
        bAnim = bAnim - animationSpeed > 0 ? bAnim - animationSpeed : 0;

        final float dbAnim = interpolator.getInterpolation(bAnim);

        canvas.restore();
        // To prepare center bar path
        canvas.save();
        canvas.translate(calcBTranslate(dbAnim), shadowReservedHeight);
        final boolean isState2 = (state == STATE_SWITCH_ON2 || state == STATE_SWITCH_OFF2);
        calcBPath(isState2 ? 1 - dbAnim : dbAnim);
        // Use center bar path to draw shadow
        if (isShowShadow) {
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(shadowGradient);
            canvas.drawPath(ballShadowPath, paint);
            paint.setShader(null);
        }
        canvas.translate(0, -shadowReservedHeight);
        // draw bar
        canvas.scale(0.98f, 0.98f, bWidth / 2, bWidth / 2);
        paint.setStyle(Paint.Style.FILL);

        //绘制球的颜色
        paint.setColor(mBallColor);
        canvas.drawPath(ballShadowPath, paint);
        //球体描边
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(bStrokeWidth * 0.5f);
        paint.setColor(mBallBorderColor);
        canvas.drawPath(ballShadowPath, paint);
        canvas.restore();

        paint.reset();
        if (sAnim > 0 || bAnim > 0) {
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((state == STATE_SWITCH_ON || state == STATE_SWITCH_OFF) && (sAnim * bAnim == 0)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;
                case MotionEvent.ACTION_UP:
                    lastState = state;

                    bAnim = 1;
                    if (state == STATE_SWITCH_OFF) {
                        refreshState(STATE_SWITCH_OFF2);
                        toggleSwitch(true);
                    } else if (state == STATE_SWITCH_ON) {
                        refreshState(STATE_SWITCH_ON2);
                        toggleSwitch(false);
                    }

                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(this);
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mOnClickListener = l;
    }

    public interface OnChangedListener {
        void onChange(boolean isOpen);
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.mOnChangedListener = onChangedListener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isOpened = isOpened;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.isOpened = ss.isOpened;
        this.state = this.isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        invalidate();
    }

    private static final class SavedState extends BaseSavedState {
        private boolean isOpened;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            isOpened = 1 == in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isOpened ? 1 : 0);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
