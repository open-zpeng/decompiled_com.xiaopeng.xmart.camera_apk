package com.xiaopeng.xmart.camera;

import android.provider.Settings;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class SystemSettings {
    static final String TAG = "SystemSettings";

    /* loaded from: classes.dex */
    public interface Keys {
        public static final String REMOTE_CAMERA_SW = "remote_camera_sw";
    }

    public static boolean isRemoteCameraEnable() {
        boolean z;
        try {
            z = Settings.System.getInt(App.getInstance().getContentResolver(), Keys.REMOTE_CAMERA_SW, 0) == 1;
        } catch (Exception e) {
            e = e;
            z = false;
        }
        try {
            CameraLog.i(TAG, "isRemoteCameraEnable: " + z, false);
        } catch (Exception e2) {
            e = e2;
            CameraLog.e(TAG, "isRemoteCameraEnable: " + z + "\n" + e, false);
            return z;
        }
        return z;
    }
}
