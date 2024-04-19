package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcPreviewRotationEnum {
    ROTATION_0(0),
    ROTATION_90(90),
    ROTATION_180(180),
    ROTATION_270(270),
    ROTATION_360(360);
    
    private int mRotation;

    AlivcPreviewRotationEnum(int i) {
        this.mRotation = i;
    }

    public int getRotation() {
        return this.mRotation;
    }
}
