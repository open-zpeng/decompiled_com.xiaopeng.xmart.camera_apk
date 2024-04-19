package com.aliyun.clientinforeport.util;

import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes.dex */
public class RLog {
    private static boolean openLog = true;

    public static void setOpen(boolean z) {
        openLog = z;
    }

    public static boolean isOpen() {
        return openLog;
    }

    public static void v(String str, String str2) {
        if (!openLog || TextUtils.isEmpty(str2)) {
            return;
        }
        Log.v(str, "" + str2);
    }

    public static void d(String str, String str2) {
        if (!openLog || TextUtils.isEmpty(str2)) {
            return;
        }
        Log.d(str, "" + str2);
    }

    public static void i(String str, String str2) {
        if (!openLog || TextUtils.isEmpty(str2)) {
            return;
        }
        Log.i(str, "" + str2);
    }

    public static void w(String str, String str2) {
        if (!openLog || TextUtils.isEmpty(str2)) {
            return;
        }
        Log.w(str, "" + str2);
    }

    public static void e(String str, String str2) {
        if (!openLog || TextUtils.isEmpty(str2)) {
            return;
        }
        Log.e(str, "" + str2);
    }
}
