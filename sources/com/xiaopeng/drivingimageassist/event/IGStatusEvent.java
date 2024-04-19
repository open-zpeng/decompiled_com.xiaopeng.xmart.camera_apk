package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class IGStatusEvent {
    int mIGStatus;

    public int getIGStatus() {
        return this.mIGStatus;
    }

    public boolean isOff() {
        return this.mIGStatus == 0;
    }

    public boolean isOn() {
        return this.mIGStatus == 1;
    }

    public IGStatusEvent(int status) {
        this.mIGStatus = status;
    }
}
