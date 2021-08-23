package com.zee.libs.example.ui.activitys


import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import com.zee.log.ZLog
import kotlinx.android.synthetic.main.activity_twotextview.*

/**
 *created by zee on 2021/7/22.
 *
 */
class LRViewActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_twotextview
    }

    override fun initViews() {
        val size = tv_systom.textSize

        ZLog.i("---->>$size---->>${tv_custom.textSize}")
    }

}