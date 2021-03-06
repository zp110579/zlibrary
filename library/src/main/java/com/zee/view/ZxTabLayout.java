package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zee.adapter.FragmentManagerAdapter;
import com.zee.adapter.ViewPageFragmentAdapter;
import com.zee.bean.ITitle;
import com.zee.libs.R;
import com.zee.listener.OnTabSelectListener;
import com.zee.log.ZLog;
import com.zee.utils.UnreadMsgUtils;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZxTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private ViewPager mViewPager;
    private ArrayList<String> mTitles = new ArrayList<String>();
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    /**
     * 用于实现滚动居中
     */
    private Rect mTabRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private static final int STYLE_NO_PADDING = 3;
    private static final int STYLE_ALL_EQUAL = 4;
    private int mIndicatorStyle = STYLE_NORMAL;

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private int mIndicatorGravity;

    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;
    private int mGap = 0;//间隔 如果tag自动大小的时候，设置tag之间间隔，

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextSize, mTextSelectSize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    private int mLastScrollX;
    private int mHeight;
    private boolean mSnapOnTabClick;

    //和Fragment关联
    FragmentManagerAdapter mFragmentManagerAdapter;
    private ViewIsCanClickListener mViewIsCanClickListener = new ViewIsCanClickListener() {
        @Override
        public boolean isCanClick(int index, View view) {
            return true;
        }
    };

    public ZxTabLayout(Context context) {
        this(context, null, 0);
    }

    public ZxTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);

        obtainAttributes(context, attrs);

        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxTabLayout);

        mIndicatorStyle = ta.getInt(R.styleable.ZxTabLayout_zv_indicator_style, STYLE_NORMAL);

        mIndicatorHeight = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_height, dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_width, dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_corner_radius, dp2px(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_margin_left, dp2px(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_margin_top, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_margin_right, dp2px(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.ZxTabLayout_zv_indicator_margin_bottom, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorGravity = ta.getInt(R.styleable.ZxTabLayout_zv_indicator_gravity, Gravity.BOTTOM);
//        mIndicatorWidthEqualTitle = ta.getBoolean(R.styleable.ZxTabLayout_zv_indicator_width_equal_title, false);

        mUnderlineColor = ta.getColor(R.styleable.ZxTabLayout_zv_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.ZxTabLayout_zv_underline_height, dp2px(0));
        mUnderlineGravity = ta.getInt(R.styleable.ZxTabLayout_zv_underline_gravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.ZxTabLayout_zv_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.ZxTabLayout_zv_divider_width, dp2px(0));
        mDividerPadding = ta.getDimension(R.styleable.ZxTabLayout_zv_divider_padding, dp2px(12));

        mTextSize = ta.getDimension(R.styleable.ZxTabLayout_zv_textSize, sp2px(14));
        if (ta.hasValue(R.styleable.ZxTabLayout_zv_textSelectSize)) {
            mTextSelectSize = ta.getDimension(R.styleable.ZxTabLayout_zv_textSelectSize, sp2px(14));
        } else {
            mTextSelectSize = mTextSize;
        }
        mTextSelectColor = ta.getColor(R.styleable.ZxTabLayout_zv_textSelectColor, Color.parseColor("#1E5FFE"));
        mIndicatorColor = ta.getColor(R.styleable.ZxTabLayout_zv_indicator_color, mIndicatorStyle == STYLE_BLOCK ? Color.parseColor("#4B6A87") : mTextSelectColor);


        mTextUnselectColor = ta.getColor(R.styleable.ZxTabLayout_zv_textColor, Color.parseColor("#1D1E1F"));
        mTextBold = ta.getInt(R.styleable.ZxTabLayout_zv_textBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.ZxTabLayout_zv_textAllCaps, false);

        mTabSpaceEqual = ta.getBoolean(R.styleable.ZxTabLayout_zv_tab_space_equal, false);
        mTabWidth = ta.getDimension(R.styleable.ZxTabLayout_zv_tab_width, dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.ZxTabLayout_zv_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(5));
        mGap = (int) ta.getDimension(R.styleable.ZxTabLayout_zv_gap, 0);
        //以“|”分割
        String title = ta.getString(R.styleable.ZxTabLayout_zv_title_text);
        if (!TextUtils.isEmpty(title)) {
            String tempTitle[] = title.split("\\|");
            Collections.addAll(mTitles, tempTitle);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 200);
        }

        ta.recycle();
    }

    public void setViewIsCanClickListener(ViewIsCanClickListener listener) {
        mViewIsCanClickListener = listener;
    }

    public void setFragmentManagerAdapter(FragmentManagerAdapter fragmentManagerAdapter) {
        mFragmentManagerAdapter = fragmentManagerAdapter;
    }

    public void setViewPageFragmentAdapter(ViewPageFragmentAdapter tabFragmentAdapter) {
        setViewPager(tabFragmentAdapter.getViewPager());
//        mFragmentManagerAdapter = tabFragmentAdapter;
    }

    /**
     * 关联ViewPager
     */
    public void setViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalArgumentException("ViewPager or ViewPager adapter can not be NULL !");
        }
        this.mViewPager = vp;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        setCurrentTab(0);
        notifyDataSetChanged();
    }

    public void setViewPager(ViewPager vp, List<ITitle> titles) {
        setViewPager(vp);
        setTitles(titles);
    }

    public void setTitles(String[] titles, OnTabSelectListener listener) {
        Collections.addAll(mTitles, titles);
        this.mListener = listener;
        notifyDataSetChanged();
    }

    public void setTitles(List<? extends ITitle> titles) {
        if (titles == null || titles.size() == 0) {
            throw new IllegalArgumentException("Titles can not be EMPTY !");
        }

        mTitles.clear();
        for (ITitle iTitle : titles) {
            mTitles.add(iTitle.getTitle());
        }
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        if (mViewPager != null) {
            this.mTabCount = ZListUtils.isEmpty(mTitles) ? mViewPager.getAdapter().getCount() : mTitles.size();
        } else {
            mTabCount = mTitles.size();
        }
        if (mTabCount < 1) {
            return;
        }
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.zv_layout_tab, null);
            String pageTitle = "";
            if (ZListUtils.isNoEmpty(mTitles)) {
                pageTitle = mTitles.get(i);
            } else if (mViewPager.getAdapter() != null) {
                pageTitle = mViewPager.getAdapter().getPageTitle(i).toString();
            }
