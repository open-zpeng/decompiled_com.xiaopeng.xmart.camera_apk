package com.xiaopeng.xmart.camera.utils;

import android.app.ActivityManager;
import android.content.Context;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import java.util.List;
/* loaded from: classes.dex */
public class ActivityUtils {
    public static final String ACTION_ACTIVITY_CHANGED = "com.xiaopeng.intent.action.ACTIVITY_CHANGED";
    public static final String EXTRA_COMPONENT = "android.intent.extra.COMPONENT";
    private static final String TAG = "ActivityUtils";

    public static boolean isEnterAutoPilot(Context context) {
        if (getTopActivityPackage(context).equals(IpcConfig.App.CAR_AUTOPILOT)) {
            CameraLog.i(TAG, "enter autopilot", false);
            return true;
        }
        return false;
    }

    public static boolean isEnter360Camera(Context context) {
        if (getTopActivityPackage(context).equals("com.xiaopeng.xmart.camera")) {
            CameraLog.i(TAG, "enter 360Camera", false);
            return true;
        }
        return false;
    }

    public static String getTopActivityPackage(Context context) {
        try {
            List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
            return (runningTasks == null || runningTasks.size() <= 0) ? "" : runningTasks.get(0).topActivity.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
