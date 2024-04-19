package com.aliyun.clientinforeport.util;

import android.content.Context;
import java.net.URLEncoder;
/* loaded from: classes.dex */
public class AppUtils {
    private static String appName = null;
    private static boolean appNameDecied = false;
    private static String packageName = null;
    private static boolean packageNameDecied = false;

    public static String getPackageName(Context context) {
        if (packageNameDecied) {
            return packageName;
        }
        if (context == null) {
            return null;
        }
        String packageName2 = context.getPackageName();
        packageName = packageName2;
        packageNameDecied = true;
        return packageName2;
    }

    public static String getAppName(Context context) {
        if (appNameDecied) {
            return appName;
        }
        if (context == null) {
            return null;
        }
        String encode = URLEncoder.encode(context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString());
        appName = encode;
        appNameDecied = true;
        return encode;
    }
}
