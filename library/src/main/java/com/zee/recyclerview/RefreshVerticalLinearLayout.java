package com.zee.recyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Administrator
 */
class RefreshVerticalLinearLayout extends LinearLayout implements IRefreshLinearLayout {
    private LinearLayout mContainer;
    private int mState = -1;

    /**
     * 提示下拉
     */
    public static final int STATE_HINT_PULL_DOWN = 0;
    /**
     * 提示松手
     */
    public static final int STATE_HINT_RELEASE = 1;
    /**
     * 刷新开始
     */
    public static final int STATE_REFRESH_START = 2;
    /**
     * 刷新结束
     */
    public static final int STATE_REFRESH_FINISH = 3;

    public int mMeasuredHeight;
    private IRecyclerViewRefreshView mDefaultRefreshView;

    /**
     * 是否可以下拉刷新
     */
    private boolean pullRefreshEnabled = false;

    private float mLastY = -1;
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    private static final float DRAG_RATE = 3f;

    /**
     * 刷新监听器
     */
    private RefreshListener mRefreshListener;
    private IRecyclerView mRecyclerView;

    public RefreshVerticalLinearLayout(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public RefreshVerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int customHeight = 0;

    private void initView() {
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(mDefaultRefreshView.getLayoutID(), null);
        customHeight = mDefaultRefreshView.getViewHeight();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);
        mDefaultRefreshView.initViews(this);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }


    public void setRecyclerView(IRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setDefaultRefreshView(IRecyclerViewRefreshView defaultRefreshView) {
        if (mContainer != null) {
            removeAllViews();
        }
        mDefaultRefreshView = defaultRefreshView;
        initView();
    }


    /**
     * 设置不能刷新
     *
     * @param enabled
     */
    public void setRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    /**
     * 是否可以下拉刷新
     *
     * @return
     */
    public boolean isRefreshEnabled() {
        return pullRefreshEnabled;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
        if (refreshListener != null) {
            setRefreshEnabled(true);
        } else {
            setRefreshEnabled(false);
        }
    }

    /**
     * 是否等待刷新
     */
    public boolean isWaitRefresh() {
        return mState < RefreshVerticalLinearLayout.STATE_REFRESH_START;
    }

    public boolean onMove(MotionEvent ev) {
        if (isRefreshEnabled()) {
            if (mLastY == -1) {
                mLastY = ev.getRawY();
            }
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float deltaY = ev.getRawY() - mLastY;
                    mLastY = ev.getRawY();
                    if (isOnTop() && isRefreshEnabled() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                        if (getVisibleHeight() < mMeasuredHeight) {
                            onMove(deltaY / 2f);
                        } else {
                            onMove(deltaY / DRAG_RATE);
                        }
                        if (getVisibleHeight() > 0 && getState() < RefreshVerticalLinearLayout.STATE_REFRESH_START) {
                            return false;
                        }
                    }
                    break;
                default:
                    mLastY = -1;
                    if (isOnTop() && isRefreshEnabled() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                        if (onReleaseHand()) {
                            startRefresh();
                        }
                    }
                    break;
            }
        }
        return true;
    }

    public void setAppbarState(AppBarStateChangeListener.State appbarState) {
        this.appbarState = appbarState;
    }

    private boolean isOnTop() {
        if (getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始刷新
     */
    public void startRefresh() {
        if (pullRefreshEnabled) {
            setState(STATE_REFRESH_START);
            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
        }
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }
        switch (state) {
            case STATE_HINT_PULL_DOWN:
                //提示下拉刷新
                mDefaultRefreshView.onHintPullDown(mState);
                break;
            case STATE_HINT_RELEASE:
                //提示松手
                mDefaultRefreshView.onHintReleaseHand();
                break;
            case STATE_REFRESH_START:
                //开始刷新
                mDefaultRefreshView.onRefreshStart();
                smoothScrollTo(mMeasuredHeight);
                if (mRecyclerView != null) {
                    //只要开始刷新，那就说明是从头来
                    mRecyclerView.setNoMore(false);
                }
                break;
            case STATE_REFRESH_FINISH:
                //刷新结束
                mDefaultRefreshView.onRefreshEnd();
                break;
            default:
        }
        mState = state;
    }

    public int getState() {
        return mState;
    }

    /**
     * 刷新结束
     */
    public void refreshFinish() {
        if (mState != STATE_HINT_PULL_DOWN) {
//            setState(STATE_REFRESH_FINISH);
            mDefaultRefreshView.onRefreshEnd();
            reset();
//            smoothScrollTo(0);
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, 200);
        }
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();

        if (height < customHeight) {
            mContainer.setTranslationY(-(customHeight - height) / 2);
        } else {
            mContainer.setTranslationY(0);
        }
        mContainer.setGravity(Gravity.CENTER);
        lp.height = height;
        mContainer.setLayoutParams(lp);
        if (mDefaultRefreshView != null) {
            mDefaultRefreshView.onHeight(height);
        }
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_HINT_RELEASE) {
                // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_HINT_RELEASE);
                } else {
                    setState(STATE_HINT_PULL_DOWN);
                }
            }
        }
    }

    /**
     * 松开手指
     *
     * @return
     */
    public boolean onReleaseHand() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {
            // not visible.
            isOnRefresh = false;
        }

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESH_START) {
            setState(STATE_REFRESH_START);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESH_START && height <= mMeasuredHeight) {
            //return;
        }
        if (mState != STATE_REFRESH_START) {
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESH_START) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_HINT_PULL_DOWN);
            }
        }, 500);
    }

    public void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void destroy() {
        if (mDefaultRefreshView != null) {
            mDefaultRefreshView.destroy();
        }
    }

    @Override
    public View getRefreshView() {
        return this;
    }
}