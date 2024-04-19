package com.xiaopeng.xmart.camera.carmanager.impl;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.tbox.CarTboxManager;
import com.xiaopeng.xmart.camera.carmanager.BaseCarController;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class TBoxController extends BaseCarController<CarTboxManager, ITboxController.Callback> implements ITboxController {
    private static final String TAG = "TBoxController";
    private final CarTboxManager.CarTboxEventCallback mCarTBoxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.TBoxController.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            TBoxController.this.handleCarEventsUpdate(carPropertyValue);
            if (carPropertyValue.getPropertyId() == 557846579) {
                CameraLog.d(TBoxController.TAG, "CarTboxManager:ID_TBOX_SOLDIER_WORK_ST", false);
            }
            int propertyId = carPropertyValue.getPropertyId();
            if (propertyId == 557846579) {
                CameraLog.d(TBoxController.TAG, "CarTboxManager:ID_TBOX_SOLDIER_WORK_ST", false);
            } else if (propertyId != 557912117) {
            } else {
                CameraLog.d(TBoxController.TAG, "CarTboxManager Receive ID_TBOX_GUARD", false);
            }
        }

        public void onErrorEvent(int propertyId, int zone) {
            CameraLog.e(TBoxController.TAG, "onErrorEvent: " + propertyId, false);
        }
    };

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public void feedbackCameraStatus(String msg) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void initCarManager(Car carClient) {
        CameraLog.d(TAG, "Init start");
        try {
            this.mCarManager = (CarTboxManager) carClient.getCarManager(CarClientWrapper.XP_TBOX_SERVICE);
            if (this.mCarManager != 0) {
                this.mCarManager.registerPropCallbackWithFlag(this.mPropertyIds, this.mCarTBoxEventCallback, 1);
            }
        } catch (CarNotConnectedException e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
        CameraLog.d(TAG, "Init end");
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected List<Integer> getRegisterPropertyIds() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(557846578);
        arrayList.add(557846579);
        arrayList.add(557912117);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void disconnect() {
        if (this.mCarManager != 0) {
            try {
                this.mCarManager.unregisterPropCallback(this.mPropertyIds, this.mCarTBoxEventCallback);
            } catch (CarNotConnectedException e) {
                CameraLog.e(TAG, e.getMessage(), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected void handleEventsUpdate(CarPropertyValue<?> value) {
        CameraLog.i(TAG, "handleEventsUpdate: " + value.getPropertyId() + ",value:" + value.getValue(), false);
        switch (value.getPropertyId()) {
            case 554700825:
                handleCameraRemoteCtrlStatus((String) getValue(value));
                return;
            case 557846578:
                handleSoldierSwStatus(((Integer) getValue(value)).intValue());
                return;
            case 557846579:
                handleSoldierWorkStatus(((Integer) getValue(value)).intValue());
                return;
            case 557912117:
                handleGuardStatus((Integer[]) getValue(value));
                return;
            default:
                CameraLog.i(TAG, "handle unknown event: " + value);
                return;
        }
    }

    private void handleSoldierSwStatus(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ITboxController.Callback) it.next()).onSoldierSwStateChanged(status);
            }
        }
    }

    private void handleSoldierWorkStatus(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ITboxController.Callback) it.next()).onSoldierWorkStatusChanged(status);
            }
        }
    }

    private void handleGuardStatus(Integer[] status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ITboxController.Callback) it.next()).onGuardStatusChanged(status);
            }
        }
    }

    private void handleCameraRemoteCtrlStatus(String cmd) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((ITboxController.Callback) it.next()).onCameraRemoteCtrlStatusChanged(cmd);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public void setCameraRemoteControlFeedback(String msg) {
        try {
            this.mCarManager.setCameraRemoteControlFeedback(msg);
        } catch (Exception e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public void setSoldierSw(int status) {
        try {
            this.mCarManager.setSoldierSw(status);
        } catch (Exception e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public int getSoldierSwState() {
        try {
            try {
                return getIntProperty(557846578);
            } catch (Exception unused) {
                return this.mCarManager.getSoldierSwState();
            }
        } catch (Exception unused2) {
            return 0;
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public int getSoldierWorkState() {
        try {
            try {
                return getIntProperty(557846579);
            } catch (Exception unused) {
                return this.mCarManager.getSoldierWorkState();
            }
        } catch (Exception unused2) {
            return 0;
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public boolean getSoldierCameraEnabled() {
        try {
            return this.mCarManager.getTboxSoliderCameraState() == 1;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public int[] getSoldierGsensorData() {
        try {
            return this.mCarManager.getSoldierGsensorData();
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController
    public void sendSoldierTickMsg() {
        try {
            this.mCarManager.sendSoldierTickMsg();
        } catch (Exception e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
    }
}
