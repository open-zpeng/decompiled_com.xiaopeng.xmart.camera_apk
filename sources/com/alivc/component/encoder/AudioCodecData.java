package com.alivc.component.encoder;

import android.media.MediaCodec;
import android.os.Build;
import android.util.Log;
import java.nio.ByteBuffer;
@LiveNativeUsed
/* loaded from: classes.dex */
public class AudioCodecData {
    public static final int DATA_NORMAL_FRAME = 2;
    public static final int DATA_TYPE_SPECIFIC = 1;
    public static final int ERROR_CODE_OK = 0;
    public static final int ERROR_CODE_TRY_AGAIN = 1;
    public static final int ERROR_UNKNOWN = -88;
    private ByteBuffer mCodecData;
    private int mDataType;
    private MediaCodec mMediaCodec;
    private int mOutputBufferId;
    private long mPts;
    private int mCode = 0;
    private boolean mEos = false;

    @LiveNativeUsed
    public int getCode() {
        return this.mCode;
    }

    @LiveNativeUsed
    public ByteBuffer getCodecData() {
        return this.mCodecData;
    }

    @LiveNativeUsed
    public int getDataType() {
        return this.mDataType;
    }

    @LiveNativeUsed
    public boolean getEos() {
        return this.mEos;
    }

    public int getPosition() {
        Log.d("AndroidAudio", "dequeueOutputBuffer get position " + this.mCodecData.limit());
        ByteBuffer byteBuffer = this.mCodecData;
        if (byteBuffer == null) {
            return 0;
        }
        return byteBuffer.limit();
    }

    @LiveNativeUsed
    public long getPts() {
        return this.mPts;
    }

    @LiveNativeUsed
    public void release() {
        MediaCodec mediaCodec;
        int i;
        if (Build.VERSION.SDK_INT >= 16 && (mediaCodec = this.mMediaCodec) != null && (i = this.mOutputBufferId) >= 0) {
            mediaCodec.releaseOutputBuffer(i, false);
            this.mMediaCodec = null;
            this.mOutputBufferId = 0;
        }
        this.mCodecData = null;
    }

    public void setCode(int i) {
        this.mCode = i;
    }

    public void setCodecData(ByteBuffer byteBuffer, MediaCodec mediaCodec, int i) {
        this.mCodecData = byteBuffer;
        this.mMediaCodec = mediaCodec;
        this.mOutputBufferId = i;
    }

    public void setDataType(int i) {
        this.mDataType = i;
    }

    public void setEos(boolean z) {
        this.mEos = z;
    }

    public void setPts(long j) {
        this.mPts = j;
    }

    public String toString() {
        return "MediaCodecData{mDataType=" + this.mDataType + ", mCode=" + this.mCode + ", mPts=" + this.mPts + '}';
    }
}
