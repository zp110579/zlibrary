package com.zee.route;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zee.fragment.CallBackFragment;
import com.zee.listener.LetsGoListener;
import com.zee.log.ZLog;
import com.zee.utils.ZLibrary;

import com.zee.bean.RouteBean;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class PActivityManagerP extends PRouterManager {
    static void openActivityClass(PActivityInfoBean activityInfo, LetsGoListener letsGoListener) {
        RouteBean goalBean = getRouteBean(activityInfo.getGoalClass());

        activityInfo.withBundle(activityInfo.getBundle());
        openBean(activityInfo, letsGoListener, goalBean);
    }

    static void openActivity(PActivityInfoBean activityInfo, LetsGoListener letsGoListener) {
        RouteBean goalBean = getRouteBean(activityInfo);
        openBean(activityInfo, letsGoListener, goalBean);
    }

    private static void openBean(PActivityInfoBean activityInfo, LetsGoListener letsGoListener, RouteBean goalBean) {
        if (letsGoListener != null) {
            letsGoListener.onStart();
        }
        if (goalBean != null) {
            if (letsGoListener != null) {
                letsGoListener.onFound(goalBean.getRouteClass().getName());
            }
            if (goalBean.getType() != activityInfo.getType()) {
                if (letsGoListener != null) {
                    letsGoListener.onError(goalBean.getRouteClass().getName() + ":不是Activity类型");
                    letsGoListener.onEnd();
                }
                return;
            }

            if (!isIntercept(goalBean, activityInfo, letsGoListener)) {
                Class<?> tempClass = goalBean.getRouteClass();
                if (tempClass != null) {
                    Activity activity = ZLibrary.getInstance().getCurrentActivity();
                    final Intent intent = new Intent(activity, tempClass);
                    final Bundle bundle = activityInfo.getBundle();

                    if (activityInfo.getRequestCode() != null) {
                        //说明有返回值
                        try {
                            if (activityInfo.getOnOpenActivityResultListener() != null) {
                                //如果有监听，那就走监听方法，如果没有
                                CallBackFragment callBackFragment = CallBackFragment.newInstant(activityInfo.getRequestCode(), tempClass);
                                callBackFragment.prepareCallBack(activityInfo.mOnOpenActivityResultListener);
                            } else {
                                activity.startActivityForResult(intent, activityInfo.getRequestCode());
                            }
                        } catch (Exception e) {
                            ZLog.exception(e);
                        }
                    } else {
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        activity.startActivity(intent);
                    }
                }
            }
        } else {
            if (letsGoListener != null) {
                letsGoListener.onError("确认【Name和 module 能对上】 ");
            }
        }
        if (letsGoListener != null) {
            letsGoListener.onEnd();
        }
    }
}
