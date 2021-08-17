package com.zee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zee.log.ZLog;
import com.zee.utils.ZEventBusUtils;

public abstract class BaseZFragment extends Fragment implements IBase {
    private Boolean mIsVisibleToUser = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mIsVisibleToUser = !hidden;
        onShowToUser(!hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (mIsVisibleToUser != isVisibleToUser) {
            mIsVisibleToUser = isVisibleToUser;
            onShowToUser(isVisibleToUser);
        }
    }


    public void onShowToUser(boolean isVisibleToUser) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsVisibleToUser) {
            onShowToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsVisibleToUser) {
            onShowToUser(false);
        }
    }

    public void startActivity(Class<?> paClass) {
        Intent loIntent = new Intent(getActivity(), paClass);
        startActivity(loIntent);
    }

    public void startActivity(Intent paIntent) {
        startActivity(paIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZEventBusUtils.unregister(this);
    }
}