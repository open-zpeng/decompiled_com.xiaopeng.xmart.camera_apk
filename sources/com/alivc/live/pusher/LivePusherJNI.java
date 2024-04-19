package com.alivc.live.pusher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.view.Surface;
import com.alivc.component.custom.AlivcLivePushCustomDetect;
import com.alivc.component.custom.AlivcLivePushCustomFilter;
import java.io.File;
/* loaded from: classes.dex */
public class LivePusherJNI {
    public static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator;
    public static boolean headSetOn = false;
    private int audioBitRate;
    private boolean audioCaptureWithouMix;
    private int audioChannel;
    private int audioEncoderMode;
    private int audioFormat;
    private boolean audioOnly;
    private int audioProfile;
    private int audioSample;
    private boolean autoFocus;
    private float beautyBigEye;
    private float beautyBright;
    private float beautyBuffing;
    private float beautyCheekPink;
    private int beautyLevel;
    private int beautyMode;
    private boolean beautyOn;
    private float beautyPink;
    private float beautyShortenFace;
    private float beautyThinFace;
    private float beautyWhite;
    private int bitrate;
    private int cameraPosition;
    private int connectRetryCount;
    private int connectRetryInterval;
    public Context context;
    private int customRotation;
    private int displayMode;
    public boolean enableAutoResolution;
    public boolean enableBitrateControl;
    private int encoderMode;
    private int exposure;
    private boolean externMainStream;
    private boolean flash;
    private boolean focusBySensor;
    private int fps;
    private int gop;
    private int initialBitrate;
    private a mLivePusherListener;
    public Intent mediaProjectionPermissionResultData;
    private int minBitrate;
    public int minFps;
    private int orientaion;
    private boolean previewMirror;
    private boolean pushMirror;
    private int qualityMode;
    private boolean requireGLSharedContext;
    private int resolutionHeight;
    private int resolutionWidth;
    private int sendTimeout;
    public boolean surfaceCbMode;
    private int targetBitrate;
    private int videoFormat;
    private boolean videoOnly;
    private long mNativeHandler = 0;
    private int apiLevel = Build.VERSION.SDK_INT;
    private long ptsMaxDiff = 200000;
    private int maxTimeoutCount = 10;
    private int maxVideoListSize = 200;
    private int dropKeepVideoListSize = 40;
    private int queueSizeNeedBitrateControl = 30;
    private int minBitrateControlInterval = 3000000;
    private int upBpsRequestFreeDuration = 60000000;
    private int rtmpUpBPsMinFreePrecent = 50;
    private int needUpBpsCacheSize = 1;
    private int maxControlFailedTime = 3;
    private AlivcLivePushCustomFilter customFilter = null;
    private boolean mCustomFilterInited = false;
    private AlivcLivePushCustomDetect customDetect = null;
    private boolean mCustomDetectorInited = false;
    private AlivcSnapshotListener mSnapshotListener = null;

    /* loaded from: classes.dex */
    public interface a {
        void onNotify(int i, String str, int i2, int i3, int i4, int i5, int i6, long j);
    }

    static {
        System.loadLibrary("live-rtmp");
        System.loadLibrary("live-openh264");
        System.loadLibrary("live-fdkaac");
        System.loadLibrary("live-pusher");
    }

