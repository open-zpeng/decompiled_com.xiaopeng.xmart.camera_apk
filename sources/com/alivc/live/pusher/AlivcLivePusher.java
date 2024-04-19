package com.alivc.live.pusher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.alivc.component.custom.AlivcLivePushCustomDetect;
import com.alivc.component.custom.AlivcLivePushCustomFilter;
import com.alivc.debug.AlivcLivePushDebugInfo;
import com.alivc.debug.DebugViewManager;
import com.alivc.live.pusher.LivePusherJNI;
import com.alivc.live.pusher.a;
import com.alivc.live.pusher.a.a;
import com.alivc.live.pusher.a.b;
import com.alivc.live.pusher.a.c;
import com.alivc.live.pusher.a.d;
import com.alivc.live.pusher.a.e;
import com.alivc.live.pusher.a.f;
import com.alivc.live.pusher.a.g;
import com.alivc.live.pusher.a.h;
import com.alivc.live.pusher.a.i;
import com.alivc.live.pusher.a.j;
import com.alivc.live.pusher.a.k;
import com.alivc.live.pusher.a.l;
import com.alivc.live.pusher.a.m;
import com.alivc.live.pusher.a.n;
import com.alivc.live.pusher.a.o;
import com.alivc.live.pusher.a.p;
import com.alivc.live.pusher.a.q;
import com.alivc.live.pusher.a.r;
import com.alivc.live.pusher.a.s;
import com.alivc.live.pusher.a.t;
import com.alivc.live.pusher.a.u;
import com.alivc.live.pusher.a.v;
import com.alivc.live.pusher.a.w;
import com.alivc.live.pusher.a.x;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.util.RLog;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class AlivcLivePusher implements ILivePusher {
    private static final String AUTH_KEY = "auth_key=";
    private static final int MAX_CHATS = 4000;
    private static final int MAX_DYNAMIC_ADDSON_COUNT = 3;
    private static final int MESSAGE_RECONNECT_SUCCESS = 18;
    private static final int NTP_TIME_OUT_MILLISECOND = 1000;
    private static final int SECOND = 1000;
    private static final float TEXTURE_RANGE_MAX = 1.0f;
    private static final float TEXTURE_RANGE_MIN = 0.0f;
    private static final int TIMESTAMP_LENGTH = 10;
    private static LivePusherJNI mNativeAlivcLivePusher;
    protected a mBluetoothHelper;
    private static AlivcResolutionEnum res = AlivcResolutionEnum.RESOLUTION_540P;
    private static String mPushUrl = null;
    private static boolean isDebugView = false;
    private static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private AlivcLivePushStats mPushStatus = AlivcLivePushStats.IDLE;
    private AlivcLivePlayStats mPlayStats = AlivcLivePlayStats.IDLE;
    private AlivcLivePushError mLastError = AlivcLivePushError.ALIVC_COMMON_RETURN_SUCCESS;
    private AlivcLivePushInfoListener mPushInfoListener = null;
    private AlivcLivePushErrorListener mPushErrorListener = null;
    private AlivcLivePushNetworkListener mPushNetworkListener = null;
    private AlivcLivePushBGMListener mPushBGMListener = null;
    private AlivcLivePusherRenderContextListener mRenderContextListener = null;
    private SurfaceStatus mSurfaceStatus = SurfaceStatus.UNINITED;
    private SurfaceView mPreviewView = null;
    private AudioManager mAudioManager = null;
    private Context mContext = null;
    private AlivcLivePushConfig mAlivcLivePushConfig = null;
    private Map<Integer, AlivcLivePushError> mErrorMap = new HashMap();
    private int mSkinSmooth = 40;
    private int mWhiten = 70;
    private int mBright = 0;
    private int mCheekPink = 15;
    private int mWholePink = 40;
    private int mSlimFace = 40;
    private int mShortenFace = 50;
    private int mBigEye = 30;
    private int mBGMVolume = 50;
    private int mCaptureVolume = 50;
    private boolean mMute = false;
    private TelephonyManager manager = null;
    private AlivcLivePushLogLevel mLogLevel = AlivcLivePushLogLevel.AlivcLivePushLogLevelError;
    private boolean registeredCallback = false;
    private boolean isReconnect = false;
    private AlivcEventPublicParam mAlivcEventPublicParam = null;
    private long mTimeStamp = -1;
    private int mExpiryTime = -1;
    private boolean mixRequireMain = false;
    private int mDynamicAddsonCount = 0;
    private AlivcSnapshotListener mSnapshotListener = null;
    private ScreenRecordStatus mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_NONE;
    private ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(100));
    private String mDebugFunctionText = "[%1$s] <thread:%2$s> ";
    private Handler mHandler = new Handler() { // from class: com.alivc.live.pusher.AlivcLivePusher.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            LogUtil.d("AlivcLivePusher", "AlivcLivePusher reconnect success");
            AlivcLivePusher.this.isReconnect = false;
            if (AlivcLivePusher.this.mPushNetworkListener != null) {
                AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AlivcLivePusher.this.mPushNetworkListener.onReconnectSucceed(AlivcLivePusher.this);
                    }
                });
            }
        }
    };
    private SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() { // from class: com.alivc.live.pusher.AlivcLivePusher.2
        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            LogUtil.d("AlivcLivePusher", "LiveActivity-->Preview surface changed");
            if (AlivcLivePusher.this.mPreviewView == null) {
                return;
            }
            AlivcLivePusher.this.mSurfaceStatus = SurfaceStatus.CHANGED;
            if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                AlivcLivePusher.mNativeAlivcLivePusher.notifySurfaceChange(surfaceHolder.getSurface(), AlivcLivePusher.this.mAlivcLivePushConfig.getPreviewOrientation());
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            LogUtil.d("AlivcLivePusher", "LiveActivity-->Preview surface created");
            if (AlivcLivePusher.this.mPreviewView == null) {
                return;
            }
            if (AlivcLivePusher.this.mSurfaceStatus == SurfaceStatus.UNINITED) {
                AlivcLivePusher.this.mSurfaceStatus = SurfaceStatus.CREATED;
            } else if (AlivcLivePusher.this.mSurfaceStatus == SurfaceStatus.DESTROYED) {
                AlivcLivePusher.this.mSurfaceStatus = SurfaceStatus.RECREATED;
                if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                    AlivcLivePusher.mNativeAlivcLivePusher.notifySurfaceReCreate(AlivcLivePusher.this.mPreviewView.getHolder().getSurface());
                }
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            LogUtil.d("AlivcLivePusher", "LiveActivity-->Preview surface destroyed");
            if (AlivcLivePusher.this.mPreviewView == null) {
                return;
            }
            if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                AlivcLivePusher.mNativeAlivcLivePusher.notifySurfaceDestroy();
            }
            AlivcLivePusher.this.mSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };
    private ScheduledExecutorService executor5 = null;
    private BroadcastReceiver mHeadsetPlugReceiver = new BroadcastReceiver() { // from class: com.alivc.live.pusher.AlivcLivePusher.7
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.media.AUDIO_BECOMING_NOISY".equals(action)) {
                LogUtil.d("BluetoothHeadsetUtils", "ACTION_AUDIO_BECOMING_NOISY headset out");
                AlivcLivePusher.this.mAudioManager.setMode(0);
                AlivcLivePusher.this.mAudioManager.setSpeakerphoneOn(true);
                LivePusherJNI.headSetOn = false;
                if (AlivcLivePusher.mNativeAlivcLivePusher == null) {
                    return;
                }
            } else if (!"android.intent.action.HEADSET_PLUG".equals(action) || !intent.hasExtra("state")) {
                return;
            } else {
                if (intent.getIntExtra("state", 0) != 0) {
                    if (intent.getIntExtra("state", 0) == 1) {
                        LogUtil.d("BluetoothHeadsetUtils", "headset in");
                        AlivcLivePusher.this.mAudioManager.setSpeakerphoneOn(false);
                        LivePusherJNI.headSetOn = true;
                        if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                            AlivcLivePusher.mNativeAlivcLivePusher.setHeadSet(true);
                            return;
                        }
                        return;
                    }
                    return;
                }
                LogUtil.d("BluetoothHeadsetUtils", "headset out");
                AlivcLivePusher.this.mAudioManager.setMode(0);
                AlivcLivePusher.this.mAudioManager.setSpeakerphoneOn(true);
                if (!LivePusherJNI.headSetOn) {
                    return;
                }
                LivePusherJNI.headSetOn = false;
                if (AlivcLivePusher.mNativeAlivcLivePusher == null) {
                    return;
                }
            }
            AlivcLivePusher.mNativeAlivcLivePusher.setHeadSet(false);
        }
    };
    private BroadcastReceiver mTelephoneReceiver = new BroadcastReceiver() { // from class: com.alivc.live.pusher.AlivcLivePusher.8
        PhoneStateListener stateListener = new PhoneStateListener() { // from class: com.alivc.live.pusher.AlivcLivePusher.8.1
            @Override // android.telephony.PhoneStateListener
            public void onCallStateChanged(int i, String str) {
                super.onCallStateChanged(i, str);
                if (i == 0) {
                    LogUtil.d("AlivcLivePusher", "PHONE: CALL_STATE_IDLE");
                    if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                        AlivcLivePusher.mNativeAlivcLivePusher.setMute(false);
                        return;
                    }
                    return;
                }
                if (i == 1) {
                    LogUtil.d("AlivcLivePusher", "PHONE: CALL_STATE_RINGING");
                    if (AlivcLivePusher.mNativeAlivcLivePusher == null) {
                        return;
                    }
                } else if (i != 2) {
                    return;
                } else {
                    LogUtil.d("AlivcLivePusher", "PHONE: CALL_STATE_OFFHOOK");
                    if (AlivcLivePusher.mNativeAlivcLivePusher == null) {
                        return;
                    }
                }
                AlivcLivePusher.mNativeAlivcLivePusher.setMute(true);
            }
        };

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL") && AlivcLivePusher.this.manager == null) {
                AlivcLivePusher.this.manager = (TelephonyManager) context.getSystemService("phone");
                AlivcLivePusher.this.manager.listen(this.stateListener, 32);
            }
        }
    };
    private AlivcSnapshotListener mInnerSnapshotListener = new AlivcSnapshotListener() { // from class: com.alivc.live.pusher.AlivcLivePusher.12
        @Override // com.alivc.live.pusher.AlivcSnapshotListener
        public void onSnapshot(final Bitmap bitmap) {
            if (AlivcLivePusher.this.mSnapshotListener != null) {
                AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.12.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AlivcLivePusher.this.mSnapshotListener.onSnapshot(bitmap);
                    }
                });
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public enum ScreenRecordStatus {
        SCREEN_RECORD_NONE,
        SCREEN_RECORD_NORMAL,
        SCREEN_RECORD_CAMERA_START,
        SCREEN_RECORD_CAMERA_MIX_START
    }

    static /* synthetic */ AlivcLivePushDebugInfo access$2500() {
        return getDebugInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addWaterMark() {
        float f;
        float height;
        int width;
        ArrayList<WaterMarkInfo> waterMarkInfos = this.mAlivcLivePushConfig.getWaterMarkInfos();
        int size = waterMarkInfos.size();
        for (int i = 0; i < size; i++) {
            int[] imageWidthHeight = getImageWidthHeight(waterMarkInfos.get(i).mWaterMarkPath);
            if (imageWidthHeight != null && imageWidthHeight[0] != 0 && imageWidthHeight[1] != 0) {
                if (this.mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT.getOrientation() || this.mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT.getOrientation()) {
                    f = waterMarkInfos.get(i).mWaterMarkWidth;
                    height = ((waterMarkInfos.get(i).mWaterMarkWidth * imageWidthHeight[1]) / imageWidthHeight[0]) * this.mAlivcLivePushConfig.getHeight();
                    width = this.mAlivcLivePushConfig.getWidth();
                } else {
                    f = waterMarkInfos.get(i).mWaterMarkWidth;
                    height = ((waterMarkInfos.get(i).mWaterMarkWidth * imageWidthHeight[1]) / imageWidthHeight[0]) * this.mAlivcLivePushConfig.getWidth();
                    width = this.mAlivcLivePushConfig.getHeight();
                }
                mNativeAlivcLivePusher.addWaterMark(waterMarkInfos.get(i).mWaterMarkPath, f, height / width, waterMarkInfos.get(i).mWaterMarkCoordX + (waterMarkInfos.get(i).mWaterMarkWidth / 2.0f), waterMarkInfos.get(i).mWaterMarkCoordY + (waterMarkInfos.get(i).mWaterMarkHeight / 2.0f));
            }
        }
    }

    private void checkConfig(AlivcLivePushConfig alivcLivePushConfig) {
        if (alivcLivePushConfig == null) {
            throw new IllegalArgumentException("Invalid parameter, config is null.");
        }
        if (alivcLivePushConfig.isVideoOnly() && alivcLivePushConfig.isAudioOnly()) {
            throw new IllegalStateException("cannot set video only and audio only simultaneously");
        }
        if (alivcLivePushConfig.getTargetVideoBitrate() < 100 || alivcLivePushConfig.getTargetVideoBitrate() > 5000) {
            throw new IllegalStateException("video target bitrate error, Range:[100 5000]");
        }
        if (alivcLivePushConfig.getMinVideoBitrate() < 100 || alivcLivePushConfig.getMinVideoBitrate() > 5000) {
            throw new IllegalStateException("video min bitrate error, Range:[100 5000]");
        }
        if (alivcLivePushConfig.getBeautyWhite() < 0 || alivcLivePushConfig.getBeautyWhite() > 100) {
            throw new IllegalStateException("beautyWhite error, Range:[0 100]");
        }
        if (alivcLivePushConfig.getBeautyBuffing() < 0 || alivcLivePushConfig.getBeautyBuffing() > 100) {
            throw new IllegalStateException("beauty buffing error, Range:[0 100]");
        }
        if (alivcLivePushConfig.getInitialVideoBitrate() < alivcLivePushConfig.getMinVideoBitrate() || alivcLivePushConfig.getInitialVideoBitrate() < 100 || alivcLivePushConfig.getInitialVideoBitrate() > 5000) {
            throw new IllegalStateException("init bitrate error");
        }
        if (alivcLivePushConfig.getConnectRetryCount() <= 0 || alivcLivePushConfig.getConnectRetryCount() > 100) {
            throw new IllegalStateException("connect retry count error, Range:[0 100]");
        }
        if (alivcLivePushConfig.getConnectRetryInterval() <= 0 || alivcLivePushConfig.getConnectRetryInterval() > 10000) {
            throw new IllegalStateException("connect retry interval error, Range:[0 10000]");
        }
        if (alivcLivePushConfig.getMinFps() <= 0 || alivcLivePushConfig.getMinFps() > alivcLivePushConfig.getFps()) {
            throw new IllegalStateException("fps error");
        }
        if (alivcLivePushConfig.getVideoEncodeMode() == AlivcEncodeModeEnum.Encode_MODE_SOFT && alivcLivePushConfig.getResolution().ordinal() >= AlivcResolutionEnum.RESOLUTION_720P.ordinal()) {
            throw new IllegalStateException("Soft encode 720P not support");
        }
        for (int i = 0; i < alivcLivePushConfig.getWaterMarkInfos().size(); i++) {
            if (alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkCoordX > 1.0f || alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkWidth > 1.0f || alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkCoordY > 1.0f || alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkHeight > 1.0f) {
                throw new IllegalStateException("watermark param error");
            }
            if (alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkCoordX < 0.0f || alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkWidth <= 0.0f || alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkCoordY < 0.0f || alivcLivePushConfig.getWaterMarkInfos().get(i).mWaterMarkHeight <= 0.0f) {
                throw new IllegalStateException("watermark param error");
            }
        }
    }

    private int getBGMVolume() {
        if (this.mMute) {
            return 0;
        }
        return this.mBGMVolume;
    }

    private boolean getBlueToothHeadSetOn() {
        a aVar = this.mBluetoothHelper;
        if (aVar != null) {
            return aVar.b();
        }
        return false;
    }

    private int getCaptureVolume() {
        if (this.mMute) {
            return 0;
        }
        return this.mCaptureVolume;
    }

    private static AlivcLivePushDebugInfo getDebugInfo() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getLivePushStatsInfo");
        AlivcLivePushDebugInfo alivcLivePushDebugInfo = new AlivcLivePushDebugInfo();
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI == null) {
            return alivcLivePushDebugInfo;
        }
        String performanceInfo = livePusherJNI.getPerformanceInfo();
        if (performanceInfo == null || "".equals(performanceInfo)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        StringTokenizer stringTokenizer = new StringTokenizer(performanceInfo, "|");
        while (stringTokenizer.hasMoreTokens()) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), ":");
            hashMap.put(stringTokenizer2.nextToken(), stringTokenizer2.hasMoreTokens() ? stringTokenizer2.nextToken() : null);
        }
        try {
            alivcLivePushDebugInfo.setCpu(d.a());
            alivcLivePushDebugInfo.setRes(AlivcResolutionEnum.values()[res.ordinal()].toString());
            alivcLivePushDebugInfo.setUrl(mPushUrl);
            LivePusherJNI livePusherJNI2 = mNativeAlivcLivePusher;
            alivcLivePushDebugInfo.setPush(livePusherJNI2 == null ? false : livePusherJNI2.isPushing());
            alivcLivePushDebugInfo.setVideoCaptureFps(d.a(hashMap, "mVideoCaptureFps"));
            alivcLivePushDebugInfo.setVideoRenderFps(d.a(hashMap, "mVideoRenderingFPS"));
            alivcLivePushDebugInfo.setVideoEncodeFps(d.a(hashMap, "mVideoEncodedFps"));
            alivcLivePushDebugInfo.setAudioUploadBitrate(d.a(hashMap, "mAudioUploadBitrate"));
            alivcLivePushDebugInfo.setVideoUploadBitrate(d.a(hashMap, "mVideoUploadBitrate"));
            alivcLivePushDebugInfo.setAudioPacketsInUploadBuffer(d.a(hashMap, "mAudioPacketsInBuffer"));
            alivcLivePushDebugInfo.setVideoPacketsInUploadBuffer(d.a(hashMap, "mVideoPacketsInBuffer"));
            alivcLivePushDebugInfo.setVideoUploadeFps(d.a(hashMap, "mVideoUploadedFps"));
            alivcLivePushDebugInfo.setTotalDurationOfDropingVideoFrames(d.b(hashMap, "mDropDurationOfVideoFrames"));
            alivcLivePushDebugInfo.setAudioFrameInEncodeBuffer(d.a(hashMap, "mAudioFramesInEncoderQueue"));
            alivcLivePushDebugInfo.setVideoFramesInEncodeBuffer(d.a(hashMap, "mVideoFramesInEncoderQueue"));
            alivcLivePushDebugInfo.setVideoFramesInRenderBuffer(d.a(hashMap, "mVideoFramesInRenderQueue"));
            alivcLivePushDebugInfo.setTotalDroppedAudioFrames(d.a(hashMap, "mTotalDroppedAudioFrames"));
            alivcLivePushDebugInfo.setLatestVideoBitrate(d.a(hashMap, "mLatestVideoBitrate"));
            alivcLivePushDebugInfo.setLatestAudioBitrate(d.a(hashMap, "mLatestAudioBitrate"));
            alivcLivePushDebugInfo.setSocketBufferSize(d.a(hashMap, "mSocketBufferSize"));
            alivcLivePushDebugInfo.setSocketSendTime(d.a(hashMap, "mSocketSendTime"));
            alivcLivePushDebugInfo.setCurrentlyUploadedVideoFramePts(d.b(hashMap, "mCurrentlyUploadedVideoFramePts"));
            alivcLivePushDebugInfo.setCurrentlyUploadedAudioFramePts(d.b(hashMap, "mCurrentlyUploadedAudioFramePts"));
            alivcLivePushDebugInfo.setAudioEncodeFps(d.a(hashMap, "mAudioEncodeFps"));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return alivcLivePushDebugInfo;
    }

    private String getFormatString(String str) {
        int indexOf = str.indexOf(AUTH_KEY);
        if (indexOf > 0) {
            String substring = str.substring(indexOf);
            return substring.indexOf("-") > 0 ? substring.substring(9, substring.indexOf("-")) : substring.substring(9);
        }
        return "";
    }

    private boolean getHeadSetPlugOn() {
        return LivePusherJNI.headSetOn;
    }

    private int[] getImageWidthHeight(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(str, options);
            return new int[]{options.outWidth, options.outHeight};
        } catch (Exception unused) {
            return new int[]{0, 0};
        }
    }

    private void getNetworkTime() {
        new ScheduledThreadPoolExecutor(1, new ThreadFactory() { // from class: com.alivc.live.pusher.AlivcLivePusher.11
            private AtomicInteger atoInteger = new AtomicInteger(0);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("LivePusher-NTP-Time-Thread " + this.atoInteger.getAndIncrement());
                return thread;
            }
        }).execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.10
            @Override // java.lang.Runnable
            public void run() {
                AlivcLivePusher alivcLivePusher;
                int i = 0;
                while (true) {
                    if (i >= 3) {
                        break;
                    }
                    AlivcLivePusher alivcLivePusher2 = AlivcLivePusher.this;
                    alivcLivePusher2.mTimeStamp = alivcLivePusher2.getTimeFromNtpServer("time.pool.aliyun.com");
                    if (AlivcLivePusher.this.mTimeStamp > 0) {
                        AlivcLivePusher.this.mTimeStamp /= 1000;
                        break;
                    }
                    i++;
                }
                if (AlivcLivePusher.this.mTimeStamp < 0) {
                    AlivcLivePusher.this.mTimeStamp = System.currentTimeMillis() / 1000;
                }
                if (AlivcLivePusher.this.getTimestamp(AlivcLivePusher.mPushUrl) != -1) {
                    AlivcLivePusher.this.mExpiryTime = (int) (alivcLivePusher.getTimestamp(AlivcLivePusher.mPushUrl) - AlivcLivePusher.this.mTimeStamp);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, String> getPerformanceMap() {
        String performanceInfo;
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI == null || (performanceInfo = livePusherJNI.getPerformanceInfo()) == null || "".equals(performanceInfo)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        StringTokenizer stringTokenizer = new StringTokenizer(performanceInfo, "|");
        while (stringTokenizer.hasMoreTokens()) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), ":");
            hashMap.put(stringTokenizer2.nextToken(), stringTokenizer2.hasMoreTokens() ? stringTokenizer2.nextToken() : null);
        }
        return hashMap;
    }

    public static String getSDKVersion() {
        return "3.4.0";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTimeFromNtpServer(String str) {
        LogUtil.d("", "get time from " + str);
        e eVar = new e();
        if (eVar.a(str, 1000)) {
            return eVar.a();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTimestamp(String str) {
        String formatString = getFormatString(str);
        if (formatString.length() >= 10 && TextUtils.isDigitsOnly(formatString)) {
            return new Long(Long.valueOf(formatString).longValue()).intValue();
        }
        return -1;
    }

    public static void hideDebugView(Context context) {
        isDebugView = false;
        try {
            DebugViewManager.hideDebugView(context);
        } catch (Exception unused) {
        }
    }

    private void registerHeadsetPlugReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        this.mContext.registerReceiver(this.mHeadsetPlugReceiver, intentFilter);
        this.mContext.registerReceiver(this.mHeadsetPlugReceiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.PHONE_STATE");
        this.mContext.registerReceiver(this.mTelephoneReceiver, intentFilter2);
        this.registeredCallback = true;
    }

    private int setFaceBeauty() {
        float f = (this.mBright + 100.0f) / 100.0f;
        int faceBeauty = mNativeAlivcLivePusher.setFaceBeauty(this.mSkinSmooth / 100.0f, this.mWhiten / 100.0f, f, this.mCheekPink / 100.0f, this.mWholePink / 100.0f, this.mSlimFace / 100.0f, this.mShortenFace / 100.0f, this.mBigEye / 100.0f);
        this.mAlivcLivePushConfig.setBeautyCheekPink(this.mCheekPink);
        this.mAlivcLivePushConfig.setBeautyBrightness(this.mBright);
        this.mAlivcLivePushConfig.setBeautyBuffing(this.mSkinSmooth);
        this.mAlivcLivePushConfig.setBeautyRuddy(this.mWholePink);
        this.mAlivcLivePushConfig.setBeautyThinFace(this.mSlimFace);
        this.mAlivcLivePushConfig.setBeautyShortenFace(this.mShortenFace);
        this.mAlivcLivePushConfig.setBeautyBigEye(this.mBigEye);
        j.a aVar = new j.a();
        aVar.f = this.mCheekPink;
        aVar.b = this.mSkinSmooth;
        aVar.a = this.mWhiten;
        aVar.g = this.mWholePink;
        aVar.c = this.mSlimFace;
        aVar.e = this.mShortenFace;
        aVar.d = this.mBigEye;
        j.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        return faceBeauty;
    }

    public static void showDebugView(Context context) {
        isDebugView = true;
        try {
            DebugViewManager.showView(context);
            DebugViewManager.setUpdateDebigListener(new DebugViewManager.UpdateDebugInfo() { // from class: com.alivc.live.pusher.AlivcLivePusher.9
                @Override // com.alivc.debug.DebugViewManager.UpdateDebugInfo
                public AlivcLivePushDebugInfo updateInfo() {
                    return AlivcLivePusher.access$2500();
                }
            });
        } catch (Exception unused) {
        }
    }

    private void start5Interval() {
        ScheduledExecutorService scheduledExecutorService = this.executor5;
        if (scheduledExecutorService != null) {
            return;
        }
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            this.executor5 = new ScheduledThreadPoolExecutor(1, new ThreadFactory() { // from class: com.alivc.live.pusher.AlivcLivePusher.5
                private AtomicInteger atoInteger = new AtomicInteger(0);

                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("LivePusher-report-Thread " + this.atoInteger.getAndIncrement());
                    return thread;
                }
            });
        }
        this.executor5.scheduleAtFixedRate(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.6
            @Override // java.lang.Runnable
            public void run() {
                if (AlivcLivePusher.this.mTimeStamp != -1) {
                    if (AlivcLivePusher.this.mExpiryTime >= 60) {
                        AlivcLivePusher alivcLivePusher = AlivcLivePusher.this;
                        alivcLivePusher.mExpiryTime -= 5;
                    } else if (AlivcLivePusher.this.mPushNetworkListener != null) {
                        String onPushURLAuthenticationOverdue = AlivcLivePusher.this.mPushNetworkListener.onPushURLAuthenticationOverdue(AlivcLivePusher.this);
                        if (!TextUtils.isEmpty(onPushURLAuthenticationOverdue) && !onPushURLAuthenticationOverdue.equals(AlivcLivePusher.mPushUrl)) {
                            AlivcLivePusher.this.reconnectPushAsync(onPushURLAuthenticationOverdue);
                        }
                    }
                }
                Map performanceMap = AlivcLivePusher.this.getPerformanceMap();
                o.a aVar = new o.a();
                aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
                aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
                aVar.c = d.a();
                aVar.d = d.a(AlivcLivePusher.this.mContext);
                aVar.e = d.a(performanceMap, "mVideoFramesInRenderQueue");
                aVar.f = d.a(performanceMap, "mVideoFramesInEncoderQueue");
                aVar.g = d.a(performanceMap, "mVideoPacketsInBuffer");
                aVar.h = d.a(performanceMap, "mAudioFramesInEncoderQueue");
                aVar.i = d.a(performanceMap, "mAudioPacketsInBuffer");
                aVar.j = d.b(performanceMap, "mAvPTSInterval");
                aVar.k = d.b(performanceMap, "mVideoEncodedFps");
                aVar.l = d.b(performanceMap, "mVideoUploadedFps");
                aVar.m = d.b(performanceMap, "mVideoCaptureFps");
                aVar.n = d.b(performanceMap, "mTotalFramesOfVideoUploaded");
                aVar.o = d.b(performanceMap, "mDropDurationOfVideoFrames");
                aVar.p = d.b(performanceMap, "mAudioPacketsInBuffer");
                aVar.q = d.b(performanceMap, "mVideoPacketsInBuffer");
                aVar.r = d.a(performanceMap, "mAudioUploadingPacketsPerSecond");
                aVar.s = d.a(performanceMap, "mTotalDroppedAudioFrames");
                aVar.t = d.b(performanceMap, "mAudioEncodeBitrate");
                aVar.u = d.b(performanceMap, "mVideoEncodeBitrate");
                aVar.v = d.b(performanceMap, "mAudioUploadBitrate");
                aVar.w = d.b(performanceMap, "mVideoUploadBitrate");
                aVar.x = d.b(performanceMap, "mPresetVideoEncoderBitrate");
                aVar.y = d.b(performanceMap, "mAudioDurationFromeCaptureToUpload");
                aVar.z = d.b(performanceMap, "mVideoDurationFromeCaptureToUpload");
                aVar.G = d.b(performanceMap, "mCurrentlyUploadedVideoFramePts");
                aVar.H = d.b(performanceMap, "mCurrentlyUploadedAudioFramePts");
                aVar.I = d.b(performanceMap, "mPreviousKeyFramePts");
                aVar.J = d.b(performanceMap, "mPreviousKeyFramePts");
                aVar.A = d.b(performanceMap, "mVideoCapturePts");
                aVar.B = d.b(performanceMap, "mAudioCapturePts");
                aVar.C = d.b(performanceMap, "mVideoRtmpPts");
                aVar.D = d.b(performanceMap, "mAudioRtmpPts");
                aVar.E = d.b(performanceMap, "mVideoRtmpPushPts");
                aVar.F = d.b(performanceMap, "mAudioRtmpPushPts");
                aVar.K = d.a(performanceMap, "mIsMultiSlice");
                aVar.L = d.a(performanceMap, "mLatestVideoBitrate");
                aVar.M = d.a(performanceMap, "mLatestAudioBitrate");
                aVar.N = d.a(performanceMap, "mSocketBufferSize");
                aVar.O = d.a(performanceMap, "mSocketSendTime");
                aVar.P = d.b(performanceMap, "mAudioRecvSize");
                aVar.Q = d.b(performanceMap, "mAudioSendSize");
                aVar.R = d.b(performanceMap, "mAudioSrcSendSize");
                aVar.S = d.b(performanceMap, "getAllPastTime");
                o.a(AlivcLivePusher.this.mAlivcEventPublicParam, aVar, AlivcLivePusher.this.mContext);
            }
        }, 0L, 5000L, TimeUnit.MILLISECONDS);
    }

    private void stop5Interval() {
        ScheduledExecutorService scheduledExecutorService = this.executor5;
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            this.executor5.shutdown();
        }
        this.executor5 = null;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int addDynamicsAddons(String str, float f, float f2, float f3, float f4) {
        LivePusherJNI livePusherJNI;
        if (f < 0.0f || f > 1.0f || f2 < 0.0f || f2 > 1.0f || f3 < 0.0f || f3 > 1.0f || f4 < 0.0f || f4 > 1.0f) {
            throw new IllegalArgumentException("x, y, w, h should in range [0~1.0f]");
        }
        int i = this.mDynamicAddsonCount;
        if (i < 3 && (livePusherJNI = mNativeAlivcLivePusher) != null) {
            this.mDynamicAddsonCount = i + 1;
            return livePusherJNI.addDynamicsAddons(str, System.currentTimeMillis() * 1000, 0L, f, f2, f3, f4, 0, false);
        }
        return -1;
    }

    public int addMixAudio(int i, AlivcSoundFormat alivcSoundFormat, int i2) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.addMixAudio(i, alivcSoundFormat.getAlivcSoundFormat(), i2);
        }
        return -1;
    }

    public int addMixVideo(AlivcImageFormat alivcImageFormat, int i, int i2, int i3, float f, float f2, float f3, float f4, boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.addMixVideo(alivcImageFormat.getAlivcImageFormat(), i, i2, i3, f, f2, f3, f4, z);
        }
        return -1;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void changeResolution(AlivcResolutionEnum alivcResolutionEnum) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Please excute init first ");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.INIT) {
            throw new IllegalStateException("changeResolution can only be called in inited or previewed status");
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.changeResolution(AlivcResolutionEnum.GetResolutionWidth(alivcResolutionEnum), AlivcResolutionEnum.GetResolutionHeight(alivcResolutionEnum));
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void destroy() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->destroy");
        recordFunction(this.mContext, "Destroy");
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.release();
        }
        LivePusherJNI livePusherJNI2 = mNativeAlivcLivePusher;
        if (livePusherJNI2 != null) {
            livePusherJNI2.release();
        }
        this.mDynamicAddsonCount = 0;
        if (this.registeredCallback) {
            try {
                this.mContext.unregisterReceiver(this.mHeadsetPlugReceiver);
                this.mContext.unregisterReceiver(this.mTelephoneReceiver);
            } catch (Exception unused) {
                LogUtil.d("AlivcLivePusher", "unregisterReceiver exception");
            }
            this.registeredCallback = false;
        }
        a aVar = this.mBluetoothHelper;
        if (aVar != null) {
            aVar.a();
        }
        this.mPushStatus = AlivcLivePushStats.IDLE;
        stop5Interval();
        ThreadPoolExecutor threadPoolExecutor = this.mThreadPoolExecutor;
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            this.mThreadPoolExecutor.shutdown();
        }
        this.mThreadPoolExecutor = null;
        mNativeAlivcLivePusher = null;
        this.mPushInfoListener = null;
        this.mPushErrorListener = null;
        this.mPushNetworkListener = null;
        this.mPreviewView = null;
        this.mContext = null;
        this.mAlivcLivePushConfig = null;
        this.mErrorMap = null;
        this.mPreviewCallback = null;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(18);
        }
        this.mHandler = null;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void focusCameraAtAdjustedPoint(float f, float f2, boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->focusCameraAtAdjustedPoint x: " + f + " y: " + f2 + " auto: " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        recordFunction(this.mContext, "FocusCameraAtAdjustedPoint -- x:" + f + " y:" + f2 + " Return:" + mNativeAlivcLivePusher.setCameraFocus(z, f, f2));
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int getCurrentExposure() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getCurrentExposure");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                int currentExposureCompensation = mNativeAlivcLivePusher.getCurrentExposureCompensation();
                recordFunction(this.mContext, "getCurrentExposure");
                return currentExposureCompensation;
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public AlivcLivePushStats getCurrentStatus() {
        return this.mPushStatus;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int getCurrentZoom() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getCurrentZoom");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
                if (livePusherJNI != null) {
                    livePusherJNI.getCameraCurrentZoom();
                    return 0;
                }
                return 0;
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("Illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public AlivcLivePushError getLastError() {
        return this.mLastError;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public AlivcLivePushStatsInfo getLivePushStatsInfo() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getLivePushStatsInfo");
        if (mNativeAlivcLivePusher == null) {
            return null;
        }
        AlivcLivePushStatsInfo alivcLivePushStatsInfo = new AlivcLivePushStatsInfo();
        String performanceInfo = mNativeAlivcLivePusher.getPerformanceInfo();
        if (performanceInfo == null || "".equals(performanceInfo)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        StringTokenizer stringTokenizer = new StringTokenizer(performanceInfo, "|");
        while (stringTokenizer.hasMoreTokens()) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), ":");
            hashMap.put(stringTokenizer2.nextToken(), stringTokenizer2.hasMoreTokens() ? stringTokenizer2.nextToken() : null);
        }
        try {
            alivcLivePushStatsInfo.setCpu(d.a());
            alivcLivePushStatsInfo.setMemory(d.a(this.mContext));
            alivcLivePushStatsInfo.setVideoCaptureFps(d.a(hashMap, "mVideoCaptureFps"));
            alivcLivePushStatsInfo.setAudioEncodeBitrate(d.a(hashMap, "mAudioEncodeBitrate"));
            alivcLivePushStatsInfo.setVideoRenderFps(d.a(hashMap, "mVideoRenderingFPS"));
            alivcLivePushStatsInfo.setAudioEncodeFps(d.a(hashMap, "mAudioEncodeFps"));
            alivcLivePushStatsInfo.setVideoEncodeMode(AlivcEncodeModeEnum.values()[d.a(hashMap, "mPresetVideoEncoderMode")]);
            alivcLivePushStatsInfo.setVideoEncodeBitrate(d.a(hashMap, "mVideoEncodeBitrate"));
            alivcLivePushStatsInfo.setVideoEncodeFps(d.a(hashMap, "mVideoEncodedFps"));
            alivcLivePushStatsInfo.setTotalFramesOfEncodedVideo(d.b(hashMap, "mTotalFramesOfEncodedVideo"));
            alivcLivePushStatsInfo.setTotalTimeOfEncodedVideo(d.b(hashMap, "mTotalTimeOfEncodedVideo"));
            alivcLivePushStatsInfo.setVideoEncodeParam(d.a(hashMap, "mPresetVideoEncoderBitrate"));
            alivcLivePushStatsInfo.setAudioUploadBitrate(d.a(hashMap, "mAudioUploadBitrate"));
            alivcLivePushStatsInfo.setVideoUploadBitrate(d.a(hashMap, "mVideoUploadBitrate"));
            alivcLivePushStatsInfo.setAudioPacketsInUploadBuffer(d.a(hashMap, "mAudioPacketsInBuffer"));
            alivcLivePushStatsInfo.setVideoPacketsInUploadBuffer(d.a(hashMap, "mVideoPacketsInBuffer"));
            alivcLivePushStatsInfo.setVideoUploadeFps(d.a(hashMap, "mVideoUploadedFps"));
            alivcLivePushStatsInfo.setAudioUploadFps(d.a(hashMap, "mAudioUploadingPacketsPerSecond"));
            alivcLivePushStatsInfo.setCurrentlyUploadedVideoFramePts(d.b(hashMap, "mCurrentlyUploadedVideoFramePts"));
            alivcLivePushStatsInfo.setCurrentlyUploadedAudioFramePts(d.b(hashMap, "mCurrentlyUploadedAudioFramePts"));
            alivcLivePushStatsInfo.setPreviousVideoKeyFramePts(d.b(hashMap, "mPreviousKeyFramePts"));
            alivcLivePushStatsInfo.setLastVideoPtsInBuffer(d.b(hashMap, "mLastVideoFramePTSInQueue"));
            alivcLivePushStatsInfo.setLastAudioPtsInBuffer(d.b(hashMap, "mLastAudioFramePTSInQueue"));
            alivcLivePushStatsInfo.setTotalSizeOfUploadedPackets(d.b(hashMap, "mTotalSizeOfUploadedPackets"));
            alivcLivePushStatsInfo.setTotalTimeOfUploading(d.b(hashMap, "mTotalTimeOfPublishing"));
            alivcLivePushStatsInfo.setTotalFramesOfUploadedVideo(d.b(hashMap, "mTotalFramesOfVideoUploaded"));
            alivcLivePushStatsInfo.setTotalDurationOfDropingVideoFrames(d.b(hashMap, "mDropDurationOfVideoFrames"));
            alivcLivePushStatsInfo.setTotalTimesOfDropingVideoFrames(d.b(hashMap, "mTotalDroppedTimes"));
            alivcLivePushStatsInfo.setTotalTimesOfDisconnect(d.a(hashMap, "mTotalNetworkDisconnectedTimes"));
            alivcLivePushStatsInfo.setTotalTimesOfReconnect(d.a(hashMap, "mTotalNetworkReconnectedTimes"));
            alivcLivePushStatsInfo.setVideoDurationFromeCaptureToUpload(d.a(hashMap, "mVideoDurationFromeCaptureToUpload"));
            alivcLivePushStatsInfo.setAudioDurationFromeCaptureToUpload(d.a(hashMap, "mAudioDurationFromeCaptureToUpload"));
            alivcLivePushStatsInfo.setCurrentUploadPacketSize(d.a(hashMap, "mCurrentUploadingPacketSize"));
            alivcLivePushStatsInfo.setMaxSizeOfVideoPacketsInBuffer(d.a(hashMap, "mMaxVideoPacketSize"));
            alivcLivePushStatsInfo.setMaxSizeOfAudioPacketsInBuffer(d.a(hashMap, "mMaxAudioPacketSize"));
            alivcLivePushStatsInfo.setLastVideoFramePTSInQueue(d.b(hashMap, "mLastVideoFramePTSInQueue"));
            alivcLivePushStatsInfo.setLastAudioFramePTSInQueue(d.b(hashMap, "mLastAudioFramePTSInQueue"));
            alivcLivePushStatsInfo.setAvPTSInterval(d.b(hashMap, "mAvPTSInterval"));
            alivcLivePushStatsInfo.setAudioFrameInEncodeBuffer(d.a(hashMap, "mAudioFramesInEncoderQueue"));
            alivcLivePushStatsInfo.setVideoFramesInEncodeBuffer(d.a(hashMap, "mVideoFramesInEncoderQueue"));
            alivcLivePushStatsInfo.setVideoFramesInRenderBuffer(d.a(hashMap, "mVideoFramesInRenderQueue"));
            alivcLivePushStatsInfo.setVideoRenderConsumingTimePerFrame(d.a(hashMap, "mVideoRenderConsumingTimePerFrame"));
            alivcLivePushStatsInfo.setTotalDroppedAudioFrames(d.a(hashMap, "mTotalDroppedAudioFrames"));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return alivcLivePushStatsInfo;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int getMaxZoom() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getMaxZoom");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
                if (livePusherJNI != null) {
                    return livePusherJNI.getCameraMaxZoom();
                }
                return 0;
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("Illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public String getPushUrl() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getPushUrl");
        return mPushUrl;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int getSupportedMaxExposure() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getSupportedMaxExposure");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                int maxExposureCompensation = mNativeAlivcLivePusher.getMaxExposureCompensation();
                recordFunction(this.mContext, "getSupportedMaxExposure");
                return maxExposureCompensation;
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int getSupportedMinExposure() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->getSupportedMinExposure");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                int minExposureCompensation = mNativeAlivcLivePusher.getMinExposureCompensation();
                recordFunction(this.mContext, "getSupportedMinExposure");
                return minExposureCompensation;
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void init(Context context, AlivcLivePushConfig alivcLivePushConfig) {
        AlivcLivePushError[] values;
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->init");
        if (this.mPushStatus != AlivcLivePushStats.IDLE && this.mPushStatus != AlivcLivePushStats.INIT) {
            throw new IllegalStateException("init state error, current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        RLog.setOpen(false);
        this.mContext = context;
        try {
            DebugViewManager.copyFaceAsset(context);
        } catch (Exception unused) {
        }
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        this.mAudioManager = audioManager;
        audioManager.setMode(0);
        this.mAudioManager.setSpeakerphoneOn(true);
        registerHeadsetPlugReceiver();
        a aVar = new a(this.mContext);
        this.mBluetoothHelper = aVar;
        aVar.a(new a.InterfaceC0023a() { // from class: com.alivc.live.pusher.AlivcLivePusher.3
            @Override // com.alivc.live.pusher.a.InterfaceC0023a
            public void onBlueTooth(boolean z) {
                LivePusherJNI.headSetOn = z;
                AudioManager audioManager2 = AlivcLivePusher.this.mAudioManager;
                if (z) {
                    audioManager2.setSpeakerphoneOn(false);
                } else {
                    audioManager2.setMode(0);
                    AlivcLivePusher.this.mAudioManager.setSpeakerphoneOn(true);
                }
                if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                    AlivcLivePusher.mNativeAlivcLivePusher.setHeadSet(z);
                }
            }
        });
        this.mAlivcLivePushConfig = alivcLivePushConfig;
        checkConfig(alivcLivePushConfig);
        res = this.mAlivcLivePushConfig.getResolution();
        for (AlivcLivePushError alivcLivePushError : AlivcLivePushError.values()) {
            this.mErrorMap.put(Integer.valueOf(alivcLivePushError.getCode()), alivcLivePushError);
        }
        LivePusherJNI livePusherJNI = new LivePusherJNI(this.mContext, alivcLivePushConfig, new LivePusherJNI.a() { // from class: com.alivc.live.pusher.AlivcLivePusher.4
            @Override // com.alivc.live.pusher.LivePusherJNI.a
            public void onNotify(final int i, String str, final int i2, final int i3, int i4, int i5, int i6, long j) {
                ThreadPoolExecutor threadPoolExecutor;
                Runnable runnable;
                AlivcLivePusher alivcLivePusher;
                AlivcLivePushStats alivcLivePushStats;
                AlivcLivePusher alivcLivePusher2;
                AlivcLivePushStats alivcLivePushStats2;
                AlivcLivePusher alivcLivePusher3;
                AlivcLivePushError alivcLivePushError2;
                if (i == AlivcLivePushError.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_NETWORK_TOO_POOR.getCode()) {
                    LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Network Too Poor");
                    AlivcLivePusher alivcLivePusher4 = AlivcLivePusher.this;
                    alivcLivePusher4.recordFunction(alivcLivePusher4.mContext, "CallBack -- Network Too Poor");
                    if (AlivcLivePusher.this.mPushNetworkListener == null) {
                        return;
                    }
                    threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                    runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AlivcLivePusher.this.mPushNetworkListener.onNetworkPoor(AlivcLivePusher.this);
                        }
                    };
                } else if (i == c.v.a()) {
                    LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Network Recovery");
                    AlivcLivePusher alivcLivePusher5 = AlivcLivePusher.this;
                    alivcLivePusher5.recordFunction(alivcLivePusher5.mContext, "CallBack -- Network Recovery");
                    if (AlivcLivePusher.this.mPushNetworkListener == null) {
                        return;
                    }
                    threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                    runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AlivcLivePusher.this.mPushNetworkListener.onNetworkRecovery(AlivcLivePusher.this);
                        }
                    };
                } else if (i != c.w.a()) {
                    if (i == c.x.a()) {
                        LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Reconnect Success");
                        AlivcLivePusher alivcLivePusher6 = AlivcLivePusher.this;
                        alivcLivePusher6.recordFunction(alivcLivePusher6.mContext, "CallBack -- Reconnect Success");
                        if (AlivcLivePusher.this.mHandler != null) {
                            AlivcLivePusher.this.mHandler.removeMessages(18);
                            AlivcLivePusher.this.mHandler.sendEmptyMessageDelayed(18, 500L);
                        }
                        if (AlivcLivePusher.this.mPushStatus == AlivcLivePushStats.PAUSED) {
                            return;
                        }
                        alivcLivePusher2 = AlivcLivePusher.this;
                        alivcLivePushStats2 = AlivcLivePushStats.PUSHED;
                    } else if (i != c.B.a()) {
                        if (i == AlivcLivePushError.ALIVC_PUSHER_ERROR_SDK_RTMP_RECONNECT_FAIL.getCode()) {
                            LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Reconnect Fail");
                            AlivcLivePusher alivcLivePusher7 = AlivcLivePusher.this;
                            alivcLivePusher7.recordFunction(alivcLivePusher7.mContext, "CallBack -- Reconnect Fail");
                            AlivcLivePusher.this.isReconnect = false;
                            if (AlivcLivePusher.this.mHandler != null) {
                                AlivcLivePusher.this.mHandler.removeMessages(18);
                            }
                            if (AlivcLivePusher.this.mPushNetworkListener != null) {
                                AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.5
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushNetworkListener.onReconnectFail(AlivcLivePusher.this);
                                    }
                                });
                            }
                            AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.ERROR;
                            alivcLivePusher3 = AlivcLivePusher.this;
                            alivcLivePushError2 = AlivcLivePushError.ALIVC_PUSHER_ERROR_SDK_RTMP_RECONNECT_FAIL;
                        } else if (i == AlivcLivePushError.ALIVC_PUSHER_ERROR_SDK_RTMP_SEND_DATA_TIMEOUT.getCode()) {
                            LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Send Data Timeout");
                            AlivcLivePusher alivcLivePusher8 = AlivcLivePusher.this;
                            alivcLivePusher8.recordFunction(alivcLivePusher8.mContext, "CallBack -- Send Data Timeout");
                            if (AlivcLivePusher.this.mPushNetworkListener == null) {
                                return;
                            }
                            threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                            runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    AlivcLivePusher.this.mPushNetworkListener.onSendDataTimeout(AlivcLivePusher.this);
                                }
                            };
                        } else if (i == AlivcLivePushError.ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT.getCode()) {
                            LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback RTMP Connect Fail");
                            AlivcLivePusher alivcLivePusher9 = AlivcLivePusher.this;
                            alivcLivePusher9.recordFunction(alivcLivePusher9.mContext, "CallBack -- RTMP Connect Fail");
                            if (AlivcLivePusher.this.mPushNetworkListener != null) {
                                AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.7
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushNetworkListener.onConnectFail(AlivcLivePusher.this);
                                    }
                                });
                            }
                            AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.ERROR;
                            alivcLivePusher3 = AlivcLivePusher.this;
                            alivcLivePushError2 = AlivcLivePushError.ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT;
                        } else if (i == c.a.a()) {
                            return;
                        } else {
                            if (i == c.b.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Preview Started");
                                AlivcLivePusher alivcLivePusher10 = AlivcLivePusher.this;
                                alivcLivePusher10.recordFunction(alivcLivePusher10.mContext, "CallBack -- Preview Started");
                                if (AlivcLivePusher.this.mPushInfoListener != null) {
                                    AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.8
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            AlivcLivePusher.this.mPushInfoListener.onPreviewStarted(AlivcLivePusher.this);
                                        }
                                    });
                                }
                                AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.PREVIEWED;
                                AlivcLivePusher.this.addWaterMark();
                                return;
                            } else if (i == c.c.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Preview Stoped");
                                AlivcLivePusher alivcLivePusher11 = AlivcLivePusher.this;
                                alivcLivePusher11.recordFunction(alivcLivePusher11.mContext, "CallBack -- Preview Stoped");
                                if (AlivcLivePusher.this.mPushInfoListener != null) {
                                    AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.9
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            AlivcLivePusher.this.mPushInfoListener.onPreviewStoped(AlivcLivePusher.this);
                                        }
                                    });
                                }
                                alivcLivePusher2 = AlivcLivePusher.this;
                                alivcLivePushStats2 = AlivcLivePushStats.INIT;
                            } else if (i == c.s.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Push Started");
                                AlivcLivePusher alivcLivePusher12 = AlivcLivePusher.this;
                                alivcLivePusher12.recordFunction(alivcLivePusher12.mContext, "CallBack -- Push Started");
                                AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.PUSHED;
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.10
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onPushStarted(AlivcLivePusher.this);
                                    }
                                };
                            } else if (i == c.t.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Push Stoped");
                                AlivcLivePusher alivcLivePusher13 = AlivcLivePusher.this;
                                alivcLivePusher13.recordFunction(alivcLivePusher13.mContext, "CallBack -- Push Stoped");
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.11
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onPushStoped(AlivcLivePusher.this);
                                    }
                                };
                            } else if (i == c.d.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Push Paused");
                                AlivcLivePusher alivcLivePusher14 = AlivcLivePusher.this;
                                alivcLivePusher14.recordFunction(alivcLivePusher14.mContext, "CallBack -- Push Paused");
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.12
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onPushPauesed(AlivcLivePusher.this);
                                    }
                                };
                            } else if (i == c.e.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Push Resumed");
                                AlivcLivePusher alivcLivePusher15 = AlivcLivePusher.this;
                                alivcLivePusher15.recordFunction(alivcLivePusher15.mContext, "CallBack -- Push Resumed");
                                if (AlivcLivePusher.mNativeAlivcLivePusher.isPushing()) {
                                    alivcLivePusher = AlivcLivePusher.this;
                                    alivcLivePushStats = AlivcLivePushStats.PUSHED;
                                } else {
                                    alivcLivePusher = AlivcLivePusher.this;
                                    alivcLivePushStats = AlivcLivePushStats.PREVIEWED;
                                }
                                alivcLivePusher.mPushStatus = alivcLivePushStats;
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.13
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onPushResumed(AlivcLivePusher.this);
                                    }
                                };
                            } else if (i == AlivcLivePushError.ALIVC_FRAMEWORK_RENDER_FIRST_FRAME_PREVIEWED.getCode()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Preview First Frame");
                                AlivcLivePusher alivcLivePusher16 = AlivcLivePusher.this;
                                alivcLivePusher16.recordFunction(alivcLivePusher16.mContext, "CallBack -- Preview First Frame");
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.14
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onFirstFramePreviewed(AlivcLivePusher.this);
                                    }
                                };
                            } else if (i == c.f.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Push Restarted");
                                AlivcLivePusher alivcLivePusher17 = AlivcLivePusher.this;
                                alivcLivePusher17.recordFunction(alivcLivePusher17.mContext, "CallBack -- Push Restarted");
                                AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.PUSHED;
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.15
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onPushRestarted(AlivcLivePusher.this);
                                    }
                                };
                            } else if (i == c.y.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Drop Frame");
                                AlivcLivePusher alivcLivePusher18 = AlivcLivePusher.this;
                                alivcLivePusher18.recordFunction(alivcLivePusher18.mContext, "CallBack -- Drop Frame");
                                k.a aVar2 = new k.a();
                                aVar2.a = i2;
                                aVar2.b = i3;
                                aVar2.c = i4;
                                aVar2.d = i5;
                                k.a(AlivcLivePusher.this.mAlivcEventPublicParam, aVar2, AlivcLivePusher.this.mContext);
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.16
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onDropFrame(AlivcLivePusher.this, i2, i3);
                                        if (AlivcLivePusher.this.mPushNetworkListener != null) {
                                            AlivcLivePusher.this.mPushNetworkListener.onPacketsLost(AlivcLivePusher.this);
                                        }
                                    }
                                };
                            } else if (i == c.i.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Adjust Bitrate");
                                AlivcLivePusher alivcLivePusher19 = AlivcLivePusher.this;
                                alivcLivePusher19.recordFunction(alivcLivePusher19.mContext, "CallBack -- Adjust Bitrate");
                                a.C0024a c0024a = new a.C0024a();
                                c0024a.a = i2 / 1000;
                                c0024a.b = i3 / 1000;
                                Map performanceMap = AlivcLivePusher.this.getPerformanceMap();
                                if (performanceMap != null) {
                                    c0024a.c = d.a(performanceMap, "mVideoEncodeBitrate") + d.a(performanceMap, "mAudioEncodeBitrate");
                                    c0024a.d = d.a(performanceMap, "mAudioUploadBitrate") + d.a(performanceMap, "mVideoUploadBitrate");
                                }
                                com.alivc.live.pusher.a.a.a(AlivcLivePusher.this.mAlivcEventPublicParam, c0024a, AlivcLivePusher.this.mContext);
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.17
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onAdjustBitRate(AlivcLivePusher.this, i2 / 1000, i3 / 1000);
                                    }
                                };
                            } else if (i == c.j.a()) {
                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Change FPS");
                                AlivcLivePusher alivcLivePusher20 = AlivcLivePusher.this;
                                alivcLivePusher20.recordFunction(alivcLivePusher20.mContext, "CallBack -- Change FPS");
                                b.a aVar3 = new b.a();
                                aVar3.a = i2;
                                aVar3.b = i3;
                                com.alivc.live.pusher.a.b.a(AlivcLivePusher.this.mAlivcEventPublicParam, aVar3, AlivcLivePusher.this.mContext);
                                if (AlivcLivePusher.this.mPushInfoListener == null) {
                                    return;
                                }
                                threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.18
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AlivcLivePusher.this.mPushInfoListener.onAdjustFps(AlivcLivePusher.this, i2, i3);
                                    }
                                };
                            } else if (i == c.k.a()) {
                                return;
                            } else {
                                if (i == c.z.a()) {
                                    v.a aVar4 = new v.a();
                                    aVar4.b = i3;
                                    aVar4.a = i2;
                                    aVar4.c = AlivcLivePusher.this.mAlivcLivePushConfig.getResolution().toString().substring(11);
                                    aVar4.d = AlivcLivePusher.this.mAlivcLivePushConfig.getAudioChannels() == 1 ? "single" : "dual";
                                    aVar4.e = AlivcLivePusher.this.mAlivcLivePushConfig.isAudioOnly();
                                    aVar4.f = AlivcLivePusher.this.mAlivcLivePushConfig.isVideoOnly();
                                    aVar4.g = AlivcLivePusher.this.mAlivcLivePushConfig.getVideoEncodeMode().equals(AlivcEncodeModeEnum.Encode_MODE_HARD);
                                    aVar4.h = AlivcLivePusher.this.mAlivcLivePushConfig.getWaterMarkInfos().size();
                                    aVar4.i = AlivcLivePusher.this.mAlivcLivePushConfig.isPushMirror();
                                    aVar4.j = AlivcLivePusher.this.mAlivcLivePushConfig.getFps();
                                    aVar4.k = AlivcLivePusher.this.mAlivcLivePushConfig.getInitialVideoBitrate();
                                    aVar4.l = AlivcLivePusher.this.mAlivcLivePushConfig.getTargetVideoBitrate();
                                    aVar4.m = AlivcLivePusher.this.mAlivcLivePushConfig.getMinVideoBitrate();
                                    aVar4.n = AlivcLivePusher.this.mAlivcLivePushConfig.getAudioSamepleRate().getAudioSampleRate();
                                    aVar4.o = AlivcLivePusher.this.mAlivcLivePushConfig.getPreviewOrientation();
                                    aVar4.p = AlivcLivePusher.this.mAlivcLivePushConfig.getCameraType();
                                    aVar4.q = AlivcLivePusher.this.mAlivcLivePushConfig.isBeautyOn();
                                    aVar4.s = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyWhite();
                                    aVar4.t = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyBuffing();
                                    aVar4.u = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyRuddy();
                                    aVar4.v = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyThinFace();
                                    aVar4.r = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyCheekPink();
                                    aVar4.w = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyShortenFace();
                                    aVar4.x = AlivcLivePusher.this.mAlivcLivePushConfig.getBeautyBigEye();
                                    aVar4.y = AlivcLivePusher.this.mAlivcLivePushConfig.isFlash();
                                    aVar4.z = AlivcLivePusher.this.mAlivcLivePushConfig.getConnectRetryCount();
                                    aVar4.A = AlivcLivePusher.this.mAlivcLivePushConfig.getConnectRetryInterval();
                                    aVar4.B = AlivcLivePusher.this.mAlivcLivePushConfig.isPreviewMirror();
                                    aVar4.C = AlivcLivePusher.this.mAlivcLivePushConfig.getVideoEncodeGop();
                                    aVar4.D = (AlivcLivePusher.this.mAlivcLivePushConfig.getConnectRetryCount() * AlivcLivePusher.this.mAlivcLivePushConfig.getConnectRetryInterval()) / 1000;
                                    v.a(AlivcLivePusher.this.mAlivcEventPublicParam, aVar4, AlivcLivePusher.this.mContext);
                                    if (AlivcLivePusher.this.mPushInfoListener == null) {
                                        return;
                                    }
                                    threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                    runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.19
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            AlivcLivePusher.this.mPushInfoListener.onFirstAVFramePushed(AlivcLivePusher.this);
                                        }
                                    };
                                } else if (i == c.n.a() || i == c.o.a() || i == c.p.a() || i == c.g.a() || i == c.h.a()) {
                                    return;
                                } else {
                                    if (i != c.A.a()) {
                                        if (i == AlivcLivePushError.ALIVC_PUSHER_ERROR_BGM_OPEN_FAILED.getCode()) {
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.IDLE;
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onOpenFailed();
                                            }
                                            if (AlivcLivePusher.mNativeAlivcLivePusher != null) {
                                                AlivcLivePusher.mNativeAlivcLivePusher.stopBGM();
                                                return;
                                            }
                                            return;
                                        } else if (i == AlivcLivePushError.ALIVC_PUSHER_ERROR_BGM_TIMEOUT.getCode()) {
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.IDLE;
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onDownloadTimeout();
                                                return;
                                            }
                                            return;
                                        } else if (i == c.C.a()) {
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.STARTED;
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onStarted();
                                                return;
                                            }
                                            return;
                                        } else if (i == c.D.a()) {
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.STOPED;
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onStoped();
                                                return;
                                            }
                                            return;
                                        } else if (i == c.E.a()) {
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.PAUSED;
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onPaused();
                                                return;
                                            }
                                            return;
                                        } else if (i == c.F.a()) {
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.STARTED;
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onResumed();
                                                return;
                                            }
                                            return;
                                        } else if (i == c.G.a()) {
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onCompleted();
                                            }
                                            AlivcLivePusher.this.mPlayStats = AlivcLivePlayStats.IDLE;
                                            return;
                                        } else if (i == c.H.a()) {
                                            if (AlivcLivePusher.this.mPushBGMListener != null) {
                                                AlivcLivePusher.this.mPushBGMListener.onProgress(i2, i3);
                                                return;
                                            }
                                            return;
                                        } else if (i == c.q.a() || i == c.r.a()) {
                                            return;
                                        } else {
                                            if (i == c.l.a()) {
                                                if (AlivcLivePusher.this.mRenderContextListener != null) {
                                                    AlivcLivePusher.this.mRenderContextListener.onSharedContextCreated(j);
                                                    return;
                                                }
                                                return;
                                            } else if (i == c.m.a()) {
                                                if (AlivcLivePusher.this.mRenderContextListener != null) {
                                                    AlivcLivePusher.this.mRenderContextListener.onSharedContextDestroyed(j);
                                                    return;
                                                }
                                                return;
                                            } else if (i > 805371904 && i < 805437440) {
                                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher SystemError Callback error: " + i);
                                                if (AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i)) != null) {
                                                    ((AlivcLivePushError) AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i))).setMsg(str);
                                                }
                                                if (AlivcLivePusher.this.mPushErrorListener != null) {
                                                    AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.21
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            AlivcLivePusher.this.mPushErrorListener.onSystemError(AlivcLivePusher.this, (AlivcLivePushError) AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i)));
                                                        }
                                                    });
                                                }
                                                l.a aVar5 = new l.a();
                                                Map performanceMap2 = AlivcLivePusher.this.getPerformanceMap();
                                                if (performanceMap2 != null) {
                                                    aVar5.c = d.b(performanceMap2, "mTotalSizeOfUploadedPackets");
                                                    aVar5.d = d.b(performanceMap2, "mTotalTimeOfPublishing");
                                                    aVar5.a = i;
                                                    if (AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i)) != null) {
                                                        aVar5.b = str;
                                                    }
                                                    l.a(AlivcLivePusher.this.mAlivcEventPublicParam, aVar5, AlivcLivePusher.this.mContext);
                                                }
                                                AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.ERROR;
                                                AlivcLivePusher.this.mLastError = AlivcLivePushError.getErrorByCode(i);
                                                return;
                                            } else {
                                                LogUtil.d("AlivcLivePusher", "AlivcLivePusher SDKError Callback error: " + i);
                                                if (AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i)) != null) {
                                                    ((AlivcLivePushError) AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i))).setMsg(str);
                                                }
                                                if (AlivcLivePusher.this.mPushErrorListener != null) {
                                                    AlivcLivePusher.this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.22
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            AlivcLivePusher.this.mPushErrorListener.onSDKError(AlivcLivePusher.this, (AlivcLivePushError) AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i)));
                                                        }
                                                    });
                                                }
                                                l.a aVar6 = new l.a();
                                                Map performanceMap3 = AlivcLivePusher.this.getPerformanceMap();
                                                if (performanceMap3 != null) {
                                                    aVar6.c = d.b(performanceMap3, "mTotalSizeOfUploadedPackets");
                                                    aVar6.d = d.b(performanceMap3, "mTotalTimeOfPublishing");
                                                    aVar6.a = i;
                                                    if (AlivcLivePusher.this.mErrorMap.get(Integer.valueOf(i)) != null) {
                                                        aVar6.b = str;
                                                    }
                                                    l.a(AlivcLivePusher.this.mAlivcEventPublicParam, aVar6, AlivcLivePusher.this.mContext);
                                                }
                                                AlivcLivePusher.this.mPushStatus = AlivcLivePushStats.ERROR;
                                                AlivcLivePusher.this.mLastError = AlivcLivePushError.getErrorByCode(i);
                                                LogUtil.d("AlivcLivePusher", "error is " + i + " msg is " + i);
                                                return;
                                            }
                                        }
                                    } else if (AlivcLivePusher.this.mPushNetworkListener == null) {
                                        return;
                                    } else {
                                        threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                                        runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.20
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                AlivcLivePusher.this.mPushNetworkListener.onSendMessage(AlivcLivePusher.this);
                                            }
                                        };
                                    }
                                }
                            }
                        }
                        alivcLivePusher3.mLastError = alivcLivePushError2;
                        return;
                    } else if (AlivcLivePusher.this.mPushNetworkListener == null) {
                        return;
                    } else {
                        threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                        runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.4
                            @Override // java.lang.Runnable
                            public void run() {
                                AlivcLivePusher.this.mPushNetworkListener.onConnectionLost(AlivcLivePusher.this);
                            }
                        };
                    }
                    alivcLivePusher2.mPushStatus = alivcLivePushStats2;
                    return;
                } else {
                    LogUtil.d("AlivcLivePusher", "AlivcLivePusher Callback Reconnect Start");
                    AlivcLivePusher alivcLivePusher21 = AlivcLivePusher.this;
                    alivcLivePusher21.recordFunction(alivcLivePusher21.mContext, "CallBack -- Reconnect Start");
                    if (AlivcLivePusher.this.isReconnect) {
                        return;
                    }
                    AlivcLivePusher.this.isReconnect = true;
                    if (AlivcLivePusher.this.mPushNetworkListener == null) {
                        return;
                    }
                    threadPoolExecutor = AlivcLivePusher.this.mThreadPoolExecutor;
                    runnable = new Runnable() { // from class: com.alivc.live.pusher.AlivcLivePusher.4.3
                        @Override // java.lang.Runnable
                        public void run() {
                            AlivcLivePusher.this.mPushNetworkListener.onReconnectStart(AlivcLivePusher.this);
                        }
                    };
                }
                threadPoolExecutor.execute(runnable);
            }
        });
        mNativeAlivcLivePusher = livePusherJNI;
        livePusherJNI.init();
        recordFunction(this.mContext, "Init");
        setLogLevel(this.mLogLevel);
        this.mPushStatus = AlivcLivePushStats.INIT;
        AlivcEventPublicParam alivcEventPublicParam = new AlivcEventPublicParam(this.mContext);
        this.mAlivcEventPublicParam = alivcEventPublicParam;
        alivcEventPublicParam.setModule("publisher");
        this.mAlivcEventPublicParam.setVideoType(AlivcEventPublicParam.VideoType.live);
        this.mAlivcEventPublicParam.setUi(null);
        this.mAlivcEventPublicParam.setProduct("pusher");
        this.mAlivcEventPublicParam.setSubModule("pusher");
        this.mAlivcEventPublicParam.setLogStore("pubchat");
        this.mAlivcEventPublicParam.setAppVersion(getSDKVersion());
        this.mAlivcEventPublicParam.setLogLevel(AlivcEventPublicParam.LogLevel.info);
        this.mAlivcEventPublicParam.setCdnIp("0.0.0.0");
        this.mAlivcEventPublicParam.setUserAgent("");
        this.mAlivcEventPublicParam.setReferer("aliyun");
    }

    public boolean inputMixAudioData(int i, byte[] bArr, int i2, long j) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.inputMixAudioData(i, bArr, i2, j);
        }
        return false;
    }

    public boolean inputMixAudioPtr(int i, long j, int i2, long j2) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.inputMixAudioPtr(i, j, i2, j2);
        }
        return false;
    }

    public void inputMixTexture(int i, int i2, int i3, int i4, long j, int i5) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputMixTexture(i, i2, i3, i4, j, i5);
        }
    }

    public void inputMixVideoData(int i, byte[] bArr, int i2, int i3, int i4, int i5, long j, int i6) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputMixVideoData(i, bArr, i2, i3, i4, i5, j, i6);
        }
    }

    public void inputMixVideoPtr(int i, long j, int i2, int i3, int i4, int i5, long j2, int i6) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputMixVideoPtr(i, j, i2, i3, i4, i5, j2, i6);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void inputStreamAudioData(byte[] bArr, int i, int i2, int i3, long j) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputStreamAudioData(bArr, i, i2, i3, j);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void inputStreamAudioPtr(long j, int i, int i2, int i3, long j2) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputStreamAudioPtr(j, i, i2, i3, j2);
        }
    }

    public void inputStreamTexture(int i, int i2, int i3, int i4, long j, int i5, long j2) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputStreamTexture(i, i2, i3, i4, j, i5, j2);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void inputStreamVideoData(byte[] bArr, int i, int i2, int i3, int i4, long j, int i5) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputStreamVideoData(bArr, i, i2, i3, i4, j, i5);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void inputStreamVideoPtr(long j, int i, int i2, int i3, int i4, long j2, int i5) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.inputStreamVideoPtr(j, i, i2, i3, i4, j2, i5);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public boolean isCameraSupportAutoFocus() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->isCameraSupportAutoFocus");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                return mNativeAlivcLivePusher.isCameraSupportAutoFocus();
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("Illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public boolean isCameraSupportFlash() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->isCameraSupportFlash");
        if (mNativeAlivcLivePusher != null) {
            if (this.mPushStatus == AlivcLivePushStats.PREVIEWED || this.mPushStatus == AlivcLivePushStats.PUSHED) {
                return mNativeAlivcLivePusher.isCameraSupportFlash();
            }
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        throw new IllegalStateException("Illegal State, you should init first");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public boolean isNetworkPushing() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->isNetworkPushing");
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.isNetworkPushing();
        }
        return false;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public boolean isPushing() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->isPushing");
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.isPushing();
        }
        return false;
    }

    public int mixStreamChangePosition(int i, float f, float f2, float f3, float f4) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.mixStreamChangePosition(i, f, f2, f3, f4);
        }
        return -1;
    }

    public void mixStreamMirror(int i, boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setMixStreamMirror(i, z);
        }
    }

    public int mixStreamRequireMain(int i, boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            return livePusherJNI.mixStreamRequireMain(i, z);
        }
        return -1;
    }

    protected int onNotification(int i, int i2, int i3) {
        return 0;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void pause() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->pause");
        if (this.mAlivcLivePushConfig.isExternMainStream()) {
            return;
        }
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PAUSED && this.mPushStatus != AlivcLivePushStats.PREVIEWED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mPushStatus = AlivcLivePushStats.PAUSED;
        mNativeAlivcLivePusher.pause();
        recordFunction(this.mContext, "Pause -- Sync:Async");
        r.a aVar = new r.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
            aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
            r.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void pauseBGM() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Invalid parameter");
        }
        if (this.mPlayStats != AlivcLivePlayStats.STARTED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePlayStats.values()[this.mPlayStats.ordinal()]);
        }
        mNativeAlivcLivePusher.pauseBGM();
        recordFunction(this.mContext, "PauseBGM");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void pauseScreenCapture() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PAUSED && this.mPushStatus != AlivcLivePushStats.PREVIEWED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mPushStatus = AlivcLivePushStats.PAUSED;
        mNativeAlivcLivePusher.pauseScreenCapture();
        recordFunction(this.mContext, "Pause -- Sync:Async");
        r.a aVar = new r.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
            aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
            r.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void reconnectPushAsync(String str) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->reconnectPushAsync");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PAUSED && this.mPushStatus != AlivcLivePushStats.ERROR) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        if ((str == null || "".equals(str)) && isNetworkPushing()) {
            return;
        }
        if (str != null && !"".equals(str)) {
            mPushUrl = str;
            getNetworkTime();
        }
        int reconnect = mNativeAlivcLivePusher.reconnect(str, false);
        if (reconnect != 0) {
            throw new IllegalStateException("reconnect push async error");
        }
        this.isReconnect = false;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(18);
        }
        this.mPushStatus = AlivcLivePushStats.PUSHED;
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
            this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_NORMAL;
        }
        recordFunction(this.mContext, "ReconnectPushAsync -- Sync:Async Return " + reconnect);
        s.a aVar = new s.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.b = d.b(performanceMap, "mVideoDurationFromeCaptureToUpload");
            aVar.a = d.b(performanceMap, "mAudioDurationFromeCaptureToUpload");
            s.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
    }

    public void recordFunction(Context context, String str) {
        if (isDebugView) {
            DebugViewManager.recordFunction(String.format(this.mDebugFunctionText, d.b(), String.valueOf(Thread.currentThread().getId())) + ("AlivcLivePusher " + str));
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void removeDynamicsAddons(int i) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            this.mDynamicAddsonCount--;
            livePusherJNI.removeDynamicsAddons(i);
        }
    }

    public void removeMixAudio(int i) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.removeMixAudio(i);
        }
    }

    public void removeMixVideo(int i) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.removeMixVideo(i);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void restartPush() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->restartPush");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPreviewView == null && this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() == null && !this.mAlivcLivePushConfig.isAudioOnly()) {
            throw new IllegalArgumentException("illegal argument");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.ERROR) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mDynamicAddsonCount = 0;
        stop5Interval();
        int restartPush = mNativeAlivcLivePusher.restartPush(this.mPreviewView.getHolder().getSurface(), true, 1000);
        if (restartPush != 0) {
            throw new IllegalStateException("restart push error");
        }
        this.mPushStatus = AlivcLivePushStats.PUSHED;
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
            this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_NORMAL;
        }
        mNativeAlivcLivePusher.addPushImage(this.mAlivcLivePushConfig.getPausePushImage() == null ? "" : this.mAlivcLivePushConfig.getPausePushImage(), this.mAlivcLivePushConfig.getNetworkPoorPushImage() != null ? this.mAlivcLivePushConfig.getNetworkPoorPushImage() : "");
        recordFunction(this.mContext, "RestartPush -- Sync:Sync Return " + restartPush);
        t.a aVar = new t.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.b = d.a(performanceMap, "mVideoDurationFromeCaptureToUpload");
            aVar.a = d.a(performanceMap, "mAudioDurationFromeCaptureToUpload");
            aVar.c = this.mAlivcLivePushConfig.getResolution().toString().substring(11);
            aVar.d = this.mAlivcLivePushConfig.getAudioChannels() == 1 ? "single" : "dual";
            aVar.e = this.mAlivcLivePushConfig.isAudioOnly();
            aVar.f = this.mAlivcLivePushConfig.getVideoEncodeMode().equals(AlivcEncodeModeEnum.Encode_MODE_HARD);
            aVar.g = this.mAlivcLivePushConfig.getWaterMarkInfos().size();
            aVar.h = this.mAlivcLivePushConfig.isPushMirror();
            aVar.i = this.mAlivcLivePushConfig.getFps();
            aVar.j = this.mAlivcLivePushConfig.getInitialVideoBitrate();
            aVar.k = this.mAlivcLivePushConfig.getTargetVideoBitrate();
            aVar.l = this.mAlivcLivePushConfig.getMinVideoBitrate();
            aVar.m = this.mAlivcLivePushConfig.getAudioSamepleRate().getAudioSampleRate();
            aVar.n = this.mAlivcLivePushConfig.getPreviewOrientation();
            aVar.o = this.mAlivcLivePushConfig.getCameraType();
            aVar.p = this.mAlivcLivePushConfig.isBeautyOn();
            aVar.q = this.mAlivcLivePushConfig.getBeautyWhite();
            aVar.r = this.mAlivcLivePushConfig.getBeautyBuffing();
            aVar.s = this.mAlivcLivePushConfig.getBeautyRuddy();
            aVar.t = this.mAlivcLivePushConfig.getBeautyThinFace();
            aVar.w = this.mAlivcLivePushConfig.getBeautyCheekPink();
            aVar.u = this.mAlivcLivePushConfig.getBeautyShortenFace();
            aVar.v = this.mAlivcLivePushConfig.getBeautyBigEye();
            aVar.x = this.mAlivcLivePushConfig.isFlash();
            aVar.y = this.mAlivcLivePushConfig.getConnectRetryCount();
            aVar.z = this.mAlivcLivePushConfig.getConnectRetryInterval();
            aVar.A = this.mAlivcLivePushConfig.isPreviewMirror();
            aVar.B = this.mAlivcLivePushConfig.getVideoEncodeGop();
            aVar.C = (this.mAlivcLivePushConfig.getConnectRetryCount() * this.mAlivcLivePushConfig.getConnectRetryInterval()) / 1000;
            t.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
        start5Interval();
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void restartPushAync() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->restartPushAync");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPreviewView == null && this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() == null && !this.mAlivcLivePushConfig.isAudioOnly()) {
            throw new IllegalArgumentException("illegal argument");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.ERROR) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        int restartPush = mNativeAlivcLivePusher.restartPush(this.mPreviewView.getHolder().getSurface(), false, 1000);
        if (restartPush != 0) {
            throw new IllegalStateException("restart push aysnc error");
        }
        this.mPushStatus = AlivcLivePushStats.RESTARTING;
        mNativeAlivcLivePusher.addPushImage(this.mAlivcLivePushConfig.getPausePushImage() == null ? "" : this.mAlivcLivePushConfig.getPausePushImage(), this.mAlivcLivePushConfig.getNetworkPoorPushImage() != null ? this.mAlivcLivePushConfig.getNetworkPoorPushImage() : "");
        recordFunction(this.mContext, "RestartPush -- Sync:Async Return " + restartPush);
        stop5Interval();
        t.a aVar = new t.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.b = d.b(performanceMap, "mVideoDurationFromeCaptureToUpload");
            aVar.a = d.b(performanceMap, "mAudioDurationFromeCaptureToUpload");
            aVar.c = this.mAlivcLivePushConfig.getResolution().toString().substring(11);
            aVar.d = this.mAlivcLivePushConfig.getAudioChannels() == 1 ? "single" : "dual";
            aVar.e = this.mAlivcLivePushConfig.isAudioOnly();
            aVar.f = this.mAlivcLivePushConfig.getVideoEncodeMode().equals(AlivcEncodeModeEnum.Encode_MODE_HARD);
            aVar.g = this.mAlivcLivePushConfig.getWaterMarkInfos().size();
            aVar.h = this.mAlivcLivePushConfig.isPushMirror();
            aVar.i = this.mAlivcLivePushConfig.getFps();
            aVar.j = this.mAlivcLivePushConfig.getInitialVideoBitrate();
            aVar.k = this.mAlivcLivePushConfig.getTargetVideoBitrate();
            aVar.l = this.mAlivcLivePushConfig.getMinVideoBitrate();
            aVar.m = this.mAlivcLivePushConfig.getAudioSamepleRate().getAudioSampleRate();
            aVar.n = this.mAlivcLivePushConfig.getPreviewOrientation();
            aVar.o = this.mAlivcLivePushConfig.getCameraType();
            aVar.p = this.mAlivcLivePushConfig.isBeautyOn();
            aVar.q = this.mAlivcLivePushConfig.getBeautyWhite();
            aVar.r = this.mAlivcLivePushConfig.getBeautyBuffing();
            aVar.s = this.mAlivcLivePushConfig.getBeautyRuddy();
            aVar.t = this.mAlivcLivePushConfig.getBeautyThinFace();
            aVar.w = this.mAlivcLivePushConfig.getBeautyCheekPink();
            aVar.u = this.mAlivcLivePushConfig.getBeautyShortenFace();
            aVar.v = this.mAlivcLivePushConfig.getBeautyBigEye();
            aVar.x = this.mAlivcLivePushConfig.isFlash();
            aVar.y = this.mAlivcLivePushConfig.getConnectRetryCount();
            aVar.z = this.mAlivcLivePushConfig.getConnectRetryInterval();
            aVar.A = this.mAlivcLivePushConfig.isPreviewMirror();
            aVar.B = this.mAlivcLivePushConfig.getVideoEncodeGop();
            aVar.C = (this.mAlivcLivePushConfig.getConnectRetryCount() * this.mAlivcLivePushConfig.getConnectRetryInterval()) / 1000;
            t.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
        start5Interval();
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void resume() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->resume");
        if (this.mAlivcLivePushConfig.isExternMainStream()) {
            return;
        }
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PAUSED && this.mPushStatus != AlivcLivePushStats.ERROR) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.resume(true);
        recordFunction(this.mContext, "Resume -- Sync:sync");
        u.a aVar = new u.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
            aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
            aVar.c = System.currentTimeMillis() - r.a;
            u.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
        this.mPushStatus = mNativeAlivcLivePusher.isPushing() ? AlivcLivePushStats.PUSHED : AlivcLivePushStats.PREVIEWED;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void resumeAsync() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->resumeAsync");
        if (this.mAlivcLivePushConfig.isExternMainStream()) {
            return;
        }
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PAUSED && this.mPushStatus != AlivcLivePushStats.ERROR) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mPushStatus = AlivcLivePushStats.RESUMING;
        mNativeAlivcLivePusher.resume(false);
        recordFunction(this.mContext, "StopPush -- Sync:Async");
        u.a aVar = new u.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
            aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
            aVar.c = System.currentTimeMillis() - r.a;
            u.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void resumeBGM() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Invalid parameter");
        }
        if (this.mPlayStats != AlivcLivePlayStats.PAUSED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePlayStats.values()[this.mPlayStats.ordinal()]);
        }
        mNativeAlivcLivePusher.resumeBGM();
        recordFunction(this.mContext, "ResumeBGM");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void resumeScreenCapture() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PAUSED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.resumeScreenCapture();
        recordFunction(this.mContext, "Resume -- Sync:sync");
        u.a aVar = new u.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
            aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
            aVar.c = System.currentTimeMillis() - r.a;
            u.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
        this.mPushStatus = mNativeAlivcLivePusher.isPushing() ? AlivcLivePushStats.PUSHED : AlivcLivePushStats.PREVIEWED;
    }

    public void sendMessage(String str, int i, int i2, boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        if (!livePusherJNI.isPushing()) {
            throw new IllegalStateException("Status error, status should be PUSHED");
        }
        if (!str.isEmpty() && str.length() > 4000) {
            throw new IllegalArgumentException("The maximum length is 4000");
        }
        LivePusherJNI livePusherJNI2 = mNativeAlivcLivePusher;
        if (livePusherJNI2 != null) {
            livePusherJNI2.addSeiInfo(str, i, i2, z);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setAudioDenoise(boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setAudioDenoise(z);
            recordFunction(this.mContext, "SetAudioDenoise -- on:" + z);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setAutoFocus(boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setAutoFocus auto: " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setCameraFocus(z, 0.0f, 0.0f);
        recordFunction(this.mContext, "SetAutoFocus -- autoFocus:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBGMEarsBack(boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setEarsBack(z);
        }
        if (z) {
            d.a aVar = new d.a();
            aVar.b = getBlueToothHeadSetOn() ? 1 : 0;
            aVar.a = getHeadSetPlugOn() ? 1 : 0;
            com.alivc.live.pusher.a.d.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        } else {
            c.a aVar2 = new c.a();
            aVar2.b = getBlueToothHeadSetOn() ? 1 : 0;
            aVar2.a = getHeadSetPlugOn() ? 1 : 0;
            com.alivc.live.pusher.a.c.a(this.mAlivcEventPublicParam, aVar2, this.mContext);
        }
        recordFunction(this.mContext, "SetBGMEarsBack -- isOpen:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBGMLoop(boolean z) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setBGMLoop(z);
        }
        if (z) {
            f.a(this.mAlivcEventPublicParam, new f.a(), this.mContext);
        } else {
            com.alivc.live.pusher.a.e.a(this.mAlivcEventPublicParam, new e.a(), this.mContext);
        }
        recordFunction(this.mContext, "SetBGMLoop -- isLoop:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBGMVolume(int i) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            this.mBGMVolume = i;
            if (i > 0 && i < 10) {
                i = 10;
            }
            livePusherJNI.setBackgroundMusicVolume(i / 10);
            recordFunction(this.mContext, "SetBGMVolume -- volume:" + i);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyBigEye(int i) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mBigEye = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyBigEye -- bigEye:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyBrightness(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setBeautyBrightness brightness: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mBright = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyBrightness -- beautyBrightness:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyBuffing(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setBeautyBuffing buffing: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mSkinSmooth = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyBuffing -- beautyBuffing:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyCheekPink(int i) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mCheekPink = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyCheekPink -- cheekpink:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyOn(boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setBeautyOn beautyOn : " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PAUSED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setBeauty(z, 0, 0);
        recordFunction(this.mContext, "SetBeautyOn -- beautyOn:" + z);
        this.mAlivcLivePushConfig.setBeautyOn(z);
        if (z) {
            j.a(this.mAlivcEventPublicParam, new j.a(), this.mContext);
            return;
        }
        i.a(this.mAlivcEventPublicParam, new i.a(), this.mContext);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyRuddy(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setBeautyRuddy ruddy: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mWholePink = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyRuddy -- beautyRuddy:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyShortenFace(int i) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mShortenFace = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyShortenFace -- shortenFace:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautySlimFace(int i) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mSlimFace = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautySlimFace -- slimFace:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setBeautyWhite(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setBeautyWhite white: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mWhiten = i;
        setFaceBeauty();
        recordFunction(this.mContext, "SetBeautyWhite -- beautyWhite:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setCaptureVolume(int i) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            this.mCaptureVolume = i;
            if (i > 0 && i < 10) {
                i = 10;
            }
            livePusherJNI.setAudioVolume(i / 10);
            recordFunction(this.mContext, "SetCaptureVolume -- volume:" + i);
        }
    }

    public void setCustomDetect(AlivcLivePushCustomDetect alivcLivePushCustomDetect) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setCustomDetect(alivcLivePushCustomDetect);
            DebugViewManager.recordFunction(String.format(this.mDebugFunctionText, d.b(), String.valueOf(Thread.currentThread().getId())));
        }
    }

    public void setCustomFilter(AlivcLivePushCustomFilter alivcLivePushCustomFilter) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setCustomFilter(alivcLivePushCustomFilter);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setExposure(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setExposure exposure: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setExposureCompensation(i);
        recordFunction(this.mContext, "setExposure exposure: " + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setFlash(boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setFlash flash: " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setFlash(z);
        this.mAlivcLivePushConfig.setFlash(z);
        if (z) {
            n.a(this.mAlivcEventPublicParam, new n.a(), this.mContext);
        } else {
            m.a(this.mAlivcEventPublicParam, new m.a(), this.mContext);
        }
        recordFunction(this.mContext, "SetFlash -- flash:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setLivePushBGMListener(AlivcLivePushBGMListener alivcLivePushBGMListener) {
        this.mPushBGMListener = alivcLivePushBGMListener;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setLivePushErrorListener(AlivcLivePushErrorListener alivcLivePushErrorListener) {
        this.mPushErrorListener = alivcLivePushErrorListener;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setLivePushInfoListener(AlivcLivePushInfoListener alivcLivePushInfoListener) {
        this.mPushInfoListener = alivcLivePushInfoListener;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setLivePushNetworkListener(AlivcLivePushNetworkListener alivcLivePushNetworkListener) {
        this.mPushNetworkListener = alivcLivePushNetworkListener;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setLivePushRenderContextListener(AlivcLivePusherRenderContextListener alivcLivePusherRenderContextListener) {
        this.mRenderContextListener = alivcLivePusherRenderContextListener;
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setLogLevel(AlivcLivePushLogLevel alivcLivePushLogLevel) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setLogLevel");
        if (mNativeAlivcLivePusher != null) {
            if (alivcLivePushLogLevel.getLevel() > AlivcLivePushLogLevel.AlivcLivePushLogLevelWarn.getLevel()) {
                LogUtil.disableDebug();
            } else {
                LogUtil.enalbeDebug();
            }
            mNativeAlivcLivePusher.setLogLevel(alivcLivePushLogLevel.getLevel());
            recordFunction(this.mContext, "setLogLevel -- level:" + alivcLivePushLogLevel.getLevel());
        }
    }

    public void setMainStreamPosition(float f, float f2, float f3, float f4) {
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setMainStreamPosition(f, f2, f3, f4);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setMinVideoBitrate(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setMinVideoBitrate minVideoBitrate: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setVideoBitrateRange(i, 0, 0);
        recordFunction(this.mContext, "SetMinVideoBitrate -- minVideoBitrate:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setMute(boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setMute mute: " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PAUSED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setMute(z);
        this.mMute = z;
        if (z) {
            q.a(this.mAlivcEventPublicParam, new q.a(), this.mContext);
        } else {
            p.a(this.mAlivcEventPublicParam, new p.a(), this.mContext);
        }
        recordFunction(this.mContext, "SetMute -- mute:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setPreviewMirror(boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setPreviewMirror previewMirror: " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setPreviewMirror(z);
        recordFunction(this.mContext, "SetPreviewMirror -- mirror:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setPreviewMode(AlivcPreviewDisplayMode alivcPreviewDisplayMode) {
        if (mNativeAlivcLivePusher == null || this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
            return;
        }
        mNativeAlivcLivePusher.setDisplayMode(alivcPreviewDisplayMode.getPreviewDisplayMode());
        recordFunction(this.mContext, "SetQualityMode -- mode:" + alivcPreviewDisplayMode.ordinal());
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setPreviewOrientation(AlivcPreviewOrientationEnum alivcPreviewOrientationEnum) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setPreviewOrientation");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setOrientaion(alivcPreviewOrientationEnum.getOrientation());
            recordFunction(this.mContext, "SetPreviewOrientation -- orientation:" + alivcPreviewOrientationEnum.getOrientation());
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setPushMirror(boolean z) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setPushMirror pushMirror: " + z);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setPusherMirror(z);
        recordFunction(this.mContext, "SetPushMirror -- mirror:" + z);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setQualityMode(AlivcQualityModeEnum alivcQualityModeEnum) {
        if (alivcQualityModeEnum.equals(AlivcQualityModeEnum.QM_CUSTOM)) {
            throw new IllegalStateException("Cannot set QM_CUSTOM dynamically");
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setQualityMode(alivcQualityModeEnum.getQualityMode());
            recordFunction(this.mContext, "SetQualityMode -- mode:" + alivcQualityModeEnum.ordinal());
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setScreenOrientation(int i) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Please excute init first ");
        }
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() == null) {
            throw new IllegalStateException("Not in ScreenCapture Mode");
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setScreenOrientation(i);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setTargetVideoBitrate(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setTargetVideoBitrate targetVideoBitrate: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        mNativeAlivcLivePusher.setVideoBitrateRange(0, i, i);
        recordFunction(this.mContext, "SetTargetVideoBitrate -- targetVideoBitrate:" + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setWaterMarkVisible(boolean z) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.setWaterMarkVisible(z);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void setZoom(int i) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->setZoom zoom: " + i);
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        recordFunction(this.mContext, "SetZoom -- zoom:" + i + " Return:" + mNativeAlivcLivePusher.setCameraZoom(i));
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void snapshot(int i, int i2, AlivcSnapshotListener alivcSnapshotListener) {
        this.mSnapshotListener = alivcSnapshotListener;
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.snapshot(i, i2, this.mInnerSnapshotListener);
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void startBGMAsync(String str) {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Invalid parameter");
        }
        if (this.mPushStatus == AlivcLivePushStats.IDLE || this.mPushStatus == AlivcLivePushStats.ERROR) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        if (this.mPlayStats == AlivcLivePlayStats.IDLE || this.mPlayStats == AlivcLivePlayStats.STOPED) {
            h.a aVar = new h.a();
            aVar.b = getBGMVolume();
            aVar.a = getCaptureVolume();
            h.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
        mNativeAlivcLivePusher.resumeBGM();
        mNativeAlivcLivePusher.stopBGM();
        mNativeAlivcLivePusher.startBGMAsync(str);
        recordFunction(this.mContext, "StartBGMAsync -- path:" + str);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int startCamera(SurfaceView surfaceView) {
        if (mNativeAlivcLivePusher != null) {
            if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
                if (this.mScreenStatus.equals(ScreenRecordStatus.SCREEN_RECORD_NORMAL)) {
                    if (mNativeAlivcLivePusher != null) {
                        int startCamera = mNativeAlivcLivePusher.startCamera(surfaceView != null ? surfaceView.getHolder().getSurface() : null);
                        this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_CAMERA_START;
                        return startCamera;
                    }
                    return -1;
                }
                throw new IllegalStateException("you should start push screen first");
            }
            throw new IllegalStateException("Not in ScreenCapture Mode");
        }
        throw new IllegalStateException("Please excute init first ");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public int startCameraMix(float f, float f2, float f3, float f4) {
        if (mNativeAlivcLivePusher != null) {
            if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
                if (this.mScreenStatus.equals(ScreenRecordStatus.SCREEN_RECORD_CAMERA_START)) {
                    LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
                    if (livePusherJNI != null) {
                        int startCameraMix = livePusherJNI.startCameraMix(f, f2, f3, f4);
                        this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_CAMERA_MIX_START;
                        return startCameraMix;
                    }
                    return -1;
                }
                throw new IllegalStateException("You should start camera first");
            }
            throw new IllegalStateException("Not in ScreenCapture Mode");
        }
        throw new IllegalStateException("Please excute init first ");
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void startPreview(SurfaceView surfaceView) {
        int i;
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->startPreview");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.INIT && this.mPushStatus != AlivcLivePushStats.PREVIEWED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mPreviewView = surfaceView;
        if (surfaceView == null) {
            i = mNativeAlivcLivePusher.startPreview(null, true);
            if (i != 0) {
                throw new IllegalStateException("start preview error");
            }
            this.mPushStatus = AlivcLivePushStats.PREVIEWED;
        } else {
            int startPreview = mNativeAlivcLivePusher.startPreview(surfaceView.getHolder().getSurface(), true);
            if (startPreview != 0) {
                throw new IllegalStateException("start preview error");
            }
            surfaceView.getHolder().addCallback(this.mPreviewCallback);
            this.mPushStatus = AlivcLivePushStats.PREVIEWED;
            i = startPreview;
        }
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
            mNativeAlivcLivePusher.setBeauty(false, 0, 0);
        }
        recordFunction(this.mContext, "StartPreview -- Sync:Sync Return " + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void startPreviewAysnc(SurfaceView surfaceView) {
        int i;
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->startPreviewAysnc");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.INIT && this.mPushStatus != AlivcLivePushStats.PREVIEWED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        this.mPushStatus = AlivcLivePushStats.PREVIEING;
        this.mPreviewView = surfaceView;
        if (surfaceView == null) {
            i = mNativeAlivcLivePusher.startPreview(null, false);
            if (i != 0) {
                throw new IllegalStateException("start preview aysnc error");
            }
            this.mPushStatus = AlivcLivePushStats.PREVIEWED;
        } else {
            int startPreview = mNativeAlivcLivePusher.startPreview(surfaceView.getHolder().getSurface(), false);
            if (startPreview != 0) {
                throw new IllegalStateException("start preview aysnc error");
            }
            surfaceView.getHolder().addCallback(this.mPreviewCallback);
            this.mPushStatus = AlivcLivePushStats.PREVIEWED;
            i = startPreview;
        }
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
            mNativeAlivcLivePusher.setBeauty(false, 0, 0);
        }
        recordFunction(this.mContext, "StartPreview -- Sync:Async Return " + i);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void startPush(String str) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->startPush");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.INIT && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        if (this.mPushStatus.equals(AlivcLivePushStats.INIT)) {
            this.mPreviewView = null;
            if (mNativeAlivcLivePusher.startPreview(null, true) != 0) {
                throw new IllegalStateException("start push error : create gl resource failed");
            }
        }
        mPushUrl = str;
        if (str.contains(AUTH_KEY)) {
            getNetworkTime();
        }
        AlivcEventPublicParam alivcEventPublicParam = this.mAlivcEventPublicParam;
        if (alivcEventPublicParam != null) {
            alivcEventPublicParam.setVideoUrl(str);
        }
        int startPush = mNativeAlivcLivePusher.startPush(str, true);
        if (startPush != 0) {
            throw new IllegalStateException("start push error");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED) {
            this.mPushStatus = AlivcLivePushStats.PUSHED;
            if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
                this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_NORMAL;
            }
        }
        mNativeAlivcLivePusher.addPushImage(this.mAlivcLivePushConfig.getPausePushImage() == null ? "" : this.mAlivcLivePushConfig.getPausePushImage(), this.mAlivcLivePushConfig.getNetworkPoorPushImage() != null ? this.mAlivcLivePushConfig.getNetworkPoorPushImage() : "");
        start5Interval();
        recordFunction(this.mContext, "StartPush -- Sync:Sync Return " + startPush);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void startPushAysnc(String str) {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->startPushAysnc");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.INIT && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        if (this.mPushStatus.equals(AlivcLivePushStats.INIT)) {
            this.mPreviewView = null;
            if (mNativeAlivcLivePusher.startPreview(null, true) != 0) {
                throw new IllegalStateException("start push error : create gl resource failed");
            }
        }
        mPushUrl = str;
        if (str.contains(AUTH_KEY)) {
            getNetworkTime();
        }
        AlivcEventPublicParam alivcEventPublicParam = this.mAlivcEventPublicParam;
        if (alivcEventPublicParam != null) {
            alivcEventPublicParam.setVideoUrl(str);
        }
        int startPush = mNativeAlivcLivePusher.startPush(str, false);
        if (startPush != 0) {
            throw new IllegalStateException("start push aysnc error");
        }
        if (this.mPushStatus != AlivcLivePushStats.PUSHED) {
            this.mPushStatus = AlivcLivePushStats.PUSHED;
            if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
                this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_NORMAL;
            }
        }
        mNativeAlivcLivePusher.addPushImage(this.mAlivcLivePushConfig.getPausePushImage() == null ? "" : this.mAlivcLivePushConfig.getPausePushImage(), this.mAlivcLivePushConfig.getNetworkPoorPushImage() != null ? this.mAlivcLivePushConfig.getNetworkPoorPushImage() : "");
        start5Interval();
        recordFunction(this.mContext, "StartPush -- Sync:Async Return " + startPush);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void stopBGMAsync() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Invalid parameter");
        }
        if (this.mPlayStats == AlivcLivePlayStats.IDLE) {
            throw new IllegalStateException("status error current state is " + AlivcLivePlayStats.values()[this.mPlayStats.ordinal()]);
        }
        mNativeAlivcLivePusher.stopBGM();
        recordFunction(this.mContext, "StopBGM -- Sync:Async");
        g.a aVar = new g.a();
        aVar.b = getBGMVolume();
        aVar.a = getCaptureVolume();
        g.a(this.mAlivcEventPublicParam, aVar, this.mContext);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void stopCamera() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Please excute init first ");
        }
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() == null) {
            throw new IllegalStateException("Not in ScreenCapture Mode");
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.stopCamera();
            this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_NORMAL;
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void stopCameraMix() {
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Please excute init first ");
        }
        if (this.mAlivcLivePushConfig.getMediaProjectionPermissionResultData() == null) {
            throw new IllegalStateException("Not in ScreenCapture Mode");
        }
        LivePusherJNI livePusherJNI = mNativeAlivcLivePusher;
        if (livePusherJNI != null) {
            livePusherJNI.stopCameraMix();
            this.mScreenStatus = ScreenRecordStatus.SCREEN_RECORD_CAMERA_START;
        }
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void stopPreview() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->stopPreview");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        this.mDynamicAddsonCount = 0;
        if (this.mPushStatus != AlivcLivePushStats.INIT && this.mPushStatus != AlivcLivePushStats.PREVIEWED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        int stopPreview = mNativeAlivcLivePusher.stopPreview();
        if (stopPreview != 0) {
            throw new IllegalStateException("stop preview error");
        }
        this.mPushStatus = AlivcLivePushStats.INIT;
        recordFunction(this.mContext, "StopPreview -- Sync:Sync Return " + stopPreview);
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void stopPush() {
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->stopPush");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("Illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED && this.mPushStatus != AlivcLivePushStats.PAUSED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        int stopPush = mNativeAlivcLivePusher.stopPush();
        if (stopPush != 0) {
            throw new IllegalStateException("stop push error");
        }
        this.mPushStatus = AlivcLivePushStats.PREVIEWED;
        if (this.mPreviewView == null) {
            mNativeAlivcLivePusher.stopPreview();
            this.mPushStatus = AlivcLivePushStats.INIT;
        }
        recordFunction(this.mContext, "StopPush -- Sync:Sync Return " + stopPush);
        w.a aVar = new w.a();
        Map<String, String> performanceMap = getPerformanceMap();
        if (performanceMap != null) {
            aVar.a = d.b(performanceMap, "mTotalSizeOfUploadedPackets");
            aVar.b = d.b(performanceMap, "mTotalTimeOfPublishing");
            w.a(this.mAlivcEventPublicParam, aVar, this.mContext);
        }
        AlivcEventPublicParam alivcEventPublicParam = this.mAlivcEventPublicParam;
        if (alivcEventPublicParam != null) {
            alivcEventPublicParam.setVideoUrl(null);
        }
        stop5Interval();
    }

    @Override // com.alivc.live.pusher.ILivePusher
    public void switchCamera() {
        AlivcLivePushConfig alivcLivePushConfig;
        AlivcLivePushCameraTypeEnum alivcLivePushCameraTypeEnum;
        LogUtil.d("AlivcLivePusher", "AlivcLivePusher-->switchCamera");
        if (mNativeAlivcLivePusher == null) {
            throw new IllegalStateException("illegal State, you should init first");
        }
        if (this.mPushStatus != AlivcLivePushStats.PREVIEWED && this.mPushStatus != AlivcLivePushStats.PUSHED) {
            throw new IllegalStateException("status error current state is " + AlivcLivePushStats.values()[this.mPushStatus.ordinal()]);
        }
        recordFunction(this.mContext, "SwitchCamera -- Retrun:" + mNativeAlivcLivePusher.switchCamera());
        if (this.mAlivcLivePushConfig.getCameraType() == AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT.getCameraId()) {
            alivcLivePushConfig = this.mAlivcLivePushConfig;
            alivcLivePushCameraTypeEnum = AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK;
        } else {
            alivcLivePushConfig = this.mAlivcLivePushConfig;
            alivcLivePushCameraTypeEnum = AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT;
        }
        alivcLivePushConfig.setCameraType(alivcLivePushCameraTypeEnum);
        x.a aVar = new x.a();
        aVar.a = this.mAlivcLivePushConfig.getCameraType() == 0 ? "back" : AccountConfig.FaceIDRegisterAction.ORIENTATION_FRONT;
        x.a(this.mAlivcEventPublicParam, aVar, this.mContext);
    }
}
