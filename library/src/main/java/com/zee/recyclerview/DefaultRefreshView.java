package com.zee.recyclerview;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zee.libs.R;
import com.zee.log.ZLog;
import com.zee.utils.UIUtils;


/**
 * @author Administrator
 */
class DefaultRefreshView implements IRecyclerViewRefreshView {
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mStatusTextView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private static final int ROTATE_ANIM_DURATION = 180;
    private HintText mHintText;

    @Override
    public int getLayoutID() {
        return R.layout.zv_recyclerview_header;
    }

    @Override
    public void initViews(LinearLayout view) {
        mArrowImageView = view.findViewById(R.id.recyclerView_header_arrow);
        mProgressBar = view.findViewById(R.id.recyclerView_header_progressbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorStateList = mHintText.getProgressViewColor();
            if (colorStateList != null) {
                mProgressBar.setIndeterminateTintList(colorStateList);
            }
        }
        mStatusTextView = view.findViewById(R.id.recyclerView_header_title);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        int textColor = mHintText.getRefresh_textColor();
        if (textColor != Color.TRANSPARENT) {
            mStatusTextView.setTextColor(mHintText.getRefresh_textColor());
        }
        float textSize = mHintText.getRefresh_textSize();
        if (textSize != -1) {
            mStatusTextView.setTextSize(textSize);
        }
        mStatusTextView.setText(mHintText.getRefresh_hintPullDownText());
        if (mHintText.getRefreshBackgroundColor() != Color.TRANSPARENT) {
            view.setBackgroundColor(mHintText.getRefreshBackgroundColor());
        }
    }

    public void setHintText(HintText hintText) {
        mHintText = hintText;
    }

    @Override
    public int getViewHeight() {
        return UIUtils.dpToPx(50);
    }

    @Override
    public void onHintPullDown(int oldState) {
        //提示下拉
        mArrowImageView.setVisibility(View.VISIBLE);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (oldState == RefreshVerticalLinearLayout.STATE_HINT_RELEASE) {
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mRotateDownAnim);
        }
        mStatusTextView.setText(mHintText.getRefresh_hintPullDownText());
    }

    @Override
    public void onHintReleaseHand() {
        //提示松手
        mArrowImageView.clearAnimation();
        mArrowImageView.startAnimation(mRotateUpAnim);
        mStatusTextView.setText(mHintText.getRefresh_hintReleaseHandText());
    }

    @Override
    public void onRefreshStart() {
        //开始刷新
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.INVISIBLE);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mStatusTextView.setText(mHintText.getRefresh_refreshStartText());
    }

    @Override
    public void onRefreshEnd() {
        //结束刷新
        mArrowImageView.setVisibility(View.INVISIBLE);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
//        mStatusTextView.setText(mHintText.getRefresh_refreshEndText());
    }

    @Override
    public void onHeight(int height) {
    }

    @Override
    public void destroy() {
        if (mRotateUpAnim != null) {
            mRotateUpAnim.cancel();
            mRotateUpAnim = null;
        }
        if (mRotateDownAnim != null) {
            mRotateDownAnim.cancel();
            mRotateDownAnim = null;
        }
    }
}
