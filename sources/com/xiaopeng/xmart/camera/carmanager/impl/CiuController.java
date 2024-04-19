package com.xiaopeng.xmart.camera.carmanager.impl;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.ciu.CarCiuManager;
import com.xiaopeng.xmart.camera.carmanager.BaseCarController;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class CiuController extends BaseCarController<CarCiuManager, ICiuController.Callback> implements ICiuController {
    private static final String TAG = "CiuController";
    private final CarCiuManager.CarCiuEventCallback mCarVcuEventCallback = new CarCiuManager.CarCiuEventCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.CiuController.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CameraLog.d(CiuController.TAG, "onChangeEvent: " + carPropertyValue);
            CiuController.this.handleCarEventsUpdate(carPropertyValue);
        }

        public void onErrorEvent(int propertyId, int zone) {
            CameraLog.e(CiuController.TAG, "onErrorEvent: " + propertyId, false);
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void initCarManager(Car carClient) {
        CameraLog.d(TAG, "Init start");
        try {
            this.mCarManager = (CarCiuManager) carClient.getCarManager(CarClientWrapper.XP_CIU_SERVICE);
            if (this.mCarManager != 0) {
                this.mCarManager.registerPropCallback(this.mPropertyIds, this.mCarVcuEventCallback);
            }
        } catch (CarNotConnectedException e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
        CameraLog.d(TAG, "Init end");
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected List<Integer> getRegisterPropertyIds() {
        ArrayList arrayList = new ArrayList();
        if (CarCameraHelper.getInstance().isSupportDvr()) {
            arrayList.add(557852701);
            arrayList.add(557852686);
            arrayList.add(557852696);
            arrayList.add(557852675);
            arrayList.add(557852689);
            arrayList.add(557852694);
            arrayList.add(557852693);
            arrayList.add(557852673);
            arrayList.add(557852703);
            arrayList.add(557852711);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void disconnect() {
        if (this.mCarManager != 0) {
            try {
                this.mCarManager.unregisterPropCallback(this.mPropertyIds, this.mCarVcuEventCallback);
            } catch (CarNotConnectedException e) {
                CameraLog.e(TAG, e.getMessage(), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected void handleEventsUpdate(CarPropertyValue<?> value) {
        CameraLog.i(TAG, "handleEventsUpdate: " + value.getPropertyId() + ",value:" + value.getValue(), false);
        switch (value.getPropertyId()) {
            case 557852673:
                handleDvrModeState(((Integer) getValue(value)).intValue());
                return;
            case 557852675:
                handleSdState(((Integer) getValue(value)).intValue());
                return;
            case 557852686:
                handleDvrLockFbState(((Integer) getValue(value)).intValue());
                return;
            case 557852689:
                handleVideoOutputState(((Integer) getValue(value)).intValue());
                return;
            case 557852693:
                handlePhotoProcessState(((Integer) getValue(value)).intValue());
                return;
            case 557852694:
                handleDvrLockState(((Integer) getValue(value)).intValue());
                return;
            case 557852696:
                handleDvrStatus(((Integer) getValue(value)).intValue());
                return;
            case 557852701:
                handleFormatSdState(((Integer) getValue(value)).intValue());
                return;
            case 557852703:
                handleAutoLockStState(((Integer) getValue(value)).intValue());
                return;
            case 557852711:
                handleDvrSwitchState(((Integer) getValue(value)).intValue());
                return;
            default:
                CameraLog.i(TAG, "handle unknown event: " + value);
                return;
        }
    }

    private void handleFormatSdState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onFormatSdState(status);
            }
        }
    }

    private void handleDvrLockFbState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onDvrLockFbState(status);
            }
        }
    }

    private void handleDvrStatus(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onDvrStatus(status);
            }
        }
    }

    private void handleSdState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onSdState(status);
            }
        }
    }

    private void handleVideoOutputState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onVideoOutputState(status);
            }
        }
    }

    private void handleDvrLockState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onDvrLockState(status);
            }
        }
    }

    private void handlePhotoProcessState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onPhotoProcessState(status);
            }
        }
    }

    private void handleDvrModeState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onDvrModeState(status);
            }
        }
    }

    private void handleAutoLockStState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onAutoLockStState(status);
            }
        }
    }

    private void handleDvrSwitchState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ICiuController.Callback) it.next()).onDvrSwitchState(status);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getCiuAutoLockSt() throws Exception {
        return this.mCarManager.getCiuAutoLockSt();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public void setDvrMode(int mode) throws Exception {
        this.mCarManager.setDvrMode(mode);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getDvrMode() throws Exception {
        return this.mCarManager.getDvrMode();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public void photoProcess() throws Exception {
        this.mCarManager.photoProcess();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public void setDvrLockMode(int mode) throws Exception {
        this.mCarManager.setDvrLockMode(mode);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getSdStatus() throws Exception {
        return this.mCarManager.getSdStatus();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public void setFormatMode(int mode) throws Exception {
        this.mCarManager.setFormatMode(mode);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public void setVideoOutputMode(int mode) throws Exception {
        this.mCarManager.setVideoOutputMode(mode);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getDVRStatus() throws Exception {
        return this.mCarManager.getDvrStatus();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getDVRFormatStatus() throws Exception {
        return this.mCarManager.getDvrFormatStatus();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getDvrLockFb() throws Exception {
        return this.mCarManager.getDvrLockFb();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public void setDvrEnable(int enable) throws Exception {
        this.mCarManager.setDvrEnable(enable);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ICiuController
    public int getDvrEnableState() throws Exception {
        return this.mCarManager.getDvrEnableState();
    }
}
