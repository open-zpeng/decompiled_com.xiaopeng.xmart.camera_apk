package com.xiaopeng.xmart.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class LaunchCameraBroadcast extends BroadcastReceiver {
    private static final String TAG = "LaunchCameraBroadcast";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        filterIntent(context, intent);
    }

    private void filterIntent(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        String action = intent.getAction();
        CameraLog.d(TAG, "filterIntent action-->" + action, false);
        if (TextUtils.equals(action, "com.xiaopeng.xui.businessevent")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("extras{");
                for (String str : extras.keySet()) {
                    sb.append(str);
                    sb.append(":");
                    sb.append(extras.get(str));
                    sb.append(",");
                }
                sb.append("}");
                CameraLog.d(TAG, "extras:" + sb.toString(), false);
            }
            Intent intent2 = new Intent();
            if (extras != null) {
                if (extras.keySet().contains(CameraDefine.KEY_IG_STATUS)) {
                    intent2.putExtra(CameraDefine.KEY_IG_STATUS, String.valueOf(extras.get(CameraDefine.KEY_IG_STATUS)));
                } else if (extras.keySet().contains(CameraDefine.KEY_TBOX_CAMERA_CTRL)) {
                    intent2.putExtra(CameraDefine.KEY_TBOX_CAMERA_CTRL, String.valueOf(extras.get(CameraDefine.KEY_TBOX_CAMERA_CTRL)));
                } else if (extras.keySet().contains(CameraDefine.KEY_TBOX_TBOX_GUARD)) {
                    intent2.putExtra(CameraDefine.KEY_TBOX_TBOX_GUARD, String.valueOf(extras.get(CameraDefine.KEY_TBOX_TBOX_GUARD)));
                } else if (!extras.keySet().contains(CameraDefine.KEY_VCU_BAT_BUMP_RECRD)) {
                    return;
                } else {
                    intent2.putExtra(CameraDefine.KEY_VCU_BAT_BUMP_RECRD, String.valueOf(extras.get(CameraDefine.KEY_VCU_BAT_BUMP_RECRD)));
                }
                intent2.setAction(CameraDefine.ACTION_LOCAL_CAMERA_SERVICE);
                intent2.setPackage(context.getPackageName());
                context.startForegroundService(intent2);
            }
        }
    }
}
