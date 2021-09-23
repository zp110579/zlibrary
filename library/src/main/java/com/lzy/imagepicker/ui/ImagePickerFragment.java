package com.lzy.imagepicker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.bean.OnImagePickerListener;
import com.zee.utils.UIUtils;
import com.zee.utils.ZEventBusUtils;
import com.zee.utils.ZListUtils;

import org.greenrobot.eventbus.SubscribeMainThread;

import java.util.ArrayList;

public class ImagePickerFragment extends Fragment {
    private OnImagePickerListener mOnImagePickerListener;
    boolean isCameraMode = false;
    private int REQUEST_CODE_SCAN = 200;

    public static ImagePickerFragment newInstant(boolean isCameraMode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCameraMode", isCameraMode);//是否是相机模式
        ImagePickerFragment fragment = new ImagePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void prepareRequest(OnImagePickerListener listener) {
        //将当前的请求码和对象添加到集合中
        this.mOnImagePickerListener = listener;
        FragmentActivity activity = (FragmentActivity) UIUtils.getCurActivity();
        activity.getSupportFragmentManager().beginTransaction().add(this, activity.getClass().getName()).commitAllowingStateLoss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        isCameraMode = bundle.getBoolean("isCameraMode");
        openActivity();
        ZEventBusUtils.register(this);
    }

    private void openActivity() {
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        if (isCameraMode) {
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        }
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }


    @SubscribeMainThread(tag = "imageSelect_picker_list")
    public void onActivityResult(int type) {
        if (mOnImagePickerListener != null) {
            ArrayList<ImageItem> imageItemArrayList=ImagePicker.getInstance().getSelectedImages();
            if (ZListUtils.isNoEmpty(imageItemArrayList)) {
                mOnImagePickerListener.onImagePickerResult(imageItemArrayList);
            } else {
                mOnImagePickerListener.onImagePickerResult(new ArrayList<ImageItem>());
            }
        }
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZEventBusUtils.unregister(this);
    }
}