    public LivePusherJNI(Context context, AlivcLivePushConfig alivcLivePushConfig, a aVar) {
        this.resolutionWidth = 0;
        this.resolutionHeight = 0;
        this.fps = 20;
        this.targetBitrate = 1500;
        this.minBitrate = AlivcLivePushConstants.DEFAULT_VALUE_INT_MIN_BITRATE;
        this.bitrate = 800;
        this.initialBitrate = 800;
        this.audioSample = AlivcLivePushConstants.DEFAULT_VALUE_INT_AUDIO_SAMPLE_RATE;
        this.audioChannel = 2;
        this.audioProfile = 2;
        this.audioBitRate = 64000;
        this.connectRetryCount = 5;
        this.connectRetryInterval = 1000;
        this.sendTimeout = 5000;
        this.orientaion = 0;
        this.customRotation = 0;
        this.cameraPosition = 1;
        this.pushMirror = false;
        this.previewMirror = false;
        this.audioOnly = false;
        this.videoOnly = false;
        this.autoFocus = true;
        this.focusBySensor = false;
        this.flash = false;
        this.beautyOn = true;
        this.beautyBuffing = 0.6f;
        this.beautyWhite = 1.0E-4f;
        this.beautyBright = 1.5f;
        this.beautyPink = 0.0f;
        this.beautyCheekPink = 0.0f;
        this.beautyThinFace = 0.0f;
        this.beautyShortenFace = 0.0f;
        this.beautyBigEye = 0.0f;
        this.beautyMode = 0;
        this.encoderMode = 0;
        this.audioEncoderMode = 0;
        this.videoFormat = 0;
        this.audioFormat = 0;
        this.exposure = 0;
        this.gop = 1;
        this.context = null;
        this.surfaceCbMode = true;
        this.enableBitrateControl = true;
        this.enableAutoResolution = false;
        this.qualityMode = 0;
        this.beautyLevel = 0;
        this.displayMode = 0;
        this.mediaProjectionPermissionResultData = null;
        this.requireGLSharedContext = false;
        this.audioCaptureWithouMix = false;
        this.mLivePusherListener = null;
        this.externMainStream = false;
        this.context = context;
        this.resolutionWidth = alivcLivePushConfig.getWidth();
        this.resolutionHeight = alivcLivePushConfig.getHeight();
        this.fps = alivcLivePushConfig.getFps();
        this.gop = alivcLivePushConfig.getVideoEncodeGop();
        this.targetBitrate = alivcLivePushConfig.getTargetVideoBitrate();
        this.minBitrate = alivcLivePushConfig.getMinVideoBitrate();
        this.bitrate = alivcLivePushConfig.getInitialVideoBitrate();
        this.initialBitrate = alivcLivePushConfig.getInitialVideoBitrate();
        this.audioSample = alivcLivePushConfig.getAudioSamepleRate().getAudioSampleRate();
        this.audioChannel = b.d(alivcLivePushConfig.getAudioChannels() > 0 ? alivcLivePushConfig.getAudioChannels() : 12);
        this.connectRetryCount = alivcLivePushConfig.getConnectRetryCount();
        this.connectRetryInterval = alivcLivePushConfig.getConnectRetryInterval();
        this.orientaion = alivcLivePushConfig.getPreviewOrientation();
        this.customRotation = alivcLivePushConfig.getPreviewRotation();
        if (this.orientaion == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT.getOrientation() || this.orientaion == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT.getOrientation()) {
            this.resolutionWidth = alivcLivePushConfig.getHeight();
            this.resolutionHeight = alivcLivePushConfig.getWidth();
        }
        if (this.customRotation == AlivcPreviewRotationEnum.ROTATION_90.getRotation() || this.customRotation == AlivcPreviewRotationEnum.ROTATION_270.getRotation()) {
            int i = this.resolutionWidth;
            this.resolutionWidth = this.resolutionHeight;
            this.resolutionHeight = i;
        }
        this.cameraPosition = alivcLivePushConfig.getCameraType();
        this.pushMirror = alivcLivePushConfig.isPushMirror();
        this.previewMirror = alivcLivePushConfig.isPreviewMirror();
        this.audioOnly = alivcLivePushConfig.isAudioOnly();
        this.videoOnly = alivcLivePushConfig.isVideoOnly();
        this.autoFocus = alivcLivePushConfig.isAutoFocus();
        this.focusBySensor = alivcLivePushConfig.isFocusBySensor();
        this.flash = alivcLivePushConfig.isFlash();
        this.beautyOn = alivcLivePushConfig.isBeautyOn();
        this.displayMode = alivcLivePushConfig.getPreviewDisplayMode().getPreviewDisplayMode();
        this.beautyWhite = alivcLivePushConfig.getBeautyWhite() > 0 ? alivcLivePushConfig.getBeautyWhite() / 100.0f : this.beautyWhite;
        this.beautyBuffing = alivcLivePushConfig.getBeautyBuffing() >= 0 ? alivcLivePushConfig.getBeautyBuffing() / 100.0f : this.beautyBuffing;
        this.beautyBright = alivcLivePushConfig.getBeautyBrightness() >= 0 ? (alivcLivePushConfig.getBeautyBrightness() + 100.0f) / 100.0f : this.beautyBright;
        this.beautyPink = alivcLivePushConfig.getBeautyRuddy() >= 0 ? alivcLivePushConfig.getBeautyRuddy() / 100.0f : this.beautyPink;
        this.beautyCheekPink = alivcLivePushConfig.getBeautyCheekPink() >= 0 ? alivcLivePushConfig.getBeautyCheekPink() / 100.0f : this.beautyCheekPink;
        this.beautyThinFace = alivcLivePushConfig.getBeautyThinFace() > 0 ? alivcLivePushConfig.getBeautyThinFace() / 100.0f : this.beautyThinFace;
        this.beautyBigEye = alivcLivePushConfig.getBeautyBigEye() >= 0 ? alivcLivePushConfig.getBeautyBigEye() / 100.0f : this.beautyBigEye;
        this.beautyShortenFace = alivcLivePushConfig.getBeautyShortenFace() >= 0 ? alivcLivePushConfig.getBeautyShortenFace() / 100.0f : this.beautyShortenFace;
        this.beautyMode = 0;
        this.encoderMode = alivcLivePushConfig.getVideoEncodeMode().ordinal();
        this.audioEncoderMode = alivcLivePushConfig.getAudioEncodeMode().ordinal();
        this.videoFormat = b.e(alivcLivePushConfig.getVideoFormat());
        this.audioFormat = b.c(alivcLivePushConfig.getAudioFormat());
        this.exposure = 0;
        this.mLivePusherListener = aVar;
        this.minFps = alivcLivePushConfig.getMinFps();
        this.qualityMode = alivcLivePushConfig.getQualityMode().getQualityMode();
        int beautyMode = alivcLivePushConfig.getBeautyLevel().getBeautyMode();
        this.beautyLevel = beautyMode;
        if (beautyMode == AlivcBeautyLevelEnum.BEAUTY_Professional.getBeautyMode()) {
            this.surfaceCbMode = false;
        }
        this.audioProfile = alivcLivePushConfig.getAudioProfile().getAudioProfile();
        this.audioBitRate = alivcLivePushConfig.getAudioBitRate();
        if (Build.VERSION.SDK_INT >= 21) {
            this.mediaProjectionPermissionResultData = alivcLivePushConfig.getMediaProjectionPermissionResultData();
        }
        if (this.mediaProjectionPermissionResultData != null) {
            this.beautyOn = false;
            this.displayMode = AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL.getPreviewDisplayMode();
        }
        this.enableAutoResolution = alivcLivePushConfig.isEnableAutoResolution();
        this.enableBitrateControl = alivcLivePushConfig.isEnableBitrateControl();
        boolean isExternMainStream = alivcLivePushConfig.isExternMainStream();
        this.externMainStream = isExternMainStream;
        if (isExternMainStream) {
            this.surfaceCbMode = false;
            this.videoFormat = alivcLivePushConfig.getAlivcExternMainImageFormat().getAlivcImageFormat();
            this.audioFormat = alivcLivePushConfig.getAlivcExternMainSoundFormat().getAlivcSoundFormat();
        }
        this.sendTimeout = alivcLivePushConfig.getSendDataTimeout();
        this.requireGLSharedContext = alivcLivePushConfig.getRequireRenderContextNotify();
        this.audioCaptureWithouMix = alivcLivePushConfig.getAudioCaptureWithoutMix();
    }

