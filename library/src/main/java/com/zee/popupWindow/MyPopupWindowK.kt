package com.zee.popupWindow

import android.graphics.drawable.ColorDrawable
import android.support.annotation.LayoutRes
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zee.bean.IDismissListener
import com.zee.dialog.BindViewAdapter
import com.zee.dialog.MyDialogK
import com.zee.log.ZLog
import com.zee.utils.UIUtils
import com.zee.utils.ZScreenUtils

class MyPopupWindowK : IDismissListener {
    var popupWindow: PopupWindow? = null
        private set
    private var mBindViewAdapter: BindViewAdapter? = null
    private var layoutRes = 0

    //左右间隔
    private var mLeftOrRightMargin = 0

    //上下间隔值
    private var mTopOrBottomMargin = 0
    private var mOutsideTouchable = false
    private var mBgAlpha = 0.5f
    private var height = 0


    fun setOutsideTouchable(touchable: Boolean): MyPopupWindowK {
        mOutsideTouchable = touchable
        return this
    }

    fun setMargin(leftOrRightDp: Int, topOrBottomDp: Int): MyPopupWindowK {
        mLeftOrRightMargin = UIUtils.dpToPx(leftOrRightDp)
        mTopOrBottomMargin = UIUtils.dpToPx(topOrBottomDp)
        return this
    }

    fun setBgAlpha(bgAlpha: Float): MyPopupWindowK {
        mBgAlpha = bgAlpha
        return this
    }

    override fun dismiss() {
        popupWindow!!.dismiss()
    }

    private fun initData() {
        val activity = UIUtils.getCurActivity()
        val popupView = activity.layoutInflater.inflate(layoutRes, null)
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        height = popupView.measuredHeight
        ZLog.i(popupView.measuredWidth.toString() + "--->" + popupView.measuredHeight)
        popupWindow!!.isTouchable = true
        popupWindow!!.isOutsideTouchable = mOutsideTouchable
        popupWindow!!.setBackgroundDrawable(ColorDrawable())
        backgroundAlpha(mBgAlpha)
        popupWindow!!.setOnDismissListener {
            backgroundAlpha(1f)
            if (mBindViewAdapter != null) {
                mBindViewAdapter!!.onDestroy()
            }
        }
        if (mBindViewAdapter != null) {
            mBindViewAdapter!!.setBindView(this)
            mBindViewAdapter!!.setView(popupView)
        }
    }

    fun showAsDropDown(anchor: View?) {
        initData()
        popupWindow!!.showAsDropDown(anchor)
    }

    fun showAsDropDown(anchor: View?, xoffDp: Int, yoffDp: Int) {
        initData()
        popupWindow!!.showAsDropDown(anchor, UIUtils.dpToPx(xoffDp), UIUtils.dpToPx(yoffDp))
    }

    fun showAsDropUp(anchor: View, xoffDp: Int, yoffDp: Int) {
        initData()
        popupWindow!!.showAsDropDown(anchor, UIUtils.dpToPx(xoffDp), UIUtils.dpToPx(yoffDp) - height - anchor.measuredHeight)
    }

    fun showAtLocation(parent: View?, gravity: Int, xoffDp: Int, yoffDp: Int) {
        initData()
        popupWindow!!.showAtLocation(parent, gravity, UIUtils.dpToPx(xoffDp), UIUtils.dpToPx(yoffDp))
    }

    private fun backgroundAlpha(bgAlpha: Float) {
        val activity = UIUtils.getCurActivity()
        val lp = activity.window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        activity.window.attributes = lp
    }
    /**
     * 可以设置
     *
     * @param anchorView
     * @param gravity    可以试着 left|top 左对齐显示在上面 或者是right|top 右对齐显示在上面
     */
    /**
     * 默认左边对齐，显示在下面
     *
     * @param anchorView
     */
    @JvmOverloads
    fun showAutoLocation(anchorView: View, gravity: Int = -1) {
        showAutoLocation(anchorView, gravity, false)
    }

    fun showCenter(anchorView: View) {
        showAutoLocation(anchorView, -1, true)
    }

    /**
     * 中间对齐显示
     *
     * @param anchorView
     * @param gravity    只有TOP和Bottom有作用，其他没有作用
     */
    fun showCenter(anchorView: View, gravity: Int) {
        showAutoLocation(anchorView, gravity, true)
    }

    private fun showAutoLocation(anchorView: View, gravity: Int, isCenter: Boolean) {
        initData()
        val windowPos = calculatePopWindowPos(anchorView, gravity, isCenter)
        popupWindow!!.showAtLocation(anchorView, Gravity.TOP or Gravity.START, windowPos[0], windowPos[1])
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @return window显示的左上角的xOff, yOff坐标
     */
    private fun calculatePopWindowPos(anchorView: View, gravity: Int, isCenter: Boolean): IntArray {
        val contentView = popupWindow!!.contentView
        val windowPos = IntArray(2)
        val anchorLoc = IntArray(2)
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc)
        val anchorHeight = anchorView.height
        // 获取屏幕的高宽
        val screenHeight = ZScreenUtils.getScreenHeight()
        val screenWidth = ZScreenUtils.getScreenWidth()
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        // 计算contentView的高宽
        val windowHeight = contentView.measuredHeight
        val windowWidth = contentView.measuredWidth
        // 判断需要向上弹出还是向下弹出显示
        val isNeedShowUp = screenHeight - anchorLoc[1] - anchorHeight < windowHeight
        //是否指定显示在上面
        val isMustShow = gravity != -1 && gravity and Gravity.TOP == Gravity.TOP
        if (isNeedShowUp || isMustShow) {
            windowPos[1] = anchorLoc[1] - windowHeight - mTopOrBottomMargin
        } else {
            windowPos[1] = anchorLoc[1] + anchorHeight + mTopOrBottomMargin
        }
        windowPos[0] = anchorLoc[0]
        if (windowPos[0] > screenWidth - windowWidth - mLeftOrRightMargin) {
            windowPos[0] = screenWidth - windowWidth - mLeftOrRightMargin
        }
        if (isCenter) {
            windowPos[0] = windowPos[0] - windowWidth / 2 + anchorView.measuredWidth / 2
        }
        if (windowPos[0] < mLeftOrRightMargin) {
            windowPos[0] = mLeftOrRightMargin
        }
        if (gravity != -1) {
            if (gravity and Gravity.RIGHT == Gravity.RIGHT) {
                windowPos[0] -= windowWidth
                windowPos[0] += anchorView.measuredWidth
            }
        }
        return windowPos
    }

    companion object {
        fun init(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyPopupWindowK {
            val myPopupWindow = newInstances(layoutID)
            myPopupWindow.mBindViewAdapter = object : BindViewAdapter() {
                override fun initViews(paView: View?) {
                    binView.invoke(this)
                }
            }
            return myPopupWindow
        }

        fun init(bindViewAdapter: BindViewAdapter): MyPopupWindowK {
            val myPopupWindow = newInstances(bindViewAdapter.layoutID)
            myPopupWindow.mBindViewAdapter = bindViewAdapter
            return myPopupWindow
        }

        private fun newInstances(@LayoutRes resID: Int): MyPopupWindowK {
            val myPopupWindow = MyPopupWindowK()
            myPopupWindow.layoutRes = resID
            return myPopupWindow
        }
    }
}