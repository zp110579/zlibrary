package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zee.bean.ZxIndicatorsConfig;
import com.zee.libs.R;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;

/**
 * @author Administrator
 */
public class ZxIndicatorsView extends LinearLayout {

    private ImageView selectView;
    private int currentItem;

    private ZxIndicatorsConfig mZxIndicatorsConfig;

    private ArrayList<ImageView> indicatorViews = new ArrayList<>();

    public ZxIndicatorsView(Context context) {
        this(context, null);
    }

    public ZxIndicatorsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxIndicatorsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZxIndicatorsView);
            mZxIndicatorsConfig = new ZxIndicatorsConfig(typedArray, context, 1);

            setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            typedArray.recycle();
        } else {
            mZxIndicatorsConfig = new ZxIndicatorsConfig();
        }
    }

    public void setIndicatorCounts(int count) {
        if (count <= 0) {
            return;
        }

        indicatorViews.clear();
        removeAllViews();

        for (int i = 0; i < count; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageDrawable(mZxIndicatorsConfig.isAnimal() ? mZxIndicatorsConfig.getUnSelectDrawable() : (currentItem == i ? mZxIndicatorsConfig.getSelectDrawable() : mZxIndicatorsConfig.getUnSelectDrawable()));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mZxIndicatorsConfig.getIndicatorsWidth(), mZxIndicatorsConfig.getIndicatorsHeight());
            lp.leftMargin = i == 0 ? 0 : mZxIndicatorsConfig.getIndicatorsGap();
            addView(iv, lp);
            indicatorViews.add(iv);
        }

        if (mZxIndicatorsConfig.isAnimal()) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mZxIndicatorsConfig.getIndicatorsWidth(), mZxIndicatorsConfig.getIndicatorsHeight());
            lp.leftMargin = (mZxIndicatorsConfig.getIndicatorsWidth() + mZxIndicatorsConfig.getIndicatorsGap()) * currentItem;
            selectView = new ImageView(getContext());
            selectView.setImageDrawable(mZxIndicatorsConfig.getSelectDrawable());
            addView(selectView, lp);
        }
    }

    /**
     * 设置选中项
     */
    public void setSelection(int selection) {
        if (selection != currentItem && ZListUtils.isNoEmpty(indicatorViews)) {
            for (int i = 0; i < indicatorViews.size(); i++) {
                indicatorViews.get(i).setImageDrawable(i == selection % indicatorViews.size() ? mZxIndicatorsConfig.getSelectDrawable() : mZxIndicatorsConfig.getUnSelectDrawable());
            }
        }
        currentItem = selection;
    }

    public void setViewPage(ViewPager viewPage) {
        setIndicatorCounts(viewPage.getAdapter().getCount());
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int index) {
                setSelection(index);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public ZxIndicatorsConfig getZxIndicatorsConfig() {
        return mZxIndicatorsConfig;
    }
}
