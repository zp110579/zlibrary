package com.zee.libs.example.ui.activitys

import android.support.v4.app.Fragment
import com.zee.activity.BaseZActivity
import com.zee.adapter.FragmentManagerAdapter
import com.zee.adapter.ViewPageFragmentAdapter
import com.zee.libs.example.R
import com.zee.libs.example.TabBean
import com.zee.libs.example.ui.activitys.fragment.TabFragment
import com.zee.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_zx_tablayout.*

import java.util.ArrayList

class ZxTabLayoutActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_zx_tablayout
    }

    override fun initViews() {
        exampleB()
        example1()
        example2()
        example3()
        example4()
    }

    private fun exampleB() {
    }

    private fun example1() {
        val list1 = arrayOf("选项1", "选项2", "选项3")
        zv_tab_Layout_a.setTitles(list1, object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
            }

            override fun onTabReselect(position: Int) {
            }

        })
    }

    private fun example2() {
        zv_tab_Layout_b.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
            }

            override fun onTabReselect(position: Int) {
            }
        })
    }

    private fun example3() {
        zv_tab_Layout_c.setFragmentManagerAdapter(object : FragmentManagerAdapter(this, R.id.fl_content_view) {

            override fun getFragment(index: Int): Fragment {
                return TabFragment.newInstance(index)
            }
        })
    }

    private fun example4() {
        val list = ArrayList<TabBean>()
        list.add(TabBean("titleA"))
        list.add(TabBean("titleB"))
        list.add(TabBean("titleC"))
        list.add(TabBean("titleD"))
        list.add(TabBean("titleE"))

        zv_tab_Layout_d.setViewPageFragmentAdapter(object : ViewPageFragmentAdapter(supportFragmentManager, viewPage_a, list) {
            override fun getV4Fragment(index: Int): Fragment {
                return TabFragment.newInstance(index);
            }
        })
    }
}
