package com.alivc.component.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.Surface;
import com.android.tools.ir.runtime.IncrementalChange;
import com.android.tools.ir.runtime.InstantReloadException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;
@NativeUsed
/* loaded from: classes.dex */
public class VideoCodecEncoder {
    public static volatile transient /* synthetic */ IncrementalChange $change = null;
    public static final int ERROR_API_LEVEL = 268448000;
    public static final int ERROR_INPUT_BUFFER_ERROR = 268448002;
    public static final int ERROR_NO_BUFFER_AVAILABLE = 268448003;
    public static final int ERROR_STATE = 268448001;
    public static final int OK = 0;
    private static final int STATE_ENCODING = 2;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_UNINITIALIZED = 0;
    public static final int[] SUPPORTED_COLOR_FORMATS = {21, 39, 19, 20, 2130706688};
    private static final String TAG = VideoCodecEncoder.class.getName();
    public static final long serialVersionUID = 2499601923112723053L;
    private int encodeFormat;
    public int inputCount;
    private MediaCodec mMediaCodec;
    private ByteBuffer[] mOutputBuffers;
    private int mOutputCount;
    private int mState;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    VideoCodecEncoder(Object[] objArr, InstantReloadException instantReloadException) {
        this();
        String str = (String) objArr[1];
        int hashCode = str.hashCode();
        if (hashCode != -1968665286) {
            if (hashCode != 661356689) {
                throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/alivc/component/encoder/VideoCodecEncoder"));
            }
        }
    }

    public static /* synthetic */ Object access$super(VideoCodecEncoder videoCodecEncoder, String str, Object... objArr) {
        switch (str.hashCode()) {
            case -2128160755:
                return super.toString();
            case -1554832987:
                super.finalize();
                return null;
            case -1021472056:
                super.wait(((Number) objArr[0]).longValue());
                return null;
            case 201261558:
                return super.getClass();
            case 1403628309:
                return new Integer(super.hashCode());
            case 1814730534:
                return new Boolean(super.equals(objArr[0]));
            case 2025021518:
                return super.clone();
            default:
                throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/alivc/component/encoder/VideoCodecEncoder"));
        }
    }

