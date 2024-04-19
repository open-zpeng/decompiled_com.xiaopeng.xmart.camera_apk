package com.alivc.live.pusher;
/* loaded from: classes.dex */
public interface AlivcLivePushNetworkListener {
    void onConnectFail(AlivcLivePusher alivcLivePusher);

    void onConnectionLost(AlivcLivePusher alivcLivePusher);

    void onNetworkPoor(AlivcLivePusher alivcLivePusher);

    void onNetworkRecovery(AlivcLivePusher alivcLivePusher);

    void onPacketsLost(AlivcLivePusher alivcLivePusher);

    String onPushURLAuthenticationOverdue(AlivcLivePusher alivcLivePusher);

    void onReconnectFail(AlivcLivePusher alivcLivePusher);

    void onReconnectStart(AlivcLivePusher alivcLivePusher);

    void onReconnectSucceed(AlivcLivePusher alivcLivePusher);

    void onSendDataTimeout(AlivcLivePusher alivcLivePusher);

    void onSendMessage(AlivcLivePusher alivcLivePusher);
}
