package com.zee.libs.example.ui.activitys

import android.widget.SeekBar
import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import com.zee.log.ZLog
import kotlinx.android.synthetic.main.activity_seekbar.*
import java.util.*

/**
 *created by zee on 2021/7/6.
 *
 */
class SeekBarActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_seekbar
    }

    override fun initViews() {
        val locale_language: String = Locale.getDefault().getLanguage()
    ZLog.i(locale_language)
//        seekBarView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//
//            }
//        })
    }
}