//            CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(i) : mTitles.get(i);
            addTab(i, pageTitle, tabView);
        }

        updateTabStyles();
    }

    public void addNewTab(String title) {
        View tabView = View.inflate(mContext, R.layout.zv_layout_tab, null);
        if (ZListUtils.isNoEmpty(mTitles)) {
            mTitles.add(title);
        }

        CharSequence pageTitle = ZListUtils.isEmpty(mTitles) ? mViewPager.getAdapter().getPageTitle(mTabCount) : mTitles.get(mTabCount);
        addTab(mTabCount, pageTitle.toString(), tabView);
        this.mTabCount = ZListUtils.isEmpty(mTitles) ? mViewPager.getAdapter().getCount() : mTitles.size();

        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title, View tabView) {
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null) {
            if (title != null) {
                tv_tab_title.setText(title);
            }
        }

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabsContainer.indexOfChild(v);
                if (mViewIsCanClickListener.isCanClick(position, v)) {
                    if (position != -1) {
                        if (mCurrentTab != position) {
                            if (mViewPager != null) {
                                if (mSnapOnTabClick) {
                                    mViewPager.setCurrentItem(position, false);
                                } else {
                                    mViewPager.setCurrentItem(position);
                                }
                            }
                            if (mListener != null) {
                                mListener.onTabSelect(position);
                            }
                        } else {
                            if (mListener != null) {
                                mListener.onTabReselect(position);
                            }
                        }
                    }
                    if (mViewPager == null) {
                        updateTabSelection(position);
                    }
                    if (mFragmentManagerAdapter != null) {
                        try {
                            mFragmentManagerAdapter.selectIndex(position);
                            mFragmentManagerAdapter.onClick(position, v);
                        } catch (Exception e) {
                            ZLog.e(e);
                        }
                    }
                    mCurrentTab = position;
                    invalidate();
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ? new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        } else if (mGap > 0) {
            //设置间隔
            if (position < mTabCount) {
                lp_tab.setMargins(0, 0, mGap, 0);
            }
        }

        mTabsContainer.addView(tabView, position, lp_tab);
    }


    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
