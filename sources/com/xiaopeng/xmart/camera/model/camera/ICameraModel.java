package com.xiaopeng.xmart.camera.model.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import com.xiaopeng.xmart.camera.model.camera.CameraModel;
/* loaded from: classes.dex */
public interface ICameraModel {

    /* loaded from: classes.dex */
    public interface Callback {
        void onCameraOpen(boolean openSuccess);

        void onCameraPreview(boolean previewSuccess);

        void onCameraRelease();

        void onPictureTaken(String picPath);

        void onRecordEnd(boolean recordEnd, String videoPath);

        void onRecordStart(boolean recordStart, String videoPath);
    }

    int getBucketId();

    Camera getCamera();

    String getFileDir();

    String getGalleryTab();

    String getLatestPicturePath();

    void gotoGalleryDetail(String filePath);

    boolean isCameraInitApi2();

    void openCamera(int cameraId);

    void release();

    void resetData();

    void startDownCounter(CameraModel.RecordTimeListener listener);

    void startPreview(SurfaceTexture surfaceTexture);

    void startPreview(SurfaceHolder holder);

    void startRecord();

    void startRecord(String videoPath);

    void stopDownCounter();

    void stopPreview();

    void stopRecord();

    void takePicture();
}
