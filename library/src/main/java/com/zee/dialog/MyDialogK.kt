
package com.zee.dialog

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.*
import com.zee.bean.IDismissListener
import com.zee.libs.R
import com.zee.utils.UIUtils
import com.zee.utils.ZEventBusUtils
import com.zee.utils.ZScreenUtils

class MyDialogK : DialogFragment(), IDismissListener {
    private var mLeftAndRightMargin = 0//左右边距
    private var mTopOrBottomMargin = 0 //上下的边距
    private var width = 0 //宽度
    private var height = 0 //高度
    private var dimAmount = 0.6f //灰度深浅
    private var gravity = Gravity.CENTER //显示的位置
    private var outCancel = true //是否点击外部取消


    private var mTheme = R.style.myDialogStyle // dialog主题
    private var isFullScreen = false

    //只有显示在上，下2面才会有效果
    @StyleRes
    private var animStyle = 0

    @LayoutRes
    private var layoutId = 0
    private var mBindViewAdapter: BindViewAdapter? = null
    private fun setAdapter(adapter: BindViewAdapter): MyDialogK {
        mBindViewAdapter = adapter
        mBindViewAdapter!!.setBindView(this)
        return this
    }

    @Deprecated("")
    fun setBindViewAdapter(paBindViewAdapter: BindViewAdapter?): MyDialogK {
        mBindViewAdapter = paBindViewAdapter
        mBindViewAdapter!!.setBindView(this)
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager
        setStyle(STYLE_NO_TITLE, mTheme)
        val arguments = arguments
        if (arguments != null) {
            gravity = arguments.getInt("type", 0)
            layoutId = arguments.getInt("layout", 0)
        }

        //恢复保存的数据
        if (savedInstanceState != null) {
            mLeftAndRightMargin = savedInstanceState.getInt(LEFTRIGHTMARGIN)
            mTopOrBottomMargin = savedInstanceState.getInt(TOPBOTTOMMARGIN)
            width = savedInstanceState.getInt(WIDTH)
            height = savedInstanceState.getInt(HEIGHT)
            dimAmount = savedInstanceState.getFloat(DIM)
            gravity = savedInstanceState.getInt(GRAVITY)
            outCancel = savedInstanceState.getBoolean(CANCEL)
            mTheme = savedInstanceState.getInt(THEME)
            animStyle = savedInstanceState.getInt(ANIM)
            layoutId = savedInstanceState.getInt(LAYOUT)
            mBindViewAdapter = savedInstanceState.getParcelable<Parcelable>(ADAPTER) as BindViewAdapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        check()
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        check()
        mBindViewAdapter!!.setView(view)
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(LEFTRIGHTMARGIN, mLeftAndRightMargin)
        outState.putInt(TOPBOTTOMMARGIN, mTopOrBottomMargin)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putFloat(DIM, dimAmount)
        outState.putInt(GRAVITY, gravity)
        outState.putBoolean(CANCEL, outCancel)
        outState.putInt(THEME, mTheme)
        outState.putInt(ANIM, animStyle)
        outState.putInt(LAYOUT, layoutId)
        try {
            outState.putParcelable(ADAPTER, mBindViewAdapter)
        } catch (e: Exception) {
//            e.printStackTrace();
        }
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    private fun initParams() {
        val window = dialog.window
        if (window != null) {
            val lp = window.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount
            if (gravity != 0) {
                lp.gravity = gravity
            }
            when (gravity) {
                Gravity.TOP -> if (animStyle == 0) {
                    animStyle = R.style.zxDialogStyle_TopAnimation
                }
                Gravity.BOTTOM -> if (animStyle == 0) {
                    animStyle = R.style.zxDialogStyle_BottomAnimation
                }
                else -> {
                }
            }

            //设置dialog宽度
            if (isFullScreen) {
                lp.width = ZScreenUtils.getScreenWidth()
                lp.height = ZScreenUtils.getScreenHeight()
                lp.y = 0
            } else {
                val tempW = UIUtils.dpToPx(width)
                if (tempW == 0) {
                    lp.width = ZScreenUtils.getScreenWidth() - 2 * UIUtils.dpToPx(mLeftAndRightMargin)
                } else if (tempW == -1) {
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                } else {
                    lp.width = tempW
                }
                //设置dialog高度
                val tempH = UIUtils.dpToPx(height)
                if (tempH == 0) {
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                } else {
                    lp.height = tempH
                }
                if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
                    lp.y = UIUtils.dpToPx(mTopOrBottomMargin)
                }
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle)
            window.attributes = lp
        }
        isCancelable = outCancel
    }

    fun setMarginLeftAndRight(margin: Int): MyDialogK {
        mLeftAndRightMargin = margin
        return this
    }

    fun setMarginTopOrBottom(margin: Int): MyDialogK {
        mTopOrBottomMargin = margin
        return this
    }

    fun setSize(width: Int, height: Int): MyDialogK {
        this.width = width
        this.height = height
        return this
    }

    fun setFullScreen(): MyDialogK {
        isFullScreen = true
        return this
    }

    fun setDimAmount(dimAmount: Float): MyDialogK {
        this.dimAmount = dimAmount
        return this
    }

    fun setAnimStyle(animStyle: Int): MyDialogK {
        this.animStyle = animStyle
        return this
    }

    fun setOutCancel(outCancel: Boolean): MyDialogK {
        this.outCancel = outCancel
        return this
    }

    fun show(manager: FragmentManager) {
        val ft = manager.beginTransaction()
        if (this.isAdded) {
            ft.remove(this).commit()
        }
        ft.add(this, System.currentTimeMillis().toString())
        ft.commitAllowingStateLoss()
    }

    /**
     * 自动获得当前的Acitivity并显示出来
     *
     * @return
     */
    @Deprecated("")
    fun showCurActivity(): MyDialogK {
        UIUtils.showDialog(this)
        return this
    }

    /**
     * 自动获得当前的Acitivity并显示出来
     *
     * @return
     */
    fun show(): MyDialogK {
        UIUtils.showDialog(this)
        return this
    }

    private fun check() {
        if (mBindViewAdapter == null) {
            Log.e("MyDialog", "BindAdapter is null")
            throw NullPointerException("BindAdapter is null")
        }
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        ZEventBusUtils.unregister(this)
        super.onDestroy()
        mBindViewAdapter?.onDestroy()
    }

    companion object {
        private const val LEFTRIGHTMARGIN = "leftRightMargin"
        private const val TOPBOTTOMMARGIN = "topBottomMargin"
        private const val WIDTH = "width"
        private const val HEIGHT = "height"
        private const val DIM = "dim_amount"
        private const val GRAVITY = "gravity"
        private const val CANCEL = "out_cancel"
        private const val THEME = "theme"
        private const val ANIM = "anim_style"
        private const val LAYOUT = "layout_id"
        private const val ADAPTER = "adapter"

        fun init(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyDialogK {
            return init(object : BindViewAdapter(layoutID) {
                override fun initViews(paView: View) {
                    binView.invoke(this)
                }
            })
        }

        fun initTop(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyDialogK {
            return initTop(object : BindViewAdapter(layoutID) {
                override fun initViews(paView: View) {
                    binView.invoke(this)
                }
            })
        }

        fun initBottom(layoutID: Int, binView: BindViewAdapter.() -> Unit): MyDialogK {
            return initBottom(object : BindViewAdapter(layoutID) {
                override fun initViews(paView: View) {
                    binView.invoke(this)
                }
            })
        }

        fun init(bindViewAdapter: BindViewAdapter): MyDialogK {
            return init(bindViewAdapter.layoutID, bindViewAdapter)
        }

        fun initTop(bindViewAdapter: BindViewAdapter): MyDialogK {
            val myDialog = newInstance(Gravity.TOP, bindViewAdapter.layoutID)
            myDialog.setAdapter(bindViewAdapter)
            return myDialog
        }

        fun initBottom(bindViewAdapter: BindViewAdapter): MyDialogK {
            val myDialog = newInstance(Gravity.BOTTOM, bindViewAdapter.layoutID)
            myDialog.setAdapter(bindViewAdapter)
            return myDialog
        }

        fun init(@LayoutRes layoutID: Int, dialogAdapter: BindViewAdapter): MyDialogK {
            val myDialog = newInstance(0, layoutID)
            myDialog.setAdapter(dialogAdapter)
            dialogAdapter.setBindView(myDialog)


            return myDialog
        }

        fun newInstance(type: Int, layoutID: Int): MyDialogK {
            val args = Bundle()
            args.putInt("type", type)
            args.putInt("layout", layoutID)
            val fragment = MyDialogK()
            fragment.arguments = args
            return fragment
        }
    }
}