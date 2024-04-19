package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcFpsEnum {
    FPS_8(8),
    FPS_10(10),
    FPS_12(12),
    FPS_15(15),
    FPS_20(20),
    FPS_25(25),
    FPS_30(30);
    
    private int mFps;

    AlivcFpsEnum(int i) {
        this.mFps = i;
    }

    public int getFps() {
        return this.mFps;
    }
}
