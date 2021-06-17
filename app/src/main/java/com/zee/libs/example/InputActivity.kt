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

/**
 * 键盘
 */
class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
    }
}