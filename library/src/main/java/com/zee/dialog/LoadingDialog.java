package com.zee.dialog;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.zee.libs.R;
import com.zee.utils.UIUtils;

/**
 * 加载动画
 */
public class LoadingDialog {
    private String loadingTxt = "加载中";
    private boolean outCancel = false;//是否点击外部取消
    private int width = 100;
    private int height = 100;
    private MyDialog mMyDialog;
    private BindViewAdapter mBindViewAdapter;

    private LoadingDialog(String loadingTxt) {
        this.loadingTxt = loadingTxt;
    }

    public static LoadingDialog newLoadDialog() {
        return new LoadingDialog("加载中");
    }

    public static LoadingDialog newLoadDialog(@StringRes int resID) {
        String title = UIUtils.getString(resID);
        return new LoadingDialog(title);
    }

    public static LoadingDialog newLoadDialog(String loadingTxt) {
        return new LoadingDialog(loadingTxt);
    }

    public LoadingDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public LoadingDialog setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public LoadingDialog setLoadingTxt(String loadingTxt) {
        this.loadingTxt = loadingTxt;
        return this;
    }


    public void updateText(String loadingTxt) {
        if (mBindViewAdapter != null) {
            TextView loadingTextView = mBindViewAdapter.findTextViewById(R.id.tv_loading_txt);
            if (loadingTextView != null) {
                loadingTextView.setText(loadingTxt);
            }
        }
    }

    public LoadingDialog show() {
        getDialogFragment().show();
        return this;
    }

    public LoadingDialog show(FragmentManager manager) {
        getDialogFragment().show(manager);
        return this;
    }

    @Deprecated
    public LoadingDialog showCurActivity() {
        getDialogFragment().show();
        return this;
    }

    public void dismiss() {
        try {
            mMyDialog.dismiss();
            mMyDialog = null;
        } catch (Exception e) {
        }
    }

    public MyDialog getDialogFragment() {
        mMyDialog = MyDialog.init(mBindViewAdapter = new BindViewAdapter(R.layout.zv_loading_layout) {
            @Override
            public void initViews(View paView) {
                TextView loadText = findViewById(R.id.tv_loading_txt);
                loadText.setText(loadingTxt);
            }
        }).setSize(width, width).setOutCancel(outCancel);
        return mMyDialog;
    }
}
