package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcSoundFormat {
    SOUND_FORMAT_UNKNOW(-1),
    SOUND_FORMAT_U8(0),
    SOUND_FORMAT_S16(1),
    SOUND_FORMAT_S32(2),
    SOUND_FORMAT_F32(3),
    SOUND_FORMAT_U8P(4),
    SOUND_FORMAT_S16P(5),
    SOUND_FORMAT_S32P(6),
    SOUND_FORMAT_F32P(7);
    
    private int mSoundFormat;

    AlivcSoundFormat(int i) {
        this.mSoundFormat = i;
    }

    public int getAlivcSoundFormat() {
        return this.mSoundFormat;
    }
}
