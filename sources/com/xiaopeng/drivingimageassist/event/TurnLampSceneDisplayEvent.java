package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class TurnLampSceneDisplayEvent {
    private boolean mStatus;

    public TurnLampSceneDisplayEvent(boolean status) {
        this.mStatus = status;
    }

    public boolean isDisPlay() {
        return this.mStatus;
    }
}
