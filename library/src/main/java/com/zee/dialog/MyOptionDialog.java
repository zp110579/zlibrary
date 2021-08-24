package com.zee.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.zee.interf.IOptionItem;
import com.zee.listener.OnMyOptionSelectListener;
import com.zee.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zee on 2020/9/3.
 */
public class MyOptionDialog {
    private int mLeftAndRightMargin;//左右边距


    private int mShowNumber;  //item显示的数量
    private float mDimAmount = 0.3f;//灰度深浅
    private double mLineSpacingMultiplier = 2.5;
    private String mTitle = "";//标题
    private int mFontSize;//设置字体的大小
    private String mLeftText, mRightText; //左右的字体显示
    private int mLeftTextColor = -1, mRightTextColor = -1;//左右的字体颜色
    private OnMyOptionSelectListener mOnMyOptionSelectListener;
    private List<IOptionItem> mIOptionItemList = new ArrayList<>();

    public static MyOptionDialog newMyTimeDialog(OnMyOptionSelectListener onTimeSelectListener, String title) {
        return new MyOptionDialog(title).setOnTimeSelectListener(onTimeSelectListener);
    }

    public static MyOptionDialog newMyTimeDialog(OnMyOptionSelectListener onTimeSelectListener) {
        return new MyOptionDialog("").setOnTimeSelectListener(onTimeSelectListener);
    }

    private MyOptionDialog(String title) {
        this.mTitle = title;
    }

    private MyOptionDialog setOnTimeSelectListener(OnMyOptionSelectListener onTimeSelectListener) {
        this.mOnMyOptionSelectListener = onTimeSelectListener;
        return this;
    }

    public MyOptionDialog setMarginLeftAndRight(int margin) {
        this.mLeftAndRightMargin = margin;
        return this;
    }

    /**
     * 设置间隔
     *
     * @param lineSpacingMultiplier
     * @return
     */
    public MyOptionDialog setLineSpacingMultiplier(double lineSpacingMultiplier) {
        this.mLineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    /**
     * 设置左右字体的颜色
     *
     * @param leftTxt
     * @param rightTxt
     * @return
     */
    public MyOptionDialog setBtnText(String leftTxt, String rightTxt) {
        mLeftText = leftTxt;
        mRightText = rightTxt;
        return this;
    }

    /**
     * 设置取消和确定字的大小
     *
     * @param fontSize
     * @return
     */
    public MyOptionDialog setBtnFontSize(int fontSize) {
        mFontSize = fontSize;
        return this;
    }

    /**
     * 设置左右字体的颜色
     *
     * @return
     */
    public MyOptionDialog setBtnTextColor(int leftTextColor, int rightTextColor) {
        mLeftTextColor = leftTextColor;
        mRightTextColor = rightTextColor;
        return this;
    }

    /**
     * 设置显示的数量
     */
    public MyOptionDialog setShowItemCount(int count) {
        mShowNumber = count;
        return this;
    }

    /**
     * 设置背景透明度
     *
     * @param dimAmount
     */
    public MyOptionDialog setDimAmount(float dimAmount) {
        this.mDimAmount = dimAmount;
        return this;
    }

    /**
     * 需要显示的OptionItem
     *
     * @param IOptionItemList
     * @return
     */
    public MyOptionDialog setIOptionItemList(List<IOptionItem> IOptionItemList) {
        mIOptionItemList = IOptionItemList;
        return this;
    }

    public void show() {
        OptionsPickerBuilder optionsPickerBuilder = new OptionsPickerBuilder(UIUtils.getCurActivity(), mOnMyOptionSelectListener);
        if (mShowNumber > 1) {
            optionsPickerBuilder.setItemVisibleCount(mShowNumber);
        }

        optionsPickerBuilder.isAlphaGradient(true).isDialog(true);
        optionsPickerBuilder.setTitleText(mTitle);
        if (mLeftText != null) {
            optionsPickerBuilder.setCancelText(mLeftText);
        }
        if (mRightText != null) {
            optionsPickerBuilder.setSubmitText(mRightText);
        }
        if (mLeftTextColor != -1) {
            optionsPickerBuilder.setCancelColor(mLeftTextColor);
        }

        if (mRightTextColor != -1) {
            optionsPickerBuilder.setSubmitColor(mRightTextColor);
        }

        if (mFontSize != 0) {
            optionsPickerBuilder.setContentTextSize(mFontSize);
        }
        optionsPickerBuilder.setLineSpacingMultiplier((float) mLineSpacingMultiplier);//当前的高度的多少倍
        OptionsPickerView optionsPickerView = optionsPickerBuilder.build();

        Dialog mDialog = optionsPickerView.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            int value = UIUtils.dpToPx(mLeftAndRightMargin);
            params.leftMargin = value;
            params.rightMargin = value;
            optionsPickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(mDimAmount);
            }
        }
        mDialog.show();
    }
}
