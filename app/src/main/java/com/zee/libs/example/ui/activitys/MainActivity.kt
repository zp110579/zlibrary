package com.zee.libs.example.ui.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zee.extendobject.*
import com.zee.libs.example.R
import com.zee.utils.ZEventBusUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}