    private native void addLiveWaterMark(String str, float f, float f2, float f3, float f4);

    private native int addNativeAddons(String str, long j, long j2, float f, float f2, float f3, float f4, int i, boolean z);

    private native int addNativeMixAudio(int i, int i2, int i3);

    private native int addNativeMixVideo(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, boolean z);

    private native void addNativePushImage(String str, String str2);

    private native void addNativeSeiInfo(String str, int i, int i2, boolean z);

    private native void changeNativeResolution(int i, int i2);

    private native int getLiveCameraCurrentExposure();

    private native int getLiveCameraCurrentZoom();

    private native int getLiveCameraMaxExposure();

    private native int getLiveCameraMaxZoom();

    private native int getLiveCameraMinExposure();

    private native String getLivePerformanceInfo();

    private native long initLive(boolean z);

    private native boolean inputNativeMixAudioData(int i, byte[] bArr, int i2, long j);

    private native boolean inputNativeMixAudioPtr(int i, long j, int i2, long j2);

    private native void inputNativeMixTexture(int i, int i2, int i3, int i4, long j, int i5);

    private native void inputNativeMixVideoData(int i, byte[] bArr, int i2, int i3, int i4, int i5, long j, int i6);

    private native void inputNativeMixVideoPtr(int i, long j, int i2, int i3, int i4, int i5, long j2, int i6);

