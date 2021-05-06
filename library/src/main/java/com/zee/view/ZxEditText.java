package com.zee.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.zee.libs.R;

/**
 * @author Administrator
 */
@SuppressWarnings(value = {"unchecked","deprecation"})
public class ZxEditText extends android.support.v7.widget.AppCompatEditText {
    BackgroundManager mBackgroundManager;
    private boolean first = true;
    Paint unLinePaint = new Paint();

    public ZxEditText(Context context) {
        this(context, null);
    }

    public ZxEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ZxEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxEditText);
        mBackgroundManager = new BackgroundManager(ta, this);

        int mWidth = ta.getDimensionPixelSize(R.styleable.ZxEditText_zv_drawableWidth, 0);
        int mHeight = ta.getDimensionPixelSize(R.styleable.ZxEditText_zv_drawableHeight, 0);


        Drawable[] drawables = getCompoundDrawables();
        setIconDrawable(drawables[0], mWidth, mHeight, 0);
        setIconDrawable(drawables[1], mWidth, mHeight, 1);
        setIconDrawable(drawables[2], mWidth, mHeight, 2);
        setIconDrawable(drawables[3], mWidth, mHeight, 3);
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (first) {
            mBackgroundManager.setBgSelector();
            first=false;
        }
    }

    public void setIconDrawable(Drawable mDrawable, int mWidth, int mHeight, int mLocation) {
        if (mDrawable != null) {
            if (mWidth != 0 && mHeight != 0) {
                mDrawable.setBounds(0, 0, mWidth, mHeight);
            }
            switch (mLocation) {
                case 1:
                    this.setCompoundDrawables(null, mDrawable, null, null);
                    break;
                case 2:
                    this.setCompoundDrawables(null, null, mDrawable, null);
                    break;
                case 3:
                    this.setCompoundDrawables(null, null, null, mDrawable);
                    break;
                default:
                    this.setCompoundDrawables(mDrawable, null, null, null);
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBackgroundManager.unline_height > 0) {
            float startX = mBackgroundManager.unline_marginLeft;
            float startY = getHeight() - mBackgroundManager.unline_height / 2;
            float startEndX = getWidth() - mBackgroundManager.unline_marginRight;
            canvas.drawLine(startX, startY, startEndX, startY, unLinePaint);
        }
    }

    class BackgroundManager {
        private GradientDrawable gd_background = new GradientDrawable();
        private GradientDrawable gd_background_press = new GradientDrawable();
        private View view;
        //        private int backgroundPressColor;
        private int backgroundColor;
        private float radius;
        private int borderWidth;
        private int borderColor;
        private float leftTopRadius;
        private float rightTopRadius;
        private float rightBottomRadius;
        private float leftBottomRadius;
        //        private int textPressColor;
//        private int borderPressColor;
        private boolean isRippleEnable = false;
        private boolean isRadiusHalfHeight = false;
        /**
         * 下划线的高度
         */
        private float unline_height = 0;
        /**
         * 下划线颜色
         */
        private int unline_color = 0;

        /**
         * 下划线距离左边的距离
         */
        private float unline_marginLeft = 0;
        /**
         * 下划线距离右边的距离
         */
        private float unline_marginRight = 0;


        BackgroundManager(TypedArray ta, View view) {
            this.view = view;
            borderWidth = ta.getDimensionPixelSize(R.styleable.ZxEditText_zv_borderWidth, 0);
            borderColor = ta.getColor(R.styleable.ZxEditText_zv_borderColor, Color.TRANSPARENT);

            leftTopRadius = ta.getDimension(R.styleable.ZxEditText_zv_leftTopRadius, 0);
            rightTopRadius = ta.getDimension(R.styleable.ZxEditText_zv_rightTopRadius, 0);
            leftBottomRadius = ta.getDimension(R.styleable.ZxEditText_zv_leftBottomRadius, 0);
            rightBottomRadius = ta.getDimension(R.styleable.ZxEditText_zv_rightBottomRadius, 0);

//            textPressColor = ta.getColor(R.styleable.ZxEditText_zv_textPressColor, Integer.MAX_VALUE);
//            borderPressColor = ta.getColor(R.styleable.ZxEditText_zv_borderPressColor, Integer.MAX_VALUE);
//            backgroundPressColor = ta.getColor(R.styleable.ZxEditText_zv_backgroundPressColor, Integer.MAX_VALUE);

            isRadiusHalfHeight = ta.getBoolean(R.styleable.ZxEditText_zv_radiusHalfHeight, false);
            Drawable background = view.getBackground();
            if (background instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) background;
                backgroundColor = colorDrawable.getColor();
            }
            radius = ta.getDimension(R.styleable.ZxEditText_zv_radius, 0);
            if (borderColor != Color.TRANSPARENT && borderWidth < 1) {
                borderWidth = 1;
            }
//            if (backgroundPressColor != Integer.MAX_VALUE) {
//                isRippleEnable = true;
//                view.setClickable(true);
//            }

            if (radius > 0) {
                if (leftTopRadius == 0) {
                    leftTopRadius = radius;
                }
                if (leftBottomRadius == 0) {
                    leftBottomRadius = radius;
                }

                if (rightTopRadius == 0) {
                    rightTopRadius = radius;
                }

                if (rightBottomRadius == 0) {
                    rightBottomRadius = radius;
                }
            }

            //下划线
            unline_height = ta.getDimension(R.styleable.ZxEditText_zv_underlinedHeight, 0);
            if (unline_height != 0) {
                unline_color = ta.getColor(R.styleable.ZxEditText_zv_underlinedColor, 0);
                unline_marginLeft = ta.getDimension(R.styleable.ZxEditText_zv_underlined_marginLeft, 0);
                unline_marginRight = ta.getDimension(R.styleable.ZxEditText_zv_underlined_marginRight, 0);
                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) (getPaddingBottom() + unline_height));
                unLinePaint.setColor(unline_color);
                unLinePaint.setStrokeWidth(unline_height);
            }
        }

        @SuppressWarnings("unchecked")
        public void setBgSelector() {
            if (isRadiusHalfHeight) {
                leftTopRadius = leftBottomRadius = rightTopRadius = rightBottomRadius = view.getHeight() / 2;
            }

            StateListDrawable bg = new StateListDrawable();

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRippleEnable) {
//                setDrawable(gd_background, backgroundColor, borderColor);
//                RippleDrawable rippleDrawable = new RippleDrawable(getPressedColorSelector(backgroundColor, backgroundPressColor), gd_background, null);
//                view.setBackground(rippleDrawable);
//            } else {
            setDrawable(gd_background, backgroundColor, borderColor);
            bg.addState(new int[]{-android.R.attr.state_pressed}, gd_background);
//                if (backgroundPressColor != Integer.MAX_VALUE || borderPressColor != Integer.MAX_VALUE) {
//                    setDrawable(gd_background_press, backgroundPressColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressColor,
//                            borderPressColor == Integer.MAX_VALUE ? borderColor : borderPressColor);
//                    bg.addState(new int[]{android.R.attr.state_pressed}, gd_background_press);
//                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                view.setBackground(bg);
            } else {
                view.setBackgroundDrawable(bg);
            }
//            }

//            if (view instanceof TextView) {
//                if (textPressColor != Integer.MAX_VALUE) {
//                    ColorStateList textColors = ((TextView) view).getTextColors();
//                    ColorStateList colorStateList = new ColorStateList(
//                            new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}},
//                            new int[]{textColors.getDefaultColor(), textPressColor});
//                    ((TextView) view).setTextColor(colorStateList);
//                }
//            }
        }

        private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
            gd.setColor(color);

            if (leftTopRadius > 0 || rightTopRadius > 0 || rightBottomRadius > 0 || leftBottomRadius > 0) {
                float radiusArr[] = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
                        rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
                gd.setCornerRadii(radiusArr);
            } else {
                gd.setCornerRadius(radius);
            }

            gd.setStroke(borderWidth, strokeColor);
        }


        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
            return new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_activated},
                            new int[]{}
                    },
                    new int[]{
                            pressedColor,
                            pressedColor,
                            pressedColor,
                            normalColor
                    }
            );
        }
    }

}
