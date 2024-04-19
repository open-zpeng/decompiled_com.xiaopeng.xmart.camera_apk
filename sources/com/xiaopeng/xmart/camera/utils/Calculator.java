package com.xiaopeng.xmart.camera.utils;

import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController;
import com.xiaopeng.xmart.camera.define.Config;
/* loaded from: classes.dex */
public class Calculator {
    private static final String TAG = "Calculator";

    public static long calculateTopCameraUpTime() {
        return 7000L;
    }

    public static double sin(double angle) {
        return Math.sin((angle * 3.141592653589793d) / 180.0d);
    }

    public static double cos(double angle) {
        return Math.cos((angle * 3.141592653589793d) / 180.0d);
    }

    public static double acos(double cosValue) {
        return (Math.acos(cosValue) * 180.0d) / 3.141592653589793d;
    }

    public static int calculateRotateAngle() {
        int i;
        Exception e;
        int cameraAngle;
        try {
            cameraAngle = ((IAvmController) CarClientWrapper.getInstance().getController(CarClientWrapper.XP_AVM_SERVICE)).getCameraAngle();
            i = cameraAngle - 180;
        } catch (Exception e2) {
            i = 0;
            e = e2;
        }
        try {
            CameraLog.d(TAG, "Top camera original angle, rotate angle " + i + ",angle:" + cameraAngle, false);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return i;
        }
        return i;
    }

    public static int calculateRotateAngle(int angle) {
        int i = angle - 180;
        CameraLog.d(TAG, "Top camera original angle, rotate angle " + i + ",angle:" + angle, false);
        return i;
    }

    public static long calculateTopCameraDownTime(int angle) {
        long abs = (Math.abs(angle) * Config.TOP_CAMERA_ROTATE_TIME) / 350;
        CameraLog.d(TAG, "calculateTopCameraDownTime, rotateTime: " + abs, false);
        long j = abs + 7000;
        CameraLog.d(TAG, "calculateTopCameraDownTime, downTime: " + j, false);
        return j;
    }
}
