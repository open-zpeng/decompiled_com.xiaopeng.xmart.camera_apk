package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class CameraDisplayEvent {
    private int mStatus;

    public CameraDisplayEvent(int status) {
        this.mStatus = status;
    }

    public boolean isDisPlay() {
        return this.mStatus == 1;
    }
}
