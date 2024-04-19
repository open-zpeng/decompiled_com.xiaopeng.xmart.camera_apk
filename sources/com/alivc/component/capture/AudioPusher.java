package com.alivc.component.capture;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Process;
import android.telephony.TelephonyManager;
import com.alivc.live.pusher.LogUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
/* loaded from: classes.dex */
public class AudioPusher {
    private static final String TAG = "AudioPusher";
    private AudioRecord audioRecord;
    private AcousticEchoCanceler mAEC;
    private int mFrameSize;
    private Status mStatus;
    private int maxBufferSize;
    private int minBufferSize;
    private final int NTP_TIME_OUT_MILLISECOND = 1000;
    private int mSampleRateInHz = 44100;
    private boolean mPusherRuning = false;
    private boolean mRegisterCallback = false;
    private AudioSourceListener mAudioSourceListener = null;
    private Context mContext = null;
    private byte[] mMuteData = null;
    private TelephonyManager telephonyManager = null;
    private int mAudioChannel = 16;
    private int mAudioFormat = 2;
    private int mAudioFormatBits = 2;
    private long mTimeDelta = 0;
    private AudioManager mAudioManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    final int HEADSET_NONE = -1;
    final int BLUTOOTH_A2DP = 2;
    final int BLUTOOTH_HEADSET = 1;
    final int BLUTOOTH_HEALTH = 3;
    final int WIRE_HEADSET = 10;
    private boolean restartPause = false;
    long startTime = 0;
    long allSendedSize = 0;
    ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();

