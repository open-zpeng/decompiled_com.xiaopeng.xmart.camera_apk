package com.xiaopeng.drivingimageassist.scene;

import com.xiaopeng.drivingimageassist.DIGActivity;
import com.xiaopeng.drivingimageassist.DIGModule;
import com.xiaopeng.drivingimageassist.R;
import com.xiaopeng.drivingimageassist.carapi.CarApiHelper;
import com.xiaopeng.drivingimageassist.displaycontrol.DisPlayControlHelper;
import com.xiaopeng.drivingimageassist.event.ApDisplayEvent;
import com.xiaopeng.drivingimageassist.event.CameraDisplayEvent;
import com.xiaopeng.drivingimageassist.event.IGStatusEvent;
import com.xiaopeng.drivingimageassist.event.NRACtrlDisplayEvent;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import com.xiaopeng.drivingimageassist.event.TurnLampSceneDisplayEvent;
import com.xiaopeng.drivingimageassist.utils.ThreadUtils;
import com.xiaopeng.drivingimageassist.utils.ToastUtil;
import com.xiaopeng.drivingimageassist.view.UIConfigManger;
import com.xiaopeng.lib.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes.dex */
public class NRACtrlScene implements IScene {
    private static final String TAG = "NRACtrlScene";
    private boolean isFromKey;
    private boolean mApDisplay;
    private boolean mCameraDisplay;
    private Runnable mCheckDisplay;
    private int mCtrlStatus;
    private boolean mTurnLampSceneDisplay;

    public NRACtrlScene() {
        register();
        this.mApDisplay = DisPlayControlHelper.instance().isApDisplay();
        this.mCameraDisplay = DisPlayControlHelper.instance().isCameraDisplay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(IGStatusEvent event) {
        LogUtils.i(TAG, "IGStatusEvent :" + event.isOff());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TurnLampSceneDisplayEvent event) {
        boolean isDisPlay = event.isDisPlay();
        if (isDisPlay != this.mTurnLampSceneDisplay) {
            LogUtils.i(TAG, "mTurnLampSceneDisplay :" + this.mTurnLampSceneDisplay);
        }
        this.mTurnLampSceneDisplay = isDisPlay;
        if (this.mCtrlStatus == 1 || DIGActivity.sDisplayType == getCameraType()) {
            checkCanShow();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(NRACtrlEvent event) {
        boolean isFromKey = event.isFromKey();
        this.mCtrlStatus = event.getValue();
        LogUtils.i(TAG, "mCtrlStatus :" + this.mCtrlStatus + " isEventFromKey : " + isFromKey);
        if (!this.isFromKey || isFromKey) {
            this.isFromKey = isFromKey;
            if (isFromKey && this.mCtrlStatus != 1) {
                this.isFromKey = false;
            }
            if (this.mCtrlStatus == 1 || DIGActivity.sDisplayType == getCameraType()) {
                checkCanShow();
            } else {
                EventBus.getDefault().post(new NRACtrlDisplayEvent(this.mCtrlStatus));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(ApDisplayEvent event) {
        this.mApDisplay = event.isDisPlay();
        LogUtils.i(TAG, "mApDisplay :" + this.mApDisplay);
        displayCheck(this.mApDisplay);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(CameraDisplayEvent event) {
        this.mCameraDisplay = event.isDisPlay();
        LogUtils.i(TAG, "mCameraDisplay :" + this.mCameraDisplay);
        displayCheck(this.mCameraDisplay);
    }

    private void displayCheck(boolean cameraDisplay) {
        Runnable runnable = this.mCheckDisplay;
        if (runnable != null) {
            ThreadUtils.removeWorker(runnable);
            this.mCheckDisplay = null;
        }
        if (cameraDisplay) {
            checkCanShow();
            return;
        }
        Runnable runnable2 = new Runnable() { // from class: com.xiaopeng.drivingimageassist.scene.NRACtrlScene.1
            @Override // java.lang.Runnable
            public void run() {
                NRACtrlScene.this.checkCanShow();
                NRACtrlScene.this.mCheckDisplay = null;
            }
        };
        this.mCheckDisplay = runnable2;
        ThreadUtils.postWorker(runnable2, 3000L);
    }

    public void checkCanShow() {
        if (check()) {
            show();
        } else {
            hide();
        }
    }

    private boolean check() {
        if (this.mCtrlStatus != 1 || this.mApDisplay || this.mCameraDisplay || (this.mTurnLampSceneDisplay && !this.isFromKey)) {
            if (this.isFromKey) {
                ToastUtil.showToast(R.string.turn_lamp_error_tip);
                return false;
            }
            return false;
        }
        return true;
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void hide() {
        if (DIGActivity.sDisplayType == getCameraType()) {
            EventBus.getDefault().post(new NRACtrlDisplayEvent(0));
            if (!DIGModule.instance().checkTurnLamp()) {
                DIGActivity.hide();
            }
        } else {
            LogUtils.i(TAG, "hide fail: sDisplayType = " + DIGActivity.sDisplayType);
        }
        if (this.isFromKey) {
            this.mCtrlStatus = 0;
            this.isFromKey = false;
        }
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void show() {
        if (this.isFromKey) {
            boolean avmWorkState = CarApiHelper.instance().getAvmWorkState();
            boolean isNedcStatus = CarApiHelper.instance().isNedcStatus();
            boolean isXpuComponent = DIGModule.instance().isXpuComponent();
            LogUtils.i(TAG, "isAvmWork:" + avmWorkState + " isNedc:" + isNedcStatus);
            if (!avmWorkState || !isXpuComponent || isNedcStatus) {
                ToastUtil.showToast(R.string.turn_lamp_error_tip);
                this.mCtrlStatus = 0;
                this.isFromKey = false;
                return;
            }
        }
        EventBus.getDefault().post(new NRACtrlDisplayEvent(1));
        DIGActivity.show(DIGModule.instance().getContext(), getCameraType());
    }

    private int getCameraType() {
        return UIConfigManger.instance().getUIConfig().getNRACtrlCameraType();
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void register() {
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void unRegister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public boolean checkNRACtrl() {
        return (this.mCtrlStatus != 1 || this.mApDisplay || this.mCameraDisplay) ? false : true;
    }

    public void reset() {
        if (this.isFromKey) {
            this.mCtrlStatus = 0;
            this.isFromKey = false;
            EventBus.getDefault().post(new NRACtrlDisplayEvent(0));
            LogUtils.i(TAG, "reset");
        }
    }
}
