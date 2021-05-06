package com.zee.utils;

import java.util.List;

/**
 * @author Administrator
 */

public class ZListUtils {

    public static boolean isNoEmpty(List list) {
        if (list == null || list.size() < 1) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(List list) {
        return !isNoEmpty(list);
    }


    public static boolean isNoEmpty(String[] paStrings) {
        if (paStrings == null || paStrings.length < 1) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String[] paStrings) {
        return !isNoEmpty(paStrings);
    }
}
