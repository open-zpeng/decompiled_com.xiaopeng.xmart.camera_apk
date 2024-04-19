package com.xiaopeng.xmart.shock.model;

import android.content.Intent;
import android.hardware.Camera;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.xiaopeng.drivingimageassist.utils.Constant;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.IRemoteStorage;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.StorageException;
import com.xiaopeng.lib.utils.NetUtils;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.lib.utils.config.CommonConfig;
import com.xiaopeng.lib.utils.config.RemoteControlConfig;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.bean.CameraFeedback;
import com.xiaopeng.xmart.camera.bean.CarCamera;
import com.xiaopeng.xmart.camera.bean.FeedbackReq;
import com.xiaopeng.xmart.camera.carmanager.Component;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.SharedPreferenceHelper;
import com.xiaopeng.xmart.camera.manager.CameraManager;
import com.xiaopeng.xmart.camera.model.camera.AvmModel;
import com.xiaopeng.xmart.camera.model.camera.ICameraModel;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.FileUtils;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.shock.bean.ShockInfo;
import com.xiaopeng.xmart.shock.model.IShockModel;
import com.xiaopeng.xmart.shock.model.ShockModel;
import com.xiaopeng.xmart.shock.utils.DateUtils;
import com.xiaopeng.xmart.shock.vm.ShockViewModel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
/* loaded from: classes2.dex */
public class ShockModel extends AvmModel implements IShockModel {
    private static final String BUCKET_NAME = "bjmarket";
    private static final String EVENT_NAME = "ABNORMAL_VIBRATION_V2";
    private static final int FEEDBACK_FILE_NOT_EXIST = -1;
    private static final int FEEDBACK_FILE_UPLOADED = 2;
    private static final int FEEDBACK_FILE_UPLOADING = 1;
    private static final int FEEDBACK_FILE_UPLOAD_FAILED = 3;
    private static final String OSS_OBJECT_FORMAT = "vehicle/%1s/%2s";
    private static final int RETRY_INTERVAL = 10000;
    private static final int RETRY_MAX_COUNT = 3;
    private static final String SHOCK_EVENT_NAME_V3 = "ABNORMAL_VIBRATION_V3";
    private static final int SHOCK_VIDEO_DURATION = 15000;
    private static final String TAG = "com.xiaopeng.xmart.shock.model.ShockModel";
    private static final int UPLOAD_MAX_COUNT = 3;
    public static final String UPLOAD_SHOCK_NUMBER = "shockNumber";
    public static final String UPLOAD_SHOCK_TIME = "shockTime";
    private Gson mGson;
    private IHttp mHttp;
    private boolean mIsShockProcessing;
    private volatile boolean mIsUploadingVideo;
    private IRemoteStorage mRemoteStorage;
    private int mRetryCount;
    private ShockInfo mShockInfo;
    private long mShockTimeStamp;
    private final Callback mUploadPicCallback;
    private final Callback mUploadVideoCallback;
    private String mVinInfo;

    public String getFileExtension() {
        return FileUtils.SENTRY_MODE_COLLISION_SUFFIX;
    }

    static /* synthetic */ int access$208(ShockModel shockModel) {
        int i = shockModel.mRetryCount;
        shockModel.mRetryCount = i + 1;
        return i;
    }

    public ShockModel(ICameraModel.Callback callback) {
        super(callback);
        this.mRetryCount = 0;
        this.mGson = new Gson();
        this.mShockTimeStamp = System.currentTimeMillis();
        this.mIsShockProcessing = false;
        this.mIsUploadingVideo = false;
        this.mUploadPicCallback = new AnonymousClass1();
        this.mUploadVideoCallback = new AnonymousClass2();
        CameraLog.d(TAG, "ShockModel", false);
        init();
    }

