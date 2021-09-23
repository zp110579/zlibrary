package com.zee.libs.example.ui.activitys

import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import com.zee.view.ZxFoldTextView
import kotlinx.android.synthetic.main.activity_zx_foldtextview.*

class ZxFoldTextViewActivity : BaseZActivity() {

    override fun getLayoutID(): Int {
        return R.layout.activity_zx_foldtextview
    }

    override fun initViews() {
//        val bean = CustomToastBeanSettings();
//        bean.apply {
////            setPadding(0, 0, 0, 0)
//        }
//        ToastUtils.setCustomToastBeanSettings(bean)
//        id_FoldTextView_root.setOnClickListener {
//            //            zv_foldTextView.change()
//            UIUtils.showToastShort("显示出的效果")
//        }

        zv_foldTextView.setOnFoldChangeListener(object : ZxFoldTextView.OnFoldChangeListener {
            override fun onViewVisible(isVisible: Int) {
                id_FoldTextView_btn.visibility = isVisible;
            }

            override fun onFoldChange(isExpand: Boolean) {
                if (isExpand) {
//                    zv_foldTextView.setShowMaxLine(Int.MAX_VALUE)
                    id_FoldTextView_btn.setImageResource(R.drawable.ic_pulltorefresh_arrow);
                } else {
//                    zv_foldTextView.setShowMaxLine(3)
                    id_FoldTextView_btn.setImageResource(R.mipmap.base_ic_arrow_down);
                }
            }
        })
//        zv_foldTextView.text=""
        zv_foldTextView.setOnClickListener {
            zv_foldTextView.foldChangText()
        }
    }

}
