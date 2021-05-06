package com.zee.utils

import com.zee.listener.BackOrLeaveAppListener

class ZAppListenerUtils {

    companion object {

        fun setAppBackOrLeaveAppListener(appListener: BackOrLeaveAppListener) {
            ZLibrary.getInstance().setBackOrLeaveAppListener(appListener)
        }
    }
}