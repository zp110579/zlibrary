package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

//import com.nineoldandroids.view.ViewHelper;
import com.zee.adapter.ZxBannerAdapter;
import com.zee.bean.ZxBannerViewConfig;
import com.zee.libs.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Administrator
 * 003
 */
@SuppressWarnings(value = {"unchecked", "deprecation"})
public class ZxBannerView extends RelativeLayout {
    private ViewPager mViewPager;

    private RelativeLayout mIndicatorsLayout;
    //记录原始数据
    private List mOldList = new ArrayList<Object>();
    private List mBannerDataList = new ArrayList<Object>();
    private int currentPosition;
    private boolean isLooping;
    private boolean isCanLoop;
    private int currentItem;

    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private ViewGroup.MarginLayoutParams layoutParams;
    private Context context;
    private View selectView;
    private ZxBannerAdapter mBannerAdapter;
    private ArrayList<ImageView> indicatorViews = new ArrayList<>();
    private HashMap<Integer, View> bannerHashMap = new HashMap<>();
    private OnZxBannerChangeListener mOnZxBannerChangeListener;
    private ZxBannerViewConfig mZxBannerViewConfig;

    public ZxBannerView(Context context) {
        this(context, null);
        init(context, null);
    }

    public ZxBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public ZxBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @SuppressWarnings("unchecked")
    private void init(Context context, AttributeSet attrs) {
        int layoutRes = R.layout.zv_003layout_cusmton_banner_view;
        this.context = context;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZxBannerView);
            mZxBannerViewConfig = new ZxBannerViewConfig(typedArray, context, 0);

            layoutRes = typedArray.getResourceId(R.styleable.ZxBannerView_zv_layout, R.layout.zv_003layout_cusmton_banner_view);
            typedArray.recycle();
        } else {
            mZxBannerViewConfig = new ZxBannerViewConfig();
        }

        View view = LayoutInflater.from(getContext()).inflate(layoutRes, this, true);
        if (layoutRes != R.layout.zv_003layout_cusmton_banner_view) {
            mViewPager = (ViewPager) view.findViewWithTag(getResources().getString(R.string.zee_str_viewPage));
            mIndicatorsLayout = view.findViewWithTag(getResources().getString(R.string.zee_str_bannerView_indicator));
        } else {
            mViewPager = (ViewPager) view.findViewById(R.id.banner_viewPage);
            mIndicatorsLayout = view.findViewById(R.id.banner_dot);
            layoutParams = (MarginLayoutParams) mIndicatorsLayout.getLayoutParams();
            layoutParams.bottomMargin = mZxBannerViewConfig.getIndicatorsMarginBottom();
            setIndicatorGravity();
        }
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        if (mOldList.size() == 0) {
            setVisibility(GONE);
        } else if (mOldList.size() == 1) {
            mBannerDataList.add(mOldList.get(0));
            setVisibility(VISIBLE);
        } else if (mOldList.size() > 1) {
            createData();
            setVisibility(VISIBLE);
        }
