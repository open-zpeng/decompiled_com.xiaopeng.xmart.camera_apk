package com.xiaopeng.xmart.camera;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.lifecycle.Observer;
import com.xiaopeng.lib.bughunter.StartPerfUtils;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.speech.vui.actor.VuiEventHandler;
import com.xiaopeng.vui.commons.model.VuiElement;
import com.xiaopeng.vui.commons.model.VuiEvent;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.ClickHelper;
import com.xiaopeng.xmart.camera.helper.SpeechHelper;
import com.xiaopeng.xmart.camera.helper.SpeechOperationHelper;
import com.xiaopeng.xmart.camera.model.camera.StartRecordMode;
import com.xiaopeng.xmart.camera.model.camera.TakePictureMode;
import com.xiaopeng.xmart.camera.speech.AvmControllerVuiEventActor;
import com.xiaopeng.xmart.camera.utils.ActivityUtils;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camera.utils.ToastUtils;
import com.xiaopeng.xmart.camera.view.controlcamera.ICameraAvmControllerView;
import com.xiaopeng.xmart.camera.view.controlpanel.CameraControlPanelView;
import com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView;
import com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
import com.xiaopeng.xmart.camera.vm.ViewModelManager;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xui.theme.XThemeManager;
import com.xiaopeng.xui.vui.floatinglayer.VuiFloatingLayerManager;
import com.xiaopeng.xui.widget.XImageView;
/* loaded from: classes.dex */
public class MainActivity extends BaseActivity implements IVuiCameraView {
    private static final String TAG = "CameraMainActivity";
    private boolean mActivityRecreating;
    private ICameraAvmControllerView mAvmControllerView;
    private AvmViewModel mAvmViewModel;
    private boolean mBroadcastRegistered;
    private int mCameraId;
    private CameraControlPanelView mControlPanel;
    private boolean mEnterAutoPilot;
    protected GeneralCameraPreview mGeneralPreview;
    private boolean mIGOFF2KillSelf;
    private boolean mIsSurfaceAvaliable;
    private int mTargetCameraType;
    private View mViewAvmRectRt;
    private boolean mNeedClean = false;
    private ActivityChangeReceiver mActivityChangeReceiver = new ActivityChangeReceiver();
    private GeneralCameraPreview.PreviewStatusListener mPreviewStatusListener = new GeneralCameraPreview.PreviewStatusListener() { // from class: com.xiaopeng.xmart.camera.MainActivity.1
        @Override // com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview.PreviewStatusListener
        public void onPreviewCreated() {
            CameraLog.i(MainActivity.TAG, "---onPreviewCreated mEnterAutoPilot:" + MainActivity.this.mEnterAutoPilot, false);
            if (!MainActivity.this.mEnterAutoPilot) {
                if (!MainActivity.this.mActivityRecreating) {
                    MainActivity.this.mIsSurfaceAvaliable = true;
                    if (MainActivity.this.mGeneralPreview == null) {
                        CameraLog.i(MainActivity.TAG, "---onPreviewCreated mGeneralPreview null", false);
                        return;
                    }
                    int numberOfCameras = Camera.getNumberOfCameras();
                    CameraLog.i(MainActivity.TAG, "camera num:" + numberOfCameras, false);
                    if (numberOfCameras > 0) {
                        MainActivity.this.startPreview();
                        return;
                    } else {
                        MainActivity.this.mGeneralPreview.onCameraError();
                        return;
                    }
                }
                CameraLog.i(MainActivity.TAG, "activity recreating,return", false);
                return;
            }
            MainActivity.this.finish();
        }

        @Override // com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview.PreviewStatusListener
        public void onPreviewChanged() {
            CameraLog.i(MainActivity.TAG, "---onPreviewChanged", false);
        }

        @Override // com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview.PreviewStatusListener
        public void onPreviewDestroyed() {
            MainActivity.this.mIsSurfaceAvaliable = false;
            boolean isRecording = MainActivity.this.mAvmViewModel.isRecording();
            CameraLog.d(MainActivity.TAG, "---onPreviewDestroyed isRecording:" + isRecording, false);
            if (isRecording) {
                MainActivity.this.mAvmViewModel.stopRecord();
            }
            MainActivity.this.stopPreviewAndReleaseCamera();
        }
    };
    private Observer<Boolean> mShowPreviewViewObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$j91pGoDBEE-B3kIk29SLk8eaRyw
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$0$MainActivity((Boolean) obj);
        }
    };
    protected Runnable mHidePreviewCoverRunnable = new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$boZ0_FKswhwLdi9nZXbF_KHiZoY
        @Override // java.lang.Runnable
        public final void run() {
            MainActivity.this.lambda$new$1$MainActivity();
        }
    };
    private Observer<Boolean> mCameraPreviewObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$PKrpjiiac-6d56A1VXWnBvo6JO0
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$2$MainActivity((Boolean) obj);
        }
    };
    private Observer<Boolean> mAvmSwitchFailObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$i6HWUMBvjm2cqmmuK8n58mKPnuo
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.lambda$new$3((Boolean) obj);
        }
    };
    private Observer<Boolean> mCamera360ChangeObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$4b6335iy7AjtH3Tzm0dJGic9MnM
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$4$MainActivity((Boolean) obj);
        }
    };
    private Observer<Boolean> mIgOffObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$rXNy3zcRA1Oiqcok41tQs4d0kr8
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$5$MainActivity((Boolean) obj);
        }
    };
    private Observer<Boolean> mCameraReleaseObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$1zdkywsQpNtlJj1VFOwj0uXPYxw
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$6$MainActivity((Boolean) obj);
        }
    };
    private Observer<TakePictureMode> mTakePicObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$OubM8fGlkPRPTe8SMnJd_MsMHao
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$7$MainActivity((TakePictureMode) obj);
        }
    };
    private Observer<StartRecordMode> mRecordObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$_zId7W8WPhaWhkLp8yv1CgjPG4A
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$8$MainActivity((StartRecordMode) obj);
        }
    };
    private Observer<Boolean> mRecordTimeObserver = new Observer() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$wQ_W4d6GkF3sD5sNeAVq3mXg8k8
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            MainActivity.this.lambda$new$9$MainActivity((Boolean) obj);
        }
    };
    private ICameraControlPanelView.OnCameraModeChangeListener mOnCameraModeChangeListener = new ICameraControlPanelView.OnCameraModeChangeListener() { // from class: com.xiaopeng.xmart.camera.MainActivity.2
        @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView.OnCameraModeChangeListener
        public void onCameraModeChange(boolean isPhoto) {
            CameraLog.d(MainActivity.TAG, "onCameraModeChange, isPhoto: " + isPhoto, false);
            if (MainActivity.this.mGeneralPreview != null) {
                MainActivity.this.mGeneralPreview.onShotModeChanged(isPhoto);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ActivityChangeReceiver extends BroadcastReceiver {
        private ActivityChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MainActivity.this.mEnterAutoPilot || !intent.getAction().equals("com.xiaopeng.intent.action.ACTIVITY_CHANGED")) {
                return;
            }
            String stringExtra = intent.getStringExtra("android.intent.extra.COMPONENT");
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            ComponentName unflattenFromString = ComponentName.unflattenFromString(stringExtra);
            CameraLog.d(MainActivity.TAG, "ACTION_ACTIVITY_CHANGED", false);
            if (unflattenFromString.getPackageName().equals(IpcConfig.App.CAR_AUTOPILOT)) {
                CameraLog.d(MainActivity.TAG, "enter autopilot.", false);
                MainActivity.this.exitForEnterAutopilot();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        StartPerfUtils.onCreateBegin();
        super.onCreate(savedInstanceState);
        CameraLog.d(TAG, "onCreate avm:" + CarCameraHelper.getInstance().hasAVM(), false);
        getWindow().addFlags(1);
        setContentView(R.layout.activity_main);
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
        initCameraId();
        initObserve();
        initView();
        checkAction(3000L);
        uploadStartBI();
        setWindowBackGround();
        initScene();
        StartPerfUtils.onCreateEnd();
    }

    private void initCameraId() {
        String xpCduType = Config.getXpCduType();
        xpCduType.hashCode();
        if (xpCduType.equals("QB")) {
            this.mCameraId = 0;
        } else {
            this.mCameraId = 2;
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CameraLog.d(TAG, "onConfigurationChanged", false);
        XThemeManager.onConfigurationChanged(newConfig, this, getWindow().getDecorView(), "activity_main.xml", null);
        if (ThemeManager.isThemeChanged(newConfig)) {
            setWindowBackGround();
        }
    }

    private void setWindowBackGround() {
        CameraLog.d(TAG, "setWindowBackGround", false);
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.window_bg));
    }

    public /* synthetic */ void lambda$new$0$MainActivity(Boolean bool) {
        observerHidePreviewCover(bool.booleanValue());
    }

    private void observerHidePreviewCover(boolean show) {
        CameraLog.i(TAG, "observerHidePreviewCover show:" + show, false);
        if (show) {
            showPreviewCover();
            return;
        }
        ThreadPoolHelper.getInstance().removeCallbacksOnMainThread(this.mHidePreviewCoverRunnable);
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(this.mHidePreviewCoverRunnable, CameraDefine.HIDE_PREVIEW_COVER_DELAY);
    }

    public /* synthetic */ void lambda$new$2$MainActivity(Boolean bool) {
        GeneralCameraPreview generalCameraPreview;
        CameraLog.i(TAG, "CameraPreview cameraPreview:" + bool, false);
        if (bool.booleanValue() || (generalCameraPreview = this.mGeneralPreview) == null) {
            return;
        }
        generalCameraPreview.onCameraError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$3(Boolean bool) {
        CameraLog.i(TAG, "mAvmSwitchFailObserver switchFail:" + bool, false);
        if (bool.booleanValue()) {
            ToastUtils.showToast((int) R.string.camera_switch_error_title);
        }
    }

    public /* synthetic */ void lambda$new$4$MainActivity(Boolean bool) {
        CameraLog.i(TAG, "onCamera360Change change:" + bool, false);
        if (!bool.booleanValue() || this.mAvmControllerView == null) {
            return;
        }
        View view = this.mViewAvmRectRt;
        if (view != null) {
            view.setSoundEffectsEnabled(CarCameraHelper.getInstance().hasAvmTopRightSoundEffects());
        }
        this.mAvmControllerView.onCamera360Change();
        this.mGeneralPreview.changeTransparentVisible();
    }

    public /* synthetic */ void lambda$new$5$MainActivity(Boolean bool) {
        CameraLog.i(TAG, "mIgOffObserver igOff:" + bool, false);
        if (bool.booleanValue()) {
            onIgOff();
        }
    }

    public /* synthetic */ void lambda$new$6$MainActivity(Boolean bool) {
        CameraLog.i(TAG, "mCameraReleaseObserver " + bool, false);
        if (bool.booleanValue()) {
            onCameraRelease();
        }
    }

    public /* synthetic */ void lambda$new$7$MainActivity(TakePictureMode takePictureMode) {
        CameraLog.i(TAG, "mTakePicState " + takePictureMode, false);
        if (takePictureMode == TakePictureMode.Start) {
            onTakingPicture();
        } else if (takePictureMode == TakePictureMode.End) {
            onPictureTaken(this.mAvmViewModel.getCurPath());
        }
    }

    public /* synthetic */ void lambda$new$8$MainActivity(StartRecordMode startRecordMode) {
        CameraLog.i(TAG, "mRecordState " + startRecordMode, false);
        if (startRecordMode == StartRecordMode.Start) {
            onRecordStart();
        } else if (startRecordMode == StartRecordMode.End) {
            onRecordStop(this.mAvmViewModel.getCurPath());
        } else if (startRecordMode == StartRecordMode.Fail) {
            onRecordError();
        }
    }

    public /* synthetic */ void lambda$new$9$MainActivity(Boolean bool) {
        CameraLog.i(TAG, "evenNumber:" + bool, false);
        this.mGeneralPreview.onRecordTimeTick(this.mAvmViewModel.getRecordTime(), bool.booleanValue());
    }

    private void initObserve() {
        this.mAvmViewModel.getShowPreviewCover().observe(this, this.mShowPreviewViewObserver);
        this.mAvmViewModel.getCameraPreview().observe(this, this.mCameraPreviewObserver);
        this.mAvmViewModel.getAvmSwitchFail().observe(this, this.mAvmSwitchFailObserver);
        this.mAvmViewModel.getCamera360Change().observe(this, this.mCamera360ChangeObserver);
        this.mAvmViewModel.getTakePicState().observe(this, this.mTakePicObserver);
        this.mAvmViewModel.getStartRecordState().observe(this, this.mRecordObserver);
        this.mAvmViewModel.getRecordTimeIsEvenNumber().observe(this, this.mRecordTimeObserver);
        this.mAvmViewModel.getCameraRelease().observe(this, this.mCameraReleaseObserver);
        this.mAvmViewModel.getIgOff().observe(this, this.mIgOffObserver);
    }

    @Override // com.xiaopeng.xmart.camera.BaseActivity
    protected void initView() {
        CameraLog.i(TAG, "---initView", false);
        this.mRootView = (ViewGroup) findViewById(R.id.view_root);
        this.mViewAvmRectRt = findViewById(R.id.avm_rect_rt);
        this.mGeneralPreview = (GeneralCameraPreview) findViewById(R.id.camera_general_preview);
        this.mControlPanel = new CameraControlPanelView(this, this.mRootView);
        if (CarCameraHelper.getInstance().isPortraitScreen() || CarCameraHelper.getInstance().hasAVM()) {
            ((ViewStub) findViewById(R.id.avm_controller_layout)).inflate();
            this.mAvmControllerView = (ICameraAvmControllerView) findViewById(R.id.view_avm_camera_controller);
        }
        setListener();
    }

    private void setListener() {
        this.mGeneralPreview.setPreviewStatusListener(this.mPreviewStatusListener);
        View view = this.mViewAvmRectRt;
        if (view != null) {
            view.setSoundEffectsEnabled(CarCameraHelper.getInstance().hasAvmTopRightSoundEffects());
            CameraLog.i(TAG, "setListener hasAvmTopRightSoundEffects:" + CarCameraHelper.getInstance().hasAvmTopRightSoundEffects(), false);
            this.mViewAvmRectRt.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$u2ZabR2ESWhhx5ptMTkOwLX_zMo
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    MainActivity.this.lambda$setListener$10$MainActivity(view2);
                }
            });
        }
        this.mControlPanel.setOnCameraModeChangeListener(this.mOnCameraModeChangeListener);
    }

    public /* synthetic */ void lambda$setListener$10$MainActivity(View view) {
        CameraLog.d(TAG, "click transparent mode change.", false);
        if (ClickHelper.getInstance().isQuickClick(5)) {
            CameraLog.d(TAG, "click too fast,ignore", false);
        } else if (!CarCameraHelper.getInstance().isSupportAvmPipTouch() || CarCameraHelper.getInstance().getCarCamera().isTransparentChassis() || CarCameraHelper.getInstance().is3DMode() || !CarCameraHelper.getInstance().hasAVM()) {
        } else {
            CameraLog.d(TAG, "click Camera transparent mode change.", false);
            this.mAvmViewModel.changeToTransparent();
        }
    }

    @Override // com.xiaopeng.xmart.camera.BaseActivity, android.app.Activity
    public void recreate() {
        super.recreate();
        this.mActivityRecreating = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ClickHelper.getInstance().resetTooQuickClick();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        StartPerfUtils.onResumeBegin();
        super.onResume();
        CameraLog.d(TAG, "onResume", false);
        resumePreview();
        this.mControlPanel.showPictureThumbnail(null);
        if (this.mEnterAutoPilot) {
            finish();
        }
        StartPerfUtils.onResumeEnd();
    }

    private void pausePreview() {
        if (SpeechOperationHelper.getInstance().isOpenCamera()) {
            return;
        }
        this.mGeneralPreview.pausePreview();
    }

    private void resumePreview() {
        this.mGeneralPreview.resumePreview();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        CameraLog.d(TAG, "onStart", false);
        AvmViewModel avmViewModel = this.mAvmViewModel;
        if (avmViewModel != null && !avmViewModel.isPreviewOpen()) {
            this.mAvmViewModel.setShowPreViewCover(true);
        }
        showPreviewCover();
        registerReceiver(this.mActivityChangeReceiver, new IntentFilter("com.xiaopeng.intent.action.ACTIVITY_CHANGED"));
        this.mBroadcastRegistered = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        CameraLog.d(TAG, "onPause", false);
        if (this.mEnterAutoPilot) {
            finish();
        }
        if (ActivityUtils.isEnterAutoPilot(this)) {
            exitForEnterAutopilot();
        }
        pausePreview();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xmart.camera.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        CameraLog.d(TAG, "onStop finish:" + isFinishing(), false);
        unregisterActivityChangeReceiver();
        if (isFinishing()) {
            stopPreviewAndReleaseCamera();
        }
        this.mGeneralPreview.onRecordStop(null);
        this.mControlPanel.onRecordStop(null);
        this.mGeneralPreview.release();
        CameraLog.d(TAG, "onStop..." + this.mGeneralPreview, true);
        if (isFinishing() && this.mNeedClean) {
            ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$NXne95pnnreTpkzn8tKD5ktrHJc
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.lambda$onStop$11();
                }
            }, 50L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onStop$11() {
        CameraLog.d(TAG, "Exit Camera App for entering AutoPilot(onStop)", false);
        Process.killProcess(Process.myPid());
    }

    private void unregisterActivityChangeReceiver() {
        if (this.mBroadcastRegistered) {
            unregisterReceiver(this.mActivityChangeReceiver);
            this.mBroadcastRegistered = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitForEnterAutopilot() {
        CameraLog.d(TAG, "exitForEnterAutoPilot", false);
        this.mEnterAutoPilot = true;
        if (this.mAvmViewModel.isRecording()) {
            this.mAvmViewModel.stopRecord();
        }
        this.mAvmViewModel.stopPreview();
        this.mAvmViewModel.closeCamera();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mAvmViewModel.getShowPreviewCover().removeObserver(this.mShowPreviewViewObserver);
        this.mAvmViewModel.getCameraPreview().removeObserver(this.mCameraPreviewObserver);
        this.mAvmViewModel.getAvmSwitchFail().removeObserver(this.mAvmSwitchFailObserver);
        this.mAvmViewModel.getCamera360Change().removeObserver(this.mCamera360ChangeObserver);
        this.mAvmViewModel.getTakePicState().removeObserver(this.mTakePicObserver);
        this.mAvmViewModel.getStartRecordState().removeObserver(this.mRecordObserver);
        this.mAvmViewModel.getRecordTimeIsEvenNumber().removeObserver(this.mRecordTimeObserver);
        this.mAvmViewModel.getCameraRelease().removeObserver(this.mCameraReleaseObserver);
        this.mAvmViewModel.getIgOff().removeObserver(this.mIgOffObserver);
        AvmViewModel avmViewModel = this.mAvmViewModel;
        if (avmViewModel != null) {
            avmViewModel.resetData();
        }
        CameraLog.d(TAG, "onDestroy", false);
        this.mGeneralPreview.release();
        this.mGeneralPreview = null;
    }

    @Override // com.xiaopeng.xmart.camera.BaseActivity
    public void gotoGallery() {
        AvmViewModel avmViewModel = this.mAvmViewModel;
        if (avmViewModel != null) {
            avmViewModel.gotoGalleryDetail(null);
        }
    }

    private void onIgOff() {
        AvmViewModel avmViewModel = this.mAvmViewModel;
        if (avmViewModel != null && avmViewModel.isRecording()) {
            CameraLog.d(TAG, "IGOFF, stop record: ", false);
            this.mIGOFF2KillSelf = true;
            this.mAvmViewModel.stopRecord();
            return;
        }
        unregisterActivityChangeReceiver();
        CameraLog.d(TAG, "IGOFF to kill self(No recording)!", false);
        Process.killProcess(Process.myPid());
    }

    private void onCameraRelease() {
        CameraLog.d(TAG, "onCameraRelease AutoPilot:" + this.mEnterAutoPilot, false);
        if (this.mEnterAutoPilot) {
            unregisterActivityChangeReceiver();
            this.mEnterAutoPilot = false;
            this.mNeedClean = true;
            ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$kXe5blPWDHgvzApj95JltrYEsvo
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.lambda$onCameraRelease$12();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onCameraRelease$12() {
        CameraLog.d(TAG, "Exit Camera App for entering AutoPilot(release camera)", false);
        Process.killProcess(Process.myPid());
    }

    private void onTakingPicture() {
        GeneralCameraPreview generalCameraPreview = this.mGeneralPreview;
        if (generalCameraPreview != null) {
            generalCameraPreview.onTakingPicture();
        }
    }

    private void onPictureTaken(String path) {
        if (this.mAvmViewModel.isRecording()) {
            return;
        }
        this.mGeneralPreview.onPictureTaken(path);
        this.mControlPanel.onPictureTaken(path);
    }

    /* renamed from: hidePreviewCover */
    public void lambda$new$1$MainActivity() {
        CameraLog.i(TAG, "---hidePreviewCover mIsSurfaceAvaliable:" + this.mIsSurfaceAvaliable, false);
        GeneralCameraPreview generalCameraPreview = this.mGeneralPreview;
        if (generalCameraPreview == null || !this.mIsSurfaceAvaliable) {
            return;
        }
        generalCameraPreview.hidePreviewCover();
    }

    private void showPreviewCover() {
        CameraLog.i(TAG, "showPreviewCover", false);
        GeneralCameraPreview generalCameraPreview = this.mGeneralPreview;
        if (generalCameraPreview != null) {
            generalCameraPreview.showPreviewCover(null);
        }
    }

    private void onRecordStart() {
        ToastUtils.showToast((int) R.string.camera_max_record_time);
        this.mGeneralPreview.onRecordStart();
        this.mControlPanel.onRecordStart();
        this.mStartRecordTime = SystemClock.elapsedRealtime();
    }

    private void onRecordStop(String videoPath) {
        CameraLog.d(TAG, "onRecordStop, videoPath: " + videoPath, false);
        uploadRecordBI(false, (int) ((SystemClock.elapsedRealtime() - this.mStartRecordTime) / 1000), 0);
        this.mGeneralPreview.onRecordStop(videoPath);
        this.mControlPanel.onRecordStop(videoPath);
        this.mStartRecordTime = 0L;
        if (this.mIGOFF2KillSelf) {
            this.mIGOFF2KillSelf = false;
            unregisterActivityChangeReceiver();
            ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$uUJotVxfRUgob0zoL6QS_Mfs1Vw
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.lambda$onRecordStop$13();
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onRecordStop$13() {
        CameraLog.d(TAG, "IGOFF to kill self(With recording)!", false);
        Process.killProcess(Process.myPid());
    }

    private void onRecordError() {
        this.mControlPanel.onRecordError();
        this.mGeneralPreview.onRecordError();
        this.mStartRecordTime = 0L;
    }

    protected void startPreview() {
        ThreadPoolHelper.getInstance().executeBySequence(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$7PKMYd_SmoVcsXEyCrDZm6kJRWQ
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$startPreview$14$MainActivity();
            }
        });
    }

    public /* synthetic */ void lambda$startPreview$14$MainActivity() {
        CameraLog.i(TAG, "startPreview isPreview:" + this.mAvmViewModel.isPreviewOpen() + " cameraId:" + this.mCameraId, false);
        if (this.mAvmViewModel.getCamera() != null || this.mAvmViewModel.isPreviewOpen()) {
            return;
        }
        this.mAvmViewModel.openCamera(this.mCameraId);
        CameraLog.i(TAG, "Camera open success", false);
        this.mAvmViewModel.startPreview(this.mGeneralPreview.getPreview().getHolder());
        CameraLog.i(TAG, "Camera startPreview success", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPreviewAndReleaseCamera() {
        CameraLog.i(TAG, "stopPreviewAndReleaseCamera", false);
        try {
            this.mAvmViewModel.stopPreview();
            this.mAvmViewModel.closeCamera();
        } catch (Exception e) {
            e.printStackTrace();
            CameraLog.d(TAG, "stopPreviewAndReleaseCamera exception:" + e.getMessage(), false);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00a8, code lost:
        if (r3.equals(com.xiaopeng.xmart.camera.define.CameraDefine.Actions.ACTION_OPEN_360_CAMERA) == false) goto L7;
     */
    @Override // com.xiaopeng.xmart.camera.BaseActivity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void checkAction(long r7) {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "checkAction delayTime:"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r7)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "CameraMainActivity"
            r2 = 0
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r1, r0, r2)
            com.xiaopeng.xmart.camera.helper.SpeechOperationHelper r0 = com.xiaopeng.xmart.camera.helper.SpeechOperationHelper.getInstance()
            r0.setOpenCamera(r2)
            android.content.Intent r0 = r6.getIntent()
            if (r0 == 0) goto Le3
            java.lang.String r3 = r0.getAction()
            if (r3 != 0) goto L2e
            goto Le3
        L2e:
            java.lang.String r3 = r0.getAction()
            int r4 = com.xiaopeng.xmart.camera.define.CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS
            java.lang.String r5 = "extra_camera_type"
            int r0 = r0.getIntExtra(r5, r4)
            r6.mTargetCameraType = r0
            com.xiaopeng.xmart.camera.helper.CarCameraHelper r0 = com.xiaopeng.xmart.camera.helper.CarCameraHelper.getInstance()
            com.xiaopeng.xmart.camera.bean.CarCamera r0 = r0.getCarCamera()
            int r0 = r0.get360CameraType()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "checkAction: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r3)
            java.lang.String r5 = " targetCameraType="
            java.lang.StringBuilder r4 = r4.append(r5)
            int r5 = r6.mTargetCameraType
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = " currentCameraType = "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r1, r4, r2)
            r3.hashCode()
            r1 = -1
            int r4 = r3.hashCode()
            switch(r4) {
                case 43912679: goto La2;
                case 286779030: goto L97;
                case 1596123026: goto L8c;
                case 1968182304: goto L81;
                default: goto L7f;
            }
        L7f:
            r2 = r1
            goto Lab
        L81:
            java.lang.String r2 = "com.xiaopeng.action.ACTION_CAPTURE_PICTURE"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L8a
            goto L7f
        L8a:
            r2 = 3
            goto Lab
        L8c:
            java.lang.String r2 = "com.xiaopeng.action.ACTION_RECORD_END"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L95
            goto L7f
        L95:
            r2 = 2
            goto Lab
        L97:
            java.lang.String r2 = "com.xiaopeng.action.ACTION_RECORD"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto La0
            goto L7f
        La0:
            r2 = 1
            goto Lab
        La2:
            java.lang.String r4 = "com.xiaopeng.action.ACTION_OPEN_360_CAMERA"
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto Lab
            goto L7f
        Lab:
            switch(r2) {
                case 0: goto Ld6;
                case 1: goto Lc9;
                case 2: goto Lbc;
                case 3: goto Laf;
                default: goto Lae;
            }
        Lae:
            goto Le2
        Laf:
            com.xiaopeng.xmart.camera.utils.ThreadPoolHelper r1 = com.xiaopeng.xmart.camera.utils.ThreadPoolHelper.getInstance()
            com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$w4yqkFassFb-lfU5B7rv0Rp0iM4 r2 = new com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$w4yqkFassFb-lfU5B7rv0Rp0iM4
            r2.<init>()
            r1.postDelayedOnMainThread(r2, r7)
            goto Le2
        Lbc:
            com.xiaopeng.xmart.camera.utils.ThreadPoolHelper r0 = com.xiaopeng.xmart.camera.utils.ThreadPoolHelper.getInstance()
            com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$JQRbxV7_1Fzo8Zcq8BhEKl2Ny4c r1 = new com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$JQRbxV7_1Fzo8Zcq8BhEKl2Ny4c
            r1.<init>()
            r0.postDelayedOnMainThread(r1, r7)
            goto Le2
        Lc9:
            com.xiaopeng.xmart.camera.utils.ThreadPoolHelper r1 = com.xiaopeng.xmart.camera.utils.ThreadPoolHelper.getInstance()
            com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$1ZGTvGQ-UkL9nDufTFGBupPNtZI r2 = new com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$1ZGTvGQ-UkL9nDufTFGBupPNtZI
            r2.<init>()
            r1.postDelayedOnMainThread(r2, r7)
            goto Le2
        Ld6:
            com.xiaopeng.xmart.camera.utils.ThreadPoolHelper r1 = com.xiaopeng.xmart.camera.utils.ThreadPoolHelper.getInstance()
            com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$-M3sE12mWRSSZDFDWnUjKvBdJvw r2 = new com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$-M3sE12mWRSSZDFDWnUjKvBdJvw
            r2.<init>()
            r1.postDelayedOnMainThread(r2, r7)
        Le2:
            return
        Le3:
            java.lang.String r7 = "intent or action is null, return"
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xmart.camera.MainActivity.checkAction(long):void");
    }

    public /* synthetic */ void lambda$checkAction$15$MainActivity(int i) {
        if (this.mTargetCameraType == i) {
            return;
        }
        if (ClickHelper.getInstance().isQuickClick(7)) {
            CameraLog.e(TAG, "checkAction, too quick click...", false);
        } else if (this.mAvmViewModel != null) {
            ClickHelper.getInstance().resetTooQuickClick();
            CameraLog.d(TAG, "mAvmViewModel: switchCamera", false);
            this.mAvmViewModel.switchCamera(this.mTargetCameraType);
        }
    }

    public /* synthetic */ void lambda$checkAction$17$MainActivity(int i) {
        boolean z = false;
        CameraLog.d(TAG, "ACTION_TAKE_PICTURE", false);
        if (this.mAvmViewModel != null) {
            if (ClickHelper.getInstance().isQuickClick(7)) {
                CameraLog.e(TAG, "checkAction, too quick click...", false);
                return;
            }
            this.mControlPanel.setCameraControlMode(true);
            if (CarCameraHelper.getInstance().is360Camera(this.mTargetCameraType)) {
                int i2 = this.mTargetCameraType;
                if (i2 != i) {
                    this.mAvmViewModel.switchCamera(i2);
                    z = true;
                }
                ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$gtgMofJnkn7mXoHyyKhRUisVBXE
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.lambda$checkAction$16$MainActivity();
                    }
                }, z ? 1000L : 0L);
                return;
            }
            ClickHelper.getInstance().resetTooQuickClick();
            this.mAvmViewModel.takePicture();
            uploadCaptureBI(1);
            SpeechHelper.getInstance().speak(App.getInstance().getString(R.string.camera_speech_has_taken_pic));
        }
    }

    public /* synthetic */ void lambda$checkAction$16$MainActivity() {
        if (this.mAvmViewModel != null) {
            ClickHelper.getInstance().resetTooQuickClick();
            this.mAvmViewModel.takePicture();
            uploadCaptureBI(1);
            SpeechHelper.getInstance().speak(App.getInstance().getString(R.string.camera_speech_has_taken_pic));
        }
    }

    public /* synthetic */ void lambda$checkAction$19$MainActivity(int i) {
        final boolean z = false;
        CameraLog.d(TAG, "ACTION_RECORD", false);
        if (this.mAvmViewModel != null) {
            if (ClickHelper.getInstance().isQuickClick(7)) {
                CameraLog.e(TAG, "checkAction, too quick click...", false);
                return;
            }
            this.mControlPanel.setCameraControlMode(false);
            if (CarCameraHelper.getInstance().is360Camera(this.mTargetCameraType)) {
                int i2 = this.mTargetCameraType;
                if (i2 != i) {
                    this.mAvmViewModel.switchCamera(i2);
                    z = true;
                }
                ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$M01OnLIhnovxswqRZOufHI45dGs
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.lambda$checkAction$18$MainActivity(z);
                    }
                }, z ? 1000L : 0L);
                return;
            }
            ClickHelper.getInstance().resetTooQuickClick();
            this.mAvmViewModel.startRecord();
            uploadRecordBI(true, 0, 1);
        }
    }

    public /* synthetic */ void lambda$checkAction$18$MainActivity(boolean z) {
        if (this.mAvmViewModel != null) {
            if (z && ClickHelper.getInstance().isQuickClick(7)) {
                CameraLog.e(TAG, "checkAction,startRecord too quick click...", false);
                return;
            }
            ClickHelper.getInstance().resetTooQuickClick();
            this.mAvmViewModel.startRecord();
            uploadRecordBI(true, 0, 1);
            SpeechHelper.getInstance().speak(App.getInstance().getString(R.string.camera_speech_start_record));
        }
    }

    public /* synthetic */ void lambda$checkAction$20$MainActivity() {
        CameraLog.d(TAG, "ACTION_RECORD_END", false);
        if (this.mAvmViewModel != null) {
            if (ClickHelper.getInstance().isQuickClick(7)) {
                CameraLog.e(TAG, "checkAction, too quick click...", false);
                return;
            }
            ClickHelper.getInstance().resetTooQuickClick();
            this.mAvmViewModel.stopRecord();
        }
    }

    @Override // com.xiaopeng.vui.commons.IVuiSceneListener
    public void onVuiEvent(View view, VuiEvent vuiEvent) {
        XImageView xImageView;
        if (view == null) {
            return;
        }
        VuiElement hitVuiElement = vuiEvent.getHitVuiElement();
        CameraLog.i(TAG, "onVuiEvent vuiEvent:" + hitVuiElement, false);
        if (hitVuiElement == null) {
            return;
        }
        CameraLog.i(TAG, "parent:2131296713,record:2131296408,id:" + hitVuiElement.getId(), false);
        if (hitVuiElement.getId().contains(String.valueOf((int) R.id.camera_shutter_record_view))) {
            if (this.mControlPanel.isPhotoMode()) {
                vuiFeedback(R.string.vui_pic_mode_pic_tts, findViewById(R.id.camera_shutter_record_view));
                return;
            }
            findViewById(R.id.camera_shutter_record_view).performClick();
        }
        if (hitVuiElement.getId().contains(String.valueOf((int) R.id.camera_direction_switch))) {
            VuiEventHandler.getInstance().runMain(new AvmControllerVuiEventActor(this, vuiEvent));
        } else if (view.getId() == R.id.view_camera_mode_switch) {
            Object values = vuiEvent.getHitVuiElement().getValues();
            if (values != null && values.toString().contains(getString(R.string.vui_switch_record))) {
                xImageView = (XImageView) findViewById(R.id.img_camera_mode_record);
            } else {
                xImageView = (XImageView) findViewById(R.id.img_camera_mode_capture);
            }
            if (xImageView != null) {
                xImageView.performClick();
                VuiFloatingLayerManager.show(xImageView);
            }
            CameraLog.i(TAG, "view_camera_mode_switch object:" + values, false);
        } else {
            VuiFloatingLayerManager.show(view);
        }
    }

    @Override // com.xiaopeng.vui.commons.IVuiSceneListener
    public boolean onInterceptVuiEvent(final View view, VuiEvent vuiEvent) {
        if (view == null) {
            return false;
        }
        if (vuiEvent != null) {
            CameraLog.i(TAG, "onInterceptVuiEvent isPhotoMode:" + this.mControlPanel.isPhotoMode() + ",HitVuiElement:" + vuiEvent.getHitVuiElement(), false);
            CameraLog.i(TAG, "shutter:2131296409,getId:" + vuiEvent.getHitVuiElement().getId(), false);
        }
        if (view.getId() == R.id.camera_shutter_view && !this.mControlPanel.isPhotoMode()) {
            vuiFeedback(R.string.vui_record_mode_pic_tts, findViewById(R.id.camera_shutter_view));
            return true;
        }
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$MainActivity$rwzDmUNL2VD-lQmLjLhcsq7nhzM
            @Override // java.lang.Runnable
            public final void run() {
                VuiFloatingLayerManager.show(view);
            }
        });
        return false;
    }

    @Override // com.xiaopeng.xmart.camera.IVuiCameraView
    public void vuiAvmSwitchHandle(int cameraType) {
        if (this.mAvmControllerView == null) {
            return;
        }
        if (ClickHelper.getInstance().isQuickClick(5)) {
            CameraLog.d(TAG, "click too fast,ignore", false);
            vuiFeedback(R.string.vui_operation_fast_tts, (View) this.mAvmControllerView);
            return;
        }
        this.mAvmControllerView.vuiAvmSwitchHandle(cameraType);
    }
}
