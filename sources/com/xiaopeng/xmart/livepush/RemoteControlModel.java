package com.xiaopeng.xmart.livepush;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.SurfaceView;
import com.alivc.live.pusher.AlivcEncodeModeEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushCameraTypeEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushError;
import com.alivc.live.pusher.AlivcLivePushErrorListener;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePushNetworkListener;
import com.alivc.live.pusher.AlivcLivePushStats;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewRotationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;
import com.alivc.live.pusher.AlivcResolutionEnum;
import com.alivc.live.pusher.LogUtil;
import com.google.gson.Gson;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.lib.utils.config.RemoteControlConfig;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.bean.CameraFeedback;
import com.xiaopeng.xmart.camera.bean.CarCamera;
import com.xiaopeng.xmart.camera.bean.FeedbackReq;
import com.xiaopeng.xmart.camera.bean.msgnotice.CodeFeedBack;
import com.xiaopeng.xmart.camera.carmanager.Component;
import com.xiaopeng.xmart.camera.define.BIConfig;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.livepush.RemoteControlModel;
import com.xiaopeng.xmart.livepush.bean.LiveUrlRes;
import com.xiaopeng.xmart.livepush.utils.NetPingUtil;
import com.xiaopeng.xmart.livepush.utils.PrintResponseUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class RemoteControlModel implements AlivcLivePushInfoListener, AlivcLivePushErrorListener, AlivcLivePushNetworkListener {
    public static final int LIVE_PREVIEW_HEIGHT = 720;
    public static final int LIVE_PREVIEW_WIDTH = 1280;
    private static final String TAG = "RemoteControlCamera";
    private AlivcLivePushConfig mAlivcLivePushConfig;
    private Context mContext;
    private String mLiveGetUrl;
    private int mLivePushStatus;
    private String mLivePushUrl;
    private Handler mPusherHandler;
    private HandlerThread mPusherThread;
    private RemoteControlPresenter mRemoteControlPresenter;
    private long mStartLiveTime;
    private boolean mIsStartPreviewCalled = false;
    private boolean mIsStarPushCalled = false;
    private AlivcLivePusher mAlivcLivePusher = null;
    private Handler mHandler = new Handler();

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onAdjustBitRate(AlivcLivePusher alivcLivePusher, int i, int i1) {
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onAdjustFps(AlivcLivePusher alivcLivePusher, int i, int i1) {
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onDropFrame(AlivcLivePusher alivcLivePusher, int i, int i1) {
    }

    public RemoteControlModel(Context context, RemoteControlPresenter remoteControlPresenter) {
        this.mContext = context;
        this.mRemoteControlPresenter = remoteControlPresenter;
        HandlerThread handlerThread = new HandlerThread("pusher-thread");
        this.mPusherThread = handlerThread;
        handlerThread.start();
        this.mPusherHandler = new Handler(this.mPusherThread.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initPush(final int cameraId) {
        CameraLog.d(TAG, "initLiveAndPlay", false);
        this.mPusherHandler.post(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$Oj0m-P0Ex8Qs5-lTHsIe7wuYaps
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$initPush$0$RemoteControlModel(cameraId);
            }
        });
    }

    public /* synthetic */ void lambda$initPush$0$RemoteControlModel(int i) {
        if (this.mAlivcLivePusher != null) {
            CameraLog.d(TAG, "Warn:initLiveAndPlay LivePusher is not null!", false);
            return;
        }
        CameraLog.d(TAG, "new LivePusher", false);
        this.mIsStartPreviewCalled = false;
        this.mIsStarPushCalled = false;
        this.mAlivcLivePusher = new AlivcLivePusher();
        setLiveCommonConfig(i);
        try {
            CameraLog.d(TAG, "LivePusher init", false);
            this.mAlivcLivePusher.init(App.getInstance(), this.mAlivcLivePushConfig);
        } catch (Exception e) {
            e.printStackTrace();
            this.mAlivcLivePusher = null;
            CameraLog.d(TAG, "LivePusher init exception:" + e.getMessage(), false);
        }
    }

    private void setLiveCommonConfig(int cameraId) {
        CameraLog.d(TAG, "setLiveCommonConfig", false);
        LogUtil.enalbeDebug();
        AlivcLivePushConfig alivcLivePushConfig = new AlivcLivePushConfig();
        this.mAlivcLivePushConfig = alivcLivePushConfig;
        alivcLivePushConfig.setVideoEncodeMode(AlivcEncodeModeEnum.Encode_MODE_HARD);
        this.mAlivcLivePushConfig.setVideoOnly(true);
        this.mAlivcLivePushConfig.setFps(AlivcFpsEnum.FPS_20);
        this.mAlivcLivePushConfig.setMinFps(AlivcFpsEnum.FPS_15);
        this.mAlivcLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
        this.mAlivcLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_SCALE_FILL);
        this.mAlivcLivePushConfig.setPreviewRotation(AlivcPreviewRotationEnum.ROTATION_360);
        this.mAlivcLivePushConfig.setCameraType(AlivcLivePushCameraTypeEnum.values()[cameraId]);
        this.mAlivcLivePushConfig.setPreviewMirror(true);
        this.mAlivcLivePushConfig.setPushMirror(true);
        this.mAlivcLivePushConfig.setBeautyOn(false);
        AlivcResolutionEnum alivcResolutionEnum = AlivcResolutionEnum.RESOLUTION_SELFDEFINE;
        alivcResolutionEnum.setSelfDefineResolution(1280, 720);
        this.mAlivcLivePushConfig.setResolution(alivcResolutionEnum);
        this.mAlivcLivePusher.setLivePushInfoListener(this);
        this.mAlivcLivePusher.setLivePushErrorListener(this);
        this.mAlivcLivePusher.setLivePushNetworkListener(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startPreviewAysnc(final SurfaceView liveSurfaceView) {
        this.mPusherHandler.post(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$ZuDb9ypr8Z_jh1gPp5nT5IY_1Q0
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$startPreviewAysnc$1$RemoteControlModel(liveSurfaceView);
            }
        });
    }

    public /* synthetic */ void lambda$startPreviewAysnc$1$RemoteControlModel(SurfaceView surfaceView) {
        AlivcLivePusher alivcLivePusher;
        if (this.mIsStartPreviewCalled || (alivcLivePusher = this.mAlivcLivePusher) == null || alivcLivePusher.getCurrentStatus() != AlivcLivePushStats.INIT) {
            return;
        }
        try {
            CameraLog.d(TAG, "LivePusher startPreviewAysnc", false);
            this.mAlivcLivePusher.startPreviewAysnc(surfaceView);
            this.mIsStartPreviewCalled = true;
        } catch (Exception e) {
            CameraLog.d(TAG, "LivePusher startPreviewAysnc exception:" + e.getMessage(), false);
            this.mIsStartPreviewCalled = false;
            try {
                this.mAlivcLivePusher.stopPreview();
            } catch (Exception e2) {
                CameraLog.d(TAG, "LivePusher stopPreview exception:" + e2.getMessage(), false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPreview() {
        this.mPusherHandler.post(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$DPvC1vBNsncwo2BpiPayUa5qTGI
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$stopPreview$2$RemoteControlModel();
            }
        });
    }

    public /* synthetic */ void lambda$stopPreview$2$RemoteControlModel() {
        try {
            try {
                CameraLog.d(TAG, "LivePusher stopPreview", false);
                if (this.mIsStartPreviewCalled) {
                    this.mAlivcLivePusher.stopPreview();
                }
            } catch (Exception e) {
                CameraLog.d(TAG, "LivePusher stopPreview exception:" + e.getMessage(), false);
            }
        } finally {
            this.mIsStartPreviewCalled = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLiveUrls(String pushUrl, String getUrl) {
        this.mLivePushUrl = pushUrl;
        this.mLiveGetUrl = getUrl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startPushAysnc(final String rtmpLiveUrl) {
        CameraLog.d(TAG, "Start living! rtmpLiveUrl-->" + rtmpLiveUrl, false);
        this.mPusherHandler.post(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$c2wnYZhMErFMM6JbBVlU6gD1al4
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$startPushAysnc$4$RemoteControlModel(rtmpLiveUrl);
            }
        });
    }

    public /* synthetic */ void lambda$startPushAysnc$4$RemoteControlModel(String str) {
        if (this.mAlivcLivePusher == null || TextUtils.isEmpty(str)) {
            return;
        }
        CameraLog.d(TAG, "LivePusher currentStatus-->" + this.mAlivcLivePusher.getCurrentStatus(), false);
        CameraLog.d(TAG, "mIsStarPushCalled--> " + this.mIsStarPushCalled + " mIsStartPreviewCalled-->" + this.mIsStartPreviewCalled, false);
        if (this.mAlivcLivePusher.getCurrentStatus() != AlivcLivePushStats.PREVIEWED || TextUtils.isEmpty(this.mLivePushUrl) || !this.mIsStartPreviewCalled || this.mIsStarPushCalled) {
            return;
        }
        try {
            this.mIsStarPushCalled = true;
            this.mHandler.postDelayed(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$iipo7OHJBHDuZspPQDsEaPxBoX8
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteControlModel.this.lambda$startPushAysnc$3$RemoteControlModel();
                }
            }, 1000L);
        } catch (Exception e) {
            CameraLog.d(TAG, "LivePusher startPushAysnc exception:" + e.getMessage(), false);
            this.mIsStarPushCalled = false;
        }
    }

    public /* synthetic */ void lambda$startPushAysnc$3$RemoteControlModel() {
        CameraLog.d(TAG, "LivePusher startPushAysnc--->" + this.mLivePushUrl, false);
        if (this.mAlivcLivePusher == null || TextUtils.isEmpty(this.mLivePushUrl)) {
            return;
        }
        this.mAlivcLivePusher.startPushAysnc(this.mLivePushUrl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPush() {
        CameraLog.d(TAG, "liveOnStop", false);
        this.mPusherHandler.post(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$QF0Prkrr06NUr6kAQflDstqERfs
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$stopPush$5$RemoteControlModel();
            }
        });
    }

    public /* synthetic */ void lambda$stopPush$5$RemoteControlModel() {
        AlivcLivePusher alivcLivePusher = this.mAlivcLivePusher;
        if (alivcLivePusher != null && this.mIsStarPushCalled && alivcLivePusher.isPushing()) {
            try {
                try {
                    CameraLog.d(TAG, "LivePusher stopPush", false);
                    this.mAlivcLivePusher.stopPush();
                } catch (Exception e) {
                    CameraLog.d(TAG, "LivePusher stopPush exception:" + e.getMessage(), false);
                }
            } finally {
                this.mIsStarPushCalled = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyPush() {
        CameraLog.d(TAG, "liveOnDestroy", false);
        this.mPusherHandler.post(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$N4np7d_qu457i9GKvGOXDfrBaaA
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$destroyPush$6$RemoteControlModel();
            }
        });
    }

    public /* synthetic */ void lambda$destroyPush$6$RemoteControlModel() {
        this.mLivePushStatus = 0;
        this.mLiveGetUrl = null;
        if (this.mAlivcLivePusher != null) {
            setLiveUrls(null, null);
            try {
                CameraLog.d(TAG, "LivePusher destroy", false);
                this.mAlivcLivePusher.destroy();
            } catch (Exception e) {
                CameraLog.d(TAG, "LivePusher destroy exception:" + e.getMessage(), false);
            }
            this.mAlivcLivePusher.setLivePushInfoListener(null);
            this.mAlivcLivePusher = null;
            if (this.mStartLiveTime > 0) {
                CameraLog.d(TAG, "Start collect network flow.", false);
                long uptimeMillis = SystemClock.uptimeMillis();
                IDataLog dataLogService = Component.getInstance().getDataLogService();
                dataLogService.sendStatData(dataLogService.buildStat().setEventName(BIConfig.EVENT_CAMERA).setProperty(AccountConfig.FaceIDRegisterAction.STATUS_START, Long.valueOf(this.mStartLiveTime)).setProperty("end", Long.valueOf(uptimeMillis)).setProperty("cost", Long.valueOf(uptimeMillis - this.mStartLiveTime)).build());
            }
            this.mStartLiveTime = 0L;
        }
    }

    public void requestLiveUrl() {
        CameraLog.d(TAG, "requestLiveUrl", false);
        Component.getInstance().getHttp().bizHelper().post(Config.HOST + Config.HTTP_URL_LIVE_URL, "{}").build().execute(new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.livepush.RemoteControlModel$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Callback {
        AnonymousClass1() {
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
        public void onSuccess(IResponse iResponse) {
            if (iResponse.code() == 200) {
                PrintResponseUtil.printLiveRequestLog(iResponse, true);
                try {
                    final LiveUrlRes liveUrlRes = (LiveUrlRes) new Gson().fromJson(iResponse.body(), (Class<Object>) LiveUrlRes.class);
                    CameraLog.d(RemoteControlModel.TAG, "Live push stream url: " + liveUrlRes.getData().getPush_stream_url(), false);
                    ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$1$jQsv4D_AV8yb8okWwr7Alo7_xx4
                        @Override // java.lang.Runnable
                        public final void run() {
                            RemoteControlModel.AnonymousClass1.this.lambda$onSuccess$0$RemoteControlModel$1(liveUrlRes);
                        }
                    });
                    return;
                } catch (Exception unused) {
                    return;
                }
            }
            PrintResponseUtil.printLiveRequestLog(iResponse, false);
        }

        public /* synthetic */ void lambda$onSuccess$0$RemoteControlModel$1(LiveUrlRes liveUrlRes) {
            RemoteControlModel.this.mRemoteControlPresenter.onLiveUrlRequested(liveUrlRes.getData().getPush_stream_url(), liveUrlRes.getData().getLive_url());
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
        public void onFailure(IResponse iResponse) {
            PrintResponseUtil.printLiveRequestLog(iResponse, false);
        }
    }

    public void feedBackCameraSt(final CarCamera camera, final long delayTime) {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$jjmUlFsnmK2vFuPeHSFMHlqSg4E
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$feedBackCameraSt$7$RemoteControlModel(delayTime, camera);
            }
        });
    }

    public /* synthetic */ void lambda$feedBackCameraSt$7$RemoteControlModel(long j, CarCamera carCamera) {
        try {
            CameraLog.d(TAG, "notifyClient delayTime=" + j, false);
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FeedbackReq feedbackReq = new FeedbackReq();
        StringBuilder sb = new StringBuilder();
        sb.append(20).append(SystemPropertyUtil.getVehicleId()).append(System.currentTimeMillis()).append((int) (Math.random() * 10.0d)).append((int) (Math.random() * 10.0d));
        feedbackReq.setMsg_id(sb.toString());
        String msgIdFromServer = RemoteControlIPCHelper.getInstance().getMsgIdFromServer();
        if (!TextUtils.isEmpty(msgIdFromServer)) {
            feedbackReq.setMsg_ref(msgIdFromServer);
            CameraLog.d(TAG, "feedbackReq msgRef:" + msgIdFromServer, false);
            RemoteControlIPCHelper.getInstance().setMsgIdFromServer(null);
        }
        feedbackReq.setMsg_type(1);
        feedbackReq.setService_type(11);
        CameraFeedback cameraFeedback = new CameraFeedback();
        cameraFeedback.setCameraTop(carCamera.getCameraTop());
        cameraFeedback.setCamera360(carCamera.getCamera360());
        cameraFeedback.setCamera_degree(carCamera.getAngle());
        cameraFeedback.setCamera_type(CarCameraHelper.getInstance().getCameraTypeForFeedbackToMobileApp());
        cameraFeedback.setCamera_live_url(this.mLiveGetUrl);
        cameraFeedback.setLive_push_status(this.mLivePushStatus);
        feedbackReq.setMsg_content(cameraFeedback);
        String json = new Gson().toJson(feedbackReq);
        Intent intent = new Intent();
        intent.setAction(RemoteControlConfig.ACTION_XMART_FEEDBACK);
        intent.putExtra(RemoteControlConfig.ACTION_XMART_FEEDBACK_VALUE, json);
        this.mContext.sendBroadcast(intent);
        CameraLog.d(TAG, "notifyClient end with msg-->" + json, false);
    }

    public void feedBackNotAllowed(final long delayTime) {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$RpsxbWej0wr2Y63okrKruUB0t6o
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.this.lambda$feedBackNotAllowed$8$RemoteControlModel(delayTime);
            }
        });
    }

    public /* synthetic */ void lambda$feedBackNotAllowed$8$RemoteControlModel(long j) {
        try {
            CameraLog.d(TAG, "notifyClient delayTime=" + j, false);
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FeedbackReq feedbackReq = new FeedbackReq();
        StringBuilder sb = new StringBuilder();
        sb.append(20).append(SystemPropertyUtil.getVehicleId()).append(System.currentTimeMillis()).append((int) (Math.random() * 10.0d)).append((int) (Math.random() * 10.0d));
        feedbackReq.setMsg_id(sb.toString());
        String msgIdFromServer = RemoteControlIPCHelper.getInstance().getMsgIdFromServer();
        if (!TextUtils.isEmpty(msgIdFromServer)) {
            feedbackReq.setMsg_ref(msgIdFromServer);
            CameraLog.d(TAG, "feedbackReq msgRef:" + msgIdFromServer, false);
            RemoteControlIPCHelper.getInstance().setMsgIdFromServer(null);
        }
        feedbackReq.setMsg_type(1);
        feedbackReq.setService_type(11);
        feedbackReq.setMsg_content(new CodeFeedBack(9));
        String json = new Gson().toJson(feedbackReq);
        Intent intent = new Intent();
        intent.setAction(RemoteControlConfig.ACTION_XMART_FEEDBACK);
        intent.putExtra(RemoteControlConfig.ACTION_XMART_FEEDBACK_VALUE, json);
        this.mContext.sendBroadcast(intent);
        CameraLog.d(TAG, "notifyClient end with msg-->" + json, false);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPreviewStarted(AlivcLivePusher alivcLivePusher) {
        this.mRemoteControlPresenter.onPreviewStarted();
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPreviewStoped(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPreviewStoped!", false);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPushStarted(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPushStarted!", false);
        this.mLivePushStatus = 0;
        CameraLog.d(TAG, "onPushStarted controlFeedBack delay 3000!", false);
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 3000L);
        this.mStartLiveTime = SystemClock.uptimeMillis();
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onFirstAVFramePushed(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onFirstAVFramePushed!", false);
        this.mLivePushStatus = 0;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPushPauesed(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPushPauesed!", false);
        this.mLivePushStatus = -2;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPushResumed(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPushResumed!", false);
        this.mLivePushStatus = 0;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPushStoped(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPushStoped!", false);
        this.mLivePushStatus = 0;
        this.mLiveGetUrl = null;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onPushRestarted(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPushRestarted!", false);
        this.mLivePushStatus = 0;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushInfoListener
    public void onFirstFramePreviewed(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onFirstFramePreviewed!", false);
        this.mIsStartPreviewCalled = true;
        if (this.mIsStarPushCalled || TextUtils.isEmpty(this.mLivePushUrl)) {
            return;
        }
        CameraLog.d(TAG, "startPushAysnc is not call,call living()", false);
        startPushAysnc(this.mLivePushUrl);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushErrorListener
    public void onSystemError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
        CameraLog.d(TAG, "onSystemError!" + alivcLivePushError.getMsg(), false);
        this.mLivePushStatus = -1;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushErrorListener
    public void onSDKError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
        if (alivcLivePushError != null) {
            CameraLog.d(TAG, "onSDKError!" + alivcLivePushError.getMsg(), false);
            this.mLivePushStatus = -1;
            feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
        }
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onNetworkPoor(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onNetworkPoor!", false);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onNetworkRecovery(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onNetworkRecovery!", false);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onReconnectStart(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onReconnectStart!", false);
        this.mLivePushStatus = 0;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onConnectionLost(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onConnectionLost!", false);
        pingRTMPHost(this.mLivePushUrl);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onReconnectFail(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onReconnectFail!", false);
        this.mLivePushStatus = -1;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
        pingRTMPHost(this.mLivePushUrl);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onReconnectSucceed(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onReconnectSucceed!", false);
        this.mLivePushStatus = 0;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onSendDataTimeout(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onSendDataTimeout!", false);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onConnectFail(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onConnectFail!", false);
        this.mLivePushStatus = -1;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
        pingRTMPHost(this.mLivePushUrl);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public String onPushURLAuthenticationOverdue(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPushURLAuthenticationOverdue!", false);
        this.mLivePushStatus = -1;
        feedBackCameraSt(CarCameraHelper.getInstance().getCarCamera(), 0L);
        return this.mLivePushUrl;
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onSendMessage(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onSendMessage!", false);
    }

    @Override // com.alivc.live.pusher.AlivcLivePushNetworkListener
    public void onPacketsLost(AlivcLivePusher alivcLivePusher) {
        CameraLog.d(TAG, "onPacketsLost!", false);
    }

    private void pingRTMPHost(final String rmtpUrl) {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.livepush.-$$Lambda$RemoteControlModel$j0ecCfr2N4OGsY1r2v-Mw2ER_0E
            @Override // java.lang.Runnable
            public final void run() {
                RemoteControlModel.lambda$pingRTMPHost$9(rmtpUrl);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pingRTMPHost$9(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Matcher matcher = Pattern.compile("((rtmp?)?(://))?([^/]*)(/?.*)").matcher(str);
            if (matcher.matches()) {
                NetPingUtil.ping(matcher.group(4));
            }
        } catch (Exception unused) {
        }
    }
}