    private native void inputNativeStreamAudioData(byte[] bArr, int i, int i2, int i3, long j);

    private native void inputNativeStreamAudioPtr(long j, int i, int i2, int i3, long j2);

    private native void inputNativeStreamTexture(int i, int i2, int i3, int i4, long j, int i5, long j2);

    private native void inputNativeStreamVideoData(byte[] bArr, int i, int i2, int i3, int i4, long j, int i5);

    private native void inputNativeStreamVideoPtr(long j, int i, int i2, int i3, int i4, long j2, int i5);

    private native boolean isLiveNetworkPushing();

    private native boolean isLivePushing();

    private native boolean isLiveSupportAutoFocus();

    private native boolean isLiveSupportFlash();

    private native int mixVideoChangePosition(int i, float f, float f2, float f3, float f4);

    private native void mixVideoMirror(int i, boolean z);

    private native int mixVideoRequireMain(int i, boolean z);

    private native void notifyLiveSurfaceChanged(Surface surface, int i);

    private native void notifyLiveSurfaceDestroy();

    private native void notifyLiveSurfaceRecreate(Surface surface);

    private native void pauseLive();

    private native void pauseLiveScreenCapture();

    private native void pauseNativeBGM();

    private native int reconnectLive(String str, boolean z);

    private native void removeNativeAddons(int i);

    private native void removeNativeMixAudio(int i);

    private native void removeNativeMixVideo(int i);

    private native void removeNativePushImage();

    private native int restartLivePush(Surface surface, boolean z, int i);

    private native void resumeLive(boolean z);

    private native int resumeLiveScreenCapture();

    private native void resumeNativeBGM();

    private native int screenCaptureStartCamera(Surface surface);

    private native int screenCaptureStartMix(float f, float f2, float f3, float f4);

    private native void screenCaptureStopCamera();

    private native void screenCaptureStopMix();

    private native void screenSetScreenOrientation(int i);

    private native void setLiveBeauty(boolean z, int i, int i2);

    private native int setLiveCameraExposure(int i);

    private native int setLiveCameraFocus(boolean z, float f, float f2);

    private native int setLiveCameraZoom(float f);

