package com.zee.manager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zee.libs.R;
import com.zee.view.ZxConstraintLayout;
import com.zee.view.ZxLinearLayout;
import com.zee.view.ZxRelativeLayout;


/**
 * @author Administrator
 */
@SuppressWarnings(value = {"unchecked", "deprecation"})
public class LayoutBackgroundManager {
    private GradientDrawable normalBackground = new GradientDrawable();
    private GradientDrawable pressBackground = new GradientDrawable();
    private GradientDrawable selectBackground;
    private View view;
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


    private int backgroundColor;
    private int backgroundPressColor;
    private int backgroundSelectColor;

    private boolean childSelect = false;

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
    Paint unLinePaint = new Paint();

    
    public LayoutBackgroundManager(Context context, AttributeSet attrs, View view) {
        this.view = view;
        if (view instanceof ZxLinearLayout) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxLinearLayout);
            radius = ta.getDimension(R.styleable.ZxLinearLayout_zv_radius, 0);
            borderWidth = ta.getDimensionPixelSize(R.styleable.ZxLinearLayout_zv_borderWidth, 0);
            borderColor = ta.getColor(R.styleable.ZxLinearLayout_zv_borderColor, Color.TRANSPARENT);
            isRadiusHalfHeight = ta.getBoolean(R.styleable.ZxLinearLayout_zv_radiusHalfHeight, false);

            leftTopRadius = ta.getDimension(R.styleable.ZxLinearLayout_zv_leftTopRadius, 0);
            rightTopRadius = ta.getDimension(R.styleable.ZxLinearLayout_zv_rightTopRadius, 0);
            leftBottomRadius = ta.getDimension(R.styleable.ZxLinearLayout_zv_leftBottomRadius, 0);
            rightBottomRadius = ta.getDimension(R.styleable.ZxLinearLayout_zv_rightBottomRadius, 0);

            borderPressColor = ta.getColor(R.styleable.ZxLinearLayout_zv_borderPressColor, Integer.MAX_VALUE);
            backgroundPressColor = ta.getColor(R.styleable.ZxLinearLayout_zv_backgroundPressColor, Integer.MAX_VALUE);

            borderSelectColor = ta.getColor(R.styleable.ZxLinearLayout_zv_borderSelectColor, Integer.MAX_VALUE);
            backgroundSelectColor = ta.getColor(R.styleable.ZxLinearLayout_zv_backgroundSelectColor, Integer.MAX_VALUE);
            childSelect = ta.getBoolean(R.styleable.ZxLinearLayout_zv_child_select_state_with_me, false);

