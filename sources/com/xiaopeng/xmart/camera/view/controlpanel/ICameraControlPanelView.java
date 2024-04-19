package com.xiaopeng.xmart.camera.view.controlpanel;
/* loaded from: classes.dex */
public interface ICameraControlPanelView {

    /* loaded from: classes.dex */
    public interface OnCameraModeChangeListener {
        void onCameraModeChange(boolean isPhoto);
    }

    void onPictureTaken(String path);

    void onRecordError();

    void onRecordStart();

    void onRecordStop(String videoPath);

    void showPictureThumbnail(String path);
}
