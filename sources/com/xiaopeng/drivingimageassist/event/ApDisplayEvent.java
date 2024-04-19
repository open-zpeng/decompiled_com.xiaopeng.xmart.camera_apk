package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class ApDisplayEvent {
    private int mStatus;

    public ApDisplayEvent(int status) {
        this.mStatus = status;
    }

    public boolean isDisPlay() {
        return this.mStatus == 1;
    }
}
