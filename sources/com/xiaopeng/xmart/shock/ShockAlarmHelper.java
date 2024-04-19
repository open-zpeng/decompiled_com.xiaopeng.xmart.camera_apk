package com.xiaopeng.xmart.shock;

import android.hardware.Camera;
import android.os.PowerManager;
import android.os.Process;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.BIHelper;
import com.xiaopeng.xmart.camera.model.camera.StartRecordMode;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.shock.ShockAlarmHelper;
import com.xiaopeng.xmart.shock.vm.ShockViewModel;
/* loaded from: classes2.dex */
public class ShockAlarmHelper implements LifecycleOwner {
    private static final String TAG = "ShockAlarmHelper";
    private volatile boolean isPreviewShow;
    private long mGuardMsgLastReceivedTime;
    private LifecycleRegistry mLifecycleRegistry;
    private SurfaceView mPreviewView;
    private ShockViewModel mShockViewModel;
    private Runnable mStopShockVideoTask;
    private PowerManager.WakeLock mWakeLock;
    private WindowManager mWindowManager;

    /* synthetic */ ShockAlarmHelper(AnonymousClass1 anonymousClass1) {
        this();
    }

    public static ShockAlarmHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    /* loaded from: classes2.dex */
    private static class SingletonHolder {
        static ShockAlarmHelper sInstance = new ShockAlarmHelper(null);

        private SingletonHolder() {
        }
    }

    private ShockAlarmHelper() {
        this.isPreviewShow = false;
        this.mStopShockVideoTask = new Runnable() { // from class: com.xiaopeng.xmart.shock.ShockAlarmHelper.2
            @Override // java.lang.Runnable
            public void run() {
                if (ShockAlarmHelper.this.mShockViewModel.isRecording()) {
                    ShockAlarmHelper.this.mShockViewModel.stopRecord();
                }
            }
        };
    }

