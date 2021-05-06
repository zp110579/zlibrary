package com.zee.listener;

public interface LetsGoListener {

    /**
     * 开始
     */
    void onStart();

    /**
     * 是否发现对应的Class
     *
     * @param className 发现对应的类的名字
     */
    void onFound(String className);

    /**
     * 错误信息
     *
     * @param msg
     */
    void onError(String msg);

    void onIntercept(String className, String name, String module, String keyWord);

    void onEnd();
}
