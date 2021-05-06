package com.zee.route;

import com.zee.listener.LetsGoListener;
import com.zee.log.ZLog;

import com.zee.interf.IRouteInterceptor;
import com.zee.bean.InterceptorBean;
import com.zee.bean.RouteBean;

public class InterceptorInfo {
    private InterceptorBean interceptorBean;
    private IRouteInterceptor mIRouteInterceptor;

    public InterceptorInfo(InterceptorBean interceptorBean) {
        this.interceptorBean = interceptorBean;
        Class<?> clazz = interceptorBean.getSubscriberClass();
        if (clazz != null) {
            try {
                Object obj = clazz.getConstructor().newInstance();
                if (obj instanceof IRouteInterceptor) {
                    mIRouteInterceptor = (IRouteInterceptor) obj;
                }
            } catch (Exception e) {
                ZLog.exception(e, "IRouteInterceptor");
            }
        }
    }

    public int getPriority() {
        return interceptorBean.getPriority();
    }

    public boolean intercept(RouteBean tempGoalBean, BaseInfoBean rActivityInfoBean, LetsGoListener letsGoListener) {
        //拦截器的name
        String interceptorName = interceptorBean.getName().trim();
        //拦截器的module
        String interceptorModule = interceptorBean.getModule().trim();
        //拦截器的关键字
        String interceptorKeyWord = interceptorBean.getKeywords().trim();

        if (interceptorModule.length() > 0) {
            //如果拦截器指定的module
            if (!interceptorModule.equals(tempGoalBean.getModule())) {//如果不一样，那就不拦截
                return false;
            }
        }

        if (interceptorName.length() > 0) {
            //如何拦截器指定的name，那就只能拦截指定的name
            if (!interceptorName.equals(tempGoalBean.getName())) {
                return false;
            }
        }

        if (interceptorKeyWord.length() > 0) {
            if (!tempGoalBean.getKeywords().contains(interceptorKeyWord)) {
                //如何拦截器的关键字在当前的Roter关键字中,那也不拦截
                return false;
            }
        }
        boolean isIntercept = mIRouteInterceptor.intercept(tempGoalBean.getRouteClass().getName(), tempGoalBean.getName(), tempGoalBean.getModule(), tempGoalBean.getKeywords(), rActivityInfoBean.getBundle());
        if (isIntercept && letsGoListener != null) {
            letsGoListener.onIntercept(mIRouteInterceptor.getClass().getName(), interceptorName, interceptorModule, interceptorKeyWord);
        }
        return isIntercept;
    }

    @Override
    public String toString() {
        return mIRouteInterceptor.getClass().getName() + " priority:" + interceptorBean.getPriority();
    }
}
