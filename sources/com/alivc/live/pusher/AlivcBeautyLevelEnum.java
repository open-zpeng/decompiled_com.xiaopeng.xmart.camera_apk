package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcBeautyLevelEnum {
    BEAUTY_Normal(0),
    BEAUTY_Professional(1);
    
    private int mBeautyMode;

    AlivcBeautyLevelEnum(int i) {
        this.mBeautyMode = i;
    }

    public int getBeautyMode() {
        return this.mBeautyMode;
    }
}
