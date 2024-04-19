package com.xiaopeng.xmart.camera.carmanager.carcontroller;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import com.xiaopeng.xmart.camera.carmanager.IBaseCarController;
/* loaded from: classes.dex */
public interface IAvmController extends IBaseCarController<Callback> {
    public static final int AVM_STATUS_ALL_ON = 1;
    public static final int AVM_STATUS_DISPLAY_ON = 3;
    public static final int AVM_STATUS_ON = 1;
    public static final int HAS_CAMERA = 2;

    /* loaded from: classes.dex */
    public interface Callback extends IBaseCallback {
        default void omCamStateChanged(int state) {
        }

        default void onAngleChanged(int state) {
        }

        default void onAvmWorkStChanged(int state) {
        }

        default void onCamMoveStateChanged(int state) {
        }

        default void onCamPosChanged(int state) {
        }

        default void onDisPlayModeChanged(int state) {
        }

        default void onHeightChanged(int state) {
        }

        default void onRoofCameraChanged(int state) {
        }
    }

    int get3603dAngle() throws Exception;

    int getAVMWorkSt() throws Exception;

    int getCameraAngle() throws Exception;

    int getCameraDisplayMode() throws Exception;

    int getRoofCameraPosition() throws Exception;

    int getRoofCameraState() throws Exception;

    int getRoofMoveCameraState() throws Exception;

    boolean getTransBodySwitchStatus() throws Exception;

    boolean getTransparentChassis() throws Exception;

    void setAvm3603DAngel(int angle) throws Exception;

    void setAvm3603DAngelNew(boolean isTransBody, int angle) throws Exception;

    void setAvmTransBody(boolean on) throws Exception;

    void setCameraAngle(int angle) throws Exception;

    void setCameraDisplayMode(int mode) throws Exception;

    void setCameraHeight(boolean isUp) throws Exception;

    void setTransparentChassis(boolean on) throws Exception;
}
