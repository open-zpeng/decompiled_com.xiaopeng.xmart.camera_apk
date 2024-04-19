package com.xiaopeng.xmart.camera.carmanager.carcontroller;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import com.xiaopeng.xmart.camera.carmanager.IBaseCarController;
/* loaded from: classes.dex */
public interface IXpuController extends IBaseCarController<Callback> {
    public static final int XPU_STATUS_NEDC_OFF = 0;
    public static final int XPU_STATUS_NEDC_ON = 1;
    public static final int XPU_STATUS_START_UP_FAILURE = 5;
    public static final int XPU_STATUS_START_UP_IN_PROGRESS = 3;
    public static final int XPU_STATUS_TURN_OFF_IN_PROGRESS = 4;

    /* loaded from: classes.dex */
    public interface Callback extends IBaseCallback {
        default void onXpuPowerStateChanged(int state) {
        }
    }

    int getNEDCStatus();
}
