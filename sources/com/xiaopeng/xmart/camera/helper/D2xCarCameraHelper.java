package com.xiaopeng.xmart.camera.helper;

import android.car.Car;
import android.os.ConditionVariable;
import android.os.SystemProperties;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IVcuController;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
/* loaded from: classes.dex */
public class D2xCarCameraHelper extends CarCameraHelper {
    private static final String TAG = "D2xCarCameraHelper";
    private static Boolean sHasCiu;
    private ConditionVariable mConditionVariable;
    private int mConfigCode = 0;
    private IMcuController mIMcuController;
    private IVcuController mIVcuController;

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean isSupportTopCamera() {
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean shouldWaitAvmReady() {
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public void init() {
        CameraLog.d(TAG, "D2xCarCameraHelper init", false);
        ConditionVariable conditionVariable = new ConditionVariable();
        this.mConditionVariable = conditionVariable;
        conditionVariable.close();
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.helper.-$$Lambda$D2xCarCameraHelper$4m8r26tDK41OS-r6mGtZNj9vmac
            @Override // java.lang.Runnable
            public final void run() {
                D2xCarCameraHelper.this.lambda$init$0$D2xCarCameraHelper();
            }
        });
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.helper.-$$Lambda$D2xCarCameraHelper$mTwzi-THWUKWz21l_Gu41EN_-hs
            @Override // java.lang.Runnable
            public final void run() {
                D2xCarCameraHelper.this.lambda$init$1$D2xCarCameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$init$0$D2xCarCameraHelper() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
            this.mConditionVariable.open();
        }
    }

    public /* synthetic */ void lambda$init$1$D2xCarCameraHelper() {
        try {
            this.mConditionVariable.block();
            if (this.mIMcuController != null) {
                CameraLog.d(TAG, "Check has top camera", false);
                hasTopCamera();
            }
        } catch (Exception e) {
            CameraLog.d(TAG, "Check top camera e:" + e.getMessage(), false);
        }
    }

    private void initController() {
        CameraLog.i(TAG, "  initController ", false);
        CarClientWrapper carClientWrapper = CarClientWrapper.getInstance();
        this.mIMcuController = (IMcuController) carClientWrapper.getController(CarClientWrapper.XP_MCU_SERVICE);
        this.mIVcuController = (IVcuController) carClientWrapper.getController(CarClientWrapper.XP_VCU_SERVICE);
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean hasTopCamera() {
        boolean z;
        CameraLog.d(TAG, "hasTopCamera D2X :" + this.mIMcuController, false);
        if (!CarCameraHelper.getInstance().isSupportTopCamera()) {
            return false;
        }
        try {
            IMcuController iMcuController = this.mIMcuController;
            if (iMcuController != null) {
                z = iMcuController.getOcuState() == 1;
                try {
                    CameraLog.i(TAG, "getHasTopCamera start,hasTopCamera: " + z + " ocu:" + this.mIMcuController.getOcuState(), false);
                } catch (Exception e) {
                    e = e;
                    CameraLog.e(TAG, "getHasTopCamera error:" + e.getMessage(), false);
                    return z;
                }
            } else {
                z = false;
            }
            CameraLog.i(TAG, "getHasTopCamera start, hasTopCamera: " + z, false);
            setHasTopCamera(z);
            return z;
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean hasDvrCamera() {
        try {
            if (isSupportDvr()) {
                CameraLog.i(TAG, "hasDvrCamera mcu:" + this.mIMcuController, false);
                IMcuController iMcuController = this.mIMcuController;
                if (iMcuController != null) {
                    return iMcuController.getCiuState() == 1;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            CameraLog.e(TAG, "hasDvrCamera error:" + e.getMessage(), false);
            return false;
        }
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public float getCarSpeed() {
        IVcuController iVcuController;
        float f = 0.0f;
        try {
            iVcuController = this.mIVcuController;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (iVcuController == null) {
            CameraLog.d(TAG, "mIVcuController is null.", false);
            return 0.0f;
        }
        f = iVcuController.getCarSpeed();
        CameraLog.d(TAG, "getCarSpeed Speed is " + f, false);
        return f;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean isSupportDvr() {
        if (SystemProperties.getBoolean(Config.SYS_CONFIG_DVR_DEBUG, false)) {
            return true;
        }
        CameraLog.d(TAG, "isSupportDvr, sHasCiu=" + sHasCiu, false);
        if (sHasCiu == null) {
            sHasCiu = Boolean.FALSE;
            if ("Q5".equals(Car.getXpCduType()) && (isHighConfig() || isMiddleConfig())) {
                sHasCiu = Boolean.TRUE;
            }
        }
        return sHasCiu.booleanValue();
    }

    private boolean isLowConfig() {
        int cfc = getCFC();
        return cfc == 4 || cfc == 1;
    }

    private boolean isMiddleConfig() {
        return getCFC() == 2;
    }

    private boolean isHighConfig() {
        return getCFC() == 3;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean hasAVM() {
        return !isLowConfig();
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean isSupportAvmPipTouch() {
        return hasAVM();
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public void setHasTopCamera(boolean hasTopCamera) {
        CameraLog.d(TAG, "setHasTopCamera1, hasTopCamera=" + hasTopCamera, false);
        getCarCamera().setHasTopCamera(hasTopCamera);
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public long getTopCameraRotateTime() {
        long abs = Math.abs((long) (getCarCamera().getAngle() * 6500.0d));
        CameraLog.d(TAG, "getTopCameraRotateTime rotateTime=" + abs + ";angle=" + getCarCamera().getAngle(), false);
        return abs;
    }

    private int getCFC() {
        CameraLog.i(TAG, "getCFC " + this.mConfigCode, false);
        int i = this.mConfigCode;
        if (i != 0) {
            return i;
        }
        try {
            this.mConfigCode = Integer.parseInt(SystemProperties.get(Config.SYS_CONFIG_CODE_CFC, ""));
            CameraLog.i(TAG, "cfc " + SystemProperties.get(Config.SYS_CONFIG_CODE_CFC, "") + " code:" + this.mConfigCode, false);
        } catch (NumberFormatException e) {
            CameraLog.i(TAG, "cfc e:" + e.getMessage(), false);
        }
        if (this.mConfigCode == 0) {
            this.mConfigCode = 1;
        }
        return this.mConfigCode;
    }
}
