package com.xiaopeng.xmart.camera;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public abstract class BaseCameraService extends Service {
    private static final String CHANNEL_ID = "CameraApp";
    private static final String CHANNEL_NAME = "CameraAppService";
    private static final String CONTENT = "CameraAppService";
    private static final String TAG = "CameraAppService";
    private static final String TITLE = "CameraApp";
    private Handler mHandler;

    public abstract void handleBumpRecord();

    public abstract void handleGuardMsg();

    public abstract void handleRemoteCtrlMsg(String cmd);

    public abstract void loadInstance(Context context);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        CameraLog.i("CameraAppService", "onCreate", false);
        this.mHandler = new Handler();
        startNotification();
        loadInstance(this);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras;
        CameraLog.i("CameraAppService", "onStartCommand:" + intent, false);
        if (intent == null || TextUtils.isEmpty(intent.getAction()) || !intent.getAction().equals(CameraDefine.ACTION_LOCAL_CAMERA_SERVICE) || (extras = intent.getExtras()) == null || extras.keySet().isEmpty()) {
            return 2;
        }
        if (extras.keySet().contains(CameraDefine.KEY_TBOX_CAMERA_CTRL)) {
            handleRemoteCtrlMsg(String.valueOf(extras.get(CameraDefine.KEY_TBOX_CAMERA_CTRL)));
            return 2;
        } else if (extras.keySet().contains(CameraDefine.KEY_TBOX_TBOX_GUARD)) {
            handleGuardMsg();
            return 2;
        } else if (extras.keySet().contains(CameraDefine.KEY_VCU_BAT_BUMP_RECRD)) {
            handleBumpRecord();
            return 2;
        } else {
            return 2;
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        CameraLog.i("CameraAppService", "onDestroy", false);
        super.onDestroy();
    }

    private void startNotification() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        CameraLog.i("CameraAppService", "startNotification", false);
        NotificationChannel notificationChannel = new NotificationChannel("CameraApp", "CameraAppService", 3);
        ((NotificationManager) getApplicationContext().getSystemService("notification")).createNotificationChannel(notificationChannel);
        startForeground(100, new NotificationCompat.Builder(this, notificationChannel.getId()).setSmallIcon(com.xiaopeng.xmart.camerabase.R.mipmap.ic_applist_video).setContentTitle("CameraApp").setContentText("CameraAppService").setPriority(3).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
    }
}
