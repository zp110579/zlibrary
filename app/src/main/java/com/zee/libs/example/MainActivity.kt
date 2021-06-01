package com.zee.libs.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zee.extendobject.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun eventBusRegisterBusWays() {
        eventBusRegisterThis()
        eventBusRegisterThis("1")
        eventBusRegisterThis(0)
        eventBusRegisterThisAndBindCurActivity()
        eventBusUnRegisterThis()
        eventBusUnRegister(this)
    }

    private fun eventBusPostExample() {
        eventBusPost("1234")
        eventBusPost("123", "123")
        eventBusPostTagNoParam("123")
        eventBusPostTagNoParam("123", "3456")

        getEventBusSubscriber("123").post(123, "123")
    }

    private fun initView() {}
}