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
public class ZxBannerViewConfig {
    ZxIndicatorsConfig mIndicatorsConfig;
    private int indicatorsMarginLeft;
    private int indicatorsMarginRight;
    private int indicatorsMarginBottom;
    private int intervalTime;
    private boolean showIndicator = true;
    private int gravity;

    public ZxBannerViewConfig() {
        mIndicatorsConfig = new ZxIndicatorsConfig();
        indicatorsMarginBottom = UIUtils.dpToPx(8);
    }

    public ZxBannerViewConfig(TypedArray typedArray, Context context, int type) {
        mIndicatorsConfig = new ZxIndicatorsConfig(typedArray, context, type);
        intervalTime = typedArray.getInteger(R.styleable.ZxBannerView_zv_interval_time, 0);
        showIndicator = typedArray.getBoolean(R.styleable.ZxBannerView_zv_indicator_visible, true);
        indicatorsMarginBottom = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_margin_bottom, UIUtils.dpToPx(10));
        indicatorsMarginLeft = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_margin_left, UIUtils.dpToPx(10));
        indicatorsMarginRight = typedArray.getDimensionPixelSize(R.styleable.ZxBannerView_zv_indicator_margin_right, UIUtils.dpToPx(10));
        gravity = typedArray.getInt(R.styleable.ZxBannerView_zv_indicator_gravity, 0);
    }

    public Drawable getSelectDrawable() {
        return mIndicatorsConfig.getSelectDrawable();
    }

    public Drawable getUnSelectDrawable() {
        return mIndicatorsConfig.getUnSelectDrawable();
    }

    public int getIndicatorsWidth() {
        return mIndicatorsConfig.getIndicatorsWidth();
    }

    public int getIndicatorsHeight() {
        return mIndicatorsConfig.getIndicatorsHeight();
    }

    public boolean isAnimal() {
//        return true;
        return mIndicatorsConfig.isAnimal();
    }

    public int getIndicatorsGap() {
        return mIndicatorsConfig.getIndicatorsGap();
    }

    public int getRadius() {
        return mIndicatorsConfig.getRadius();
    }

    public int getBorderWidth() {
        return mIndicatorsConfig.getBorderWidth();
    }

    public int getBorderColor() {
        return mIndicatorsConfig.getBorderColor();
    }


    public int getIndicatorsMarginLeft() {
        return indicatorsMarginLeft;
    }

    public int getIndicatorsMarginRight() {
        return indicatorsMarginRight;
    }

    public int getIndicatorsMarginBottom() {
        return indicatorsMarginBottom;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public boolean isShowIndicator() {
        return showIndicator;
    }

    public int getGravity() {
        return gravity;
    }

    public void setColor(@ColorRes int unSelectColor, @ColorRes int selectBorderColor) {
        mIndicatorsConfig.setColor(unSelectColor, selectBorderColor);
    }

    public void setBorderColor(@ColorRes int unSelectBorderColor, @ColorRes int selectBorderColor) {
        mIndicatorsConfig.setBorderColor(unSelectBorderColor, selectBorderColor);
    }

    public void setDrawable(@DrawableRes int selectDrawable, @DrawableRes int unSelectDrawable) {
        mIndicatorsConfig.setDrawable(selectDrawable, unSelectDrawable);
    }

    public void setIndicatorsWidth(int dpValue) {
        mIndicatorsConfig.setIndicatorsWidth(dpValue);
    }

    public void setIndicatorsHeight(int dpValue) {
        mIndicatorsConfig.setIndicatorsHeight(dpValue);
    }

    public void setAnimal(boolean animal) {
        mIndicatorsConfig.setAnimal(animal);
    }

    public void setIndicatorsGap(int dpValue) {
        mIndicatorsConfig.setIndicatorsGap(dpValue);
    }

    public void setRadius(int radius) {
        mIndicatorsConfig.setRadius(radius);
    }

    public void setBorderWidth(int dpValue) {
        mIndicatorsConfig.setBorderWidth(dpValue);
    }

    public void setBorderColor(int borderColor) {
        mIndicatorsConfig.setBorderColor(borderColor);
    }


    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setIndicatorsMarginLeft(int indicatorsMarginLeft) {
        this.indicatorsMarginLeft = indicatorsMarginLeft;
    }

    public void setIndicatorsMarginRight(int indicatorsMarginRight) {
        this.indicatorsMarginRight = indicatorsMarginRight;
    }

    public void setIndicatorsMarginBottom(int indicatorsMarginBottom) {
        this.indicatorsMarginBottom = indicatorsMarginBottom;
    }

    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
}
