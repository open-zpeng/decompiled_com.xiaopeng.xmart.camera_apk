package com.xiaopeng.xmart.camera.presenter;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import com.xiaopeng.xmart.camera.model.camera.ICameraModel;
/* loaded from: classes.dex */
public interface ICameraPresenter extends ICameraModel.Callback {
    void closeCamera();

    void destroy();

    Camera getCamera();

    String getLatestPicturePath();

    void gotoGalleryDetail(String path);

    void hidePreviewCover();

    boolean isCurrentCameraThumbnail(String path);

    boolean isPreviewOpen();

    boolean isRecording();

    boolean isShowPreviewCover();

    boolean isSurfaceViewPreview();

    boolean isTakingPic();

    void onTableChanged();

    void openCamera(int cameraId);

    void showPreviewCover();

    void startPreview(SurfaceTexture surfaceTexture);

    void startPreview(SurfaceHolder holder);

    void startRecord();

    void stopPreview();

    void stopRecord();

    void takePicture();
}
