package com.zee.listener

import android.app.Activity

interface BackOrLeaveAppListener {

    fun back2App(activity: Activity) {
    }

    fun leaveApp(activity: Activity) {

    }
}