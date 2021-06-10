package com.zee.libs.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.zee.base.Z1RecyclerAdapter
import com.zee.dialog.MyDialogK
import com.zee.extendobject.*
import com.zee.popupWindow.MyPopupWindowK
import com.zee.recyclerview.RefreshAndLoadMoreAdapter
import com.zee.utils.ZLibrary
import com.zee.view.Zx1RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_dialog.setTextEx(1)
        findViewById<TextView>(R.id.tv_dialog).setOnNoDoubleClickListener {
            MyPopupWindowK.init(R.layout.dialog_main) {
                setOnClickListenerAndDismiss(R.id.tv_cancel) {
                    showToastShort("----->>>")
                }
                setOnClickListener(R.id.tv_sure) {
                    showToastShort("确定成功")
                }
            }.showAsDropDown(tv_dialog)
        }
        val recycler = Zx1RecyclerView(this)




        val list = arrayListOf("R.string.ZBannerView_indicator", "R.string.ZBannerView_viewPage")
        recyclerView.setRefreshAndLoadMordListener(
                object : RefreshAndLoadMoreAdapter() {
                    override fun onStartLoad(curPage: Int) {

                    }
                })

//        tv_dialog.setOnNoDoubleClickListener {
//
//        }
        recyclerView.setList(list)

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