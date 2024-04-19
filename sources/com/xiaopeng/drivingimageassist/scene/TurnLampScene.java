package com.xiaopeng.drivingimageassist.scene;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.xiaopeng.drivingimageassist.DIGActivity;
import com.xiaopeng.drivingimageassist.DIGModule;
import com.xiaopeng.drivingimageassist.R;
import com.xiaopeng.drivingimageassist.carapi.CarApiHelper;
import com.xiaopeng.drivingimageassist.displaycontrol.DisPlayControlHelper;
import com.xiaopeng.drivingimageassist.event.ApDisplayEvent;
import com.xiaopeng.drivingimageassist.event.AvmWorkEvent;
import com.xiaopeng.drivingimageassist.event.CameraDisplayEvent;
import com.xiaopeng.drivingimageassist.event.GearLevelEvent;
import com.xiaopeng.drivingimageassist.event.NRACtrlDisplayEvent;
import com.xiaopeng.drivingimageassist.event.TurnLampEvent;
import com.xiaopeng.drivingimageassist.event.TurnLampSceneDisplayEvent;
import com.xiaopeng.drivingimageassist.statistic.StatisticConstants;
import com.xiaopeng.drivingimageassist.utils.Constant;
import com.xiaopeng.drivingimageassist.utils.ThreadUtils;
import com.xiaopeng.drivingimageassist.utils.ToastUtil;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes.dex */
public class TurnLampScene implements IScene {
    private static final String TAG = "TurnScene";
    private boolean isTurnOn;
    private boolean mApDisplay;
    private boolean mCameraDisplay;
    private Runnable mCheckDisplay;
    private Context mContext;
    private int mGearLevel;
    private boolean mIsAvmWork;
    private boolean mIsLeftLamp;
    private boolean mIsRightLamp;
    private boolean mNRACtrlDisplay;
    private Runnable mSpeechTask;
    private float mSpeed;
    private Runnable mSpeedCheck = new Runnable() { // from class: com.xiaopeng.drivingimageassist.scene.TurnLampScene.3
        @Override // java.lang.Runnable
        public void run() {
            if (TurnLampScene.this.checkWithNotSpeech()) {
                updateSpeed();
                ThreadUtils.postWorker(TurnLampScene.this.mSpeedCheck, 200L);
            }
            TurnLampScene.this.checkCanShow();
        }

        private void updateSpeed() {
            float rawCarSpeed = CarApiHelper.instance().getRawCarSpeed();
            boolean z = true;
            boolean z2 = TurnLampScene.this.isInSpeedLimit() && rawCarSpeed > 10.0f;
            if (TurnLampScene.this.isInSpeedLimit() || rawCarSpeed > 10.0f) {
                z = false;
            }
            if (z2 || z) {
                LogUtils.i(TurnLampScene.TAG, "mSpeed:" + rawCarSpeed);
            }
            TurnLampScene.this.mSpeed = rawCarSpeed;
        }
    };
    private ContentObserver mTurnBtnObserver;

