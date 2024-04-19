package com.xiaopeng.xmart.camera.view.controlpanel;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xiaopeng.lib.utils.ContextUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.speech.vui.utils.VuiUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.MainActivity;
import com.xiaopeng.xmart.camera.R;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.FileUtils;
import com.xiaopeng.xmart.camera.utils.GlideUtil;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camera.view.controlpanel.CameraControlPanelView;
import com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView;
import com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
import com.xiaopeng.xmart.camera.vm.ViewModelManager;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xui.widget.XFrameLayout;
import com.xiaopeng.xui.widget.XImageButton;
import com.xiaopeng.xui.widget.XImageView;
/* loaded from: classes.dex */
public class CameraControlPanelView implements ICameraControlPanelView {
    private static final String TAG = "com.xiaopeng.xmart.camera.view.controlpanel.CameraControlPanelView";
    private AvmViewModel mAvmViewModel;
    private XImageButton mBtnExit;
    private CameraRecordShutterView mCameraRecordShutterView;
    private CameraShutterView mCameraShutterView;
    private ICameraSwitchView mCameraSwitchView;
    private Activity mContext;
    private XImageView mGalleryEntrance;
    private XFrameLayout mGalleryLayout;
    private String mInitThumbnailPath;
    private ICameraControlPanelView.OnCameraModeChangeListener mOnCameraModeChangeListener;
    private ViewGroup mRootView;
    private boolean mIsPhotoMode = true;
    private MultiTransformation multiTransformation = new MultiTransformation(new FitCenter(), new CircleCrop());
    private View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraControlPanelView$-smjRH4oVY9oFnPQxbjUJuwW65Y
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            CameraControlPanelView.this.lambda$new$0$CameraControlPanelView(view);
        }
    };
    private ICameraSwitchView.OnSwitchChangeListener mCameraModeSwitchListener = new ICameraSwitchView.OnSwitchChangeListener() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.CameraControlPanelView.1
        @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView.OnSwitchChangeListener
        public boolean onInterceptTabChange(View tabLayout, int index, boolean tabChange, boolean fromUser) {
            return false;
        }

        @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView.OnSwitchChangeListener
        public void onTabChangeStart(View tabLayout, int index, boolean tabChange, boolean fromUser) {
        }

        @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView.OnSwitchChangeListener
        public void onTabChangeEnd(View tabLayout, int index, boolean tabChange, boolean fromUser) {
            CameraControlPanelView.this.mIsPhotoMode = index == 0;
            CameraControlPanelView cameraControlPanelView = CameraControlPanelView.this;
            cameraControlPanelView.setTakePictureViewVisibility(cameraControlPanelView.mIsPhotoMode);
            CameraLog.i(CameraControlPanelView.TAG, "onTabChangeEnd mIsPhotoMode:" + CameraControlPanelView.this.mIsPhotoMode + ",index:" + index, false);
            if (CameraControlPanelView.this.mOnCameraModeChangeListener != null) {
                CameraControlPanelView.this.mOnCameraModeChangeListener.onCameraModeChange(CameraControlPanelView.this.mIsPhotoMode);
            }
        }
    };

    public /* synthetic */ void lambda$new$0$CameraControlPanelView(View view) {
        int id = view.getId();
        if (id != R.id.btn_exit) {
            if (id != R.id.gallery_layout) {
                return;
            }
            CameraLog.d(TAG, "Go to CarGallery! ", false);
            this.mAvmViewModel.gotoGalleryDetail(null);
            return;
        }
        CameraLog.d(TAG, "btn_exit! ", false);
        Activity activity = this.mContext;
        if (activity != null) {
            activity.finish();
        }
    }

    public boolean isPhotoMode() {
        return this.mIsPhotoMode;
    }

    public CameraControlPanelView(Activity act, ViewGroup rootView) {
        this.mContext = act;
        this.mRootView = rootView;
        init();
    }

    private void init() {
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
        new AsyncLayoutInflater(App.getInstance()).inflate(R.layout.layout_camera_button_control_panel, null, new AsyncLayoutInflater.OnInflateFinishedListener() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraControlPanelView$NQhMvJYe0aMoDyJakT1ZiukZZbA
            @Override // androidx.asynclayoutinflater.view.AsyncLayoutInflater.OnInflateFinishedListener
            public final void onInflateFinished(View view, int i, ViewGroup viewGroup) {
                CameraControlPanelView.this.lambda$init$1$CameraControlPanelView(view, i, viewGroup);
            }
        });
    }

    public /* synthetic */ void lambda$init$1$CameraControlPanelView(View view, int i, ViewGroup viewGroup) {
        this.mBtnExit = (XImageButton) view.findViewById(R.id.btn_exit);
        ICameraSwitchView iCameraSwitchView = (ICameraSwitchView) view.findViewById(R.id.view_camera_mode_switch);
        this.mCameraSwitchView = iCameraSwitchView;
        iCameraSwitchView.setOnCameraModeSwitchListener(this.mCameraModeSwitchListener);
        XImageButton xImageButton = this.mBtnExit;
        if (xImageButton != null) {
            xImageButton.setOnClickListener(this.mOnClickListener);
        }
        this.mCameraShutterView = (CameraShutterView) view.findViewById(R.id.camera_shutter_view);
        this.mCameraRecordShutterView = (CameraRecordShutterView) view.findViewById(R.id.camera_shutter_record_view);
        this.mGalleryEntrance = (XImageView) view.findViewById(R.id.gallery_entrance);
        XFrameLayout xFrameLayout = (XFrameLayout) view.findViewById(R.id.gallery_layout);
        this.mGalleryLayout = xFrameLayout;
        xFrameLayout.setOnClickListener(this.mOnClickListener);
        setModeView(this.mIsPhotoMode);
        showPictureThumbnail(this.mInitThumbnailPath);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) App.getInstance().getResources().getDimension(R.dimen.left_control_width), -1);
        if (CarCameraHelper.getInstance().isPortraitScreen()) {
            layoutParams = new RelativeLayout.LayoutParams(-1, (int) App.getInstance().getResources().getDimension(R.dimen.panel_control_height));
            layoutParams.addRule(3, R.id.camera_general_preview);
        }
        this.mRootView.addView(view, layoutParams);
        CameraLog.i(TAG, "init...", false);
        updatePicButtonScene();
        VuiUtils.addCanVoiceControlProp(this.mCameraRecordShutterView);
        VuiUtils.addHasFeedbackProp(this.mCameraRecordShutterView);
        VuiUtils.setStatefulButtonAttr(this.mCameraRecordShutterView, 0, App.getInstance().getResources().getStringArray(R.array.record_state_labels));
    }

    private void updatePicButtonScene() {
        CameraLog.d(TAG, "updatePicButtonScene mIsPhotoMode", false);
        VuiUtils.addCanVoiceControlProp(this.mCameraShutterView);
        VuiUtils.addHasFeedbackProp(this.mCameraShutterView);
    }

    private void updateRecordButtonScene(int level) {
        if (this.mAvmViewModel != null) {
            CameraLog.d(TAG, "updateRecordButtonScene level:" + level + ",record:" + this.mAvmViewModel.isRecording(), false);
        }
        VuiUtils.setStatefulButtonValue(this.mCameraRecordShutterView, level);
    }

    public void setOnCameraModeChangeListener(ICameraControlPanelView.OnCameraModeChangeListener onCameraModeChangeListener) {
        this.mOnCameraModeChangeListener = onCameraModeChangeListener;
    }

    public void setCameraControlMode(boolean isPhoto) {
        if (this.mIsPhotoMode != isPhoto) {
            this.mIsPhotoMode = isPhoto;
            setModeView(isPhoto);
            setTakePictureViewVisibility(isPhoto);
            ICameraControlPanelView.OnCameraModeChangeListener onCameraModeChangeListener = this.mOnCameraModeChangeListener;
            if (onCameraModeChangeListener != null) {
                onCameraModeChangeListener.onCameraModeChange(isPhoto);
            }
        }
    }

    private void setModeView(boolean isPhoto) {
        this.mCameraSwitchView.selectTab(isPhoto);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTakePictureViewVisibility(boolean isPhoto) {
        int i = 0;
        CameraLog.d(TAG, "setTakePictureViewVisibility isPhoto:" + isPhoto, false);
        this.mCameraShutterView.setVisibility(isPhoto ? 0 : 8);
        this.mCameraRecordShutterView.setVisibility(isPhoto ? 8 : 0);
        if (!isPhoto) {
            i = this.mAvmViewModel.isRecording() ? 2 : 1;
        }
        updateRecordButtonScene(i);
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView
    public void showPictureThumbnail(final String path) {
        if (this.mGalleryEntrance == null) {
            this.mInitThumbnailPath = path;
            CameraLog.d(TAG, "mGalleryEntrance is null，path:" + path, false);
            return;
        }
        AvmViewModel avmViewModel = this.mAvmViewModel;
        if (avmViewModel != null && avmViewModel.isRecording()) {
            CameraLog.d(TAG, "Recording not need thumb show!", false);
            return;
        }
        String str = TAG;
        CameraLog.d(str, "showPictureThumbnail path:" + path, false);
        if (path == null) {
            ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraControlPanelView$WFc4Tn2VxL3kap_MHkDyP5KF2a0
                @Override // java.lang.Runnable
                public final void run() {
                    CameraControlPanelView.this.lambda$showPictureThumbnail$3$CameraControlPanelView();
                }
            });
            return;
        }
        CameraLog.d(str, "showPictureThumbnail: " + path, false);
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraControlPanelView$HvhsYsgVi93oABuSyEcTRz4RmC8
            @Override // java.lang.Runnable
            public final void run() {
                CameraControlPanelView.this.lambda$showPictureThumbnail$4$CameraControlPanelView(path);
            }
        });
    }

    public /* synthetic */ void lambda$showPictureThumbnail$3$CameraControlPanelView() {
        final String latestPicturePath = this.mAvmViewModel.getLatestPicturePath();
        CameraLog.d(TAG, "LatestPicturePath: " + latestPicturePath, false);
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraControlPanelView$WOhcTn_pBgrTfQTYO-BiSMc1ThA
            @Override // java.lang.Runnable
            public final void run() {
                CameraControlPanelView.this.lambda$showPictureThumbnail$2$CameraControlPanelView(latestPicturePath);
            }
        });
    }

    public /* synthetic */ void lambda$showPictureThumbnail$2$CameraControlPanelView(String str) {
        if (str != null) {
            GlideUtil.loadWithListener(str, this.mGalleryEntrance, R.drawable.ic_mid_picture, R.drawable.ic_mid_picture, this.multiTransformation, new AnonymousClass2(str));
            setGalleryEntranceVisibility(0);
            return;
        }
        this.mGalleryEntrance.setImageResource(R.drawable.ic_mid_picture);
        setGalleryEntranceVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.camera.view.controlpanel.CameraControlPanelView$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements RequestListener<Drawable> {
        final /* synthetic */ String val$desPath;

        AnonymousClass2(final String val$desPath) {
            this.val$desPath = val$desPath;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            FileUtils.deleteFile(this.val$desPath);
            final String latestPicturePath = CameraControlPanelView.this.mAvmViewModel.getLatestPicturePath();
            CameraLog.d(CameraControlPanelView.TAG, "Load  Thumbnail Failure ! get second last file:" + latestPicturePath);
            if (latestPicturePath == null || latestPicturePath.equals(this.val$desPath)) {
                if (CameraControlPanelView.this.mGalleryEntrance != null) {
                    CameraControlPanelView.this.mGalleryEntrance.setImageResource(R.drawable.ic_mid_picture);
                    CameraControlPanelView.this.setGalleryEntranceVisibility(0);
                }
                return false;
            }
            ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraControlPanelView$2$ezn7qUCH13T_pdW70GFpJ2oDi2Y
                @Override // java.lang.Runnable
                public final void run() {
                    CameraControlPanelView.AnonymousClass2.this.lambda$onLoadFailed$0$CameraControlPanelView$2(latestPicturePath);
                }
            });
            return false;
        }

        public /* synthetic */ void lambda$onLoadFailed$0$CameraControlPanelView$2(String str) {
            GlideUtil.loadCircleThumbnail(str, CameraControlPanelView.this.mGalleryEntrance, R.drawable.ic_mid_picture, R.drawable.ic_mid_picture, CameraControlPanelView.this.multiTransformation);
            CameraControlPanelView.this.setGalleryEntranceVisibility(0);
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            CameraLog.d(CameraControlPanelView.TAG, "onResourceReady:" + resource);
            if (CameraControlPanelView.this.mGalleryEntrance != null) {
                CameraControlPanelView.this.mGalleryEntrance.setImageResource(0);
                CameraControlPanelView.this.mGalleryEntrance.setImageDrawable(resource);
            }
            return false;
        }
    }

    public /* synthetic */ void lambda$showPictureThumbnail$4$CameraControlPanelView(String str) {
        GlideUtil.loadCircleThumbnail(str, this.mGalleryEntrance, R.drawable.ic_mid_picture, R.drawable.ic_mid_picture, this.multiTransformation);
        setGalleryEntranceVisibility(0);
    }

    public void setGalleryEntranceVisibility(int visibility) {
        CameraLog.d(TAG, "setGalleryEntranceVisibility:" + visibility);
        XFrameLayout xFrameLayout = this.mGalleryLayout;
        if (xFrameLayout != null) {
            xFrameLayout.setVisibility(visibility);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView
    public void onPictureTaken(String picturePath) {
        CameraLog.d(TAG, "onPictureTaken:" + picturePath);
        showPictureThumbnail(picturePath);
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView
    public void onRecordStart() {
        CameraLog.i(TAG, "onRecordStart", false);
        setGalleryEntranceVisibility(8);
        CameraRecordShutterView cameraRecordShutterView = this.mCameraRecordShutterView;
        if (cameraRecordShutterView != null) {
            cameraRecordShutterView.onRecordStart();
            updateRecordButtonScene(2);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView
    public void onRecordStop(String videoPath) {
        if (videoPath != null && ContextUtils.isTopActivity(App.getInstance(), MainActivity.class)) {
            showPictureThumbnail(videoPath);
        }
        CameraRecordShutterView cameraRecordShutterView = this.mCameraRecordShutterView;
        if (cameraRecordShutterView != null) {
            cameraRecordShutterView.onRecordStop();
            updateRecordButtonScene(1);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraControlPanelView
    public void onRecordError() {
        setGalleryEntranceVisibility(0);
        CameraRecordShutterView cameraRecordShutterView = this.mCameraRecordShutterView;
        if (cameraRecordShutterView != null) {
            cameraRecordShutterView.onRecordStop();
            updateRecordButtonScene(1);
        }
    }
}
