package com.zee.recyclerview;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zee.libs.R;

/**
 * @author Administrator
 * @date 2018/11/21 0021
 */

class DefaultLoadMoreView implements IRecyclerViewLoadMoreView {
    private TextView mWordTextView;

    private ProgressBar mProgressBar;
    private HintText mHintText;

    @Override
    public int getLayoutID() {
        return R.layout.zv_recyclerview_footer;
    }

    @Override
    public void initViews(LinearLayout linearLayout) {
        mProgressBar = linearLayout.findViewById(R.id.recyclerView_footer_progressbar);
        mWordTextView = linearLayout.findViewById(R.id.recyclerView_footer_title);
    }

    public void setHintText(HintText hintText) {
        mHintText = hintText;
    }

    @Override
    public void destroy() {
        mProgressBar = null;
    }

    @Override
    public void onLoadStart() {
        mProgressBar.setVisibility(View.VISIBLE);
        initTextViewSizeAndColor();
        mWordTextView.setText(mHintText.getLoadMore_loadStartText());
    }


    @Override
    public void onLoadEnd() {
        initTextViewSizeAndColor();
        mWordTextView.setText(mHintText.getLoadMore_loadEndText());
    }

    @Override
    public void onNoData() {
        mProgressBar.setVisibility(View.GONE);
        initTextViewSizeAndColor();
        mWordTextView.setText(mHintText.getLoadMore_noMoreText());
    }

    private void initTextViewSizeAndColor() {
        int color = mHintText.getLoadMore_textColor();
        if (color != Color.TRANSPARENT) {
            mWordTextView.setTextColor(color);
        }

        float textSize = mHintText.getLoadMore_textSize();
        if (textSize != -1) {
            mWordTextView.setTextSize(textSize);
        }
    }
}
