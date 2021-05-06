package com.zee.popupWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zee.bean.IDismissListener;
import com.zee.dialog.BindViewAdapter;
import com.zee.log.ZLog;
import com.zee.utils.UIUtils;
import com.zee.utils.ZScreenUtils;

public class MyPopupWindow implements IDismissListener {
    private PopupWindow mPopupWindow;
    private BindViewAdapter mBindViewAdapter;
    private int layoutRes;
    //左右间隔
    private int mLeftOrRightMargin;
    //上下间隔值
    private int mTopOrBottomMargin;
    private boolean mOutsideTouchable = false;
    private float mBgAlpha = 0.5f;
    private int height = 0;

    public static MyPopupWindow init(BindViewAdapter bindViewAdapter) {
        MyPopupWindow myPopupWindow = newInstances(bindViewAdapter.getLayoutID());
        myPopupWindow.mBindViewAdapter = bindViewAdapter;
        return myPopupWindow;
    }

    @Deprecated
    public static MyPopupWindow init(@LayoutRes int resID) {
        return newInstances(resID);
    }

    @Deprecated
    public static MyPopupWindow init(@LayoutRes int resID, BindViewAdapter adapter) {
        MyPopupWindow myPopupWindow = newInstances(resID);
        myPopupWindow.setMyPopupWindowAdapter(adapter);
        return myPopupWindow;
    }

    @Deprecated
    public static MyPopupWindow newInstances(@LayoutRes int resID) {
        MyPopupWindow myPopupWindow = new MyPopupWindow();
        myPopupWindow.layoutRes = resID;
        return myPopupWindow;
    }

    @Deprecated
    public MyPopupWindow setMyPopupWindowAdapter(BindViewAdapter bindViewAdapter) {
        mBindViewAdapter = bindViewAdapter;
        return this;
    }

    public MyPopupWindow setOutsideTouchable(boolean touchable) {
        mOutsideTouchable = touchable;
        return this;
    }

    public MyPopupWindow setMargin(int leftOrRightDp, int topOrBottomDp) {
        mLeftOrRightMargin = UIUtils.dpToPx(leftOrRightDp);
        mTopOrBottomMargin = UIUtils.dpToPx(topOrBottomDp);
        return this;
    }

    public MyPopupWindow setBgAlpha(float bgAlpha) {
        mBgAlpha = bgAlpha;
        return this;
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    private void initData() {
        final Activity activity = UIUtils.getCurActivity();
        View popupView = activity.getLayoutInflater().inflate(layoutRes, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        height = popupView.getMeasuredHeight();
        ZLog.i(popupView.getMeasuredWidth() + "--->" + popupView.getMeasuredHeight());
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(mOutsideTouchable);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        backgroundAlpha(mBgAlpha);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                if (mBindViewAdapter != null) {
                    mBindViewAdapter.onDestroy();
                }
            }
        });
        if (mBindViewAdapter != null) {
            mBindViewAdapter.setBindView(this);
            mBindViewAdapter.setView(popupView);
        }
    }

    public void showAsDropDown(View anchor) {
        initData();
        mPopupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoffDp, int yoffDp) {
        initData();
        mPopupWindow.showAsDropDown(anchor, UIUtils.dpToPx(xoffDp), UIUtils.dpToPx(yoffDp));
    }

    public void showAsDropUp(View anchor, int xoffDp, int yoffDp) {
        initData();
        mPopupWindow.showAsDropDown(anchor, UIUtils.dpToPx(xoffDp), UIUtils.dpToPx(yoffDp) - height - anchor.getMeasuredHeight());
    }

    public void showAtLocation(View parent, int gravity, int xoffDp, int yoffDp) {
        initData();
        mPopupWindow.showAtLocation(parent, gravity, UIUtils.dpToPx(xoffDp), UIUtils.dpToPx(yoffDp));
    }

    private void backgroundAlpha(float bgAlpha) {
        Activity activity = UIUtils.getCurActivity();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 默认左边对齐，显示在下面
     *
     * @param anchorView
     */
    public void showAutoLocation(View anchorView) {
        showAutoLocation(anchorView, -1);
    }

    /**
     * 可以设置
     *
     * @param anchorView
     * @param gravity    可以试着 left|top 左对齐显示在上面 或者是right|top 右对齐显示在上面
     */
    public void showAutoLocation(View anchorView, int gravity) {
        showAutoLocation(anchorView, gravity, false);
    }

    public void showCenter(View anchorView) {
        showAutoLocation(anchorView, -1, true);
    }

    /**
     * 中间对齐显示
     *
     * @param anchorView
     * @param gravity    只有TOP和Bottom有作用，其他没有作用
     */
    public void showCenter(View anchorView, int gravity) {
        showAutoLocation(anchorView, gravity, true);
    }

    private void showAutoLocation(View anchorView, int gravity, boolean isCenter) {
        initData();
        int[] windowPos = calculatePopWindowPos(anchorView, gravity, isCenter);
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView, int gravity, boolean isCenter) {
        View contentView = mPopupWindow.getContentView();

        final int[] windowPos = new int[2];
        final int[] anchorLoc = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ZScreenUtils.getScreenHeight();
        final int screenWidth = ZScreenUtils.getScreenWidth();
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        //是否指定显示在上面
        boolean isMustShow = (gravity != -1 && (gravity & Gravity.TOP) == Gravity.TOP);
        if (isNeedShowUp || isMustShow) {
            windowPos[1] = anchorLoc[1] - windowHeight - mTopOrBottomMargin;
        } else {
            windowPos[1] = anchorLoc[1] + anchorHeight + mTopOrBottomMargin;
        }

        windowPos[0] = anchorLoc[0];
        if (windowPos[0] > screenWidth - windowWidth - mLeftOrRightMargin) {
            windowPos[0] = screenWidth - windowWidth - mLeftOrRightMargin;
        }
        if (isCenter) {
            windowPos[0] = windowPos[0] - windowWidth / 2 + anchorView.getMeasuredWidth() / 2;
        }
        if (windowPos[0] < mLeftOrRightMargin) {
            windowPos[0] = mLeftOrRightMargin;
        }
        if (gravity != -1) {
            if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
                windowPos[0] -= windowWidth;
                windowPos[0] += anchorView.getMeasuredWidth();
            }
        }
        return windowPos;
    }
}
