package com.xiaopeng.xmart.camera.carmanager.carcontroller;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import com.xiaopeng.xmart.camera.carmanager.IBaseCarController;
/* loaded from: classes.dex */
public interface IVcuController extends IBaseCarController<Callback> {
    public static final int GEAR_LEVEL_D = 1;
    public static final int GEAR_LEVEL_INVALID = 0;
    public static final int GEAR_LEVEL_N = 2;
    public static final int GEAR_LEVEL_P = 4;
    public static final int GEAR_LEVEL_R = 3;

    /* loaded from: classes.dex */
    public interface Callback extends IBaseCallback {
        default void onGearChanged(int gear) {
        }

        default void onRawCarSpeedChanged(float speed) {
        }
    }

    void feedbackBatBumpRecrdStatus(int status);

    float getCarSpeed() throws Exception;

    int getGearLevel();

    boolean isReverseGear();
}
