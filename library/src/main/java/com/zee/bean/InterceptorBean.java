package com.zee.bean;

/**
 * Created by Administrator on 2018/10/30 0030.
 */

public class InterceptorBean {
    private Class subscriberClass;

    private String name;
    private String module;
    private String keywords;
    private int priority;

    public InterceptorBean(Class<?> subscriberClass, String name, String module, String keywords, int priority) {
        this.subscriberClass = subscriberClass;
        this.name = name;
        this.module = module;
        this.keywords = keywords;
        this.priority = priority;
    }

    public Class getSubscriberClass() {
        return subscriberClass;
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    public String getKeywords() {
        return keywords;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "InterceptorBean{" +
                "subscriberClass=" + subscriberClass +
                ", priority=" + priority +
                '}';
    }
}
