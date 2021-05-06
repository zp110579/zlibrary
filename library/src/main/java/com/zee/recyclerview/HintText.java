package com.zee.recyclerview;

import android.graphics.Color;

public class HintText {
    /**
     * 字体颜色
     */
    private int textColor = Color.TRANSPARENT;
    /**
     * 字体大小
     */
    private float textSize = -1;

    //*********************************刷新的颜色**********************************
    /**
     * 设置刷新的字体颜色
     */
    private int refresh_textColor = Color.TRANSPARENT;
    /**
     * 设置刷新字体的大小
     */
    private float refresh_textSize = -1;
    /**
     * 提示下拉刷新
     */
    private String refresh_hintPullDownText = "下拉刷新";
    /**
     * 提示松手开始刷新
     */
    private String refresh_hintReleaseHandText = "释放刷新";
    /**
     * 开始刷新
     */
    private String refresh_refreshStartText = "正在刷新...";
    /**
     * 刷新结束
     */
    private String refresh_refreshEndText = "刷新结束";

    //*********************************加载更多**********************************
    /**
     * 加载中
     */
    private String loadMore_loadStartText = "正在加载...";
    /**
     * 加载结束
     */
    private String loadMore_loadEndText = "加载完成";
    /**
     * 没有数据了
     */
    private String loadMore_noMoreText = "没有数据了";
    /**
     * 加载的字体颜色设置
     */
    private int loadMore_textColor = Color.TRANSPARENT;
    /**
     * 设置加载字体的大小
     */
    private float loadMore_textSize = -1;
    /**
     * 刷新的背景颜色
     */
    public int refreshBackgroundColor=Color.TRANSPARENT;
    /**
     * 还剩下几个数据的时候自动加载
     */
    private int limitNumber;


    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getRefresh_textColor() {
        int tempTextColor = refresh_textColor;
        if (tempTextColor == Color.TRANSPARENT) {
            tempTextColor = textColor;
        }
        return tempTextColor;
    }

    public void setRefresh_textColor(int refresh_textColor) {
        this.refresh_textColor = refresh_textColor;
    }

    public float getRefresh_textSize() {
        float tempSize = refresh_textSize;
        if (tempSize == -1) {
            tempSize = textSize;
        }

        return tempSize;
    }

    public void setRefresh_textSize(float refresh_textSize) {
        this.refresh_textSize = refresh_textSize;
    }

    public String getRefresh_hintPullDownText() {
        return refresh_hintPullDownText;
    }

    public void setRefresh_hintPullDownText(String refresh_hintPullDownText) {
        this.refresh_hintPullDownText = refresh_hintPullDownText;
    }

    public String getRefresh_hintReleaseHandText() {
        return refresh_hintReleaseHandText;
    }

    public void setRefresh_hintReleaseHandText(String refresh_hintReleaseHandText) {
        this.refresh_hintReleaseHandText = refresh_hintReleaseHandText;
    }

    public String getRefresh_refreshStartText() {
        return refresh_refreshStartText;
    }

    public void setRefresh_refreshStartText(String refresh_refreshStartText) {
        this.refresh_refreshStartText = refresh_refreshStartText;
    }

    public String getRefresh_refreshEndText() {
        return refresh_refreshEndText;
    }

    public void setRefreshBackgroundColor(int refreshBackgroundColor) {
        this.refreshBackgroundColor = refreshBackgroundColor;
    }

    public int getRefreshBackgroundColor() {
        return refreshBackgroundColor;
    }

    public void setRefresh_refreshEndText(String refresh_refreshEndText) {
        this.refresh_refreshEndText = refresh_refreshEndText;
    }

    public String getLoadMore_loadStartText() {

        return loadMore_loadStartText;
    }

    public void setLoadMore_loadStartText(String loadMore_loadStartText) {
        this.loadMore_loadStartText = loadMore_loadStartText;
    }

    public String getLoadMore_loadEndText() {
        return loadMore_loadEndText;
    }

    public void setLoadMore_loadEndText(String loadMore_loadEndText) {
        this.loadMore_loadEndText = loadMore_loadEndText;
    }

    public String getLoadMore_noMoreText() {
        return loadMore_noMoreText;
    }

    public void setLoadMore_noMoreText(String loadMore_noMoreText) {
        this.loadMore_noMoreText = loadMore_noMoreText;
    }

    public int getLoadMore_textColor() {
        int tempTextColor = loadMore_textColor;
        if (tempTextColor == Color.TRANSPARENT) {
            tempTextColor = textColor;
        }

        return tempTextColor;
    }

    public void setLoadMore_textColor(int loadMore_textColor) {
        this.loadMore_textColor = loadMore_textColor;
    }

    public float getLoadMore_textSize() {
        float tempTextSize = loadMore_textSize;
        if (tempTextSize == -1) {
            tempTextSize = textSize;
        }
        return tempTextSize;
    }

    public void setLoadMore_textSize(float loadMore_textSize) {
        this.loadMore_textSize = loadMore_textSize;
    }

    public int getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(int limitNumber) {
        this.limitNumber = limitNumber;
    }
}
