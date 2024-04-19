package com.xiaopeng.xmart.camera.carmanager.carcontroller;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import com.xiaopeng.xmart.camera.carmanager.IBaseCarController;
/* loaded from: classes.dex */
public interface ITboxController extends IBaseCarController<Callback> {

    /* loaded from: classes.dex */
    public interface Callback extends IBaseCallback {
        default void onCameraRemoteCtrlStatusChanged(String cmd) {
        }

        default void onGuardStatusChanged(Integer[] status) {
        }

        default void onSoldierSwStateChanged(int status) {
        }

        default void onSoldierWorkStatusChanged(int status) {
        }
    }

    void feedbackCameraStatus(String status);

    boolean getSoldierCameraEnabled();

    int[] getSoldierGsensorData();

    int getSoldierSwState();

    int getSoldierWorkState();

    void sendSoldierTickMsg();

    void setCameraRemoteControlFeedback(String msg);

    void setSoldierSw(int status);
}
