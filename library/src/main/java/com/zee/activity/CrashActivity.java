package com.zee.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.zee.libs.R;
import com.zee.log.ZLog;
import com.zee.utils.FileUtil;
import com.zee.utils.UIUtils;

import org.greenrobot.eventbus.SubscribeTag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 崩溃详情页面展示
 * @author Administrator
 */
public class CrashActivity extends FragmentActivity {
    TextView textView;
    boolean isAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zv_activity_crash);


        final String text = getIntent().getStringExtra("txt");

        textView = findViewById(R.id.textView);
        showCrashMessage(text);
        final TextView clearTV = findViewById(R.id.id_zv_crash_clear);
        clearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                if (isAll) {
                    final String crashContent = FileUtil.getString(ZLog.getCrashExceptionFilePath());
                    showCrashMessage(crashContent);
                    clearTV.setText("当前");
                } else {
                    clearTV.setText("全部");
                    showCrashMessage(text);
                }

                isAll = !isAll;

//                UIUtils.runOnAsyncThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        File file = new File(ZLog.getCrashExceptionFilePath());
//                        if (file.delete()) {
//                            UIUtils.showToastShort("清除成功");
//                        } else {
//                            UIUtils.showToastShort("清除失败");
//                        }
//                    }
//                });
            }
        });
//        UIUtils.runOnAsyncThread(new Runnable() {
//            public void run() {
//
//            }
//        }, 1000);
    }

    private void showCrashMessage(final String crashContent) {
//        Spannable spannable = Spannable.Factory.getInstance().newSpannable(crashContent);
//        String packageName = getPackageName();
//        spannable = addNewSpannable(spannable, crashContent, packageName, Color.parseColor("#ff0000BB"), 16);
//        spannable = addNewSpannable(spannable, crashContent, "Exception", Color.parseColor("#f41fe7"), 18);
//
//        List<Class> activitiesClass = getActivitiesClass(UIUtils.getApplication(), getPackageName(), null);
//        if (activitiesClass != null && activitiesClass.size() > 0) {
//            for (int i = 0; i < activitiesClass.size(); i++) {
//                spannable = addNewSpannable(spannable, crashContent, activitiesClass.get(i).getSimpleName(), Color.parseColor("#FFFF0000"), 16);
//            }
//        }

        if (textView != null) {
            textView.setText(crashContent);
        }

    }


    public final static List<Class> getActivitiesClass(Context context, String packageName, List<Class> excludeList) {
        List<Class> returnClassList = new ArrayList<Class>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo ai : packageInfo.activities) {
                    Class c;
                    try {
                        c = Class.forName(ai.name);
                        if (Activity.class.isAssignableFrom(c)) {
                            returnClassList.add(c);
                        }
                    } catch (ClassNotFoundException e) {
                    }
                }
                if (excludeList != null) {
                    returnClassList.removeAll(excludeList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnClassList;
    }

    public Spannable addNewSpannable(Spannable spannable, String allContent, String matchContent, @ColorInt int foregroundColor, int textSize) {
        Pattern pattern = Pattern.compile(Pattern.quote(matchContent));
        Matcher matcher = pattern.matcher(allContent);
        while (matcher.find()) {
            int start = matcher.start();
            if (start >= 0) {
                int end = start + matchContent.length();
                if (textSize > 0) {
                    spannable.setSpan(new AbsoluteSizeSpan(UIUtils.spToPx(textSize)), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                spannable.setSpan(new ForegroundColorSpan(foregroundColor), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        return spannable;
    }
}
