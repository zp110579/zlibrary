package com.zee.manager;

public class ViewPropertyManager {
    private float radius;
    private int borderWidth;
    private int borderColor;
    private int borderPressColor;
    private int borderSelectColor;

    private boolean isRadiusHalfHeight = false;


    private int textSelectColor;
    private float leftTopRadius;
    private float rightTopRadius;
    private float rightBottomRadius;
    private float leftBottomRadius;


    private int backgroundColor;
    private int backgroundPressColor;
    private int backgroundSelectColor;

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderPressColor(int borderPressColor) {
        this.borderPressColor = borderPressColor;
    }

    public void setBorderSelectColor(int borderSelectColor) {
        this.borderSelectColor = borderSelectColor;
    }

    public void setRadiusHalfHeight(boolean radiusHalfHeight) {
        isRadiusHalfHeight = radiusHalfHeight;
    }

    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
    }

    public void setLeftTopRadius(float leftTopRadius) {
        this.leftTopRadius = leftTopRadius;
    }

    public void setRightTopRadius(float rightTopRadius) {
        this.rightTopRadius = rightTopRadius;
    }

    public void setRightBottomRadius(float rightBottomRadius) {
        this.rightBottomRadius = rightBottomRadius;
    }

    public void setLeftBottomRadius(float leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundPressColor(int backgroundPressColor) {
        this.backgroundPressColor = backgroundPressColor;
    }

    public void setBackgroundSelectColor(int backgroundSelectColor) {
        this.backgroundSelectColor = backgroundSelectColor;
    }


}
