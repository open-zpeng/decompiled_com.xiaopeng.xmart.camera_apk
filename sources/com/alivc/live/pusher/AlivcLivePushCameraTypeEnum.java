package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcLivePushCameraTypeEnum {
    CAMERA_TYPE_BACK(0),
    CAMERA_TYPE_FRONT(1),
    CAMERA_TYPE_CAR_AVM(2),
    CAMERA_TYPE_CAR_TOP(3);
    
    private int mCameraId;

    AlivcLivePushCameraTypeEnum(int var3) {
        this.mCameraId = var3;
    }

    public int getCameraId() {
        return this.mCameraId;
    }
}
