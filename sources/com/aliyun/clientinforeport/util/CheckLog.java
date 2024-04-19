package com.aliyun.clientinforeport.util;

import java.util.List;
/* loaded from: classes.dex */
public class CheckLog {
    private static final String TAG = "CheckLog";

    public static void empty(String str) {
        error("ERROR: " + str + " is not set or empty");
    }

    public static void outRange(String str, String str2, List<String> list) {
        error("ERROR: " + str + " set : " + str2 + " , should be one of " + CollectionUtils.toString(list));
    }

    public static void space(String str, String str2) {
        error("ERROR: " + str + " set : \"" + str2 + "\" , contains Space , should be urlEncoded ");
    }

    public static void ok(String str, String str2) {
        RLog.i(TAG, str + " : " + str2);
    }

    public static void error(String str) {
        RLog.e(TAG, str);
    }
}
