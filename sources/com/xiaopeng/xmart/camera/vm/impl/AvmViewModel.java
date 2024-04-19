package com.xiaopeng.xmart.camera.vm.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.model.camera.AvmModel;
import com.xiaopeng.xmart.camera.model.camera.IAvmCallBack;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
/* loaded from: classes.dex */
public class AvmViewModel extends BaseViewModel<AvmModel, IAvmCallBack> implements IAvmViewModel, IAvmController.Callback, IMcuController.Callback, IAvmCallBack {
    private static final String TAG = "AvmViewModel";
    protected IAvmController mIAvmController;
    protected IMcuController mIMcuController;
    protected int mIgStatus;
    private MutableLiveData<Integer> mAvmWorkSt = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIgOff = new MutableLiveData<>();
    protected MutableLiveData<Boolean> mLocalIgOn = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCamera360Change = new MutableLiveData<>();
    protected MutableLiveData<Boolean> mAvmSwitchFail = new MutableLiveData<>();

    public AvmViewModel() {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.vm.impl.-$$Lambda$AvmViewModel$XRHO_JdKuLilpRPn2HMAu0LdtB0
            @Override // java.lang.Runnable
            public final void run() {
                AvmViewModel.this.lambda$new$0$AvmViewModel();
            }
        });
    }

    public /* synthetic */ void lambda$new$0$AvmViewModel() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel
    public void initModel() {
        this.mICameraModel = new AvmModel(this);
    }

    public void initController() {
        CameraLog.i(TAG, "  initController ", false);
        CarClientWrapper carClientWrapper = CarClientWrapper.getInstance();
        this.mIAvmController = (IAvmController) carClientWrapper.getController(CarClientWrapper.XP_AVM_SERVICE);
        this.mIMcuController = (IMcuController) carClientWrapper.getController(CarClientWrapper.XP_MCU_SERVICE);
        this.mIAvmController.registerCallback(this);
        this.mIMcuController.registerCallback(this);
        CameraLog.d(TAG, "initController mIMcuController:" + this.mIMcuController, false);
        CameraLog.d(TAG, "initController mIAvmController:" + this.mIAvmController, false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void openCamera(int cameraId) {
        super.openCamera(cameraId);
        setShowPreViewCover(true);
        this.mCamera360Change.postValue(false);
        this.mAvmSwitchFail.postValue(false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onCameraOpen(boolean success) {
        super.onCameraOpen(success);
        this.mCamera360Change.postValue(false);
        if (success) {
            ((AvmModel) this.mICameraModel).onCameraOpen();
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void closeCamera() {
        super.closeCamera();
        this.mAvmSwitchFail.postValue(false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public boolean isCameraInitApi2() {
        if (this.mICameraModel == 0) {
            return false;
        }
        return ((AvmModel) this.mICameraModel).isCameraInitApi2();
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.IAvmCallBack
    public void onAvmSwitchFail() {
        this.mAvmSwitchFail.postValue(true);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.IAvmCallBack
    public void onCamera360Change() {
        this.mCamera360Change.postValue(true);
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.vm.impl.-$$Lambda$AvmViewModel$T-YKE61rJqFbWrManFzw1fWF1yQ
            @Override // java.lang.Runnable
            public final void run() {
                AvmViewModel.this.lambda$onCamera360Change$1$AvmViewModel();
            }
        }, 50L);
    }

    public /* synthetic */ void lambda$onCamera360Change$1$AvmViewModel() {
        switchCameraAnimationState(false);
    }

    @Override // com.xiaopeng.xmart.camera.vm.impl.BaseViewModel, com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void resetData() {
        CameraLog.i(TAG, "resetData", false);
        super.resetData();
        ((AvmModel) this.mICameraModel).resetData();
        CarCameraHelper.getInstance().getCarCamera().reset360CameraType();
        CarCameraHelper.getInstance().setIs3dMode(false);
    }

    private void switchCameraAnimationState(boolean show) {
        if (CarCameraHelper.getInstance().isPortraitScreen() && isPreviewOpen()) {
            setShowPreViewCover(show);
        }
    }

    public void setAvmTransparentChassisApi2(boolean isOpen) {
        ((AvmModel) this.mICameraModel).setAvmTransparentChassisWorkSt(isOpen);
    }

    public void setAVM3DAngleApi2(int angle) {
        ((AvmModel) this.mICameraModel).setAVM3DAngle(angle);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController.Callback
    public void onDisPlayModeChanged(int displayMode) {
        CameraLog.d(TAG, "onDisPlayModeChanged: " + displayMode, false);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController.Callback
    public void onAvmWorkStChanged(int avmWorkSt) {
        CameraLog.d(TAG, "onAvmWorkStChanged avmWorkSt:" + avmWorkSt, false);
        this.mAvmWorkSt.postValue(Integer.valueOf(avmWorkSt));
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController.Callback
    public void onIgStatusChanged(int igValue) {
        CameraLog.d(TAG, "onIgStatusChanged igValue:" + igValue + ",mIgStatus:" + this.mIgStatus, false);
        this.mIgStatus = igValue;
        this.mIgOff.postValue(Boolean.valueOf(igValue == 0));
        this.mLocalIgOn.postValue(Boolean.valueOf(igValue == 1));
    }

    public int getIgStatus() {
        return this.mIgStatus;
    }

    @Override // com.xiaopeng.xmart.camera.vm.IAvmViewModel
    public void switchCamera(int direction) {
        switchCameraAnimationState(true);
        this.mCamera360Change.postValue(false);
        ((AvmModel) this.mICameraModel).switchCamera(direction);
    }

    @Override // com.xiaopeng.xmart.camera.vm.IAvmViewModel
    public void changeToNormal() {
        switchCameraAnimationState(true);
        this.mCamera360Change.postValue(false);
        ((AvmModel) this.mICameraModel).changeToNormal();
    }

    @Override // com.xiaopeng.xmart.camera.vm.IAvmViewModel
    public void changeToTransparent() {
        switchCameraAnimationState(true);
        this.mCamera360Change.postValue(false);
        ((AvmModel) this.mICameraModel).changeToTransparent();
    }

    @Override // com.xiaopeng.xmart.camera.vm.IAvmViewModel
    public void changeSmallLargePreview() {
        switchCameraAnimationState(true);
        this.mCamera360Change.postValue(false);
        ((AvmModel) this.mICameraModel).changeSmallLargePreview();
    }

    @Override // com.xiaopeng.xmart.camera.vm.IAvmViewModel
    public boolean isAVMActived() {
        this.mCamera360Change.postValue(false);
        return this.mCarServiceControl.isAVMActived();
    }

    public LiveData<Boolean> getAvmSwitchFail() {
        return this.mAvmSwitchFail;
    }

    public LiveData<Boolean> getCamera360Change() {
        return this.mCamera360Change;
    }

    public LiveData<Integer> getAvmWorkSt() {
        return this.mAvmWorkSt;
    }

    public LiveData<Boolean> getLocalIgOn() {
        return this.mLocalIgOn;
    }

    public LiveData<Boolean> getIgOff() {
        return this.mIgOff;
    }
}
