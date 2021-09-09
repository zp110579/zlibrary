package com.zee.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.zee.bean.ITitle;
import com.zee.libs.R;
import com.zee.listener.OnTabSelectListener;
import com.zee.manager.FragmentChangeManager;
import com.zee.utils.UnreadMsgUtils;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;

/**
 * @author Administrator
 */
public class ZxSolidItemTabLayout extends ZxLinearLayout implements ValueAnimator.AnimatorUpdateListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    private String[] mTitles;
    //private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private GradientDrawable mRectDrawable = new GradientDrawable();

    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private float mIndicatorMargin;

    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextsize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    private int mBarColor;
    private int mBorderColor;
    private float mBorderWidth;

    private int mHeight;
    private ViewPager mViewPager;
    private int mGap = 0;
    /**
     * anim
     */
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(0.8f);

    private FragmentChangeManager mFragmentChangeManager;
    private float[] mRadiusArr = new float[8];

    public ZxSolidItemTabLayout(Context context) {
        this(context, null, 0);
    }

    public ZxSolidItemTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxSolidItemTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);
//        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口

        this.mContext = context;
        obtainAttributes(context, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), mLastP, mCurrentP);
        mValueAnimator.addUpdateListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZxSolidItemTabLayout);

        mIndicatorColor = ta.getColor(R.styleable.ZxSolidItemTabLayout_zv_indicator_color, Color.parseColor("#222831"));
        mIndicatorHeight = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_height, -1);
        mIndicatorCornerRadius = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_corner_radius, -1);
        mIndicatorMargin = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_margin, dp2px(0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_margin_left, dp2px(0) + mIndicatorMargin);
        mIndicatorMarginTop = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_margin_top, mIndicatorMargin);
        mIndicatorMarginRight = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_margin_right, dp2px(0) + mIndicatorMargin);
        mIndicatorMarginBottom = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_indicator_margin_bottom, mIndicatorMargin);
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.ZxSolidItemTabLayout_zv_indicator_animal, false);
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.ZxSolidItemTabLayout_zv_indicator_bounce_enable, true);
        mIndicatorAnimDuration = ta.getInt(R.styleable.ZxSolidItemTabLayout_zv_indicator_anim_duration, -1);

//        mDividerColor = ta.getColor(R.styleable.ZxSolidItemTabLayout_zv_divider_color, mIndicatorColor);
//        mDividerWidth = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_divider_width, dp2px(1));
//        mDividerPadding = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_divider_padding, 0);

        mTextsize = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_textSize, sp2px(13f));
        mTextSelectColor = ta.getColor(R.styleable.ZxSolidItemTabLayout_zv_textSelectColor, Color.parseColor("#ffffff"));
        mTextUnselectColor = ta.getColor(R.styleable.ZxSolidItemTabLayout_zv_textColor, mIndicatorColor);
        mTextBold = ta.getInt(R.styleable.ZxSolidItemTabLayout_zv_textBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.ZxSolidItemTabLayout_zv_textAllCaps, false);

        mTabSpaceEqual = ta.getBoolean(R.styleable.ZxSolidItemTabLayout_zv_tab_space_equal, false);
        mTabWidth = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_tab_width, dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(10));

