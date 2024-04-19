package com.alivc.component.capture;

import android.content.Context;
import com.alivc.component.capture.AudioPusher;
import com.alivc.live.pusher.LogUtil;
import com.alivc.live.pusher.b;
/* loaded from: classes.dex */
public class AudioPusherJNI {
    private AudioPusher mAudioPusher;
    private AudioPusher.AudioSourceListener mAudioPusherDataListener = new AudioPusher.AudioSourceListener() { // from class: com.alivc.component.capture.AudioPusherJNI.1
        @Override // com.alivc.component.capture.AudioPusher.AudioSourceListener
        public void onAudioFrame(byte[] bArr, int i, long j, int i2, int i3, int i4) {
            AudioPusherJNI.this.onData(bArr, i, j, i2, i3, i4);
        }
    };
    private long mNativeHandler;

    public AudioPusherJNI(long j) {
        this.mNativeHandler = 0L;
        this.mAudioPusher = null;
        LogUtil.d("AudioPusherJNI", "ME ME ME, AudioPusherJNI construct " + j);
        if (this.mAudioPusher == null) {
            AudioPusher audioPusher = new AudioPusher();
            this.mAudioPusher = audioPusher;
            audioPusher.setAudioSourceListener(this.mAudioPusherDataListener);
        }
        this.mNativeHandler = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public native int onData(byte[] bArr, int i, long j, int i2, int i3, int i4);

    private native int onStarted();

    private native int onStopped();

    public void destroy() {
        LogUtil.d("AudioPusherJNI", "AudioPusherJNI destroy");
        AudioPusher audioPusher = this.mAudioPusher;
        if (audioPusher != null) {
            audioPusher.stop();
            this.mAudioPusher = null;
        }
        this.mNativeHandler = 0L;
    }

    public long getAudioHandler() {
        return this.mNativeHandler;
    }

    public void init(int i, int i2, int i3, int i4, int i5, Context context) {
        LogUtil.d("AudioPusherJNI", "AudioPusherJNI init : src " + i + ", channel " + i2 + ", sampelrate " + i3 + ", framesize " + i5);
        AudioPusher audioPusher = this.mAudioPusher;
        if (audioPusher != null) {
            audioPusher.init(i, b.b(i2), i3, b.a(i4), i5, context);
        }
    }

    public void pause() {
        LogUtil.d("AudioPusherJNI", "AudioPusherJNI pause");
        AudioPusher audioPusher = this.mAudioPusher;
        if (audioPusher != null) {
            audioPusher.pause();
        }
    }

    public void resume() {
        LogUtil.d("AudioPusherJNI", "AudioPusherJNI resume");
        AudioPusher audioPusher = this.mAudioPusher;
        if (audioPusher != null) {
            audioPusher.resume();
        }
    }

    public int start() {
        LogUtil.d("AudioPusherJNI", "AudioPusherJNI start");
        AudioPusher audioPusher = this.mAudioPusher;
        if (audioPusher != null) {
            try {
                audioPusher.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.mAudioPusher.isPushing() ? 0 : -1;
        }
        return -1;
    }

    public void stop() {
        LogUtil.d("AudioPusherJNI", "AudioPusherJNI stop");
        AudioPusher audioPusher = this.mAudioPusher;
        if (audioPusher != null) {
            audioPusher.stop();
        }
    }
}
