package com.zee.orderwork;

import java.util.LinkedList;

public class OrderWorkManager {
    LinkedList<WorkBean> mWorkBeanArrayList = new LinkedList<>();

    public OrderWorkManager addWork(IWork iwork) {
        WorkBean workBean = new WorkBean();
        workBean.setIWork(iwork);
        workBean.setOrderWorkManager(this);
        mWorkBeanArrayList.add(workBean);
        return this;
    }

    public void startWork() {
        if (mWorkBeanArrayList.size() > 0) {
            WorkBean workBean = mWorkBeanArrayList.pop();
            if (workBean != null) {
                workBean.onWork();
            }
        }
    }
}
