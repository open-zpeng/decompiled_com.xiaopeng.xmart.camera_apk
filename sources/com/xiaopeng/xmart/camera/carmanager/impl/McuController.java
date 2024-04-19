package com.xiaopeng.xmart.camera.carmanager.impl;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import com.xiaopeng.xmart.camera.carmanager.BaseCarController;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class McuController extends BaseCarController<CarMcuManager, IMcuController.Callback> implements IMcuController {
    private static final String TAG = "McuController";
    private final CarMcuManager.CarMcuEventCallback mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.McuController.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CameraLog.d(McuController.TAG, "onChangeEvent: " + carPropertyValue);
            McuController.this.handleCarEventsUpdate(carPropertyValue);
        }

        public void onErrorEvent(int propertyId, int zone) {
            CameraLog.e(McuController.TAG, "onErrorEvent: " + propertyId, false);
        }
    };

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController
    public void setRemoteControlFeedback(int status) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void initCarManager(Car carClient) {
        try {
            this.mCarManager = (CarMcuManager) carClient.getCarManager(CarClientWrapper.XP_MCU_SERVICE);
            if (this.mCarManager != 0) {
                this.mCarManager.registerPropCallback(this.mPropertyIds, this.mCarMcuEventCallback);
            }
        } catch (CarNotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected List<Integer> getRegisterPropertyIds() {
        ArrayList arrayList = new ArrayList();
        if (CarCameraHelper.getInstance().isSupportTopCamera()) {
            arrayList.add(557847612);
        }
        if (CarCameraHelper.getInstance().isSupportDvr()) {
            arrayList.add(557847613);
        }
        arrayList.add(557847561);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void disconnect() {
        try {
            this.mCarManager.unregisterPropCallback(this.mPropertyIds, this.mCarMcuEventCallback);
        } catch (CarNotConnectedException e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected void handleEventsUpdate(CarPropertyValue<?> value) {
        CameraLog.i(TAG, "handleEventsUpdate: " + value.getPropertyId() + ",value:" + value.getValue(), false);
        switch (value.getPropertyId()) {
            case 557847561:
                handleIgStatusChanged(((Integer) getValue(value)).intValue());
                return;
            case 557847612:
                handleOcuStatusChanged(((Integer) getValue(value)).intValue());
                return;
            case 557847613:
                handleCiuStatusChanged(((Integer) getValue(value)).intValue());
                return;
            default:
                CameraLog.i(TAG, "handle unknown event: " + value);
                return;
        }
    }

    private void handleIgStatusChanged(int igValue) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IMcuController.Callback) it.next()).onIgStatusChanged(igValue);
            }
        }
    }

    private void handleOcuStatusChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IMcuController.Callback) it.next()).onOcuStatusChanged(status);
            }
        }
    }

    private void handleCiuStatusChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IMcuController.Callback) it.next()).onCiuStatusChanged(status);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController
    public void setFlash(boolean on) throws Exception {
        this.mCarManager.setFlash(on ? 1 : 0);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController
    public int getCiuState() throws Exception {
        int ciuState = this.mCarManager.getCiuState();
        CameraLog.d(TAG, "getCiuState: " + ciuState);
        return ciuState;
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController
    public int getOcuState() throws Exception {
        int ocuState = this.mCarManager.getOcuState();
        CameraLog.d(TAG, "getOcuState: " + ocuState);
        return ocuState;
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController
    public int getIgStatusFromMcu() throws Exception {
        int igStatusFromMcu = this.mCarManager.getIgStatusFromMcu();
        CameraLog.d(TAG, "getIgState: " + igStatusFromMcu, false);
        return igStatusFromMcu;
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController
    public String getXpCduType() throws Exception {
        return this.mCarManager.getXpCduType();
    }
}
