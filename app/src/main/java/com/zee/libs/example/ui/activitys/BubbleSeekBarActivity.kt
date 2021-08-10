package com.zee.libs.example.ui.activitys

import com.zee.activity.BaseZActivity
import com.zee.dialog.BindViewAdapter
import com.zee.dialog.MyDialogK
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_bubble.*

/**
 *created by zee on 2021/7/16.
 *
 */
class BubbleSeekBarActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_bubble
    }

    override fun initViews() {
        tv_dialog.setOnClickListener {
            MyDialogK.init(R.layout.dialog_seekbar) {}.show()

            MyDialogK.initBottom(R.layout.dialog_seekbar) {

            }.show()
        }
    }
}