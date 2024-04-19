package com.alivc.live.pusher;
/* loaded from: classes.dex */
public interface AlivcLivePushInfoListener {
    void onAdjustBitRate(AlivcLivePusher alivcLivePusher, int i, int i2);

    void onAdjustFps(AlivcLivePusher alivcLivePusher, int i, int i2);

    void onDropFrame(AlivcLivePusher alivcLivePusher, int i, int i2);

    void onFirstAVFramePushed(AlivcLivePusher alivcLivePusher);

    void onFirstFramePreviewed(AlivcLivePusher alivcLivePusher);

    void onPreviewStarted(AlivcLivePusher alivcLivePusher);

    void onPreviewStoped(AlivcLivePusher alivcLivePusher);

    void onPushPauesed(AlivcLivePusher alivcLivePusher);

    void onPushRestarted(AlivcLivePusher alivcLivePusher);

    void onPushResumed(AlivcLivePusher alivcLivePusher);

    void onPushStarted(AlivcLivePusher alivcLivePusher);

    void onPushStoped(AlivcLivePusher alivcLivePusher);
}
