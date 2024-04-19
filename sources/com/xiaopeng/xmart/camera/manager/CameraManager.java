package com.xiaopeng.xmart.camera.manager;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import androidx.core.app.ActivityCompat;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.BIConfig;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.CarFunction;
import com.xiaopeng.xmart.camera.utils.FileUtils;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public class CameraManager implements ICamera {
    private static final int RECORD_BITRATE_1080P = 8000000;
    private static final int RECORD_BITRATE_720P = 4000000;
    private static final int RECORD_WIDTH_1080P = 1920;
    private static final String TAG = "CameraManager";
    public static CameraManager mInstance;
    private HandlerThread mBgCameraThread;
    private Camera mCamera;
    private Camera2DeviceCallback mCamera2Cb;
    private CameraDevice mCameraDevice;
    private Handler mCameraHandler;
    private android.hardware.camera2.CameraManager mCameraManager;
    private CameraCaptureSession mCaptureSession;
    private ImageReader mImageReader;
    private boolean mIsRecoding;
    private int mMagicVideoHeight;
    private int mMagicVideoWidth;
    private MediaRecorder mMediaRecorder;
    private Camera.PictureCallback mPicCallback;
    private Camera2PreviewCallback mPreviewCb;
    private boolean mPreviewOpened;
    private CaptureRequest mPreviewRequest;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private int mVideoHeight;
    private String mVideoPath;
    private int mVideoWidth;
    private int mCameraId = -1;
    private ConditionVariable mCamera2OpenCloseLock = new ConditionVariable();
    private boolean mTakingPic = false;
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() { // from class: com.xiaopeng.xmart.camera.manager.CameraManager.1
        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            CameraLog.i(CameraManager.TAG, "onOpened mCameraDevice:" + cameraDevice, false);
            CameraManager.this.mCameraDevice = cameraDevice;
            CameraManager.this.mCamera2OpenCloseLock.open();
            CameraManager.this.mCamera2Cb.onOpened(cameraDevice);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            CameraLog.i(CameraManager.TAG, "onDisconnected" + cameraDevice, false);
            CameraManager.this.mCamera2OpenCloseLock.open();
            CameraManager.this.mCaptureSession = null;
            CameraManager.this.releaseCameraApi2();
            CameraManager.this.mCamera2Cb.onDisconnected(cameraDevice);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int error) {
            CameraLog.i(CameraManager.TAG, "onError:" + error, false);
            CameraManager.this.mCamera2OpenCloseLock.open();
            CameraManager.this.releaseCameraApi2();
            CameraManager.this.mCamera2Cb.onError(cameraDevice);
        }
    };
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() { // from class: com.xiaopeng.xmart.camera.manager.CameraManager.2
        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader reader) {
            Image acquireNextImage = reader.acquireNextImage();
            ByteBuffer buffer = acquireNextImage.getPlanes()[0].getBuffer();
            byte[] bArr = new byte[buffer.remaining()];
            buffer.get(bArr);
            acquireNextImage.close();
            CameraManager.this.mTakingPic = false;
            if (CameraManager.this.mPicCallback != null) {
                CameraManager.this.mPicCallback.onPictureTaken(bArr, null);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Camera2DeviceCallback {
        void onDisconnected(CameraDevice camera);

        void onError(CameraDevice camera);

        void onOpened(CameraDevice camera);
    }

    /* loaded from: classes.dex */
    public interface Camera2PreviewCallback {
        void onPreviewFailed();

        void onPreviewSuccess();
    }

    public static CameraManager getInstance() {
        if (mInstance == null) {
            mInstance = new CameraManager();
        }
        return mInstance;
    }

    @Override // com.xiaopeng.xmart.camera.manager.ICamera
    public Camera openCamera(int cameraId) throws RuntimeException {
        CameraLog.d(TAG, "openCamera " + cameraId + " mCamera:" + this.mCamera, false);
        if (this.mCameraId == cameraId && this.mCamera != null) {
            CameraLog.d(TAG, "Camera " + cameraId + " already opened", false);
            return this.mCamera;
        }
        Camera open = Camera.open(cameraId);
        this.mCamera = open;
        this.mCameraId = cameraId;
        Camera.Parameters parameters = open.getParameters();
        if (cameraId == 2 && !Build.DEVICE.equals("au8155_xp")) {
            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                if (size.width == 1280 && size.height == 720) {
                    parameters.setPreviewSize(1280, 720);
                }
            }
        }
        List<Camera.Size> supportedVideoSizes = parameters.getSupportedVideoSizes();
        if (supportedVideoSizes != null && !supportedVideoSizes.isEmpty()) {
            this.mVideoWidth = supportedVideoSizes.get(0).width;
            this.mVideoHeight = supportedVideoSizes.get(0).height;
            if (supportedVideoSizes.size() > 1) {
                this.mMagicVideoWidth = supportedVideoSizes.get(1).width;
                this.mMagicVideoHeight = supportedVideoSizes.get(1).height;
            }
            CameraLog.d(TAG, "supportedVideoSize width:" + this.mVideoWidth + " height:" + this.mVideoHeight + " mMagicVideoWidth:" + this.mMagicVideoWidth + " mMagicVideoHeight:" + this.mMagicVideoHeight, false);
        }
        parameters.setPreviewFormat(17);
        this.mCamera.setParameters(parameters);
        CameraLog.d(TAG, "openCamera mCamera is ? " + this.mCamera, false);
        return this.mCamera;
    }

    public void openCameraApi2(int cameraId, Camera2DeviceCallback callback) throws Exception {
        CameraLog.i(TAG, "openCameraApi2:" + cameraId, false);
        this.mTakingPic = false;
        startCameraBgThread();
        this.mCamera2Cb = callback;
        android.hardware.camera2.CameraManager cameraManager = (android.hardware.camera2.CameraManager) App.getInstance().getSystemService(BIConfig.MODULE_CAMERA);
        this.mCameraManager = cameraManager;
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraManager.getCameraCharacteristics(String.valueOf(cameraId)).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            CameraLog.i(TAG, "get camera:" + cameraId + "stream config failed", false);
            return;
        }
        Size size = (Size) Collections.max(Arrays.asList(streamConfigurationMap.getOutputSizes(256)), new Comparator() { // from class: com.xiaopeng.xmart.camera.manager.-$$Lambda$CameraManager$ZDwoNjItDbkjg_FE4j2MAu1wT8I
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int signum;
                Size size2 = (Size) obj;
                Size size3 = (Size) obj2;
                signum = Long.signum((size2.getWidth() * size2.getHeight()) - (size3.getWidth() * size3.getHeight()));
                return signum;
            }
        });
        ImageReader newInstance = ImageReader.newInstance(size.getWidth(), size.getHeight(), 256, 2);
        this.mImageReader = newInstance;
        newInstance.setOnImageAvailableListener(this.mOnImageAvailableListener, this.mCameraHandler);
        try {
            this.mCameraManager.openCamera(String.valueOf(cameraId), this.mStateCallback, this.mCameraHandler);
        } catch (CameraAccessException e) {
            LogUtils.d(TAG, "openCameraApi2 failed! Reason: " + e.getMessage());
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException("camera open exception:", e2);
        }
    }

    @Override // com.xiaopeng.xmart.camera.manager.ICamera
    public boolean startPreview(SurfaceHolder holder) throws Exception {
        CameraLog.d(TAG, "Surface " + (holder.getSurface() != null ? " is not null" : "is null"), false);
        CameraLog.d(TAG, "startPreview by SurfaceView mCamera:" + this.mCamera + " mPreviewOpened:" + this.mPreviewOpened, false);
        if (this.mCamera != null && holder.getSurface() != null && !this.mPreviewOpened) {
            if (holder.getSurface().isValid()) {
                this.mCamera.setPreviewDisplay(holder);
                CameraLog.i(TAG, "camera startPreview");
                this.mCamera.startPreview();
                CameraLog.i(TAG, "startPreview success");
                this.mPreviewOpened = true;
                return true;
            }
            CameraLog.i(TAG, "Surface is not valid");
        }
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.manager.ICamera
    public boolean startPreview(SurfaceTexture surfaceTexture) throws Exception {
        CameraLog.i(TAG, "startPreview by surfaceTexture mCamera:" + this.mCamera + " mPreviewOpened:" + this.mPreviewOpened + ",isRelease:" + (surfaceTexture == null ? null : Boolean.valueOf(surfaceTexture.isReleased())), false);
        if (this.mCamera != null && surfaceTexture != null && !surfaceTexture.isReleased() && !this.mPreviewOpened) {
            CameraLog.i(TAG, "camera startPreview", false);
            this.mCamera.setPreviewTexture(surfaceTexture);
            this.mCamera.startPreview();
            CameraLog.i(TAG, "startPreview success", false);
            this.mPreviewOpened = true;
            return true;
        }
        CameraLog.i(TAG, "Surface is not valid", false);
        return false;
    }

    public void startPreviewApi2(SurfaceHolder surfaceHolder) {
        try {
            CameraLog.i(TAG, "startPreviewApi2", false);
            createCameraPreviewSession(surfaceHolder);
        } catch (CameraAccessException e) {
            Camera2PreviewCallback camera2PreviewCallback = this.mPreviewCb;
            if (camera2PreviewCallback != null) {
                camera2PreviewCallback.onPreviewFailed();
            }
            e.printStackTrace();
        }
    }

    public void startPreviewApi2(SurfaceTexture surfaceTexture) {
        try {
            CameraLog.i(TAG, "startPreviewApi2", false);
            createCameraPreviewSession(surfaceTexture);
        } catch (CameraAccessException e) {
            Camera2PreviewCallback camera2PreviewCallback = this.mPreviewCb;
            if (camera2PreviewCallback != null) {
                camera2PreviewCallback.onPreviewFailed();
            }
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession(final SurfaceHolder surfaceHolder) throws CameraAccessException, IllegalArgumentException {
        CameraLog.i(TAG, "createCameraPreviewSession", false);
        CameraDevice cameraDevice = this.mCameraDevice;
        if (cameraDevice != null) {
            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), this.mImageReader.getSurface()), new CameraCaptureSession.StateCallback() { // from class: com.xiaopeng.xmart.camera.manager.CameraManager.3
                @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    CameraLog.i(CameraManager.TAG, "onConfigured:" + CameraManager.this.mCameraDevice, false);
                    if (CameraManager.this.mCameraDevice == null) {
                        if (CameraManager.this.mPreviewCb != null) {
                            CameraManager.this.mPreviewCb.onPreviewFailed();
                            return;
                        }
                        return;
                    }
                    CameraManager.this.startPreviewApi2Inner(cameraCaptureSession, surfaceHolder.getSurface());
                }

                @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    if (CameraManager.this.mPreviewCb != null) {
                        CameraManager.this.mPreviewCb.onPreviewFailed();
                    }
                }
            }, this.mCameraHandler);
        }
    }

    private void createCameraPreviewSession(SurfaceTexture surfaceTexture) throws CameraAccessException, IllegalArgumentException {
        CameraLog.i(TAG, "createCameraPreviewSession", false);
        if (this.mCameraDevice != null) {
            if (CarCameraHelper.getInstance().hasAVM()) {
                surfaceTexture.setDefaultBufferSize(1920, 1080);
            } else {
                surfaceTexture.setDefaultBufferSize(1280, 720);
            }
            final Surface surface = new Surface(surfaceTexture);
            this.mCameraDevice.createCaptureSession(Arrays.asList(surface, this.mImageReader.getSurface()), new CameraCaptureSession.StateCallback() { // from class: com.xiaopeng.xmart.camera.manager.CameraManager.4
                @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    CameraLog.i(CameraManager.TAG, "onConfigured:" + CameraManager.this.mCameraDevice, false);
                    if (CameraManager.this.mCameraDevice == null) {
                        if (CameraManager.this.mPreviewCb != null) {
                            CameraManager.this.mPreviewCb.onPreviewFailed();
                            return;
                        }
                        return;
                    }
                    CameraManager.this.startPreviewApi2Inner(cameraCaptureSession, surface);
                }

                @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    if (CameraManager.this.mPreviewCb != null) {
                        CameraManager.this.mPreviewCb.onPreviewFailed();
                    }
                }
            }, this.mCameraHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPreviewApi2Inner(CameraCaptureSession cameraCaptureSession, Surface surface) throws IllegalArgumentException {
        CameraLog.i(TAG, "startPreviewApi2Inner surface:" + surface, false);
        this.mCaptureSession = cameraCaptureSession;
        try {
            CaptureRequest.Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(1);
            this.mPreviewRequestBuilder = createCaptureRequest;
            createCaptureRequest.addTarget(surface);
            CaptureRequest build = this.mPreviewRequestBuilder.build();
            this.mPreviewRequest = build;
            this.mCaptureSession.setRepeatingRequest(build, null, this.mCameraHandler);
            this.mPreviewOpened = true;
            if (this.mPreviewCb != null) {
                CameraLog.i(TAG, "startPreviewApi2Inner onPreviewSuccess", false);
                this.mPreviewCb.onPreviewSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CameraLog.i(TAG, "startPreviewApi2Inner exception:" + e.getMessage(), false);
            Camera2PreviewCallback camera2PreviewCallback = this.mPreviewCb;
            if (camera2PreviewCallback != null) {
                camera2PreviewCallback.onPreviewFailed();
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.manager.ICamera
    public void stopPreview() throws Exception {
        CameraLog.i(TAG, "stopPreview", false);
        if (this.mCamera == null || !this.mPreviewOpened) {
            return;
        }
        CameraLog.i(TAG, "camera stopPreview", false);
        this.mCamera.stopPreview();
        this.mPreviewOpened = false;
    }

    public void stopPreviewApi2() {
        CameraLog.i(TAG, "stopPreviewApi2", false);
        CameraCaptureSession cameraCaptureSession = this.mCaptureSession;
        if (cameraCaptureSession != null) {
            try {
                if (this.mCameraDevice != null) {
                    try {
                        cameraCaptureSession.stopRepeating();
                    } catch (Exception e) {
                        CameraLog.d(TAG, "stopRepeating exception:" + e.getMessage(), false);
                        e.printStackTrace();
                    }
                    this.mPreviewOpened = false;
                    this.mCaptureSession.close();
                    this.mCaptureSession = null;
                }
            } catch (Exception e2) {
                CameraLog.d(TAG, "stopPreviewApi2 exception:" + e2.getMessage(), false);
                e2.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.manager.ICamera
    public void releaseCamera() throws Exception {
        CameraLog.d(TAG, "releaseCamera mCamera:" + this.mCamera + " mIsRecoding:" + this.mIsRecoding + " mPreviewOpened:" + this.mPreviewOpened, false);
        if (this.mPreviewOpened) {
            CameraLog.d(TAG, "stop preview before release camera", false);
            stopPreview();
        }
        if (this.mCamera != null) {
            CameraLog.i(TAG, "camera releaseCamera", false);
            this.mCamera.release();
            this.mCamera = null;
        }
        this.mCameraId = -1;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public void releaseCameraApi2() {
        try {
            try {
                this.mCamera2OpenCloseLock.block(3000L);
                if (this.mCameraDevice != null) {
                    if (this.mPreviewOpened && this.mCaptureSession != null) {
                        stopPreviewApi2();
                    }
                    this.mCameraDevice.close();
                    this.mCameraDevice = null;
                }
                stopCameraBgThread();
                ImageReader imageReader = this.mImageReader;
                if (imageReader != null) {
                    imageReader.close();
                    this.mImageReader = null;
                }
            } catch (Exception e) {
                CameraLog.d(TAG, e.getMessage(), false);
            }
        } finally {
            this.mCamera2OpenCloseLock.close();
        }
    }

    private void startCameraBgThread() {
        HandlerThread handlerThread = new HandlerThread("CameraBgThread");
        this.mBgCameraThread = handlerThread;
        handlerThread.start();
        this.mCameraHandler = new Handler(this.mBgCameraThread.getLooper());
    }

    private void stopCameraBgThread() {
        try {
            Handler handler = this.mCameraHandler;
            if (handler != null) {
                handler.removeCallbacks(null);
                this.mBgCameraThread.quitSafely();
                this.mBgCameraThread.join();
                this.mBgCameraThread = null;
                this.mCameraHandler = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.xmart.camera.manager.ICamera
    public void takePicture(Camera.PictureCallback jpeg) {
        CameraLog.d(TAG, "take picture", false);
        if (isSupportCameraApi2()) {
            try {
                CameraDevice cameraDevice = this.mCameraDevice;
                if (cameraDevice != null && this.mCaptureSession != null && this.mPreviewOpened && !this.mTakingPic) {
                    this.mTakingPic = true;
                    this.mPicCallback = jpeg;
                    CaptureRequest.Builder createCaptureRequest = cameraDevice.createCaptureRequest(2);
                    createCaptureRequest.addTarget(this.mImageReader.getSurface());
                    createCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, 0);
                    CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() { // from class: com.xiaopeng.xmart.camera.manager.CameraManager.5
                        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                            CameraLog.d(CameraManager.TAG, "onCaptureCompleted", false);
                            CameraManager.this.mPreviewRequestBuilder.removeTarget(CameraManager.this.mImageReader.getSurface());
                            CameraManager cameraManager = CameraManager.this;
                            cameraManager.mPreviewRequest = cameraManager.mPreviewRequestBuilder.build();
                            try {
                                CameraManager.this.mCaptureSession.setRepeatingRequest(CameraManager.this.mPreviewRequest, null, CameraManager.this.mCameraHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                                CameraManager.this.mTakingPic = false;
                            }
                        }
                    };
                    this.mCaptureSession.stopRepeating();
                    this.mCaptureSession.abortCaptures();
                    this.mCaptureSession.capture(createCaptureRequest.build(), captureCallback, this.mCameraHandler);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                this.mTakingPic = false;
                CameraLog.d(TAG, "takePicture exception:" + e.getMessage());
            }
        } else if (this.mCamera == null || !this.mPreviewOpened) {
        } else {
            try {
                CameraLog.d(TAG, "camera take picture");
                this.mCamera.takePicture(null, null, jpeg);
            } catch (Exception e2) {
                e2.printStackTrace();
                CameraLog.d(TAG, "take picture exception:" + e2.getMessage(), false);
            }
        }
    }

    public boolean startRecord(String videoPath) {
        CameraLog.d(TAG, "enter startRecord", false);
        if (prepareMediaRecorder(videoPath)) {
            try {
                this.mMediaRecorder.start();
                CameraLog.d(TAG, "startRecord success");
                this.mIsRecoding = true;
                CarCameraHelper.getInstance().setCameraRecording(true);
                return true;
            } catch (Exception e) {
                CameraLog.d(TAG, "startRecord failed e:" + e.getMessage());
                FileUtils.deleteFile(videoPath);
                e.printStackTrace();
                this.mIsRecoding = false;
                CarCameraHelper.getInstance().setCameraRecording(false);
                return false;
            }
        }
        CameraLog.d(TAG, "startRecord failed");
        return false;
    }

    public boolean startRecord(String videoPath, int maxDurationMs) {
        CameraLog.d(TAG, "enter startRecord", false);
        if (prepareMediaRecorder(videoPath, maxDurationMs)) {
            try {
                this.mMediaRecorder.start();
                CameraLog.d(TAG, "startRecord success");
                this.mIsRecoding = true;
                CarCameraHelper.getInstance().setCameraRecording(true);
                return true;
            } catch (Exception e) {
                CameraLog.d(TAG, "startRecord failed e:" + e.getMessage());
                FileUtils.deleteFile(videoPath);
                e.printStackTrace();
                this.mIsRecoding = false;
                CarCameraHelper.getInstance().setCameraRecording(false);
                return false;
            }
        }
        CameraLog.d(TAG, "startRecord failed");
        return false;
    }

    private boolean prepareMediaRecorder(String videoPath) {
        return prepareMediaRecorder(videoPath, CameraDefine.CarCamera.VIDEO_RECORD_MAX_DURATION);
    }

    private boolean prepareMediaRecorder(String videoPath, int maxDurationMs) {
        int i;
        try {
            boolean isMagicPath = isMagicPath(videoPath);
            CameraLog.d(TAG, "prepareMediaRecorder is Magic Video:" + isMagicPath, false);
            this.mMediaRecorder = new MediaRecorder();
            Camera camera = this.mCamera;
            if (camera != null) {
                camera.unlock();
                this.mMediaRecorder.setCamera(this.mCamera);
            }
            this.mMediaRecorder.setVideoSource(1);
            CamcorderProfile camcorderProfile = CamcorderProfile.get(0, 5);
            if (isMagicPath && (i = this.mMagicVideoWidth) > 0 && this.mMagicVideoHeight > 0) {
                camcorderProfile.videoFrameWidth = i;
                camcorderProfile.videoFrameHeight = this.mMagicVideoHeight;
                camcorderProfile.videoBitRate = RECORD_BITRATE_720P;
            } else {
                int i2 = this.mVideoWidth;
                if (i2 > 0 && this.mVideoHeight > 0) {
                    camcorderProfile.videoFrameWidth = i2;
                    camcorderProfile.videoFrameHeight = this.mVideoHeight;
                    if (camcorderProfile.videoFrameWidth >= 1920) {
                        camcorderProfile.videoBitRate = RECORD_BITRATE_1080P;
                    } else {
                        camcorderProfile.videoBitRate = RECORD_BITRATE_720P;
                    }
                }
            }
            CameraLog.d(TAG, "update CamcorderProfile video width:" + camcorderProfile.videoFrameWidth + "video height:" + camcorderProfile.videoFrameHeight, false);
            this.mMediaRecorder.setProfile(camcorderProfile);
            this.mMediaRecorder.setMaxDuration(maxDurationMs);
            this.mVideoPath = videoPath;
            CameraLog.d(TAG, "record path:" + videoPath);
            this.mMediaRecorder.setOutputFile(videoPath);
            this.mMediaRecorder.prepare();
            return true;
        } catch (Exception e) {
            releaseMediaRecorder();
            CameraLog.d(TAG, "startRecord exception delete video file" + videoPath, false);
            e.printStackTrace();
            CameraLog.d(TAG, "prepareMediaRecorder exception:" + e.getMessage(), false);
            return false;
        }
    }

    public String stopRecording() {
        CameraLog.d(TAG, "enter stopRecording", false);
        try {
            try {
                MediaRecorder mediaRecorder = this.mMediaRecorder;
                if (mediaRecorder != null && this.mIsRecoding) {
                    mediaRecorder.stop();
                    this.mIsRecoding = false;
                    CarCameraHelper.getInstance().setCameraRecording(false);
                    CameraLog.d(TAG, "stopRecording,video file path:" + this.mVideoPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                CameraLog.d(TAG, "stopRecording exception delete video file" + this.mVideoPath, false);
                this.mIsRecoding = false;
                CarCameraHelper.getInstance().setCameraRecording(false);
                FileUtils.deleteFile(this.mVideoPath);
                this.mVideoPath = null;
            }
            return this.mVideoPath;
        } finally {
            releaseMediaRecorder();
        }
    }

    private void releaseMediaRecorder() {
        try {
            CameraLog.d(TAG, "enter releaseMediaRecorder", false);
            MediaRecorder mediaRecorder = this.mMediaRecorder;
            if (mediaRecorder != null) {
                mediaRecorder.reset();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
                CameraLog.d(TAG, "releaseMediaRecorder success");
            }
        } catch (Exception e) {
            CameraLog.d(TAG, "releaseMediaRecorder exception:" + e.getMessage());
        }
    }

    public boolean isCameraOpened() {
        return this.mCamera != null;
    }

    public boolean isCameraPreviewStarted() {
        return this.mPreviewOpened;
    }

    public void setAVMDisplayMode(int mode) throws Exception {
        CameraLog.d(TAG, "setAVMDisplayMode mode:" + mode, false);
        CarCameraHelper.getInstance().getCarCamera().set360CameraType(mode);
        if (isSupportCameraApi2()) {
            setAVMDisplayModeApi2(mode);
        } else if (Config.getXpCduType().equals("Q1") || Config.getXpCduType().equals("Q3") || Config.getXpCduType().equals("Q3A") || Config.getXpCduType().equals("Q7") || Config.getXpCduType().equals(CarFunction.F30) || Config.getXpCduType().equals("Q8")) {
            Camera camera = this.mCamera;
            if (camera != null) {
                try {
                    Camera.Parameters cmdControlParameters = camera.getCmdControlParameters();
                    CameraLog.d(TAG, "---getCmdControlParameters");
                    cmdControlParameters.setAvmDisplayMode(mode);
                    cmdControlParameters.setAvmOverlayWorkSt(0);
                    if (mode != CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS) {
                        cmdControlParameters.setAvmTransparentChassisWorkSt(0);
                    } else {
                        cmdControlParameters.setAvmTransparentChassisWorkSt(1);
                    }
                    CameraLog.d(TAG, "---setCmdControlParameters");
                    this.mCamera.setCmdControlParameters(cmdControlParameters);
                } catch (Exception e) {
                    CameraLog.d(TAG, "setCameraDisplayMode exception:" + e.getMessage(), false);
                }
            }
        } else {
            Camera camera2 = this.mCamera;
            if (camera2 != null) {
                try {
                    Camera.Parameters cmdControlParameters2 = camera2.getCmdControlParameters();
                    cmdControlParameters2.setAvmDisplayMode(mode);
                    this.mCamera.setCmdControlParameters(cmdControlParameters2);
                } catch (Exception e2) {
                    CameraLog.d(TAG, "---" + e2.getMessage());
                }
            }
        }
    }

    public void setAVMDisplayMode(int mode, boolean isTransChassis, boolean isTransBody, int angle) throws Exception {
        CameraLog.d(TAG, "setAVMDisplayMode mode:" + mode + " isTransChassis:" + isTransChassis + "  isTransBody:" + isTransBody + "  angle:" + angle, false);
        CarCameraHelper.getInstance().getCarCamera().set360CameraType(mode);
        if (isSupportCameraApi2()) {
            setAVMDisplayModeApi2(mode);
        } else if (Config.getXpCduType().equals("Q1") || Config.getXpCduType().equals("Q3") || Config.getXpCduType().equals("Q3A") || Config.getXpCduType().equals("Q7") || Config.getXpCduType().equals(CarFunction.F30) || Config.getXpCduType().equals("Q8")) {
            Camera camera = this.mCamera;
            if (camera != null) {
                try {
                    Camera.Parameters cmdControlParameters = camera.getCmdControlParameters();
                    CameraLog.d(TAG, "---getCmdControlParameters");
                    cmdControlParameters.setAvmDisplayMode(mode);
                    cmdControlParameters.setAvmOverlayWorkSt(0);
                    cmdControlParameters.setAvmCalibration(255);
                    cmdControlParameters.setAvmFineTuneMode(255);
                    cmdControlParameters.setAvm3603DAngel(angle);
                    int i = 1;
                    cmdControlParameters.setAvmTransparentChassisWorkSt(isTransChassis ? 1 : 0);
                    if (!isTransBody) {
                        i = 0;
                    }
                    cmdControlParameters.setAvmTransBody(i);
                    CameraLog.d(TAG, "---setCmdControlParameters");
                    this.mCamera.setCmdControlParameters(cmdControlParameters);
                } catch (Exception e) {
                    CameraLog.d(TAG, "setCameraDisplayMode exception:" + e.getMessage(), false);
                }
            }
        } else {
            Camera camera2 = this.mCamera;
            if (camera2 != null) {
                try {
                    Camera.Parameters cmdControlParameters2 = camera2.getCmdControlParameters();
                    cmdControlParameters2.setAvmDisplayMode(mode);
                    this.mCamera.setCmdControlParameters(cmdControlParameters2);
                } catch (Exception e2) {
                    CameraLog.d(TAG, "---" + e2.getMessage());
                }
            }
        }
    }

    public void setAVMDisplayModeApi2(int mode) {
        if (ActivityCompat.checkSelfPermission(App.getInstance().getApplicationContext(), "android.permission.CAMERA") != 0) {
            LogUtils.d(TAG, "setAVMDisplayModeApi2: no permission!");
            return;
        }
        if (this.mCameraManager == null) {
            this.mCameraManager = (android.hardware.camera2.CameraManager) App.getInstance().getSystemService(BIConfig.MODULE_CAMERA);
        }
        try {
            CameraManager.Parameters cmdControlParameters = this.mCameraManager.getCmdControlParameters();
            LogUtils.d(TAG, "setAVMDisplayModeApi2: mode = " + mode);
            cmdControlParameters.setAvmDisplayMode(mode);
            this.mCameraManager.setCmdControlParameters(cmdControlParameters);
        } catch (Exception e) {
            LogUtils.d(TAG, "Open camera failed(Api 2)! Reason: " + e.getMessage());
        }
    }

    public void setAvm3603DAngel(int angel) {
        CameraLog.d(TAG, "setAvm3603DAngel angel:" + angel, false);
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                Camera.Parameters cmdControlParameters = camera.getCmdControlParameters();
                cmdControlParameters.setAvmDisplayMode(22);
                cmdControlParameters.setAvmOverlayWorkSt(0);
                cmdControlParameters.setAvmCalibration(255);
                cmdControlParameters.setAvmFineTuneMode(255);
                cmdControlParameters.setAvmTransparentChassisWorkSt(0);
                cmdControlParameters.setAvm3603DAngel(angel);
                this.mCamera.setCmdControlParameters(cmdControlParameters);
            } catch (Exception e) {
                CameraLog.d(TAG, "---" + e.getMessage());
            }
        }
    }

    public int get3603dAngle() {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                return camera.getCmdControlParameters().get3603dAngle();
            } catch (Exception e) {
                CameraLog.d(TAG, "---" + e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    public void setAvmTransBody(int value) {
        CameraLog.d(TAG, "setAvmTransBody value:" + value, false);
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                Camera.Parameters cmdControlParameters = camera.getCmdControlParameters();
                cmdControlParameters.setAvmDisplayMode(22);
                cmdControlParameters.setAvmOverlayWorkSt(0);
                cmdControlParameters.setAvmCalibration(255);
                cmdControlParameters.setAvmFineTuneMode(255);
                cmdControlParameters.setAvmTransparentChassisWorkSt(0);
                cmdControlParameters.setAvmTransBody(value);
                this.mCamera.setCmdControlParameters(cmdControlParameters);
            } catch (Exception e) {
                CameraLog.d(TAG, "---" + e.getMessage());
            }
        }
    }

    public int getTransBodySwitchStatus() {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                return camera.getCmdControlParameters().getTransBodySwitchStatus();
            } catch (Exception e) {
                CameraLog.d(TAG, "---" + e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    public Camera getCamera() {
        return this.mCamera;
    }

    public MediaRecorder getMediaRecorder() {
        return this.mMediaRecorder;
    }

    private boolean isMagicPath(String videoPath) {
        return videoPath != null && videoPath.contains("record_");
    }

    public void setPreviewCallback(Camera2PreviewCallback callback) {
        this.mPreviewCb = callback;
    }

    public void setAvmTransparentChassisWorkSt(boolean open) {
        int i = open ? 1 : 0;
        try {
            CameraManager.Parameters cmdControlParameters = this.mCameraManager.getCmdControlParameters();
            cmdControlParameters.setAvmTransparentChassisWorkSt(i);
            this.mCameraManager.setCmdControlParameters(cmdControlParameters);
        } catch (Exception e) {
            CameraLog.e(TAG, "setAvmTransBodyApi2 exception, " + e.getMessage());
        }
    }

    public void setAvm3603DAngelApi2(int value) {
        try {
            CameraManager.Parameters cmdControlParameters = this.mCameraManager.getCmdControlParameters();
            cmdControlParameters.setAvm3603DAngel(value);
            this.mCameraManager.setCmdControlParameters(cmdControlParameters);
        } catch (Exception e) {
            CameraLog.e(TAG, "setAvm3603DAngelApi2 exception, " + e.getMessage());
        }
    }

    public boolean isSupportCameraApi2() {
        return Build.DEVICE.equals("au8295_xp");
    }
}
