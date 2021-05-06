package com.zee.utils;

import com.zee.route.PActivityInfoBean;
import com.zee.route.ZRouter;

public class Z1RouterUtils {

    private static ZRouter getZRouter() {
        return ZRouter.getInstance();
    }


    public static PActivityInfoBean startActivity(Class<?> classZ) {
        return getZRouter().startActivity(classZ);
    }

    public static PActivityInfoBean startActivity(String activityName) {
        return getZRouter().startActivity(activityName);
    }

    public static PActivityInfoBean startActivity(String activityName, String module) {
        return getZRouter().startActivity(activityName, module);
    }
}