    /* loaded from: classes.dex */
    class AudioRecordTask implements Runnable {
        AudioRecordTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int i;
            long j;
            long j2;
            byte[] bArr;
            int i2;
            long j3;
            long j4;
            long j5;
            long j6;
            LogUtil.d(AudioPusher.TAG, "run begin ." + AudioPusher.this.mPusherRuning + " " + AudioPusher.this.audioRecord.getRecordingState());
            try {
                Process.setThreadPriority(-19);
            } catch (Exception e) {
                LogUtil.e(AudioPusher.TAG, "Set record thread priority failed: " + e.getMessage());
            }
            int i3 = AudioPusher.this.mFrameSize;
            byte[] bArr2 = new byte[i3];
            long sampleRate = AudioPusher.this.audioRecord.getSampleRate() * AudioPusher.this.audioRecord.getChannelCount() * AudioPusher.this.mAudioFormatBits;
            AudioPusher.this.startTime = System.nanoTime();
            long j7 = 0;
            AudioPusher.this.allSendedSize = 0L;
            long j8 = 0;
            long j9 = -1;
            while (AudioPusher.this.mPusherRuning) {
                if (AudioPusher.this.mPusherRuning && AudioPusher.this.restartPause) {
                    try {
                        Thread.sleep(40L);
                    } catch (Exception unused) {
                    }
                } else if (AudioPusher.this.audioRecord.getRecordingState() != 3) {
                    return;
                } else {
                    int read = AudioPusher.this.audioRecord.read(bArr2, 0, i3);
                    if (read > 0) {
                        if (AudioPusher.this.mStatus == Status.PAUSED && j9 < j7 && AudioPusher.this.isTelephonyCalling()) {
                            j9 = System.nanoTime();
                            LogUtil.d("======", "Pause: cur " + j9);
                        }
                        if (j9 <= j7 || AudioPusher.this.isTelephonyCalling()) {
                            i2 = read;
                            bArr = bArr2;
                            j = sampleRate;
                            j3 = j9;
                            j4 = j8;
                        } else {
                            long j10 = (AudioPusher.this.allSendedSize * 1000) / sampleRate;
                            i2 = read;
                            bArr = bArr2;
                            long nanoTime = (System.nanoTime() - AudioPusher.this.startTime) / 1000000;
                            LogUtil.d(AudioPusher.TAG, "BlueTooth: Alarm, audio capture lower, sendDueTime " + nanoTime + ",sendedTime " + j10);
                            if (nanoTime - j10 > 100) {
                                j = sampleRate;
                                long j11 = ((sampleRate * nanoTime) / 1000) - AudioPusher.this.allSendedSize;
                                LogUtil.d(AudioPusher.TAG, "BlueTooth: Alarm, audio capture lower, sendDueTime " + nanoTime + ",sendedTime " + j10 + ", compensateSize " + j11);
                                while (j11 > 0) {
                                    long j12 = j11 > ((long) AudioPusher.this.mFrameSize) ? AudioPusher.this.mFrameSize : j11;
                                    j11 -= j12;
                                    AudioPusher.this.mAudioSourceListener.onAudioFrame(AudioPusher.this.mMuteData, (int) j12, (System.nanoTime() / 1000) + AudioPusher.this.mTimeDelta, AudioPusher.this.audioRecord.getAudioFormat(), AudioPusher.this.audioRecord.getChannelCount(), AudioPusher.this.audioRecord.getSampleRate());
                                    AudioPusher.this.allSendedSize += j12;
                                }
                            } else {
                                j = sampleRate;
                            }
                            j3 = -1;
                            j4 = 0;
                        }
                        if (AudioPusher.this.mTimeDelta == 0) {
                            AudioPusher.this.mTimeDelta = (System.currentTimeMillis() * 1000) - (System.nanoTime() / 1000);
                            j5 = 0;
                        } else {
                            j5 = 0;
                        }
                        if (j3 > j5) {
                            long nanoTime2 = (((System.nanoTime() - j3) * j) / 1000000000) - j4;
                            long j13 = j4 + nanoTime2;
                            while (nanoTime2 > j5) {
                                long j14 = nanoTime2 > ((long) AudioPusher.this.mFrameSize) ? AudioPusher.this.mFrameSize : nanoTime2;
                                nanoTime2 -= j14;
                                AudioPusher.this.mAudioSourceListener.onAudioFrame(AudioPusher.this.mMuteData, (int) j14, (System.nanoTime() / 1000) + AudioPusher.this.mTimeDelta, AudioPusher.this.audioRecord.getAudioFormat(), AudioPusher.this.audioRecord.getChannelCount(), AudioPusher.this.audioRecord.getSampleRate());
                                AudioPusher.this.allSendedSize += j14;
                                j5 = 0;
                            }
                            i = i3;
                            j9 = j3;
                            j8 = j13;
                            j2 = 0;
                        } else {
                            if (AudioPusher.this.mAudioSourceListener != null) {
                                i = i3;
                                j6 = j3;
                                AudioPusher.this.mAudioSourceListener.onAudioFrame(bArr, i2, AudioPusher.this.mTimeDelta + (System.nanoTime() / 1000), AudioPusher.this.audioRecord.getAudioFormat(), AudioPusher.this.audioRecord.getChannelCount(), AudioPusher.this.audioRecord.getSampleRate());
                                AudioPusher.this.allSendedSize += i2;
                                long j15 = (AudioPusher.this.allSendedSize * 1000) / j;
                                long nanoTime3 = (System.nanoTime() - AudioPusher.this.startTime) / 1000000;
                                LogUtil.d(AudioPusher.TAG, "Alarm, audio capture lower, sendDueTime " + nanoTime3 + ",sendedTime " + j15);
                                if (nanoTime3 - j15 > 100) {
                                    long j16 = ((j * nanoTime3) / 1000) - AudioPusher.this.allSendedSize;
                                    LogUtil.d(AudioPusher.TAG, "BlueTooth: Alarm, audio capture lower, sendDueTime " + nanoTime3 + ",sendedTime " + j15 + ", compensateSize " + j16);
                                    j2 = 0;
                                    while (j16 > 0) {
                                        long j17 = j16 > ((long) AudioPusher.this.mFrameSize) ? AudioPusher.this.mFrameSize : j16;
                                        j16 -= j17;
                                        AudioPusher.this.mAudioSourceListener.onAudioFrame(AudioPusher.this.mMuteData, (int) j17, (System.nanoTime() / 1000) + AudioPusher.this.mTimeDelta, AudioPusher.this.audioRecord.getAudioFormat(), AudioPusher.this.audioRecord.getChannelCount(), AudioPusher.this.audioRecord.getSampleRate());
                                        AudioPusher.this.allSendedSize += j17;
                                    }
                                    j8 = j4;
                                    j9 = j6;
                                }
                            } else {
                                i = i3;
                                j6 = j3;
                            }
                            j2 = 0;
                            j8 = j4;
                            j9 = j6;
                        }
                    } else {
                        i = i3;
                        j = sampleRate;
                        j2 = j7;
                        bArr = bArr2;
                    }
                    bArr2 = bArr;
                    sampleRate = j;
                    i3 = i;
                    j7 = j2;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface AudioSourceListener {
        void onAudioFrame(byte[] bArr, int i, long j, int i2, int i3, int i4);
    }

    /* loaded from: classes.dex */
    enum Status {
        STOPED,
        PAUSED,
        RUNNING
    }

    public AudioPusher() {
        LogUtil.d(TAG, "new AudioPusher.");
        this.mStatus = Status.STOPED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTelephonyCalling() {
        Context context = this.mContext;
        if (context != null) {
            if (this.telephonyManager == null) {
                this.telephonyManager = (TelephonyManager) context.getSystemService("phone");
            }
            if (2 == this.telephonyManager.getCallState() || 1 == this.telephonyManager.getCallState()) {
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        LogUtil.d(TAG, "destroy.");
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord == null) {
            return;
        }
        this.mPusherRuning = false;
        if (audioRecord.getRecordingState() == 1) {
            this.audioRecord.release();
        }
        this.audioRecord = null;
        this.mAudioManager = null;
        this.mContext = null;
        this.mMuteData = null;
        ScheduledExecutorService scheduledExecutorService = this.mExecutorService;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            this.mExecutorService = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0012, code lost:
        if (r5 == 4) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void init(int r2, int r3, int r4, int r5, int r6, android.content.Context r7) {
        /*
            r1 = this;
            r1.mContext = r7
            r1.mSampleRateInHz = r4
            r1.mAudioChannel = r3
            r1.mFrameSize = r6
            r1.mAudioFormat = r5
            r0 = 3
            if (r5 != r0) goto L11
            r0 = 1
        Le:
            r1.mAudioFormatBits = r0
            goto L15
        L11:
            r0 = 4
            if (r5 != r0) goto L15
            goto Le
        L15:
            android.media.AudioManager r0 = r1.mAudioManager
            if (r0 != 0) goto L23
            java.lang.String r0 = "audio"
            java.lang.Object r7 = r7.getSystemService(r0)
            android.media.AudioManager r7 = (android.media.AudioManager) r7
            r1.mAudioManager = r7
        L23:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "====> Init src: "
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.StringBuilder r2 = r7.append(r2)
            java.lang.String r7 = ", channel: "
            java.lang.StringBuilder r2 = r2.append(r7)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = ", sampleRate:"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r3 = ", format:"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r5)
            java.lang.String r3 = ", frameSize:"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "AudioPusher"
            com.alivc.live.pusher.LogUtil.d(r3, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alivc.component.capture.AudioPusher.init(int, int, int, int, int, android.content.Context):void");
    }

    public boolean isPushing() {
        return this.mPusherRuning;
    }

    public void pause() {
        LogUtil.d(TAG, "pause.");
        if (this.mStatus == Status.RUNNING) {
            this.mStatus = Status.PAUSED;
        }
    }

    public void resume() {
        LogUtil.d(TAG, "resume.");
        if (this.mStatus == Status.PAUSED) {
            this.mStatus = Status.RUNNING;
        }
    }

    public void setAudioSourceListener(AudioSourceListener audioSourceListener) {
        this.mAudioSourceListener = audioSourceListener;
    }

    public void start() {
        LogUtil.d(TAG, "start.");
        if (this.mStatus != Status.STOPED) {
            return;
        }
        if (this.mMuteData == null) {
            this.mMuteData = new byte[this.mFrameSize];
            for (int i = 0; i < this.mFrameSize; i++) {
                this.mMuteData[i] = 0;
            }
        }
        int minBufferSize = AudioRecord.getMinBufferSize(this.mSampleRateInHz, this.mAudioChannel, this.mAudioFormat);
        this.minBufferSize = minBufferSize;
        int i2 = this.mFrameSize;
        if (minBufferSize <= i2) {
            this.minBufferSize = i2;
        }
        try {
            AudioRecord audioRecord = new AudioRecord(0, this.mSampleRateInHz, this.mAudioChannel, this.mAudioFormat, this.minBufferSize * 20);
            this.audioRecord = audioRecord;
            try {
                AcousticEchoCanceler create = AcousticEchoCanceler.create(audioRecord.getAudioSessionId());
                this.mAEC = create;
                if (create != null) {
                    create.setEnabled(false);
                    if (this.mAEC.getEnabled()) {
                        this.mAEC.setEnabled(false);
                    }
                }
            } catch (Exception unused) {
            }
            this.maxBufferSize = this.minBufferSize * 10;
            this.mStatus = Status.RUNNING;
            this.mPusherRuning = true;
            if (this.audioRecord.getRecordingState() == 1) {
                try {
                    this.audioRecord.startRecording();
                    int read = this.audioRecord.read(new byte[256], 0, 256);
                    LogUtil.d("======", "audioRecord read len " + read);
                    if (read < 0) {
                        this.mStatus = Status.STOPED;
                        this.mPusherRuning = false;
                        this.audioRecord.release();
                        this.audioRecord = null;
                        return;
                    }
                    LogUtil.d(TAG, "new thread and start thread. " + this.audioRecord.getRecordingState());
                    if (this.audioRecord.getRecordingState() != 1) {
                        this.mExecutorService.execute(new AudioRecordTask());
                        return;
                    }
                    this.mPusherRuning = false;
                    this.audioRecord.release();
                    this.audioRecord = null;
                    throw new IllegalStateException("audio record read fail");
                } catch (Exception unused2) {
                    this.mStatus = Status.STOPED;
                    this.mPusherRuning = false;
                    this.audioRecord.release();
                    this.audioRecord = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        LogUtil.d(TAG, "stop.");
        if (this.audioRecord == null) {
            return;
        }
        this.mPusherRuning = false;
        if (this.mStatus != Status.STOPED) {
            this.mStatus = Status.STOPED;
            if (this.audioRecord.getRecordingState() == 3) {
                this.audioRecord.stop();
            }
        }
        AcousticEchoCanceler acousticEchoCanceler = this.mAEC;
        if (acousticEchoCanceler != null) {
            acousticEchoCanceler.setEnabled(false);
            this.mAEC.release();
            this.mAEC = null;
        }
        if (this.mAudioManager.isBluetoothScoOn()) {
            this.mAudioManager.setBluetoothScoOn(false);
            this.mAudioManager.stopBluetoothSco();
        }
    }
}
