package com.zee.libs.example.ui.activitys

import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.widget.Toast
import com.zee.activity.BaseZActivity
import com.zee.extendobject.setOnClickAndOpenActivityEx
import com.zee.libs.example.R
import com.zee.libs.example.ui.activitys.recyclerview.FlexBoxRecyclerViewActivity
import kotlinx.android.synthetic.main.activity_marquee.*
import kotlinx.android.synthetic.main.activity_zxrecyclerview.*
import java.lang.String
import java.util.*

/**
 *created by zee on 2021/7/16.
 *
 */
class ZxRecyclerViewActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_zxrecyclerview
    }

    override fun initViews() {
        tv_Flexbox.setOnClickAndOpenActivityEx(FlexBoxRecyclerViewActivity::class.java)
    }
}