    public void init() {
        if (this.mShockViewModel == null) {
            this.mShockViewModel = new ShockViewModel();
            this.mLifecycleRegistry = new LifecycleRegistry(this);
            ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$bYy-yxn1NRMniFRxiHHKk4vayb0
                @Override // java.lang.Runnable
                public final void run() {
                    ShockAlarmHelper.this.lambda$init$0$ShockAlarmHelper();
                }
            });
        }
    }

    public /* synthetic */ void lambda$init$0$ShockAlarmHelper() {
        CameraLog.i(TAG, "init", false);
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        initObserve();
    }

    private void initObserve() {
        this.mShockViewModel.getTBoxGuard().observe(this, new Observer() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$0ZONGK4Ed-izrJmoZSCpsEKOs48
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ShockAlarmHelper.this.lambda$initObserve$2$ShockAlarmHelper((Integer[]) obj);
            }
        });
        this.mShockViewModel.getAvmWorkSt().observe(this, new Observer() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$75hd_tZlIsCT6zy7N53t5tEEuDE
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ShockAlarmHelper.this.lambda$initObserve$3$ShockAlarmHelper((Integer) obj);
            }
        });
        this.mShockViewModel.getCameraOpen().observe(this, new Observer() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$kIfPzyt4AV5kPwiAd5oYGuQMROQ
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ShockAlarmHelper.this.lambda$initObserve$4$ShockAlarmHelper((Boolean) obj);
            }
        });
        this.mShockViewModel.getCameraPreview().observe(this, new Observer() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$9GXzbvqOWfvnMskwM4QQNladrCg
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ShockAlarmHelper.this.lambda$initObserve$5$ShockAlarmHelper((Boolean) obj);
            }
        });
        this.mShockViewModel.getStartRecordState().observe(this, new Observer() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$dmEEv4pga5-5os9H_CFh0RHxWEc
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ShockAlarmHelper.this.lambda$initObserve$6$ShockAlarmHelper((StartRecordMode) obj);
            }
        });
        this.mShockViewModel.getLocalIgOn().observe(this, new Observer() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$oN109VnXqXUXYr-lO8s-oPf84tY
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ShockAlarmHelper.this.lambda$initObserve$8$ShockAlarmHelper((Boolean) obj);
            }
        });
    }

    public /* synthetic */ void lambda$initObserve$2$ShockAlarmHelper(Integer[] numArr) {
        CameraLog.d(TAG, "getTBoxGuard, mIgStatus is " + this.mShockViewModel.getIgStatus() + ",state:" + numArr, false);
        long currentTimeMillis = System.currentTimeMillis();
        this.mShockViewModel.resetShockModel();
        if (currentTimeMillis - this.mGuardMsgLastReceivedTime > 300000) {
            this.mGuardMsgLastReceivedTime = currentTimeMillis;
            ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$6BQj6M4agKxFpy2UtIspPnMfjjY
                @Override // java.lang.Runnable
                public final void run() {
                    ShockAlarmHelper.this.lambda$initObserve$1$ShockAlarmHelper();
                }
            }, 1000L);
        }
    }

    public /* synthetic */ void lambda$initObserve$1$ShockAlarmHelper() {
        if (this.mShockViewModel.getIgStatus() == 1) {
            CameraLog.d(TAG, "mIgStatus is local on, Return", false);
        } else if (this.mShockViewModel.isShockProcessing()) {
            CameraLog.d(TAG, "getTBoxGuard, but already in Shock Process", false);
        } else {
            CameraLog.d(TAG, "getTBoxGuard, and enter ShockProcess", false);
            enterShockProcess();
        }
    }

    public /* synthetic */ void lambda$initObserve$3$ShockAlarmHelper(Integer num) {
        boolean isShockProcessing = this.mShockViewModel.isShockProcessing();
        CameraLog.d(TAG, "getAvmWorkSt avmWorkSt:" + num + ",mIsShockProcessing:" + isShockProcessing + ",isPreviewShow:" + this.isPreviewShow, false);
        if (isShockProcessing) {
            if ((num.intValue() == 3 || num.intValue() == 1) && !this.isPreviewShow) {
                CameraLog.d(TAG, "avmWorkSt:" + num + ",show Preview View", false);
                this.mShockViewModel.openCamera();
            }
        }
    }

    public /* synthetic */ void lambda$initObserve$4$ShockAlarmHelper(Boolean bool) {
        CameraLog.d(TAG, "getCameraOpen, cameraOpen is:" + bool, false);
        if (bool.booleanValue()) {
            showPreviewView();
        } else {
            hidePreView();
        }
    }

    public /* synthetic */ void lambda$initObserve$5$ShockAlarmHelper(Boolean bool) {
        CameraLog.i(TAG, "---CameraPreview:" + bool, false);
        if (bool.booleanValue()) {
            excuteCollisionShoot();
            return;
        }
        this.mShockViewModel.onShockEnd();
        releaseCameraAndHidePreviewView();
    }

    public /* synthetic */ void lambda$initObserve$6$ShockAlarmHelper(StartRecordMode startRecordMode) {
        CameraLog.i(TAG, "---getStartRecordState:" + startRecordMode, false);
        if (startRecordMode == StartRecordMode.End || startRecordMode == StartRecordMode.Fail) {
            ThreadPoolHelper.getInstance().removeCallbacksOnMainThread(this.mStopShockVideoTask);
            releaseCameraAndHidePreviewView();
        }
    }

    public /* synthetic */ void lambda$initObserve$8$ShockAlarmHelper(Boolean bool) {
        if (this.mShockViewModel.isShockProcessing() && bool.booleanValue()) {
            CameraLog.d(TAG, "getLocalIgOn isShockProcessing ", false);
            releaseCameraAndHidePreviewView();
            ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$ifu848RssAf1Krxvr7qKc5tKePs
                @Override // java.lang.Runnable
                public final void run() {
                    ShockAlarmHelper.lambda$initObserve$7();
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$initObserve$7() {
        CameraLog.d(TAG, "Kill self for launching by ShockAlarm.", false);
        Process.killProcess(Process.myPid());
    }

    public boolean isCarServiceConnected() {
        return this.mShockViewModel.isCarServiceConnected();
    }

    public void waitForCarSrvConnected() {
        this.mShockViewModel.waitForCarSrvConnected();
    }

    public void enterShockProcess() {
        if (this.mShockViewModel == null) {
            init();
        }
        CameraLog.d(TAG, "enterShockProcess, mIsShockProcessing: " + this.mShockViewModel.isShockProcessing(), false);
        if (this.mShockViewModel.isShockProcessing()) {
            CameraLog.d(TAG, "in shock process and return", false);
        } else {
            this.mShockViewModel.enterShockProcess();
        }
    }

    private void showPreviewView() {
        CameraLog.d(TAG, "showPreviewView, isPreviewShow is: " + this.isPreviewShow);
        if (this.isPreviewShow) {
            return;
        }
        if (this.mPreviewView == null) {
            this.mPreviewView = new SurfaceView(App.getInstance());
        }
        CameraLog.d(TAG, "showPreviewView ", false);
        this.mPreviewView.getHolder().addCallback(new AnonymousClass1());
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = 2006;
        layoutParams.flags = 40;
        layoutParams.format = 4;
        layoutParams.width = 1;
        layoutParams.height = 1;
        layoutParams.gravity = 17;
        if (this.mWindowManager == null) {
            this.mWindowManager = (WindowManager) App.getInstance().getSystemService("window");
        }
        this.mWindowManager.addView(this.mPreviewView, layoutParams);
        this.isPreviewShow = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.shock.ShockAlarmHelper$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements SurfaceHolder.Callback {
        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        AnonymousClass1() {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder holder) {
            CameraLog.i(ShockAlarmHelper.TAG, "---onPreviewCreated", false);
            int numberOfCameras = Camera.getNumberOfCameras();
            CameraLog.i(ShockAlarmHelper.TAG, "camera num:" + numberOfCameras);
            if (numberOfCameras > 0) {
                ThreadPoolHelper.getInstance().executeForLongTask(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$1$ToMKH5Ci6vSimYs1u_ZcopQpW30
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShockAlarmHelper.AnonymousClass1.this.lambda$surfaceCreated$0$ShockAlarmHelper$1();
                    }
                });
            }
        }

        public /* synthetic */ void lambda$surfaceCreated$0$ShockAlarmHelper$1() {
            ShockAlarmHelper.this.acquireWakeLock();
            ShockAlarmHelper.this.startPreview();
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder holder) {
            CameraLog.i(ShockAlarmHelper.TAG, "---surfaceDestroyed", false);
            ShockAlarmHelper.this.releaseWakeLock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPreview() {
        CameraLog.i(TAG, "startPreview ", false);
        this.mShockViewModel.startPreview(this.mPreviewView.getHolder());
        CameraLog.i(TAG, "Camera startPreview success");
    }

    private void hidePreView() {
        SurfaceView surfaceView;
        CameraLog.d(TAG, "hidePreView", false);
        if (this.isPreviewShow) {
            WindowManager windowManager = this.mWindowManager;
            if (windowManager != null && (surfaceView = this.mPreviewView) != null) {
                windowManager.removeView(surfaceView);
            }
            this.mPreviewView = null;
            this.isPreviewShow = false;
        }
    }

    private void excuteCollisionShoot() {
        CameraLog.d(TAG, "excuteCollisionShoot", false);
        if (!ShockViewModel.isSupportVideoMasking()) {
            takeCollisionPic();
        }
        takeCollisionVideo();
    }

    private void takeCollisionPic() {
        CameraLog.d(TAG, "excuteCollisionShoot, takeCollisionPic", false);
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$Di2e3m6bNo7vVUqxB7pk8D3wrCc
            @Override // java.lang.Runnable
            public final void run() {
                ShockAlarmHelper.this.lambda$takeCollisionPic$9$ShockAlarmHelper();
            }
        }, 100L);
    }

    public /* synthetic */ void lambda$takeCollisionPic$9$ShockAlarmHelper() {
        BIHelper.getInstance().uploadShockTakePic();
        this.mShockViewModel.takePicture();
    }

    private void takeCollisionVideo() {
        CameraLog.d(TAG, "excuteCollisionShoot, takeCollisionVideo", false);
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$yE205oMf_IdshD5mb_BL1ccYnqk
            @Override // java.lang.Runnable
            public final void run() {
                ShockAlarmHelper.this.lambda$takeCollisionVideo$10$ShockAlarmHelper();
            }
        }, 300L);
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(this.mStopShockVideoTask, 16300L);
    }

    public /* synthetic */ void lambda$takeCollisionVideo$10$ShockAlarmHelper() {
        ThreadPoolHelper.getInstance().removeCallbacksOnMainThread(this.mStopShockVideoTask);
        this.mShockViewModel.startRecord();
        if (CameraDefine.isAVMSupport4Camera()) {
            return;
        }
        this.mShockViewModel.shockAvmDisplayChange();
    }

    private void releaseCameraAndHidePreviewView() {
        CameraLog.d(TAG, "releaseCameraAndHidePreviewView  ", false);
        releaseCamera();
        hidePreView();
    }

    private void releaseCamera() {
        boolean isRecording = this.mShockViewModel.isRecording();
        CameraLog.d(TAG, "releaseCamera isRecording:" + isRecording, false);
        if (isRecording) {
            ThreadPoolHelper.getInstance().removeCallbacksOnMainThread(this.mStopShockVideoTask);
            this.mShockViewModel.stopRecord();
        }
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.shock.-$$Lambda$ShockAlarmHelper$hWMMGi2AJTeCvVuIGb_dUZ0ITjs
            @Override // java.lang.Runnable
            public final void run() {
                ShockAlarmHelper.this.lambda$releaseCamera$11$ShockAlarmHelper();
            }
        }, isRecording ? 300L : 0L);
    }

    public /* synthetic */ void lambda$releaseCamera$11$ShockAlarmHelper() {
        CameraLog.d(TAG, "releaseCamera  closeCamera", false);
        this.mShockViewModel.stopPreview();
        this.mShockViewModel.closeCamera();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acquireWakeLock() {
        if (this.mWakeLock == null) {
            try {
                PowerManager.WakeLock newWakeLock = ((PowerManager) App.getInstance().getSystemService("power")).newWakeLock(6, "360CameraApp:Shock Mode");
                this.mWakeLock = newWakeLock;
                newWakeLock.setReferenceCounted(false);
                this.mWakeLock.acquire();
                CameraLog.d(TAG, "acquire wakelock", false);
            } catch (Exception e) {
                CameraLog.d(TAG, "acquire wakelock exception:" + e.getMessage(), false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            try {
                wakeLock.release();
                this.mWakeLock = null;
                CameraLog.d(TAG, "release wakelock", false);
            } catch (Exception e) {
                CameraLog.d(TAG, "release wakelock exception:" + e.getMessage(), false);
            }
        }
    }
}
