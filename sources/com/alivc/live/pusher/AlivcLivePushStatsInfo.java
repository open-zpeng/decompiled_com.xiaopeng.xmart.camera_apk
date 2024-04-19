package com.alivc.live.pusher;
/* loaded from: classes.dex */
public class AlivcLivePushStatsInfo {
    private int mAudioCaptureFps;
    private int mAudioDurationFromeCaptureToUpload;
    private int mAudioEncodeBitrate;
    private int mAudioEncodeFps;
    private int mAudioFrameInEncodeBuffer;
    private int mAudioPacketsInUploadBuffer;
    private int mAudioUploadBitrate;
    private int mAudioUploadFps;
    private int mAudioVideoPtsDiff;
    private long mAvPTSInterval;
    private float mCpu;
    private int mCurrentUploadPacketSize;
    private long mCurrentlyUploadedAudioFramePts;
    private long mCurrentlyUploadedVideoFramePts;
    private long mLastAudioFramePTSInQueue;
    private long mLastAudioPtsInBuffer;
    private long mLastVideoFramePTSInQueue;
    private long mLastVideoPtsInBuffer;
    private int mMaxSizeOfAudioPacketsInBuffer;
    private int mMaxSizeOfVideoPacketsInBuffer;
    private float mMemory;
    private long mPreviousVideoKeyFramePts;
    private int mTotalDroppedAudioFrames;
    private long mTotalDurationOfDropingVideoFrames;
    private long mTotalFramesOfEncodedVideo;
    private long mTotalFramesOfUploadedVideo;
    private long mTotalSendedPacketSizeInTwoSecond;
    private long mTotalSizeOfUploadedPackets;
    private long mTotalTimeOfEncodedVideo;
    private long mTotalTimeOfUploading;
    private int mTotalTimesOfDisconnect;
    private long mTotalTimesOfDropingVideoFrames;
    private int mTotalTimesOfReconnect;
    private int mVideoCaptureFps;
    private int mVideoDurationFromeCaptureToUpload;
    private int mVideoEncodeBitrate;
    private int mVideoEncodeFps;
    private AlivcEncodeModeEnum mVideoEncodeMode;
    private int mVideoEncodeParam;
    private int mVideoFramesInEncodeBuffer;
    private int mVideoFramesInRenderBuffer;
    private int mVideoPacketsInUploadBuffer;
    private int mVideoRenderConsumingTimePerFrame;
    private int mVideoRenderFps;
    private int mVideoUploadBitrate;
    private int mVideoUploadeFps;

    public int getAudioCaptureFps() {
        return this.mAudioCaptureFps;
    }

    public int getAudioDurationFromeCaptureToUpload() {
        return this.mAudioDurationFromeCaptureToUpload;
    }

    public int getAudioEncodeBitrate() {
        return this.mAudioEncodeBitrate;
    }

    public int getAudioEncodeFps() {
        return this.mAudioEncodeFps;
    }

    public int getAudioFrameInEncodeBuffer() {
        return this.mAudioFrameInEncodeBuffer;
    }

    public int getAudioPacketsInUploadBuffer() {
        return this.mAudioPacketsInUploadBuffer;
    }

    public int getAudioUploadBitrate() {
        return this.mAudioUploadBitrate;
    }

    public int getAudioUploadFps() {
        return this.mAudioUploadFps;
    }

    public int getAudioVideoPtsDiff() {
        return this.mAudioVideoPtsDiff;
    }

    public long getAvPTSInterval() {
        return this.mAvPTSInterval;
    }

    public float getCpu() {
        return this.mCpu;
    }

    public int getCurrentUploadPacketSize() {
        return this.mCurrentUploadPacketSize;
    }

    public long getCurrentlyUploadedAudioFramePts() {
        return this.mCurrentlyUploadedAudioFramePts;
    }

    public long getCurrentlyUploadedVideoFramePts() {
        return this.mCurrentlyUploadedVideoFramePts;
    }

    public long getLastAudioFramePTSInQueue() {
        return this.mLastAudioFramePTSInQueue;
    }

