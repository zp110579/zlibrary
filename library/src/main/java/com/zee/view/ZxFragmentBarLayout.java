package com.zee.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.zee.utils.ZListUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31 0031.
 */

public class ZxFragmentBarLayout extends ZxLinearLayout {
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private int mFragmentLayoutID;
    private static final String saveTag = "ZEE_FRAGMENT";
    private static final String saveSelectTag = "ZEE_FRAGMENT_CURINDEX";

    private int curIndex;//当前选的TextView

    public ZxFragmentBarLayout(Context context) {
        super(context);
    }

    public ZxFragmentBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ZxFragmentBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final int finalI = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (curIndex != finalI) {
                        ZxFragmentBarLayout.this.onClick(v, finalI);
                        curIndex = finalI;
                    }
                }
            });
        }
    }

    public void setFragmentLayoutID(int mFragmentLayoutID) {
        this.mFragmentLayoutID = mFragmentLayoutID;
    }

    public void setFragmentViewData(final FragmentManager fragmentManager, @IdRes final int fragmentLayoutID, final List<Fragment> fragmentList) {
        if (ZListUtils.isEmpty(fragmentList)) {
            throw new NullPointerException("fragmentList is Null");
        }

        mFragmentManager = fragmentManager;
        mFragmentList = fragmentList;
        mFragmentLayoutID = fragmentLayoutID;
    }

    private void onClick(View view, int finalI) {
        FragmentTransaction trx = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.findFragmentByTag(finalI + "");
        trx.hide(currentFragment);

        Fragment nextFragment = mFragmentManager.findFragmentByTag(finalI + "");
        if (nextFragment == null) {
            nextFragment = mFragmentList.get(finalI);
        }
        if (!nextFragment.isAdded()) {
            trx.add(mFragmentLayoutID, nextFragment, finalI + "");
        }
        trx.show(nextFragment).commitAllowingStateLoss();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle outState = new Bundle();
        outState.putInt(saveSelectTag, curIndex);
        if (ZListUtils.isNoEmpty(mFragmentList)) {
            for (int i = 0; i < mFragmentList.size(); i++) {
                if (mFragmentList.get(i).isAdded()) {
                    mFragmentManager.putFragment(outState, i + saveTag, mFragmentList.get(i));
                }
            }
        }
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            curIndex = bundle.getInt(saveSelectTag);
        }
        super.onRestoreInstanceState(state);
    }
}
