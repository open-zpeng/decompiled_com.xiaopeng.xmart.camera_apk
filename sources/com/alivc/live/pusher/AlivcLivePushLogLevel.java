package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcLivePushLogLevel {
    AlivcLivePushLogLevelAll(1),
    AlivcLivePushLogLevelVerbose(2),
    AlivcLivePushLogLevelDebug(3),
    AlivcLivePushLogLevelInfo(4),
    AlivcLivePushLogLevelWarn(5),
    AlivcLivePushLogLevelError(6),
    AlivcLivePushLogLevelFatal(7);
    
    private int mLevel;

    AlivcLivePushLogLevel(int i) {
        this.mLevel = i;
    }

    public int getLevel() {
        return this.mLevel;
    }
}
