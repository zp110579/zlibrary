package com.lzy.imagepicker.util

import com.lzy.imagepicker.adapter.SelectImageAdapter
import com.lzy.imagepicker.bean.ImageFolder
import com.zee.adapter.OnItemClickListener
import com.zee.dialog.MyDialogK
import com.zee.extendobject.eventBusPost
import com.zee.libs.R
import com.zee.utils.UIUtils
import com.zee.utils.ZScreenUtils
import com.zee.view.ZxMarqueeView

class ImageDialogManager {

    fun showDial0g(index: Int, list: List<ImageFolder>) {
        MyDialogK.initBottom(R.layout.lib_dialog_folder) {
            val recyclerView = recyclerViewById(R.id.pictureRV)
            val adapter = SelectImageAdapter()
            recyclerView.adapter = adapter
            adapter.setList(list)
            adapter.setSelectItemAndNotifyDataSetChanged(index)
            adapter.setItemClickListener(object : OnItemClickListener<ImageFolder> {
                override fun onItemClick(bean: ImageFolder?, position: Int) {
                    adapter.notifyItemChanged(position + 1)
                    eventBusPost(position, "select_item")
                    dismiss()
                }
            })
        }.showOnlyOneTag("ImageDialogManager_showDialog").show()
    }
}