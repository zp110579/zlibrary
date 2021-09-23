package com.zee.dialog

import android.view.View
import com.zee.bean.IDismissListener

class MyDialogK : MyDialog(), IDismissListener {
    companion object {
        fun init(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyDialog {
            return init(object : BindViewAdapter(layoutID) {
                override fun initViews(paView: View) {
                    binView.invoke(this)
                }
            })
        }

        fun initTop(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyDialog {
            return initTop(object : BindViewAdapter(layoutID) {
                override fun initViews(paView: View) {
                    binView.invoke(this)
                }
            })
        }

        fun initBottom(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyDialog {
            return initBottom(object : BindViewAdapter(layoutID) {
                override fun initViews(paView: View) {
                    binView.invoke(this)
                }
            })
        }
    }
}