//        mBarColor = ta.getColor(R.styleable.ZxSolidItemTabLayout_zv_bar_color, Color.TRANSPARENT);
        mBorderColor = ta.getColor(R.styleable.ZxSolidItemTabLayout_zv_borderColor, mIndicatorColor);
        mBorderWidth = ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_borderWidth, dp2px(1));
        String title = ta.getString(R.styleable.ZxSolidItemTabLayout_zv_title_text);
        mGap = (int) ta.getDimension(R.styleable.ZxSolidItemTabLayout_zv_gap, 0);
        if (!TextUtils.isEmpty(title)) {
            mTitles = title.split("\\|");
        }

        ta.recycle();
    }

    public void setTabData(String[] titles) {
        if (titles == null || titles.length == 0) {
            throw new IllegalArgumentException("Titles can not be NULL or EMPTY !");
        }

        this.mTitles = titles;

        notifyDataSetChanged();
    }

    public void setTitles(ArrayList<? extends ITitle> titles) {
        if (titles != null && titles.size() != 0) {
            mTitles = new String[titles.size()];
            for (int i = 0; i < titles.size(); i++) {
                mTitles[i] = titles.get(i).getTitle();
            }

            notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("Titles can not be NULL or EMPTY !");
        }

    }


    /**
     * 关联数据支持同时切换fragments
     */
    public void setTabData(String[] titles, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        setTabData(titles);
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        removeAllViews();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.zv_layout_tab_segment, null);
            tabView.setTag(i);
            addTab(i, tabView);
        }

        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, View tabView) {
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);

        String pageTitle = "";
        if (ZListUtils.isNoEmpty(mTitles)) {
            pageTitle = mTitles[position];
        } else if (mViewPager.getAdapter() != null) {
            pageTitle = mViewPager.getAdapter().getPageTitle(position).toString();
        }

        tv_tab_title.setText(pageTitle);

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if (mCurrentTab != position) {
                    setCurrentTab(position);
                    if (mListener != null) {
                        mListener.onTabSelect(position);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onTabReselect(position);
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LayoutParams lp_tab = mTabSpaceEqual ?
                new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }
        if (mGap > 0) {
            if (position < mTabCount - 1) {
                lp_tab.setMargins(0, 0, mGap, 0);
            }
        }
        addView(tabView, position, lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
            tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);
//            tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            if (mTextAllCaps) {
                tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
            }

            if (mTextBold == TEXT_BOLD_BOTH) {
                tv_tab_title.getPaint().setFakeBoldText(true);
            } else if (mTextBold == TEXT_BOLD_NONE) {
                tv_tab_title.getPaint().setFakeBoldText(false);
            }
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tab_title.getPaint().setFakeBoldText(isSelect);
            }
        }
    }

    private void calcOffset() {
        final View currentTabView = getChildAt(this.mCurrentTab);
        mCurrentP.left = currentTabView.getLeft();
        mCurrentP.right = currentTabView.getRight();

        final View lastTabView = getChildAt(this.mLastTab);
        mLastP.left = lastTabView.getLeft();
        mLastP.right = lastTabView.getRight();

//        Log.d("AAA", "mLastP--->" + mLastP.left + "&" + mLastP.right);
//        Log.d("AAA", "mCurrentP--->" + mCurrentP.left + "&" + mCurrentP.right);
        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate();
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP);
            if (mIndicatorBounceEnable) {
                mValueAnimator.setInterpolator(mInterpolator);
            }

            if (mIndicatorAnimDuration < 0) {
                mIndicatorAnimDuration = mIndicatorBounceEnable ? 500 : 250;
            }
            mValueAnimator.setDuration(mIndicatorAnimDuration);
            mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;

        if (!mIndicatorAnimEnable) {
            if (mCurrentTab == 0) {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                mRadiusArr[0] = mIndicatorCornerRadius;
                mRadiusArr[1] = mIndicatorCornerRadius;
                mRadiusArr[2] = 0;
                mRadiusArr[3] = 0;
                mRadiusArr[4] = 0;
                mRadiusArr[5] = 0;
                mRadiusArr[6] = mIndicatorCornerRadius;
                mRadiusArr[7] = mIndicatorCornerRadius;
            } else if (mCurrentTab == mTabCount - 1) {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                mRadiusArr[0] = 0;
                mRadiusArr[1] = 0;
                mRadiusArr[2] = mIndicatorCornerRadius;
                mRadiusArr[3] = mIndicatorCornerRadius;
                mRadiusArr[4] = mIndicatorCornerRadius;
                mRadiusArr[5] = mIndicatorCornerRadius;
                mRadiusArr[6] = 0;
                mRadiusArr[7] = 0;
            } else {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                for (int i = 0; i < mRadiusArr.length; i++) {
                    mRadiusArr[i] = 0;
                }

            }
        } else {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
            for (int i = 0; i < mRadiusArr.length; i++) {
                mRadiusArr[i] = mIndicatorCornerRadius;
            }
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;
        invalidate();
    }

    private boolean mIsFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();

        if (mIndicatorHeight < 0) {
            mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
        }

        if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
            mIndicatorCornerRadius = mIndicatorHeight / 2;
        }

        //draw rect


        // draw divider
//        if (!mIndicatorAnimEnable && mDividerWidth > 0) {
//            mDividerPaint.setStrokeWidth(mDividerWidth);
//            mDividerPaint.setColor(mDividerColor);
//            for (int i = 0; i < mTabCount - 1; i++) {
//                View tab = getChildAt(i);
//                canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), height - mDividerPadding, mDividerPaint);
//            }
//        }


        //draw indicator line
        if (mIndicatorAnimEnable) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }

        mIndicatorDrawable.setColor(mIndicatorColor);
        mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
        mIndicatorDrawable.setBounds((int) mIndicatorMarginLeft + mIndicatorRect.left,
                (int) mIndicatorMarginTop, (int) (mIndicatorRect.right - mIndicatorMarginRight),
                (int) (mIndicatorMarginTop + mIndicatorHeight));
