package com.zee.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zee.listener.OnOpenActivityResultListener;
import com.zee.utils.ZLibrary;

public final class CallBackFragment extends Fragment {

    private static final String PERMISSIONS = "requestCode";//请求的权限
    private static final String CLASSNAME = "className";//请求的权限
    private OnOpenActivityResultListener mOnPermissionListener;

    public static CallBackFragment newInstant(int requestCode, Class<?> className) {
        CallBackFragment fragment = new CallBackFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PERMISSIONS, requestCode);
        bundle.putString(CLASSNAME, className.getName());
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 准备请求
     */
    public void prepareCallBack(OnOpenActivityResultListener listener) {
        //将当前的请求码和对象添加到集合中
        this.mOnPermissionListener = listener;
        FragmentActivity activity = (FragmentActivity) ZLibrary.getInstance().getCurrentActivity();
        activity.getSupportFragmentManager().beginTransaction().add(this, activity.getClass().getName()).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int permissions = getArguments().getInt(PERMISSIONS);
        String className = getArguments().getString(CLASSNAME);
        try {
            Class<?> clazz = Class.forName(className);
            Intent intent = new Intent(getActivity(), clazz);
            startActivityForResult(intent, permissions);
        } catch (Exception e) {
            remove();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mOnPermissionListener != null) {
            mOnPermissionListener.onActivityResult(data);
        }
        remove();
    }

    private void remove() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}