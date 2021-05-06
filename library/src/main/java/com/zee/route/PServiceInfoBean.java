package com.zee.route;

import android.os.Bundle;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class PServiceInfoBean {
    private String name;
    private Bundle mBundle;

    public PServiceInfoBean(String name) {
        this.name = name;
    }

    public PServiceInfoBean withBundle(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }

    public void startService() {
    }
}
