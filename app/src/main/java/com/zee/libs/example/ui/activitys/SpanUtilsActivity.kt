package com.zee.libs.example.ui.activitys

import android.graphics.Color
import com.zee.activity.BaseZActivity
import com.zee.extendobject.showToastShort
import com.zee.libs.example.R
import com.zee.span.MyImageSpan
import com.zee.span.Z1SpanBuild
import kotlinx.android.synthetic.main.activity_spanutils.*


class SpanUtilsActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_spanutils
    }

    override fun initViews() {
        Z1SpanBuild.get("例子：").append("粗体").setTextBold().appendSpace(1)
                .append("斜体").setTextItalic().appendSpace(4).append("间隔")
                .appendAndSetUrl("URL链接(可以点击)\n", "http://www.baidu.com").setUnderLine()
                .append("粗斜体\n").setTextBoldItalic()
                .append("字体大小25px\n").setTextSize(65).setClick {
                    showToastShort("字体大小25px")
                }
                .append("URL链接(黑色)(可以点击)\n").setUnderLine().setUrl("http://www.baidu.com").setTextColor(Color.BLACK)
                .append("字体大小15dp\n").setTextSize(15, true)
                .append("URL链接(红色)无下划线(可以点击)\n").setUrl("http://www.baidu.com").setTextColor(Color.RED).setUnderLine(false)
                .append("字体颜色蓝色\n").setTextColor(Color.BLUE).setTextSize(65).setClick {
                    showToastShort("字体颜色蓝色")
                }
                .append("背景黄色(可以点击)\n").setBgColor(Color.YELLOW).setClick { showToastShort("点击生效") }
                .append("下划线+换行效果(文字前面有点)").setUnderLine()
                .setNewlinePoint(20, Color.GRAY, 30)
                .append("相对前面字体大小的0.5f\n").setRelativeTextSizeScale(0.5f)
                .append("中间删除线\n").setCenterLine()
                .append("相对前面字体大小的1.5f\n").setRelativeTextSizeScale(1.5f)
                .append("中间线+下划线\n").setCenterLine().setUnderLine()
                .append("文字偏上显示").setTextShowTop()
                .append("中间位置")
                .append("文字偏下显示\n").setTextShowBottom()
                .append("换行效果(文字前面有竖线)\n")
                .setQuoteColor(Color.RED) //                .setQuoteGapWidth(10)
                .setQuoteStripeWidth(20)
                .setLineMarginOther(60)
                .setLineMarginCurrent(30)
                .append("设置点击群(可以点击)\n").setTextBold().setTextColor(Color.BLUE).setClick {
                    showToastShort("设置点击群点击sdfadfasdf2")
                }.setTextColor(Color.RED).setUnderLine().setTextItalic()
                .append("BlurOuter\n").setBlurOuter()
                .append("BlurInner\n").setBlurInner()
                .append("BlurSolid\n").setBlurSolid()
                .append("X伸缩1.5f\n").setScaleXSize(1.5f)
                .append("X伸缩0.5f\n").setScaleXSize(0.5f)
                .append("serif字体\n").setTextFamily("serif")
                .append("原始大小图片")
                .appendImage(MyImageSpan(R.drawable.ic_arrow_back))
                .append("\n50px宽度的图片")
                .appendImage(MyImageSpan(R.drawable.ic_arrow_back).setWidth(50))
                .append("\n250px宽度的图片且设置成红色")
                .setTextAlignRight()
                .appendImage(MyImageSpan(R.drawable.ic_arrow_back).setHeight(250).setColor(Color.RED)).build(tv_contents)
        //如果需要点击事件或者设置了并需要点击效果，需要在build方法传入该TextView
    }
}
