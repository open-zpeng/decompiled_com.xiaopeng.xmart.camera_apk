package com.xiaopeng.drivingimageassist.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;
/* loaded from: classes.dex */
public class AppUtils {
    public static ComponentName getCurrentRunningTask(Context context) {
        List<ActivityManager.RunningTaskInfo> runningTasks;
        ActivityManager.RunningTaskInfo runningTaskInfo;
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null || (runningTasks = activityManager.getRunningTasks(1)) == null || runningTasks.isEmpty() || (runningTaskInfo = runningTasks.get(0)) == null) {
            return null;
        }
        ComponentName componentName = runningTaskInfo.topActivity;
        LogUtils.i("AppUtils", "getCurrentRunningTask:" + (componentName != null ? componentName.getPackageName() + MqttTopic.TOPIC_LEVEL_SEPARATOR + componentName.getClassName() : null));
        return componentName;
    }
}