            //下划线
            unline_height = ta.getDimension(R.styleable.ZxLinearLayout_zv_underlinedHeight, 0);
            if (unline_height != 0) {
                unline_color = ta.getColor(R.styleable.ZxLinearLayout_zv_underlinedColor, 0);
                unline_marginLeft = ta.getDimension(R.styleable.ZxLinearLayout_zv_underlined_marginLeft, 0);
                unline_marginRight = ta.getDimension(R.styleable.ZxLinearLayout_zv_underlined_marginRight, 0);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), (int) (view.getPaddingBottom() + unline_height));
                unLinePaint.setColor(unline_color);
                unLinePaint.setStrokeWidth(unline_height);
            }
            ta.recycle();
        } else if (view instanceof ZxRelativeLayout) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxRelativeLayout);
            radius = ta.getDimension(R.styleable.ZxRelativeLayout_zv_radius, 0);
            borderWidth = ta.getDimensionPixelSize(R.styleable.ZxRelativeLayout_zv_borderWidth, 0);
            borderColor = ta.getColor(R.styleable.ZxRelativeLayout_zv_borderColor, Color.TRANSPARENT);
            isRadiusHalfHeight = ta.getBoolean(R.styleable.ZxRelativeLayout_zv_radiusHalfHeight, false);

            leftTopRadius = ta.getDimension(R.styleable.ZxRelativeLayout_zv_leftTopRadius, 0);
            rightTopRadius = ta.getDimension(R.styleable.ZxRelativeLayout_zv_rightTopRadius, 0);
            leftBottomRadius = ta.getDimension(R.styleable.ZxRelativeLayout_zv_leftBottomRadius, 0);
            rightBottomRadius = ta.getDimension(R.styleable.ZxRelativeLayout_zv_rightBottomRadius, 0);

            borderPressColor = ta.getColor(R.styleable.ZxRelativeLayout_zv_borderPressColor, Integer.MAX_VALUE);
            backgroundPressColor = ta.getColor(R.styleable.ZxRelativeLayout_zv_backgroundPressColor, Integer.MAX_VALUE);

            borderSelectColor = ta.getColor(R.styleable.ZxRelativeLayout_zv_borderSelectColor, Integer.MAX_VALUE);
            backgroundSelectColor = ta.getColor(R.styleable.ZxRelativeLayout_zv_backgroundSelectColor, Integer.MAX_VALUE);
            childSelect = ta.getBoolean(R.styleable.ZxRelativeLayout_zv_child_select_state_with_me, false);
            //下划线
            unline_height = ta.getDimension(R.styleable.ZxRelativeLayout_zv_underlinedHeight, 0);
            if (unline_height != 0) {
                unline_color = ta.getColor(R.styleable.ZxRelativeLayout_zv_underlinedColor, 0);
                unline_marginLeft = ta.getDimension(R.styleable.ZxRelativeLayout_zv_underlined_marginLeft, 0);
                unline_marginRight = ta.getDimension(R.styleable.ZxRelativeLayout_zv_underlined_marginRight, 0);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), (int) (view.getPaddingBottom() + unline_height));
                unLinePaint.setColor(unline_color);
                unLinePaint.setStrokeWidth(unline_height);
            }
            ta.recycle();
        } else if (view instanceof ZxConstraintLayout) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxConstraintLayout);
            radius = ta.getDimension(R.styleable.ZxConstraintLayout_zv_radius, 0);
            borderWidth = ta.getDimensionPixelSize(R.styleable.ZxConstraintLayout_zv_borderWidth, 0);
            borderColor = ta.getColor(R.styleable.ZxConstraintLayout_zv_borderColor, Color.TRANSPARENT);
            isRadiusHalfHeight = ta.getBoolean(R.styleable.ZxConstraintLayout_zv_radiusHalfHeight, false);

            leftTopRadius = ta.getDimension(R.styleable.ZxConstraintLayout_zv_leftTopRadius, 0);
            rightTopRadius = ta.getDimension(R.styleable.ZxConstraintLayout_zv_rightTopRadius, 0);
            leftBottomRadius = ta.getDimension(R.styleable.ZxConstraintLayout_zv_leftBottomRadius, 0);
            rightBottomRadius = ta.getDimension(R.styleable.ZxConstraintLayout_zv_rightBottomRadius, 0);

            borderPressColor = ta.getColor(R.styleable.ZxConstraintLayout_zv_borderPressColor, Integer.MAX_VALUE);
            backgroundPressColor = ta.getColor(R.styleable.ZxConstraintLayout_zv_backgroundPressColor, Integer.MAX_VALUE);

            borderSelectColor = ta.getColor(R.styleable.ZxConstraintLayout_zv_borderSelectColor, Integer.MAX_VALUE);
            backgroundSelectColor = ta.getColor(R.styleable.ZxConstraintLayout_zv_backgroundSelectColor, Integer.MAX_VALUE);
            childSelect = ta.getBoolean(R.styleable.ZxConstraintLayout_zv_child_select_state_with_me, false);

            ta.recycle();
        }

        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            backgroundColor = colorDrawable.getColor();
        }
        if (borderColor != Color.TRANSPARENT && borderWidth < 1) {
            borderWidth = 1;
        }
        if (backgroundPressColor != Integer.MAX_VALUE) {
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
    }



    public void setSelected(boolean selected) {
        if (selectBackground != null) {
            if (selected) {
                if (selectBackground != null) {
                    view.setBackgroundDrawable(selectBackground);
                }
            } else {
                view.setBackgroundDrawable(normalBackground);
            }
            if (childSelect) {
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int totalViewsCount = viewGroup.getChildCount();
                    for (int i = 0; i < totalViewsCount; i++) {
                        viewGroup.getChildAt(i).setSelected(selected);
                    }
                }
            }
        }
    }

    public void setBgSelector() {
        if (isRadiusHalfHeight) {
            leftTopRadius = leftBottomRadius = rightTopRadius = rightBottomRadius = view.getHeight() / 2;
        }

        StateListDrawable bg = new StateListDrawable();

        setDrawable(normalBackground, backgroundColor, borderColor);
        bg.addState(new int[]{-android.R.attr.state_pressed}, normalBackground);
        if (backgroundPressColor != Integer.MAX_VALUE || borderPressColor != Integer.MAX_VALUE) {
            pressBackground = new GradientDrawable();
            setDrawable(pressBackground, backgroundPressColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressColor,
                    borderPressColor == Integer.MAX_VALUE ? borderColor : borderPressColor);
            bg.addState(new int[]{android.R.attr.state_pressed}, pressBackground);
        }

        //设置选择后的颜色
        if (backgroundSelectColor != Integer.MAX_VALUE || borderPressColor != Integer.MAX_VALUE) {
            selectBackground = new GradientDrawable();
            setDrawable(selectBackground, backgroundSelectColor == Integer.MAX_VALUE ? backgroundColor : backgroundSelectColor,
                    borderSelectColor == Integer.MAX_VALUE ? borderColor : borderSelectColor);
            bg.addState(new int[]{android.R.attr.state_selected}, selectBackground);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(bg);
        } else {
            view.setBackgroundDrawable(bg);
        }
        if (view.isSelected()) {
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
    }
    public float getUnline_height() {
        return unline_height;
    }

    public int getUnline_color() {
        return unline_color;
    }

    public float getUnline_marginLeft() {
        return unline_marginLeft;
    }

    public float getUnline_marginRight() {
        return unline_marginRight;
    }

    public Paint getUnLinePaint() {
        return unLinePaint;
    }
}

