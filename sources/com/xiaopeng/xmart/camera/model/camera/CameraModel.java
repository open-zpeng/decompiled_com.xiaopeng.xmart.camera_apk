package com.xiaopeng.xmart.camera.model.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.SoundPoolHelper;
import com.xiaopeng.xmart.camera.manager.CameraManager;
import com.xiaopeng.xmart.camera.model.camera.ICameraModel;
import com.xiaopeng.xmart.camera.speech.ISpeechContants;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.FileUtils;
import com.xiaopeng.xmart.camera.utils.JumpGalleryUtil;
import com.xiaopeng.xmart.camera.utils.MediaStoreUtils;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import java.io.File;
/* loaded from: classes.dex */
public class CameraModel implements ICameraModel, Camera.PictureCallback, MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener, CameraManager.Camera2DeviceCallback, CameraManager.Camera2PreviewCallback {
    private static final String TAG = "CameraModel";
    private static final int VIDEO_MAX_OFFSET = 1000;
    public static final Object sLock = new Object();
    protected ICameraModel.Callback mCallback;
    private Camera mCamera;
    private CameraDevice mCameraDevices;
    private volatile boolean mCameraOpened;
    protected Context mContext;
    private MediaRecorder mMediaRecorder;
    private volatile boolean mStartPreviedCalled;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceTexture mSurfaceTexture;
    private CountDownTimer mTimer;

    /* loaded from: classes.dex */
    public interface RecordTimeListener {
        void onFinish();

        void onTick(String timeText, boolean isEvenNumber);
    }

