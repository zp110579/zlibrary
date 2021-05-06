import android.app.AlertDialog
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.lzf.easyfloat.permission.PermissionUtils
import com.zee.bean.IDismissListener
import com.zee.dialog.BindViewAdapter
import com.zee.utils.UIUtils

/**
 *created by zee on 2020/11/30.
 *浮动的窗口
 */
class MyFloatWindow : IDismissListener {

    private var mBindViewAdapter: BindViewAdapter
    private var mTag: String? = null

    //默认是吸附左右2边
    private var mSidePattern: SidePattern = SidePattern.RESULT_HORIZONTAL

    companion object {

        fun init(bindViewAdapter: BindViewAdapter, tag: String? = null): MyFloatWindow {
            return MyFloatWindow(bindViewAdapter, tag)
        }

        fun show(tag: String? = null) {
            EasyFloat.show(UIUtils.getCurActivity(), tag)
        }

        fun hide(tag: String? = null) {
            EasyFloat.hide(UIUtils.getCurActivity(), tag)
        }

        fun dismiss(tag: String? = null) {
            EasyFloat.dismiss(UIUtils.getCurActivity(), tag)
        }
    }

    private constructor(bindViewAdapter: BindViewAdapter, tag: String?) {
        this.mBindViewAdapter = bindViewAdapter
        this.mTag = tag
    }

    override fun dismiss() {
        mBindViewAdapter.apply {
            onDestroy()
        }

        EasyFloat.dismiss(UIUtils.getCurActivity(), mTag)
    }

    /**
     * 设置吸附的方向
     */
    fun setShowAdsorbWay(sidePattern: SidePattern): MyFloatWindow {
        mSidePattern = sidePattern
        return this
    }

    /**
     * 显示在当前Activity上面
     */
    fun showCurActivity(gravity: Int, offsetX: Int = 0, offsetY: Int = 0) {
        EasyFloat.with(UIUtils.getCurActivity()).setGravity(gravity, offsetX, offsetY)
                .setLayout(mBindViewAdapter.layoutID, OnInvokeView { mBindViewAdapter.setView(it) })
                .setSidePattern(mSidePattern)
                .show()
    }

    /**
     * 一直显示
     */
    fun showAlways(gravity: Int, offsetX: Int = 0, offsetY: Int = 0) {
        showDialog(gravity, offsetX, offsetY, ShowPattern.ALL_TIME)
    }

    /**
     * APP显示的时候显示
     */
    fun showOnForeground(gravity: Int, offsetX: Int = 0, offsetY: Int = 0) {
        showDialog(gravity, offsetX, offsetY, ShowPattern.FOREGROUND)
    }

    /**
     * APP进入后台的时候显示出来
     */
    fun showOnBackground(gravity: Int, offsetX: Int = 0, offsetY: Int = 0) {
        showDialog(gravity, offsetX, offsetY, ShowPattern.BACKGROUND)
    }

    private fun showDialog(gravity: Int, offsetX: Int = 0, offsetY: Int = 0, showPattern: ShowPattern) {
        if (PermissionUtils.checkPermission(UIUtils.getCurActivity())) {

            EasyFloat.with(UIUtils.getCurActivity())
                    .setTag(mTag)
                    .setShowPattern(showPattern)
                    .setGravity(gravity, offsetX, offsetY)
                    .setAppFloatAnimator(null)
                    .setSidePattern(mSidePattern)
                    .setLayout(mBindViewAdapter.layoutID, OnInvokeView { mBindViewAdapter.setView(it) })
                    .show()
        } else {
            AlertDialog.Builder(UIUtils.getCurActivity())
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")

                    .setPositiveButton("去开启") { _, _ ->
                        EasyFloat.with(UIUtils.getCurActivity())
                                .setTag(mTag)
                                .setShowPattern(showPattern)
                                .setGravity(gravity, offsetX, offsetY)
                                .setAppFloatAnimator(null)
                                .setSidePattern(mSidePattern)
                                .setLayout(mBindViewAdapter.layoutID, OnInvokeView { mBindViewAdapter.setView(it) })
                                .show()
                    }
                    .setNegativeButton("取消") { _, _ -> }
                    .show()
        }
    }

}