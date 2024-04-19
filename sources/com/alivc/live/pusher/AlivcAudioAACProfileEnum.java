package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcAudioAACProfileEnum {
    AAC_LC(2),
    HE_AAC(5),
    HE_AAC_v2(29),
    AAC_LD(23);
    
    private int mAudioProfile;

    AlivcAudioAACProfileEnum(int i) {
        this.mAudioProfile = i;
    }

    public int getAudioProfile() {
        return this.mAudioProfile;
    }
}