    public TurnLampScene(Context context) {
        this.mContext = context;
        register();
        updateStatus();
        this.mApDisplay = DisPlayControlHelper.instance().isApDisplay();
        this.mCameraDisplay = DisPlayControlHelper.instance().isCameraDisplay();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TurnLampEvent event) {
        this.mIsLeftLamp = event.isLeftLamp();
        this.mIsRightLamp = event.isRightLamp();
        LogUtils.i(TAG, "mIsLeftLamp :" + this.mIsLeftLamp);
        LogUtils.i(TAG, "mIsRightLamp :" + this.mIsRightLamp);
        this.mIsAvmWork = CarApiHelper.instance().getAvmWorkState();
        LogUtils.i(TAG, "mIsAvmWork :" + this.mIsAvmWork);
        if (checkWithNotSpeech()) {
            ThreadUtils.removeWorker(this.mSpeedCheck);
            ThreadUtils.postWorker(this.mSpeedCheck);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(StatisticConstants.TurnLampError.AVM_WORK_ERROR);
        arrayList.add(StatisticConstants.TurnLampError.XPU_ERROR);
        boolean checkWithNot = checkWithNot(arrayList);
        if (checkWithNot && (!DIGModule.instance().isXpuComponent() || !this.mIsAvmWork)) {
            ToastUtil.showToast(R.string.turn_lamp_error_tip);
        }
        LogUtils.i(TAG, "checkWithNotXpuLoaded: " + checkWithNot);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(GearLevelEvent event) {
        this.mGearLevel = event.getGearLevel();
        LogUtils.i(TAG, "mGearLevel :" + this.mGearLevel);
        if (checkWithNotSpeech()) {
            ThreadUtils.removeWorker(this.mSpeedCheck);
            ThreadUtils.postWorker(this.mSpeedCheck);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(ApDisplayEvent event) {
        this.mApDisplay = event.isDisPlay();
        LogUtils.i(TAG, "mApDisplay :" + this.mApDisplay);
        displayCheck(this.mApDisplay);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(AvmWorkEvent event) {
        this.mIsAvmWork = event.isWork();
        LogUtils.i(TAG, "mIsAvmWork :" + this.mIsAvmWork);
        if (checkWithNotSpeech()) {
            ThreadUtils.removeWorker(this.mSpeedCheck);
            ThreadUtils.postWorker(this.mSpeedCheck);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(CameraDisplayEvent event) {
        this.mCameraDisplay = event.isDisPlay();
        LogUtils.i(TAG, "mCameraDisplay :" + this.mCameraDisplay);
        displayCheck(this.mCameraDisplay);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(NRACtrlDisplayEvent event) {
        this.mNRACtrlDisplay = event.isDisPlay();
        LogUtils.i(TAG, "mNRACtrlDisplay :" + this.mNRACtrlDisplay);
        if (this.mNRACtrlDisplay || !checkWithNotSpeech()) {
            return;
        }
        ThreadUtils.removeWorker(this.mSpeedCheck);
        ThreadUtils.postWorker(this.mSpeedCheck);
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void hide() {
        EventBus.getDefault().post(new TurnLampSceneDisplayEvent(false));
        if (this.mNRACtrlDisplay || DIGModule.instance().checkNRACtrl()) {
            return;
        }
        DIGActivity.hide();
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void show() {
        if (isLeftLamp()) {
            EventBus.getDefault().post(new TurnLampSceneDisplayEvent(true));
            DIGActivity.show(DIGModule.instance().getContext(), 2);
        }
        if (isRightLamp()) {
            EventBus.getDefault().post(new TurnLampSceneDisplayEvent(true));
            DIGActivity.show(DIGModule.instance().getContext(), 3);
        }
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.mTurnBtnObserver = new ContentObserver(new Handler()) { // from class: com.xiaopeng.drivingimageassist.scene.TurnLampScene.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                TurnLampScene.this.updateStatus();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Constant.TurnScene.TURN_ASSISTANT_SW), true, this.mTurnBtnObserver);
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
        Runnable runnable2 = new Runnable() { // from class: com.xiaopeng.drivingimageassist.scene.TurnLampScene.2
            @Override // java.lang.Runnable
            public void run() {
                if (TurnLampScene.this.checkWithNotSpeech()) {
                    ThreadUtils.removeWorker(TurnLampScene.this.mSpeedCheck);
                    ThreadUtils.postWorker(TurnLampScene.this.mSpeedCheck);
                }
                TurnLampScene.this.mCheckDisplay = null;
            }
        };
        this.mCheckDisplay = runnable2;
        ThreadUtils.postWorker(runnable2, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatus() {
        this.isTurnOn = Settings.System.getInt(this.mContext.getContentResolver(), Constant.TurnScene.TURN_ASSISTANT_SW, 0) == 1;
        LogUtils.i(TAG, "isTurnOn:" + this.isTurnOn);
    }

    @Override // com.xiaopeng.drivingimageassist.scene.IScene
    public void unRegister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private boolean isTurnOn() {
        return this.isTurnOn;
    }

    private boolean check() {
        return (isLeftLamp() || isRightLamp()) && isNoRLevel() && isInSpeedLimit() && isTurnOn() && !this.mApDisplay && !this.mCameraDisplay && !this.mNRACtrlDisplay && this.mIsAvmWork && !CarApiHelper.instance().isNedcStatus() && DIGModule.instance().isXpuComponent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkWithNotSpeech() {
        return (isLeftLamp() || isRightLamp()) && isNoRLevel() && isTurnOn() && !this.mApDisplay && !this.mCameraDisplay && !this.mNRACtrlDisplay && this.mIsAvmWork && !CarApiHelper.instance().isNedcStatus() && DIGModule.instance().isXpuComponent();
    }

    private boolean checkWithNotXpuLoaded() {
        return (isLeftLamp() || isRightLamp()) && isNoRLevel() && isTurnOn() && isInSpeedLimit() && !this.mApDisplay && !this.mCameraDisplay && !this.mNRACtrlDisplay && !CarApiHelper.instance().isNedcStatus() && this.mIsAvmWork;
    }

    public boolean checkWithNotNRA() {
        return (isLeftLamp() || isRightLamp()) && isNoRLevel() && isInSpeedLimit() && isTurnOn() && !this.mApDisplay && !this.mCameraDisplay && this.mIsAvmWork && !CarApiHelper.instance().isNedcStatus() && DIGModule.instance().isXpuComponent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCanShow() {
        if (check()) {
            show();
        } else {
            hide();
        }
    }

    private boolean isLeftLamp() {
        return this.mIsLeftLamp;
    }

    private boolean isRightLamp() {
        return this.mIsRightLamp;
    }

    public boolean isNoRLevel() {
        return !GearLevelEvent.isRLevel(this.mGearLevel);
    }

    public boolean isInSpeedLimit() {
        return this.mSpeed <= 10.0f;
    }

    private boolean checkWithNot(List<StatisticConstants.TurnLampError> errors) {
        boolean z = false;
        boolean z2 = isContains(errors, StatisticConstants.TurnLampError.TURN_LAMP_ERROR) || isLeftLamp() || isRightLamp();
        if (!isContains(errors, StatisticConstants.TurnLampError.R_LEVEL_ERROR)) {
            z2 = z2 && isNoRLevel();
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.SPEED_LIMIT)) {
            z2 = z2 && isInSpeedLimit();
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.TURN_OFF)) {
            z2 = z2 && isTurnOn();
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.AP_DISPLAY)) {
            z2 = z2 && !this.mApDisplay;
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.CAMERA_DISPLAY)) {
            z2 = z2 && !this.mCameraDisplay;
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.NRA_CTRL_DISPLAY)) {
            z2 = z2 && !this.mNRACtrlDisplay;
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.AVM_WORK_ERROR)) {
            z2 = z2 && this.mIsAvmWork;
        }
        if (!isContains(errors, StatisticConstants.TurnLampError.NEDC_ERROR)) {
            z2 = z2 && !CarApiHelper.instance().isNedcStatus();
        }
        if (isContains(errors, StatisticConstants.TurnLampError.XPU_ERROR)) {
            return z2;
        }
        if (z2 && DIGModule.instance().isXpuComponent()) {
            z = true;
        }
        return z;
    }

    private boolean isContains(List<StatisticConstants.TurnLampError> errors, StatisticConstants.TurnLampError error) {
        return errors != null && errors.contains(error);
    }
}
