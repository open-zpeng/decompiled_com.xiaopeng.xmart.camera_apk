package com.xiaopeng.xmart.camera.utils;

import android.util.Log;
/* loaded from: classes.dex */
public class CameraLog {
    private static final int LOG_LEVEL_DEBUG = 2;
    private static final int LOG_LEVEL_RELEASE = 1;
    private static int logLevel = 1;

    public static boolean isDebug() {
        return logLevel == 2;
    }

    public static void i(String tag, String message) {
        i(tag, message, 2);
    }

    public static void i(String tag, String message, boolean debug) {
        if (debug) {
            i(tag, message);
        } else {
            i(tag, message, 1);
        }
    }

    private static void i(String tag, String message, int level) {
        if (level <= logLevel) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        d(tag, message, 2);
    }

    public static void d(String tag, String message, boolean debug) {
        if (debug) {
            d(tag, message);
        } else {
            d(tag, message, 1);
        }
    }

    private static void d(String tag, String message, int level) {
        if (level <= logLevel) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag, String message) {
        e(tag, message, 2);
    }

    public static void e(String tag, String message, boolean debug) {
        if (debug) {
            e(tag, message);
        } else {
            e(tag, message, 1);
        }
    }

    private static void e(String tag, String message, int level) {
        if (level <= logLevel) {
            Log.e(tag, message);
        }
    }

    public static void printStackTrace(String tag, String message) {
        printStackTrace(tag, message, true);
    }

    public static void printStackTrace(String tag, String message, boolean debug) {
        d(tag, Log.getStackTraceString(new Throwable(message)), debug);
    }
}
