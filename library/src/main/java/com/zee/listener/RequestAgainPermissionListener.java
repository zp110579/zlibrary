package com.zee.listener;

//失败后再次请求
public interface RequestAgainPermissionListener {
    //再次请求
    void onRequestResult();
}
