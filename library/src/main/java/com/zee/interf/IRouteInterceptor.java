package com.zee.interf;

import android.os.Bundle;

/**
 * @author Administrator
 */
public interface IRouteInterceptor {
    /**
     * 拦截信息
     *
     * @param className
     * @param name
     * @param module
     * @param keyWord
     * @param bundle
     * @return
     */
    boolean intercept(String className, String name, String module, String keyWord, Bundle bundle);
}