    private void init() {
        this.mShockInfo = new ShockInfo();
        this.mVinInfo = SystemProperties.get("sys.xiaopeng.vin", "");
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.AvmModel, com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public String getFileDir() {
        String str = FileUtils.DIR_SHOCK_FULL_PATH;
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str + File.separator;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.AvmModel, com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public int getBucketId() {
        return FileUtils.BUCKET_ID_SHOCK;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void takePicture() {
        CameraLog.i(TAG, "takePicture", false);
        CameraManager.getInstance().takePicture(this);
        this.mShockTimeStamp = System.currentTimeMillis();
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, android.hardware.Camera.PictureCallback
    public void onPictureTaken(final byte[] data, Camera camera) {
        ThreadPoolHelper.getInstance().executeForLongTask(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$tQNy646fUslaugt0nYk3UinImHU
            @Override // java.lang.Runnable
            public final void run() {
                ShockModel.this.lambda$onPictureTaken$0$ShockModel(data);
            }
        });
    }

    public /* synthetic */ void lambda$onPictureTaken$0$ShockModel(byte[] bArr) {
        String str = getFileDir() + System.currentTimeMillis() + getFileExtension() + CameraDefine.PIC_EXTENSION;
        boolean saveImage = FileUtils.saveImage(bArr, str);
        String str2 = TAG;
        CameraLog.i(str2, "onPictureTaken path:" + str, false);
        if (TextUtils.isEmpty(str) || !saveImage) {
            return;
        }
        scanFileAsync(str);
        CameraLog.i(str2, "onPictureTaken:" + str, false);
        if (this.mCallback != null) {
            this.mCallback.onPictureTaken(str);
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void startRecord() {
        super.startRecord(getFileDir() + System.currentTimeMillis() + getFileExtension() + CameraDefine.VIDEO_EXTENSION, SHOCK_VIDEO_DURATION);
        this.mShockTimeStamp = System.currentTimeMillis();
    }

    @Override // com.xiaopeng.xmart.shock.model.IShockModel
    public void setGsensor(int gSensor) {
        this.mShockInfo.gSensor = gSensor;
    }

    @Override // com.xiaopeng.xmart.shock.model.IShockModel
    public void uploadShockMedia(String picPath, String videoPath) {
        String str = TAG;
        CameraLog.i(str, "uploadShockMedia picPath:" + picPath + ",videoPath:" + videoPath, false);
        if (this.mVinInfo == null) {
            CameraLog.i(str, "uploadShockMedia mVinInfo is null ", false);
            onShockEnd();
        } else if (picPath == null || videoPath == null) {
        } else {
            if (picPath.startsWith(File.separator)) {
                this.mShockInfo.picPath = picPath.substring(1);
            } else {
                this.mShockInfo.picPath = picPath;
            }
            if (videoPath.startsWith(File.separator)) {
                this.mShockInfo.videoPath = videoPath.substring(1);
            } else {
                this.mShockInfo.videoPath = videoPath;
            }
            CameraLog.i(str, "uploadShockMedia picPath: " + this.mShockInfo.picPath + ",  videoPath: " + this.mShockInfo.videoPath, false);
            int curDayUpLoadNumber = getCurDayUpLoadNumber();
            if (supportFileUpload() && curDayUpLoadNumber < 3) {
                lambda$uploadFile$1$ShockModel(this.mShockInfo.picPath, this.mUploadPicCallback);
            } else {
                onShockEnd();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.shock.model.ShockModel$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Callback {
        AnonymousClass1() {
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback
        public void onStart(String remoteUrl, String localFilePath) {
            CameraLog.d(ShockModel.TAG, "oss upload pic start... " + remoteUrl, false);
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback
        public void onSuccess(String remoteUrl, String localFilePath) {
            CameraLog.d(ShockModel.TAG, "oss upload pic success to " + remoteUrl, false);
            ShockModel.this.deleteUploadCacheFile(localFilePath);
            FileUtils.deleteFile(localFilePath);
            if (localFilePath != null) {
                ShockModel.this.mRetryCount = 0;
                if (localFilePath.equals(ShockModel.this.mShockInfo.picPath)) {
                    ShockModel.this.mShockInfo.picUrl = remoteUrl;
                    ShockModel.this.mShockInfo.videoUrl = ShockModel.this.mShockInfo.picUrl.replace(ShockModel.this.mShockInfo.picPath, ShockModel.this.mShockInfo.videoPath);
                    ShockModel.this.controlUploadShockVideo();
                }
            }
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback
        public void onFailure(String remoteUrl, String localFilePath, StorageException storageException) {
            ShockModel.access$208(ShockModel.this);
            CameraLog.d(ShockModel.TAG, "oss upload pic failed... " + storageException.getMessage(), false);
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$1$eUeKtj9oPtZXHTc-z5_ZTCl1PoY
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.AnonymousClass1.this.lambda$onFailure$0$ShockModel$1();
                }
            }, Constant.Time.NAPALOADED_CHECK_DELAYMILLIS);
        }

        public /* synthetic */ void lambda$onFailure$0$ShockModel$1() {
            ShockModel shockModel = ShockModel.this;
            shockModel.lambda$uploadFile$1$ShockModel(shockModel.mShockInfo.picPath, ShockModel.this.mUploadPicCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.shock.model.ShockModel$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Callback {
        AnonymousClass2() {
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback
        public void onStart(String remoteUrl, String localFilePath) {
            CameraLog.d(ShockModel.TAG, "oss upload video start... " + remoteUrl, false);
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback
        public void onSuccess(String remoteUrl, String localFilePath) {
            CameraLog.d(ShockModel.TAG, "oss upload video success to " + remoteUrl, false);
            ShockModel.this.mIsUploadingVideo = false;
            ShockModel.this.deleteUploadCacheFile(localFilePath);
            FileUtils.deleteFile(localFilePath);
            ShockModel.this.setUploadNumber();
            if (localFilePath != null) {
                ShockModel.this.mRetryCount = 0;
                if (localFilePath.equals(ShockModel.this.mShockInfo.videoPath)) {
                    ShockModel.this.lambda$uploadShockAlarmMsg$2$ShockModel();
                    ShockModel.this.controlFeedBack(2);
                }
            }
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback
        public void onFailure(String remoteUrl, String localFilePath, StorageException storageException) {
            ShockModel.access$208(ShockModel.this);
            CameraLog.d(ShockModel.TAG, "oss upload video failed... " + storageException.getMessage(), false);
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$2$LZ3kmyOPn_vetZpY1W3zmLzItbI
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.AnonymousClass2.this.lambda$onFailure$0$ShockModel$2();
                }
            }, Constant.Time.NAPALOADED_CHECK_DELAYMILLIS);
        }

        public /* synthetic */ void lambda$onFailure$0$ShockModel$2() {
            ShockModel shockModel = ShockModel.this;
            shockModel.lambda$uploadFile$1$ShockModel(shockModel.mShockInfo.videoPath, ShockModel.this.mUploadVideoCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void controlUploadShockVideo() {
        if (TextUtils.isEmpty(this.mVinInfo)) {
            CameraLog.d(TAG, "Current car do not have VIN code", false);
            controlFeedBack(3);
            onShockEnd();
        } else if (this.mIsUploadingVideo) {
            controlFeedBack(1);
        } else {
            this.mIsUploadingVideo = true;
            if (this.mShockInfo.videoPath == null || !new File(this.mShockInfo.videoPath).exists()) {
                CameraLog.d(TAG, "upload video not exist", false);
                controlFeedBack(-1);
                onShockEnd();
                return;
            }
            lambda$uploadFile$1$ShockModel(this.mShockInfo.videoPath, this.mUploadVideoCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: uploadFile */
    public void lambda$uploadFile$1$ShockModel(final String uploadFilePath, final Callback callback) {
        if (this.mRetryCount >= 3) {
            CameraLog.d(TAG, "Max retried for uploading file to oss, return!", false);
            FileUtils.deleteFile(uploadFilePath);
            if (uploadFilePath.endsWith(CameraDefine.VIDEO_EXTENSION)) {
                controlFeedBack(3);
            }
            onShockEnd();
        } else if (!NetUtils.isNetworkAvailable(App.getInstance())) {
            CameraLog.d(TAG, "Network not available while uploading file to oss, post delayed and return!", false);
            this.mRetryCount++;
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$BEj4v8PAYcL44BEMuzjlGqw3ch4
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.this.lambda$uploadFile$1$ShockModel(uploadFilePath, callback);
                }
            }, Constant.Time.NAPALOADED_CHECK_DELAYMILLIS);
        } else {
            String format = String.format(OSS_OBJECT_FORMAT, this.mVinInfo, uploadFilePath);
            CameraLog.d(TAG, "upload oss object key: " + format, false);
            try {
                if (this.mRemoteStorage == null) {
                    this.mRemoteStorage = Component.getInstance().getRemoteStorage();
                }
                this.mRemoteStorage.uploadWithPathAndCallback(BUCKET_NAME, format, uploadFilePath, callback);
            } catch (Exception e) {
                e.printStackTrace();
                CameraLog.e(TAG, "oss upload exception " + e.getMessage(), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.shock.model.IShockModel
    public void createUploadCacheFile(String filePath) {
        String cacheFileName = getCacheFileName(filePath);
        CameraLog.d(TAG, "createUploadCacheFile: " + filePath + ",cacheName:" + cacheFileName, false);
        File file = new File(FileUtils.DIR_SHOCK_CACHE_FULL_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(FileUtils.DIR_SHOCK_CACHE_FULL_PATH + File.separator + cacheFileName);
        if (file2.exists()) {
            return;
        }
        try {
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            CameraLog.d(TAG, "createNewFile: " + e.getMessage(), false);
        }
    }

    @Override // com.xiaopeng.xmart.shock.model.IShockModel
    public void setShockProcess(boolean process) {
        CameraLog.d(TAG, "setShockProcess process:" + process, false);
        this.mIsShockProcessing = process;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onShockEnd() {
        setShockProcess(false);
        ((IShockModel.Callback) this.mCallback).onShockEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteUploadCacheFile(String filePath) {
        String cacheFileName = getCacheFileName(filePath);
        CameraLog.d(TAG, "deleteUploadCacheFile: " + filePath + ",cacheName:" + cacheFileName, false);
        File file = new File(FileUtils.DIR_SHOCK_CACHE_FULL_PATH + File.separator + cacheFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUploadNumber() {
        String formatDate = DateUtils.formatDate(System.currentTimeMillis());
        String string = SharedPreferenceHelper.getInstance().getString(UPLOAD_SHOCK_TIME, "");
        int i = SharedPreferenceHelper.getInstance().getInt(UPLOAD_SHOCK_NUMBER, 0);
        CameraLog.d(TAG, "setUploadNumber curDay:" + formatDate + ",spUploadNumber:" + i, false);
        if (TextUtils.equals(formatDate, string)) {
            SharedPreferenceHelper.getInstance().putInt(UPLOAD_SHOCK_NUMBER, i + 1);
            return;
        }
        SharedPreferenceHelper.getInstance().putString(UPLOAD_SHOCK_TIME, formatDate);
        SharedPreferenceHelper.getInstance().putInt(UPLOAD_SHOCK_NUMBER, 0);
    }

    private int getCurDayUpLoadNumber() {
        String str = TAG;
        CameraLog.d(str, "getCurDayUpLoadNumber ", false);
        String formatDate = DateUtils.formatDate(System.currentTimeMillis());
        String string = SharedPreferenceHelper.getInstance().getString(UPLOAD_SHOCK_TIME, "");
        int i = SharedPreferenceHelper.getInstance().getInt(UPLOAD_SHOCK_NUMBER, 0);
        CameraLog.d(str, "getCurDayUpLoadNumber spDay:" + string + ",spUploadNumber:" + i, false);
        if (TextUtils.equals(formatDate, string)) {
            return i;
        }
        SharedPreferenceHelper.getInstance().putString(UPLOAD_SHOCK_TIME, formatDate);
        SharedPreferenceHelper.getInstance().putInt(UPLOAD_SHOCK_NUMBER, 0);
        CameraLog.d(str, "getCurDayUpLoadNumber spDay:" + formatDate + ",spUploadNumber:0", false);
        return 0;
    }

    private String getCacheFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    /* renamed from: uploadShockAlarmMsg */
    public void lambda$uploadShockAlarmMsg$2$ShockModel() {
        if (this.mRetryCount >= 3) {
            CameraLog.d(TAG, "Max retried for uploading shock msg, return!", false);
            onShockEnd();
        } else if (!NetUtils.isNetworkAvailable(App.getInstance())) {
            CameraLog.d(TAG, "Network not available while uploading shock msg, post delayed and return!", false);
            this.mRetryCount++;
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$Mla5fLF9BWTj_Mao97icU1phaAI
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.this.lambda$uploadShockAlarmMsg$2$ShockModel();
                }
            }, Constant.Time.NAPALOADED_CHECK_DELAYMILLIS);
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("ts", String.valueOf(this.mShockTimeStamp));
            hashMap.put("event", ShockViewModel.isSupportVideoMasking() ? SHOCK_EVENT_NAME_V3 : EVENT_NAME);
            hashMap.put(IpcConfig.DeviceCommunicationConfig.KEY_APP_MESSAGE_PARAMS, this.mGson.toJson(this.mShockInfo));
            String str = TAG;
            CameraLog.d(str, "mShockInfo gson to String: " + this.mGson.toJson(this.mShockInfo), false);
            CameraLog.d(str, "upload msg, url : " + Config.HTTP_URL_UPLOAD, false);
            if (this.mHttp == null) {
                this.mHttp = Component.getInstance().getHttp();
            }
            this.mHttp.bizHelper().post(Config.HTTP_URL_UPLOAD, this.mGson.toJson(hashMap)).appId("xp_car_camera_biz").buildWithSecretKey(getAppSecret()).execute(new AnonymousClass3());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.shock.model.ShockModel$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback {
        AnonymousClass3() {
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
        public void onSuccess(IResponse response) {
            ShockModel.this.mRetryCount = 0;
            CameraLog.d(ShockModel.TAG, "uploadShockAlarmMsg response success:" + response.body() + " code:" + response.code() + " exception:" + response.getException(), false);
            CameraLog.d(ShockModel.TAG, "Delay 5 seconds and exit shock process");
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$3$EubzRYAWeJBIrqPwgQszAcIF6sI
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.AnonymousClass3.this.lambda$onSuccess$0$ShockModel$3();
                }
            }, 5000L);
        }

        public /* synthetic */ void lambda$onSuccess$0$ShockModel$3() {
            ShockModel.this.onShockEnd();
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
        public void onFailure(IResponse response) {
            CameraLog.e(ShockModel.TAG, "uploadShockAlarmMsg response error:" + response.body(), false);
            ShockModel.access$208(ShockModel.this);
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$3$9LOKlsJ9ndGR3Jw_SqhMeFuPvBY
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.AnonymousClass3.this.lambda$onFailure$1$ShockModel$3();
                }
            }, Constant.Time.NAPALOADED_CHECK_DELAYMILLIS);
        }

        public /* synthetic */ void lambda$onFailure$1$ShockModel$3() {
            ShockModel.this.lambda$uploadShockAlarmMsg$2$ShockModel();
        }
    }

    private String getAppSecret() {
        return CommonConfig.HTTP_HOST.startsWith("https://xmart.xiaopeng.com") ? Config.APP_SECRET_RELEASE : Config.APP_SECRET;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void controlFeedBack(final int fileStatus) {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$AFsWYF_Q5bOxHAJibf-M33HxhaQ
            @Override // java.lang.Runnable
            public final void run() {
                ShockModel.this.lambda$controlFeedBack$3$ShockModel(fileStatus);
            }
        });
    }

    public /* synthetic */ void lambda$controlFeedBack$3$ShockModel(int i) {
        CarCamera carCamera = CarCameraHelper.getInstance().getCarCamera();
        FeedbackReq feedbackReq = new FeedbackReq();
        StringBuilder sb = new StringBuilder();
        sb.append(20).append(SystemPropertyUtil.getVehicleId()).append(System.currentTimeMillis()).append((int) (Math.random() * 10.0d)).append((int) (Math.random() * 10.0d));
        feedbackReq.setMsg_id(sb.toString());
        feedbackReq.setMsg_type(1);
        feedbackReq.setService_type(11);
        CameraFeedback cameraFeedback = new CameraFeedback();
        cameraFeedback.setCameraTop(carCamera.getCameraTop());
        cameraFeedback.setCamera360(carCamera.getCamera360());
        cameraFeedback.setCamera_degree(carCamera.getAngle());
        cameraFeedback.setFileStatus(i);
        feedbackReq.setMsg_content(cameraFeedback);
        String json = new Gson().toJson(feedbackReq);
        Intent intent = new Intent();
        intent.setAction(RemoteControlConfig.ACTION_XMART_FEEDBACK);
        intent.putExtra(RemoteControlConfig.ACTION_XMART_FEEDBACK_VALUE, json);
        App.getInstance().sendBroadcast(intent);
        CameraLog.d(TAG, "notifyClient end with msg-->" + json, false);
    }

    public void shockAvmDisplayChange() {
        if (CarCameraHelper.getInstance().hasAVM()) {
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$57Clu98EEfYe5dU1nZjVqlQui_o
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.this.lambda$shockAvmDisplayChange$4$ShockModel();
                }
            }, 3000L);
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$q8MS4ctYT9kPow0WrHT87iZBEEU
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.this.lambda$shockAvmDisplayChange$5$ShockModel();
                }
            }, 6000L);
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$N6ANNh4gOhQp3C7GgvcdcdxKBEA
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.this.lambda$shockAvmDisplayChange$6$ShockModel();
                }
            }, 9000L);
            ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.shock.model.-$$Lambda$ShockModel$QRcT7iqdXS-nWs4Ecd2SkFyHnK4
                @Override // java.lang.Runnable
                public final void run() {
                    ShockModel.this.lambda$shockAvmDisplayChange$7$ShockModel();
                }
            }, 12000L);
        }
    }

    public /* synthetic */ void lambda$shockAvmDisplayChange$4$ShockModel() {
        if (this.mIsShockProcessing) {
            switchCamera(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT);
        }
    }

    public /* synthetic */ void lambda$shockAvmDisplayChange$5$ShockModel() {
        if (this.mIsShockProcessing) {
            switchCamera(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR);
        }
    }

    public /* synthetic */ void lambda$shockAvmDisplayChange$6$ShockModel() {
        if (this.mIsShockProcessing) {
            switchCamera(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT);
        }
    }

    public /* synthetic */ void lambda$shockAvmDisplayChange$7$ShockModel() {
        if (this.mIsShockProcessing) {
            switchCamera(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT);
        }
    }

    public boolean supportFileUpload() {
        return ShockViewModel.isSupportVideoMasking();
    }

    public void resetRetryCount() {
        this.mRetryCount = 0;
    }
}
