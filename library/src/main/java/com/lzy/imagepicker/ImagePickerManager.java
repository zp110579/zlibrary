package com.lzy.imagepicker;

import android.Manifest;

import com.lzy.imagepicker.bean.CameraPickerImageSelectManager;
import com.lzy.imagepicker.bean.ManyImageSelectManager;
import com.lzy.imagepicker.bean.SingleImageSelectManager;
import com.lzy.imagepicker.loader.ImageLoader;
import com.zee.listener.OnPermissionListener;
import com.zee.utils.SuperZPerMissionUtils;

import java.util.List;

public class ImagePickerManager {

    /**
     * 图库里单个图片选择
     *
     * @return
     */
    public static SingleImageSelectManager singleSelectImage() {
        return new SingleImageSelectManager();
    }

    /**
     * 图库里多个图片选择
     *
     * @return
     */
    public static ManyImageSelectManager manySelectImages(int selectLimit) {
        return new ManyImageSelectManager(selectLimit);
    }

    /**
     * 像机直接拍摄
     *
     * @return
     */
    public static CameraPickerImageSelectManager cameraImage() {
        return new CameraPickerImageSelectManager();
    }

    public static void setImageLoader(ImageLoader imageLoader) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(imageLoader);   //设置图片加载器
    }
}
