package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcPreviewOrientationEnum {
    ORIENTATION_PORTRAIT(0),
    ORIENTATION_LANDSCAPE_HOME_RIGHT(90),
    ORIENTATION_LANDSCAPE_HOME_LEFT(270);
    
    private int mOrientation;

    AlivcPreviewOrientationEnum(int i) {
        this.mOrientation = i;
    }

    public int getOrientation() {
        return this.mOrientation;
    }
}
