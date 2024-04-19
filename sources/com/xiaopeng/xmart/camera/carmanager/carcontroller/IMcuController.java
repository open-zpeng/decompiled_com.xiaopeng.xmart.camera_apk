package com.xiaopeng.xmart.camera.carmanager.carcontroller;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import com.xiaopeng.xmart.camera.carmanager.IBaseCarController;
/* loaded from: classes.dex */
public interface IMcuController extends IBaseCarController<Callback> {
    public static final int MCU_IG_STATUS_IG_OFF = 0;
    public static final int MCU_IG_STATUS_LOCAL_IG_ON = 1;
    public static final int MCU_IG_STATUS_REMOTE_IG_ON = 2;
    public static final int REMOTE_CONTROL_PROCESSING = 1;

    /* loaded from: classes.dex */
    public interface Callback extends IBaseCallback {
        default void onCiuStatusChanged(int state) {
        }

        default void onIgStatusChanged(int igValue) {
        }

        default void onOcuStatusChanged(int state) {
        }
    }

    int getCiuState() throws Exception;

    int getIgStatusFromMcu() throws Exception;

    int getOcuState() throws Exception;

    String getXpCduType() throws Exception;

    void setFlash(boolean on) throws Exception;

    void setRemoteControlFeedback(int status);
}
