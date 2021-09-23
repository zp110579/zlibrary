package com.lzy.imagepicker.adapter

import android.view.View
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageFolder
import com.lzy.imagepicker.util.Utils
import com.zee.base.Z1RecyclerAdapter
import com.zee.extendobject.curActivityEx
import com.zee.extendobject.getString
import com.zee.extendobject.setOnClick
import com.zee.extendobject.setVisibleOrGone
import com.zee.libs.R

class SelectImageAdapter : Z1RecyclerAdapter<ImageFolder>(R.layout.adapter_folder_list_item) {
    var width = 0

    init {
        width = Utils.getImageItemWidth(curActivityEx());
    }

    override fun initViews(parentView: View, location: Int, bean: ImageFolder) {
        bean.apply {
            ImagePicker.getInstance().imageLoader.displayImage(curActivityEx(), cover.path, imageViewById(R.id.iv_cover), width, width)
            setText(R.id.tv_folder_name, name)//文件名字
            setText(R.id.tv_image_count, getString(R.string.ip_folder_image_count, images.size))//文件里的图片数量
            viewById(R.id.iv_folder_check).setVisibleOrGone(selectItemIndex == location)//选中
        }
        parentView.setOnClick {
            setSelectItemAndNotifyDataSetChanged(location)
        }
    }
}