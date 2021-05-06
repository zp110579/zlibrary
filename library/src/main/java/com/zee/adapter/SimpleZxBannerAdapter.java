package com.zee.adapter;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zee.libs.R;
import com.zee.utils.UIUtils;

import java.util.List;


/**
 * @author Administrator
 */
public abstract class SimpleZxBannerAdapter<T> extends ZxBannerAdapter<T> {
    private int leftMargin;
    private int topMargin;
    private int rightMargin;
    private int bottomMargin;

    public SimpleZxBannerAdapter(List list) {
        super(list);
    }

    @Override
    public View createView(Context context,int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.zv_banner_view, null, false);
        ImageView imageView = view.findViewById(R.id.iv_image_banner);
        initViews(imageView,position);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        return view;
    }

    public void setImageMargins(int left, int top, int right, int bottom) {
        leftMargin = left;
        topMargin = top;
        rightMargin = right;
        bottomMargin = bottom;
    }


    /**
     * 初始化BannerView图片
     *
     * @param imageView
     */
    public abstract void initViews(ImageView imageView,int position);
}
