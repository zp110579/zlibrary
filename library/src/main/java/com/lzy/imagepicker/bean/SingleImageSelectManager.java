package com.lzy.imagepicker.bean;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePickerFragment;
import com.lzy.imagepicker.view.CropImageView;

/**
 * 单个图片的选择
 */
public class SingleImageSelectManager {

    ImagePicker imagePicker = ImagePicker.getInstance();

    public SingleImageSelectManager() {
        imagePicker.setShowCamera(false);
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(false);//设置选择后可以编辑
    }

    /**
     * 是否在第一个位置显示照相机的图片
     */
    public SingleImageSelectManager setShowCameraFirst() {
        imagePicker.setShowCamera(true);
        return this;
    }


    public SingleImageSelectManager setMultiMode() {
        imagePicker.setMultiMode(true);
        return this;
    }

    /**
     * 设置矩形编辑
     *
     * @return
     */
    public SingleImageSelectManager setRectangleEditSize(int width, int height) {
        imagePicker.setFocusSize(width, height);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setCrop(true);//设置选择后可以编辑
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        return this;
    }

    /**
     * 设置圆形编辑
     *
     * @return
     */
    public SingleImageSelectManager setCircleEditSize(int width) {
        imagePicker.setFocusSize(width, width);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setCrop(true);//设置选择后可以编辑
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        return this;
    }


    public void letsGo(OnImagePickerListener onImagePickerListener) {
        ImagePickerFragment imagePickerFragment = ImagePickerFragment.newInstant(false);
        imagePickerFragment.prepareRequest( onImagePickerListener);
    }
}
