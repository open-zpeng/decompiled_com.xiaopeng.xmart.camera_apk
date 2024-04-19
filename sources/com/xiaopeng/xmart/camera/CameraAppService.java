package com.xiaopeng.xmart.camera;

import android.content.Context;
import android.text.TextUtils;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.livepush.RemoteControlPresenter;
import com.xiaopeng.xmart.shock.ShockAlarmHelper;
/* loaded from: classes.dex */
public class CameraAppService extends BaseCameraService {
    private static final String TAG = "CameraAppService";
    private Runnable mDelayToHandleMsg = new Runnable() { // from class: com.xiaopeng.xmart.camera.CameraAppService.1
        @Override // java.lang.Runnable
        public void run() {
            if (CameraAppService.this.mRemoteControl.isCarServiceConnected()) {
                if (TextUtils.isEmpty(CameraAppService.this.mLastCmd)) {
                    return;
                }
                CameraAppService cameraAppService = CameraAppService.this;
                cameraAppService.handRemoteControlCmd(cameraAppService.mLastCmd);
                CameraAppService.this.mLastCmd = null;
                return;
            }
            CameraAppService.this.getHandler().postDelayed(this, 1000L);
        }
    };
    private String mLastCmd;
    private RemoteControlPresenter mRemoteControl;

    @Override // com.xiaopeng.xmart.camera.BaseCameraService
    public void loadInstance(Context context) {
    }

    @Override // com.xiaopeng.xmart.camera.BaseCameraService
    public void handleRemoteCtrlMsg(String cmd) {
        CameraLog.i(TAG, "receive remote control message:" + cmd, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handRemoteControlCmd(String cmd) {
        CameraLog.i(TAG, "handle remote control cmd");
        if (TextUtils.isEmpty(cmd) || this.mRemoteControl.getIgStatus() != 2) {
            return;
        }
        this.mRemoteControl.doRemoteControlCommand(cmd);
    }

    @Override // com.xiaopeng.xmart.camera.BaseCameraService
    public void handleGuardMsg() {
        CameraLog.i(TAG, "handle guard message", false);
    }

    private static /* synthetic */ void lambda$handleGuardMsg$0() {
        ShockAlarmHelper.getInstance().waitForCarSrvConnected();
        ShockAlarmHelper.getInstance().enterShockProcess();
    }

    @Override // com.xiaopeng.xmart.camera.BaseCameraService
    public void handleBumpRecord() {
        CameraLog.i(TAG, "handle bump record", false);
    }
}
