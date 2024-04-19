package com.xiaopeng.xmart.camera.helper;

import android.media.SoundPool;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public class SoundPoolHelper implements SoundPool.OnLoadCompleteListener {
    private static final String SOUND_RECORD_END = "sound_record_end";
    private static final String SOUND_RECORD_START = "sound_record_start";
    private static final int STREAM_TYPE = 5;
    private static final String TAG = "SoundPoolHelper";
    private Map<String, Integer> mAudioIds;
    private boolean mAudioLoadCompleted;
    private int mAutoLoadCount;
    private SoundPool mSoundPool;
    private static final String SOUND_TAKE_PICTURE = "sound_take_picture";
    private static final String[] AUDIO_TO_LOAD_ARRAYS = {SOUND_TAKE_PICTURE, SOUND_TAKE_PICTURE, SOUND_TAKE_PICTURE};
    private static SoundPoolHelper INSTANCE = new SoundPoolHelper();

    public static SoundPoolHelper getInstance() {
        return INSTANCE;
    }

    private SoundPoolHelper() {
        this(5);
    }

    private SoundPoolHelper(int streamType) {
        this.mAutoLoadCount = 0;
        SoundPool soundPool = new SoundPool(1, streamType, 0);
        this.mSoundPool = soundPool;
        soundPool.setOnLoadCompleteListener(this);
        this.mAudioIds = new ConcurrentHashMap();
    }

    public void loadSound() {
        load(SOUND_TAKE_PICTURE, "/system/media/audio/xiaopeng/cdu/wav/CDU_camera_shutter.wav");
        load(SOUND_RECORD_START, "/system/media/audio/xiaopeng/cdu/wav/CDU_video_start_1.wav");
        load(SOUND_RECORD_END, "/system/media/audio/xiaopeng/cdu/wav/CDU_video_end_1.wav");
    }

    @Override // android.media.SoundPool.OnLoadCompleteListener
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        CameraLog.i(TAG, "onLoadComplete sampleId" + sampleId);
        int i = this.mAutoLoadCount + 1;
        this.mAutoLoadCount = i;
        if (i == AUDIO_TO_LOAD_ARRAYS.length) {
            this.mAudioLoadCompleted = true;
        }
    }

    private SoundPoolHelper load(String audioName, String path) {
        if (this.mAudioIds.containsKey(audioName)) {
            CameraLog.i(TAG, "audio " + audioName + "is loaded,ignore");
            return this;
        }
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            try {
                this.mAudioIds.put(audioName, Integer.valueOf(soundPool.load(path, 1)));
            } catch (Exception unused) {
                CameraLog.i(TAG, "load audio exception:" + audioName, false);
            }
        }
        return this;
    }

    public void playTakePicture() {
        if (this.mSoundPool != null && this.mAudioIds.containsKey(SOUND_TAKE_PICTURE) && this.mAudioLoadCompleted) {
            ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.helper.-$$Lambda$SoundPoolHelper$kpq2kc_j6iQ2div4LC-UUQR8OyU
                @Override // java.lang.Runnable
                public final void run() {
                    SoundPoolHelper.this.lambda$playTakePicture$0$SoundPoolHelper();
                }
            });
        }
    }

    public /* synthetic */ void lambda$playTakePicture$0$SoundPoolHelper() {
        this.mSoundPool.play(this.mAudioIds.get(SOUND_TAKE_PICTURE).intValue(), 1.0f, 1.0f, 1, 0, 1.0f);
        CameraLog.i(TAG, "playTakePicture:sound_take_picture");
    }

    public void playRecordStart() {
        if (this.mSoundPool != null && this.mAudioIds.containsKey(SOUND_RECORD_START) && this.mAudioLoadCompleted) {
            ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.helper.-$$Lambda$SoundPoolHelper$SWpQXUihZCLvQe1Roq8bDnF8Evg
                @Override // java.lang.Runnable
                public final void run() {
                    SoundPoolHelper.this.lambda$playRecordStart$1$SoundPoolHelper();
                }
            });
        }
    }

    public /* synthetic */ void lambda$playRecordStart$1$SoundPoolHelper() {
        this.mSoundPool.play(this.mAudioIds.get(SOUND_RECORD_START).intValue(), 1.0f, 1.0f, 1, 0, 1.0f);
        CameraLog.i(TAG, "playRecordStart:sound_record_start");
    }

    public void playRecordEnd() {
        if (this.mSoundPool != null && this.mAudioIds.containsKey(SOUND_RECORD_END) && this.mAudioLoadCompleted) {
            ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.helper.-$$Lambda$SoundPoolHelper$9ISlmuLYZE2jNOeR0es74Msko64
                @Override // java.lang.Runnable
                public final void run() {
                    SoundPoolHelper.this.lambda$playRecordEnd$2$SoundPoolHelper();
                }
            });
        }
    }

    public /* synthetic */ void lambda$playRecordEnd$2$SoundPoolHelper() {
        this.mSoundPool.play(this.mAudioIds.get(SOUND_RECORD_END).intValue(), 1.0f, 1.0f, 1, 0, 1.0f);
        CameraLog.i(TAG, "start play:sound_record_end");
    }

    public void release() {
        CameraLog.i(TAG, "release");
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            soundPool.release();
        }
        this.mAudioIds.clear();
        this.mAudioLoadCompleted = false;
        this.mAutoLoadCount = 0;
    }
}
