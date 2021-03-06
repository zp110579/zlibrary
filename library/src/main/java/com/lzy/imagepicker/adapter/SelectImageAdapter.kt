package com.lzy.imagepicker.adapter

import android.view.View
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageFolder
import com.lzy.imagepicker.util.Utils
import com.zee.base.Z1RecyclerAdapter
import com.zee.extendobject.curActivityEx
import com.zee.extendobject.setOnNoDoubleClickListener
import com.zee.extendobject.setVisibleOrGone
import com.zee.libs.R
import com.zee.utils.UIUtils

class SelectImageAdapter : Z1RecyclerAdapter<ImageFolder>(R.layout.adapter_folder_list_item) {
    var width = 0

    init {
        width = Utils.getImageItemWidth(curActivityEx());
    }

    override fun initViews(parentView: View, location: Int, bean: ImageFolder) {
        bean.apply {
            ImagePicker.getInstance().imageLoader.displayImage(curActivityEx(), cover.path, imageViewById(R.id.iv_cover), width, width)
            setText(R.id.tv_folder_name, name)//文件名字
            setText(R.id.tv_image_count, UIUtils.getCurActivity().getString(R.string.zee_str_ip_folder_image_count, images.size))//文件里的图片数量

            viewById(R.id.iv_folder_check).setVisibleOrGone(selectItemIndex == location)//选中
        }
        parentView.setOnNoDoubleClickListener {
            setSelectItemAndNotifyDataSetChanged(location)
        }
    }
}