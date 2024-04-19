package com.xiaopeng.xmart.livepush;
/* loaded from: classes.dex */
public interface RemoteControlHandler extends RemoteControlIpcCommand {

    /* loaded from: classes.dex */
    public interface RemoteCmdStatus {
        public static final int PENDING = 2;
        public static final int START = 1;
        public static final int STOP = 0;
        public static final int WAITING = 3;
    }

    void feedbackCameraNotAllowed(long delay);

    void feedbackCameraStatus(long delay);

    void feedbackCameraStatus(String status);

    int getIgStatus();

    boolean isCarServiceConnected();

    void onLiveUrlRequested(String pushUrl, String getUrl);

    void onPreviewStarted();

    void requestLiveUrl();
}
