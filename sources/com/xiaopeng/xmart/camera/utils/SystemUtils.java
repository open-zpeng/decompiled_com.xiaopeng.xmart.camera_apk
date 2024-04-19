package com.xiaopeng.xmart.camera.utils;

import android.content.Context;
import android.view.WindowManager;
import com.xiaopeng.view.WindowManagerFactory;
/* loaded from: classes.dex */
public class SystemUtils {
    public static boolean isAppInMainScreen(Context context, String packageName) {
        return WindowManager.isPrimaryId(((WindowManager) context.getSystemService("window")).getSharedId(packageName));
    }

    public static boolean isAppInPsnScreen(Context context, String packageName) {
        return ((WindowManager) context.getSystemService("window")).getSharedId(packageName) == 1;
    }

    public static boolean isCurrentAppInMainScreen(Context context) {
        return isAppInMainScreen(context, context.getPackageName());
    }

    public static boolean isCurrentAppInPsnScreen(Context context) {
        return isAppInPsnScreen(context, context.getPackageName());
    }

    public static void slideCurrentAppToScreen(Context context, int screenId) {
        WindowManagerFactory.create(context).setSharedEvent(0, screenId, (String) null);
    }
}