    private native int setLiveFaceBeauty(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private native int setLiveFlash(boolean z);

    private native int setLiveMute(boolean z);

    private native void setLivePreviewMirror(boolean z);

    private native void setLivePusherMirror(boolean z);

    private native void setLiveScreenOrientation(int i);

    private native void setLiveVideoBitrateRange(int i, int i2, int i3);

    private native void setNativeBGMLoop(boolean z);

    private native void setNativeBackgroundMusicVolume(int i);

    private native void setNativeCaptureAudioVolume(int i);

    private native void setNativeDenoise(boolean z);

    private native void setNativeDisplayMode(int i);

    private native void setNativeEarsBack(boolean z);

    private native void setNativeHeadSet(boolean z);

    private native void setNativeLogLevel(int i);

    private native void setNativeMainStreamPosition(float f, float f2, float f3, float f4);

    private native void setNativeQualityMode(int i);

    private native void setNativeWatermarkVisible(boolean z);

    private native void snapshotNative(int i, int i2);

    private native int startLivePreview(Surface surface, boolean z, boolean z2);

    private native int startLivePush(String str, boolean z, boolean z2);

    private native void startNativeBGMAsync(String str);

    private native int stopLivePreview();

    private native int stopLivePush();

    private native void stopNativeBGM();

    private native int switchLiveCamera();

    private native void unInitLive();

    public int addDynamicsAddons(String str, long j, long j2, float f, float f2, float f3, float f4, int i, boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return addNativeAddons(str, j, j2, f, f2, f3, f4, i, z);
    }

    public int addMixAudio(int i, int i2, int i3) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return addNativeMixAudio(i, i2, i3);
    }

    public int addMixVideo(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return addNativeMixVideo(i, i2, i3, i4, f, f2, f3, f4, z);
    }

    public void addPushImage(String str, String str2) {
        if (this.mNativeHandler == 0) {
            return;
        }
        addNativePushImage(str, str2);
    }

    public void addSeiInfo(String str, int i, int i2, boolean z) {
        if (this.mNativeHandler == 0) {
            return;
        }
        addNativeSeiInfo(str, i, i2, z);
    }

    public void addWaterMark(String str, float f, float f2, float f3, float f4) {
        if (this.mNativeHandler == 0) {
            return;
        }
        addLiveWaterMark(str, f, f2, f3, f4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeResolution(int i, int i2) {
        if (this.mNativeHandler == 0) {
            return;
        }
        if (this.orientaion == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT.getOrientation() || this.orientaion == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT.getOrientation()) {
            this.resolutionWidth = i2;
            this.resolutionHeight = i;
        } else {
            this.resolutionWidth = i;
            this.resolutionHeight = i2;
        }
        changeNativeResolution(this.resolutionWidth, this.resolutionHeight);
    }

    void customBeautyCreate() {
        AlivcLivePushCustomFilter alivcLivePushCustomFilter = this.customFilter;
        if (alivcLivePushCustomFilter == null) {
            return;
        }
        alivcLivePushCustomFilter.customFilterCreate();
        this.mCustomFilterInited = true;
        this.customFilter.customFilterSwitch(this.beautyOn);
    }

    void customBeautyDestroy() {
        AlivcLivePushCustomFilter alivcLivePushCustomFilter = this.customFilter;
        if (alivcLivePushCustomFilter == null) {
            return;
        }
        if (this.mCustomFilterInited) {
            alivcLivePushCustomFilter.customFilterDestroy();
        }
        this.mCustomFilterInited = false;
    }

    int customBeautyProcess(int i, int i2, int i3, long j) {
        AlivcLivePushCustomFilter alivcLivePushCustomFilter = this.customFilter;
        return (alivcLivePushCustomFilter != null && this.mCustomFilterInited) ? alivcLivePushCustomFilter.customFilterProcess(i, i2, i3, j) : i;
    }

    void customDetectCreate() {
        AlivcLivePushCustomDetect alivcLivePushCustomDetect = this.customDetect;
        if (alivcLivePushCustomDetect == null) {
            return;
        }
        alivcLivePushCustomDetect.customDetectCreate();
        this.mCustomDetectorInited = true;
    }

    void customDetectDestroy() {
        AlivcLivePushCustomDetect alivcLivePushCustomDetect = this.customDetect;
        if (alivcLivePushCustomDetect == null) {
            return;
        }
        if (this.mCustomDetectorInited) {
            alivcLivePushCustomDetect.customDetectDestroy();
        }
        this.mCustomDetectorInited = false;
    }

    long customDetectProcess(long j, int i, int i2, int i3, int i4, long j2) {
        AlivcLivePushCustomDetect alivcLivePushCustomDetect = this.customDetect;
        if (alivcLivePushCustomDetect != null && this.mCustomDetectorInited) {
            return alivcLivePushCustomDetect.customDetectProcess(j, i, i2, i3, i4, j2);
        }
        return 0L;
    }

    public int getCameraCurrentZoom() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return getLiveCameraCurrentZoom();
    }

    public int getCameraMaxZoom() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return getLiveCameraMaxZoom();
    }

