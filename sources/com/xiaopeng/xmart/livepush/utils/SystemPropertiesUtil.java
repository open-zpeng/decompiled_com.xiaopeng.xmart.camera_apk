package com.xiaopeng.xmart.livepush.utils;

import com.xiaopeng.xvs.xid.sync.api.ISync;
/* loaded from: classes.dex */
public class SystemPropertiesUtil {
    private static final String SYSTEM_PROPERTIES_SWITCH_CODING = "com.xiaopeng.camera.assist.masking";
    private static final String TAG = "SystemPropertiesUtil";

    public static String getProperty(String key, String defaultValue) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod(ISync.SYNC_CALL_METHOD_GET, String.class, String.class).invoke(cls, key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static void setProperty(String key, String value) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            cls.getMethod("set", String.class, String.class).invoke(cls, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openCoding() {
        setProperty(SYSTEM_PROPERTIES_SWITCH_CODING, String.valueOf(true));
    }

    public static void closeCoding() {
        setProperty(SYSTEM_PROPERTIES_SWITCH_CODING, String.valueOf(false));
    }
}
