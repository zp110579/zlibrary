package com.zee.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class ZxBottomBarAdapter {
    private int preIndex = -1;//记录上一次选择的位置
    private FragmentManager loSupportFragmentManager;
    private int defaultSelectViewID = 0;//默认选择View的ID
    private int mContainerViewID = 0;

    /**
     * @param fragmentManager FragmentManager是Super还是Child
     * @param defaultViewID   默认选中的ViewID
     * @param containerViewId Fragment 容器ID
     */
    public ZxBottomBarAdapter(FragmentManager fragmentManager, int defaultViewID, int containerViewId) {
        loSupportFragmentManager = fragmentManager;
        defaultSelectViewID = defaultViewID;
        mContainerViewID = containerViewId;
        selectIndex(defaultSelectViewID);
    }

    public ZxBottomBarAdapter(AppCompatActivity paActivity, int defaultViewID, int containerViewId) {
        this(paActivity.getSupportFragmentManager(), defaultViewID, containerViewId);
    }

    public ZxBottomBarAdapter(Fragment fragment, int defaultViewTag, int containerViewId) {
        this(fragment.getChildFragmentManager(), defaultViewTag, containerViewId);
    }

    public int getDefaultSelectViewID() {
        return defaultSelectViewID;
    }


    public abstract Fragment getFragment(int viewID);


    private Fragment findFragmentByTag(int paIndex) {
        Fragment loFragmentByTag = loSupportFragmentManager.findFragmentByTag(paIndex + "");
        return loFragmentByTag;
    }

    public void selectIndex(int index) {
        if (index != preIndex) {
            FragmentTransaction loFragmentTransaction = loSupportFragmentManager.beginTransaction();
            Fragment preFragment = null;//记录上一个Fragment
            if (preIndex != -1) {
                preFragment = findFragmentByTag(preIndex);
            }

            Fragment loFragmentByTag = findFragmentByTag(index);
            if (loFragmentByTag == null) {
                Fragment tempFragment = getFragment(index);
                if (tempFragment != null) {
                    if (preFragment != null) {
                        loFragmentTransaction.hide(preFragment);
                    }
                    loFragmentTransaction.add(mContainerViewID, tempFragment, index + "");
                }
            } else {
                if (preFragment != null) {
                    loFragmentTransaction.hide(preFragment);
                }
                loFragmentTransaction.show(loFragmentByTag);
            }
            loFragmentTransaction.commitAllowingStateLoss();
            preIndex = index;
        }
    }

    public boolean isCanClick(View view) {
        return true;
    }

    public void onSaveState(Bundle bundle) {
        bundle.putInt("index", preIndex);
    }

    public void onRestoreState(Bundle bundle) {
        bundle.getInt("index", 0);
    }
}
