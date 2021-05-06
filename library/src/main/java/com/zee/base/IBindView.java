package com.zee.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Administrator on 2017-05-11.
 */

public interface IBindView {
    @LayoutRes
    int getLayoutID();

    void initViews();
}
