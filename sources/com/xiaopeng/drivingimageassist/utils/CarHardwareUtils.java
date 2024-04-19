package com.xiaopeng.drivingimageassist.utils;

import android.car.Car;
import android.os.SystemProperties;
import android.util.Log;
import com.xiaopeng.speech.vui.utils.VuiUtils;
import com.xiaopeng.xmart.camera.utils.CarFunction;
/* loaded from: classes.dex */
public class CarHardwareUtils {
    public static final String CAR_TYPE_D55 = "D55";
    public static final String CAR_TYPE_E38 = "E38";
    private static final String TAG = "CarHardwareUtils";
    private static Boolean sIsHasAvm;

    public static String getXpCduType() {
        String xpCduType = Car.getXpCduType();
        Log.d(TAG, "carType=" + xpCduType);
        return xpCduType;
    }

    public static boolean isH93() {
        return "QB".equals(getXpCduType());
    }

    public static boolean isE38() {
        return "Q7".equals(getXpCduType());
    }

    public static boolean isE28() {
        return "Q1".equals(getXpCduType());
    }

    public static boolean isF30() {
        return CarFunction.F30.equals(getXpCduType());
    }

    public static boolean isE28a() {
        return "Q8".equals(getXpCduType());
    }

    public static boolean isSupportNRACtrl() {
        return isE38() || isE28a() || isF30() || isE28() || isH93();
    }

    public static boolean isUseCarServiceNRACtrl() {
        return isE28();
    }

    public static boolean isUseWindowReceiver() {
        return isE28() || isD55();
    }

    public static boolean isSupportTurnLamp() {
        return isE38() || isE28a() || isF30() || isH93();
    }

    public static boolean isD55() {
        return "Q3".equals(getXpCduType()) || "Q3A".equals(getXpCduType());
    }

    public static boolean isLandscape() {
        String xpCduType = getXpCduType();
        xpCduType.hashCode();
        char c = 65535;
        switch (xpCduType.hashCode()) {
            case 2064:
                if (xpCduType.equals(VuiUtils.CAR_PLATFORM_A1)) {
                    c = 0;
                    break;
                }
                break;
            case 2065:
                if (xpCduType.equals(VuiUtils.CAR_PLATFORM_A2)) {
                    c = 1;
                    break;
                }
                break;
            case 2066:
                if (xpCduType.equals(VuiUtils.CAR_PLATFORM_A3)) {
                    c = 2;
                    break;
                }
                break;
            case 2560:
                if (xpCduType.equals("Q1")) {
                    c = 3;
                    break;
                }
                break;
            case 2561:
                if (xpCduType.equals("Q2")) {
                    c = 4;
                    break;
                }
                break;
            case 2562:
                if (xpCduType.equals("Q3")) {
                    c = 5;
                    break;
                }
                break;
            case 2565:
                if (xpCduType.equals("Q6")) {
                    c = 6;
                    break;
                }
                break;
            case 2566:
                if (xpCduType.equals("Q7")) {
                    c = 7;
                    break;
                }
                break;
            case 2567:
                if (xpCduType.equals("Q8")) {
                    c = '\b';
                    break;
                }
                break;
            case 2568:
                if (xpCduType.equals(CarFunction.F30)) {
                    c = '\t';
                    break;
                }
                break;
            case 2577:
                if (xpCduType.equals("QB")) {
                    c = '\n';
                    break;
                }
                break;
            case 79487:
                if (xpCduType.equals("Q3A")) {
                    c = 11;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            case 11:
            default:
                return false;
            case 3:
            case 7:
            case '\b':
            case '\t':
            case '\n':
                return true;
        }
    }

    public static boolean hasAVM() {
        Boolean bool = sIsHasAvm;
        if (bool != null) {
            return bool.booleanValue();
        }
        Boolean valueOf = Boolean.valueOf(SystemProperties.getBoolean("persist.sys.xiaopeng.AVM", false));
        sIsHasAvm = valueOf;
        return valueOf.booleanValue();
    }
}
