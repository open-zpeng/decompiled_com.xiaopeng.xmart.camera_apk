package com.alivc.live.pusher;

import android.content.Intent;
import android.os.Build;
import java.io.Serializable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class AlivcLivePushConfig implements Serializable {
    public static final String CONFIG = "AlivcLivePushConfig";
    private static final int RESOLUTION_ALIGN = 16;
    private static Intent mediaProjectionPermissionResultData;
    private boolean mAudioCaptureWithouMix;
    private AlivcResolutionEnum mResolution = AlivcLivePushConstants.DEFAULT_VALUE_INT_DEFINITION;
    private AlivcFpsEnum mFps = AlivcFpsEnum.FPS_20;
    private int mTargetVideoBitrate = AlivcLivePushConstants.DEFAULT_VALUE_INT_TARGET_BITRATE;
    private int mMinVideoBitrate = AlivcLivePushConstants.DEFAULT_VALUE_INT_MIN_BITRATE;
    private int mInitialVideoBitrate = 1000;
    private AlivcAudioSampleRateEnum mAudioSamepleRate = AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_32000;
    private AlivcPreviewOrientationEnum mPreviewOrientation = AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT;
    private AlivcPreviewRotationEnum mPreviewRotation = AlivcPreviewRotationEnum.ROTATION_0;
    private AlivcPreviewDisplayMode mPreviewDisplayMode = AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FIT;
    private AlivcLivePushCameraTypeEnum mCameraType = AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT;
    private boolean mPreviewMirror = false;
    private boolean mPushMirror = false;
    private boolean mAudioOnly = false;
    private boolean mVideoOnly = false;
    private boolean mAutoFocus = true;
    private boolean mFocusBySensor = false;
    private boolean mBeautyOn = true;
    private int mBeautyWhite = 70;
    private int mBeautyBuffing = 40;
    private int mBeautyCheekPink = 15;
    private int mBeautyBrightness = 50;
    private int mBeautyRuddy = 40;
    private int mBeautyThinFace = 40;
    private int mBeautyBigEye = 30;
    private int mBeautyShortenFace = 50;
    private boolean mFlash = false;
    private int mWidth = AlivcResolutionEnum.GetResolutionWidth(AlivcLivePushConstants.DEFAULT_VALUE_INT_DEFINITION);
    private int mHeight = AlivcResolutionEnum.GetResolutionHeight(AlivcLivePushConstants.DEFAULT_VALUE_INT_DEFINITION);
    private int mConnectRetryCount = 5;
    private int mConnectRetryInterval = 1000;
    private long mPtsMaxDiff = 200000;
    private int mMaxTimeoutCount = 10;
    private AlivcAudioChannelEnum mAudioChannels = AlivcAudioChannelEnum.AUDIO_CHANNEL_TWO;
    private AlivcEncodeModeEnum mVideoEncodeMode = AlivcEncodeModeEnum.Encode_MODE_HARD;
    private AlivcEncodeModeEnum mAudioEncodeMode = AlivcEncodeModeEnum.Encode_MODE_SOFT;
    private int mVideoFormat = 17;
    private int mAudioFormat = 2;
    private AlivcImageFormat mAlivcExternMainImageFormat = AlivcImageFormat.IMAGE_FORMAT_YUV420P;
    private AlivcSoundFormat mAlivcExternMainSoundFormat = AlivcSoundFormat.SOUND_FORMAT_S16;
    private AlivcVideoEncodeGopEnum mVideoEncodeGop = AlivcVideoEncodeGopEnum.GOP_TWO;
    private int mExposure = 0;
    private AlivcFpsEnum minFps = AlivcFpsEnum.FPS_8;
    private boolean enableBitrateControl = true;
    private boolean enableAutoResolution = false;
    private boolean initBitFlag = false;
    private boolean minBitFlag = false;
    private boolean targetBitFlag = false;
    private boolean externMainStream = false;
    private int sendDataTimeout = 3000;
    private AlivcQualityModeEnum qualityMode = AlivcQualityModeEnum.QM_RESOLUTION_FIRST;
    private AlivcBeautyLevelEnum beautyLevel = AlivcBeautyLevelEnum.BEAUTY_Professional;
    private AlivcAudioAACProfileEnum audioProfile = AlivcAudioAACProfileEnum.AAC_LC;
    private int audioBitRate = 64000;
    private String mPausePushImagePath = "";
    private String mNetworkPoorPushImagePath = "";
    private boolean requireGLSharedContext = false;
    private ArrayList<WaterMarkInfo> waterMarkInfos = new ArrayList<>();

    public AlivcLivePushConfig() {
        this.mAudioCaptureWithouMix = false;
        mediaProjectionPermissionResultData = null;
        if (Build.MANUFACTURER.equals("Letv") || Build.MODEL.equals("Le X620")) {
            this.mAudioCaptureWithouMix = true;
        }
    }

    public static void setMediaProjectionPermissionResultData(Intent intent) {
        mediaProjectionPermissionResultData = intent;
    }

    public void addWaterMark(String str, float f, float f2, float f3) {
        WaterMarkInfo waterMarkInfo = new WaterMarkInfo();
        waterMarkInfo.mWaterMarkPath = str;
        waterMarkInfo.mWaterMarkCoordX = f;
        waterMarkInfo.mWaterMarkCoordY = f2;
        waterMarkInfo.mWaterMarkWidth = f3;
        if (this.waterMarkInfos.size() < 3) {
            this.waterMarkInfos.add(waterMarkInfo);
        }
    }

    public AlivcImageFormat getAlivcExternMainImageFormat() {
        return this.mAlivcExternMainImageFormat;
    }

    public AlivcSoundFormat getAlivcExternMainSoundFormat() {
        return this.mAlivcExternMainSoundFormat;
    }

    public int getAudioBitRate() {
        return this.audioBitRate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean getAudioCaptureWithoutMix() {
        return this.mAudioCaptureWithouMix;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getAudioChannels() {
        return this.mAudioChannels.getChannelCount();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlivcEncodeModeEnum getAudioEncodeMode() {
        return this.mAudioEncodeMode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getAudioFormat() {
        return this.mAudioFormat;
    }

    public AlivcAudioAACProfileEnum getAudioProfile() {
        return this.audioProfile;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlivcAudioSampleRateEnum getAudioSamepleRate() {
        return this.mAudioSamepleRate;
    }

    public int getBeautyBigEye() {
        return this.mBeautyBigEye;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBeautyBrightness() {
        return this.mBeautyBrightness;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBeautyBuffing() {
        return this.mBeautyBuffing;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBeautyCheekPink() {
        return this.mBeautyCheekPink;
    }

    public AlivcBeautyLevelEnum getBeautyLevel() {
        return this.beautyLevel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBeautyRuddy() {
        return this.mBeautyRuddy;
    }

    public int getBeautyShortenFace() {
        return this.mBeautyShortenFace;
    }

    public int getBeautyThinFace() {
        return this.mBeautyThinFace;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBeautyWhite() {
        return this.mBeautyWhite;
    }

    public int getCameraType() {
        return this.mCameraType.getCameraId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getConnectRetryCount() {
        return this.mConnectRetryCount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getConnectRetryInterval() {
        return this.mConnectRetryInterval;
    }

    protected int getExposure() {
        return this.mExposure;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getFps() {
        return this.mFps.getFps();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getHeight() {
        return this.mHeight;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getInitialVideoBitrate() {
        return this.mInitialVideoBitrate;
    }

    public Intent getMediaProjectionPermissionResultData() {
        return mediaProjectionPermissionResultData;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getMinFps() {
        return this.minFps.getFps();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getMinVideoBitrate() {
        return this.mMinVideoBitrate;
    }

    public String getNetworkPoorPushImage() {
        return this.mNetworkPoorPushImagePath;
    }

    public String getPausePushImage() {
        return this.mPausePushImagePath;
    }

    public AlivcPreviewDisplayMode getPreviewDisplayMode() {
        return this.mPreviewDisplayMode;
    }

    public int getPreviewOrientation() {
        return this.mPreviewOrientation.getOrientation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getPreviewRotation() {
        return this.mPreviewRotation.getRotation();
    }

    public AlivcQualityModeEnum getQualityMode() {
        return this.qualityMode;
    }

    public boolean getRequireRenderContextNotify() {
        return this.requireGLSharedContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlivcResolutionEnum getResolution() {
        return this.mResolution;
    }

    public int getSendDataTimeout() {
        return this.sendDataTimeout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getTargetVideoBitrate() {
        return this.mTargetVideoBitrate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getVideoEncodeGop() {
        return this.mVideoEncodeGop.getGop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlivcEncodeModeEnum getVideoEncodeMode() {
        return this.mVideoEncodeMode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getVideoFormat() {
        return this.mVideoFormat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<WaterMarkInfo> getWaterMarkInfos() {
        return this.waterMarkInfos;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getWidth() {
        return this.mWidth;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAudioOnly() {
        return this.mAudioOnly;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAutoFocus() {
        return this.mAutoFocus;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isBeautyOn() {
        return this.mBeautyOn;
    }

    public boolean isEnableAutoResolution() {
        return this.enableAutoResolution;
    }

    public boolean isEnableBitrateControl() {
        return this.enableBitrateControl;
    }

    public boolean isExternMainStream() {
        return this.externMainStream;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isFlash() {
        return this.mFlash;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isFocusBySensor() {
        return this.mFocusBySensor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPreviewMirror() {
        return this.mPreviewMirror;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPushMirror() {
        return this.mPushMirror;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isVideoOnly() {
        return this.mVideoOnly;
    }

    public void setAlivcExternMainImageFormat(AlivcImageFormat alivcImageFormat) {
        this.mAlivcExternMainImageFormat = alivcImageFormat;
    }

    public void setAlivcExternMainSoundFormat(AlivcSoundFormat alivcSoundFormat) {
        this.mAlivcExternMainSoundFormat = alivcSoundFormat;
    }

    public void setAudioBitRate(int i) {
        this.audioBitRate = i;
    }

    public void setAudioCaptureWithoutMix(boolean z) {
        this.mAudioCaptureWithouMix = z;
    }

    public void setAudioChannels(AlivcAudioChannelEnum alivcAudioChannelEnum) {
        this.mAudioChannels = alivcAudioChannelEnum;
    }

    public void setAudioEncodeMode(AlivcEncodeModeEnum alivcEncodeModeEnum) {
        this.mAudioEncodeMode = alivcEncodeModeEnum;
    }

    protected void setAudioFormat(int i) {
        this.mAudioFormat = i;
    }

    public void setAudioOnly(boolean z) {
        this.mAudioOnly = z;
    }

    public void setAudioProfile(AlivcAudioAACProfileEnum alivcAudioAACProfileEnum) {
        this.audioProfile = alivcAudioAACProfileEnum;
    }

    public void setAudioSamepleRate(AlivcAudioSampleRateEnum alivcAudioSampleRateEnum) {
        this.mAudioSamepleRate = alivcAudioSampleRateEnum;
    }

    public void setAutoFocus(boolean z) {
        this.mAutoFocus = z;
    }

    public void setBeautyBigEye(int i) {
        this.mBeautyBigEye = i;
    }

    public void setBeautyBrightness(int i) {
        this.mBeautyBrightness = i;
    }

    public void setBeautyBuffing(int i) {
        this.mBeautyBuffing = i;
    }

    public void setBeautyCheekPink(int i) {
        this.mBeautyCheekPink = i;
    }

    public void setBeautyLevel(AlivcBeautyLevelEnum alivcBeautyLevelEnum) {
        this.beautyLevel = alivcBeautyLevelEnum;
    }

    public void setBeautyOn(boolean z) {
        this.mBeautyOn = z;
    }

    public void setBeautyRuddy(int i) {
        this.mBeautyRuddy = i;
    }

    public void setBeautyShortenFace(int i) {
        this.mBeautyShortenFace = i;
    }

    public void setBeautyThinFace(int i) {
        this.mBeautyThinFace = i;
    }

    public void setBeautyWhite(int i) {
        this.mBeautyWhite = i;
    }

    public void setCameraType(AlivcLivePushCameraTypeEnum alivcLivePushCameraTypeEnum) {
        this.mCameraType = alivcLivePushCameraTypeEnum;
    }

    public void setConnectRetryCount(int i) {
        this.mConnectRetryCount = i;
    }

    public void setConnectRetryInterval(int i) {
        this.mConnectRetryInterval = i;
    }

    public void setEnableAutoResolution(boolean z) {
        this.enableAutoResolution = z;
    }

    public void setEnableBitrateControl(boolean z) {
        this.enableBitrateControl = z;
    }

    public void setExposure(int i) {
        this.mExposure = i;
    }

    public void setExternMainStream(boolean z) {
        this.externMainStream = z;
    }

    public void setExternMainStream(boolean z, AlivcImageFormat alivcImageFormat, AlivcSoundFormat alivcSoundFormat) {
        this.externMainStream = z;
        this.mAlivcExternMainImageFormat = alivcImageFormat;
        this.mAlivcExternMainSoundFormat = alivcSoundFormat;
    }

    public void setFlash(boolean z) {
        this.mFlash = z;
    }

    public void setFocusBySensor(boolean z) {
        this.mFocusBySensor = z;
    }

    public void setFps(AlivcFpsEnum alivcFpsEnum) {
        this.mFps = alivcFpsEnum;
    }

    public void setInitialVideoBitrate(int i) {
        this.mInitialVideoBitrate = i;
        this.initBitFlag = true;
    }

    public void setMinFps(AlivcFpsEnum alivcFpsEnum) {
        this.minFps = alivcFpsEnum;
    }

    public void setMinVideoBitrate(int i) {
        this.mMinVideoBitrate = i;
        this.minBitFlag = true;
    }

    public void setNetworkPoorPushImage(String str) {
        this.mNetworkPoorPushImagePath = str;
    }

    public void setPausePushImage(String str) {
        this.mPausePushImagePath = str;
    }

    public void setPreviewDisplayMode(AlivcPreviewDisplayMode alivcPreviewDisplayMode) {
        this.mPreviewDisplayMode = alivcPreviewDisplayMode;
    }

    public void setPreviewMirror(boolean z) {
        this.mPreviewMirror = z;
    }

    public void setPreviewOrientation(AlivcPreviewOrientationEnum alivcPreviewOrientationEnum) {
        this.mPreviewOrientation = alivcPreviewOrientationEnum;
    }

    public void setPreviewRotation(AlivcPreviewRotationEnum alivcPreviewRotationEnum) {
        this.mPreviewRotation = alivcPreviewRotationEnum;
    }

    public void setPushMirror(boolean z) {
        this.mPushMirror = z;
    }

    public void setQualityMode(AlivcQualityModeEnum alivcQualityModeEnum) {
        this.qualityMode = alivcQualityModeEnum;
    }

    public void setRequireRenderContextNotify(boolean z) {
        this.requireGLSharedContext = z;
    }

    public void setResolution(AlivcResolutionEnum alivcResolutionEnum) {
        this.mResolution = alivcResolutionEnum;
        if (!this.targetBitFlag) {
            this.mTargetVideoBitrate = AlivcResolutionEnum.getTargetBitrate(alivcResolutionEnum);
        }
        if (!this.initBitFlag) {
            this.mInitialVideoBitrate = AlivcResolutionEnum.getInitBitrate(alivcResolutionEnum);
        }
        if (!this.minBitFlag) {
            this.mMinVideoBitrate = AlivcResolutionEnum.getMinBitrate(alivcResolutionEnum);
        }
        this.mWidth = AlivcResolutionEnum.GetResolutionWidth(alivcResolutionEnum);
        this.mHeight = AlivcResolutionEnum.GetResolutionHeight(alivcResolutionEnum);
    }

    public void setSendDataTimeout(int i) {
        this.sendDataTimeout = i;
    }

    public void setTargetVideoBitrate(int i) {
        this.mTargetVideoBitrate = i;
        this.targetBitFlag = true;
    }

    public void setVideoEncodeGop(AlivcVideoEncodeGopEnum alivcVideoEncodeGopEnum) {
        this.mVideoEncodeGop = alivcVideoEncodeGopEnum;
    }

    public void setVideoEncodeMode(AlivcEncodeModeEnum alivcEncodeModeEnum) {
        this.mVideoEncodeMode = alivcEncodeModeEnum;
    }

    protected void setVideoFormat(int i) {
        this.mVideoFormat = i;
    }

    public void setVideoOnly(boolean z) {
        this.mVideoOnly = z;
    }
}
