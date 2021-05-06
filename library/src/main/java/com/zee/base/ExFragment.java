package com.zee.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zee.utils.ZEventBusUtils;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public abstract class ExFragment extends Fragment implements IBindView {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutID(), container, false);// initViews(inflater, container);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(savedInstanceState);
        initViews();
        getView();
    }

    public View getRootView() {
        return mView;
    }


    public void initViews(Bundle savedInstanceState) {
    }

}
