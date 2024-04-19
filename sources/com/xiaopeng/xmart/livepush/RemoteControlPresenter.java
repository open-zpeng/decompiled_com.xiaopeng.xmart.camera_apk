package com.xiaopeng.xmart.livepush;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.libconfig.ipc.bean.MqttMsgBase;
import com.xiaopeng.libconfig.remotecontrol.CommandItem;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.bean.ControlMsg;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.livepush.utils.SystemPropertiesUtil;
/* loaded from: classes.dex */
public class RemoteControlPresenter implements RemoteControlHandler, SurfaceHolder.Callback, IAvmController.Callback, IMcuController.Callback, ITboxController.Callback {
    private static final int HEIGHT_PUSH_PREVIEW = 1;
    private static final int INTERVAL_TIME_CHECK_WHETHER_CLIENT_IS_LIVING = 20000;
    private static final int INTERVAL_TIME_KEEP_POWER_ON = 30000;
    public static final int OCU_ORIGIN_ANGLE = 180;
    private static final String TAG = "RemoteControlCamera";
    private static final int WIDTH_PUSH_PREVIEW = 1;
    private volatile String mFeedbackMsgId;
    public IAvmController mIAvmController;
    public IMcuController mIMcuController;
    public ITboxController mITboxController;
    private int mIgStatus;
    private boolean mIsClientLiveExit;
    private boolean mIsLaunchedByMobile;
    private boolean mIsSurfaceAvaliable;
    private boolean mIsSurfaceViewAdd;
    private volatile boolean mIsTboxConnected;
    private volatile String mLastMsgIdFromServer;
    private SurfaceView mLiveDisplayPreview;
    private String mLiveGetUrl;
    private String mLivePushUrl;
    private WindowManager.LayoutParams mLiveViewLayoutParams;
    RemoteControlModel mRemoteControlModel;
    private int mRemoteControlState;
    private int mWaitTimes;
    private WindowManager mWindowManager;
    int mCameraType = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
    private boolean mIsRemoteCmdReceived = false;
    private boolean mIsCmdCameraStatusHandled = false;
    boolean mCameraTypeSelected = false;
    private Handler mHandler = new Handler();
    private Runnable mKeepPowerOnRunnable = new Runnable() { // from class: com.xiaopeng.xmart.livepush.RemoteControlPresenter.1
        @Override // java.lang.Runnable
        public void run() {
            CameraLog.d(RemoteControlPresenter.TAG, "mKeepPowerOnRunnable mRemoteControlState:" + RemoteControlPresenter.this.mRemoteControlState, false);
            if (RemoteControlPresenter.this.mRemoteControlState != 1 && RemoteControlPresenter.this.mRemoteControlState != 2) {
                if (RemoteControlPresenter.this.mRemoteControlState != 3 || RemoteControlPresenter.this.mWaitTimes > 1) {
                    return;
                }
                RemoteControlPresenter.access$304(RemoteControlPresenter.this);
                RemoteControlPresenter.this.mHandler.removeCallbacks(RemoteControlPresenter.this.mKeepPowerOnRunnable);
                RemoteControlPresenter.this.mHandler.postDelayed(RemoteControlPresenter.this.mKeepPowerOnRunnable, 30000L);
                return;
            }
            RemoteControlPresenter.this.mIMcuController.setRemoteControlFeedback(1);
            RemoteControlPresenter.this.mHandler.removeCallbacks(RemoteControlPresenter.this.mKeepPowerOnRunnable);
            RemoteControlPresenter.this.mHandler.postDelayed(RemoteControlPresenter.this.mKeepPowerOnRunnable, 30000L);
        }
    };
    private Runnable mCheckClientLivingRunnable = new Runnable() { // from class: com.xiaopeng.xmart.livepush.RemoteControlPresenter.2
        @Override // java.lang.Runnable
        public void run() {
            CameraLog.d(RemoteControlPresenter.TAG, "CheckClientLivingRunnable mIsClientLiveExit=" + RemoteControlPresenter.this.mIsClientLiveExit, false);
            if (!RemoteControlPresenter.this.mIsClientLiveExit) {
                RemoteControlPresenter.this.mHandler.postDelayed(RemoteControlPresenter.this.mCheckClientLivingRunnable, 20000L);
            } else {
                CameraLog.d(RemoteControlPresenter.TAG, "CheckClientLivingRunnable find that client is out of living so stop live", false);
                RemoteControlPresenter.this.mHandler.removeCallbacks(RemoteControlPresenter.this.mCheckToStartLiveRunnable);
                RemoteControlPresenter.this.mRemoteControlModel.stopPush();
                RemoteControlPresenter.this.mRemoteControlModel.stopPreview();
                RemoteControlPresenter.this.mRemoteControlModel.destroyPush();
                RemoteControlPresenter.this.mLivePushUrl = null;
                RemoteControlPresenter.this.mRemoteControlModel.setLiveUrls(null, null);
                RemoteControlPresenter.this.hideCameraPreview();
                RemoteControlPresenter.this.mCameraTypeSelected = false;
                RemoteControlPresenter.this.mIsRemoteCmdReceived = false;
                RemoteControlPresenter.this.mIsCmdCameraStatusHandled = false;
                CarCameraHelper.getInstance().setCameraNavigationTab(101);
                CameraLog.d(RemoteControlPresenter.TAG, "CheckClientLivingRunnable Jenwis powerState close topCamera", false);
                if (CarCameraHelper.getInstance().hasTopCamera() && CarCameraHelper.getInstance().getCarCamera().isUp()) {
                    CameraLog.d(RemoteControlPresenter.TAG, "CheckClientLivingRunnable CarCamera().isUp", false);
                    RemoteControlPresenter.this.fallDownTopCamera();
                } else {
                    RemoteControlPresenter.this.feedbackCameraStatus(0L);
                }
            }
            RemoteControlPresenter.this.mIsClientLiveExit = true;
        }
    };
    private Runnable mCheckToStartLiveRunnable = new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$Vxv3-7WkR3X4En-piAm2cyLP8B4
        @Override // java.lang.Runnable
        public final void run() {
            RemoteControlPresenter.this.lambda$new$1$RemoteControlPresenter();
        }
    };
    private Runnable mFeedbackFallDownTopCameraRunnable = new Runnable() { // from class: com.xiaopeng.xmart.livepush.RemoteControlPresenter.3
        @Override // java.lang.Runnable
        public void run() {
            boolean isUp = CarCameraHelper.getInstance().getCarCamera().isUp();
            long j = isUp ? Config.TOP_CAMERA_ROTATE_TIME : 0L;
            CameraLog.d(RemoteControlPresenter.TAG, "topcamera is up " + isUp + ",feedback falldown topcamera delayTime:" + j, false);
            RemoteControlPresenter.this.mRemoteControlModel.feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), j);
        }
    };

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public void feedbackCameraStatus(String status) {
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.ITboxController.Callback
    public void onCameraRemoteCtrlStatusChanged(String cmd) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    static /* synthetic */ int access$304(RemoteControlPresenter remoteControlPresenter) {
        int i = remoteControlPresenter.mWaitTimes + 1;
        remoteControlPresenter.mWaitTimes = i;
        return i;
    }

    public RemoteControlPresenter(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mRemoteControlModel = new RemoteControlModel(context, this);
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$COwJtLG9G5lwAKoDOvXM58r-eVo
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlPresenter.this.lambda$new$0$RemoteControlPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$new$0$RemoteControlPresenter() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
        }
    }

    public void setRemoteControlling(int status) {
        this.mRemoteControlState = status;
        if (status != 3) {
            this.mWaitTimes = 0;
        } else {
            this.mWaitTimes = 1;
        }
    }

    public /* synthetic */ void lambda$new$1$RemoteControlPresenter() {
        CameraLog.d(TAG, "check to start live,mCameraTypeSelected:" + this.mCameraTypeSelected, false);
        if (this.mCameraTypeSelected) {
            return;
        }
        ControlMsg controlMsg = new ControlMsg();
        controlMsg.setCmdValue(3.0f);
        onLiveOpenOrCloseCmd(controlMsg);
    }

    private void initController() {
        CameraLog.i(TAG, "  initController ", false);
        CarClientWrapper carClientWrapper = CarClientWrapper.getInstance();
        this.mIAvmController = (IAvmController) carClientWrapper.getController(CarClientWrapper.XP_AVM_SERVICE);
        this.mIMcuController = (IMcuController) carClientWrapper.getController(CarClientWrapper.XP_MCU_SERVICE);
        this.mIAvmController.registerCallback(this);
        this.mIMcuController.registerCallback(this);
        try {
            this.mIgStatus = this.mIMcuController.getIgStatusFromMcu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public void requestLiveUrl() {
        this.mRemoteControlModel.requestLiveUrl();
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public void onLiveUrlRequested(String pushUrl, String getUrl) {
        this.mLivePushUrl = pushUrl;
        this.mLiveGetUrl = getUrl;
        this.mRemoteControlModel.setLiveUrls(pushUrl, getUrl);
        this.mRemoteControlModel.startPushAysnc(this.mLivePushUrl);
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public void onPreviewStarted() {
        IAvmController iAvmController;
        CameraLog.d(TAG, "onPreviewStarted!", false);
        if ((CarCameraHelper.getInstance().getCameraNavigationTab() == 100) && this.mCameraTypeSelected && CarCameraHelper.getInstance().is360Camera(this.mCameraType)) {
            if (CarCameraHelper.getInstance().hasAVM() && (iAvmController = this.mIAvmController) != null) {
                try {
                    iAvmController.setCameraDisplayMode(this.mCameraType);
                    CarCameraHelper.getInstance().getCarCamera().set360CameraType(this.mCameraType);
                } catch (Exception e) {
                    e.printStackTrace();
                    CameraLog.d(TAG, "setCameraDisplayMode error:" + e.getMessage(), false);
                }
            } else {
                CarCameraHelper.getInstance().getCarCamera().set360CameraType(this.mCameraType);
            }
        }
        checkClientLiving();
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public boolean isCarServiceConnected() {
        return this.mIsTboxConnected;
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlIpcCommand
    public void onLiveStateCmd(ControlMsg controlMsg) {
        if (!this.mCameraTypeSelected) {
            CarCameraHelper.getInstance().setCameraNavigationTab(101);
        }
        this.mIsLaunchedByMobile = true;
        setRemoteCmdReceived(true);
        setIsCmdCameraStatusHandled(true);
        feedbackCameraStatus(0L);
        checkClientLiving();
    }

    public void setRemoteCmdReceived(boolean remoteCmdReceived) {
        this.mIsRemoteCmdReceived = remoteCmdReceived;
    }

    public boolean isIsRemoteCmdReceived() {
        return this.mIsRemoteCmdReceived;
    }

    public void setIsCmdCameraStatusHandled(boolean isCmdCameraStatusHandled) {
        this.mIsCmdCameraStatusHandled = isCmdCameraStatusHandled;
    }

    public boolean isIsCmdCameraStatusHandled() {
        return this.mIsCmdCameraStatusHandled;
    }

    public int getAVMWorkStatus() {
        try {
            IAvmController iAvmController = this.mIAvmController;
            if (iAvmController != null) {
                return iAvmController.getAVMWorkSt();
            }
            return 0;
        } catch (Exception e) {
            CameraLog.i(TAG, "getAVMWorkStatus e:" + e.getMessage());
            return 0;
        }
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public int getIgStatus() {
        return this.mIgStatus;
    }

    private void showCameraPreview() {
        if (this.mIsSurfaceViewAdd) {
            return;
        }
        if (this.mLiveDisplayPreview == null) {
            this.mLiveDisplayPreview = new SurfaceView(App.getInstance());
        }
        this.mLiveDisplayPreview.getHolder().addCallback(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.mLiveViewLayoutParams = layoutParams;
        layoutParams.type = 2006;
        this.mLiveViewLayoutParams.flags = 40;
        this.mLiveViewLayoutParams.format = 1;
        this.mLiveViewLayoutParams.width = 1;
        this.mLiveViewLayoutParams.height = 1;
        this.mLiveViewLayoutParams.gravity = 17;
        this.mLiveViewLayoutParams.x = 0;
        this.mLiveViewLayoutParams.y = 0;
        this.mWindowManager.addView(this.mLiveDisplayPreview, this.mLiveViewLayoutParams);
        this.mIsSurfaceViewAdd = true;
    }

    protected void hideCameraPreview() {
        SurfaceView surfaceView;
        CameraLog.d(TAG, "hideCameraPreview" + this.mIsSurfaceViewAdd + " mLiveDisplayPreview:" + this.mLiveDisplayPreview, false);
        if (this.mIsSurfaceViewAdd && (surfaceView = this.mLiveDisplayPreview) != null) {
            this.mWindowManager.removeView(surfaceView);
            this.mLiveDisplayPreview = null;
        }
        this.mIsSurfaceViewAdd = false;
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public void feedbackCameraStatus(long delay) {
        this.mRemoteControlModel.feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), delay);
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlHandler
    public void feedbackCameraNotAllowed(long delay) {
        this.mRemoteControlModel.feedBackNotAllowed(delay);
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlIpcCommand
    public void onLiveOpenOrCloseCmd(ControlMsg controlMsg) {
        if (controlMsg != null) {
            int cmdValue = (int) controlMsg.getCmdValue();
            CameraLog.d(TAG, "cmdValue-->" + cmdValue, false);
            this.mIsLaunchedByMobile = true;
            switch (cmdValue) {
                case -1:
                    if (CarCameraHelper.getInstance().hasTopCamera() && CarCameraHelper.getInstance().getCarCamera().isUp()) {
                        fallDownTopCamera();
                    }
                    hideCameraPreview();
                    this.mLivePushUrl = null;
                    this.mCameraTypeSelected = false;
                    this.mIsRemoteCmdReceived = false;
                    this.mIsCmdCameraStatusHandled = false;
                    this.mHandler.removeCallbacks(this.mCheckToStartLiveRunnable);
                    return;
                case 0:
                    if (CarCameraHelper.getInstance().hasTopCamera() && CarCameraHelper.getInstance().getCarCamera().isUp()) {
                        fallDownTopCamera();
                    }
                    hideCameraPreview();
                    this.mLivePushUrl = null;
                    this.mCameraTypeSelected = false;
                    this.mIsRemoteCmdReceived = false;
                    this.mIsCmdCameraStatusHandled = false;
                    this.mHandler.removeCallbacks(this.mCheckToStartLiveRunnable);
                    return;
                case 1:
                    this.mCameraTypeSelected = true;
                    this.mHandler.removeCallbacks(this.mCheckToStartLiveRunnable);
                    if (CarCameraHelper.getInstance().hasTopCamera()) {
                        openTopCamera(controlMsg);
                        return;
                    }
                    return;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    open360Camera(controlMsg);
                    this.mCameraTypeSelected = true;
                    this.mHandler.removeCallbacks(this.mCheckToStartLiveRunnable);
                    return;
                case 11:
                    CameraLog.d(TAG, "REMOTE_CMD_VALUE_REQUEST_LIVE", false);
                    if (TextUtils.isEmpty(this.mLivePushUrl)) {
                        requestLiveUrl();
                    } else {
                        this.mRemoteControlModel.setLiveUrls(this.mLivePushUrl, this.mLiveGetUrl);
                        this.mRemoteControlModel.startPushAysnc(this.mLivePushUrl);
                    }
                    if (this.mCameraTypeSelected) {
                        return;
                    }
                    this.mHandler.postDelayed(this.mCheckToStartLiveRunnable, 8000L);
                    return;
                default:
                    return;
            }
        }
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlIpcCommand
    public void onLiveHeartBeat() {
        checkClientLiving();
    }

    private void checkClientLiving() {
        this.mIsClientLiveExit = false;
        this.mHandler.removeCallbacks(this.mCheckClientLivingRunnable);
        this.mHandler.postDelayed(this.mCheckClientLivingRunnable, 20000L);
    }

    private void open360Camera(final ControlMsg controlMsg) {
        boolean z = CarCameraHelper.getInstance().getCameraNavigationTab() == 100;
        if (!z) {
            this.mRemoteControlModel.stopPush();
            this.mRemoteControlModel.stopPreview();
            this.mRemoteControlModel.destroyPush();
            this.mLivePushUrl = null;
            hideCameraPreview();
            CarCameraHelper.getInstance().setCameraNavigationTab(100);
        }
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$Us6YDme1r6cbTd1gsREq0NSOgiU
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlPresenter.this.lambda$open360Camera$2$RemoteControlPresenter(controlMsg);
            }
        }, z ? 0L : 1500L);
    }

    public /* synthetic */ void lambda$open360Camera$2$RemoteControlPresenter(ControlMsg controlMsg) {
        int cmdValue = (int) controlMsg.getCmdValue();
        CameraLog.d(TAG, "open360Camera cmdValue+" + cmdValue, false);
        this.mCameraType = convertAVMDisplayMode(cmdValue);
        if (CarCameraHelper.getInstance().hasAVM() && this.mIAvmController != null && CarCameraHelper.getInstance().is360Camera(this.mCameraType)) {
            try {
                this.mIAvmController.setCameraDisplayMode(this.mCameraType);
                CarCameraHelper.getInstance().getCarCamera().set360CameraType(this.mCameraType);
            } catch (Exception e) {
                e.printStackTrace();
                CameraLog.d(TAG, "setCameraDisplayMode error:" + e.getMessage(), false);
            }
        } else {
            CarCameraHelper.getInstance().getCarCamera().set360CameraType(this.mCameraType);
        }
        feedbackCameraStatus(0L);
        this.mRemoteControlModel.initPush(2);
        showCameraPreview();
        if (this.mIsSurfaceAvaliable) {
            this.mRemoteControlModel.startPreviewAysnc(this.mLiveDisplayPreview);
            this.mRemoteControlModel.startPushAysnc(this.mLivePushUrl);
        }
    }

    private int convertAVMDisplayMode(int cmdValue) {
        int i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        switch (cmdValue) {
            case 2:
                return CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT;
            case 3:
                return CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
            case 4:
                return CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT;
            case 5:
                return CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT;
            case 6:
                return 4;
            case 7:
                return 5;
            case 8:
                return 6;
            case 9:
                return 7;
            case 10:
                return CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS;
            default:
                return i;
        }
    }

    private void openTopCamera(final ControlMsg controlMsg) {
        CameraLog.d(TAG, "openTopCamera cmdValue+" + ((int) controlMsg.getCmdValue()), false);
        boolean z = CarCameraHelper.getInstance().getCameraNavigationTab() == 101;
        if (!z) {
            this.mRemoteControlModel.stopPush();
            this.mRemoteControlModel.stopPreview();
            this.mRemoteControlModel.destroyPush();
            this.mLivePushUrl = null;
            hideCameraPreview();
            CarCameraHelper.getInstance().setCameraNavigationTab(101);
        }
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$l874nCy8HBpCYR5EP7y9p-F638I
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlPresenter.this.lambda$openTopCamera$3$RemoteControlPresenter(controlMsg);
            }
        }, z ? 0L : 1500L);
        checkClientLiving();
    }

    public /* synthetic */ void lambda$openTopCamera$3$RemoteControlPresenter(ControlMsg controlMsg) {
        riseUpTopCamera();
        CameraLog.d(TAG, "openTopCamera cmdValue+" + ((int) controlMsg.getCmdValue()), false);
        showCameraPreview();
        this.mRemoteControlModel.initPush(3);
        if (this.mIsSurfaceAvaliable) {
            this.mRemoteControlModel.startPreviewAysnc(this.mLiveDisplayPreview);
            this.mRemoteControlModel.startPushAysnc(this.mLivePushUrl);
        }
    }

    private void riseUpTopCamera() {
        CameraLog.d(TAG, "riseUpTopCamera", false);
        if (CarCameraHelper.getInstance().getCarCamera().isUp()) {
            CameraLog.e(TAG, "riseUpTopCamera, top camera is opened, no need open again.", false);
        } else if (this.mIAvmController == null) {
        } else {
            long j = CarCameraHelper.getInstance().getCarCamera().isUp() ? 0L : 7000L;
            try {
                this.mIAvmController.setCameraHeight(true);
                CarCameraHelper.getInstance().getCarCamera().setUp(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mRemoteControlModel.feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), j);
            checkClientLiving();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fallDownTopCamera() {
        CameraLog.d(TAG, "fallDownTopCamera", false);
        if (!CarCameraHelper.getInstance().getCarCamera().isUp()) {
            CameraLog.e(TAG, "fallDownTopCamera, top camera is closed, no need close again.", false);
        } else if (this.mIAvmController == null) {
        } else {
            long j = 0;
            if (CarCameraHelper.getInstance().getCarCamera().isUp()) {
                j = CarCameraHelper.getInstance().getTopCameraRotateTime() + 7000;
                try {
                    this.mIAvmController.setCameraAngle(180);
                    this.mHandler.postDelayed(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$qd9YdpgsPTMYitsPek6VUYF8CNM
                        @Override // java.lang.Runnable
                        public final void run() {
                            RemoteControlPresenter.this.lambda$fallDownTopCamera$4$RemoteControlPresenter();
                        }
                    }, 200L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mHandler.postDelayed(this.mFeedbackFallDownTopCameraRunnable, j);
        }
    }

    public /* synthetic */ void lambda$fallDownTopCamera$4$RemoteControlPresenter() {
        try {
            this.mIAvmController.setCameraHeight(false);
        } catch (Exception e) {
            CameraLog.i(TAG, "CameraHeight ex:" + e.getMessage());
        }
        CarCameraHelper.getInstance().getCarCamera().setUp(false);
    }

    private void rotateTopCamera(ControlMsg controlMsg) {
        double d;
        if (controlMsg != null) {
            float cmdValue = controlMsg.getCmdValue();
            CameraLog.d(TAG, "doRotateTopCamera rate-->" + cmdValue, false);
            if (CarCameraHelper.getInstance().hasTopCamera() && CarCameraHelper.getInstance().getCarCamera().isUp() && this.mIAvmController != null) {
                CameraLog.d(TAG, "targetAngle:" + ((int) Math.abs(CameraDefine.CAMERA_OCU.getTopCameraRotateRange() * d)), false);
                int i = (int) ((cmdValue + 1.0d) * 180.0d);
                if (i > 355) {
                    i = 355;
                } else if (i < 5) {
                    i = 5;
                }
                CameraLog.d(TAG, "rate-->" + cmdValue, false);
                CameraLog.d(TAG, "targetAngle-->" + i, false);
                try {
                    this.mIAvmController.setCameraAngle(i);
                    CarCameraHelper.getInstance().getCarCamera().setAngle(cmdValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mRemoteControlModel.feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
            }
        }
    }

    @Override // com.xiaopeng.xmart.livepush.RemoteControlIpcCommand
    public void onLiveRotateCameraCmd(ControlMsg controlMsg) {
        this.mIsLaunchedByMobile = true;
        rotateTopCamera(controlMsg);
        checkClientLiving();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        CameraLog.e(TAG, "surfaceCreated", false);
        SystemPropertiesUtil.openCoding();
        this.mIsSurfaceAvaliable = true;
        this.mRemoteControlModel.startPreviewAysnc(this.mLiveDisplayPreview);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraLog.e(TAG, "surfaceDestroyed", false);
        SystemPropertiesUtil.closeCoding();
        this.mIsSurfaceAvaliable = false;
        this.mRemoteControlModel.stopPush();
        this.mRemoteControlModel.stopPreview();
        this.mRemoteControlModel.destroyPush();
        this.mLivePushUrl = null;
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IMcuController.Callback
    public void onIgStatusChanged(final int igValue) {
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$eXZzzPLyydFdmIZdNFTGHC-aHHs
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlPresenter.this.lambda$onIgStatusChanged$7$RemoteControlPresenter(igValue);
            }
        });
    }

    public /* synthetic */ void lambda$onIgStatusChanged$7$RemoteControlPresenter(final int i) {
        CameraLog.d(TAG, "onIgStatusChanged: igValue is: " + i + ", mIgStatus is: " + this.mIgStatus, false);
        if (i == 0) {
            CameraLog.d(TAG, "MCU_IG_STATUS_IG_OFF ", false);
            if (this.mIsLaunchedByMobile) {
                exitLivePush();
                ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$IsGP1w4d5Ms9hgrC05f3H2RA_2g
                    @Override // java.lang.Runnable
                    public final void run() {
                        RemoteControlPresenter.this.lambda$onIgStatusChanged$5$RemoteControlPresenter();
                    }
                }, 5000L);
            }
        } else if (i == 1) {
            CameraLog.d(TAG, "MCU_IG_STATUS_LOCAL_IG_ON ", false);
            if (this.mIsLaunchedByMobile) {
                feedbackCameraNotAllowed(0L);
                exitLivePush();
                ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$b_pSAlvcXnMZOCGHnwQkCFgOk6g
                    @Override // java.lang.Runnable
                    public final void run() {
                        RemoteControlPresenter.lambda$onIgStatusChanged$6(i);
                    }
                }, 2000L);
            }
        } else if (i == 2) {
            CameraLog.d(TAG, "MCU_IG_STATUS_REMOTE_IG_ON ", false);
        }
        this.mIgStatus = i;
    }

    public /* synthetic */ void lambda$onIgStatusChanged$5$RemoteControlPresenter() {
        if (getIgStatus() == 0) {
            CameraLog.d(TAG, "Kill self for launching by mobile.", false);
            Process.killProcess(Process.myPid());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onIgStatusChanged$6(int i) {
        if (i == 1) {
            CameraLog.d(TAG, "Kill self for launching by mobile.", false);
            Process.killProcess(Process.myPid());
        }
    }

    private void exitLivePush() {
        CameraLog.d(TAG, "exitLivePush launchedByMobile：" + this.mIsLaunchedByMobile, false);
        SystemPropertiesUtil.closeCoding();
        this.mIsLaunchedByMobile = false;
        this.mHandler.removeCallbacks(this.mCheckToStartLiveRunnable);
        this.mRemoteControlModel.stopPush();
        this.mRemoteControlModel.stopPreview();
        this.mRemoteControlModel.destroyPush();
        hideCameraPreview();
        this.mLivePushUrl = null;
        this.mCameraTypeSelected = false;
        this.mIsRemoteCmdReceived = false;
        this.mIsCmdCameraStatusHandled = false;
        CarCameraHelper.getInstance().setCameraNavigationTab(101);
        feedbackCameraStatus(0L);
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController.Callback
    public void onAvmWorkStChanged(final int avmWorkSt) {
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlPresenter$tzZetHvzpfAN0BZxjVoZ5vfi2lc
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlPresenter.this.lambda$onAvmWorkStChanged$8$RemoteControlPresenter(avmWorkSt);
            }
        });
    }

    public /* synthetic */ void lambda$onAvmWorkStChanged$8$RemoteControlPresenter(int i) {
        CameraLog.d(TAG, "Receive AVMWorkStEventMsg avmWorkSt: " + i + " mIsCmdCameraStatusHandled:" + this.mIsCmdCameraStatusHandled, false);
        if (this.mIsRemoteCmdReceived) {
            if ((i == 1 || i == 3) && !this.mIsCmdCameraStatusHandled) {
                CameraLog.d(TAG, "last command live status is not handled,handle it after avm actived.", false);
                feedbackCameraStatus(0L);
            }
        }
    }

    private /* synthetic */ void lambda$onCameraRemoteCtrlStatusChanged$9(String str) {
        CameraLog.e(TAG, "onCameraRemoteCtrlStatusChanged,msg:" + str, false);
        if (isCarServiceConnected() && !TextUtils.isEmpty(str) && getIgStatus() == 2) {
            doRemoteControlCommand(str);
        }
    }

    public String getFeedbackMsgId() {
        return this.mFeedbackMsgId;
    }

    public void setFeedbackMsgId(String FeedbackMsgId) {
        this.mFeedbackMsgId = FeedbackMsgId;
    }

    public void doRemoteControlCommand(String cmd) {
        MqttMsgBase mqttMsgBase = (MqttMsgBase) new Gson().fromJson(cmd, new TypeToken<MqttMsgBase<CommandItem>>() { // from class: com.xiaopeng.xmart.livepush.RemoteControlPresenter.4
        }.getType());
        String msgId = mqttMsgBase.getMsgId();
        CameraLog.d(TAG, "msgId: " + msgId, false);
        if (msgId.equals(this.mLastMsgIdFromServer)) {
            return;
        }
        int msgType = mqttMsgBase.getMsgType();
        int serviceType = mqttMsgBase.getServiceType();
        CameraLog.d(TAG, "msgType: " + msgType, false);
        CameraLog.d(TAG, "serviceType: " + serviceType, false);
        this.mLastMsgIdFromServer = msgId;
        this.mFeedbackMsgId = msgId;
        if (msgType == 4 && serviceType == 11) {
            CameraLog.d(TAG, "receive heart beat msg, set client live exit flase", false);
            onLiveHeartBeat();
            return;
        }
        CommandItem commandItem = (CommandItem) mqttMsgBase.getMsgContent();
        CameraLog.d(TAG, "commandItem: " + commandItem.toString(), false);
        int cmd_type = commandItem.getCmd_type();
        CameraLog.d(TAG, "cmdType: " + cmd_type, false);
        if (serviceType == 11) {
            ControlMsg controlMsg = new ControlMsg();
            controlMsg.setCmdValue(commandItem.getCmd_value());
            if (cmd_type == 1) {
                CameraLog.d(TAG, "RemoteControlConfig.REMOTE_COMMAND_TYPE_LIVE_STATE", false);
                CarCameraHelper.getInstance().hasTopCamera();
                onLiveStateCmd(controlMsg);
            } else if (cmd_type == 2) {
                CameraLog.d(TAG, "RemoteControlConfig.REMOTE_COMMAND_TYPE_LIVE_OPEN_OR_CLOSE", false);
                onLiveOpenOrCloseCmd(controlMsg);
            } else if (cmd_type != 3) {
            } else {
                CameraLog.d(TAG, "RemoteControlConfig.REMOTE_COMMAND_TYPE_LIVE_MOVE", false);
                onLiveRotateCameraCmd(controlMsg);
            }
        }
    }
}
