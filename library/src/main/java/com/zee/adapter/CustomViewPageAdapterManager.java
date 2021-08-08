package com.zee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * created by zee on 2020/9/11
 */
public class CustomViewPageAdapterManager {
    private FragmentManager mSupportFragmentManager;
    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();

    public CustomViewPageAdapterManager(FragmentActivity fragmentActivity) {
        mSupportFragmentManager = fragmentActivity.getSupportFragmentManager();
    }

    public CustomViewPageAdapterManager(Fragment fragment) {
        mSupportFragmentManager = fragment.getChildFragmentManager();
    }

    public CustomViewPageAdapterManager(FragmentManager fragmentManager) {
        mSupportFragmentManager = fragmentManager;
    }

    public CustomViewPageAdapterManager addFragment(Fragment fragment) {
        mFragmentArrayList.add(fragment);
        return this;
    }

    public FragmentStatePagerAdapter getFragmentStatePagerAdapter() {
        return new FragmentStatePagerAdapter(mSupportFragmentManager) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentArrayList.get(i);
            }

            @Override
            public int getCount() {
                return mFragmentArrayList.size();
            }
        };
    }

    public FragmentPagerAdapter getFragmentPagerAdapter() {
        return new FragmentPagerAdapter(mSupportFragmentManager) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentArrayList.get(i);
            }

            @Override
            public int getCount() {
                return mFragmentArrayList.size();
            }
        };
    }
}
