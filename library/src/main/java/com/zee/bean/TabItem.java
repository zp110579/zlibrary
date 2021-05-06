package com.zee.bean;

public class TabItem {
    private String title;
    private int normalResId;
    private int selectResId;

    public TabItem(String title, int normalResId, int selectResId) {
        this.title = title;
        this.normalResId = normalResId;
        this.selectResId = selectResId;
    }

    public String getTitle() {
        return title;
    }

    public int getNormalResId() {
        return normalResId;
    }

    public int getSelectResId() {
        return selectResId;
    }
}
