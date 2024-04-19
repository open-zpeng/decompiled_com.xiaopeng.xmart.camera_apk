package com.xiaopeng.drivingimageassist.utils;

import android.text.TextUtils;
import java.text.DecimalFormat;
/* loaded from: classes.dex */
public class NumberUtils {
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("^[0-9]+(.[0-9]+)?$");
    }

    public static String getLengthFormat(int meter) {
        if (meter < 1000) {
            return meter + "米";
        }
        return format(meter / 1000.0f) + "公里";
    }

    private static String format(double number) {
        return new DecimalFormat("##0.0").format(number);
    }

    public static long getLong(String src, long defaultValue) {
        try {
            return Long.valueOf(src).longValue();
        } catch (Throwable unused) {
            return defaultValue;
        }
    }

    public static int getInteger(String src, int defaultValue) {
        try {
            return Integer.valueOf(src).intValue();
        } catch (Throwable unused) {
            return defaultValue;
        }
    }

    public static int pause(String str, int defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return defaultValue;
        }
    }

    public static float pauseFloat(String str, float defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception unused) {
            return defaultValue;
        }
    }

    public static long pauseLong(String str, long defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception unused) {
            return defaultValue;
        }
    }
}
