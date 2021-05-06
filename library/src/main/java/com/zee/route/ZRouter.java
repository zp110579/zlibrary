package com.zee.route;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class ZRouter {
    private static ZRouter instance;

    public static ZRouter getInstance() {

        if (instance == null) {
            synchronized (ZRouter.class) {
                if (instance == null) {
                    instance = new ZRouter();
                }
            }
        }
        return instance;
    }
    public PActivityInfoBean startActivity(Class<?> classZ) {
        return new PActivityInfoBean(classZ);
    }

    public PActivityInfoBean startActivity(String name) {
        return startActivity(name, "");
    }

    public PActivityInfoBean startActivity(String name, String module) {
        return new PActivityInfoBean(name, module);
    }

    public Rv4FragmentInfoBean getFragment(Class<?> classZ) {
        return new Rv4FragmentInfoBean(classZ);
    }

    public Rv4FragmentInfoBean getFragment(String name) {
        return getFragment(name, "");
    }

    public Rv4FragmentInfoBean getFragment(String name, String module) {
        return new Rv4FragmentInfoBean(name, module);
    }

}
