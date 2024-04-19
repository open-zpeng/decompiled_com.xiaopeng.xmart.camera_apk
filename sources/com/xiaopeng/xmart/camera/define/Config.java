package com.xiaopeng.xmart.camera.define;

import android.car.Car;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.xiaopeng.lib.utils.config.CommonConfig;
import com.xiaopeng.lib.utils.info.AppInfoUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class Config {
    public static final String APP_ID = "xp_car_camera_biz";
    public static final String APP_SECRET = "8akHD02indsn2slC";
    public static final String APP_SECRET_CHASSIS_BUMP = "kxHTJO04sv8aszcv";
    public static final String APP_SECRET_RELEASE = "jhgdw9mdhHbdv08w";
    public static final int BODY_COLOR_BLACK = 3;
    public static final int BODY_COLOR_BLUE = 9;
    public static final int BODY_COLOR_BLUE_BLACK = 7;
    public static final int BODY_COLOR_CHAMPAGNE = 11;
    public static final int BODY_COLOR_COFFEE = 6;
    public static final int BODY_COLOR_FUGUANG_GREEN = 17;
    public static final int BODY_COLOR_GRAY = 4;
    public static final int BODY_COLOR_JINGMI_BLACK = 14;
    public static final int BODY_COLOR_QINGYUE_BLUE = 15;
    public static final int BODY_COLOR_QINGYUE_BLUE_BLACK = 16;
    public static final int BODY_COLOR_RED = 10;
    public static final int BODY_COLOR_RED_BLACK = 1;
    public static final int BODY_COLOR_SILVER = 8;
    public static final int BODY_COLOR_SILVER_BLACK = 5;
    public static final int BODY_COLOR_WHITE = 2;
    public static final int BODY_COLOR_XINGJI_GRAY = 12;
    public static final int BODY_COLOR_YINGHUO_BLUE = 13;
    public static final int CFC_CONFIGURE_TRAVEL_VERSION = 4;
    public static final int CFC_VEHICLE_TYPE_DAVID_21 = 1;
    public static final int DAVID2_TEST_CONFIG = 2;
    public static final int HARDWARE_VERSION_4 = 4;
    public static final int HARDWARE_VERSION_7 = 7;
    public static final String HOST;
    public static final String HTTP_URL_CHASSIS_BUMP_RELEASE = "https://bd-callback.xiaopeng.com/oss/monitorData";
    public static final String HTTP_URL_CHASSIS_BUMP_TEST = "http://test-tunnel.xiaopeng.com:9001/oss/monitorData";
    public static final String HTTP_URL_LIVE_URL = "v5/aliyun/live";
    public static final String HTTP_URL_UPLOAD;
    public static final boolean IS_TEST_MODE = false;
    public static final float MAX_CAR_SPEED_LIMIT = 80.0f;
    public static final int MSG_TYPE_FROM_XMART = 20;
    public static final int MSG_TYPE_NORMAL_MSG = 1;
    private static final String SYS_BODY_COLOR_CFC = "persist.sys.xiaopeng.bodyColor";
    public static final String SYS_CONFIG_CODE_CFC = "persist.sys.xiaopeng.configCode";
    public static final String SYS_CONFIG_DVR_DEBUG = "persist.sys.xiaopeng.DVR.feature";
    private static final String TAG = "CameraConfig";
    public static final long TOP_CAMERA_DOWN_DELAY_FEEDBACK_TIME = 7000;
    public static final long TOP_CAMERA_DOWN_TIME = 7000;
    public static final long TOP_CAMERA_ROTATE_TIME = 13000;
    public static final long TOP_CAMERA_UP_TIME = 7000;
    private static String mCarType = "";

    static {
        String str = CommonConfig.HTTP_HOST + "/biz/";
        HOST = str;
        HTTP_URL_UPLOAD = str + "v5/event/vehicleEvent";
    }

    public static String getXpCduType() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            CameraLog.d(TAG, "getXpCduType exception:" + e.getMessage(), false);
        }
        if (!TextUtils.isEmpty(mCarType)) {
            return mCarType;
        }
        mCarType = Car.getXpCduType();
        CameraLog.d(TAG, "carType:" + mCarType, false);
        return mCarType;
    }

    public static void printVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        sb.append("firmware:").append(SystemProperties.get("ro.xiaopeng.software", "")).append("\n");
        sb.append("hardwareVersion:").append(getHardwareVersion()).append("\n");
        sb.append("carType:").append(getXpCduType()).append("\n");
        sb.append("configCode:").append(SystemProperties.get(SYS_CONFIG_CODE_CFC, "")).append("\n");
        sb.append("avm:").append(CarCameraHelper.getInstance().hasAVM()).append("\n");
        sb.append("versioncode:").append(AppInfoUtils.getVersionCode(App.getInstance(), App.getInstance().getPackageName())).append("\n");
        sb.append("versionName:").append(AppInfoUtils.getVersionName(App.getInstance(), App.getInstance().getPackageName())).append("\n");
        sb.append("hasTopCamera:").append(CarCameraHelper.getInstance().hasTopCamera()).append("\n");
        sb.append("isSupportDvr:").append(CarCameraHelper.getInstance().isSupportDvr()).append("\n");
        sb.append("cduType:").append(Car.getXpCduType()).append("\n");
        CameraLog.d(TAG, sb.toString(), false);
    }

    public static int getHardwareVersion() {
        try {
            return Car.getHardwareVersion();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getBodyColor() {
        return SystemProperties.getInt(SYS_BODY_COLOR_CFC, 0);
    }

    public static boolean isInter() {
        try {
            return Car.isExportVersion();
        } catch (Exception e) {
            e.printStackTrace();
            return !App.getInstance().getResources().getConfiguration().locale.getLanguage().equals("zh");
        }
    }

    public static boolean isSupportShockAlarm() {
        return !isInter();
    }

    public static boolean isSupportAuth() {
        return !isInter();
    }

    public static boolean isSupportRemoteControl() {
        return !isInter();
    }
}
