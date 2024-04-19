package com.xiaopeng.xmart.camera.vm;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
/* loaded from: classes.dex */
public interface IBaseViewModel {
    void closeCamera();

    Camera getCamera();

    String getLatestPicturePath();

    void gotoGalleryDetail(String path);

    boolean isCameraInitApi2();

    boolean isCurrentCameraThumbnail(String path);

    void openCamera(int cameraId);

    void resetData();

    void startPreview(SurfaceTexture surfaceTexture);

    void startPreview(SurfaceHolder holder);

    void startRecord();

    void stopPreview();

    void stopRecord();

    void takePicture();
}
