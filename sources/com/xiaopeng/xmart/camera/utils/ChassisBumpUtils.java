package com.xiaopeng.xmart.camera.utils;

import android.content.Context;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public class ChassisBumpUtils {
    private static final String CLZ_CHASSISBUMP_HELP = "com.xiaopeng.xmart.chassisbump.ChassisBumpHelper";
    private static final String TAG = "ChassisBumpUtils";

    public static boolean enterChassisBump() {
        return false;
    }

    public static void initChassisBump(Context context) {
        try {
            CameraLog.i(TAG, "init chassisbump module", false);
            Class<?> cls = Class.forName(CLZ_CHASSISBUMP_HELP);
            cls.getDeclaredMethod("init", Context.class).invoke(cls.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]), context);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            CameraLog.i(TAG, "chassisbump module init failed:" + e.getMessage(), false);
        }
    }

    public static boolean isEnterChassisBump() {
        try {
            Class<?> cls = Class.forName(CLZ_CHASSISBUMP_HELP);
            return ((Boolean) cls.getDeclaredMethod("isEnterChassisBump", new Class[0]).invoke(cls.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]), new Object[0])).booleanValue();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            CameraLog.i(TAG, "isEnterChassisBump failed:" + e.getMessage(), false);
            return false;
        }
    }

    public static boolean exitChassisBump() {
        try {
            CameraLog.i(TAG, "exit chassis bump", false);
            Class<?> cls = Class.forName(CLZ_CHASSISBUMP_HELP);
            cls.getDeclaredMethod("exitChassisBump", new Class[0]).invoke(cls.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]), new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            CameraLog.i(TAG, "exit chassis bump failed:" + e.getMessage(), false);
        }
        return false;
    }

    public static boolean isCarServiceConnected() {
        try {
            Class<?> cls = Class.forName(CLZ_CHASSISBUMP_HELP);
            cls.getDeclaredMethod("isCarServiceConnected", new Class[0]).invoke(cls.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]), new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
