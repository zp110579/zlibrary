package com.zee.orderwork;

public class WorkBean implements IWorkFinish {
    private OrderWorkManager mOrderWorkManager;
    private IWork mIWork;

    public void setIWork(IWork IWork) {
        mIWork = IWork;
    }

    void setOrderWorkManager(OrderWorkManager orderWorkManager) {
        mOrderWorkManager = orderWorkManager;
    }

    public void onWork() {
        mIWork.onWork(this);
    }

    /**
     * 完成
     */
    public void workFinish() {
        if (mOrderWorkManager != null) {
            mOrderWorkManager.startWork();
        }
    }
}
