package com.zee.interf;

import com.zee.bean.InterceptorBean;
import com.zee.bean.RouteBean;

import java.util.ArrayList;

public interface IRoute {

    RouteBean getRouteBean(String name);

    RouteBean getRouteBean(Class<?> classz);

    ArrayList<InterceptorBean> getInterceptors();
}
