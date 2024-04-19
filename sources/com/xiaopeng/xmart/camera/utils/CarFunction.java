package com.xiaopeng.xmart.camera.utils;

import android.car.Car;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class CarFunction {
    public static final String D21 = "Q2";
    public static final String D22 = "Q6";
    public static final String D55 = "Q3";
    public static final String E28 = "Q1";
    public static final String E28A = "Q8";
    public static final String E38 = "Q7";
    public static final String EU = "EU";
    public static final String F30 = "Q9";
    public static final String Q5 = "Q5";
    static final String TAG = "CarFunction";

    public static String getProduct() {
        try {
            return Car.getXpCduType();
        } catch (Exception e) {
            LogUtils.e(TAG, "can not get HardwareCarType error = " + e);
            return "";
        }
    }

    public static String getRegionType() {
        try {
            return Car.getVersionInCountryCode();
        } catch (Throwable th) {
            LogUtils.e(TAG, "can not get VersionInCountryCode error = " + th);
            return "";
        }
    }

    public static final boolean isSupportPageDirectMultiParams() {
        String product = getProduct();
        String regionType = getRegionType();
        regionType.hashCode();
        if (regionType.equals(EU)) {
            return "Q7".equals(product);
        }
        return false;
    }
}
