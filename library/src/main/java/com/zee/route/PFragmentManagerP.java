package com.zee.route;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.zee.listener.LetsGoListener;

import com.zee.bean.RouteBean;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class PFragmentManagerP extends PRouterManager {
    static Fragment getFragment(@NonNull Class<?> classz, Bundle bundle, LetsGoListener letsGoListener) {
        if (letsGoListener != null) {
            letsGoListener.onStart();
        }

        Fragment fragment = getFragment(classz, bundle);

        if (letsGoListener != null) {
            letsGoListener.onEnd();
        }
        return fragment;
    }

    static Fragment getFragment(Rv4FragmentInfoBean rv4FragmentInfoBean, LetsGoListener letsGoListener) {
        if (letsGoListener != null) {
            letsGoListener.onStart();
        }
        RouteBean goalBean = getRouteBean(rv4FragmentInfoBean);
        if (goalBean != null) {
            if (letsGoListener != null) {
                letsGoListener.onFound(goalBean.getRouteClass().getName());
            }

            try {
                Class<?> classz = goalBean.getRouteClass();
                final Bundle bundle = rv4FragmentInfoBean.getBundle();
                Fragment fragment = getFragment(classz, bundle);
                fragment.setArguments(bundle);
                if (letsGoListener != null) {
                    letsGoListener.onEnd();
                }
                return fragment;
            } catch (Exception e) {
                e.printStackTrace();
                if (letsGoListener != null) {
                    letsGoListener.onError("获取Fragment发生异常");
                }
            }
        }
        if (letsGoListener != null) {
            letsGoListener.onError("没有发现");
            letsGoListener.onEnd();
        }
        return null;
    }

    private static Fragment getFragment(Class<?> classz, Bundle bundle) {
        Class<?> aClass = classz;
        try {
            Object object = aClass.newInstance();
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                fragment.setArguments(bundle);
                return fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
