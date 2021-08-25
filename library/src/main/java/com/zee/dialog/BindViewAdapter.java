/*
 * Copyright (c) 2019.8 - Present by KewenC.
 */

package com.zee.dialog;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.zee.base.OnNoDoubleClickListener;
import com.zee.bean.BindAdapter;
import com.zee.bean.IDismissListener;
import com.zee.log.ZLog;
import com.zee.utils.UIUtils;

public abstract class BindViewAdapter extends BindAdapter implements Parcelable {
    private transient IDismissListener iDismissListener;
    private @LayoutRes
    int mLayoutID;

    public BindViewAdapter() {
    }

    public BindViewAdapter(@LayoutRes int layoutID) {
        mLayoutID = layoutID;
    }

    public void setBindView(IDismissListener paMyDialog) {
        iDismissListener = paMyDialog;
    }

    public IDismissListener getDismissListener() {
        return iDismissListener;
    }

    public int getLayoutID() {
        return mLayoutID;
    }

    public void dismiss() {
        try {
            iDismissListener.dismiss();
        } catch (Exception e) {
            ZLog.e(e, true);
        }
    }

    public final void startActivityAndDismiss(@IdRes int id, final Class<?> cls) {
        findViewById(id).setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                UIUtils.startActivity(cls);
                dismiss();
            }
        });
    }

    /**
     * 点击并关闭Dialog
     *
     * @param id
     * @param onClickListener
     */
    public void setOnClickListenerAndDismiss(@IdRes int id, final View.OnClickListener onClickListener) {
        findViewById(id).setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onClickListener.onClick(v);
                dismiss();

            }
        });
    }

    /**
     * 点击并关闭Dialog
     *
     * @param id
     */
    public void setOnClickListenerAndDismiss(@IdRes int id) {
        findViewById(id).setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLayoutID);
    }
}
