package com.zee.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zee.adapter.ZxBottomBarAdapter;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;

public class ZxBottomBarLayout extends ZxLinearLayout {
    ArrayList<View> mArrayList = new ArrayList<>();
    private ZxBottomBarAdapter mZxBottomBarAdapter;

    public ZxBottomBarLayout(Context context) {
        super(context);
    }

    public ZxBottomBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZxBottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initViews() {
        mArrayList.clear();
        int size = getChildCount();
        for (int loI = 0; loI < size; loI++) {
            View loView = getChildAt(loI);
            mArrayList.add(loView);
        }
        if (ZListUtils.isNoEmpty(mArrayList)) {
            for (int i = 0; i < mArrayList.size(); i++) {
                View loView = mArrayList.get(i);
                final int index = i;
                loView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int viewID = -1;
                        viewID = view.getId();
                        if (mZxBottomBarAdapter != null && mZxBottomBarAdapter.isCanClick(view)) {
                            for (View loView1 : mArrayList) {
                                loView1.setSelected(false);
                            }
                            view.setSelected(true);
                            try {
                                mZxBottomBarAdapter.selectIndex(viewID);
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            }
        }
    }

    public void setZxBottomBarAdapter(ZxBottomBarAdapter paZxBottomBarAdapter) {
        initViews();
        mZxBottomBarAdapter = paZxBottomBarAdapter;
        View view = findViewById(paZxBottomBarAdapter.getDefaultSelectViewID());
        if (view != null) {
            view.setSelected(true);
        }
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        super.onSaveInstanceState();
//        Bundle bundle = new Bundle();
//        if (mZxBottomBarAdapter != null) {
//            mZxBottomBarAdapter.onSaveState(bundle);
//        }
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        super.onRestoreInstanceState(state);
//        if (state instanceof Bundle) {
//            Bundle bundle = (Bundle) state;
//            if (mZxBottomBarAdapter != null) {
//                mZxBottomBarAdapter.onRestoreState(bundle);
//            }
//        }
//    }
}
