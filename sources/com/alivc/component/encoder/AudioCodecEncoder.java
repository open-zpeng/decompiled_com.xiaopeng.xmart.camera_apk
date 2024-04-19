package com.alivc.component.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
@LiveNativeUsed
/* loaded from: classes.dex */
public class AudioCodecEncoder {
    public static final int ERROR_API_LEVEL = 268448000;
    public static final int ERROR_INPUT_BUFFER_ERROR = 268448002;
    public static final int ERROR_NO_BUFFER_AVAILABLE = 268448003;
    public static final int ERROR_STATE = 268448001;
    public static final int OK = 0;
    private static final int STATE_ENCODING = 2;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_UNINITIALIZED = 0;
    private static final String TAG = "com.alivc.component.encoder.AudioCodecEncoder";
    private ByteBuffer[] mInputBuffers;
    private MediaCodec mMediaCodec;
    private ByteBuffer[] mOutputBuffers;
    int mAudioBytes = 0;
    long mAudioTime = 0;
    long mFirstPts = 0;
    final int[] kSampleRates = {8000, IpcConfig.AIAssistantConfig.IPC_NOTICE_OTA_SUCCESS_MESSAGE_SHOWED, 22050, 44100, 48000};
    final int[] kBitRates = {64000, 128000};
    private int mState = 0;
    private int mSampleRateInHz = 0;
    private int mChannels = 2;
    private int mBitrate = 0;
    private long mBytesPerSecond = 0;
    private int mCurInputBufferIndex = -1;
    private BufferedOutputStream mSaveAAC = null;
    MediaCodecInfo.CodecCapabilities mCodecCaps = null;
    MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private void addADTStoPacket(byte[] bArr, int i) {
        bArr[0] = -1;
        bArr[1] = -7;
        bArr[2] = (byte) 84;
        bArr[3] = (byte) (128 + (i >> 11));
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    public static void clone(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        byteBuffer.rewind();
        byteBuffer2.put(byteBuffer);
        byteBuffer.rewind();
        byteBuffer2.flip();
    }

    private boolean isSupportFormat(int i) {
        if (this.mCodecCaps == null || Build.VERSION.SDK_INT < 21) {
            return false;
        }
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setInteger("aac-profile", i);
        return this.mCodecCaps.isFormatSupported(mediaFormat);
    }

    private MediaCodecInfo selectCodecInfo(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
                continue;
            }
        }
        return null;
    }

    @LiveNativeUsed
    public ByteBuffer getBuffer(long j) {
        int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(j);
        this.mCurInputBufferIndex = dequeueInputBuffer;
        if (dequeueInputBuffer >= 0) {
            return Build.VERSION.SDK_INT >= 21 ? this.mMediaCodec.getInputBuffer(this.mCurInputBufferIndex) : this.mInputBuffers[this.mCurInputBufferIndex];
        }
        Log.d("AndroidAudio", "dequeueInputBuffer failed " + this.mCurInputBufferIndex);
        return null;
    }

    @LiveNativeUsed
    public boolean init(String str, int i, int i2, int i3, int i4) {
        this.mSampleRateInHz = i2;
        this.mChannels = i;
        this.mBitrate = i3;
        if (this.mState == 0) {
            try {
                MediaCodecInfo selectCodecInfo = selectCodecInfo(str);
                if (selectCodecInfo == null) {
                    Log.d(TAG, "not supported mime type (" + str + ")");
                    return false;
                }
                Log.e(TAG, "Create MediaCodec " + selectCodecInfo.getName());
                this.mMediaCodec = MediaCodec.createByCodecName(selectCodecInfo.getName());
                this.mCodecCaps = selectCodecInfo.getCapabilitiesForType(str);
                if (i4 == 23) {
                    i4 = 2;
                }
                if (Build.VERSION.SDK_INT >= 21 && !isSupportFormat(i4)) {
                    i4 = 2;
                }
                MediaFormat mediaFormat = new MediaFormat();
                mediaFormat.setString("mime", str);
                mediaFormat.setInteger("channel-count", i);
                mediaFormat.setInteger("sample-rate", i2);
                mediaFormat.setInteger("bitrate", i3);
                mediaFormat.setInteger("aac-profile", i4);
                mediaFormat.setInteger("max-input-size", 8000);
                try {
                    this.mMediaCodec.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
                } catch (IllegalStateException unused) {
                    Log.e(TAG, "codec '" + str + "' failed configuration.");
                }
                this.mBytesPerSecond = this.mSampleRateInHz * this.mChannels * 2;
                this.mState = 1;
                Log.d(TAG, "Phone Model: " + Build.MODEL);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Create MediaCodec Failed");
                e.printStackTrace();
                return false;
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @LiveNativeUsed
    public int inputFrame(int i, long j) {
        if (i <= 0 || j < 0) {
            if (this.mCurInputBufferIndex < 0) {
                this.mCurInputBufferIndex = this.mMediaCodec.dequeueInputBuffer(-1L);
            }
            this.mMediaCodec.queueInputBuffer(this.mCurInputBufferIndex, 0, 0, 0L, 4);
            return 0;
        }
        int i2 = this.mCurInputBufferIndex;
        if (i2 >= 0) {
            this.mMediaCodec.queueInputBuffer(i2, 0, i, j, 0);
            return 0;
        }
        return 0;
    }

    @LiveNativeUsed
    public int release() {
        if (this.mState == 1) {
            this.mMediaCodec.release();
            this.mMediaCodec = null;
            this.mState = 0;
            return 0;
        }
        return 268448001;
    }

    @LiveNativeUsed
    public int start() {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null && this.mState == 1) {
            try {
                mediaCodec.start();
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
                this.mInputBuffers = this.mMediaCodec.getInputBuffers();
                this.mState = 2;
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 268448001;
    }

    @LiveNativeUsed
    public int stop() {
        MediaCodec mediaCodec;
        if (this.mState != 2 || (mediaCodec = this.mMediaCodec) == null) {
            return 268448001;
        }
        mediaCodec.stop();
        this.mState = 1;
        return 0;
    }

    @LiveNativeUsed
    public AudioCodecData tryRead(long j) {
        AudioCodecData audioCodecData = new AudioCodecData();
        this.mBufferInfo.size = 0;
        int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, j);
        if (dequeueOutputBuffer >= 0) {
            Log.d("AndroidAudio", "dequeueOutputBuffer suc " + this.mBufferInfo.size + ", outindex " + dequeueOutputBuffer + ", dts " + this.mBufferInfo.presentationTimeUs + ", cur " + System.currentTimeMillis());
            if (this.mOutputBuffers != null) {
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
            }
            ByteBuffer outputBuffer = Build.VERSION.SDK_INT >= 21 ? this.mMediaCodec.getOutputBuffer(dequeueOutputBuffer) : this.mOutputBuffers[dequeueOutputBuffer];
            if (this.mBufferInfo.size != 0) {
                outputBuffer.position(this.mBufferInfo.offset);
                outputBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
            }
            if (this.mSaveAAC != null) {
                int i = this.mBufferInfo.size;
                int i2 = i + 7;
                byte[] bArr = new byte[i2];
                addADTStoPacket(bArr, i2);
                outputBuffer.get(bArr, 7, i);
                try {
                    this.mSaveAAC.write(bArr, 0, i2);
                    this.mSaveAAC.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            audioCodecData.setCodecData(outputBuffer, this.mMediaCodec, dequeueOutputBuffer);
            audioCodecData.setPts(System.currentTimeMillis() * 1000);
            if ((this.mBufferInfo.flags & 2) > 0) {
                audioCodecData.setDataType(1);
            } else if ((this.mBufferInfo.flags & 4) > 0) {
                audioCodecData.setEos(true);
            } else {
                audioCodecData.setDataType(2);
            }
        } else {
            if (dequeueOutputBuffer == -2) {
                Log.d("AndroidAudio", "dequeueOutputBuffer INFO_OUTPUT_FORMAT_CHANGED");
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
            } else if (dequeueOutputBuffer == -1) {
                Log.d("AndroidAudio", "dequeueOutputBuffer INFO_TRY_AGAIN_LATER");
            } else if (dequeueOutputBuffer == -3) {
                Log.d("AndroidAudio", "dequeueOutputBuffer INFO_OUTPUT_BUFFERS_CHANGED");
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
            } else {
                Log.d("AndroidAudio", "dequeueOutputBuffer ERROR");
                audioCodecData.setCode(-88);
            }
            audioCodecData.setCode(1);
        }
        return audioCodecData;
    }
}
