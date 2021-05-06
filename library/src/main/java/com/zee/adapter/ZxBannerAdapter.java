package com.zee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.zee.log.ZLog;
import com.zee.utils.UIUtils;
import com.zee.view.ZxBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public abstract class ZxBannerAdapter<T> {
    private List mList = new ArrayList();
    private int curIndex;
    private Context mContext;

    @SuppressWarnings("unchecked")
    public ZxBannerAdapter(List list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
    }

    public List getList() {
        return mList;
    }

    public View createViews(Context context, int realPositon, int position) {
        curIndex = position;
        mContext = context;
        return createView(context, curIndex);
    }

    public abstract View createView(Context context, int position);

    @SuppressWarnings("unchecked")
    public T getBean() {
        Object object = mList.get(curIndex);
        return (T) object;
    }

    public Context getContext() {
        return mContext;
    }

    public void startActivity(Intent paIntent) {
        UIUtils.startActivity(paIntent);

    }

    public void startActivity(Class<?> paClass) {
        UIUtils.startActivity(paClass);
    }
}