    public int getCurrentExposureCompensation() {
        if (this.mNativeHandler == 0) {
            return 0;
        }
        return getLiveCameraCurrentExposure();
    }

    public int getMaxExposureCompensation() {
        if (this.mNativeHandler == 0) {
            return 0;
        }
        return getLiveCameraMaxExposure();
    }

    public int getMinExposureCompensation() {
        if (this.mNativeHandler == 0) {
            return 0;
        }
        return getLiveCameraMinExposure();
    }

    public String getPerformanceInfo() {
        if (this.mNativeHandler == 0) {
            return null;
        }
        return getLivePerformanceInfo();
    }

    public long getPusherHandler() {
        LogUtil.d("LivePusherJNI", "getPusherHandler " + this.mNativeHandler);
        return this.mNativeHandler;
    }

    public void init() {
        initLive(this.externMainStream);
        LogUtil.d("LivePusherJNI", "native handler " + this.mNativeHandler);
    }

    public void initStream() {
        initLive(true);
        LogUtil.d("LivePusherJNI", "native handler " + this.mNativeHandler);
    }

    public boolean inputMixAudioData(int i, byte[] bArr, int i2, long j) {
        if (this.mNativeHandler == 0) {
            return false;
        }
        return inputNativeMixAudioData(i, bArr, i2, j);
    }

    public boolean inputMixAudioPtr(int i, long j, int i2, long j2) {
        if (this.mNativeHandler == 0) {
            return false;
        }
        return inputNativeMixAudioPtr(i, j, i2, j2);
    }