//        if (mBannerAdapter != null) {
//            mBannerAdapter.setList(mBannerDataList);
//        }
    }

    private void setIndicatorGravity() {
        switch (mZxBannerViewConfig.getGravity()) {
            case LEFT:
                mIndicatorsLayout.setGravity(Gravity.START);
                layoutParams.leftMargin = mZxBannerViewConfig.getIndicatorsMarginLeft();
                layoutParams.rightMargin = 0;
                break;
            case RIGHT:
                layoutParams.rightMargin = mZxBannerViewConfig.getIndicatorsMarginRight();
                layoutParams.leftMargin = 0;
                mIndicatorsLayout.setGravity(Gravity.END);
                break;
            default:
                mIndicatorsLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void createData() {
        if (isCanLoop) {
            currentPosition = 1;
            for (int i = 0; i < mOldList.size() + 2; i++) {
                if (i == 0) {
                    int index = mOldList.size() - 1;
                    Object object = mOldList.get(index);
                    mBannerDataList.add(object);
                } else if (i == mOldList.size() + 1) {
                    Object object = mOldList.get(0);
                    mBannerDataList.add(object);
                } else {
                    mBannerDataList.add(mOldList.get(i - 1));
                }
            }
        } else {
            mBannerDataList.addAll(mOldList);
        }
    }

    private void setTouchListener() {
        if (mZxBannerViewConfig.getIntervalTime() < 1 || mOldList.size() < 2) {
            return;
        }

        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isLooping = true;
                        stopLoop();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isLooping = false;
                        startLoop();
                    default:
                        break;
                }
                return false;
            }

        });
    }

    public void setCurrentPosition(int position){
        this.currentItem=position;
        mViewPager.setCurrentItem(currentItem);

    }
    private void startLoop() {
        if (!isLooping && mViewPager != null) {
            removeCallbacks(mRunnable);
            postDelayed(mRunnable, mZxBannerViewConfig.getIntervalTime());
            isLooping = true;
        }
    }

    private void stopLoop() {
        if (isLooping && mViewPager != null) {
            removeCallbacks(mRunnable);
            isLooping = false;
        }
    }

    public void onStopLoop() {
        if (mZxBannerViewConfig.getIntervalTime() > 0) {
            stopLoop();
        }
    }

    public void onStartLoop() {
        if (mZxBannerViewConfig.getIntervalTime() > 0) {
            startLoop();
        }
    }

    private void setViewPager() {
        BannerViewPagerAdapter adapter = new BannerViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        setScrollSpeed();
        setPageChangeListener();

        setTouchListener();
        if (mZxBannerViewConfig.isShowIndicator() && mOldList.size() > 1) {
            mIndicatorsLayout.setVisibility(VISIBLE);
        } else {
            mIndicatorsLayout.setVisibility(GONE);
        }
        mViewPager.setCurrentItem(currentPosition);
    }

    @SuppressWarnings("unchecked")
    public void setBannerAdapter(ZxBannerAdapter zxBannerAdapter) {
        this.mBannerAdapter = zxBannerAdapter;
        mOldList.clear();
        mBannerDataList.clear();
        bannerHashMap.clear();
        mOldList.addAll(mBannerAdapter.getList());
        isCanLoop = true;
        createIndicators(mOldList.size());
        if (mOldList.size() < 2) {
            mIndicatorsLayout.setVisibility(GONE);
        } else {
            mIndicatorsLayout.setVisibility(VISIBLE);
        }
        if (mZxBannerViewConfig.getIntervalTime() > 0 && mOldList.size() > 1) {
            startLoop();
        }
        initData();
        setViewPager();
    }

    /**
     * 返回指示器
     *
     * @return
     */
    public RelativeLayout getIndicatorsLayout() {
        return mIndicatorsLayout;
    }

    private void setPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                boolean istrue = false;
                if (position == 0 && isCanLoop) {
                    position = mOldList.size();
                    istrue = true;
                    positionOffset = 0;
                }
                boolean isTrue = position < mOldList.size() && position > 0 || istrue || !isCanLoop;
                if (isTrue) {
                    currentItem = position;
                    if (isCanLoop) {
                        currentItem = position - 1;
                    }

                    if (mZxBannerViewConfig.isAnimal()) {
                        float translationX = (mZxBannerViewConfig.getIndicatorsWidth() + mZxBannerViewConfig.getIndicatorsGap()) * (currentItem + positionOffset);
                        selectView.setTranslationX(translationX);
                    }
                    if (mOnZxBannerChangeListener != null) {
                        mOnZxBannerChangeListener.onPageScrolled(currentItem, positionOffset, positionOffsetPixels);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
                int temp = position;
                if (isCanLoop) {
                    temp = position - 1;
                    if (temp < 0) {
                        temp = mOldList.size() - 1;
                    }
                }
                if (!mZxBannerViewConfig.isAnimal()) {
                    for (int i = 0; i < indicatorViews.size(); i++) {
                        indicatorViews.get(i).setImageDrawable(i == (temp) % mOldList.size() ? mZxBannerViewConfig.getSelectDrawable() : mZxBannerViewConfig.getUnSelectDrawable());
                    }
                }

                if (isCanLoop) {
                    temp = position - 1;
                }

                if (mOnZxBannerChangeListener != null && temp < mOldList.size()) {
                    mOnZxBannerChangeListener.onIndicatorsSelected(temp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(currentPosition, false);
                }
            }
        });
    }

    private void pageSelected(int position) {
        if (isCanLoop) {
            if (position == 0) {
                currentPosition = mOldList.size();
            } else if (position == mOldList.size() + 1) {
                currentPosition = 1;
            } else {
                currentPosition = position;
            }
        } else {
            currentPosition = position;
        }
    }


    @SuppressWarnings("all")
    private void createIndicators(int count) {
        if (count <= 0) {
            return;
        }

        indicatorViews.clear();
        mIndicatorsLayout.removeAllViews();

        LinearLayout ll_unselect_views = new LinearLayout(context);
        mIndicatorsLayout.addView(ll_unselect_views);

        for (int i = 0; i < count; i++) {
            ImageView iv = new ImageView(context);
            iv.setImageDrawable(mZxBannerViewConfig.isAnimal() ? mZxBannerViewConfig.getUnSelectDrawable() : (currentItem == i ? mZxBannerViewConfig.getSelectDrawable() : mZxBannerViewConfig.getUnSelectDrawable()));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mZxBannerViewConfig.getIndicatorsWidth(), mZxBannerViewConfig.getIndicatorsHeight());
            lp.leftMargin = i == 0 ? 0 : mZxBannerViewConfig.getIndicatorsGap();
            ll_unselect_views.addView(iv, lp);
            indicatorViews.add(iv);
        }

        if (mZxBannerViewConfig.isAnimal()) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mZxBannerViewConfig.getIndicatorsWidth(), mZxBannerViewConfig.getIndicatorsHeight());

            lp.leftMargin = (mZxBannerViewConfig.getIndicatorsWidth() + mZxBannerViewConfig.getIndicatorsGap()) * currentItem;
            selectView = new View(context);
            selectView.setBackgroundDrawable(mZxBannerViewConfig.getSelectDrawable());
            mIndicatorsLayout.addView(selectView, lp);
        }
    }

    public ZxBannerViewConfig getZxBannerViewConfig() {
        return mZxBannerViewConfig;
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getChildCount() > 1) {
                postDelayed(this, mZxBannerViewConfig.getIntervalTime());
                currentPosition++;
                mViewPager.setCurrentItem(currentPosition, true);
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void setScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
            ZxBannerViewScroller myScroller = new ZxBannerViewScroller(getContext(), interpolator, 450);
            mScroller.set(mViewPager, myScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class BannerViewPagerAdapter extends PagerAdapter {

        public BannerViewPagerAdapter() {
        }

        @Override
        public int getCount() {
            return mBannerDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View view = getView(position, container);
            container.addView(view);
            return view;
        }

        private View getView(final int position, ViewGroup container) {
            View view = bannerHashMap.get(position);
            if (view != null) {
                return view;
            }
            if (mBannerDataList != null && mBannerDataList.size() > 0) {
                if (isCanLoop) {
                    int size = mBannerDataList.size();
                    if (mBannerDataList.size() > 1) {
                        size = mBannerDataList.size() - 2;
                    }

                    if (position == 0) {
                        view = mBannerAdapter.createViews(container.getContext(), position, size - 1);
                    } else if (position == mBannerDataList.size() - 1) {
                        view = mBannerAdapter.createViews(container.getContext(), position, 0);
                    } else {
                        view = mBannerAdapter.createViews(container.getContext(), position, position - 1);
                    }
                } else {
                    view = mBannerAdapter.createViews(container.getContext(), position, position);
                }
            }
            bannerHashMap.put(position, view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }
    }

    private int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private class ZxBannerViewScroller extends Scroller {
        private int scrollSpeed = 450;

        public ZxBannerViewScroller(Context context, Interpolator interpolator, int scrollSpeed) {
            super(context, interpolator);
            this.scrollSpeed = scrollSpeed;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.scrollSpeed);
        }
    }

    public void setOnBannerChangeListener(OnZxBannerChangeListener mOnZxBannerChangeListener) {
        this.mOnZxBannerChangeListener = mOnZxBannerChangeListener;
    }

    public interface OnZxBannerChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onIndicatorsSelected(int position);
    }
}
