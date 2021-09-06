package com.zee.libs.example.ui.activitys

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import com.zee.libs.example.ui.activitys.fragment.TabFragment
import kotlinx.android.synthetic.main.activity_zx_solideitem.*

class ZxSolidItemTagLayoutActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_zx_solideitem
    }

    override fun initViews() {
        val title = arrayListOf<String>("第一", "第二显示的多些")
        id_fragment_zxSolidItem_layout.setTabData(title.toTypedArray())
        id_Solid_viewPage.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return 2;
            }

            override fun getItem(p0: Int): Fragment {
                return TabFragment.newInstance(p0)
            }
        }
        id_zxSolidItem_b.setViewPager(id_Solid_viewPage)
    }


}
