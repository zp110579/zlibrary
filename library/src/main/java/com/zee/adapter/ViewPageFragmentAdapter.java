package com.zee.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.zee.bean.ITitle;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewPageFragmentAdapter {
    private List<? extends ITitle> mITitleList = new ArrayList<>();
    private ViewPager mViewPager;

    public ViewPageFragmentAdapter(FragmentActivity fragmentActivity, ViewPager viewPager, ArrayList<? extends ITitle> list) {
        this(fragmentActivity.getSupportFragmentManager(), viewPager, list);
    }

    public ViewPageFragmentAdapter(Fragment fragment, ViewPager viewPager, ArrayList<? extends ITitle> list) {
        this(fragment.getChildFragmentManager(), viewPager, list);
    }

    public ViewPageFragmentAdapter(FragmentManager fragmentManager, ViewPager viewPager, ArrayList<? extends ITitle> list) {
        mITitleList = list;
        viewPager.setAdapter(new TagViewPageFragment(fragmentManager));
        mViewPager = viewPager;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public abstract Fragment getV4Fragment(int index);

    class TagViewPageFragment extends FragmentStatePagerAdapter {

        TagViewPageFragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return getV4Fragment(i);
        }

        @Override
        public int getCount() {
            return mITitleList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (ZListUtils.isNoEmpty(mITitleList)) {
                return mITitleList.get(position).getTitle();
            }
            return null;
        }
    }
}
