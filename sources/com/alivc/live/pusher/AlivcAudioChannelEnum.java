package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcAudioChannelEnum {
    AUDIO_CHANNEL_ONE(1),
    AUDIO_CHANNEL_TWO(2);
    
    private int mChannelCount;

    AlivcAudioChannelEnum(int i) {
        this.mChannelCount = i;
    }

    public int getChannelCount() {
        return this.mChannelCount;
    }
}
