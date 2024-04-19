package com.xiaopeng.xmart.livepush;

import com.xiaopeng.xmart.camera.bean.ControlMsg;
/* loaded from: classes.dex */
public interface RemoteControlIpcCommand {
    void onLiveHeartBeat();

    void onLiveOpenOrCloseCmd(ControlMsg controlMsg);

    void onLiveRotateCameraCmd(ControlMsg controlMsg);

    void onLiveStateCmd(ControlMsg controlMsg);
}
