package com.xiaopeng.xmart.camera;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
/* loaded from: classes.dex */
public class CameraService extends Service {
    private static final String TAG = "CameraService";
    private CameraBroadcastReceiver mBootBroadcastReceiver;
    private ICiuController mICiuController;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        CameraLog.d(TAG, "onCreate...", false);
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$CameraService$7UP-PRg-6jztFeA2fsVqawtwgWA
            @Override // java.lang.Runnable
            public final void run() {
                CameraService.this.lambda$onCreate$0$CameraService();
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        CameraBroadcastReceiver cameraBroadcastReceiver = new CameraBroadcastReceiver();
        this.mBootBroadcastReceiver = cameraBroadcastReceiver;
        registerReceiver(cameraBroadcastReceiver, intentFilter);
    }

    public /* synthetic */ void lambda$onCreate$0$CameraService() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
        }
    }

    private void initController() {
        CameraLog.i(TAG, "  initController ", false);
        this.mICiuController = (ICiuController) CarClientWrapper.getInstance().getController(CarClientWrapper.XP_CIU_SERVICE);
        CameraLog.d(TAG, "initController mICiuController:" + this.mICiuController, false);
    }

    private void setDvrEnable() {
        CameraLog.d(TAG, "setDvrEnable-->" + this.mICiuController, false);
        ICiuController iCiuController = this.mICiuController;
        if (iCiuController != null) {
            try {
                iCiuController.setDvrEnable(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        filterIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void filterIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        final String action = intent.getAction();
        CameraLog.d(TAG, "filterIntent action-->" + action, false);
        action.hashCode();
        if (action.equals("android.intent.action.SCREEN_ON")) {
            ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$CameraService$4rZ40J9VkH7CGUpJoph5qhzGLqQ
                @Override // java.lang.Runnable
                public final void run() {
                    CameraService.this.lambda$filterIntent$1$CameraService(action);
                }
            });
        }
    }

    public /* synthetic */ void lambda$filterIntent$1$CameraService(String str) {
        CameraLog.d(TAG, "filterIntent action:" + str, false);
        while (this.mICiuController == null) {
            SystemClock.sleep(200L);
        }
        SystemClock.sleep(200L);
        setDvrEnable();
    }

    @Override // android.app.Service
    public void onDestroy() {
        CameraLog.d(TAG, "onDestroy, ", false);
        super.onDestroy();
        CameraBroadcastReceiver cameraBroadcastReceiver = this.mBootBroadcastReceiver;
        if (cameraBroadcastReceiver != null) {
            unregisterReceiver(cameraBroadcastReceiver);
            this.mBootBroadcastReceiver = null;
        }
    }
}
