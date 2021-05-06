package com.zee.toast;

public interface IBaseToast {

    void onShowToast(String message);

    void onShowToastWithBottom(String message);

    void showBaseToast(final String str, final int showTime, final int gravity, int xOffset, int yOffset);

    void setCustomToastBeanSettings(CustomToastBeanSettings customToastBeanSettings);

}
