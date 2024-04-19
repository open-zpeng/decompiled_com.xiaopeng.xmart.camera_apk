package com.alivc.component.capture.video;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import com.alivc.live.pusher.LogUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class VideoPusher {
    private static int MIN_CAP_RESOLUTION = 192;
    private static final int SCREEN_LANDSCAPE_LEFT = 90;
    private static final int SCREEN_LANDSCAPE_RIGHT = 270;
    private static final int SCREEN_PORTRAIT = 0;
    private static final String TAG = "VideoPusher";
    private static byte[] buffer;
    private static byte[] buffer1;
    private static byte[] buffer2;
    private static int supportMaxWH;
    private Camera mCamera;
    private int mDataOrientation;
    private int mOrientation;
    private VideoParam mParam;
    private boolean mPreviewRunning;
    private int mScreen;
    private static Map<Integer, List<Camera.Size>> sSupportedResolutionMap = new HashMap();
    private static List<Integer> sSupportedFormat = new ArrayList();
    private final int TIME_MILLISECOND = 1000;
    private final float INITIATE_VALUE = -1.0f;
    private final float MAG_DELTA_DIFF = 5.0f;
    private final float ACCEL_DELTA_DIFF = 0.6f;
    private final float ACCEL_DELTAXYZ_DIFF = 1.4f;
    private boolean mPause = false;
    private VideoSourceListener mVideoSourceListener = null;
    private VideoSourceTextureListener mVideoSourceTextureListener = null;
    private boolean mSwitchCamera = false;
    private long mLastCaptureTime = 0;
    private long mLastFpsCountTime = 0;
    private int mLastFpsCounter = 0;
    private int mBelowMinFpsNumberTimes = 0;
    private int mCurrentFps = 0;
    private long mTimeDelta = 0;
    private boolean mSurfaceCbMode = false;
    private int mSurfaceTextureId = -1;
    private boolean mFlashOn = false;
    private boolean mAutoFocus = false;
    private boolean mAutoFocusing = false;
    private SurfaceTexture mSurfaceTexture = null;
    private SensorManager mSensorManager = null;
    private Sensor mAccelSensor = null;
    private Sensor mMagneticSensor = null;
    private float mLastXAccel = -1.0f;
    private float mLastYAccel = -1.0f;
    private float mLastZAccel = -1.0f;
    private float mLastXMag = -1.0f;
    private float mLastYMag = -1.0f;
    private float mLastZMag = -1.0f;
    private int mCustomRotation = 0;
    private boolean mSensorFocus = false;
    private SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() { // from class: com.alivc.component.capture.video.VideoPusher.1
        @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            VideoSourceTextureListener videoSourceTextureListener;
            long nanoTime;
            int cameraId;
            int i;
            System.currentTimeMillis();
            if (VideoPusher.this.mVideoSourceTextureListener != null) {
                if (VideoPusher.this.mTimeDelta == 0) {
                    VideoPusher.this.mTimeDelta = (System.currentTimeMillis() * 1000) - (System.nanoTime() / 1000);
                }
                if (VideoPusher.this.mParam != null) {
                    if (VideoPusher.this.mCustomRotation > 0) {
                        videoSourceTextureListener = VideoPusher.this.mVideoSourceTextureListener;
                        nanoTime = ((System.nanoTime() / 1000) + VideoPusher.this.mTimeDelta) / 1;
                        cameraId = VideoPusher.this.mParam.getCameraId();
                        i = VideoPusher.this.mCustomRotation % 360;
                    } else {
                        videoSourceTextureListener = VideoPusher.this.mVideoSourceTextureListener;
                        nanoTime = ((System.nanoTime() / 1000) + VideoPusher.this.mTimeDelta) / 1;
                        cameraId = VideoPusher.this.mParam.getCameraId();
                        i = VideoPusher.this.mDataOrientation;
                    }
                    videoSourceTextureListener.onVideoFrame(nanoTime, cameraId, i, VideoPusher.this.mParam.getWidth(), VideoPusher.this.mParam.getHeight(), 17);
                }
            }
            VideoPusher.this.mLastCaptureTime = System.currentTimeMillis();
            VideoPusher.access$608(VideoPusher.this);
        }
    };
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() { // from class: com.alivc.component.capture.video.VideoPusher.2
        @Override // android.hardware.Camera.PreviewCallback
        public void onPreviewFrame(byte[] bArr, Camera camera) {
            System.currentTimeMillis();
            if (VideoPusher.this.mVideoSourceListener != null) {
                if (VideoPusher.this.mTimeDelta == 0) {
                    VideoPusher.this.mTimeDelta = (System.currentTimeMillis() * 1000) - (System.nanoTime() / 1000);
                }
                if (!VideoPusher.this.mPause && VideoPusher.this.mParam != null) {
                    if (VideoPusher.this.mCustomRotation > 0) {
                        VideoPusher.this.mVideoSourceListener.onVideoFrame(bArr, ((System.nanoTime() / 1000) + VideoPusher.this.mTimeDelta) / 1, VideoPusher.this.mParam.getCameraId(), VideoPusher.this.mCustomRotation % 360, VideoPusher.this.mParam.getWidth(), VideoPusher.this.mParam.getHeight(), 17);
                    } else {
                        VideoPusher.this.mVideoSourceListener.onVideoFrame(bArr, ((System.nanoTime() / 1000) + VideoPusher.this.mTimeDelta) / 1, VideoPusher.this.mParam.getCameraId(), VideoPusher.this.mDataOrientation, VideoPusher.this.mParam.getWidth(), VideoPusher.this.mParam.getHeight(), 17);
                    }
                }
            }
            VideoPusher.this.mLastCaptureTime = System.currentTimeMillis();
            VideoPusher.access$608(VideoPusher.this);
            if (VideoPusher.this.mLastFpsCountTime == 0) {
                VideoPusher videoPusher = VideoPusher.this;
                videoPusher.mLastFpsCountTime = videoPusher.mLastCaptureTime;
            }
            if (VideoPusher.this.mLastCaptureTime - VideoPusher.this.mLastFpsCountTime > 1000) {
                LogUtil.d(VideoPusher.TAG, "StatLog: video capture fps = " + VideoPusher.this.mLastFpsCounter);
                VideoPusher videoPusher2 = VideoPusher.this;
                videoPusher2.mCurrentFps = videoPusher2.mLastFpsCounter;
                if (VideoPusher.this.mLastFpsCounter >= 12 || VideoPusher.this.mBelowMinFpsNumberTimes > 5) {
                    VideoPusher.this.mBelowMinFpsNumberTimes = 0;
                } else {
                    VideoPusher.access$1108(VideoPusher.this);
                }
                VideoPusher.this.mLastFpsCounter = 0;
                VideoPusher videoPusher3 = VideoPusher.this;
                videoPusher3.mLastFpsCountTime = videoPusher3.mLastCaptureTime;
            }
            if (camera != null) {
                camera.addCallbackBuffer(bArr);
            }
        }
    };
    private SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.alivc.component.capture.video.VideoPusher.4
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (VideoPusher.this.isPreviewRunning() && VideoPusher.this.mParam.getCameraId() == 0) {
                if (sensorEvent.sensor.getType() == 2) {
                    if (VideoPusher.this.mLastXMag == -1.0f) {
                        VideoPusher.this.mLastXMag = sensorEvent.values[0];
                        VideoPusher.this.mLastYMag = sensorEvent.values[1];
                        VideoPusher.this.mLastZMag = sensorEvent.values[2];
                        return;
                    }
                    float abs = Math.abs(VideoPusher.this.mLastXMag - sensorEvent.values[0]);
                    float abs2 = Math.abs(VideoPusher.this.mLastYMag - sensorEvent.values[1]);
                    float abs3 = Math.abs(VideoPusher.this.mLastZMag - sensorEvent.values[2]);
                    if ((abs > 5.0f || abs2 > 5.0f || abs3 > 5.0f) && !VideoPusher.this.mAutoFocusing) {
                        VideoPusher.this.cameraAutoFocus();
                        VideoPusher.this.mLastXMag = sensorEvent.values[0];
                        VideoPusher.this.mLastYMag = sensorEvent.values[1];
                        VideoPusher.this.mLastZMag = sensorEvent.values[2];
                    }
                }
                if (sensorEvent.sensor.getType() == 1) {
                    if (VideoPusher.this.mLastXAccel == -1.0f) {
                        VideoPusher.this.mLastXAccel = sensorEvent.values[0];
                        VideoPusher.this.mLastYAccel = sensorEvent.values[1];
                        VideoPusher.this.mLastZAccel = sensorEvent.values[2];
                        return;
                    }
                    float abs4 = Math.abs(VideoPusher.this.mLastXAccel - sensorEvent.values[0]);
                    float abs5 = Math.abs(VideoPusher.this.mLastYAccel - sensorEvent.values[1]);
                    float abs6 = Math.abs(VideoPusher.this.mLastZAccel - sensorEvent.values[2]);
                    if (Math.sqrt((abs4 * abs4) + (abs5 * abs5) + (abs6 * abs6)) > 1.399999976158142d && !VideoPusher.this.mAutoFocusing) {
                        VideoPusher.this.cameraAutoFocus();
                        VideoPusher.this.mLastXAccel = sensorEvent.values[0];
                        VideoPusher.this.mLastYAccel = sensorEvent.values[1];
                        VideoPusher.this.mLastZAccel = sensorEvent.values[2];
                    } else if ((abs4 > 0.6f || abs5 > 0.6f || abs6 > 0.6f) && !VideoPusher.this.mAutoFocusing) {
                        VideoPusher.this.cameraAutoFocus();
                        VideoPusher.this.mLastXAccel = sensorEvent.values[0];
                        VideoPusher.this.mLastYAccel = sensorEvent.values[1];
                        VideoPusher.this.mLastZAccel = sensorEvent.values[2];
                    }
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface VideoSourceListener {
        void onVideoFrame(byte[] bArr, long j, int i, int i2, int i3, int i4, int i5);
    }

    /* loaded from: classes.dex */
    public interface VideoSourceTextureListener {
        void onVideoFrame(long j, int i, int i2, int i3, int i4, int i5);
    }

    static /* synthetic */ int access$1108(VideoPusher videoPusher) {
        int i = videoPusher.mBelowMinFpsNumberTimes;
        videoPusher.mBelowMinFpsNumberTimes = i + 1;
        return i;
    }

    static /* synthetic */ int access$608(VideoPusher videoPusher) {
        int i = videoPusher.mLastFpsCounter;
        videoPusher.mLastFpsCounter = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cameraAutoFocus() {
        this.mAutoFocusing = true;
        if (this.mCamera.getParameters().getFocusMode().equals("continuous-video")) {
            return;
        }
        if (!this.mCamera.getParameters().getFocusMode().equals("auto")) {
            try {
                Camera.Parameters parameters = this.mCamera.getParameters();
                parameters.setFocusMode("auto");
                this.mCamera.setParameters(parameters);
            } catch (Exception unused) {
            }
        }
        this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.alivc.component.capture.video.VideoPusher.5
            @Override // android.hardware.Camera.AutoFocusCallback
            public void onAutoFocus(boolean z, Camera camera) {
                VideoPusher.this.mAutoFocusing = false;
            }
        });
    }

    public static List<Integer> getSupportedFormats() {
        if (sSupportedFormat.size() <= 0) {
            Camera open = Camera.open(0);
            try {
                Camera.Parameters parameters = open.getParameters();
                open.release();
                sSupportedFormat = parameters.getSupportedPreviewFormats();
            } catch (Exception unused) {
                return null;
            }
        }
        return sSupportedFormat;
    }

    public static List<Camera.Size> getSupportedResolutions(int i) {
        if (!sSupportedResolutionMap.containsKey(Integer.valueOf(i))) {
            Camera open = Camera.open(i);
            try {
                Camera.Parameters parameters = open.getParameters();
                open.release();
                sSupportedResolutionMap.put(Integer.valueOf(i), parameters.getSupportedPreviewSizes());
            } catch (Exception unused) {
                return null;
            }
        }
        return sSupportedResolutionMap.get(Integer.valueOf(i));
    }

    private boolean isHasPermission() {
        try {
            Field declaredField = this.mCamera.getClass().getDeclaredField("mHasPermission");
            declaredField.setAccessible(true);
            return ((Boolean) declaredField.get(this.mCamera)).booleanValue();
        } catch (Exception unused) {
            return true;
        }
    }

    private void preparePublisher(int i, int i2) {
        LogUtil.d(TAG, "prepare publisher. " + this.mOrientation + " " + this.mParam.getCameraId());
        if (this.mSwitchCamera || this.mPause) {
            this.mSwitchCamera = false;
        } else {
            LogUtil.d(TAG, "prepare publisher over.");
        }
    }

    private void setPreviewFpsRange(Camera.Parameters parameters) {
        int i;
        int fps = this.mParam.getFps() * 1000;
        List<int[]> supportedPreviewFpsRange = parameters.getSupportedPreviewFpsRange();
        int[] iArr = new int[2];
        if (supportedPreviewFpsRange.size() > 0) {
            int[] iArr2 = supportedPreviewFpsRange.get(0);
            i = Math.abs(iArr2[0] - fps) + Math.abs(iArr2[1] - fps);
            iArr[0] = iArr2[0];
            iArr[1] = iArr2[1];
        } else {
            i = 0;
        }
        for (int i2 = 1; i2 < supportedPreviewFpsRange.size(); i2++) {
            int[] iArr3 = supportedPreviewFpsRange.get(i2);
            int abs = Math.abs(iArr3[0] - fps) + Math.abs(iArr3[1] - fps);
            if (abs < i) {
                iArr[0] = iArr3[0];
                iArr[1] = iArr3[1];
                i = abs;
            }
        }
        try {
            parameters.setPreviewFpsRange(iArr[0], iArr[1]);
            parameters.setPreviewFrameRate(this.mParam.getFps());
        } catch (Exception unused) {
        }
        LogUtil.d(TAG, "预览帧率 fps:" + iArr[0] + " - " + iArr[1]);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0094  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void setPreviewOrientation(android.hardware.Camera.Parameters r5, int r6) {
        /*
            r4 = this;
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r0 = "SetRotation : "
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            java.lang.String r0 = "VideoPusher"
            com.alivc.live.pusher.LogUtil.d(r0, r5)
            android.hardware.Camera$CameraInfo r5 = new android.hardware.Camera$CameraInfo
            r5.<init>()
            com.alivc.component.capture.video.VideoParam r0 = r4.mParam
            int r0 = r0.getCameraId()
            android.hardware.Camera.getCameraInfo(r0, r5)
            r0 = 0
            r4.mScreen = r0
            r0 = 1
            if (r6 == 0) goto L6b
            r1 = 270(0x10e, float:3.78E-43)
            r2 = 90
            if (r6 == r2) goto L4e
            if (r6 == r1) goto L35
            goto L88
        L35:
            int r1 = r5.facing
            if (r1 != r0) goto L45
            int r1 = r5.orientation
            int r3 = r4.mOrientation
            int r1 = r1 + r3
            int r1 = r1 % 360
            r4.mDataOrientation = r1
            int r1 = 360 - r1
            goto L4c
        L45:
            int r1 = r5.orientation
            int r3 = r4.mOrientation
            int r1 = r1 - r3
            int r1 = r1 + 360
        L4c:
            int r1 = r1 + r2
            goto L84
        L4e:
            int r2 = r5.facing
            if (r2 != r0) goto L5e
            int r2 = r5.orientation
            int r3 = r4.mOrientation
            int r2 = r2 + r3
            int r2 = r2 % 360
            r4.mDataOrientation = r2
            int r2 = 360 - r2
            goto L65
        L5e:
            int r2 = r5.orientation
            int r3 = r4.mOrientation
            int r2 = r2 - r3
            int r2 = r2 + 360
        L65:
            int r2 = r2 + r1
            int r2 = r2 % 360
            r4.mDataOrientation = r2
            goto L88
        L6b:
            int r1 = r5.facing
            if (r1 != r0) goto L7d
            int r1 = r5.orientation
            int r2 = r4.mOrientation
            int r1 = r1 + r2
            int r1 = r1 % 360
            r4.mDataOrientation = r1
            int r1 = 360 - r1
            int r1 = r1 + 180
            goto L84
        L7d:
            int r1 = r5.orientation
            int r2 = r4.mOrientation
            int r1 = r1 - r2
            int r1 = r1 + 360
        L84:
            int r1 = r1 % 360
            r4.mDataOrientation = r1
        L88:
            int r1 = r5.facing
            int r5 = r5.orientation
            if (r1 != r0) goto L94
            int r5 = r5 + r6
            int r5 = r5 % 360
            int r5 = 360 - r5
            goto L97
        L94:
            int r5 = r5 - r6
            int r5 = r5 + 360
        L97:
            int r5 = r5 % 360
            android.hardware.Camera r6 = r4.mCamera
            r6.setDisplayOrientation(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alivc.component.capture.video.VideoPusher.setPreviewOrientation(android.hardware.Camera$Parameters, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00fc, code lost:
        if (r11.mParam.getWidth() < r11.mParam.getHeight()) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0110, code lost:
        if (r11.mParam.getWidth() > r11.mParam.getHeight()) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void setPreviewSize(android.hardware.Camera.Parameters r12) {
        /*
            Method dump skipped, instructions count: 436
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alivc.component.capture.video.VideoPusher.setPreviewSize(android.hardware.Camera$Parameters):void");
    }

    private void startPreview0() {
        if (this.mPreviewRunning) {
            return;
        }
        Camera open = Camera.open(this.mParam.getCameraId());
        this.mCamera = open;
        try {
            Camera.Parameters parameters = open.getParameters();
            if (!sSupportedResolutionMap.containsKey(Integer.valueOf(this.mParam.getCameraId()))) {
                sSupportedResolutionMap.put(Integer.valueOf(this.mParam.getCameraId()), parameters.getSupportedPreviewSizes());
            }
            if (sSupportedFormat.size() <= 0) {
                sSupportedFormat = parameters.getSupportedPictureFormats();
            }
            if (!isHasPermission()) {
                this.mCamera.release();
                this.mCamera = null;
                throw new Exception("permission not allowed");
            }
            if (this.mSurfaceCbMode) {
                parameters.setRecordingHint(true);
            } else {
                parameters.setPreviewFormat(17);
            }
            if (parameters.isZoomSupported()) {
                if (this.mParam.getCurrentZoom() >= parameters.getMaxZoom()) {
                    this.mParam.setCurrentZoom(parameters.getMaxZoom());
                }
                parameters.setZoom(this.mParam.getCurrentZoom());
                this.mParam.setMaxZoom(parameters.getMaxZoom());
            }
            setPreviewSize(parameters);
            setPreviewFpsRange(parameters);
            setPreviewOrientation(parameters, this.mParam.getRotation());
            try {
                this.mCamera.setParameters(parameters);
            } catch (Exception unused) {
            }
            LogUtil.d(TAG, "start camera, parameters " + parameters.getPreviewSize().width + ", " + parameters.getPreviewSize().height);
            if (buffer == null) {
                buffer = new byte[((this.mParam.getWidth() * this.mParam.getHeight()) * 3) / 2];
            }
            if (buffer1 == null) {
                buffer1 = new byte[((this.mParam.getWidth() * this.mParam.getHeight()) * 3) / 2];
            }
            if (buffer2 == null) {
                buffer2 = new byte[((this.mParam.getWidth() * this.mParam.getHeight()) * 3) / 2];
            }
            if (!this.mSurfaceCbMode || this.mSurfaceTextureId < 0) {
                this.mCamera.setPreviewCallbackWithBuffer(null);
                this.mCamera.addCallbackBuffer(buffer);
                this.mCamera.addCallbackBuffer(buffer1);
                this.mCamera.addCallbackBuffer(buffer2);
                this.mCamera.setPreviewCallbackWithBuffer(this.mPreviewCallback);
            } else {
                this.mSurfaceTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener);
            }
            this.mCamera.setPreviewTexture(this.mSurfaceTexture);
            this.mCamera.startPreview();
            this.mPreviewRunning = true;
            LogUtil.d(TAG, "start preivew over.");
        } catch (Exception unused2) {
            this.mCamera.release();
            this.mCamera = null;
            throw new Exception("permission not allowed");
        }
    }

    private static void turnLightOff(Camera camera) {
        Camera.Parameters parameters;
        if (camera == null || (parameters = camera.getParameters()) == null) {
            return;
        }
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (supportedFlashModes == null || "off".equals(flashMode)) {
            return;
        }
        if (!supportedFlashModes.contains("off")) {
            LogUtil.e(TAG, "FLASH_MODE_OFF not supported");
            return;
        }
        parameters.setFlashMode("off");
        try {
            camera.setParameters(parameters);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void turnLightOn(Camera camera) {
        Camera.Parameters parameters;
        List<String> supportedFlashModes;
        if (camera == null || (parameters = camera.getParameters()) == null || (supportedFlashModes = parameters.getSupportedFlashModes()) == null || "torch".equals(parameters.getFlashMode()) || !supportedFlashModes.contains("torch")) {
            return;
        }
        parameters.setFlashMode("torch");
        try {
            camera.setParameters(parameters);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void destroy() {
        LogUtil.d(TAG, "destroy.");
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
        this.mSurfaceTexture = null;
        this.mParam = null;
    }

    public int getCurrentExposure() {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                return camera.getParameters().getExposureCompensation();
            } catch (Exception unused) {
                return 0;
            }
        }
        return 0;
    }

    public int getCurrentFps() {
        return this.mCurrentFps;
    }

    public int getCurrentZoom() {
        Camera camera = this.mCamera;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.isZoomSupported()) {
                return parameters.getZoom();
            }
            return 1;
        }
        return 1;
    }

    public long getLastCaptureTime() {
        return this.mLastCaptureTime;
    }

    public int getMaxExposure() {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                return camera.getParameters().getMaxExposureCompensation();
            } catch (Exception unused) {
                return 0;
            }
        }
        return 0;
    }

    public int getMaxZoom() {
        Camera camera = this.mCamera;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.isZoomSupported()) {
                return parameters.getMaxZoom();
            }
            return 0;
        }
        return 0;
    }

    public int getMinExposure() {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                return camera.getParameters().getMinExposureCompensation();
            } catch (Exception unused) {
                return 0;
            }
        }
        return 0;
    }

    public void getTransformMatrix(float[] fArr) {
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture == null) {
            LogUtil.d("VideoPusherRotation", "getTransformMatrix return null !");
        } else {
            surfaceTexture.getTransformMatrix(fArr);
        }
    }

    public void init(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, Context context) {
        this.mParam = new VideoParam(i2, i3, i4, i, i5);
        this.mSurfaceCbMode = z;
        this.mCustomRotation = i6;
        if (!z) {
            this.mSurfaceTexture = new SurfaceTexture(10);
        }
        if (this.mSensorManager == null) {
            this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        }
        if (this.mAccelSensor == null) {
            this.mAccelSensor = this.mSensorManager.getDefaultSensor(1);
        }
        if (this.mMagneticSensor == null) {
            this.mMagneticSensor = this.mSensorManager.getDefaultSensor(2);
        }
        if (Build.MODEL.contains("MI MAX") || "MIX".equals(Build.MODEL) || "MIX 2".equals(Build.MODEL)) {
            this.mSensorFocus = true;
        }
        if (z2) {
            this.mSensorFocus = true;
            LogUtil.d(TAG, "set focus by Sensor");
        }
        LogUtil.d(TAG, "====> Init src: " + i + ", width: " + i2 + ", height:" + i3 + ", fps:" + i4 + ", rotation:" + i5 + ", mSurfaceCbMode: " + this.mSurfaceCbMode);
    }

    public boolean isPreviewRunning() {
        return this.mPreviewRunning;
    }

    public boolean isSupportAutoFocus() {
        Camera camera = this.mCamera;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getSupportedFocusModes() == null || parameters.getSupportedFocusModes().size() <= 0) {
                return false;
            }
            return parameters.getSupportedFocusModes().contains("continuous-video");
        }
        return false;
    }

    public boolean isSupportFlash() {
        Camera camera = this.mCamera;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getSupportedFlashModes() == null || parameters.getSupportedFlashModes().size() <= 0) {
                return false;
            }
            return parameters.getSupportedFlashModes().contains("torch");
        }
        return false;
    }

    public void pause(boolean z) {
        LogUtil.d(TAG, "pause preview.");
        this.mPause = true;
    }

    public void resume() {
        LogUtil.d(TAG, "resume preview.");
        if (this.mPause) {
            stopInner();
            try {
                startInner();
                this.mPause = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAutoFocus(boolean z) {
        if (this.mCamera != null && this.mParam.getCameraId() == 0) {
            this.mCamera.cancelAutoFocus();
            Camera.Parameters parameters = this.mCamera.getParameters();
            String str = "auto";
            try {
                if (z) {
                    if (this.mSensorFocus) {
                        parameters.setFocusMode("auto");
                        this.mCamera.autoFocus(null);
                        this.mCamera.setParameters(parameters);
                    } else {
                        str = "continuous-video";
                    }
                }
                this.mCamera.setParameters(parameters);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            parameters.setFocusMode(str);
        }
        this.mAutoFocus = z;
    }

    public void setExposure(int i) {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                if (i < parameters.getMinExposureCompensation() || i > parameters.getMaxExposureCompensation()) {
                    return;
                }
                parameters.setExposureCompensation(i);
                this.mCamera.setParameters(parameters);
            } catch (Exception unused) {
            }
        }
    }

    public void setFlashOn(boolean z) {
        if (this.mCamera != null) {
            if (z && this.mParam.getCameraId() == 0) {
                turnLightOn(this.mCamera);
            } else {
                turnLightOff(this.mCamera);
            }
        }
        this.mFlashOn = z;
    }

    public void setFocus(float f, float f2) {
        if (this.mCamera == null || this.mParam.getCameraId() == 1) {
            return;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        LogUtil.d(TAG, "focusAreas is " + parameters.getMaxNumFocusAreas());
        if (parameters == null || parameters.getMaxNumFocusAreas() != 0) {
            Camera camera = this.mCamera;
            if (camera != null) {
                camera.cancelAutoFocus();
            }
            int i = (int) ((f * 2000.0f) - 1000.0f);
            int i2 = (int) ((f2 * 2000.0f) - 1000.0f);
            Camera.Area area = new Camera.Area(new Rect(i - 50, i2 - 50, i + 50, i2 + 50), 1);
            ArrayList arrayList = new ArrayList();
            arrayList.add(area);
            parameters.setFocusAreas(arrayList);
            try {
                this.mCamera.setParameters(parameters);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.alivc.component.capture.video.VideoPusher.3
                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean z, Camera camera2) {
                    camera2.cancelAutoFocus();
                }
            });
        }
    }

    public void setLastCaptureTime(long j) {
        this.mLastCaptureTime = j;
    }

    public void setOrientation(int i) {
        LogUtil.d(TAG, "setOrientation.");
    }

    public void setVideoSourceListener(VideoSourceListener videoSourceListener) {
        this.mVideoSourceListener = videoSourceListener;
    }

    public void setVideoSourceTextureListener(VideoSourceTextureListener videoSourceTextureListener) {
        this.mVideoSourceTextureListener = videoSourceTextureListener;
    }

    public void setZoom(float f) {
        VideoParam videoParam;
        Camera camera = this.mCamera;
        if (camera == null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (!parameters.isZoomSupported()) {
            return;
        }
        int currentZoom = (int) (f * this.mParam.getCurrentZoom());
        int i = 1;
        try {
            if (currentZoom <= 1) {
                videoParam = this.mParam;
            } else if (currentZoom < parameters.getMaxZoom()) {
                this.mParam.setCurrentZoom(currentZoom);
                parameters.setZoom(this.mParam.getCurrentZoom());
                this.mCamera.setParameters(parameters);
                return;
            } else {
                videoParam = this.mParam;
                i = parameters.getMaxZoom();
            }
            this.mCamera.setParameters(parameters);
            return;
        } catch (Throwable th) {
            th.printStackTrace();
            return;
        }
        videoParam.setCurrentZoom(i);
        parameters.setZoom(this.mParam.getCurrentZoom());
    }

    public void setZoom(int i) {
        Camera camera = this.mCamera;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (!parameters.isZoomSupported() || i < 0 || i > parameters.getMaxZoom()) {
                return;
            }
            this.mParam.setCurrentZoom(i);
            parameters.setZoom(this.mParam.getCurrentZoom());
            try {
                this.mCamera.setParameters(parameters);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void start(int i) {
        SurfaceTexture surfaceTexture;
        LogUtil.d(TAG, "start.");
        boolean z = this.mSurfaceCbMode;
        try {
            if (!z || i < 0) {
                if (z && i < 0) {
                    this.mSurfaceCbMode = false;
                    if (this.mSurfaceTexture == null) {
                        surfaceTexture = new SurfaceTexture(10);
                    }
                }
                startInner();
                return;
            }
            this.mSurfaceTextureId = i;
            SurfaceTexture surfaceTexture2 = this.mSurfaceTexture;
            if (surfaceTexture2 != null) {
                surfaceTexture2.release();
                this.mSurfaceTexture = null;
            }
            surfaceTexture = new SurfaceTexture(this.mSurfaceTextureId);
            startInner();
            return;
        } catch (Exception e) {
            throw e;
        }
        this.mSurfaceTexture = surfaceTexture;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0015  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x000e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void startInner() {
        /*
            r4 = this;
            r4.startPreview0()     // Catch: java.lang.Throwable -> L4
            goto La
        L4:
            r4.stopInner()
            r4.startPreview0()     // Catch: java.lang.Exception -> L32
        La:
            boolean r0 = r4.mFlashOn
            if (r0 == 0) goto L11
            r4.setFlashOn(r0)
        L11:
            boolean r0 = r4.mAutoFocus
            if (r0 == 0) goto L18
            r4.setAutoFocus(r0)
        L18:
            android.hardware.SensorManager r0 = r4.mSensorManager
            if (r0 == 0) goto L31
            boolean r1 = r4.mSensorFocus
            if (r1 == 0) goto L31
            android.hardware.SensorEventListener r1 = r4.mSensorEventListener
            android.hardware.Sensor r2 = r4.mAccelSensor
            r3 = 2
            r0.registerListener(r1, r2, r3)
            android.hardware.SensorManager r0 = r4.mSensorManager
            android.hardware.SensorEventListener r1 = r4.mSensorEventListener
            android.hardware.Sensor r2 = r4.mMagneticSensor
            r0.registerListener(r1, r2, r3)
        L31:
            return
        L32:
            r0 = move-exception
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alivc.component.capture.video.VideoPusher.startInner():void");
    }

    public void stop() {
        SurfaceTexture surfaceTexture;
        LogUtil.d(TAG, "stop.");
        stopInner();
        if (!this.mSurfaceCbMode || (surfaceTexture = this.mSurfaceTexture) == null) {
            return;
        }
        surfaceTexture.release();
        this.mSurfaceTexture = null;
        this.mSurfaceTextureId = -1;
    }

    public void stopInner() {
        Camera camera;
        LogUtil.d(TAG, "stopInner.");
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager != null && this.mSensorFocus) {
            try {
                sensorManager.unregisterListener(this.mSensorEventListener);
            } catch (Exception unused) {
            }
        }
        if (!this.mPreviewRunning || (camera = this.mCamera) == null) {
            return;
        }
        camera.setPreviewCallback(null);
        this.mCamera.stopPreview();
        this.mCamera.release();
        this.mCamera = null;
        this.mPreviewRunning = false;
        LogUtil.d(TAG, "stopInner over.");
    }

    public void switchCamera() {
        VideoParam videoParam;
        int i;
        LogUtil.d(TAG, "switchCamera.");
        if (this.mParam.getCameraId() == 0) {
            videoParam = this.mParam;
            i = 1;
        } else {
            videoParam = this.mParam;
            i = 0;
        }
        videoParam.setCameraId(i);
        stopInner();
        startInner();
    }

    public int updateTexImage() {
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture == null) {
            return -1;
        }
        surfaceTexture.updateTexImage();
        return 0;
    }
}
