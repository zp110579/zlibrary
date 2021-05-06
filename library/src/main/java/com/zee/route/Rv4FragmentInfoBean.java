package com.zee.route;

import android.support.v4.app.Fragment;

import com.zee.listener.LetsGoListener;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class Rv4FragmentInfoBean extends BaseInfoBean<Rv4FragmentInfoBean> {

    public Rv4FragmentInfoBean(Class<?> classZ) {
        super(classZ, 2);
    }

    public Rv4FragmentInfoBean(String name, String module) {
        super(name, module, 2);
    }

    public Fragment letsGo() {
        return letsGo(null);
    }

    public Fragment letsGo(LetsGoListener letsGoListener) {
        if (mGoalClass != null) {
            return PFragmentManagerP.getFragment(mGoalClass, mBundle, letsGoListener);
        } else {
            return PFragmentManagerP.getFragment(this, letsGoListener);
        }
    }
}
