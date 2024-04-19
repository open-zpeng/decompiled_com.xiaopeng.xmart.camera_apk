package com.xiaopeng.xmart.shock.vm;

import android.car.Car;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.model.camera.CarServiceControl;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.CarFunction;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xmart.shock.model.IShockModel;
import com.xiaopeng.xmart.shock.model.ShockModel;
import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Arrays;
import vendor.xpeng.imgproc.V1_0.IXpengImgProc;
import vendor.xpeng.imgproc.V1_0.IXpengImgProcCallback;
/* loaded from: classes2.dex */
public class ShockViewModel extends AvmViewModel implements ITboxController.Callback, IShockModel.Callback {
    private static final String TAG = "ShockViewModel";
    private volatile boolean mCarServiceConnected;
    private ConditionVariable mCarSrvConnectedSignal;
    private ITboxController mITboxController;
    private String mPicPath;
    private ShockModel mShockModel;
    protected MutableLiveData<Integer[]> mTBoxGuard = new MutableLiveData<>();
    protected MutableLiveData<Integer> mSoldierWorkSt = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsShockProcessing = new MutableLiveData<>();

    @Override // com.xiaopeng.xmart.camera.vm.impl.AvmViewModel, com.xiaopeng.xmart.camera.vm.impl.BaseViewModel
    public void initModel() {
        ShockModel shockModel = new ShockModel(this);
        this.mShockModel = shockModel;
        this.mICameraModel = shockModel;
        this.mCarSrvConnectedSignal = new ConditionVariable();
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.AvmViewModel
    public void initController() {
        super.initController();
        String str = TAG;
        CameraLog.i(str, "  initController ", false);
        ITboxController iTboxController = (ITboxController) CarClientWrapper.getInstance().getController(CarClientWrapper.XP_TBOX_SERVICE);
        this.mITboxController = iTboxController;
        iTboxController.registerCallback(this);
        this.mCarServiceConnected = true;
        this.mCarSrvConnectedSignal.open();
        CameraLog.d(str, "mITboxController:" + this.mITboxController, false);
    }

    public void waitForCarSrvConnected() {
        this.mCarSrvConnectedSignal.block(1000L);
        CarServiceControl.getInstance().getCarSrvConnectedSignal().block(1000L);
    }

    public boolean isShockProcessing() {
        MutableLiveData<Boolean> mutableLiveData = this.mIsShockProcessing;
        return (mutableLiveData == null || mutableLiveData.getValue() == null || !this.mIsShockProcessing.getValue().booleanValue()) ? false : true;
    }

    public boolean isCarServiceConnected() {
        return this.mCarServiceConnected;
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.AvmViewModel, com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onCameraOpen(boolean openSuccess) {
        CameraLog.d(TAG, "onCameraOpen openSuccess:" + openSuccess, false);
        this.mCameraOpen.postValue(Boolean.valueOf(openSuccess));
        this.mIsCameraOpen = openSuccess;
        if (openSuccess) {
            if (CarCameraHelper.getInstance().hasAVM()) {
                if (CameraDefine.isAVMSupport4Camera()) {
                    changeToFourCamera();
                    return;
                } else {
                    changeToTransparent();
                    return;
                }
            }
            return;
        }
        setShockProcess(false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onCameraPreview(boolean previewSuccess) {
        super.onCameraPreview(previewSuccess);
        if (previewSuccess) {
            return;
        }
        setShockProcess(false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onPictureTaken(String path) {
        super.onPictureTaken(path);
        this.mPicPath = path;
        this.mShockModel.createUploadCacheFile(path);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onRecordStart(boolean recordStart, String videoPath) {
        super.onRecordStart(recordStart, videoPath);
        if (recordStart) {
            return;
        }
        setShockProcess(false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onRecordEnd(boolean recordEnd, String videoPath) {
        super.onRecordEnd(recordEnd, videoPath);
        if (recordEnd) {
            if (!this.mShockModel.supportFileUpload()) {
                LogUtils.i(TAG, "onRecordEnd: not support file upload!");
                this.mShockModel.lambda$uploadShockAlarmMsg$2$ShockModel();
                return;
            } else if (!isSupportVideoMasking()) {
                LogUtils.i(TAG, "onRecordEnd: not support video masking!");
                return;
            } else {
                getFirstFrameFromVideo(videoPath);
                final String maskVideoPath = getMaskVideoPath(videoPath);
                LogUtils.d(TAG, "maskVideoPath: " + maskVideoPath);
                try {
                    IXpengImgProc.getService(true).doMosaicNonBlock(videoPath, maskVideoPath, new IXpengImgProcCallback.Stub() { // from class: com.xiaopeng.xmart.shock.vm.ShockViewModel.1
                        @Override // vendor.xpeng.imgproc.V1_0.IXpengImgProcCallback
                        public void notifyCallback(int msgType, String msg, int process) throws RemoteException {
                            LogUtils.d(ShockViewModel.TAG, "msgType: " + msgType + ", msg: " + msg + ", process: " + process);
                            if (2 != msgType) {
                                if (1 == msgType) {
                                    LogUtils.e(ShockViewModel.TAG, "onRecordEnd Error: " + msg);
                                    return;
                                }
                                return;
                            }
                            LogUtils.i(ShockViewModel.TAG, "onRecordEnd Masked");
                            ShockViewModel shockViewModel = ShockViewModel.this;
                            shockViewModel.mPicPath = shockViewModel.getFirstFrameFromVideo(maskVideoPath);
                            ShockViewModel.this.mShockModel.createUploadCacheFile(ShockViewModel.this.mPicPath);
                            ShockViewModel.this.mShockModel.createUploadCacheFile(maskVideoPath);
                            if (ShockViewModel.this.mShockModel.supportFileUpload()) {
                                ShockViewModel.this.mShockModel.uploadShockMedia(ShockViewModel.this.mPicPath, maskVideoPath);
                            }
                        }
                    });
                    return;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        setShockProcess(false);
    }

    public static boolean isSupportVideoMasking() {
        String xpCduType = Car.getXpCduType();
        xpCduType.hashCode();
        char c = 65535;
        switch (xpCduType.hashCode()) {
            case 2562:
                if (xpCduType.equals("Q3")) {
                    c = 0;
                    break;
                }
                break;
            case 2566:
                if (xpCduType.equals("Q7")) {
                    c = 1;
                    break;
                }
                break;
            case 2567:
                if (xpCduType.equals("Q8")) {
                    c = 2;
                    break;
                }
                break;
            case 2568:
                if (xpCduType.equals(CarFunction.F30)) {
                    c = 3;
                    break;
                }
                break;
            case 2577:
                if (xpCduType.equals("QB")) {
                    c = 4;
                    break;
                }
                break;
            case 79487:
                if (xpCduType.equals("Q3A")) {
                    c = 5;
                    break;
                }
                break;
            case 79611:
                if (xpCduType.equals("Q7A")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFirstFrameFromVideo(String videoPath) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath);
        Bitmap frameAtIndex = mediaMetadataRetriever.getFrameAtIndex(0);
        String shockPicPath = getShockPicPath(videoPath);
        LogUtils.i(TAG, "getFirstFrameFromVideo picPath: " + shockPicPath);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(shockPicPath, new String[0]), new OpenOption[0]));
            frameAtIndex.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            LogUtils.e(TAG, "getFirstFrameFromVideo: " + e.getMessage());
        }
        return shockPicPath;
    }

    private String getMaskVideoPath(String videoPath) {
        if (videoPath == null || !videoPath.endsWith(CameraDefine.VIDEO_EXTENSION)) {
            return null;
        }
        return videoPath.substring(0, videoPath.lastIndexOf(".")) + "-mask" + CameraDefine.VIDEO_EXTENSION;
    }

    private String getShockPicPath(String videoPath) {
        if (videoPath == null || !videoPath.endsWith(CameraDefine.VIDEO_EXTENSION)) {
            return null;
        }
        return videoPath.substring(0, videoPath.lastIndexOf(".")) + CameraDefine.PIC_EXTENSION;
    }

    public void changeToFourCamera() {
        this.mShockModel.changeToFourCamera();
    }

    public void shockAvmDisplayChange() {
        this.mShockModel.shockAvmDisplayChange();
    }

    public void enterShockProcess() {
        setShockProcess(true);
        if (this.mCarServiceControl.isInvalidShockGSensorData()) {
            setShockProcess(false);
            CameraLog.d(TAG, "enterShockProcess isInvalidShockGSensorData return ", false);
            return;
        }
        int soldierGsensorData = this.mCarServiceControl.getSoldierGsensorData();
        this.mShockModel.setGsensor(soldierGsensorData);
        CameraLog.d(TAG, "enterShockProcess gSensor is: " + soldierGsensorData, false);
        excuteSentryMode();
        this.mCarServiceControl.sendSoldierTickMsg();
    }

    private void excuteSentryMode() {
        CameraLog.d(TAG, "excuteSentryMode", false);
        ThreadPoolHelper.getInstance().executeForLongTask(new Runnable() { // from class: com.xiaopeng.xmart.shock.vm.-$$Lambda$ShockViewModel$f8Rali6xv0CMMMutuLDlwhQeU0I
            @Override // java.lang.Runnable
            public final void run() {
                ShockViewModel.this.lambda$excuteSentryMode$0$ShockViewModel();
            }
        });
    }

    public /* synthetic */ void lambda$excuteSentryMode$0$ShockViewModel() {
        if ((CarCameraHelper.getInstance().hasAVM() && this.mCarServiceControl.isAVMWorkOnSt()) || !CarCameraHelper.getInstance().hasAVM()) {
            CameraLog.d(TAG, "avmWorkSt == CarAvmManager.AVM_STATUS_ON, and open camera", false);
            openCamera();
            return;
        }
        CameraLog.d(TAG, "avm is not active", false);
    }

    public void openCamera() {
        CameraLog.d(TAG, "openCamera", false);
        if ("au8295_xp".equals(Build.DEVICE)) {
            openCamera(0);
        } else {
            openCamera(2);
        }
    }

    private void setShockProcess(boolean process) {
        CameraLog.d(TAG, "setShockProcess process:" + process, false);
        this.mShockModel.setShockProcess(process);
        this.mCarServiceControl.setShockProcessing(process);
        this.mIsShockProcessing.postValue(Boolean.valueOf(process));
        if (process) {
            return;
        }
        resetData();
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.AvmViewModel, com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController.Callback
    public void onIgStatusChanged(int igValue) {
        super.onIgStatusChanged(igValue);
        if (isShockProcessing() && igValue == 1) {
            CameraLog.d(TAG, "onIgStatusChanged isShockProcessing ", false);
            setShockProcess(false);
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController.Callback
    public void onGuardStatusChanged(Integer[] state) {
        CameraLog.d(TAG, "onGuardStatusChanged state:" + (state != null ? Arrays.toString(state) : ""), false);
        this.mTBoxGuard.postValue(state);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController.Callback
    public void onSoldierWorkStatusChanged(int state) {
        CameraLog.d(TAG, "onSoldierWorkStatusChanged " + state, false);
    }

    public LiveData<Boolean> getShockProcessing() {
        return this.mIsShockProcessing;
    }

    public LiveData<Integer[]> getTBoxGuard() {
        return this.mTBoxGuard;
    }

    public MutableLiveData<Integer> getSoldierWorkSt() {
        return this.mSoldierWorkSt;
    }

    public void resetShockModel() {
        ShockModel shockModel = this.mShockModel;
        if (shockModel != null) {
            shockModel.resetRetryCount();
        }
    }

    @Override // com.xiaopeng.xmart.shock.model.IShockModel.Callback
    public void onShockEnd() {
        setShockProcess(false);
    }

    @Override // com.xiaopeng.xmart.shock.model.IShockModel.Callback
    public void onCarControlFeedBack(String cmd) {
        if (TextUtils.isEmpty(cmd)) {
            return;
        }
        this.mCarServiceControl.feedbackCameraStatus(cmd);
    }
}
