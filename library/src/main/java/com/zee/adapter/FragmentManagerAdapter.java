package com.zee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class FragmentManagerAdapter extends ZxBottomBarAdapter {

    public FragmentManagerAdapter(Fragment fragment, int containerViewId) {
        super(fragment, 0, containerViewId);
    }

    public FragmentManagerAdapter(AppCompatActivity paActivity, int containerViewId) {
        super(paActivity, 0, containerViewId);
    }

    public void onClick(int position, View view) {

    }

    public abstract Fragment getFragment(int index);


}
