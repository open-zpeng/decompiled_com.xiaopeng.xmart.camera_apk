package com.alivc.live.pusher;

import android.content.Context;
import android.view.SurfaceView;
/* loaded from: classes.dex */
public interface ILivePusher {
    int addDynamicsAddons(String str, float f, float f2, float f3, float f4);

    void changeResolution(AlivcResolutionEnum alivcResolutionEnum);

    void destroy();

    void focusCameraAtAdjustedPoint(float f, float f2, boolean z);

    int getCurrentExposure();

    AlivcLivePushStats getCurrentStatus();

    int getCurrentZoom();

    AlivcLivePushError getLastError();

    AlivcLivePushStatsInfo getLivePushStatsInfo();

    int getMaxZoom();

    String getPushUrl();

    int getSupportedMaxExposure();

    int getSupportedMinExposure();

    void init(Context context, AlivcLivePushConfig alivcLivePushConfig);

    void inputStreamAudioData(byte[] bArr, int i, int i2, int i3, long j);

    void inputStreamAudioPtr(long j, int i, int i2, int i3, long j2);

    void inputStreamVideoData(byte[] bArr, int i, int i2, int i3, int i4, long j, int i5);

    void inputStreamVideoPtr(long j, int i, int i2, int i3, int i4, long j2, int i5);

    boolean isCameraSupportAutoFocus();

    boolean isCameraSupportFlash();

    boolean isNetworkPushing();

    boolean isPushing();

    void pause();

    void pauseBGM();

    void pauseScreenCapture();

    void reconnectPushAsync(String str);

    void removeDynamicsAddons(int i);

    void restartPush();

    void restartPushAync();

    void resume();

    void resumeAsync();

    void resumeBGM();

    void resumeScreenCapture();

    void setAudioDenoise(boolean z);

    void setAutoFocus(boolean z);

    void setBGMEarsBack(boolean z);

    void setBGMLoop(boolean z);

    void setBGMVolume(int i);

    void setBeautyBigEye(int i);

    void setBeautyBrightness(int i);

    void setBeautyBuffing(int i);

    void setBeautyCheekPink(int i);

    void setBeautyOn(boolean z);

    void setBeautyRuddy(int i);

    void setBeautyShortenFace(int i);

    void setBeautySlimFace(int i);

    void setBeautyWhite(int i);

    void setCaptureVolume(int i);

    void setExposure(int i);

    void setFlash(boolean z);

    void setLivePushBGMListener(AlivcLivePushBGMListener alivcLivePushBGMListener);

    void setLivePushErrorListener(AlivcLivePushErrorListener alivcLivePushErrorListener);

    void setLivePushInfoListener(AlivcLivePushInfoListener alivcLivePushInfoListener);

    void setLivePushNetworkListener(AlivcLivePushNetworkListener alivcLivePushNetworkListener);

    void setLivePushRenderContextListener(AlivcLivePusherRenderContextListener alivcLivePusherRenderContextListener);

    void setLogLevel(AlivcLivePushLogLevel alivcLivePushLogLevel);

    void setMinVideoBitrate(int i);

    void setMute(boolean z);

    void setPreviewMirror(boolean z);

    void setPreviewMode(AlivcPreviewDisplayMode alivcPreviewDisplayMode);

    void setPreviewOrientation(AlivcPreviewOrientationEnum alivcPreviewOrientationEnum);

    void setPushMirror(boolean z);

    void setQualityMode(AlivcQualityModeEnum alivcQualityModeEnum);

    void setScreenOrientation(int i);

    void setTargetVideoBitrate(int i);

    void setWaterMarkVisible(boolean z);

    void setZoom(int i);

    void snapshot(int i, int i2, AlivcSnapshotListener alivcSnapshotListener);

    void startBGMAsync(String str);

    int startCamera(SurfaceView surfaceView);

    int startCameraMix(float f, float f2, float f3, float f4);

    void startPreview(SurfaceView surfaceView);

    void startPreviewAysnc(SurfaceView surfaceView);

    void startPush(String str);

    void startPushAysnc(String str);

    void stopBGMAsync();

    void stopCamera();

    void stopCameraMix();

    void stopPreview();

    void stopPush();

    void switchCamera();
}
