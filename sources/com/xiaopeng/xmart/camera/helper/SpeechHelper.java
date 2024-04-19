package com.xiaopeng.xmart.camera.helper;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class SpeechHelper {
    private static final String TAG = "SpeechHelper";
    private TextToSpeech.OnInitListener mOnInitListener;
    private TextToSpeech mTextToSpeech;
    private UtteranceProgressListener mUtteranceProgressListener;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        static SpeechHelper sInstance = new SpeechHelper();

        private SingletonHolder() {
        }
    }

    public static SpeechHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private SpeechHelper() {
        this.mOnInitListener = new TextToSpeech.OnInitListener() { // from class: com.xiaopeng.xmart.camera.helper.SpeechHelper.1
            @Override // android.speech.tts.TextToSpeech.OnInitListener
            public void onInit(int status) {
                if (status == 0) {
                    CameraLog.d(SpeechHelper.TAG, "TextToSpeech init Success", false);
                    if (SpeechHelper.this.mTextToSpeech != null) {
                        SpeechHelper.this.mTextToSpeech.setOnUtteranceProgressListener(SpeechHelper.this.mUtteranceProgressListener);
                        return;
                    }
                    return;
                }
                CameraLog.e(SpeechHelper.TAG, "TextToSpeech init error", false);
            }
        };
        this.mUtteranceProgressListener = new UtteranceProgressListener() { // from class: com.xiaopeng.xmart.camera.helper.SpeechHelper.2
            @Override // android.speech.tts.UtteranceProgressListener
            public void onStart(String utteranceId) {
                CameraLog.d(SpeechHelper.TAG, "onStart: " + utteranceId, false);
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onDone(String utteranceId) {
                CameraLog.d(SpeechHelper.TAG, "onDone: " + utteranceId, false);
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onError(String utteranceId) {
                CameraLog.d(SpeechHelper.TAG, "onError: " + utteranceId, false);
            }
        };
    }

    public void initSpeechService() {
        this.mTextToSpeech = new TextToSpeech(App.getInstance(), this.mOnInitListener);
    }

    public void speak(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        CameraLog.d(TAG, "speak text:" + text, false);
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            textToSpeech.speak(text, 0, null, text);
        }
    }

    public void stop() {
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
}