    public long getLastAudioPtsInBuffer() {
        return this.mLastAudioPtsInBuffer;
    }

    public long getLastVideoFramePTSInQueue() {
        return this.mLastVideoFramePTSInQueue;
    }

    public long getLastVideoPtsInBuffer() {
        return this.mLastVideoPtsInBuffer;
    }

    public int getMaxSizeOfAudioPacketsInBuffer() {
        return this.mMaxSizeOfAudioPacketsInBuffer;
    }

    public int getMaxSizeOfVideoPacketsInBuffer() {
        return this.mMaxSizeOfVideoPacketsInBuffer;
    }

    public float getMemory() {
        return this.mMemory;
    }

    public long getPreviousVideoKeyFramePts() {
        return this.mPreviousVideoKeyFramePts;
    }

    public int getTotalDroppedAudioFrames() {
        return this.mTotalDroppedAudioFrames;
    }

    public long getTotalDurationOfDropingVideoFrames() {
        return this.mTotalDurationOfDropingVideoFrames;
    }

    public long getTotalFramesOfEncodedVideo() {
        return this.mTotalFramesOfEncodedVideo;
    }

    public long getTotalFramesOfUploadedVideo() {
        return this.mTotalFramesOfUploadedVideo;
    }

    public long getTotalSendedPacketSizeInTwoSecond() {
        return this.mTotalSendedPacketSizeInTwoSecond;
    }

    public long getTotalSizeOfUploadedPackets() {
        return this.mTotalSizeOfUploadedPackets;
    }

    public long getTotalTimeOfEncodedVideo() {
        return this.mTotalTimeOfEncodedVideo;
    }

    public long getTotalTimeOfUploading() {
        return this.mTotalTimeOfUploading;
    }

    public int getTotalTimesOfDisconnect() {
        return this.mTotalTimesOfDisconnect;
    }

    public long getTotalTimesOfDropingVideoFrames() {
        return this.mTotalTimesOfDropingVideoFrames;
    }

    public int getTotalTimesOfReconnect() {
        return this.mTotalTimesOfReconnect;
    }

    public int getVideoCaptureFps() {
        return this.mVideoCaptureFps;
    }

    public int getVideoDurationFromeCaptureToUpload() {
        return this.mVideoDurationFromeCaptureToUpload;
    }

    public int getVideoEncodeBitrate() {
        return this.mVideoEncodeBitrate;
    }

    public int getVideoEncodeFps() {
        return this.mVideoEncodeFps;
    }

    public AlivcEncodeModeEnum getVideoEncodeMode() {
        return this.mVideoEncodeMode;
    }

    public int getVideoEncodeParam() {
        return this.mVideoEncodeParam;
    }

    public int getVideoFramesInEncodeBuffer() {
        return this.mVideoFramesInEncodeBuffer;
    }

    public int getVideoFramesInRenderBuffer() {
        return this.mVideoFramesInRenderBuffer;
    }

    public int getVideoPacketsInUploadBuffer() {
        return this.mVideoPacketsInUploadBuffer;
    }

    public int getVideoRenderConsumingTimePerFrame() {
        return this.mVideoRenderConsumingTimePerFrame;
    }

    public int getVideoRenderFps() {
        return this.mVideoRenderFps;
    }

    public int getVideoUploadBitrate() {
        return this.mVideoUploadBitrate;
    }

    public int getVideoUploadeFps() {
        return this.mVideoUploadeFps;
    }

