package com.zee.example

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lzy.imagepicker.loader.ImageLoader

/**
 * Created by liuzuo on 18-5-21.
 */
class PhotoPickerImageLoader : ImageLoader {
    override fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(activity).load(path).into(imageView)
    }

    override fun displayImagePreview(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity).load(path).into(imageView)
    }

    override fun clearMemoryCache() {}
}