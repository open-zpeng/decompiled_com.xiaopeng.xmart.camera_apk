package com.alivc.debug;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes.dex */
public class ParameterUtil {
    public static int getInteger(Map<String, String> map, String str) {
        String str2;
        if (map == null || str == null || !map.containsKey(str) || (str2 = map.get(str)) == null) {
            return 0;
        }
        try {
            return Integer.parseInt(str2);
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public static long getLong(Map<String, String> map, String str) {
        String str2;
        if (map == null || str == null || !map.containsKey(str) || (str2 = map.get(str)) == null) {
            return 0L;
        }
        try {
            return Long.parseLong(str2);
        } catch (Throwable th) {
            th.printStackTrace();
            return 0L;
        }
    }

    public static float getProcessCpuRate() {
        float totalCpuTime = (float) getTotalCpuTime();
        float appCpuTime = (float) getAppCpuTime();
        try {
            Thread.sleep(360L);
        } catch (Exception unused) {
        }
        return ((((float) getAppCpuTime()) - appCpuTime) * 100.0f) / (((float) getTotalCpuTime()) - totalCpuTime);
    }

    private static long getTotalCpuTime() {
        String[] strArr;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            strArr = readLine.split(" ");
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        if (strArr == null || strArr.length < 9) {
            return 0L;
        }
        return Long.parseLong(strArr[2]) + Long.parseLong(strArr[3]) + Long.parseLong(strArr[4]) + Long.parseLong(strArr[6]) + Long.parseLong(strArr[5]) + Long.parseLong(strArr[7]) + Long.parseLong(strArr[8]);
    }

    private static long getAppCpuTime() {
        String[] strArr;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + Process.myPid() + "/stat")), 1000);
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            strArr = readLine.split(" ");
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        return Long.parseLong(strArr[13]) + Long.parseLong(strArr[14]) + Long.parseLong(strArr[15]) + Long.parseLong(strArr[16]);
    }

    public static float getRunningAppProcessInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            int i = runningAppProcessInfo.pid;
            int i2 = runningAppProcessInfo.uid;
            String str = runningAppProcessInfo.processName;
            int[] iArr = {i};
            if (i == Process.myPid()) {
                return activityManager.getProcessMemoryInfo(iArr)[0].getTotalPss() / 1024.0f;
            }
        }
        return 0.0f;
    }

    public static String getTodayDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getSecondTime() {
        return new SimpleDateFormat("mm:ss", Locale.getDefault()).format(new Date());
    }
}
