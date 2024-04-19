package com.xiaopeng.xmart.camera.carmanager.impl;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.condition.CarConditionInfo;
import android.car.hardware.condition.CarConditionManager;
import android.car.hardware.vcu.CarVcuManager;
import android.os.RemoteException;
import com.xiaopeng.xmart.camera.carmanager.BaseCarController;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IVcuController;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class VcuController extends BaseCarController<CarVcuManager, IVcuController.Callback> implements IVcuController {
    public static final int BUMP_RECRD_ST_RECORD_FAILED = 2;
    public static final int BUMP_RECRD_ST_RECORD_OK = 1;
    public static final int BUMP_RECRD_ST_UPLOAD_FAILED = 4;
    public static final int RECRD_ST_UPLOAD_OK = 3;
    private static final String TAG = "VcuController";
    private CarConditionManager mCarConditionManager;
    private final CarVcuManager.CarVcuEventCallback mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.VcuController.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            if (carPropertyValue.getPropertyId() == 557847045) {
                carPropertyValue.getValue();
            }
            CameraLog.d(VcuController.TAG, "onChangeEvent: " + carPropertyValue);
            VcuController.this.handleCarEventsUpdate(carPropertyValue);
        }

        public void onErrorEvent(int propertyId, int zone) {
            CameraLog.e(VcuController.TAG, "onErrorEvent: " + propertyId, false);
        }
    };
    CarConditionManager.CarConditionCallback mCarConditionCallback = new CarConditionManager.CarConditionCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.VcuController.2
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            if (carPropertyValue.getPropertyId() == 559944229) {
                CameraLog.i(VcuController.TAG, "car speed onChangeEvent: " + carPropertyValue, false);
                VcuController.this.handleCarEventsUpdate(carPropertyValue);
            }
            CameraLog.i(VcuController.TAG, "handle unknown event: " + carPropertyValue);
        }
    };

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IVcuController
    public void feedbackBatBumpRecrdStatus(int status) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void initCarManager(Car carClient) {
        CameraLog.d(TAG, "Init start");
        try {
            this.mCarManager = (CarVcuManager) carClient.getCarManager(CarClientWrapper.XP_VCU_SERVICE);
            if (this.mCarManager != 0) {
                this.mCarManager.registerPropCallback(this.mPropertyIds, this.mCarVcuEventCallback);
            }
            if (CarCameraHelper.getInstance().isSupportTopCamera()) {
                CarConditionManager carConditionManager = (CarConditionManager) carClient.getCarManager("xp_condition");
                this.mCarConditionManager = carConditionManager;
                if (carConditionManager != null) {
                    this.mCarConditionManager.registerCondition(new CarConditionInfo.Builder().setLimit(559944229, new Float[]{Float.valueOf(80.0f), Float.valueOf(80.0f)}).build(), this.mCarConditionCallback);
                }
            }
        } catch (RemoteException | CarNotConnectedException e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
        CameraLog.d(TAG, "Init end");
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected List<Integer> getRegisterPropertyIds() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(557847045);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void disconnect() {
        if (this.mCarManager != 0) {
            try {
                this.mCarManager.unregisterPropCallback(this.mPropertyIds, this.mCarVcuEventCallback);
                CarConditionManager carConditionManager = this.mCarConditionManager;
                if (carConditionManager != null) {
                    carConditionManager.unregisterCondition(this.mCarConditionCallback);
                }
            } catch (RemoteException | CarNotConnectedException e) {
                CameraLog.e(TAG, e.getMessage(), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected void handleEventsUpdate(CarPropertyValue<?> value) {
        CameraLog.i(TAG, "handleEventsUpdate: " + value.getPropertyId() + ",value:" + value.getValue(), false);
        int propertyId = value.getPropertyId();
        if (propertyId == 557847045) {
            handleGearUpdate(((Integer) getValue(value)).intValue());
        } else if (propertyId == 559944229) {
            handleRawCarSpeed(((Float) getValue(value)).floatValue());
        } else {
            CameraLog.i(TAG, "handle unknown event: " + value);
        }
    }

    private void handleRawCarSpeed(float status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IVcuController.Callback) it.next()).onRawCarSpeedChanged(status);
            }
        }
    }

    private void handleGearUpdate(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IVcuController.Callback) it.next()).onGearChanged(status);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IVcuController
    public int getGearLevel() {
        try {
            return this.mCarManager.getDisplayGearLevel();
        } catch (Exception unused) {
            return 0;
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IVcuController
    public boolean isReverseGear() {
        return getGearLevel() == 3;
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IVcuController
    public float getCarSpeed() {
        try {
            return this.mCarManager.getRawCarSpeed();
        } catch (Exception unused) {
            return 0.0f;
        }
    }
}
