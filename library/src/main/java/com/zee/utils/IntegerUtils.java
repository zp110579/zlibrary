package com.zee.utils;

public class IntegerUtils {

    public static int value(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defaultValue;
    }
}
