package com.zee.libs.example.ui.activitys

import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.widget.Toast
import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_marquee.*
import java.lang.String
import java.util.*

/**
 *created by zee on 2021/7/16.
 *
 */
class MarqueeActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_marquee
    }

    override fun initViews() {
        val list: MutableList<CharSequence> = ArrayList()
        val ss1 = SpannableString("1、MarqueeView开源项目")
        ss1.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorPrimary)), 2, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        list.add(ss1)
        val ss2 = SpannableString("2、GitHub：sunfusheng")
        ss2.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorAccent)), 9, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        list.add(ss2)
        val ss3 = SpannableString("3、个人博客：sunfusheng.com")
        ss3.setSpan(URLSpan("http://sunfusheng.com/"), 7, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        list.add(ss3)
        list.add("4、新浪微博：@孙福生微博")
        //set Custom font
        //set Custom font
        marqueeView.setTypeface(ResourcesCompat.getFont(this, R.font.huawenxinwei))

        marqueeView.startWithList(list)
        marqueeView.setOnItemClickListener { position, textView -> Toast.makeText(this, textView.getText().toString() + "", Toast.LENGTH_SHORT).show() }

//        marqueeView1.startWithText(getString(R.string.marquee_texts), R.anim.anim_top_in, R.anim.anim_bottom_out);

//        marqueeView1.startWithText(getString(R.string.marquee_texts), R.anim.anim_top_in, R.anim.anim_bottom_out);
        marqueeView1.setOnItemClickListener { position, textView -> Toast.makeText(this, String.valueOf(position).toString() + ". " + textView.getText(), Toast.LENGTH_SHORT).show() }

        marqueeView2.startWithText("增加了新功能设置自定义的Model数据类型")

        marqueeView3.startWithText("增加了新功能设置自定义的Model数据类型")
        marqueeView3.setOnItemClickListener { position, textView ->
//            val model = marqueeView3.getMessages().get(position) as CharSequence
//            Toast.makeText(getContext(), model, Toast.LENGTH_SHORT).show()
        }

        val models: MutableList<CharSequence> = ArrayList()
        models.add("增加了新功能：\n设置自定义的Model数据类型")
        models.add("GitHub：sunfusheng\n新浪微博：@孙福生微博")
        models.add("MarqueeView开源项目\n个人博客：sunfusheng.com")
        marqueeView4.startWithList(models)
        marqueeView4.setOnItemClickListener { position, textView -> }

    }
}