package com.alivc.component.encoder;

import android.media.MediaCodec;
import android.os.Build;
import android.util.Log;
import com.android.tools.ir.runtime.IncrementalChange;
import com.android.tools.ir.runtime.InstantReloadException;
import java.nio.ByteBuffer;
@NativeUsed
/* loaded from: classes.dex */
public class MediaCodecData {
    public static volatile transient /* synthetic */ IncrementalChange $change = null;
    public static final int DATA_KEY_FRAME = 3;
    public static final int DATA_NORMAL_FRAME = 2;
    public static final int DATA_TYPE_SPECIFIC = 1;
    public static final int ERROR_CODE_OK = 0;
    public static final int ERROR_CODE_TRY_AGAIN = 1;
    public static final int ERROR_UNKNOWN = -1;
    public static final long serialVersionUID = 8367171912036616618L;
    private boolean isEos;
    private int mCode;
    private ByteBuffer mCodecData;
    private int mDataType;
    private long mDts;
    private MediaCodec mMediaCodec;
    private int mOutputBufferId;
    private long mPts;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    MediaCodecData(Object[] objArr, InstantReloadException instantReloadException) {
        this();
        String str = (String) objArr[1];
        int hashCode = str.hashCode();
        if (hashCode != -1968665286) {
            if (hashCode != 357447738) {
                throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/alivc/component/encoder/MediaCodecData"));
            }
        }
    }

    public static /* synthetic */ Object access$super(MediaCodecData mediaCodecData, String str, Object... objArr) {
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
                throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/alivc/component/encoder/MediaCodecData"));
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaCodecData() {
        /*
            r6 = this;
            com.android.tools.ir.runtime.IncrementalChange r0 = com.alivc.component.encoder.MediaCodecData.$change
            if (r0 == 0) goto L27
            r1 = 2
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = 0
            r3 = 0
            r1[r2] = r3
            r4 = 1
            java.lang.Object[] r5 = new java.lang.Object[r2]
            r1[r4] = r5
            java.lang.String r4 = "init$args.([Lcom/alivc/component/encoder/MediaCodecData;[Ljava/lang/Object;)Ljava/lang/Object;"
            java.lang.Object r1 = r0.access$dispatch(r4, r1)
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            r4 = r1[r2]
            java.lang.Object[] r4 = (java.lang.Object[]) r4
            r6.<init>(r1, r3)
            r4[r2] = r6
            java.lang.String r1 = "init$body.(Lcom/alivc/component/encoder/MediaCodecData;[Ljava/lang/Object;)V"
            r0.access$dispatch(r1, r4)
            return
        L27:
            r6.<init>()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alivc.component.encoder.MediaCodecData.<init>():void");
    }

    @NativeUsed
    public int getDataType() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? ((Number) incrementalChange.access$dispatch("getDataType.()I", new Object[]{this})).intValue() : this.mDataType;
    }

    public void setDataType(int i) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("setDataType.(I)V", new Object[]{this, new Integer(i)});
        } else {
            this.mDataType = i;
        }
    }

    @NativeUsed
    public int getCode() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? ((Number) incrementalChange.access$dispatch("getCode.()I", new Object[]{this})).intValue() : this.mCode;
    }

    public void setCode(int i) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("setCode.(I)V", new Object[]{this, new Integer(i)});
        } else {
            this.mCode = i;
        }
    }

    @NativeUsed
    public long getPts() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? ((Number) incrementalChange.access$dispatch("getPts.()J", new Object[]{this})).longValue() : this.mPts;
    }

    public void setPts(long j) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("setPts.(J)V", new Object[]{this, new Long(j)});
        } else {
            this.mPts = j;
        }
    }

    @NativeUsed
    public long getDts() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? ((Number) incrementalChange.access$dispatch("getDts.()J", new Object[]{this})).longValue() : this.mDts;
    }

    public void setDts(long j) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("setDts.(J)V", new Object[]{this, new Long(j)});
        } else {
            this.mDts = j;
        }
    }

    @NativeUsed
    public ByteBuffer getCodecData() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? (ByteBuffer) incrementalChange.access$dispatch("getCodecData.()Ljava/nio/ByteBuffer;", new Object[]{this}) : this.mCodecData;
    }

    public void setCodecData(ByteBuffer byteBuffer, MediaCodec mediaCodec, int i) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("setCodecData.(Ljava/nio/ByteBuffer;Landroid/media/MediaCodec;I)V", new Object[]{this, byteBuffer, mediaCodec, new Integer(i)});
            return;
        }
        this.mCodecData = byteBuffer;
        this.mMediaCodec = mediaCodec;
        this.mOutputBufferId = i;
    }

    public int getPosition() {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            return ((Number) incrementalChange.access$dispatch("getPosition.()I", new Object[]{this})).intValue();
        }
        ByteBuffer byteBuffer = this.mCodecData;
        if (byteBuffer == null) {
            return 0;
        }
        return byteBuffer.limit();
    }

    @NativeUsed
    public void release() {
        MediaCodec mediaCodec;
        int i;
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("release.()V", new Object[]{this});
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= 16 && (mediaCodec = this.mMediaCodec) != null && (i = this.mOutputBufferId) > 0) {
                mediaCodec.releaseOutputBuffer(i, false);
                this.mMediaCodec = null;
                this.mOutputBufferId = 0;
            }
        } catch (Exception e) {
            Log.e("MediaCodecData", "releaseOutputBuffer error" + e.getMessage());
            e.printStackTrace();
        }
        this.mCodecData = null;
    }

    @NativeUsed
    public boolean isEOS() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? ((Boolean) incrementalChange.access$dispatch("isEOS.()Z", new Object[]{this})).booleanValue() : this.isEos;
    }

    public void setEos(boolean z) {
        IncrementalChange incrementalChange = $change;
        if (incrementalChange != null) {
            incrementalChange.access$dispatch("setEos.(Z)V", new Object[]{this, new Boolean(z)});
        } else {
            this.isEos = z;
        }
    }

    public String toString() {
        IncrementalChange incrementalChange = $change;
        return incrementalChange != null ? (String) incrementalChange.access$dispatch("toString.()Ljava/lang/String;", new Object[]{this}) : "MediaCodecData{mDataType=" + this.mDataType + ", mCode=" + this.mCode + ", mPts=" + this.mPts + ", mDts=" + this.mDts + '}';
    }
}
