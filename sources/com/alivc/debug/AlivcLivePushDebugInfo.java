package com.alivc.debug;
/* loaded from: classes.dex */
public class AlivcLivePushDebugInfo {
    private int mAudioCaptureFps;
    private int mAudioEncodeFps;
    private int mAudioFrameInEncodeBuffer;
    private int mAudioPacketsInUploadBuffer;
    private int mAudioUploadBitrate;
    private float mCpu;
    private long mCurrentlyUploadedAudioFramePts;
    private long mCurrentlyUploadedVideoFramePts;
    private int mLatestAudioBitrate;
    private int mLatestVideoBitrate;
    private float mMemory;
    private int mSocketBufferSize;
    private int mSocketSendTime;
    private int mTotalDroppedAudioFrames;
    private long mTotalDurationOfDropingVideoFrames;
    private int mVideoCaptureFps;
    private int mVideoEncodeFps;
    private int mVideoFramesInEncodeBuffer;
    private int mVideoFramesInRenderBuffer;
    private int mVideoPacketsInUploadBuffer;
    private int mVideoRenderFps;
    private int mVideoUploadBitrate;
    private int mVideoUploadeFps;
    private String url;
    private String res = "RESOLUTION_540P";
    private boolean isPush = false;

    public int getAudioUploadBitrate() {
        return this.mAudioUploadBitrate;
    }

    public void setAudioUploadBitrate(int i) {
        this.mAudioUploadBitrate = i;
    }

    public int getVideoUploadBitrate() {
        return this.mVideoUploadBitrate;
    }

    public void setVideoUploadBitrate(int i) {
        this.mVideoUploadBitrate = i;
    }

    public int getAudioPacketsInUploadBuffer() {
        return this.mAudioPacketsInUploadBuffer;
    }

    public void setAudioPacketsInUploadBuffer(int i) {
        this.mAudioPacketsInUploadBuffer = i;
    }

    public int getVideoPacketsInUploadBuffer() {
        return this.mVideoPacketsInUploadBuffer;
    }

    public void setVideoPacketsInUploadBuffer(int i) {
        this.mVideoPacketsInUploadBuffer = i;
    }

    public int getVideoUploadeFps() {
        return this.mVideoUploadeFps;
    }

    public void setVideoUploadeFps(int i) {
        this.mVideoUploadeFps = i;
    }

    public int getVideoCaptureFps() {
        return this.mVideoCaptureFps;
    }

    public void setVideoCaptureFps(int i) {
        this.mVideoCaptureFps = i;
    }

    public long getTotalDurationOfDropingVideoFrames() {
        return this.mTotalDurationOfDropingVideoFrames;
    }

    public void setTotalDurationOfDropingVideoFrames(long j) {
        this.mTotalDurationOfDropingVideoFrames = j;
    }

    public int getVideoEncodeFps() {
        return this.mVideoEncodeFps;
    }

    public void setVideoEncodeFps(int i) {
        this.mVideoEncodeFps = i;
    }

    public float getMemory() {
        return this.mMemory;
    }

    public void setMemory(float f) {
        this.mMemory = f;
    }

    public float getCpu() {
        return this.mCpu;
    }

    public void setCpu(float f) {
        this.mCpu = f;
    }

    public int getVideoRenderFps() {
        return this.mVideoRenderFps;
    }

    public void setVideoRenderFps(int i) {
        this.mVideoRenderFps = i;
    }

    public int getAudioFrameInEncodeBuffer() {
        return this.mAudioFrameInEncodeBuffer;
    }

    public void setAudioFrameInEncodeBuffer(int i) {
        this.mAudioFrameInEncodeBuffer = i;
    }

    public int getVideoFramesInRenderBuffer() {
        return this.mVideoFramesInRenderBuffer;
    }

    public void setVideoFramesInRenderBuffer(int i) {
        this.mVideoFramesInRenderBuffer = i;
    }

    public int getVideoFramesInEncodeBuffer() {
        return this.mVideoFramesInEncodeBuffer;
    }

    public void setVideoFramesInEncodeBuffer(int i) {
        this.mVideoFramesInEncodeBuffer = i;
    }

    public int getTotalDroppedAudioFrames() {
        return this.mTotalDroppedAudioFrames;
    }

    public void setTotalDroppedAudioFrames(int i) {
        this.mTotalDroppedAudioFrames = i;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getRes() {
        return this.res;
    }

    public void setRes(String str) {
        this.res = str;
    }

    public int getLatestVideoBitrate() {
        return this.mLatestVideoBitrate;
    }

    public void setLatestVideoBitrate(int i) {
        this.mLatestVideoBitrate = i;
    }

    public int getLatestAudioBitrate() {
        return this.mLatestAudioBitrate;
    }

    public void setLatestAudioBitrate(int i) {
        this.mLatestAudioBitrate = i;
    }

    public int getSocketBufferSize() {
        return this.mSocketBufferSize;
    }

    public void setSocketBufferSize(int i) {
        this.mSocketBufferSize = i;
    }

    public int getSocketSendTime() {
        return this.mSocketSendTime;
    }

    public void setSocketSendTime(int i) {
        this.mSocketSendTime = i;
    }

    public int getAudioCaptureFps() {
        return this.mAudioCaptureFps;
    }

    public void setAudioCaptureFps(int i) {
        this.mAudioCaptureFps = i;
    }

    public long getCurrentlyUploadedVideoFramePts() {
        return this.mCurrentlyUploadedVideoFramePts;
    }

    public void setCurrentlyUploadedVideoFramePts(long j) {
        this.mCurrentlyUploadedVideoFramePts = j;
    }

    public long getCurrentlyUploadedAudioFramePts() {
        return this.mCurrentlyUploadedAudioFramePts;
    }

    public void setCurrentlyUploadedAudioFramePts(long j) {
        this.mCurrentlyUploadedAudioFramePts = j;
    }

    public int getAudioEncodeFps() {
        return this.mAudioEncodeFps;
    }

    public void setAudioEncodeFps(int i) {
        this.mAudioEncodeFps = i;
    }

    public boolean isPush() {
        return this.isPush;
    }

    public void setPush(boolean z) {
        this.isPush = z;
    }
}
