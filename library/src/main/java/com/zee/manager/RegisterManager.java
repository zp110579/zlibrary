package com.zee.manager;

import android.app.Activity;

import com.zee.log.ZLog;
import com.zee.utils.ViewServer;
import com.zee.utils.ZConfig;
import com.zee.utils.ZLibrary;

public class RegisterManager {

    public static void register(Object subscriber) {
        isCanAddViewService(subscriber, 0);
    }

    public static void unregister(Object subscriber) {
        isCanAddViewService(subscriber, 1);
    }

    private static void isCanAddViewService(Object object, int type) {
        if (ZLog.DEBUG) {
            ZLibrary zLibrary = ZLibrary.getInstance();
            if (zLibrary != null) {
                ZConfig zConfig = zLibrary.getZConfig();
                boolean isAddView = zConfig.isAddViewServer();
                if (isAddView && (object instanceof Activity)) {
                    Activity activity = (Activity) object;
                    if (type == 0) {
                        ViewServer.get(activity).addWindow(activity);
                    } else {
                        ViewServer.get(activity).removeWindow(activity);
                    }
                }
            }
        }
    }
}
