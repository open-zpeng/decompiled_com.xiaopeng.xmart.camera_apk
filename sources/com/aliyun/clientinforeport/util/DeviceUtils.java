package com.aliyun.clientinforeport.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.xiaopeng.xvs.xid.sync.api.ISync;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.UUID;
/* loaded from: classes.dex */
public class DeviceUtils {
    private static final String KEY_SHARED_PREFERENCE = "aliyun_svideo_global_info";
    private static final String KEY_UUID = "uuid";
    private static boolean isPad = false;
    private static boolean isPadDecied = false;
    private static String uuid = null;
    private static boolean uuidDecied = false;

    /* loaded from: classes.dex */
    public enum Connection {
        wifi,
        cellnetwork
    }

    public static boolean isPad(Context context) {
        if (isPadDecied) {
            return isPad;
        }
        if (context == null) {
            return false;
        }
        try {
            Display defaultDisplay = ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            if (Math.sqrt(Math.pow(displayMetrics.widthPixels / displayMetrics.xdpi, 2.0d) + Math.pow(displayMetrics.heightPixels / displayMetrics.ydpi, 2.0d)) >= 6.0d) {
                isPad = true;
            } else {
                isPad = false;
            }
            isPadDecied = true;
            return isPad;
        } catch (Exception unused) {
            return false;
        }
    }

    public static String getDeviceModel() {
        String str = Build.MODEL;
        if (str.contains(" ")) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException unused) {
                return URLEncoder.encode(str);
            }
        }
        return str;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getUuid(Context context) {
        if (uuidDecied) {
            return uuid;
        }
        if (context == null) {
            return "";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCE, 0);
        if (sharedPreferences.contains("uuid")) {
            uuid = sharedPreferences.getString("uuid", null);
        }
        if (uuid == null) {
            uuid = UUID.randomUUID().toString().toUpperCase();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("uuid", uuid);
            edit.apply();
        }
        uuidDecied = true;
        return uuid;
    }

    public static String getOperationSystem(Context context) {
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod(ISync.SYNC_CALL_METHOD_GET, String.class);
            method.setAccessible(true);
            String str = (String) method.invoke(null, "ro.os.name");
            if (TextUtils.isEmpty(str)) {
                str = (String) method.invoke(null, "net.bt.name");
            }
            return !TextUtils.isEmpty(str) ? str : "Android";
        } catch (Exception e) {
            e.printStackTrace();
            return "Android";
        }
    }

    public static String getDeviceBrand() {
        String str = Build.BRAND;
        if (str.contains(" ")) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException unused) {
                return URLEncoder.encode(str);
            }
        }
        return str;
    }

    public static String getDeviceManufacture() {
        String str = Build.MANUFACTURER;
        if (str.contains(" ")) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException unused) {
                return URLEncoder.encode(str);
            }
        }
        return str;
    }

    public static String getConnection(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return "";
        }
        int type = activeNetworkInfo.getType();
        if (type == 0) {
            return Connection.cellnetwork.name();
        }
        return type == 1 ? Connection.wifi.name() : "";
    }
}
