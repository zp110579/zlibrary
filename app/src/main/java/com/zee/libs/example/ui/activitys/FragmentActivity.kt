package com.zee.libs.example.ui.activitys

import com.zee.activity.BaseZActivity
import com.zee.extendobject.setOnClickAndOpenActivityEx
import com.zee.libs.example.R
import com.zee.libs.example.ui.activitys.fragment.FragmentViewPageActivity
import kotlinx.android.synthetic.main.activity_fragment.*


class FragmentActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_fragment
    }

    override fun initViews() {
        tv_viewPage.setOnClickAndOpenActivityEx(FragmentViewPageActivity::class.java)
    }

}