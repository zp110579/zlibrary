package com.zee.route;

import com.zee.listener.LetsGoListener;
import com.zee.listener.OnActivityResultListener;
import com.zee.utils.UIUtils;

public class PActivityInfoBean extends BaseInfoBean<PActivityInfoBean> {
    private Integer mRequestCode;
    OnActivityResultListener mOnActivityResultListener;

    public PActivityInfoBean(String name, String module) {
        super(name, module, 1);
    }

    public PActivityInfoBean(Class<?> clssz) {
        super(clssz, 1);
    }

    public void letsGo() {
        letsGo(null);
    }

    public void letsGo(final LetsGoListener letsGoListener) {
        if (UIUtils.isOnMainThread()) {
            openActivity(letsGoListener);
        } else {
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    openActivity(letsGoListener);
                }
            });
        }
    }

    public PActivityInfoBean requestCodeCallBack(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PActivityInfoBean requestCodeCallBack(OnActivityResultListener listener) {
        mRequestCode = 10;
        mOnActivityResultListener = listener;
        return this;
    }

    protected Integer getRequestCode() {
        return mRequestCode;
    }

    protected OnActivityResultListener getOnActivityResultListener() {
        return mOnActivityResultListener;
    }

    private void openActivity(LetsGoListener letsGoListener) {
        if (mGoalClass != null) {
            PActivityManagerP.openActivityClass(this, letsGoListener);
        } else {
            PActivityManagerP.openActivity(this, letsGoListener);
        }
    }
}
