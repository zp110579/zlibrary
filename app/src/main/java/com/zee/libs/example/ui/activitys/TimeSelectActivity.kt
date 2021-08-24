package com.zee.libs.example.ui.activitys

import android.graphics.Color
import android.widget.Toast
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.contrarywind.listener.OnItemSelectedListener
import com.zee.activity.BaseZActivity
import com.zee.extendobject.showToastShort
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_time_select.*
import java.util.*

/**
 * 时间选择
 */
class TimeSelectActivity : BaseZActivity() {
    private val mShowType = booleanArrayOf(true, true, true, false, false, false) //显示类型，默认显示： 年月日


    override fun getLayoutID(): Int {
        return R.layout.activity_time_select
    }


    override fun initViews() {
//        initTimePicker()
        val mOptionsItems: MutableList<String> = ArrayList()
        mOptionsItems.add("2010")
        mOptionsItems.add("2020")
        mOptionsItems.add("2030")
        mOptionsItems.add("2040")
        mOptionsItems.add("2050")
        mOptionsItems.add("2060")
        mOptionsItems.add("2070")
        wheelview_a.setAdapter(ArrayWheelAdapter<String>(mOptionsItems))
        wheelview_a.setOnItemSelectedListener(OnItemSelectedListener { index -> showToastShort("" + mOptionsItems[index]) })

        wheelview_b.setAdapter(ArrayWheelAdapter<String>(mOptionsItems))
        wheelview_c.setAdapter(ArrayWheelAdapter<String>(mOptionsItems))
    }


//    private fun initTimePicker() { //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
////因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
//        val selectedDate = Calendar.getInstance()
//        val startDate = Calendar.getInstance()
//        startDate[2013, 0] = 23
//        val endDate = Calendar.getInstance()
//        endDate[2019, 11] = 28
//        //时间选择器
//        val pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
//            //选中事件回调
//// 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
///*btn_Time.setText(getTime(date));*/
////            val btn = v as Button
////            btn.setText(getTime(date))
//        })
//                .setLayoutRes(R.layout.dialog_main) { v ->
//                    //                    val tvSubmit = v.findViewById<View>(R.id.tv_finish) as TextView
////                    val ivCancel = v.findViewById<View>(R.id.iv_cancel) as ImageView
////                    tvSubmit.setOnClickListener {
//////                        pvTime.returnData()
////                        /*pvTime.dismiss();*/
////                    }
////                    ivCancel.setOnClickListener { /*pvTime.dismiss();*/ }
//                }
//                .setType(booleanArrayOf(true, true, true, false, false, false))
//                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
//                .setDividerColor(Color.DKGRAY)
//                .setContentTextSize(20)
//                .setDate(selectedDate)
//                .setRangDate(startDate, selectedDate)
//                .setDecorView(rootView) //非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
//                .setOutSideColor(0x00000000)
//                .setOutSideCancelable(false)
//                .build()
//        pvTime.setKeyBackCancelable(false) //系统返回键监听屏蔽掉
//    }
}