package com.xiaopeng.xmart.camera.model.camera;

import android.os.ConditionVariable;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IXpuController;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import java.util.Arrays;
/* loaded from: classes.dex */
public class CarServiceControl {
    public static final long MCU_HANDLE_INTERVAL = 5000;
    private static final String TAG = "CarServiceControl";
    private IAvmController mIAvmController;
    private ITboxController mITboxController;
    private IXpuController mIXpuController;
    private boolean mIsShockProcessing = false;
    private int[] mSoldierGsensorData = null;
    private ConditionVariable mCarSrvConnectedSignal = new ConditionVariable();

    public static CarServiceControl getInstance() {
        return SingletonHolder.sInstance;
    }

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        static CarServiceControl sInstance = new CarServiceControl();

        private SingletonHolder() {
        }
    }

    public CarServiceControl() {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CarServiceControl$LJI5V0unkk2gg-4oeWOcP_y1Occ
            @Override // java.lang.Runnable
            public final void run() {
                CarServiceControl.this.lambda$new$0$CarServiceControl();
            }
        });
    }

    public /* synthetic */ void lambda$new$0$CarServiceControl() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
        }
    }

    public void initController() {
        CameraLog.i(TAG, "  initController ", false);
        CarClientWrapper carClientWrapper = CarClientWrapper.getInstance();
        this.mIAvmController = (IAvmController) carClientWrapper.getController(CarClientWrapper.XP_AVM_SERVICE);
        this.mITboxController = (ITboxController) carClientWrapper.getController(CarClientWrapper.XP_TBOX_SERVICE);
        this.mIXpuController = (IXpuController) carClientWrapper.getController(CarClientWrapper.XP_XPU_SERVICE);
        this.mCarSrvConnectedSignal.open();
    }

    public ConditionVariable getCarSrvConnectedSignal() {
        return this.mCarSrvConnectedSignal;
    }

    public int getAVMWorkSt() {
        try {
            IAvmController iAvmController = this.mIAvmController;
            if (iAvmController != null) {
                return iAvmController.getAVMWorkSt();
            }
            return 0;
        } catch (Exception e) {
            CameraLog.d(TAG, "getAVMWorkSt e:" + e.getMessage(), false);
            return 0;
        }
    }

    public boolean isAVMActived() {
        if (CarCameraHelper.getInstance().hasAVM() && CarCameraHelper.getInstance().shouldWaitAvmReady()) {
            int aVMWorkSt = getAVMWorkSt();
            return aVMWorkSt == 3 || aVMWorkSt == 1;
        }
        return false;
    }

    public boolean isAVMWorkOnSt() {
        int aVMWorkSt = getAVMWorkSt();
        CameraLog.d(TAG, "isAVMWorkOnSt avmWorkSt:" + aVMWorkSt, false);
        return aVMWorkSt == 3 || aVMWorkSt == 1;
    }

    public boolean isInvalidShockGSensorData() {
        ITboxController iTboxController = this.mITboxController;
        if (iTboxController != null) {
            this.mSoldierGsensorData = iTboxController.getSoldierGsensorData();
        }
        StringBuilder append = new StringBuilder().append("mcuShockInfo is: ");
        int[] iArr = this.mSoldierGsensorData;
        CameraLog.d(TAG, append.append(iArr == null ? " is null " : Arrays.toString(iArr)).toString(), false);
        int[] iArr2 = this.mSoldierGsensorData;
        if (iArr2 == null || iArr2.length < 3) {
            CameraLog.e(TAG, "isInvalidShockGSensorData return invalid", false);
            return true;
        }
        return false;
    }

    public int getSoldierGsensorData() {
        CameraLog.d(TAG, "getSoldierGsensorData:" + this.mSoldierGsensorData, false);
        int[] iArr = this.mSoldierGsensorData;
        if (iArr == null) {
            return 0;
        }
        int i = iArr[0];
        int i2 = iArr[1];
        return Math.max(Math.max(i, i2), iArr[2]);
    }

    public void sendSoldierTickMsg() {
        ThreadPoolHelper.getInstance().executeForLongTask(new Runnable() { // from class: com.xiaopeng.xmart.camera.model.camera.-$$Lambda$CarServiceControl$rSkDUiRIzQo5lGJCW6vv2v-pgQQ
            @Override // java.lang.Runnable
            public final void run() {
                CarServiceControl.this.lambda$sendSoldierTickMsg$1$CarServiceControl();
            }
        });
    }

    public /* synthetic */ void lambda$sendSoldierTickMsg$1$CarServiceControl() {
        CameraLog.d(TAG, "Send Shock handle cmd to MCU every 5 seconds", false);
        while (this.mIsShockProcessing) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.mIsShockProcessing) {
                try {
                    ITboxController iTboxController = this.mITboxController;
                    if (iTboxController != null) {
                        iTboxController.sendSoldierTickMsg();
                    }
                } catch (Exception e2) {
                    CameraLog.e(TAG, "setShockHandleCmd processing failed: " + e2.getMessage(), false);
                }
            }
        }
    }

    public void feedbackCameraStatus(String status) {
        ITboxController iTboxController = this.mITboxController;
        if (iTboxController != null) {
            iTboxController.feedbackCameraStatus(status);
        }
    }

    public void setShockProcessing(boolean shockProcessing) {
        this.mIsShockProcessing = shockProcessing;
    }
}
