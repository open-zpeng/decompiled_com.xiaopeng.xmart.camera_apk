package com.alivc.live.pusher;
/* loaded from: classes.dex */
public interface AlivcLivePushErrorListener {
    void onSDKError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError);

    void onSystemError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError);
}
