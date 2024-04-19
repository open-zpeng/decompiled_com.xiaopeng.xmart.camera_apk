package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class NRACtrlDisplayEvent {
    private int mStatus;

    public NRACtrlDisplayEvent(int status) {
        this.mStatus = status;
    }

    public boolean isDisPlay() {
        return this.mStatus == 1;
    }
}
