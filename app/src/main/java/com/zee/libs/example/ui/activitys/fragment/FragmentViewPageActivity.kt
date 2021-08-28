package com.zee.libs.example.ui.activitys.fragment

import android.view.View
import com.zee.activity.BaseZActivity
import com.zee.adapter.CustomViewPageAdapterManager
import com.zee.extendobject.showToastShort
import com.zee.libs.example.R
import com.zee.libs.example.ui.fragments.FragmentA
import com.zee.libs.example.ui.fragments.FragmentB
import com.zee.view.ViewIsCanClickListener
import kotlinx.android.synthetic.main.activity_fragment_viewpage.*

class FragmentViewPageActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_fragment_viewpage
    }

    override fun initViews() {
        viewPage.adapter = CustomViewPageAdapterManager(this).addFragment(FragmentA(), FragmentB()).fragmentStatePagerAdapter
        tab_title.setViewPager(viewPage)
        tab_title.setViewIsCanClickListener(object : ViewIsCanClickListener {
            override fun isCanClick(index: Int, view: View?): Boolean {

                if (index == 1) {
                    showToastShort("不能点击")
                    return false
                }
                return true
            }

        })
    }
}