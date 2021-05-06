package org.greenrobot.eventbus;

public class StickyBean {
    private String methodTag;
    private Class<?> aClass;

    public StickyBean(Class<?> aClass, String methodTag) {
        this.methodTag = methodTag;
        this.aClass = aClass;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public String getMethodTag() {
        return methodTag;
    }
}