    protected void setAudioCaptureFps(int i) {
        this.mAudioCaptureFps = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAudioDurationFromeCaptureToUpload(int i) {
        this.mAudioDurationFromeCaptureToUpload = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAudioEncodeBitrate(int i) {
        this.mAudioEncodeBitrate = i;
    }

    public void setAudioEncodeFps(int i) {
        this.mAudioEncodeFps = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAudioFrameInEncodeBuffer(int i) {
        this.mAudioFrameInEncodeBuffer = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAudioPacketsInUploadBuffer(int i) {
        this.mAudioPacketsInUploadBuffer = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAudioUploadBitrate(int i) {
        this.mAudioUploadBitrate = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAudioUploadFps(int i) {
        this.mAudioUploadFps = i;
    }

    protected void setAudioVideoPtsDiff(int i) {
        this.mAudioVideoPtsDiff = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAvPTSInterval(long j) {
        this.mAvPTSInterval = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCpu(float f) {
        this.mCpu = f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCurrentUploadPacketSize(int i) {
        this.mCurrentUploadPacketSize = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCurrentlyUploadedAudioFramePts(long j) {
        this.mCurrentlyUploadedAudioFramePts = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCurrentlyUploadedVideoFramePts(long j) {
        this.mCurrentlyUploadedVideoFramePts = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setLastAudioFramePTSInQueue(long j) {
        this.mLastAudioFramePTSInQueue = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setLastAudioPtsInBuffer(long j) {
        this.mLastAudioPtsInBuffer = j;
    }

    public void setLastVideoFramePTSInQueue(long j) {
        this.mLastVideoFramePTSInQueue = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setLastVideoPtsInBuffer(long j) {
        this.mLastVideoPtsInBuffer = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMaxSizeOfAudioPacketsInBuffer(int i) {
        this.mMaxSizeOfAudioPacketsInBuffer = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMaxSizeOfVideoPacketsInBuffer(int i) {
        this.mMaxSizeOfVideoPacketsInBuffer = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMemory(float f) {
        this.mMemory = f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPreviousVideoKeyFramePts(long j) {
        this.mPreviousVideoKeyFramePts = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalDroppedAudioFrames(int i) {
        this.mTotalDroppedAudioFrames = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalDurationOfDropingVideoFrames(long j) {
        this.mTotalDurationOfDropingVideoFrames = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalFramesOfEncodedVideo(long j) {
        this.mTotalFramesOfEncodedVideo = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalFramesOfUploadedVideo(long j) {
        this.mTotalFramesOfUploadedVideo = j;
    }

    protected void setTotalSendedPacketSizeInTwoSecond(long j) {
        this.mTotalSendedPacketSizeInTwoSecond = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalSizeOfUploadedPackets(long j) {
        this.mTotalSizeOfUploadedPackets = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalTimeOfEncodedVideo(long j) {
        this.mTotalTimeOfEncodedVideo = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalTimeOfUploading(long j) {
        this.mTotalTimeOfUploading = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalTimesOfDisconnect(int i) {
        this.mTotalTimesOfDisconnect = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalTimesOfDropingVideoFrames(long j) {
        this.mTotalTimesOfDropingVideoFrames = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTotalTimesOfReconnect(int i) {
        this.mTotalTimesOfReconnect = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoCaptureFps(int i) {
        this.mVideoCaptureFps = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoDurationFromeCaptureToUpload(int i) {
        this.mVideoDurationFromeCaptureToUpload = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoEncodeBitrate(int i) {
        this.mVideoEncodeBitrate = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoEncodeFps(int i) {
        this.mVideoEncodeFps = i;
    }

    public void setVideoEncodeMode(AlivcEncodeModeEnum alivcEncodeModeEnum) {
        this.mVideoEncodeMode = alivcEncodeModeEnum;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoEncodeParam(int i) {
        this.mVideoEncodeParam = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoFramesInEncodeBuffer(int i) {
        this.mVideoFramesInEncodeBuffer = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoFramesInRenderBuffer(int i) {
        this.mVideoFramesInRenderBuffer = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoPacketsInUploadBuffer(int i) {
        this.mVideoPacketsInUploadBuffer = i;
    }

    public void setVideoRenderConsumingTimePerFrame(int i) {
        this.mVideoRenderConsumingTimePerFrame = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoRenderFps(int i) {
        this.mVideoRenderFps = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoUploadBitrate(int i) {
        this.mVideoUploadBitrate = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setVideoUploadeFps(int i) {
        this.mVideoUploadeFps = i;
    }
}
