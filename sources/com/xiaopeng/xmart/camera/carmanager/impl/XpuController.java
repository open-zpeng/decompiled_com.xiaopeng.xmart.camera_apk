package com.xiaopeng.xmart.camera.carmanager.impl;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.xpu.CarXpuManager;
import com.xiaopeng.xmart.camera.carmanager.BaseCarController;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IXpuController;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class XpuController extends BaseCarController<CarXpuManager, IXpuController.Callback> implements IXpuController {
    private static final String TAG = "XpuController";
    private final CarXpuManager.CarXpuEventCallback mCarXpuEventCallback = new CarXpuManager.CarXpuEventCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.XpuController.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CameraLog.d(XpuController.TAG, "onChangeEvent: " + carPropertyValue);
            XpuController.this.handleCarEventsUpdate(carPropertyValue);
        }

        public void onErrorEvent(int propertyId, int zone) {
            CameraLog.e(XpuController.TAG, "onErrorEvent: " + propertyId, false);
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void initCarManager(Car carClient) {
        CameraLog.d(TAG, "Init start");
        try {
            this.mCarManager = (CarXpuManager) carClient.getCarManager(CarClientWrapper.XP_XPU_SERVICE);
            if (this.mCarManager != 0) {
                this.mCarManager.registerPropCallback(this.mPropertyIds, this.mCarXpuEventCallback);
            }
        } catch (CarNotConnectedException e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
        CameraLog.d(TAG, "Init end");
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected List<Integer> getRegisterPropertyIds() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(557856775);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void disconnect() {
        if (this.mCarManager != 0) {
            try {
                this.mCarManager.unregisterPropCallback(this.mPropertyIds, this.mCarXpuEventCallback);
            } catch (CarNotConnectedException e) {
                CameraLog.e(TAG, e.getMessage(), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected void handleEventsUpdate(CarPropertyValue<?> value) {
        CameraLog.i(TAG, "handleEventsUpdate: " + value.getPropertyId() + ",value:" + value.getValue(), false);
        if (value.getPropertyId() == 557856775) {
            handlePowerCameraChanged(((Integer) getValue(value)).intValue());
        } else {
            CameraLog.i(TAG, "handle unknown event: " + value);
        }
    }

    private void handlePowerCameraChanged(int status) {
        CameraLog.i(TAG, "handlePowerCameraChanged", false);
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IXpuController.Callback) it.next()).onXpuPowerStateChanged(status);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IXpuController
    public int getNEDCStatus() {
        int i;
        try {
            i = this.mCarManager.getNedcSwitchStatus();
        } catch (Exception unused) {
            i = 0;
        }
        CameraLog.i(TAG, "getNEDCStatusState: " + i, false);
        return i;
    }
}
