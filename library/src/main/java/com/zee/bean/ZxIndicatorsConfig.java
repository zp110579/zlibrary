package com.zee.bean;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.zee.libs.R;
import com.zee.utils.UIUtils;


/**
 * @author Administrator
 */

@SuppressWarnings(value = {"unchecked", "deprecation"})
public class ZxIndicatorsConfig {
    private Drawable selectDrawable;
    private Drawable unSelectDrawable;
    private int indicatorsWidth;
    private int indicatorsHeight;
    private boolean isAnimal;
    private int indicatorsGap;
    private int radius;
    private int borderWidth;
    private int borderColor = 0;
    private int borderColor_select = 0;
    int selectColor = Color.parseColor("#ffffff");
    int unSelectColor = Color.parseColor("#990D0D0D");
    int selectRes = 0;
    int normalRes = 0;
    private int defaultValue = 7;

    public ZxIndicatorsConfig() {
        indicatorsWidth = UIUtils.dpToPx(defaultValue);
        indicatorsHeight = UIUtils.dpToPx(defaultValue);
        indicatorsGap = UIUtils.dpToPx(defaultValue);
        radius = indicatorsHeight / 2;
        borderWidth = 1;
    }

    @SuppressWarnings("unchecked")
    public ZxIndicatorsConfig(TypedArray typedArray, Context context, int type) {
        if (type == 0) {
            indicatorsWidth = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_width, dp2px(defaultValue, context));
            indicatorsHeight = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_height, dp2px(defaultValue, context));
            indicatorsGap = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_gap, dp2px(defaultValue, context));
            radius = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_radius, dp2px(indicatorsHeight, context) / 2);
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_borderWidth, dp2px(1, context));
            borderColor = typedArray.getColor(R.styleable.ZxBannerView_zv_indicator_borderColor, borderColor);
            borderColor_select = typedArray.getColor(R.styleable.ZxBannerView_zv_indicator_borderColor_select, borderColor_select);
            isAnimal = typedArray.getBoolean(R.styleable.ZxBannerView_zv_indicator_animal, false);

            selectColor = typedArray.getColor(R.styleable.ZxBannerView_zv_indicator_color_select, selectColor);
            unSelectColor = typedArray.getColor(R.styleable.ZxBannerView_zv_indicator_color, unSelectColor);
            selectRes = typedArray.getResourceId(R.styleable.ZxBannerView_zv_indicator_res_select, 0);
            normalRes = typedArray.getResourceId(R.styleable.ZxBannerView_zv_indicator_res, 0);

            if (selectRes != 0) {
                this.selectDrawable = context.getResources().getDrawable(selectRes);
            } else {
                this.selectDrawable = getDrawable(selectColor, radius, borderColor_select);
            }

            if (normalRes != 0) {
                this.unSelectDrawable = context.getResources().getDrawable(normalRes);
            } else {
                this.unSelectDrawable = getDrawable(unSelectColor, radius, borderColor);
            }
        } else if (type == 1) {
            indicatorsWidth = typedArray.getDimensionPixelSize(R.styleable.ZxIndicatorsView_zv_width, dp2px(defaultValue, context));
            indicatorsHeight = typedArray.getDimensionPixelSize(R.styleable.ZxIndicatorsView_zv_height, dp2px(defaultValue, context));
            radius = typedArray.getDimensionPixelSize(R.styleable.ZxIndicatorsView_zv_radius, dp2px(indicatorsHeight, context) / 2);
            indicatorsGap = typedArray.getDimensionPixelSize(R.styleable.ZxIndicatorsView_zv_gap, dp2px(defaultValue, context));
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.ZxIndicatorsView_zv_borderWidth, dp2px(1, context));

            selectColor = typedArray.getColor(R.styleable.ZxIndicatorsView_zv_color_select, Color.parseColor("#ffffff"));
            unSelectColor = typedArray.getColor(R.styleable.ZxIndicatorsView_zv_color, Color.parseColor("#990D0D0D"));
            borderColor = typedArray.getColor(R.styleable.ZxIndicatorsView_zv_borderColor, unSelectColor);
            borderColor_select = typedArray.getColor(R.styleable.ZxIndicatorsView_zv_borderColor_select, borderColor);

            selectRes = typedArray.getResourceId(R.styleable.ZxIndicatorsView_zv_res_select, 0);
            normalRes = typedArray.getResourceId(R.styleable.ZxIndicatorsView_zv_res, 0);

            if (selectRes != 0) {
                this.selectDrawable = context.getResources().getDrawable(selectRes);
            } else {
                this.selectDrawable = getDrawable(selectColor, radius, borderColor_select);
            }

            if (normalRes != 0) {
                this.unSelectDrawable = context.getResources().getDrawable(normalRes);
            } else {
                this.unSelectDrawable = getDrawable(unSelectColor, radius, borderColor);
            }
        }
    }

    public Drawable getSelectDrawable() {
        if (selectDrawable == null) {
            if (selectRes != 0) {
                this.selectDrawable = UIUtils.getResources().getDrawable(selectRes);
            } else {
                this.selectDrawable = getDrawable(selectColor, radius, borderColor_select);
            }
        }
        return selectDrawable;
    }

    public Drawable getUnSelectDrawable() {
        if (unSelectDrawable == null) {
            if (normalRes != 0) {
                this.unSelectDrawable = UIUtils.getResources().getDrawable(normalRes);
            } else {
                this.unSelectDrawable = getDrawable(unSelectColor, radius, borderColor);
            }
        }
        return unSelectDrawable;
    }

    public int getIndicatorsWidth() {
        return indicatorsWidth;
    }

    public int getIndicatorsHeight() {
        return indicatorsHeight;
    }

    public boolean isAnimal() {
        return isAnimal;
    }

    public int getIndicatorsGap() {
        return indicatorsGap;
    }

    public int getRadius() {
        return radius;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getBorderColor_select() {
        return borderColor_select;
    }


    public void setColor(@ColorRes int unSelectColor, @ColorRes int selectColor) {
        this.unSelectColor = UIUtils.getColor(unSelectColor);
        this.selectColor = UIUtils.getColor(selectColor);
        if (borderColor > 0 || borderColor_select > 0) {
            setBorderColor(unSelectColor, selectColor);
        }
    }

    public void setBorderColor(@ColorRes int unSelectBorderColor, @ColorRes int selectBorderColor) {
        this.borderColor = UIUtils.getColor(unSelectBorderColor);
        this.borderColor_select = UIUtils.getColor(selectBorderColor);
    }

    public void setDrawable(@DrawableRes int selectDrawable, @DrawableRes int unSelectDrawable) {
        if (selectDrawable != 0) {
            this.selectDrawable = UIUtils.getResources().getDrawable(selectDrawable);
        }
        if (unSelectDrawable != 0) {
            this.unSelectDrawable = UIUtils.getResources().getDrawable(unSelectDrawable);
        }
    }

    public void setIndicatorsWidth(int dpValue) {
        this.indicatorsWidth = UIUtils.dpToPx(dpValue);
    }

    public void setIndicatorsHeight(int dpValue) {
        this.indicatorsHeight = UIUtils.dpToPx(dpValue);
        if (radius < 0) {
            radius = indicatorsHeight / 2;
        }
    }

    public void setAnimal(boolean animal) {
        isAnimal = animal;
    }

    public void setIndicatorsGap(int dpValue) {
        this.indicatorsGap = UIUtils.dpToPx(dpValue);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setBorderWidth(int dpValue) {
        this.borderWidth = UIUtils.dpToPx(dpValue);
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    private GradientDrawable getDrawable(int color, float raduis, int borderColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(raduis);
        drawable.setStroke(borderWidth, borderColor);
        drawable.setColor(color);
        return drawable;
    }

    private int dp2px(float dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
