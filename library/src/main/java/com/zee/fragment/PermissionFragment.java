package com.zee.fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zee.listener.OnPermissionListener;
import com.zee.utils.PermissionTextUtils;

import java.util.ArrayList;
import java.util.List;

public final class PermissionFragment extends Fragment {

    private static final String PERMISSIONS = "permissions";//请求的权限
    private OnPermissionListener mOnPermissionListener;


    public static PermissionFragment newInstant(ArrayList<String> permissions) {
        PermissionFragment fragment = new PermissionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PERMISSIONS, permissions);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 准备请求
     */
    public void prepareRequest(FragmentActivity activity, OnPermissionListener listener) {
        //将当前的请求码和对象添加到集合中
        this.mOnPermissionListener = listener;
        activity.getSupportFragmentManager().beginTransaction().add(this, activity.getClass().getName()).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> permissions = getArguments().getStringArrayList(PERMISSIONS);

        if (permissions == null) {
            return;
        }
        //开始请求权限
//        requestPermissions(permissions.toArray(new String[0]), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions.toArray(new String[permissions.size() - 1]), 100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> list = new ArrayList<>();

        if (permissions != null && permissions.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //如果被拒绝，那就记录下。
                    list.add(permissions[i]);
                }
            }
        }
        if (mOnPermissionListener != null) {
            List<String> loStrings = PermissionTextUtils.transformText(list);
            mOnPermissionListener.onPerMission(list, loStrings);
        }
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}