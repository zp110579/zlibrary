package com.zee.bean;


public class RouteBean {
    private Class<?> mRouteClass;

    private String name;
    private String module;
    private String keywords;
    /**
     * 0:未定，1:activity,2:fragment_v4
     */
    private int type;

    public RouteBean(int type, Class<?> routeClass) {
        this.type=type;
        this.mRouteClass=routeClass;
    }
    public RouteBean(int type, Class<?> routeClass, String name, String module, String keywords) {
        this.mRouteClass = routeClass;
        this.name = name;
        this.module = module.trim();
        this.keywords = keywords.trim();
        this.type = type;
    }

    public Class<?> getRouteClass() {
        return mRouteClass;
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

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RouteBean{" +
                "mRouteClass=" + mRouteClass +
                ", name='" + name + '\'' +
                ", module='" + module + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
