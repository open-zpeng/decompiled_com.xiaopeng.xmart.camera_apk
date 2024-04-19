package com.xiaopeng.xmart.livepush;

import android.content.Context;
import com.xiaopeng.xmart.camera.bean.ControlMsg;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
/* loaded from: classes.dex */
public class DelayRemoteControlPresenter extends RemoteControlPresenter {
    public DelayRemoteControlPresenter(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlPresenter, com.xiaopeng.xmart.livepush.RemoteControlIpcCommand
    public void onLiveStateCmd(ControlMsg controlMsg) {
        setRemoteCmdReceived(true);
        setIsCmdCameraStatusHandled(false);
        if (CarCameraHelper.getInstance().hasAVM()) {
            if (getAVMWorkStatus() == 1 || getAVMWorkStatus() == 3) {
                super.onLiveStateCmd(controlMsg);
                return;
            }
            return;
        }
        super.onLiveStateCmd(controlMsg);
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlPresenter, com.xiaopeng.xmart.livepush.RemoteControlIpcCommand
    public void onLiveHeartBeat() {
        super.onLiveHeartBeat();
        if (CarCameraHelper.getInstance().hasAVM() && isIsRemoteCmdReceived() && !isIsCmdCameraStatusHandled()) {
            if (getAVMWorkStatus() == 1 || getAVMWorkStatus() == 3) {
                super.onLiveStateCmd(null);
            }
        }
    }
}
