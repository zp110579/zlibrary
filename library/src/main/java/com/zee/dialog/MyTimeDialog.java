package com.zee.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zee.listener.OnMyTimeSelectListener;
import com.zee.utils.UIUtils;

import java.util.Calendar;

public class MyTimeDialog {
    private OnTimeSelectListener mOnTimeSelectListener;
    private int mLeftAndRightMargin;//左右边距

    private Calendar mStartCalendar, mEndCalendar, selectCalendar;//开始时间,结束时间，默认选择的时间

    private int mShowNumber;  //item显示的数量
    private float mDimAmount = 0.3f;//灰度深浅
    private double mLineSpacingMultiplier = 2.5;
    private String mTitle = "";//标题
    private int mFontSize;//设置字体的大小
    private String mLeftText, mRightText; //左右的字体显示
    private int mLeftTextColor = -1, mRightTextColor = -1;//左右的字体颜色

    private boolean[] mShowType = new boolean[]{true, true, true, false, false, false};//显示类型，默认显示： 年月日

    public static MyTimeDialog newMyTimeDialog(OnMyTimeSelectListener onTimeSelectListener, String title) {
        return new MyTimeDialog(title).setOnTimeSelectListener(onTimeSelectListener);
    }

    public static MyTimeDialog newMyTimeDialog(OnMyTimeSelectListener onTimeSelectListener) {
        return new MyTimeDialog("").setOnTimeSelectListener(onTimeSelectListener);
    }

    private MyTimeDialog(String title) {
        this.mTitle = title;
    }

    private MyTimeDialog setOnTimeSelectListener(OnTimeSelectListener onTimeSelectListener) {
        this.mOnTimeSelectListener = onTimeSelectListener;
        return this;
    }

    public MyTimeDialog setMarginLeftAndRight(int margin) {
        this.mLeftAndRightMargin = margin;
        return this;
    }

    /**
     * 设置开始时间，结束时间到当前
     *
     * @param startDate
     * @return
     */
    public MyTimeDialog setStartTime(Calendar startDate) {
        mStartCalendar = startDate;
        mEndCalendar = Calendar.getInstance();
        return this;
    }

    /**
     * Calendar startDate = Calendar.getInstance(); //系统当前时间
     * startDate.set(2014, 1, 23);
     * Calendar endDate = Calendar.getInstance();
     * endDate.set(2027, 2, 28);
     */
    public MyTimeDialog setTimeRang(Calendar startDate, Calendar endDate) {
        mStartCalendar = startDate;
        mEndCalendar = endDate;
        return this;
    }

    /**
     * 设置间隔
     *
     * @param lineSpacingMultiplier
     * @return
     */
    public MyTimeDialog setLineSpacingMultiplier(double lineSpacingMultiplier) {
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
    public MyTimeDialog setBtnText(String leftTxt, String rightTxt) {
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
    public MyTimeDialog setBtnFontSize(int fontSize) {
        mFontSize = fontSize;
        return this;
    }

    /**
     * 设置左右字体的颜色
     *
     * @return
     */
    public MyTimeDialog setBtnTextColor(int leftTextColor, int rightTextColor) {
        mLeftTextColor = leftTextColor;
        mRightTextColor = rightTextColor;
        return this;
    }

    /**
     * 设置显示的数量
     */
    public MyTimeDialog setShowItemCount(int count) {
        mShowNumber = count;
        return this;
    }

    /**
     * 设置背景透明度
     *
     * @param dimAmount
     */
    public MyTimeDialog setDimAmount(float dimAmount) {
        this.mDimAmount = dimAmount;
        return this;
    }

    public MyTimeDialog setShowYear(boolean showYear) {
        mShowType[0] = showYear;
        return this;
    }

    public MyTimeDialog setShowMonth(boolean showMonth) {
        mShowType[1] = showMonth;
        return this;
    }

    public MyTimeDialog setShowDay(boolean showDay) {
        mShowType[2] = showDay;
        return this;
    }

    public MyTimeDialog setShowHours(boolean showMinutes) {
        mShowType[3] = showMinutes;
        return this;
    }

    public MyTimeDialog setShowMinutes(boolean showMinutes) {
        mShowType[4] = showMinutes;
        return this;
    }

    public MyTimeDialog setShowSeconds(boolean showSeconds) {
        mShowType[5] = showSeconds;
        return this;
    }

    public MyTimeDialog setShowSelectData(Calendar selectData) {
        selectCalendar = selectData;
        return this;
    }

    public void show() {
        TimePickerBuilder tempTimePickerBuiler = new TimePickerBuilder(UIUtils.getCurActivity(), mOnTimeSelectListener).setRangDate(mStartCalendar, mEndCalendar);
        if (mShowNumber > 1) {
            tempTimePickerBuiler.setItemVisibleCount(mShowNumber);
        }
        //设置默认选择的时间
        if (selectCalendar != null) {
            tempTimePickerBuiler.setDate(selectCalendar);
        } else {
            tempTimePickerBuiler.setDate(Calendar.getInstance());
        }
        tempTimePickerBuiler.isAlphaGradient(true).isDialog(true);
        tempTimePickerBuiler.setType(mShowType).setTitleText(mTitle);
        if (mLeftText != null) {
            tempTimePickerBuiler.setCancelText(mLeftText);
        }
        if (mRightText != null) {
            tempTimePickerBuiler.setSubmitText(mRightText);
        }
        if (mLeftTextColor != -1) {
            tempTimePickerBuiler.setCancelColor(mLeftTextColor);
        }

        if (mRightTextColor != -1) {
            tempTimePickerBuiler.setSubmitColor(mRightTextColor);
        }

        if (mFontSize != 0) {
            tempTimePickerBuiler.setContentTextSize(mFontSize);
        }
        tempTimePickerBuiler.setLineSpacingMultiplier((float) mLineSpacingMultiplier);//当前的高度的多少倍
        TimePickerView timePickerView = tempTimePickerBuiler.build();

        Dialog mDialog = timePickerView.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            int value = UIUtils.dpToPx(mLeftAndRightMargin);
            params.leftMargin = value;
            params.rightMargin = value;
            timePickerView.getDialogContainerLayout().setLayoutParams(params);

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
