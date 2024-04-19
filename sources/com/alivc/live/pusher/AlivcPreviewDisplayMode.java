package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcPreviewDisplayMode {
    ALIVC_LIVE_PUSHER_PREVIEW_SCALE_FILL(0),
    ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FIT(1),
    ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL(2);
    
    private int displayMode;

    AlivcPreviewDisplayMode(int i) {
        this.displayMode = i;
    }

    public int getPreviewDisplayMode() {
        return this.displayMode;
    }
}
