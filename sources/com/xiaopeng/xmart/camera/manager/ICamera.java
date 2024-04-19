package com.xiaopeng.xmart.camera.manager;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
/* loaded from: classes.dex */
public interface ICamera {
    Camera openCamera(int cameraId);

    void releaseCamera() throws Exception;

    boolean startPreview(SurfaceTexture surfaceTexture) throws Exception;

    boolean startPreview(SurfaceHolder holder) throws Exception;

    void stopPreview() throws Exception;

    void takePicture(Camera.PictureCallback jpeg);
}
