package com.zee.libs.example.ui.activitys.recyclerview

import android.view.View
import com.zee.activity.BaseZActivity
import com.zee.base.Z1RecyclerAdapter
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_recyclerview_flex.*
import com.google.android.flexbox.FlexboxLayoutManager

/**
 *created by zee on 2021/7/16.
 *FlexBox布局的Recyclerview
 */
class FlexBoxRecyclerViewActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_recyclerview_flex
    }

    override fun initViews() {
//        recycler_flex.layoutManager=FlexboxLayoutManager(this);

        recycler_flex.adapter = object : Z1RecyclerAdapter<String>(R.layout.activity_recyclerview_flex_item) {
            override fun initViews(parentView: View, location: Int, bean: String) {
                bean.apply {
                    setText(R.id.zv_name, this)
                }
            }
        }
        val list = arrayListOf<String>("天猫", "淘宝", "进洞", "平多多", "平多多", "平多多", "平多多", "平阿斯顿发多多", "平文案防守打法多多")
        recycler_flex.setList(list)
    }

}