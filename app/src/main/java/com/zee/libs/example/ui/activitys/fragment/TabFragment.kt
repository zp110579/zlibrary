package com.zee.libs.example.ui.activitys.fragment

import android.os.Bundle
import com.zee.activity.BaseZFragment
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.fragment_zx_tablayout.*


class TabFragment : BaseZFragment() {
    companion object {

        fun newInstance(index: Int): TabFragment {
            val argument = Bundle()
            argument.putInt("index", index)
            val tabFragment = TabFragment()
            tabFragment.arguments = argument;
            return tabFragment
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_zx_tablayout
    }


    override fun initViews() {
        val index = arguments!!.getInt("index")
        tv_tabLayout_title.text = "$index 位置"
    }

}