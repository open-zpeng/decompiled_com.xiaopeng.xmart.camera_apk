package com.xiaopeng.xmart.camera.helper;

import android.content.Intent;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
/* loaded from: classes.dex */
public class SpeechOperationHelper {
    public static final String ACTION_TOP_CAMERA_ROTATE = "com.xiaopeng.speech.topcamera.rotate";
    public static final String RATE = "rate";
    private static final String TAG = "SpeechOperationHelper";
    private boolean mOpenCamera = false;

    public static SpeechOperationHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public void openTopCameraFromSpeech() {
        workingBg(CameraDefine.Actions.ACTION_OPEN_TOP_CAMERA, 101);
    }

    public void closeTopCameraFromSpeech() {
        workingBg(CameraDefine.Actions.ACTION_CLOSE_TOP_CAMERA, 101);
    }

    public void rotateTopCameraFromSpeech(float rate) {
        speechRotateTopCamera(rate);
    }

    public void open360CameraFromSpeech(int type) {
        workingBg(CameraDefine.Actions.ACTION_OPEN_360_CAMERA, type);
    }

    public void takePicFromSpeech(int type) {
        workingBg(CameraDefine.Actions.ACTION_TAKE_PICTURE, type);
    }

    public void recordFromSpeech(int type) {
        workingBg(CameraDefine.Actions.ACTION_RECORD, type);
    }

    public void recordEndFromSpeech() {
        CameraLog.d(TAG, "recordEndFromSpeech", false);
        this.mOpenCamera = true;
        App app = App.getInstance();
        Intent intent = new Intent();
        intent.setClassName(app.getPackageName(), "com.xiaopeng.xmart.camera.MainActivity");
        intent.setAction(CameraDefine.Actions.ACTION_RECORD_END);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        app.startActivity(intent);
    }

    private void workingBg(String action, int type) {
        CameraLog.d(TAG, "recordBg...", false);
        if (type == 101 && !CarCameraHelper.getInstance().hasTopCamera()) {
            CameraLog.d(TAG, "workingBg but without type:" + type + "!", false);
            return;
        }
        this.mOpenCamera = true;
        App app = App.getInstance();
        Intent intent = new Intent();
        intent.setClassName(app.getPackageName(), "com.xiaopeng.xmart.camera.MainActivity");
        intent.setAction(action);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        intent.putExtra(CameraDefine.Actions.ACTION_EXTRA_CAMERA_TYPE, type);
        app.startActivity(intent);
    }

    private void speechRotateTopCamera(float rate) {
        CameraLog.i(TAG, "speechRotateTopCamera().", false);
        this.mOpenCamera = true;
        App app = App.getInstance();
        Intent intent = new Intent();
        intent.setClassName(app.getPackageName(), "com.xiaopeng.xmart.camera.MainActivity");
        intent.setAction("com.xiaopeng.speech.topcamera.rotate");
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        intent.putExtra(RATE, rate);
        app.startActivity(intent);
    }

    public boolean isOpenCamera() {
        return this.mOpenCamera;
    }

    public void setOpenCamera(boolean openCamera) {
        this.mOpenCamera = openCamera;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        static SpeechOperationHelper sInstance = new SpeechOperationHelper();

        private SingletonHolder() {
        }
    }
}
