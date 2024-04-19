package com.xiaopeng.xmart.camera.carmanager.impl;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.avm.CarAvmManager;
import com.xiaopeng.xmart.camera.carmanager.BaseCarController;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.manager.CameraManager;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.CarFunction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class AvmController extends BaseCarController<CarAvmManager, IAvmController.Callback> implements IAvmController {
    private static final String TAG = "AvmController";
    private final CarAvmManager.CarAvmEventCallback mCarAvmEventCallback = new CarAvmManager.CarAvmEventCallback() { // from class: com.xiaopeng.xmart.camera.carmanager.impl.AvmController.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CameraLog.d(AvmController.TAG, "onChangeEvent: " + carPropertyValue);
            AvmController.this.handleCarEventsUpdate(carPropertyValue);
        }

        public void onErrorEvent(int propertyId, int zone) {
            CameraLog.e(AvmController.TAG, "onErrorEvent: " + propertyId, false);
        }
    };

    public static BaseCarController createCarController(String carType) {
        String str;
        int hashCode = carType.hashCode();
        if (hashCode != 2560) {
            str = hashCode == 2561 ? "Q2" : "Q1";
            return new AvmController();
        }
        carType.equals(str);
        return new AvmController();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void initCarManager(Car carClient) {
        CameraLog.d(TAG, "Init start");
        try {
            this.mCarManager = (CarAvmManager) carClient.getCarManager(CarClientWrapper.XP_AVM_SERVICE);
            if (this.mCarManager != 0) {
                this.mCarManager.registerPropCallback(this.mPropertyIds, this.mCarAvmEventCallback);
            }
        } catch (CarNotConnectedException e) {
            CameraLog.e(TAG, e.getMessage(), false);
        }
        CameraLog.d(TAG, "Init end");
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected List<Integer> getRegisterPropertyIds() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(557855752);
        arrayList.add(557855755);
        arrayList.add(557855753);
        arrayList.add(557855758);
        arrayList.add(557855756);
        arrayList.add(557855754);
        arrayList.add(557855745);
        arrayList.add(557855760);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    public void disconnect() {
        if (this.mCarManager != 0) {
            try {
                this.mCarManager.unregisterPropCallback(this.mPropertyIds, this.mCarAvmEventCallback);
            } catch (CarNotConnectedException e) {
                CameraLog.e(TAG, e.getMessage(), false);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.BaseCarController
    protected void handleEventsUpdate(CarPropertyValue<?> value) {
        CameraLog.i(TAG, "handleEventsUpdate: " + value.getPropertyId() + ",value:" + value.getValue(), false);
        int propertyId = value.getPropertyId();
        if (propertyId == 557855745) {
            handleDisPlayModeState(((Integer) getValue(value)).intValue());
        } else if (propertyId == 557855758) {
            handleCamPosChanged(((Integer) getValue(value)).intValue());
        } else if (propertyId != 557855760) {
            switch (propertyId) {
                case 557855752:
                    handleRoofCameraChanged(((Integer) getValue(value)).intValue());
                    return;
                case 557855753:
                    handleAngleChanged(((Integer) getValue(value)).intValue());
                    return;
                case 557855754:
                    handleCamMoveStateChanged(((Integer) getValue(value)).intValue());
                    return;
                case 557855755:
                    handleHeightChanged(((Integer) getValue(value)).intValue());
                    return;
                case 557855756:
                    handleCamStateChanged(((Integer) getValue(value)).intValue());
                    return;
                default:
                    CameraLog.i(TAG, "handle unknown event: " + value);
                    return;
            }
        } else {
            handleAvmWorkState(((Integer) getValue(value)).intValue());
        }
    }

    private void handleDisPlayModeState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onDisPlayModeChanged(status);
            }
        }
    }

    private void handleCamMoveStateChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onCamMoveStateChanged(status);
            }
        }
    }

    private void handleCamStateChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).omCamStateChanged(status);
            }
        }
    }

    private void handleCamPosChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onCamPosChanged(status);
            }
        }
    }

    private void handleAngleChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onAngleChanged(status);
            }
        }
    }

    private void handleHeightChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onHeightChanged(status);
            }
        }
    }

    private void handleRoofCameraChanged(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onRoofCameraChanged(status);
            }
        }
    }

    private void handleAvmWorkState(int status) {
        synchronized (this.mCallbackLock) {
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((IAvmController.Callback) it.next()).onAvmWorkStChanged(status);
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setCameraAngle(int angle) throws Exception {
        CameraLog.d(TAG, "setCameraAngle angle:" + angle);
        this.mCarManager.setCameraAngle(angle);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int getCameraAngle() throws Exception {
        CameraLog.d(TAG, "getCameraAngle");
        return this.mCarManager.getCameraAngle();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setCameraHeight(boolean isUp) throws Exception {
        CameraLog.d(TAG, "setCameraHeight isUp:" + isUp);
        this.mCarManager.setCameraHeight(isUp ? 1 : 0);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setCameraDisplayMode(int mode) throws Exception {
        CameraLog.d(TAG, "setCameraDisplayMode mode:" + mode);
        CarCameraHelper.getInstance().getCarCamera().set360CameraType(mode);
        if (Config.getXpCduType().equals("Q1") || Config.getXpCduType().equals("Q3") || Config.getXpCduType().equals("Q3A") || Config.getXpCduType().equals("Q7") || Config.getXpCduType().equals(CarFunction.F30) || Config.getXpCduType().equals("Q8")) {
            if (mode != CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS) {
                this.mCarManager.setMultipleDisplayProperties(mode, 255, 0, 0, 255);
                return;
            } else {
                this.mCarManager.setMultipleDisplayProperties(mode, 255, 0, 1, 255);
                return;
            }
        }
        this.mCarManager.setCameraDisplayMode(mode);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int getRoofCameraState() throws Exception {
        CameraLog.d(TAG, "getRoofCameraState");
        return this.mCarManager.getRoofCameraState();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int getRoofMoveCameraState() throws Exception {
        CameraLog.d(TAG, "getRoofMoveCameraState");
        return this.mCarManager.getRoofMoveCameraState();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int getRoofCameraPosition() throws Exception {
        CameraLog.d(TAG, "getRoofCameraPosition");
        return this.mCarManager.getRoofCameraPosition();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int getCameraDisplayMode() throws Exception {
        return this.mCarManager.getCameraDisplayMode();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int getAVMWorkSt() throws Exception {
        return this.mCarManager.getAvmWorkState();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setAvm3603DAngelNew(boolean isTransBody, int angle) throws Exception {
        setAvm3603DAngel(angle);
        setAvmTransBody(isTransBody);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setAvm3603DAngel(int angle) throws Exception {
        CameraManager.getInstance().setAvm3603DAngel(angle);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public int get3603dAngle() throws Exception {
        return CameraManager.getInstance().get3603dAngle();
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setAvmTransBody(boolean on) throws Exception {
        CameraManager.getInstance().setAvmTransBody(on ? 1 : 0);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public boolean getTransBodySwitchStatus() throws Exception {
        return CameraManager.getInstance().getTransBodySwitchStatus() == 1;
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public void setTransparentChassis(boolean on) throws Exception {
        this.mCarManager.setTransparentChassis(on ? 1 : 0);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController
    public boolean getTransparentChassis() throws Exception {
        return this.mCarManager.getTransparentChassis() == 1;
    }
}
