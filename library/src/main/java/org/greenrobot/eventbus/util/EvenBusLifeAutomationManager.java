package org.greenrobot.eventbus.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zee.manager.FragmentLifecycleManager;

/**
 * created by zee on 2021/7/2.
 * EventBus检测生命周期是否可以自动管理
 */
public class EvenBusLifeAutomationManager {

    public static Boolean isAutomationManager(Object object) {
        if (object instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) object;
            fragmentActivity.getLifecycle().addObserver(FragmentLifecycleManager.INSTANCE);
            return true;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            fragment.getLifecycle().addObserver(FragmentLifecycleManager.INSTANCE);
            return true;
        }
        return false;
    }

}