//            v.setPadding((int) mTabPadding, v.getPaddingTop(), (int) mTabPadding, v.getPaddingBottom());
            TextView tv_tab_title = (TextView) v.findViewById(R.id.tv_tab_title);
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, i == mCurrentTab ? mTextSelectSize : mTextSize);
                tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
                if (mTextAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (i == mCurrentTab && mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                }
                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position:当前View的位置
         * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
         */
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }

        int offset = (int) (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab).getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mTabsContainer.getChildAt(mCurrentTab).getLeft() + offset;

        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0);
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);

            if (tab_title != null) {
                tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
                tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, isSelect ? mTextSelectSize : mTextSize);

                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tab_title.getPaint().setFakeBoldText(isSelect);
                }
            }
        }

        if (mListener != null) {
            mListener.onTabSelect(position);
        }
    }

    private float margin;

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NO_PADDING) {
            TextView tab_title = (TextView) currentTabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextSize);
            float textWidth = mTextPaint.measureText(tab_title.getText().toString());
            margin = (right - left - textWidth) / 2;
        }

        if (this.mCurrentTab < mTabCount - 1) {
            View nextTabView = mTabsContainer.getChildAt(this.mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + mCurrentPositionOffset * (nextTabLeft - left);
            right = right + mCurrentPositionOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NO_PADDING) {
                TextView next_tab_title = (TextView) nextTabView.findViewById(R.id.tv_tab_title);
                mTextPaint.setTextSize(mTextSize);
                float nextTextWidth = mTextPaint.measureText(next_tab_title.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                margin = margin + mCurrentPositionOffset * (nextMargin - margin);
            }
        }

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NO_PADDING) {
            mIndicatorRect.left = (int) (left + margin - 1);
            mIndicatorRect.right = (int) (right - margin - 1);
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        if (mIndicatorStyle == STYLE_NO_PADDING) {
            mTabRect.left = (int) (left + mTabPadding);
            mTabRect.right = (int) (right - mTabPadding);
        }

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            if (this.mCurrentTab < mTabCount - 1) {
                View nextTab = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
            }

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line

        calcIndicatorRect();
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.setColor(mIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
            } else {

            }

            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2;
                }

                mIndicatorDrawable.setColor(mIndicatorColor);
                mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                        (int) mIndicatorMarginTop, (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                        (int) (mIndicatorMarginTop + mIndicatorHeight));
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        } else {
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor);

                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            height - (int) mIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            (int) mIndicatorMarginTop, paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            (int) mIndicatorHeight + (int) mIndicatorMarginTop);
                }
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab);

    }

    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab, smoothScroll);
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setTabPadding(float tabPadding) {
        this.mTabPadding = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        this.mUnderlineHeight = dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        this.mDividerWidth = dp2px(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        this.mDividerPadding = dp2px(dividerPadding);
        invalidate();
    }

    public void setTextSize(float textsize) {
        this.mTextSize = sp2px(textsize);
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public void setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
    }


    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    public float getTextsize() {
        return mTextSize;
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    //setter and getter

    // show MsgTipView
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        ZxTextView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return;
            }

            setMsgMargin(position, 6, 4);
            mInitSetMap.put(position, true);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        showMsg(position, 0);
    }

    /**
     * 隐藏未读消息
     */
    public void hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        ZxTextView tipView = (ZxTextView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
     */
    public void setMsgMargin(int position, float leftPadding, float bottomPadding) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        ZxTextView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextSize);
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();
            lp.leftMargin = mTabWidth >= 0 ? (int) (mTabWidth / 2 + textWidth / 2 + dp2px(leftPadding)) : (int) (mTabPadding + textWidth + dp2px(leftPadding));
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - dp2px(bottomPadding) : 0;
            tipView.setLayoutParams(lp);
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取ZxTextView对象从而各种设置
     */
    public ZxTextView getZxTextView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        ZxTextView tipView = (ZxTextView) tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}

