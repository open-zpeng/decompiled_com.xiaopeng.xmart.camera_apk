package com.xiaopeng.xmart.camera.carmanager.carcontroller;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import com.xiaopeng.xmart.camera.carmanager.IBaseCarController;
/* loaded from: classes.dex */
public interface ICiuController extends IBaseCarController<Callback> {

    /* loaded from: classes.dex */
    public interface Callback extends IBaseCallback {
        void onAutoLockStState(int state);

        void onDvrLockFbState(int state);

        void onDvrLockState(int state);

        void onDvrModeState(int state);

        void onDvrStatus(int state);

        void onDvrSwitchState(int state);

        void onFormatSdState(int state);

        void onPhotoProcessState(int state);

        void onSdState(int state);

        void onVideoOutputState(int state);
    }

    int getCiuAutoLockSt() throws Exception;

    int getDVRFormatStatus() throws Exception;

    int getDVRStatus() throws Exception;

    int getDvrEnableState() throws Exception;

    int getDvrLockFb() throws Exception;

    int getDvrMode() throws Exception;

    int getSdStatus() throws Exception;

    void photoProcess() throws Exception;

    void setDvrEnable(int enable) throws Exception;

    void setDvrLockMode(int mode) throws Exception;

    void setDvrMode(int mode) throws Exception;

    void setFormatMode(int mode) throws Exception;

    void setVideoOutputMode(int mode) throws Exception;
}
