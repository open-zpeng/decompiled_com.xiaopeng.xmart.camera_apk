package com.xiaopeng.xmart.shock.model;
/* loaded from: classes2.dex */
public interface IShockModel {

    /* loaded from: classes2.dex */
    public interface Callback {
        void onCarControlFeedBack(String cmd);

        void onShockEnd();
    }

    void createUploadCacheFile(String filePath);

    void setGsensor(int gSensor);

    void setShockProcess(boolean on);

    void uploadShockMedia(String videoPath, String picPath);
}
