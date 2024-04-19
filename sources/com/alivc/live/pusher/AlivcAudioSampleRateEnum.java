package com.alivc.live.pusher;

import com.xiaopeng.libconfig.ipc.IpcConfig;
/* loaded from: classes.dex */
public enum AlivcAudioSampleRateEnum {
    AUDIO_SAMPLE_RATE_16000(IpcConfig.CarControlConfig.REMOTE_COMMAND_DVR),
    AUDIO_SAMPLE_RATE_32000(AlivcLivePushConstants.DEFAULT_VALUE_INT_AUDIO_SAMPLE_RATE),
    AUDIO_SAMPLE_RATE_44100(44100),
    AUDIO_SAMPLE_RATE_48000(48000);
    
    private int mAudioSampleRate;

    AlivcAudioSampleRateEnum(int i) {
        this.mAudioSampleRate = i;
    }

    public int getAudioSampleRate() {
        return this.mAudioSampleRate;
    }
}