    private long getMaxRecordTime() {
        return 301000L;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public int getBucketId() {
        return 0;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public String getFileDir() {
        return null;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public String getGalleryTab() {
        return null;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void resetData() {
    }

    public CameraModel(ICameraModel.Callback callback) {
        CameraLog.d(TAG, "CameraModel, callback", false);
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public synchronized void openCamera(final int cameraId) {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$Y64KrvQ-PWo-sfGDzV-FmJ6vI94
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$openCamera$0$CameraModel(cameraId);
            }
        });
    }

    public /* synthetic */ void lambda$openCamera$0$CameraModel(int i) {
        String str = TAG;
        CameraLog.d(str, "openCamera, runnable,cameraId:" + i, false);
        synchronized (sLock) {
            if (CameraManager.getInstance().isSupportCameraApi2()) {
                try {
                    CameraManager.getInstance().openCameraApi2(i, this);
                } catch (Exception unused) {
                    CameraLog.d(TAG, "openCamera, runnable,cameraId:" + i + ",openSuccess：false", false);
                    ICameraModel.Callback callback = this.mCallback;
                    if (callback != null) {
                        callback.onCameraOpen(false);
                    }
                }
            } else {
                try {
                    this.mCamera = CameraManager.getInstance().openCamera(i);
                    CameraLog.d(str, "omCamera:" + this.mCamera, false);
                    CameraLog.d(str, "openCamera, runnable,cameraId:" + i + ",openSuccess：true", false);
                    ICameraModel.Callback callback2 = this.mCallback;
                    if (callback2 != null) {
                        callback2.onCameraOpen(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CameraLog.d(TAG, "openCamera, runnable,cameraId:" + i + ",openSuccess：false", false);
                    ICameraModel.Callback callback3 = this.mCallback;
                    if (callback3 != null) {
                        callback3.onCameraOpen(false);
                    }
                }
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public synchronized void startPreview(final SurfaceHolder holder) {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$6II1-pvZC3WnHRC95SRLTeYbgK0
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$startPreview$1$CameraModel(holder);
            }
        });
    }

    public /* synthetic */ void lambda$startPreview$1$CameraModel(SurfaceHolder surfaceHolder) {
        synchronized (sLock) {
            if (CameraManager.getInstance().isSupportCameraApi2()) {
                CameraLog.d(TAG, "startPreview, mCameraOpened." + this.mCameraOpened + ",mStartPreviedCalled:" + this.mStartPreviedCalled, false);
                this.mSurfaceHolder = surfaceHolder;
                try {
                    CameraManager.getInstance().setPreviewCallback(this);
                    if (this.mCameraOpened && this.mCameraDevices != null && !this.mStartPreviedCalled) {
                        CameraManager.getInstance().startPreviewApi2(surfaceHolder);
                        this.mStartPreviedCalled = true;
                    }
                } catch (Exception e) {
                    this.mStartPreviedCalled = false;
                    LogUtils.d(TAG, "startPreviewApi2 failed! Reason: " + e.getMessage());
                }
            } else {
                String str = TAG;
                CameraLog.d(str, "startPreview, runnable." + this.mCamera, false);
                try {
                    boolean startPreview = CameraManager.getInstance().startPreview(surfaceHolder);
                    CameraLog.d(str, "finally runnable,startSuccess:" + startPreview + ",camera:" + this.mCamera, false);
                    ICameraModel.Callback callback = this.mCallback;
                    if (callback != null) {
                        callback.onCameraPreview(startPreview);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    CameraLog.d(TAG, "finally runnable,startSuccess:false,camera:" + this.mCamera, false);
                    ICameraModel.Callback callback2 = this.mCallback;
                    if (callback2 != null) {
                        callback2.onCameraPreview(false);
                    }
                }
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public synchronized void startPreview(final SurfaceTexture surfaceTexture) {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$1d8M0ouu8_jecF3Zkfxy0PG7lnw
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$startPreview$2$CameraModel(surfaceTexture);
            }
        });
    }

    public /* synthetic */ void lambda$startPreview$2$CameraModel(SurfaceTexture surfaceTexture) {
        synchronized (sLock) {
            if (CameraManager.getInstance().isSupportCameraApi2()) {
                CameraLog.d(TAG, "startPreview, mCameraOpened." + this.mCameraOpened + ",mStartPreviedCalled:" + this.mStartPreviedCalled, false);
                this.mSurfaceTexture = surfaceTexture;
                CameraManager.getInstance().setPreviewCallback(this);
                if (this.mCameraOpened) {
                    if ((!this.mStartPreviedCalled) & (this.mCameraDevices != null)) {
                        CameraManager.getInstance().startPreviewApi2(surfaceTexture);
                        this.mStartPreviedCalled = true;
                    }
                }
            } else {
                String str = TAG;
                CameraLog.d(str, "startPreview, runnable." + this.mCamera, false);
                try {
                    boolean startPreview = CameraManager.getInstance().startPreview(surfaceTexture);
                    CameraLog.d(str, "finally runnable,previewSuccess:" + startPreview + ",camera:" + this.mCamera, false);
                    ICameraModel.Callback callback = this.mCallback;
                    if (callback != null) {
                        callback.onCameraPreview(startPreview);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String str2 = TAG;
                    CameraLog.d(str2, "startPreview exception:" + (TextUtils.isEmpty(e.getMessage()) ? "" : e.getMessage()), false);
                    CameraLog.d(str2, "finally runnable,previewSuccess:false,camera:" + this.mCamera, false);
                    ICameraModel.Callback callback2 = this.mCallback;
                    if (callback2 != null) {
                        callback2.onCameraPreview(false);
                    }
                }
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public synchronized void stopPreview() {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$uNFZfpqgyNLq7bGzx8Yhj_V0roM
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$stopPreview$3$CameraModel();
            }
        });
    }

    public /* synthetic */ void lambda$stopPreview$3$CameraModel() {
        CameraLog.d(TAG, "stopPreview, runnable.", false);
        synchronized (sLock) {
            try {
                this.mStartPreviedCalled = false;
                if (CameraManager.getInstance().isSupportCameraApi2()) {
                    CameraManager.getInstance().setPreviewCallback(null);
                    CameraManager.getInstance().stopPreviewApi2();
                } else {
                    CameraManager.getInstance().stopPreview();
                }
            } catch (Exception e) {
                CameraLog.d(TAG, "stopPreview exception:" + (TextUtils.isEmpty(e.getMessage()) ? "" : e.getMessage()), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public synchronized void release() {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$PVpkQr76X_Rl5RATG20aPkiGVZI
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$release$4$CameraModel();
            }
        });
    }

    public /* synthetic */ void lambda$release$4$CameraModel() {
        CameraLog.d(TAG, "release, runnable.", false);
        synchronized (sLock) {
            try {
                if (CameraManager.getInstance().isSupportCameraApi2()) {
                    this.mStartPreviedCalled = false;
                    this.mCameraOpened = false;
                    CameraManager.getInstance().releaseCameraApi2();
                } else {
                    CameraManager.getInstance().releaseCamera();
                }
            } catch (Exception e) {
                CameraLog.d(TAG, "releaseCamera exception:" + (TextUtils.isEmpty(e.getMessage()) ? "" : e.getMessage()), false);
            }
            this.mCameraDevices = null;
            this.mCamera = null;
            ICameraModel.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onCameraRelease();
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void takePicture() {
        playTakingPicAudio();
        CameraLog.i(TAG, "takePicture", false);
        CameraManager.getInstance().takePicture(this);
    }

    @Override // android.hardware.Camera.PictureCallback
    public void onPictureTaken(final byte[] data, Camera camera) {
        ThreadPoolHelper.getInstance().executeForLongTask(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$MVsb-uqonEtcS515Uj6jk51ABqs
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$onPictureTaken$5$CameraModel(data);
            }
        });
    }

    public /* synthetic */ void lambda$onPictureTaken$5$CameraModel(byte[] bArr) {
        String str = getFileDir() + System.currentTimeMillis() + CameraDefine.PIC_EXTENSION;
        boolean saveImage = FileUtils.saveImage(bArr, str);
        CameraLog.i(TAG, "onPictureTaken path:" + str, false);
        if (TextUtils.isEmpty(str) || !saveImage) {
            return;
        }
        scanFileAsync(str);
        ICameraModel.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onPictureTaken(str);
        }
    }

    @Override // android.media.MediaRecorder.OnErrorListener
    public void onError(MediaRecorder mr, int what, int extra) {
        stopRecord();
    }

    @Override // android.media.MediaRecorder.OnInfoListener
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == 800) {
            stopRecord();
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void startRecord() {
        startRecord(getFileDir() + System.currentTimeMillis() + CameraDefine.VIDEO_EXTENSION);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void startRecord(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
            videoPath = getFileDir() + System.currentTimeMillis() + CameraDefine.VIDEO_EXTENSION;
        }
        CameraLog.d(TAG, "startRecord videoPath:" + videoPath, false);
        handleStartRecord(videoPath);
    }

    public void startRecord(int maxDurationMs) {
        String str = getFileDir() + System.currentTimeMillis() + CameraDefine.VIDEO_EXTENSION;
        CameraLog.d(TAG, "startRecord videoPath:" + str, false);
        handleStartRecord(str, maxDurationMs);
    }

    public void startRecord(String videoPath, int maxDurationMs) {
        CameraLog.d(TAG, "startRecord videoPath:" + videoPath, false);
        handleStartRecord(videoPath, maxDurationMs);
    }

    protected void handleStartRecord(final String videoPath) {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$lNq2GrMqTKQrU6btmWBhvfFg4Mg
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$handleStartRecord$6$CameraModel(videoPath);
            }
        });
    }

    public /* synthetic */ void lambda$handleStartRecord$6$CameraModel(String str) {
        synchronized (sLock) {
            playStartRecordAudio();
            boolean z = false;
            if (CameraManager.getInstance().startRecord(str)) {
                MediaRecorder mediaRecorder = CameraManager.getInstance().getMediaRecorder();
                this.mMediaRecorder = mediaRecorder;
                mediaRecorder.setOnInfoListener(this);
                this.mMediaRecorder.setOnErrorListener(this);
                z = true;
            }
            ICameraModel.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onRecordStart(z, str);
            }
        }
    }

    protected void handleStartRecord(final String videoPath, final int maxDurationMs) {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$_jWg2RCtvDIBu-IQB0ikXRANW-4
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$handleStartRecord$7$CameraModel(videoPath, maxDurationMs);
            }
        });
    }

    public /* synthetic */ void lambda$handleStartRecord$7$CameraModel(String str, int i) {
        synchronized (sLock) {
            playStartRecordAudio();
            boolean z = false;
            if (CameraManager.getInstance().startRecord(str, i)) {
                MediaRecorder mediaRecorder = CameraManager.getInstance().getMediaRecorder();
                this.mMediaRecorder = mediaRecorder;
                mediaRecorder.setOnInfoListener(this);
                this.mMediaRecorder.setOnErrorListener(this);
                z = true;
            }
            ICameraModel.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onRecordStart(z, str);
            }
        }
    }

    private void handleStopRecord() {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$SEOlXDqCLamBDOW_Ae01gI-n5Js
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$handleStopRecord$8$CameraModel();
            }
        });
    }

    public /* synthetic */ void lambda$handleStopRecord$8$CameraModel() {
        synchronized (sLock) {
            MediaRecorder mediaRecorder = this.mMediaRecorder;
            if (mediaRecorder != null) {
                mediaRecorder.setOnInfoListener(null);
                this.mMediaRecorder.setOnErrorListener(null);
            }
            String stopRecording = CameraManager.getInstance().stopRecording();
            CameraLog.d(TAG, "handleStopRecord, videoPath is: " + stopRecording, false);
            if (stopRecording == null) {
                ICameraModel.Callback callback = this.mCallback;
                if (callback != null) {
                    callback.onRecordEnd(false, "");
                }
                return;
            }
            insertIntoMediaStore(stopRecording, true);
            ICameraModel.Callback callback2 = this.mCallback;
            if (callback2 != null) {
                callback2.onRecordEnd(true, stopRecording);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void stopRecord() {
        CameraLog.d(TAG, "stopRecord.", false);
        playEndRecordAudio();
        handleStopRecord();
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public Camera getCamera() {
        Camera camera;
        synchronized (sLock) {
            camera = this.mCamera;
        }
        return camera;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public boolean isCameraInitApi2() {
        return this.mCameraDevices != null;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void startDownCounter(final RecordTimeListener listener) {
        CameraLog.d(TAG, "start count down timer.");
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        final long maxRecordTime = getMaxRecordTime();
        CountDownTimer countDownTimer2 = new CountDownTimer(maxRecordTime, 500L) { // from class: com.xiaopeng.xmart.camera.model.camera.CameraModel.1
            @Override // android.os.CountDownTimer
            public void onTick(long millisUntilFinished) {
                if (listener != null) {
                    listener.onTick(CameraModel.this.getRecordTimeText(maxRecordTime, millisUntilFinished), ((int) ((maxRecordTime - millisUntilFinished) / 500)) % 2 == 0);
                }
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                CameraModel.this.stopRecord();
                RecordTimeListener recordTimeListener = listener;
                if (recordTimeListener != null) {
                    recordTimeListener.onFinish();
                }
            }
        };
        this.mTimer = countDownTimer2;
        countDownTimer2.start();
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void stopDownCounter() {
        CameraLog.d(TAG, "stop count down timer. timer:" + this.mTimer);
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    protected String getRecordTimeText(long totalTime, long millisUntilFinished) {
        int i = (int) ((totalTime - millisUntilFinished) / 1000);
        int i2 = i / 60;
        int i3 = i % 60;
        return (i2 < 10 ? new StringBuilder().append(ISpeechContants.SCREEN_ID_MAIN) : new StringBuilder().append("")).append(i2).toString() + ":" + (i3 < 10 ? new StringBuilder().append(ISpeechContants.SCREEN_ID_MAIN) : new StringBuilder().append("")).append(i3).toString();
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void gotoGalleryDetail(String filePath) {
        JumpGalleryUtil.gotoGalleryDetail(filePath, getGalleryTab());
    }

    protected void insertIntoMediaStore(String mediaPath, boolean isVideo) {
        MediaStoreUtils.insertMedia(App.getInstance(), mediaPath, isVideo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void scanFileAsync(String mediaPath) {
        MediaStoreUtils.scanFileAsync(App.getInstance(), mediaPath);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public String getLatestPicturePath() {
        String fileDir = getFileDir();
        String dirLastFileName = getDirLastFileName(fileDir);
        if ((FileUtils.DIR_360_FULL_PATH + File.separator).equals(fileDir)) {
            String dirLastFileName2 = getDirLastFileName(FileUtils.DIR_SHOCK_FULL_PATH + File.separator);
            if (TextUtils.isEmpty(dirLastFileName)) {
                return dirLastFileName2;
            }
            if (TextUtils.isEmpty(dirLastFileName2)) {
                return dirLastFileName;
            }
            if (dirLastFileName == null || dirLastFileName2 == null) {
                return null;
            }
            String[] split = dirLastFileName.split(File.separator);
            String[] split2 = dirLastFileName2.split(File.separator);
            return split[split.length + (-1)].compareTo(split2[split2.length + (-1)]) > 0 ? dirLastFileName : dirLastFileName2;
        }
        return dirLastFileName;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x008b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008c A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getDirLastFileName(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.String r0 = com.xiaopeng.xmart.camera.model.camera.CameraModel.TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getDirLastFileName fileDir:"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r7)
            java.lang.String r1 = r1.toString()
            r2 = 0
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r0, r1, r2)
            r0 = 0
            if (r7 == 0) goto L82
            java.io.File r1 = new java.io.File
            r1.<init>(r7)
            boolean r3 = r1.exists()
            if (r3 == 0) goto L82
            java.lang.String[] r1 = r1.list()
            if (r1 == 0) goto L82
            int r3 = r1.length
            if (r3 <= 0) goto L82
            java.util.Arrays.sort(r1)
            int r3 = r1.length
            int r3 = r3 + (-1)
            r4 = r0
        L37:
            if (r3 < 0) goto L83
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.StringBuilder r4 = r4.append(r7)
            r5 = r1[r3]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = com.xiaopeng.xmart.camera.utils.FileUtils.DIR_SHOCK_CACHE_FULL_PATH
            boolean r5 = android.text.TextUtils.equals(r5, r4)
            if (r5 == 0) goto L55
            goto L7f
        L55:
            boolean r5 = com.xiaopeng.xmart.camera.utils.FileUtils.invalidFile(r4)
            if (r5 != 0) goto L7f
            java.lang.String r7 = com.xiaopeng.xmart.camera.model.camera.CameraModel.TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "get last file from file list! lastFileName:"
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.StringBuilder r0 = r0.append(r4)
            java.lang.String r3 = ",list file size:"
            java.lang.StringBuilder r0 = r0.append(r3)
            int r1 = r1.length
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r7, r0, r2)
            return r4
        L7f:
            int r3 = r3 + (-1)
            goto L37
        L82:
            r4 = r0
        L83:
            java.lang.String r7 = com.xiaopeng.xmart.camera.utils.FileUtils.DIR_SHOCK_CACHE_FULL_PATH
            boolean r7 = r7.equals(r4)
            if (r7 == 0) goto L8c
            return r0
        L8c:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xmart.camera.model.camera.CameraModel.getDirLastFileName(java.lang.String):java.lang.String");
    }

    @Override // com.xiaopeng.xmart.camera.manager.CameraManager.Camera2DeviceCallback
    public void onOpened(CameraDevice camera) {
        this.mCameraOpened = true;
        CameraLog.i(TAG, "onOpend:" + this.mCameraOpened + ",preview start:" + this.mStartPreviedCalled, false);
        this.mCameraDevices = camera;
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$qQAP99kVtA_Cej__QLqAtPcMdgs
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$onOpened$9$CameraModel();
            }
        });
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$ftM2DoK8ar66pD8alFmZX0VKmLg
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$onOpened$10$CameraModel();
            }
        });
    }

    public /* synthetic */ void lambda$onOpened$9$CameraModel() {
        ICameraModel.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCameraOpen(true);
        }
    }

    public /* synthetic */ void lambda$onOpened$10$CameraModel() {
        if (this.mStartPreviedCalled) {
            return;
        }
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null && !surfaceTexture.isReleased()) {
            startPreview(this.mSurfaceTexture);
            return;
        }
        SurfaceHolder surfaceHolder = this.mSurfaceHolder;
        if (surfaceHolder == null || surfaceHolder.getSurface() == null || !this.mSurfaceHolder.getSurface().isValid()) {
            return;
        }
        startPreview(this.mSurfaceHolder);
    }

    @Override // com.xiaopeng.xmart.camera.manager.CameraManager.Camera2DeviceCallback
    public void onDisconnected(CameraDevice camera) {
        CameraLog.i(TAG, "onDisconnected", false);
        this.mCameraOpened = false;
        this.mStartPreviedCalled = false;
        CameraManager.getInstance().releaseCameraApi2();
        this.mCameraDevices = null;
    }

    @Override // com.xiaopeng.xmart.camera.manager.CameraManager.Camera2DeviceCallback
    public void onError(CameraDevice camera) {
        CameraLog.i(TAG, "onError", false);
        this.mCameraOpened = false;
        this.mStartPreviedCalled = false;
        CameraManager.getInstance().releaseCameraApi2();
        this.mCameraDevices = null;
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$31QPXoVgk6PKaVfsMrj5mrX2hGM
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$onError$11$CameraModel();
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$onError$11$CameraModel() {
        ICameraModel.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCameraPreview(false);
        }
        CameraLog.d(TAG, "onPreviewFailed", false);
    }

    @Override // com.xiaopeng.xmart.camera.manager.CameraManager.Camera2PreviewCallback
    public void onPreviewSuccess() {
        CameraLog.d(TAG, "onPreviewSuccess", false);
        this.mStartPreviedCalled = true;
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$mXy-saskVJgcUA47rLrf-cCTxwc
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$onPreviewSuccess$12$CameraModel();
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$onPreviewSuccess$12$CameraModel() {
        ICameraModel.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCameraPreview(true);
        }
    }

    @Override // com.xiaopeng.xmart.camera.manager.CameraManager.Camera2PreviewCallback
    public void onPreviewFailed() {
        this.mStartPreviedCalled = false;
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CameraModel$YYrGkmq3h7mkMIvXBj1dOorKAWQ
            @Override // java.lang.Runnable
            public final void run() {
                CameraModel.this.lambda$onPreviewFailed$13$CameraModel();
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$onPreviewFailed$13$CameraModel() {
        ICameraModel.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCameraPreview(false);
        }
        CameraLog.d(TAG, "onPreviewFailed", false);
    }

    public void playTakingPicAudio() {
        SoundPoolHelper.getInstance().playTakePicture();
    }

    public void playStartRecordAudio() {
        SoundPoolHelper.getInstance().playRecordStart();
    }

    public void playEndRecordAudio() {
        SoundPoolHelper.getInstance().playRecordEnd();
    }
}
