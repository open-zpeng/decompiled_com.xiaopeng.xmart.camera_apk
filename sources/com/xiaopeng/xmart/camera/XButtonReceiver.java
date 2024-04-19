package com.xiaopeng.xmart.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.ActivityUtils;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
/* loaded from: classes.dex */
public class XButtonReceiver extends BroadcastReceiver {
    private static final int DURATION_X_BUTTON = 600;
    private static final int MSG_X_BUTTON = 257;
    private static final String TAG = "XButtonReceiver";
    private static final int XKEY_TAKE_PHOTO = 3;
    public static final String XP_XKEY_INTENT = "com.xiaopeng.intent.action.xkey";
    private static boolean isDoubleClick;
    private static long mXButtonLastHandleTime;
    private static long mXButtonLastReceive;
    private Handler mHandler = new Handler() { // from class: com.xiaopeng.xmart.camera.XButtonReceiver.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 257) {
                XButtonReceiver.this.mHandler.removeMessages(257);
                if (ActivityUtils.isEnterAutoPilot(App.getInstance())) {
                    return;
                }
                if (CarCameraHelper.getInstance().getCurParkingType() != 1) {
                    XButtonReceiver.this.workingBg(XButtonReceiver.isDoubleClick ? CameraDefine.Actions.ACTION_RECORD : CameraDefine.Actions.ACTION_TAKE_PICTURE, 102, "com.xiaopeng.xmart.camera.MainActivity");
                    boolean unused = XButtonReceiver.isDoubleClick = false;
                }
                long unused2 = XButtonReceiver.mXButtonLastHandleTime = SystemClock.elapsedRealtime();
            }
            super.handleMessage(msg);
        }
    };

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.xiaopeng.intent.action.xkey") && intent.getIntExtra("keyfunc", 0) == 3) {
            CameraLog.d(TAG, "XButtonReceiver:" + intent.getAction() + ",keytype:" + intent.getStringExtra("keytype") + ",keyfunc:" + intent.getIntExtra("keyfunc", 0), false);
            if (ActivityUtils.isEnterAutoPilot(context)) {
                this.mHandler.removeMessages(257);
            } else if (!intent.getStringExtra("keytype").equals("short") || CarCameraHelper.getInstance().getCurParkingType() == 1) {
            } else {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (elapsedRealtime - mXButtonLastHandleTime > 1500) {
                    if (this.mHandler.hasMessages(257) && elapsedRealtime - mXButtonLastReceive < 600) {
                        isDoubleClick = true;
                    } else {
                        isDoubleClick = false;
                        this.mHandler.sendEmptyMessageDelayed(257, 600L);
                    }
                    mXButtonLastReceive = elapsedRealtime;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void workingBg(String action, int type, String className) {
        CameraLog.d(TAG, "workingBg...", false);
        if (type == 102 && !CarCameraHelper.getInstance().hasDvrCamera()) {
            CameraLog.d(TAG, "workingBg but without type:" + type + "!", false);
            return;
        }
        try {
            App app = App.getInstance();
            Intent intent = new Intent(app, Class.forName(className));
            intent.setAction(action);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            intent.putExtra(CameraDefine.Actions.ACTION_EXTRA_CAMERA_TYPE, type);
            app.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
