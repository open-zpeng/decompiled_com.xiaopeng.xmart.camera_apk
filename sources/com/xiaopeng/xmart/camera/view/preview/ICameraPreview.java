package com.xiaopeng.xmart.camera.view.preview;
/* loaded from: classes.dex */
public interface ICameraPreview {
    void hidePreviewCover();

    void onCameraError();

    void onPictureTaken(String path);

    void onRecordError();

    void onRecordStart();

    void onRecordStop(String videoPath);

    void onRecordTimeTick(String timeText, boolean isEvenNumber);

    void onTakingPicture();

    void showPreviewCover(String msg);
}
