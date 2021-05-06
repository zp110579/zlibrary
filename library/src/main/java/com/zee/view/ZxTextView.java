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
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zee.libs.R;

/**
 * @author Administrator
 */
@SuppressWarnings(value = {"unchecked", "deprecation"})
public class ZxTextView extends AppCompatTextView {
    BackgroundManager mBackgroundManager;
    private boolean first = true;
    Paint unLinePaint = new Paint();

    public ZxTextView(Context context) {
        this(context, null);
    }

    public ZxTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxTextView);
        mBackgroundManager = new BackgroundManager(ta, this);
        int mWidth = ta.getDimensionPixelSize(R.styleable.ZxTextView_zv_drawableWidth, 0);
        int mHeight = ta.getDimensionPixelSize(R.styleable.ZxTextView_zv_drawableHeight, 0);
        if (mWidth > 0) {
            Drawable[] drawables = getCompoundDrawables();
            setIconDrawable(drawables[0], mWidth, mHeight, 0);
            setIconDrawable(drawables[1], mWidth, mHeight, 1);
            setIconDrawable(drawables[2], mWidth, mHeight, 2);
            setIconDrawable(drawables[3], mWidth, mHeight, 3);
        }
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (first && mBackgroundManager.isRadiusHalfHeight) {
            if (mBackgroundManager.backgroundDrawable == null) {
                mBackgroundManager.setBgColorSelector();
            } else {
                mBackgroundManager.setBgDrawableSelector();
            }
            first = false;
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
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mBackgroundManager.setSelected(selected);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mBackgroundManager.setEnableEx(enabled);
    }

    public BackgroundManager getBackgroundManager() {
        return mBackgroundManager;
    }

    public void setBorderWidth(int value) {
        mBackgroundManager.setBorderWidth(value);
        mBackgroundManager.setBgColorSelector();
    }

    class BackgroundManager {
        private GradientDrawable normalBackground;
        private GradientDrawable gd_background_press;
        private GradientDrawable selectBackground;
        private GradientDrawable disAbleBackground;

        private TextView mTextView;
        private float radius;
        private int borderWidth;
        private int borderColor;
        private int borderPressColor;
        private int borderSelectColor;

        private boolean isRadiusHalfHeight = false;


        private float leftTopRadius;
        private float rightTopRadius;
        private float rightBottomRadius;
        private float leftBottomRadius;

        private int textSelectColor;
        private int textDisableColor;

        //背景颜色设置
        private int backgroundColor;
        private int backgroundPressColor;
        private int backgroundSelectColor;
        private int backgroundDisableColor;

        //背景图片设置
        private Drawable backgroundDrawable;
        private Drawable backgroundPressDrawable;
        private Drawable backgroundSelectDrawable;
        private Drawable backgroundDisableDrawable;


        private int textPressColor;
        private ColorStateList normalTextColor;

        private boolean isRippleEnable = false;
        /**
         * 选中后的显示的字
         */
        private String textSelect;

        /**
         * 不能点击后的显示的文字
         */
        private String textDisable;
        /**
         * 记录原来的字
         */
        private String oldSelect;

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

        BackgroundManager(TypedArray ta, TextView view) {
            this.mTextView = view;
            borderWidth = ta.getDimensionPixelSize(R.styleable.ZxTextView_zv_borderWidth, 0);


            leftTopRadius = ta.getDimension(R.styleable.ZxTextView_zv_leftTopRadius, 0);
            rightTopRadius = ta.getDimension(R.styleable.ZxTextView_zv_rightTopRadius, 0);
            leftBottomRadius = ta.getDimension(R.styleable.ZxTextView_zv_leftBottomRadius, 0);
            rightBottomRadius = ta.getDimension(R.styleable.ZxTextView_zv_rightBottomRadius, 0);
            textSelect = ta.getString(R.styleable.ZxTextView_zv_textSelect);
            textDisable = ta.getString(R.styleable.ZxTextView_zv_textDisable);
            isRadiusHalfHeight = ta.getBoolean(R.styleable.ZxTextView_zv_radiusHalfHeight, false);

            borderColor = ta.getColor(R.styleable.ZxTextView_zv_borderColor, Color.TRANSPARENT);
            borderSelectColor = ta.getColor(R.styleable.ZxTextView_zv_borderSelectColor, Integer.MAX_VALUE);
            borderPressColor = ta.getColor(R.styleable.ZxTextView_zv_borderPressColor, Integer.MAX_VALUE);

            textSelectColor = ta.getColor(R.styleable.ZxTextView_zv_textSelectColor, Integer.MAX_VALUE);
            textPressColor = ta.getColor(R.styleable.ZxTextView_zv_textPressColor, Integer.MAX_VALUE);
            textDisableColor = ta.getColor(R.styleable.ZxTextView_zv_textDisableColor, Integer.MAX_VALUE);

            backgroundPressColor = ta.getColor(R.styleable.ZxTextView_zv_backgroundPressColor, Integer.MAX_VALUE);
            backgroundSelectColor = ta.getColor(R.styleable.ZxTextView_zv_backgroundSelectColor, Integer.MAX_VALUE);
            backgroundDisableColor = ta.getColor(R.styleable.ZxTextView_zv_backgroundDisableColor, Integer.MAX_VALUE);

            backgroundSelectDrawable = ta.getDrawable(R.styleable.ZxTextView_zv_backgroundSelectDrawable);
            backgroundPressDrawable = ta.getDrawable(R.styleable.ZxTextView_zv_backgroundPressDrawable);
            backgroundDisableDrawable = ta.getDrawable(R.styleable.ZxTextView_zv_backgroundDisableDrawable);

            //下划线
            unline_height = ta.getDimension(R.styleable.ZxTextView_zv_underlinedHeight, 0);
            if (unline_height != 0) {
                unline_color = ta.getColor(R.styleable.ZxTextView_zv_underlinedColor, 0);
                unline_marginLeft = ta.getDimension(R.styleable.ZxTextView_zv_underlined_marginLeft, 0);
                unline_marginRight = ta.getDimension(R.styleable.ZxTextView_zv_underlined_marginRight, 0);
                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) (getPaddingBottom() + unline_height));
                unLinePaint.setColor(unline_color);
                unLinePaint.setStrokeWidth(unline_height);
            }

            oldSelect = getText().toString();

            Drawable background = view.getBackground();
            if (background instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) background;
                backgroundColor = colorDrawable.getColor();
            } else if (background instanceof Drawable) {
                backgroundDrawable = background;
            }

            radius = ta.getDimension(R.styleable.ZxTextView_zv_radius, 0);
            if (borderColor != Color.TRANSPARENT && borderWidth < 1) {
                borderWidth = 1;
            }
            if (backgroundPressColor != Integer.MAX_VALUE) {
                isRippleEnable = true;
                view.setClickable(true);
            }

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
            normalTextColor = getTextColors();

            //设置字体颜色
            ColorStateList textColors = view.getTextColors();
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{new int[]{-android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_selected},
                            new int[]{-android.R.attr.state_enabled}},
                    new int[]{textColors.getDefaultColor(),
                            textPressColor != Integer.MAX_VALUE ? textPressColor : textColors.getDefaultColor(),
                            textSelectColor != Integer.MAX_VALUE ? textSelectColor : textColors.getDefaultColor(),
                            textDisableColor != Integer.MAX_VALUE ? textDisableColor : textColors.getDefaultColor()
                    });
            view.setTextColor(colorStateList);
            if (!isRadiusHalfHeight) {
                if (backgroundDrawable != null) {//图片类型
                    setBgDrawableSelector();
                } else {
                    setBgColorSelector();
                }
            }
        }


        @SuppressWarnings("unchecked")
        public void setSelected(boolean selected) {
            if (selected) {//选中
                if (selectBackground != null) {
                    mTextView.setBackgroundDrawable(selectBackground);
                } else if (backgroundSelectDrawable != null) {
                    mTextView.setBackgroundDrawable(backgroundSelectDrawable);
                }
                if (textSelectColor != Integer.MAX_VALUE) {
                    mTextView.setTextColor(textSelectColor);
                }
                if (!TextUtils.isEmpty(textSelect)) {
                    mTextView.setText(textSelect);
                }
            } else {
                if (normalBackground != null) {
                    mTextView.setBackgroundDrawable(normalBackground);
                } else if (backgroundDrawable != null) {
                    mTextView.setBackgroundDrawable(backgroundDrawable);
                }
                if (textSelectColor != Integer.MAX_VALUE) {
                    mTextView.setTextColor(normalTextColor);
                }
                if (!TextUtils.isEmpty(textSelect)) {
                    mTextView.setText(oldSelect);
                }
            }
        }


        @SuppressWarnings("unchecked")
        public void setEnableEx(boolean enable) {
            if (!enable) {
                if (disAbleBackground != null) {
                    mTextView.setBackgroundDrawable(disAbleBackground);
                } else if (backgroundDisableDrawable != null) {
                    mTextView.setBackgroundDrawable(backgroundDisableDrawable);
                }
                if (textDisableColor != Integer.MAX_VALUE) {
                    mTextView.setTextColor(textDisableColor);
                }
                if (!TextUtils.isEmpty(textDisable)) {
                    mTextView.setText(textDisable);
                }
            } else {
                if (normalBackground != null) {
                    mTextView.setBackgroundDrawable(normalBackground);
                } else if (backgroundDrawable != null) {
                    mTextView.setBackgroundDrawable(backgroundDrawable);
                }
                if (normalTextColor != null) {
                    mTextView.setTextColor(normalTextColor);
                }
                if (!TextUtils.isEmpty(textDisable)) {
                    mTextView.setText(oldSelect);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public void setBgColorSelector() {
            if (isRadiusHalfHeight) {
                leftTopRadius = leftBottomRadius = rightTopRadius = rightBottomRadius = mTextView.getHeight() / 2;
            }
            StateListDrawable bg = new StateListDrawable();
            normalBackground = new GradientDrawable();
            setDrawable(normalBackground, backgroundColor, borderColor);
            bg.addState(new int[]{-android.R.attr.state_pressed}, normalBackground);
            if (backgroundPressColor != Integer.MAX_VALUE || borderPressColor != Integer.MAX_VALUE) {
                gd_background_press = new GradientDrawable();
                setDrawable(gd_background_press, backgroundPressColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressColor,
                        borderPressColor == Integer.MAX_VALUE ? borderColor : borderPressColor);
                bg.addState(new int[]{android.R.attr.state_pressed}, gd_background_press);
            }


            //设置选择后的颜色
            if (backgroundSelectColor != Integer.MAX_VALUE || borderSelectColor != Integer.MAX_VALUE) {
                selectBackground = new GradientDrawable();
                setDrawable(selectBackground, backgroundSelectColor == Integer.MAX_VALUE ? backgroundColor : backgroundSelectColor,
                        borderSelectColor == Integer.MAX_VALUE ? borderColor : borderSelectColor);
                bg.addState(new int[]{android.R.attr.state_selected}, selectBackground);
            }

            //Disable状态背景颜色
            if (backgroundDisableColor != Integer.MAX_VALUE || borderPressColor != Integer.MAX_VALUE) {
                disAbleBackground = new GradientDrawable();
                setDrawable(disAbleBackground, backgroundDisableColor == Integer.MAX_VALUE ? backgroundColor : backgroundDisableColor,
                        borderColor == Integer.MAX_VALUE ? borderColor : borderColor);
                bg.addState(new int[]{android.R.attr.state_enabled}, disAbleBackground);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                mTextView.setBackground(bg);
            } else {
                mTextView.setBackgroundDrawable(bg);
            }

            if (mTextView.isSelected()) {
                setSelected(true);
            }

        }

        public void setBgDrawableSelector() {

            StateListDrawable bg = new StateListDrawable();
            bg.addState(new int[]{-android.R.attr.state_pressed}, backgroundDrawable);
            if (backgroundPressDrawable != null) {
                bg.addState(new int[]{android.R.attr.state_pressed}, backgroundPressDrawable);
            }
            if (backgroundSelectDrawable != null) {
                bg.addState(new int[]{android.R.attr.state_selected}, backgroundSelectDrawable);
            }
            if (backgroundDisableDrawable != null) {//禁用的图片设置
                bg.addState(new int[]{-android.R.attr.state_enabled}, backgroundDisableDrawable);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                mTextView.setBackground(bg);
            } else {
                mTextView.setBackgroundDrawable(bg);
            }

            if (mTextView.isSelected()) {
                setSelected(true);
            }
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
            gd.setBounds(-borderWidth, -borderWidth, -borderWidth, borderWidth);
        }


        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private ColorStateList getPressedColorSelector(int normalColor, int pressedColor, int selectColor) {
            return new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_activated},
                            new int[]{android.R.attr.state_selected},
                            new int[]{}
                    },
                    new int[]{
                            pressedColor,
                            pressedColor,
                            pressedColor,
                            selectColor,
                            normalColor
                    }
            );
        }

        public void setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
        }
    }

}
