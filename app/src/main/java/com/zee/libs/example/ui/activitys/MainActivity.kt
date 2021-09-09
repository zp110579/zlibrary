package com.zee.libs.example.ui.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.zee.extendobject.*
import com.zee.http.utils.MyOkHandlerUtils
import com.zee.libs.example.R

import com.zee.utils.ZEventBusUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_ZxSolidItemTabLayout.setOnClickAndOpenActivityEx(ZxSolidItemTagLayoutActivity::class.java)
        tv_zxSolidTabLayout.setOnClickAndOpenActivityEx(ZxSolidTabLayoutActivity::class.java)
        tv_zxTabLayout.setOnClickAndOpenActivityEx(ZxTabLayoutActivity::class.java)
        tv_recyclerView.setOnClickAndOpenActivityEx(ZxRecyclerViewActivity::class.java)
        tv_fragment.setOnClickAndOpenActivityEx(FragmentActivity::class.java)
        tv_app_upDate.setOnClickAndOpenActivityEx(AppUpdateActivity::class.java)

        tv_time.setOnClickAndOpenActivityEx(TimeSelectActivity::class.java)

        tv_scan.setOnClickListener {
            cameraScan {
                showToastShort(it)
            }
        }
        tv_picture.setOnClickAndOpenActivityEx(PictureActivity::class.java) //选择图片
        tv_marquee.setOnClickListener {
            startActivityEx(MarqueeActivity::class.java)
        }

        tv_dialog.setOnClickListener {
            startActivityEx(DialogManagerActivity::class.java)
        }
        tv_two_textView.setOnClickListener {
            startActivityEx(TwoTextViewActivity::class.java)
        }
        tv_webView.setOnClickListener {
            startActivityEx(WebViewActivity::class.java)
        }
        tv_seekbar.setOnClickListener {
            startActivityEx(SeekBarActivity::class.java)
        }
        tv_key.setOnClickListener {
            startActivityEx(InputActivity::class.java)
        }
        tv_eventBus.setOnClickListener {
            startActivityEx(EventBusActivity::class.java)
        }
        tv_life.setOnClickListener {
            startActivityEx(LifecycleActivity::class.java)
        }
        tv_eventBus_lifecycle.setOnClickListener {
            ZEventBusUtils.printCurAllSubscribers()
        }
        tv_bubble.setOnClickListener {
            startActivityEx(BubbleSeekBarActivity::class.java)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click()
        }
        return false
    }

    private var isExit = false
    private fun exitBy2Click() {
        if (!isExit) {
            isExit = true
            showToastShort("再按一次退出程序")
            postDelayed(2000) { isExit = false }
        } else {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}