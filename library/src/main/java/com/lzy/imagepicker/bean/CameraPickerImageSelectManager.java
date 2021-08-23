package com.lzy.imagepicker.bean;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePickerFragment;
import com.lzy.imagepicker.view.CropImageView;
import com.zee.utils.UIUtils;

public class CameraPickerImageSelectManager {
    private ImagePicker imagePicker = ImagePicker.getInstance();
    private int screenWith;

    public CameraPickerImageSelectManager() {
        imagePicker.setCrop(false);//设置选择后可以编辑
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) UIUtils.getCurActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWith = displayMetrics.widthPixels;
    }

    /**
     * 设置截取的屏幕的宽度和高度都是等于屏幕的宽度
     *
     * @return
     */
    public CameraPickerImageSelectManager setRectangleEdit() {
        return setRectangleEditSize(screenWith, screenWith);
    }


    /**
     * 设置矩形编辑
     *
     * @return
     */
    public CameraPickerImageSelectManager setRectangleEditSize(int width, int height) {
        imagePicker.setFocusSize(width, height);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setCrop(true);//设置选择后可以编辑
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        return this;
    }

    /**
     * 设置圆形编辑
     *
     * @return
     */
    public CameraPickerImageSelectManager setCircleEditSize(int width) {
        imagePicker.setFocusSize(width, width);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setCrop(true);//设置选择后可以编辑
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        return this;
    }

    public void letsGo(OnImagePickerListener onImagePickerListener) {
        ImagePickerFragment imagePickerFragment = ImagePickerFragment.newInstant(true);
        imagePickerFragment.prepareRequest( onImagePickerListener);
    }
}