//        mIndicatorDrawable.setCornerRadii(mRadiusArr);
        mIndicatorDrawable.draw(canvas);

        //描边
        mRectDrawable.setStroke((int) mBorderWidth, mBorderColor);
        mRectDrawable.setCornerRadius(mIndicatorCornerRadius);
        mRectDrawable.setBounds((int) mIndicatorMarginLeft + mIndicatorRect.left,
                (int) mIndicatorMarginTop, (int) (mIndicatorRect.right - mIndicatorMarginRight),
                (int) (mIndicatorMarginTop + mIndicatorHeight));
        mRectDrawable.draw(canvas);
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        mLastTab = this.mCurrentTab;
        this.mCurrentTab = currentTab;
        updateTabSelection(currentTab);
        if (mFragmentChangeManager != null) {
            mFragmentChangeManager.setFragments(currentTab);
        }
        if (mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
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

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
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

    public void setIndicatorAnimDuration(long indicatorAnimDuration) {
        this.mIndicatorAnimDuration = indicatorAnimDuration;
    }

    public void setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        this.mIndicatorAnimEnable = indicatorAnimEnable;
    }

    public void setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        this.mIndicatorBounceEnable = indicatorBounceEnable;
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

    public void setTextsize(float textsize) {
        this.mTextsize = sp2px(textsize);
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

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
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

    public long getIndicatorAnimDuration() {
        return mIndicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return mIndicatorBounceEnable;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getIndicatorDividerPadding() {
        return mDividerPadding;
    }

    public float getTextsize() {
        return mTextsize;
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
        View tabView = getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    private void showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = getChildAt(position);
        ZxTextView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return;
            }

            setMsgMargin(position, 2, 2);

            mInitSetMap.put(position, true);
        }
    }

    /**
     * @param position     显示数量的tab位置
     * @param number       显示的数量
     * @param isShowNumber 是否显示数量，不显示数量，那就只显示红点
     */
    public void showNumber(int position, int number, boolean isShowNumber) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        if (number < 1) {
            hideDot(position);
        } else {
            showMsg(position, number);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    private void showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        showMsg(position, 0);
    }

    private void hideDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = getChildAt(position);
        ZxTextView tipView = (ZxTextView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     */
    public void setMsgMargin(int position, float leftPadding, float bottomPadding) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = getChildAt(position);
        ZxTextView tipView = (ZxTextView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextsize);
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();

            lp.leftMargin = dp2px(leftPadding);
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - dp2px(bottomPadding) : dp2px(bottomPadding);

            tipView.setLayoutParams(lp);
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    public ZxTextView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = getChildAt(position);
        ZxTextView tipView = (ZxTextView) tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }


    public void setViewPager(final ViewPager viewPage) {
        setViewPager(viewPage, null);
    }

    public void setViewPager(final ViewPager viewPage, String[] titles) {
        mViewPager = viewPage;
        if (titles == null) {
            PagerAdapter adapter = viewPage.getAdapter();
            mTabCount = ZListUtils.isEmpty(mTitles) ? mViewPager.getAdapter().getCount() : mTitles.length;
            if (ZListUtils.isEmpty(mTitles)) {
                if (mTabCount > 0 && !TextUtils.isEmpty(adapter.getPageTitle(0))) {
                    mTitles = new String[mTabCount];
                    for (int i = 0; i < mTabCount; i++) {
                        String title = adapter.getPageTitle(i).toString();
                        if (!TextUtils.isEmpty(title)) {
                            mTitles[i] = title;
                        }
                    }
                }
            }
        } else {
            mTitles = titles;
        }

        this.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPage.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        viewPage.removeOnPageChangeListener(this);
        viewPage.addOnPageChangeListener(this);

        notifyDataSetChanged();
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
            if (mCurrentTab != 0 && getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
            }
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class IndicatorPoint {
        public float left;
        public float right;
    }

    private IndicatorPoint mCurrentP = new IndicatorPoint();
    private IndicatorPoint mLastP = new IndicatorPoint();

    class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        @Override
        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
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
