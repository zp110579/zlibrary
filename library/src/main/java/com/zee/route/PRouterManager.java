package com.zee.route;

import android.text.TextUtils;

import com.zee.listener.LetsGoListener;
import com.zee.log.ZLog;
import com.zee.utils.ZListUtils;

import com.zee.interf.IRoute;
import com.zee.bean.InterceptorBean;
import com.zee.bean.RouteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class PRouterManager {
    private static List<IRoute> sIRouteArrayList = new ArrayList<>();
    private static List<InterceptorInfo> mInterceptorList = new ArrayList<>();

    public static void addRoteBean(IRoute route) {
        if (route != null) {
            sIRouteArrayList.add(route);
            List<InterceptorBean> list = route.getInterceptors();
            if (!ZListUtils.isEmpty(list)) {
                for (InterceptorBean interceptorBean : list) {
                    int size = mInterceptorList.size();
                    if (size > 0) {
                        boolean isadd = false;
                        for (int i = 0; i < mInterceptorList.size(); i++) {
                            if (interceptorBean.getPriority() > mInterceptorList.get(i).getPriority()) {
                                mInterceptorList.add(i, new InterceptorInfo(interceptorBean));
                                isadd = true;
                                break;
                            }
                        }
                        if (!isadd) {
                            mInterceptorList.add(new InterceptorInfo(interceptorBean));
                        }
                    } else {
                        mInterceptorList.add(new InterceptorInfo(interceptorBean));
                    }
                }
            }
        }
    }

    static RouteBean getRouteBean(BaseInfoBean bean) {
        String name = bean.getName();
        StringBuilder builder = new StringBuilder(name);
        if (!TextUtils.isEmpty(bean.getModule())) {
            builder.append("$$");
            builder.append(bean.getModule());
        }
        List<RouteBean> routeBeanList = new ArrayList<>(5);
        for (IRoute iRoute : sIRouteArrayList) {
            RouteBean routeBean = iRoute.getRouteBean(builder.toString());
            if (routeBean != null) {
                routeBeanList.add(routeBean);
            }
        }

        if (routeBeanList.size() == 1) {
            return routeBeanList.get(0);
        } else if (routeBeanList.size() > 1) {
            printInfo(routeBeanList);
        }
        return null;
    }

    static RouteBean getRouteBean(Class<?> classZ) {
        List<RouteBean> routeBeanList = new ArrayList<>(8);
        for (IRoute iRoute : sIRouteArrayList) {
            RouteBean routeBean = iRoute.getRouteBean(classZ);
            if (routeBean != null) {
                routeBeanList.add(routeBean);
            }
        }

        if (routeBeanList.size() == 1) {
            return routeBeanList.get(0);
        } else if (routeBeanList.size() > 1) {
            printInfo(routeBeanList);
        }
        return new RouteBean(1, classZ);
    }

    private static void printInfo(List<RouteBean> routeBeanList) {
        ZLog.i("此条件有:" + routeBeanList.size() + "个符合:");
        StringBuilder builder = new StringBuilder();
        for (RouteBean routeBean : routeBeanList) {
            builder.append("\n");
            builder.append(routeBean);
        }
        ZLog.i(builder);
    }


    /**
     * 是否拦截
     *
     * @param tempGoalBean
     * @return
     */
    static boolean isIntercept(RouteBean tempGoalBean, BaseInfoBean rActivityInfoBean, LetsGoListener letsGoListener) {
        if (mInterceptorList.size() > 0) {
            for (InterceptorInfo iRouteInterceptor : mInterceptorList) {
                if (iRouteInterceptor.intercept(tempGoalBean, rActivityInfoBean, letsGoListener)) {
                    return true;
                }
            }
        }
        return false;
    }
}
