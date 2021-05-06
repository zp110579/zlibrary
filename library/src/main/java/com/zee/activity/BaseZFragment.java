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

import com.zee.utils.ZEventBusUtils;

public abstract class BaseZFragment extends Fragment implements IBase {

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

    public void startActivity(Class<?> paClass) {
        Intent loIntent = new Intent(getActivity(), paClass);
        startActivity(loIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZEventBusUtils.unregister(this);
    }
}