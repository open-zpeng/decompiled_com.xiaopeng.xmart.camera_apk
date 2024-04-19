package com.xiaopeng.xmart.camera.helper;

import android.os.SystemProperties;
/* loaded from: classes.dex */
public class E28CarCameraHelper extends CarCameraHelper {
    public static final String SYS_CONFIG_AVM_CFC = "persist.sys.xiaopeng.AVM";
    private static final String TAG = "E28CarCameraHelper";
    private Boolean mIsHasAvm;

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public void init() {
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean isSupportAvmPipTouch() {
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean isSupportDvr() {
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean isSupportTopCamera() {
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean shouldWaitAvmReady() {
        return true;
    }

    @Override // com.xiaopeng.xmart.camera.helper.CarCameraHelper
    public boolean hasAVM() {
        Boolean bool = this.mIsHasAvm;
        if (bool != null) {
            return bool.booleanValue();
        }
        Boolean valueOf = Boolean.valueOf(SystemProperties.getBoolean("persist.sys.xiaopeng.AVM", false));
        this.mIsHasAvm = valueOf;
        return valueOf.booleanValue();
    }
}