    public void inputMixTexture(int i, int i2, int i3, int i4, long j, int i5) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeMixTexture(i, i2, i3, i4, j, i5);
    }

    public void inputMixVideoData(int i, byte[] bArr, int i2, int i3, int i4, int i5, long j, int i6) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeMixVideoData(i, bArr, i2, i3, i4, i5, j, i6);
    }

    public void inputMixVideoPtr(int i, long j, int i2, int i3, int i4, int i5, long j2, int i6) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeMixVideoPtr(i, j, i2, i3, i4, i5, j2, i6);
    }

    public void inputStreamAudioData(byte[] bArr, int i, int i2, int i3, long j) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeStreamAudioData(bArr, i, i2, i3, j);
    }

    public void inputStreamAudioPtr(long j, int i, int i2, int i3, long j2) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeStreamAudioPtr(j, i, i2, i3, j2);
    }

    public void inputStreamTexture(int i, int i2, int i3, int i4, long j, int i5, long j2) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeStreamTexture(i, i2, i3, i4, j, i5, j2);
    }

    public void inputStreamVideoData(byte[] bArr, int i, int i2, int i3, int i4, long j, int i5) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeStreamVideoData(bArr, i, i2, i3, i4, j, i5);
    }

    public void inputStreamVideoPtr(long j, int i, int i2, int i3, int i4, long j2, int i5) {
        if (this.mNativeHandler == 0) {
            return;
        }
        inputNativeStreamVideoPtr(j, i, i2, i3, i4, j2, i5);
    }

    public boolean isCameraSupportAutoFocus() {
        if (this.mNativeHandler == 0) {
            return false;
        }
        return isLiveSupportAutoFocus();
    }

    public boolean isCameraSupportFlash() {
        if (this.mNativeHandler == 0) {
            return false;
        }
        return isLiveSupportFlash();
    }

    public boolean isNetworkPushing() {
        if (this.mNativeHandler == 0) {
            return false;
        }
        return isLiveNetworkPushing();
    }

    public boolean isPushing() {
        if (this.mNativeHandler == 0) {
            return false;
        }
        return isLivePushing();
    }

    public int mixStreamChangePosition(int i, float f, float f2, float f3, float f4) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return mixVideoChangePosition(i, f, f2, f3, f4);
    }

    public int mixStreamRequireMain(int i, boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return mixVideoRequireMain(i, z);
    }

    public void notifySurfaceChange(Surface surface, int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        notifyLiveSurfaceChanged(surface, i);
    }

    public void notifySurfaceDestroy() {
        if (this.mNativeHandler == 0) {
            return;
        }
        notifyLiveSurfaceDestroy();
    }

    public void notifySurfaceReCreate(Surface surface) {
        if (this.mNativeHandler == 0) {
            return;
        }
        notifyLiveSurfaceRecreate(surface);
    }

    public int onNotification(int i, String str, int i2, int i3, int i4, int i5, int i6, long j) {
        a aVar = this.mLivePusherListener;
        if (aVar != null) {
            aVar.onNotify(i, str, i2, i3, i4, i5, i6, j);
            return 0;
        }
        return 0;
    }

    public void pause() {
        if (this.mNativeHandler == 0) {
            return;
        }
        pauseLive();
    }

    public void pauseBGM() {
        pauseNativeBGM();
    }

    public void pauseScreenCapture() {
        if (this.mNativeHandler == 0) {
            return;
        }
        pauseLiveScreenCapture();
    }

    public int reconnect(String str, boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return reconnectLive(str, z);
    }

    public void release() {
        if (this.mNativeHandler == 0) {
            return;
        }
        unInitLive();
        this.mNativeHandler = 0L;
    }

    public void releaseStream() {
        if (this.mNativeHandler == 0) {
            return;
        }
        unInitLive();
        this.mNativeHandler = 0L;
    }

    public void removeDynamicsAddons(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        removeNativeAddons(i);
    }

    public void removeMixAudio(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        removeNativeMixAudio(i);
    }

    public void removeMixVideo(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        removeNativeMixVideo(i);
    }

    public void removePushImage() {
        if (this.mNativeHandler == 0) {
            return;
        }
        removeNativePushImage();
    }

    public int restartPush(Surface surface, boolean z, int i) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return restartLivePush(surface, z, i);
    }

    public void resume(boolean z) {
        if (this.mNativeHandler == 0) {
            return;
        }
        resumeLive(z);
    }

    public void resumeBGM() {
        resumeNativeBGM();
    }

    public int resumeScreenCapture() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return resumeLiveScreenCapture();
    }

    public void setAudioDenoise(boolean z) {
        setNativeDenoise(z);
    }

    public void setAudioVolume(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setNativeCaptureAudioVolume(i);
    }

    public void setBGMLoop(boolean z) {
        setNativeBGMLoop(z);
    }

    public void setBackgroundMusicVolume(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setNativeBackgroundMusicVolume(i);
    }

    public void setBeauty(boolean z, int i, int i2) {
        if (this.mNativeHandler == 0) {
            return;
        }
        if (this.mCustomFilterInited) {
            this.customFilter.customFilterSwitch(z);
        }
        setLiveBeauty(z, i, i2);
        this.beautyOn = z;
    }

    public int setCameraFocus(boolean z, float f, float f2) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return setLiveCameraFocus(z, f, f2);
    }

    public int setCameraZoom(float f) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return setLiveCameraZoom(f);
    }

    public void setCustomDetect(AlivcLivePushCustomDetect alivcLivePushCustomDetect) {
        this.customDetect = alivcLivePushCustomDetect;
    }

    public void setCustomFilter(AlivcLivePushCustomFilter alivcLivePushCustomFilter) {
        this.customFilter = alivcLivePushCustomFilter;
    }

    public void setDisplayMode(int i) {
        setNativeDisplayMode(i);
    }

    public void setEarsBack(boolean z) {
        setNativeEarsBack(z);
    }

    public void setExposureCompensation(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setLiveCameraExposure(i);
    }

    public int setFaceBeauty(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = f2 <= 0.0f ? 0.001f : f2;
        if (this.mNativeHandler == 0) {
            return -1;
        }
        if (this.mCustomFilterInited) {
            this.customFilter.customFilterUpdateParam(f, f9, f5, f4, f6, f7, f8);
        }
        return setLiveFaceBeauty(f, f9, f3, f4, f5, f6, f7, f8);
    }

    public int setFlash(boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return setLiveFlash(z);
    }

    public void setFps(int i) {
        this.fps = i;
    }

    public void setGop(int i) {
        this.gop = i;
    }

    public void setHeadSet(boolean z) {
        headSetOn = z;
        if (this.mNativeHandler == 0) {
            return;
        }
        setNativeHeadSet(z);
    }

    public void setLogLevel(int i) {
        setNativeLogLevel(i);
    }

    public void setMainStreamPosition(float f, float f2, float f3, float f4) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setNativeMainStreamPosition(f, f2, f3, f4);
    }

    public void setMixStreamMirror(int i, boolean z) {
        if (this.mNativeHandler == 0) {
            return;
        }
        mixVideoMirror(i, z);
    }

    public int setMute(boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        LogUtil.d("LivePusherJNI", "setMute");
        return setLiveMute(z);
    }

    public void setOrientaion(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setLiveScreenOrientation(i);
    }

    public void setPreOrientaion(int i) {
        this.orientaion = i;
    }

    public void setPreviewMirror(boolean z) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setLivePreviewMirror(z);
    }

    public void setPusherMirror(boolean z) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setLivePusherMirror(z);
    }

    public void setQualityMode(int i) {
        setNativeQualityMode(i);
    }

    public void setScreenOrientation(int i) {
        if (this.mNativeHandler == 0) {
            return;
        }
        screenSetScreenOrientation(i);
    }

    public void setVideoBitrateRange(int i, int i2, int i3) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setLiveVideoBitrateRange(i, i2, i3);
    }

    public void setWaterMarkVisible(boolean z) {
        if (this.mNativeHandler == 0) {
            return;
        }
        setNativeWatermarkVisible(z);
    }

    public void snapshot(int i, int i2, AlivcSnapshotListener alivcSnapshotListener) {
        if (this.mNativeHandler == 0) {
            return;
        }
        this.mSnapshotListener = alivcSnapshotListener;
        snapshotNative(i, i2);
    }

    void snapshotCallback(Bitmap bitmap) {
        AlivcSnapshotListener alivcSnapshotListener;
        if (bitmap == null || (alivcSnapshotListener = this.mSnapshotListener) == null) {
            return;
        }
        alivcSnapshotListener.onSnapshot(bitmap);
    }

    public void startBGMAsync(String str) {
        setNativeHeadSet(headSetOn);
        startNativeBGMAsync(str);
    }

    public int startCamera(Surface surface) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return screenCaptureStartCamera(surface);
    }

    public int startCameraMix(float f, float f2, float f3, float f4) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return screenCaptureStartMix(f, f2, f3, f4);
    }

    public int startPreview(Surface surface, boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return startLivePreview(surface, z, this.externMainStream);
    }

    public int startPreviewStream(Surface surface) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return startLivePreview(surface, true, true);
    }

    public int startPush(String str, boolean z) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        setNativeHeadSet(headSetOn);
        return startLivePush(str, z, this.externMainStream);
    }

    public int startPushStream(String str) {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        setNativeHeadSet(headSetOn);
        return startLivePush(str, true, true);
    }

    public void stopBGM() {
        stopNativeBGM();
    }

    public void stopCamera() {
        if (this.mNativeHandler == 0) {
            return;
        }
        screenCaptureStopCamera();
    }

    public void stopCameraMix() {
        if (this.mNativeHandler == 0) {
            return;
        }
        screenCaptureStopMix();
    }

    public int stopPreview() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return stopLivePreview();
    }

    public int stopPreviewStream() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return stopLivePreview();
    }

    public int stopPush() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return stopLivePush();
    }

    public int stopPushStream() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return stopLivePush();
    }

    public int switchCamera() {
        if (this.mNativeHandler == 0) {
            return -1;
        }
        return switchLiveCamera();
    }
}
