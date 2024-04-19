package com.xiaopeng.xmart.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class CameraBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "CameraBroadcastReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        CameraLog.d(TAG, "receive action: " + action, false);
        if (!CarCameraHelper.getInstance().isSupportDvr()) {
            CameraLog.d(TAG, "isNotSupportDvr", false);
        } else if (TextUtils.equals(action, "android.intent.action.BOOT_COMPLETED") || TextUtils.equals(action, "android.intent.action.SCREEN_ON")) {
            Intent intent2 = new Intent(context, CameraService.class);
            intent2.setAction(action);
            context.startService(intent2);
        }
    }
}
