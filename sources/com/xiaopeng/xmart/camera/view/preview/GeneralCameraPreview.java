package com.xiaopeng.xmart.camera.view.preview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewStub;
import androidx.lifecycle.Lifecycle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xiaopeng.lib.utils.ContextUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.MainActivity;
import com.xiaopeng.xmart.camera.R;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camera.utils.VuiManager;
import com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
import com.xiaopeng.xmart.camera.vm.ViewModelManager;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xui.widget.XFrameLayout;
import com.xiaopeng.xui.widget.XImageView;
import com.xiaopeng.xui.widget.XRelativeLayout;
import com.xiaopeng.xui.widget.XTextView;
/* loaded from: classes.dex */
public class GeneralCameraPreview extends XFrameLayout implements ICameraPreview {
    private static final String TAG = "GeneralCameraPreview";
    private AvmViewModel mAvmViewModel;
    private SurfaceHolder.Callback mCallback;
    private CameraSurfaceView mCameraPreview;
    private ObjectAnimator mCaptureAnimator;
    private Runnable mCaptureThumbRunnable;
    private View mCoverView;
    private XImageView mImgMidMute;
    private int mLoadVideoThumbCount;
    private View mPreviewCover;
    private PreviewStatusListener mPreviewStatusListener;
    private XImageView mRecordIcon;
    private XRelativeLayout mRecordTimeArea;
    private XImageView mRedPointImg;
    private XImageView mThumbImg;
    private XFrameLayout mThumbLayout;
    private XTextView mTimeText;
    private XImageView mTransparentChangeView;
    private XTextView mTxtLoadingInfo;
    private String mVideoThumbPath;

    /* loaded from: classes.dex */
    public interface PreviewStatusListener {
        void onPreviewChanged();

        void onPreviewCreated();

        void onPreviewDestroyed();
    }

    static /* synthetic */ int access$508(GeneralCameraPreview generalCameraPreview) {
        int i = generalCameraPreview.mLoadVideoThumbCount;
        generalCameraPreview.mLoadVideoThumbCount = i + 1;
        return i;
    }

    public /* synthetic */ void lambda$new$0$GeneralCameraPreview() {
        XImageView xImageView = this.mThumbImg;
        if (xImageView != null) {
            xImageView.setImageDrawable(null);
        }
        setThumbLayout(8);
    }

    public GeneralCameraPreview(Context context) {
        this(context, null);
    }

    public GeneralCameraPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralCameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCallback = new SurfaceHolder.Callback() { // from class: com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview.1
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) {
                CameraLog.d(GeneralCameraPreview.TAG, "surfaceCreated", false);
                if (GeneralCameraPreview.this.mPreviewStatusListener != null) {
                    GeneralCameraPreview.this.mPreviewStatusListener.onPreviewCreated();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                CameraLog.d(GeneralCameraPreview.TAG, "surfaceChanged", false);
                if (GeneralCameraPreview.this.mPreviewStatusListener != null) {
                    GeneralCameraPreview.this.mPreviewStatusListener.onPreviewChanged();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                CameraLog.d(GeneralCameraPreview.TAG, "surfaceDestroyed", false);
                if (GeneralCameraPreview.this.mPreviewStatusListener != null) {
                    GeneralCameraPreview.this.mPreviewStatusListener.onPreviewDestroyed();
                }
            }
        };
        this.mCaptureThumbRunnable = new Runnable() { // from class: com.xiaopeng.xmart.camera.view.preview.-$$Lambda$GeneralCameraPreview$Hrx9bgASDW2YoyZhS2GIP2-WQjk
            @Override // java.lang.Runnable
            public final void run() {
                GeneralCameraPreview.this.lambda$new$0$GeneralCameraPreview();
            }
        };
        init();
    }

