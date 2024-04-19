package com.xiaopeng.drivingimageassist.utils;

import android.hardware.Camera;
import com.xiaopeng.drivingimageassist.statistic.StatisticConstants;
import com.xiaopeng.drivingimageassist.statistic.StatisticManager;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class CameraUtil {
    private static final String TAG = "CameraUtil";

    public static void setCameraDisplayMode(Camera camera, int type) {
        if (camera == null) {
            return;
        }
        LogUtils.i(TAG, "组件setCameraDisplayMode：" + type);
        StatisticManager.logWithOneParam(StatisticConstants.EVENT_DIG_ASSIST, StatisticConstants.DIG_OPEN, "value", String.valueOf(type));
        try {
            Camera.Parameters cmdControlParameters = camera.getCmdControlParameters();
            cmdControlParameters.setAvmDisplayMode(type);
            cmdControlParameters.setAvmOverlayWorkSt(1);
            cmdControlParameters.setAvmOutMode(0);
            cmdControlParameters.setAvmCalibration(0);
            cmdControlParameters.setAvmTransparentChassisWorkSt(1);
            camera.setCmdControlParameters(cmdControlParameters);
            LogUtils.i(TAG, "组件setCameraDisplayMode执行完毕：" + type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
