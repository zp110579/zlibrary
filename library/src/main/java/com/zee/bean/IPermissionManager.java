package com.zee.bean;

import android.support.annotation.NonNull;

import com.zee.listener.PermissionListener;
import com.zee.listener.RequestAgainPermissionListener;

public interface IPermissionManager {

    boolean checkPermission(String permission);

    boolean checkPermission(String[] permissions);

    void gotoPermissionActivity();

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);


    IPermissionManager setPermissionListener(PermissionListener permissionListener);

    IPermissionManager setRequestAgainPermissionListener(RequestAgainPermissionListener requestAgainPermissionListener);
}
