package com.zee.utils;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.zee.fragment.PermissionFragment;
import com.zee.listener.OnPermissionListener;

import java.util.ArrayList;
import java.util.List;


public class SuperZPerMissionUtils {
    private ArrayList<String> mPermissions = new ArrayList<>();

    public static SuperZPerMissionUtils getInstance() {
        return new SuperZPerMissionUtils();
    }


    public SuperZPerMissionUtils add(String permission) {
        mPermissions.add(permission);
        return this;
    }

    public SuperZPerMissionUtils add(String... permissions) {
        for (String permission : permissions) {
            mPermissions.add(permission);
        }
        return this;
    }

    public SuperZPerMissionUtils addAll(List<String> permissions) {
        if (ZListUtils.isNoEmpty(permissions)) {
            mPermissions.addAll(permissions);
        }
        return this;
    }

    public SuperZPerMissionUtils addAll(String[] permissions) {

        if (ZListUtils.isNoEmpty(permissions)) {
            for (String loPermission : permissions) {
                mPermissions.add(loPermission);
            }
        }
        return this;
    }

    public void requestPermissions(OnPermissionListener listener) {
        requestPermissions(listener, (FragmentActivity) UIUtils.getCurActivity());
    }

    @Deprecated
    public void requestPermissions(OnPermissionListener listener, FragmentActivity paContext) {
        ArrayList<String> tempList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : mPermissions) {
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(paContext, permission)) {
                    tempList.add(permission);
                }
            }
        }

        if (tempList.size() > 0) {
            PermissionFragment fragment = PermissionFragment.newInstant(tempList);
            fragment.prepareRequest(paContext, listener);
        } else if (listener != null) {
            listener.onPerMission(new ArrayList<String>(), new ArrayList<String>());
        }
    }


    /**
     * 打开权限设置界面
     */
    public static void openSettingActivity() {
        ZPerMissionUtils.openSettingActivity();
    }
}