    private static boolean isRecognizedFormat(int i) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Boolean) incrementalChange.access$dispatch("isRecognizedFormat.(I)Z", new Object[]{new Integer(i)})).booleanValue();
        }
        if (i != 39 && i != 2130706688) {
            switch (i) {
                case 19:
                case 20:
                case 21:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public VideoCodecEncoder() {
        /*
            r6 = this;
            com.android.tools.ir.runtime.IncrementalChange r0 = com.alivc.component.encoder.VideoCodecEncoder.$change
            r1 = 0
            if (r0 == 0) goto L27
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r1] = r3
            r4 = 1
            java.lang.Object[] r5 = new java.lang.Object[r1]
            r2[r4] = r5
            java.lang.String r4 = "init$args.([Lcom/alivc/component/encoder/VideoCodecEncoder;[Ljava/lang/Object;)Ljava/lang/Object;"
            java.lang.Object r2 = r0.access$dispatch(r4, r2)
            java.lang.Object[] r2 = (java.lang.Object[]) r2
            r4 = r2[r1]
            java.lang.Object[] r4 = (java.lang.Object[]) r4
            r6.<init>(r2, r3)
            r4[r1] = r6
            java.lang.String r1 = "init$body.(Lcom/alivc/component/encoder/VideoCodecEncoder;[Ljava/lang/Object;)V"
            r0.access$dispatch(r1, r4)
            return
        L27:
            r6.<init>()
            r6.mState = r1
            r0 = -1
            r6.encodeFormat = r0
            r6.mOutputCount = r1
            r6.inputCount = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alivc.component.encoder.VideoCodecEncoder.<init>():void");
    }

    @NativeUsed
    public int initWithColorSpace(String str, MediaFormat mediaFormat, boolean z, int i, int i2, int i3) {
        int i4;
        InvocationTargetException invocationTargetException;
        int i5;
        NoSuchMethodException noSuchMethodException;
        int i6;
        IllegalAccessException illegalAccessException;
        int i7;
        IOException iOException;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        if (this.mState != 0) {
            return 268448001;
        }
        try {
            try {
                this.mOutputCount = 0;
                try {
                    MediaCodecInfo selectCodecInfo = selectCodecInfo(str);
                    if (selectCodecInfo == null) {
                        Log.e(TAG, "not supported mime type (" + str + ")");
                        return -1;
                    }
                    String str8 = TAG;
                    Log.i(str8, "Create MediaCodec " + selectCodecInfo.getName());
                    this.mMediaCodec = MediaCodec.createByCodecName(selectCodecInfo.getName());
                    MediaCodecInfo.CodecCapabilities capabilitiesForType = selectCodecInfo.getCapabilitiesForType(str);
                    Log.i(str8, Arrays.toString(capabilitiesForType.colorFormats));
                    int selectColorFormat = selectColorFormat(selectCodecInfo, z, str);
                    this.encodeFormat = selectColorFormat;
                    if (selectColorFormat == 0) {
                        return -1;
                    }
                    Log.i(str8, "selected format " + this.encodeFormat);
                    mediaFormat.setInteger("color-format", this.encodeFormat);
                    mediaFormat.getInteger("frame-rate");
                    mediaFormat.setInteger("i-frame-interval", i);
                    mediaFormat.setInteger("profile", 1);
                    mediaFormat.setInteger("level", 512);
                    if (Build.VERSION.SDK_INT >= 21) {
                        try {
                            str2 = "level";
                            MediaCodecInfo.VideoCapabilities videoCapabilities = (MediaCodecInfo.VideoCapabilities) MediaCodecInfo.VideoCapabilities.class.getMethod("create", MediaFormat.class, MediaCodecInfo.CodecCapabilities.class).invoke(null, mediaFormat, capabilitiesForType);
                            Range<Integer> bitrateRange = videoCapabilities.getBitrateRange();
                            int widthAlignment = videoCapabilities.getWidthAlignment();
                            int heightAlignment = videoCapabilities.getHeightAlignment();
                            Range<Integer> supportedWidths = videoCapabilities.getSupportedWidths();
                            Range<Integer> supportedHeights = videoCapabilities.getSupportedHeights();
                            int integer = mediaFormat.getInteger("width");
                            str3 = "width";
                            int integer2 = mediaFormat.getInteger("height");
                            str4 = "height";
                            str5 = "profile";
                            str6 = "frame-rate";
                            str7 = "i-frame-interval";
                            Log.i(str8, "bitrateRange [" + bitrateRange.getLower() + "," + bitrateRange.getUpper() + "], widthAlignment = " + widthAlignment + ", heightAlignment = " + heightAlignment + ", widthRange [" + supportedWidths.getLower() + "," + supportedWidths.getUpper() + "], heightRange [" + supportedHeights.getLower() + "," + supportedHeights.getUpper() + "], isSizeSupport = " + videoCapabilities.isSizeSupported(integer, integer2) + ", sizeAndRateSupport = " + videoCapabilities.areSizeAndRateSupported(integer, integer2, mediaFormat.getInteger("frame-rate")));
                            if (Build.VERSION.SDK_INT >= 23) {
                                mediaFormat.setInteger("stride", integer);
                                mediaFormat.setInteger("slice-height", integer2);
                            }
                        } catch (IOException e) {
                            e = e;
                            iOException = e;
                            i7 = 268448001;
                            Log.e(TAG, "Create MediaCodec Failed");
                            iOException.printStackTrace();
                            return i7;
                        } catch (IllegalAccessException e2) {
                            e = e2;
                            illegalAccessException = e;
                            i6 = 268448001;
                            illegalAccessException.printStackTrace();
                            return i6;
                        } catch (NoSuchMethodException e3) {
                            e = e3;
                            noSuchMethodException = e;
                            i5 = 268448001;
                            noSuchMethodException.printStackTrace();
                            return i5;
                        } catch (InvocationTargetException e4) {
                            e = e4;
                            invocationTargetException = e;
                            i4 = 268448001;
                            invocationTargetException.printStackTrace();
                            return i4;
                        } catch (Exception e5) {
                            e = e5;
                            e.printStackTrace();
                            return 268448001;
                        }
                    } else {
                        str3 = "width";
                        str2 = "level";
                        str5 = "profile";
                        str7 = "i-frame-interval";
                        str6 = "frame-rate";
                        str4 = "height";
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        if (1 != i2) {
                            if (2 != i2) {
                                if (6 == i2) {
                                }
                                if (1 != i3 || 2 == i3) {
                                    mediaFormat.setInteger("color-range", i3);
                                }
                            }
                        }
                        mediaFormat.setInteger("color-standard", i2);
                        mediaFormat.setInteger("color-transfer", 3);
                        if (1 != i3) {
                        }
                        mediaFormat.setInteger("color-range", i3);
                    } else {
                        Log.i(str8, "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT + " is lower than 24");
                    }
                    Log.i(str8, "encoder bitrate = " + mediaFormat.getInteger("bitrate") + ", encoder i frame interval = " + mediaFormat.getInteger(str7) + ", encoder frame rate = " + mediaFormat.getInteger(str6) + ", encoder profile = " + mediaFormat.getInteger(str5) + ", encoder level = " + mediaFormat.getInteger(str2) + ", encoder width = " + mediaFormat.getInteger(str3) + ", encoder height = " + mediaFormat.getInteger(str4) + ", colorStand = " + i2 + ", colorRange = " + i3);
                    this.mMediaCodec.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
                    this.mState = 1;
                    if ("OMX.IMG.TOPAZ.VIDEO.Encoder".equals(selectCodecInfo.getName())) {
                        Log.i(str8, "Product:" + Build.PRODUCT);
                        int i8 = this.encodeFormat;
                        if (i8 != 39) {
                            switch (i8) {
                                case 19:
                                    this.encodeFormat = 20;
                                    break;
                                case 20:
                                    this.encodeFormat = 19;
                                    break;
                                case 21:
                                    this.encodeFormat = 39;
                                    break;
                            }
                        } else {
                            this.encodeFormat = 21;
                        }
                    }
                    return this.encodeFormat;
                } catch (IOException e6) {
                    e = e6;
                } catch (IllegalAccessException e7) {
                    e = e7;
                } catch (NoSuchMethodException e8) {
                    e = e8;
                } catch (InvocationTargetException e9) {
                    e = e9;
                }
            } catch (Exception e10) {
                e = e10;
            }
        } catch (IOException e11) {
            i7 = 268448001;
            iOException = e11;
        } catch (IllegalAccessException e12) {
            i6 = 268448001;
            illegalAccessException = e12;
        } catch (NoSuchMethodException e13) {
            i5 = 268448001;
            noSuchMethodException = e13;
        } catch (InvocationTargetException e14) {
            i4 = 268448001;
            invocationTargetException = e14;
        }
    }

    @NativeUsed
    public int init(String str, MediaFormat mediaFormat, boolean z, int i) {
        int i2;
        InvocationTargetException invocationTargetException;
        int i3;
        NoSuchMethodException noSuchMethodException;
        int i4;
        IllegalAccessException illegalAccessException;
        int i5;
        IOException iOException;
        MediaCodecInfo mediaCodecInfo;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        if (this.mState != 0) {
            return 268448001;
        }
        try {
            try {
                this.mOutputCount = 0;
                try {
                    MediaCodecInfo selectCodecInfo = selectCodecInfo(str);
                    if (selectCodecInfo == null) {
                        Log.d(TAG, "not supported mime type (" + str + ")");
                        return -1;
                    }
                    String str7 = TAG;
                    Log.i(str7, "Create MediaCodec " + selectCodecInfo.getName());
                    this.mMediaCodec = MediaCodec.createByCodecName(selectCodecInfo.getName());
                    MediaCodecInfo.CodecCapabilities capabilitiesForType = selectCodecInfo.getCapabilitiesForType(str);
                    Log.i(str7, Arrays.toString(capabilitiesForType.colorFormats));
                    int selectColorFormat = selectColorFormat(selectCodecInfo, z, str);
                    this.encodeFormat = selectColorFormat;
                    if (selectColorFormat == 0) {
                        return -1;
                    }
                    Log.i(str7, "selected format " + this.encodeFormat);
                    mediaFormat.setInteger("color-format", this.encodeFormat);
                    mediaFormat.getInteger("frame-rate");
                    mediaFormat.setInteger("i-frame-interval", i);
                    mediaFormat.setInteger("profile", 1);
                    mediaFormat.setInteger("level", 512);
                    if (Build.VERSION.SDK_INT >= 21) {
                        try {
                            mediaCodecInfo = selectCodecInfo;
                            MediaCodecInfo.VideoCapabilities videoCapabilities = (MediaCodecInfo.VideoCapabilities) MediaCodecInfo.VideoCapabilities.class.getMethod("create", MediaFormat.class, MediaCodecInfo.CodecCapabilities.class).invoke(null, mediaFormat, capabilitiesForType);
                            Range<Integer> bitrateRange = videoCapabilities.getBitrateRange();
                            int widthAlignment = videoCapabilities.getWidthAlignment();
                            int heightAlignment = videoCapabilities.getHeightAlignment();
                            Range<Integer> supportedWidths = videoCapabilities.getSupportedWidths();
                            Range<Integer> supportedHeights = videoCapabilities.getSupportedHeights();
                            int integer = mediaFormat.getInteger("width");
                            str2 = "width";
                            int integer2 = mediaFormat.getInteger("height");
                            str3 = "height";
                            str4 = "level";
                            str5 = "profile";
                            str6 = "frame-rate";
                            Log.i(str7, "bitrateRange [" + bitrateRange.getLower() + "," + bitrateRange.getUpper() + "], widthAlignment = " + widthAlignment + ", heightAlignment = " + heightAlignment + ", widthRange [" + supportedWidths.getLower() + "," + supportedWidths.getUpper() + "], heightRange [" + supportedHeights.getLower() + "," + supportedHeights.getUpper() + "], isSizeSupport = " + videoCapabilities.isSizeSupported(integer, integer2) + ", sizeAndRateSupport = " + videoCapabilities.areSizeAndRateSupported(integer, integer2, mediaFormat.getInteger("frame-rate")));
                            if (Build.VERSION.SDK_INT >= 23) {
                                mediaFormat.setInteger("stride", integer);
                                mediaFormat.setInteger("slice-height", integer2);
                            }
                        } catch (IOException e) {
                            e = e;
                            iOException = e;
                            i5 = 268448001;
                            Log.e(TAG, "Create MediaCodec Failed");
                            iOException.printStackTrace();
                            return i5;
                        } catch (IllegalAccessException e2) {
                            e = e2;
                            illegalAccessException = e;
                            i4 = 268448001;
                            illegalAccessException.printStackTrace();
                            return i4;
                        } catch (NoSuchMethodException e3) {
                            e = e3;
                            noSuchMethodException = e;
                            i3 = 268448001;
                            noSuchMethodException.printStackTrace();
                            return i3;
                        } catch (InvocationTargetException e4) {
                            e = e4;
                            invocationTargetException = e;
                            i2 = 268448001;
                            invocationTargetException.printStackTrace();
                            return i2;
                        } catch (Exception e5) {
                            e = e5;
                            e.printStackTrace();
                            return 268448001;
                        }
                    } else {
                        str4 = "level";
                        str5 = "profile";
                        str6 = "frame-rate";
                        str2 = "width";
                        mediaCodecInfo = selectCodecInfo;
                        str3 = "height";
                    }
                    Log.i(str7, "encoder bitrate = " + mediaFormat.getInteger("bitrate") + ", encoder i frame interval = " + mediaFormat.getInteger("i-frame-interval") + ", encoder frame rate = " + mediaFormat.getInteger(str6) + ", encoder profile = " + mediaFormat.getInteger(str5) + ", encoder level = " + mediaFormat.getInteger(str4) + ", encoder width = " + mediaFormat.getInteger(str2) + ", encoder height = " + mediaFormat.getInteger(str3));
                    this.mMediaCodec.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
                    this.mState = 1;
                    if ("OMX.IMG.TOPAZ.VIDEO.Encoder".equals(mediaCodecInfo.getName())) {
                        Log.i(str7, "Product:" + Build.PRODUCT);
                        int i6 = this.encodeFormat;
                        if (i6 != 39) {
                            switch (i6) {
                                case 19:
                                    this.encodeFormat = 20;
                                    break;
                                case 20:
                                    this.encodeFormat = 19;
                                    break;
                                case 21:
                                    this.encodeFormat = 39;
                                    break;
                            }
                        } else {
                            this.encodeFormat = 21;
                        }
                    }
                    return this.encodeFormat;
                } catch (IOException e6) {
                    e = e6;
                } catch (IllegalAccessException e7) {
                    e = e7;
                } catch (NoSuchMethodException e8) {
                    e = e8;
                } catch (InvocationTargetException e9) {
                    e = e9;
                }
            } catch (Exception e10) {
                e = e10;
            }
        } catch (IOException e11) {
            i5 = 268448001;
            iOException = e11;
        } catch (IllegalAccessException e12) {
            i4 = 268448001;
            illegalAccessException = e12;
        } catch (NoSuchMethodException e13) {
            i3 = 268448001;
            noSuchMethodException = e13;
        } catch (InvocationTargetException e14) {
            i2 = 268448001;
            invocationTargetException = e14;
        }
    }

    @NativeUsed
    public Surface createInputSurface() {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return (Surface) incrementalChange.access$dispatch("createInputSurface.()Landroid/view/Surface;", new Object[]{this});
        }
        if (this.mMediaCodec != null && this.mState == 1 && Build.VERSION.SDK_INT >= 18) {
            try {
                return this.mMediaCodec.createInputSurface();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "create surface input failed");
                return null;
            }
        }
        Log.e(TAG, "not support surface input");
        return null;
    }

    @NativeUsed
    public int start() {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("start.()I", new Object[]{this})).intValue();
        }
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null && this.mState == 1) {
            try {
                mediaCodec.start();
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
                this.mState = 2;
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 268448001;
    }

    @NativeUsed
    public int inputFrame(byte[] bArr, long j, long j2, boolean z) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("inputFrame.([BJJZ)I", new Object[]{this, bArr, new Long(j), new Long(j2), new Boolean(z)})).intValue();
        }
        try {
            ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
            int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(j2);
            if (dequeueInputBuffer >= 0) {
                inputBuffers[dequeueInputBuffer].clear();
                if (bArr == null) {
                    Log.e(TAG, "Symptom of the \"Callback buffer was to small\" problem...");
                } else {
                    inputBuffers[dequeueInputBuffer].put(bArr, 0, bArr.length);
                }
                this.inputCount++;
                if (z && Build.VERSION.SDK_INT >= 19) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("request-sync", 0);
                    this.mMediaCodec.setParameters(bundle);
                }
                this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, inputBuffers[dequeueInputBuffer].position(), j, z ? 1 : 0);
                return 0;
            }
            Log.e(TAG, "No buffer available !");
            return 268448003;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @NativeUsed
    public MediaCodecData tryRead(long j) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return (MediaCodecData) incrementalChange.access$dispatch("tryRead.(J)Lcom/alivc/component/encoder/MediaCodecData;", new Object[]{this, new Long(j)});
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        MediaCodecData mediaCodecData = new MediaCodecData();
        try {
            int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(bufferInfo, j);
            if (dequeueOutputBuffer >= 0) {
                ByteBuffer byteBuffer = this.mOutputBuffers[dequeueOutputBuffer];
                if (bufferInfo.size != 0) {
                    byteBuffer.position(bufferInfo.offset);
                    byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                }
                mediaCodecData.setCodecData(byteBuffer, this.mMediaCodec, dequeueOutputBuffer);
                this.mOutputCount++;
                if ((bufferInfo.flags & 2) > 0) {
                    mediaCodecData.setDataType(1);
                } else if ((bufferInfo.flags & 1) > 0) {
                    mediaCodecData.setDataType(3);
                } else {
                    mediaCodecData.setDataType(2);
                }
                mediaCodecData.setPts(bufferInfo.presentationTimeUs);
                mediaCodecData.setDts(bufferInfo.presentationTimeUs);
            } else if (dequeueOutputBuffer == -2) {
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
                mediaCodecData.setCode(1);
            } else if (dequeueOutputBuffer == -1) {
                mediaCodecData.setCode(1);
            } else if (dequeueOutputBuffer == -3) {
                this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
                mediaCodecData.setCode(1);
            } else {
                mediaCodecData.setCode(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaCodecData;
    }

    @NativeUsed
    public int updateBitrate(int i) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("updateBitrate.(I)I", new Object[]{this, new Integer(i)})).intValue();
        }
        if (Build.VERSION.SDK_INT < 19) {
            return 268448000;
        }
        if (this.mState != 2 || this.mMediaCodec == null) {
            return 268448001;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("video-bitrate", i * 1000);
        try {
            this.mMediaCodec.setParameters(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @NativeUsed
    public int stop() {
        MediaCodec mediaCodec;
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("stop.()I", new Object[]{this})).intValue();
        }
        if (this.mState == 2 && (mediaCodec = this.mMediaCodec) != null) {
            try {
                if (this.mOutputCount > 0) {
                    mediaCodec.flush();
                }
                this.mMediaCodec.stop();
                this.mState = 1;
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 268448001;
    }

    @NativeUsed
    public int release() {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("release.()I", new Object[]{this})).intValue();
        }
        Log.i(TAG, "input" + this.inputCount + "output" + this.mOutputCount);
        if (this.mState == 1) {
            this.mMediaCodec.release();
            this.mMediaCodec = null;
            this.mState = 0;
            return 0;
        }
        return 268448001;
    }

    private MediaCodecInfo selectCodecInfo(String str) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return (MediaCodecInfo) incrementalChange.access$dispatch("selectCodecInfo.(Ljava/lang/String;)Landroid/media/MediaCodecInfo;", new Object[]{this, str});
        }
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

    private static int selectColorFormat(MediaCodecInfo mediaCodecInfo, boolean z, String str) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("selectColorFormat.(Landroid/media/MediaCodecInfo;ZLjava/lang/String;)I", new Object[]{mediaCodecInfo, new Boolean(z), str})).intValue();
        }
        if (z) {
            return 2130708361;
        }
        MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
        for (int i = 0; i < capabilitiesForType.colorFormats.length; i++) {
            int i2 = capabilitiesForType.colorFormats[i];
            if (isRecognizedFormat(i2)) {
                return i2;
            }
        }
        return 0;
    }
}
