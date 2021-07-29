package com.zee.libs.example.ui.activitys

import android.view.View
import com.zee.activity.BaseZActivity
import com.zee.dialog.BindViewAdapter
import com.zee.dialog.MyLRDialog
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_dialog.*

/**
 * Dialog
 */
class DialogManagerActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_dialog
    }

    override fun initViews() {
        tv_left_dialog.setOnClickListener {
            MyLRDialog.initFromLeft(object : BindViewAdapter(R.layout.dialog_main) {
                override fun initViews(paView: View?) {
                }
            }).setMargin(40).setFullScreen(false).show()
        }
        tv_right_dialog.setOnClickListener {
            MyLRDialog.initFromRight(object : BindViewAdapter(R.layout.dialog_main) {
                override fun initViews(paView: View?) {

                }
            }).setMargin(40).setFullScreen(false).show()
        }
    }
}