    private void init() {
        CameraLog.i(TAG, "init", false);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_general_camera_preview, this);
        this.mPreviewCover = inflate.findViewById(R.id.camera_preview_cover);
        this.mTxtLoadingInfo = (XTextView) inflate.findViewById(R.id.camera_loading_info);
        this.mLoadVideoThumbCount = 0;
        this.mVideoThumbPath = "";
        CameraSurfaceView cameraSurfaceView = (CameraSurfaceView) inflate.findViewById(R.id.camera_preview);
        this.mCameraPreview = cameraSurfaceView;
        cameraSurfaceView.getHolder().addCallback(this.mCallback);
        delayInflate();
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
    }

    private void delayInflate() {
        postDelayed(new Runnable() { // from class: com.xiaopeng.xmart.camera.view.preview.-$$Lambda$GeneralCameraPreview$FUuTtYGtsZtSQeGaGuo9tlvx-CA
            @Override // java.lang.Runnable
            public final void run() {
                GeneralCameraPreview.this.lambda$delayInflate$2$GeneralCameraPreview();
            }
        }, 1000L);
    }

    public /* synthetic */ void lambda$delayInflate$2$GeneralCameraPreview() {
        CameraLog.i(TAG, "delayInflate", false);
        if (CarCameraHelper.getInstance().isPortraitScreen()) {
            ((ViewStub) findViewById(R.id.transparent_change_layout)).inflate();
            XImageView xImageView = (XImageView) findViewById(R.id.transparent_change);
            this.mTransparentChangeView = xImageView;
            xImageView.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xmart.camera.view.preview.-$$Lambda$GeneralCameraPreview$bjkh9oXYO1Bb_630nVsoyjD5M8c
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GeneralCameraPreview.this.lambda$delayInflate$1$GeneralCameraPreview(view);
                }
            });
            VuiManager.getInstance().vuiInterceptBack(this.mTransparentChangeView);
        }
        ((ViewStub) findViewById(R.id.img_mid_mute_layout)).inflate();
        XImageView xImageView2 = (XImageView) findViewById(R.id.img_mid_mute);
        this.mImgMidMute = xImageView2;
        xImageView2.setImageResource(R.drawable.ic_mid_mute);
        ((ViewStub) findViewById(R.id.view_camera_preview_bg_layout)).inflate();
        this.mCoverView = findViewById(R.id.view_camera_preview_bg);
        ((ViewStub) findViewById(R.id.record_time_layout)).inflate();
        this.mRecordTimeArea = (XRelativeLayout) findViewById(R.id.record_time_container);
        this.mRedPointImg = (XImageView) findViewById(R.id.record_red_point_img);
        this.mTimeText = (XTextView) findViewById(R.id.record_time_text);
        ((ViewStub) findViewById(R.id.capture_thumb_layout)).inflate();
        this.mThumbLayout = (XFrameLayout) findViewById(R.id.camera_capture_thumb_layout);
        this.mThumbImg = (XImageView) findViewById(R.id.camera_capture_thumb_img);
        this.mRecordIcon = (XImageView) findViewById(R.id.camera_record_mask_img);
        if (CarCameraHelper.getInstance().hasAVM()) {
            changeTransparentVisible();
        }
    }

    public /* synthetic */ void lambda$delayInflate$1$GeneralCameraPreview(View view) {
        CameraLog.i(TAG, "onClick mTransparentChangeView", false);
        this.mAvmViewModel.changeToNormal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setThumbLayout(int visibility) {
        CameraLog.i(TAG, "setThumbLayout visibility:" + visibility, false);
        XFrameLayout xFrameLayout = this.mThumbLayout;
        if (xFrameLayout == null) {
            return;
        }
        xFrameLayout.setVisibility(visibility);
    }

    public void changeTransparentVisible() {
        boolean isTransparentChassis = CarCameraHelper.getInstance().getCarCamera().isTransparentChassis();
        CameraLog.d(TAG, "changeTransparentVisible, visible:" + isTransparentChassis, false);
        int i = isTransparentChassis ? 0 : 8;
        XImageView xImageView = this.mTransparentChangeView;
        if (xImageView != null) {
            xImageView.setVisibility(i);
        }
    }

    public CameraSurfaceView getPreview() {
        return this.mCameraPreview;
    }

    public void setPreviewStatusListener(PreviewStatusListener listener) {
        this.mPreviewStatusListener = listener;
    }

    public void release() {
        removeThumbView();
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onTakingPicture() {
        CameraLog.i(TAG, "onTakingPicture", false);
        removeThumbView();
        startCaptureAnimation(300L);
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onPictureTaken(String picturePath) {
        showCaptureThumbView(true, picturePath);
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onRecordStart() {
        XTextView xTextView = this.mTimeText;
        if (xTextView != null) {
            xTextView.setText(R.string.record_time_init);
        }
        XRelativeLayout xRelativeLayout = this.mRecordTimeArea;
        if (xRelativeLayout != null) {
            xRelativeLayout.setVisibility(0);
        }
        removeThumbView();
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onRecordStop(String videoPath) {
        XRelativeLayout xRelativeLayout = this.mRecordTimeArea;
        if (xRelativeLayout != null) {
            xRelativeLayout.setVisibility(8);
        }
        showCaptureThumbView(false, videoPath);
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onRecordError() {
        CameraLog.i(TAG, "onRecordError");
        XRelativeLayout xRelativeLayout = this.mRecordTimeArea;
        if (xRelativeLayout != null) {
            xRelativeLayout.setVisibility(8);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onRecordTimeTick(String timeText, boolean isEvenNumber) {
        XTextView xTextView = this.mTimeText;
        if (xTextView == null || this.mRedPointImg == null) {
            return;
        }
        xTextView.setText(timeText);
        if (!isEvenNumber) {
            this.mRedPointImg.setVisibility(4);
        } else {
            this.mRedPointImg.setVisibility(0);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void hidePreviewCover() {
        CameraLog.d(TAG, "hidePreviewCover", true);
        this.mPreviewCover.setVisibility(8);
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void showPreviewCover(String msg) {
        CameraLog.d(TAG, "showPreviewCover", false);
        if (this.mPreviewCover.getVisibility() != 0) {
            XTextView xTextView = this.mTxtLoadingInfo;
            if (TextUtils.isEmpty(msg)) {
                msg = App.getInstance().getString(R.string.camera_open_loading);
            }
            xTextView.setText(msg);
            this.mPreviewCover.setVisibility(0);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.preview.ICameraPreview
    public void onCameraError() {
        CameraLog.i(TAG, "onCameraError", false);
        showPreviewCover(App.getInstance().getString(R.string.camera_open_error));
    }

    private void startCaptureAnimation(long duration) {
        CameraLog.i(TAG, "startCaptureAnimation", false);
        View view = this.mCoverView;
        if (view == null) {
            CameraLog.i(TAG, "mCoverView is null", false);
            return;
        }
        if (this.mCaptureAnimator == null) {
            ObjectAnimator duration2 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration(duration);
            this.mCaptureAnimator = duration2;
            duration2.setRepeatCount(1);
            this.mCaptureAnimator.setRepeatMode(2);
            this.mCaptureAnimator.addListener(new Animator.AnimatorListener() { // from class: com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    CameraLog.i(GeneralCameraPreview.TAG, "startCaptureAnimation onAnimationStart", false);
                    GeneralCameraPreview.this.mCoverView.setVisibility(0);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    CameraLog.i(GeneralCameraPreview.TAG, "startCaptureAnimation onAnimationEnd", false);
                    GeneralCameraPreview.this.mCoverView.setVisibility(8);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                    CameraLog.i(GeneralCameraPreview.TAG, "startCaptureAnimation onAnimationCancel", false);
                    GeneralCameraPreview.this.mCoverView.setVisibility(8);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animation) {
                    CameraLog.i(GeneralCameraPreview.TAG, "startCaptureAnimation onAnimationRepeat", false);
                }
            });
        }
        this.mCaptureAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCaptureThumbView(final boolean isPhoto, final String path) {
        if (this.mThumbLayout == null) {
            CameraLog.d(TAG, "mThumbLayout null", false);
            return;
        }
        CameraLog.d(TAG, "showCaptureThumbView, path: " + path + " isPhoto:" + isPhoto, false);
        if (TextUtils.isEmpty(path)) {
            this.mThumbImg.removeCallbacks(this.mCaptureThumbRunnable);
        } else if (isPhoto || ContextUtils.isTopActivity(App.getInstance(), MainActivity.class)) {
            if (!isActivityResume()) {
                CameraLog.d(TAG, "The MainActiviy is not resume", false);
                return;
            }
            if (!isPhoto) {
                if (TextUtils.equals(this.mVideoThumbPath, path)) {
                    this.mLoadVideoThumbCount = 0;
                } else {
                    this.mVideoThumbPath = path;
                }
            }
            MultiTransformation multiTransformation = new MultiTransformation(new CenterCrop(), new RoundedCorners(10));
            if (isPhoto) {
                multiTransformation = new MultiTransformation(new CenterCrop(), new RoundedCorners(1));
            }
            Glide.with(App.getInstance()).load(path).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(343, 186)).transform(multiTransformation).listener(new AnonymousClass3(path, isPhoto)).into(this.mThumbImg);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xmart.camera.view.preview.GeneralCameraPreview$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements RequestListener<Drawable> {
        final /* synthetic */ boolean val$isPhoto;
        final /* synthetic */ String val$path;

        AnonymousClass3(final String val$path, final boolean val$isPhoto) {
            this.val$path = val$path;
            this.val$isPhoto = val$isPhoto;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            CameraLog.d(GeneralCameraPreview.TAG, "onResourceReady, path:" + this.val$path, false);
            if (!GeneralCameraPreview.this.mAvmViewModel.isCurrentCameraThumbnail(this.val$path)) {
                GeneralCameraPreview.this.setThumbLayout(8);
                return false;
            }
            GeneralCameraPreview.this.setThumbLayout(0);
            if (this.val$isPhoto) {
                GeneralCameraPreview.this.mRecordIcon.setVisibility(8);
            } else {
                GeneralCameraPreview.this.mLoadVideoThumbCount = 0;
                GeneralCameraPreview.this.mRecordIcon.setVisibility(0);
            }
            XFrameLayout xFrameLayout = GeneralCameraPreview.this.mThumbLayout;
            final String str = this.val$path;
            xFrameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xmart.camera.view.preview.-$$Lambda$GeneralCameraPreview$3$YmGbCZ7qyPVQtWfGUI4v1v1oVJQ
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GeneralCameraPreview.AnonymousClass3.this.lambda$onResourceReady$0$GeneralCameraPreview$3(str, view);
                }
            });
            GeneralCameraPreview.this.mThumbImg.postDelayed(GeneralCameraPreview.this.mCaptureThumbRunnable, 5000L);
            return false;
        }

        public /* synthetic */ void lambda$onResourceReady$0$GeneralCameraPreview$3(String str, View view) {
            CameraLog.d(GeneralCameraPreview.TAG, "onPreviewThumbClick, Go to CarGalleryDetaill, path: " + str, false);
            if (GeneralCameraPreview.this.mAvmViewModel != null) {
                GeneralCameraPreview.this.mAvmViewModel.gotoGalleryDetail(str);
            }
            GeneralCameraPreview.this.removeThumbView();
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            CameraLog.d(GeneralCameraPreview.TAG, "onLoadFailed, path: " + this.val$path, false);
            if (this.val$isPhoto) {
                GeneralCameraPreview.this.removeThumbView();
            } else if (GeneralCameraPreview.access$508(GeneralCameraPreview.this) < 3) {
                ThreadPoolHelper threadPoolHelper = ThreadPoolHelper.getInstance();
                final boolean z = this.val$isPhoto;
                final String str = this.val$path;
                threadPoolHelper.postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.view.preview.-$$Lambda$GeneralCameraPreview$3$ZGEySAq26yTXbzIL9xycAxWgc2A
                    @Override // java.lang.Runnable
                    public final void run() {
                        GeneralCameraPreview.AnonymousClass3.this.lambda$onLoadFailed$1$GeneralCameraPreview$3(z, str);
                    }
                }, 1000L);
            } else {
                CameraLog.d(GeneralCameraPreview.TAG, "The loading_video_thumb count exceeds max limits", false);
                GeneralCameraPreview.this.mLoadVideoThumbCount = 0;
                GeneralCameraPreview.this.removeThumbView();
            }
            return false;
        }

        public /* synthetic */ void lambda$onLoadFailed$1$GeneralCameraPreview$3(boolean z, String str) {
            GeneralCameraPreview.this.showCaptureThumbView(z, str);
        }
    }

    public void onShotModeChanged(boolean isPhoto) {
        CameraLog.d(TAG, "onShotModeChanged, isPhoto is " + isPhoto);
        XImageView xImageView = this.mImgMidMute;
        if (xImageView != null) {
            xImageView.setVisibility(isPhoto ? 8 : 0);
        }
    }

    public void removeThumbView() {
        XFrameLayout xFrameLayout = this.mThumbLayout;
        if (xFrameLayout == null || xFrameLayout.getVisibility() != 0) {
            return;
        }
        this.mThumbImg.removeCallbacks(this.mCaptureThumbRunnable);
        setThumbLayout(8);
        this.mThumbImg.setImageDrawable(null);
        this.mRecordIcon.setVisibility(8);
    }

    private boolean isActivityResume() {
        Activity activityFromView = getActivityFromView();
        return activityFromView != null && ((MainActivity) activityFromView).getLifecycle().getCurrentState() == Lifecycle.State.RESUMED;
    }

    public Activity getActivityFromView() {
        Context context = getContext();
        if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            return (Activity) ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public void pausePreview() {
        if (this.mCameraPreview.getParent() == this) {
            removeView(this.mCameraPreview);
        }
    }

    public void resumePreview() {
        if (this.mCameraPreview.getParent() == null) {
            addView(this.mCameraPreview, 0);
        }
    }
}
