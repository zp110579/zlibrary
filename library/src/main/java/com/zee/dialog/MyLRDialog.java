/*
 * 只处理左右弹出的Dialog
 */

package com.zee.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zee.bean.IDismissListener;
import com.zee.libs.R;
import com.zee.utils.UIUtils;
import com.zee.utils.ZEventBusUtils;
import com.zee.utils.ZScreenUtils;

import org.jetbrains.annotations.NotNull;

public class MyLRDialog extends DialogFragment implements IDismissListener {
    private static final String LEFTRIGHTMARGIN = "leftRightMargin";
    private static final String TOPBOTTOMMARGIN = "topBottomMargin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String GRAVITY = "gravity";
    private static final String CANCEL = "out_cancel";
    private static final String THEME = "theme";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";
    private static final String ADAPTER = "adapter";


    private int mLeftAndRightMargin;//左右边距
    private int mTopOrBottomMargin;//上下的边距
    private int width;//宽度
    private int height;//高度
    private float dimAmount = 0.6f;//灰度深浅
    private int gravity = Gravity.CENTER;//显示的位置
    private boolean outCancel = true;//是否点击外部取消
    @StyleRes
    protected int theme = R.style.myDialogStyle; // dialog主题
    private boolean isFullScreen = false;
    private boolean mIsHaveMargin = false;//全屏的情况下有设置左右间隔无效
    //只有显示在上，下2面才会有效果
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;

    private BindViewAdapter mBindViewAdapter;


    public static MyLRDialog initFromLeft(BindViewAdapter bindViewAdapter) {
        MyLRDialog myDialog = newInstance(Gravity.LEFT, bindViewAdapter.getLayoutID());
        myDialog.setAdapter(bindViewAdapter);
        bindViewAdapter.setBindView(myDialog);
        return myDialog;
    }

    public static MyLRDialog initFromRight(BindViewAdapter bindViewAdapter) {
        MyLRDialog myDialog = newInstance(Gravity.RIGHT, bindViewAdapter.getLayoutID());
        myDialog.setAdapter(bindViewAdapter);
        bindViewAdapter.setBindView(myDialog);
        return myDialog;
    }

    private MyLRDialog setAdapter(BindViewAdapter adapter) {
        mBindViewAdapter = adapter;
        mBindViewAdapter.setBindView(this);
        return this;
    }

    private static MyLRDialog newInstance(int type, int layoutID) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("layout", layoutID);
        MyLRDialog fragment = new MyLRDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, theme);
        Bundle arguments = getArguments();
        if (arguments != null) {
            gravity = arguments.getInt("type", 0);
            layoutId = arguments.getInt("layout", 0);
        }

        //恢复保存的数据
        if (savedInstanceState != null) {
            mLeftAndRightMargin = savedInstanceState.getInt(LEFTRIGHTMARGIN);
            mTopOrBottomMargin = savedInstanceState.getInt(TOPBOTTOMMARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            gravity = savedInstanceState.getInt(GRAVITY);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            theme = savedInstanceState.getInt(THEME);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
            mBindViewAdapter = (BindViewAdapter) savedInstanceState.getParcelable(ADAPTER);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        check();
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        check();
        mBindViewAdapter.setView(view);
    }


    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LEFTRIGHTMARGIN, mLeftAndRightMargin);
        outState.putInt(TOPBOTTOMMARGIN, mTopOrBottomMargin);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putInt(GRAVITY, gravity);
        outState.putBoolean(CANCEL, outCancel);
        outState.putInt(THEME, theme);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
        try {
            outState.putParcelable(ADAPTER, mBindViewAdapter);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            if (gravity != 0) {
                lp.gravity = gravity;
            }
            switch (gravity) {
                case Gravity.LEFT:
                    if (animStyle == 0) {
                        animStyle = R.style.zxDialogStyle_leftAnimation;
                    }
                    break;

                case Gravity.RIGHT:
                    if (animStyle == 0) {
                        animStyle = R.style.zxDialogStyle_rightAnimation;
                    }
                    break;
                default:
                    break;

            }

            //设置dialog宽度
            if (isFullScreen) {
                lp.width = ZScreenUtils.getScreenWidth();
                if (mIsHaveMargin) {
                    lp.width = ZScreenUtils.getScreenWidth() - UIUtils.dpToPx(mLeftAndRightMargin);
                }
                lp.height = ZScreenUtils.getScreenHeight();
                lp.y = 0;
            } else {
                int tempW = UIUtils.dpToPx(width);
                if (tempW == 0) {
                    lp.width = ZScreenUtils.getScreenWidth() - 2 * UIUtils.dpToPx(mLeftAndRightMargin);
                } else if (tempW == -1) {
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                } else {
                    lp.width = tempW;
                }
                //设置dialog高度
                int tempH = UIUtils.dpToPx(height);
                if (tempH == 0) {
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                } else {
                    lp.height = tempH;
                }
//                if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
//                    lp.y = UIUtils.dpToPx(mTopOrBottomMargin);
//                }
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
    }

    public MyLRDialog setMargin(int margin) {
        this.mLeftAndRightMargin = margin;
        return this;
    }

    public MyLRDialog setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public MyLRDialog setFullScreen(boolean isNoHaveMargin) {
        isFullScreen = true;
        mIsHaveMargin = !isNoHaveMargin;
        return this;
    }

    public MyLRDialog setFullScreen() {
        isFullScreen = true;
        return this;
    }

    public MyLRDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public MyLRDialog setAnimStyle(int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public MyLRDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public void show(@NotNull FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        if (this.isAdded()) {
            ft.remove(this).commit();
        }
        ft.add(this, String.valueOf(System.currentTimeMillis()));
        ft.commitAllowingStateLoss();
    }


    /**
     * 自动获得当前的Acitivity并显示出来
     *
     * @return
     */
    public MyLRDialog show() {
        UIUtils.showDialog(this);
        return this;
    }

    private void check() {
        if (mBindViewAdapter == null) {
            Log.e("MyDialog", "BindAdapter is null");
            throw new NullPointerException("BindAdapter is null");
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        ZEventBusUtils.unregister(this);
        super.onDestroy();
        if (mBindViewAdapter != null) {
            mBindViewAdapter.onDestroy();
        }
    }
}
