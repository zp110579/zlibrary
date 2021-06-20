package com.zee.libs.example.ui.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zee.extendobject.*
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_key.setOnClickListener {
            startActivityEx(InputActivity::class.java)
        }
        tv_eventBus.setOnClickListener {
            startActivityEx(EventBusActivity::class.java)
        }
    }
}