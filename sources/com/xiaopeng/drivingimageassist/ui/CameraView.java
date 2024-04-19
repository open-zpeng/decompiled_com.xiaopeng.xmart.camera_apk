package com.xiaopeng.drivingimageassist.ui;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import com.xiaopeng.drivingimageassist.R;
import com.xiaopeng.drivingimageassist.utils.CameraUtil;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.Iterator;
/* loaded from: classes.dex */
public class CameraView extends FrameLayout implements TextureView.SurfaceTextureListener, SurfaceTexture.OnFrameAvailableListener, ICamera {
    private static final String TAG = "CameraView";
    private Camera mCamera;
    private int mCameraDisplayMode;
    private TextureView mTexture;

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public CameraView(Context context) {
        super(context);
        this.mCameraDisplayMode = 10;
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCameraDisplayMode = 10;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextureView textureView = (TextureView) findViewById(R.id.gl_camera);
        this.mTexture = textureView;
        textureView.setSurfaceTextureListener(this);
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.xiaopeng.drivingimageassist.ui.CameraView.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 16.0f);
            }
        });
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtils.i(TAG, "onSurfaceTextureAvailable");
        try {
            Camera open = Camera.open(2);
            this.mCamera = open;
            if (open != null) {
                Camera.Parameters parameters = open.getParameters();
                if (parameters == null) {
                    LogUtils.i(TAG, "resetCameraParameters()，parameters = null");
                    return;
                }
                Iterator<Camera.Size> it = parameters.getSupportedPreviewSizes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Camera.Size next = it.next();
                    LogUtils.i(TAG, "getSupportedPreviewSizes() = " + next.width + "x" + next.height);
                    if (next.width == 1920 && next.height == 1080) {
                        LogUtils.i(TAG, "setPreviewSize()， " + next.width + "x" + next.height);
                        parameters.setPreviewSize(1920, 1080);
                        break;
                    }
                }
                this.mCamera.setParameters(parameters);
                CameraUtil.setCameraDisplayMode(this.mCamera, this.mCameraDisplayMode);
                this.mCamera.setPreviewTexture(surface);
            }
            this.mCamera.startPreview();
            postDelayed(new Runnable() { // from class: com.xiaopeng.drivingimageassist.ui.CameraView.2
                @Override // java.lang.Runnable
                public void run() {
                }
            }, 3000L);
        } catch (Exception e) {
            LogUtils.i(TAG, "onSurfaceTextureAvailable :" + e);
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.i(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtils.i(TAG, "onSurfaceTextureDestroyed");
        try {
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        } catch (Exception e) {
            LogUtils.i(TAG, "onSurfaceTextureDestroyed :" + e);
        }
        surface.setOnFrameAvailableListener(null);
        return true;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        LogUtils.i(TAG, "onFrameAvailable");
    }

    @Override // com.xiaopeng.drivingimageassist.ui.ICamera
    public void switchCamera(int type) {
        this.mCameraDisplayMode = type;
        Camera camera = this.mCamera;
        if (camera != null) {
            CameraUtil.setCameraDisplayMode(camera, type);
        }
    }
}
