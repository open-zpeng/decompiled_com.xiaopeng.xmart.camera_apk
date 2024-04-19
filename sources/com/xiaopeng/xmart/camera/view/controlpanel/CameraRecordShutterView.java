package com.xiaopeng.xmart.camera.view.controlpanel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.xmart.camera.R;
import com.xiaopeng.xmart.camera.helper.BIHelper;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.ClickHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
import com.xiaopeng.xmart.camera.vm.ViewModelManager;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xui.widget.XFrameLayout;
import com.xiaopeng.xui.widget.XImageView;
/* loaded from: classes.dex */
public class CameraRecordShutterView extends XFrameLayout implements View.OnClickListener {
    private static final int DURATION = 400;
    private static final String TAG = "CameraRecordShutterView";
    private boolean handleByOnClick;
    private AvmViewModel mAvmViewModel;
    private XImageView mInnerCircleView;
    private XImageView mOuterCircleView;
    private AnimatorListenerAdapter mShutterRecordAnimatorListener;
    private AnimatorSet mStartRecordingSet;

    public CameraRecordShutterView(Context context) {
        this(context, null);
    }

    public CameraRecordShutterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mShutterRecordAnimatorListener = new AnimatorListenerAdapter() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.CameraRecordShutterView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                CameraRecordShutterView.this.resetInnerImageViewAnim();
                CameraRecordShutterView.this.mInnerCircleView.setImageDrawable(CameraRecordShutterView.this.getContext().getDrawable(R.drawable.camera_inner_circle_recording));
                if (CameraRecordShutterView.this.mStartRecordingSet != null) {
                    CameraRecordShutterView.this.mStartRecordingSet.removeAllListeners();
                }
            }
        };
        init(context);
        setOnClickListener(this);
        setTheme();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.take_pic_view, this);
        this.mInnerCircleView = (XImageView) findViewById(R.id.take_pic_inner_circle);
        this.mOuterCircleView = (XImageView) findViewById(R.id.take_pic_outer_circle);
        setSoundEffectsEnabled(false);
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
    }

    private void setTheme() {
        setInnerImageResource();
        this.mOuterCircleView.setImageDrawable(getContext().getDrawable(R.drawable.camera_outer_circle));
    }

    private void setInnerImageResource() {
        this.mInnerCircleView.setImageDrawable(getContext().getDrawable(isRecording() ? R.drawable.camera_inner_circle_recording : R.drawable.camera_inner_circle_record));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XFrameLayout, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ThemeManager.isThemeChanged(newConfig)) {
            setTheme();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (isRecording()) {
            if (ClickHelper.getInstance().isQuickClick(3)) {
                CameraLog.e(TAG, "CameraRecordShutterView, too quick click, 结束录像太快...", false);
                return;
            }
        } else if (ClickHelper.getInstance().isQuickClick(2)) {
            CameraLog.e(TAG, "CameraRecordShutterView, too quick click, 开始录像太快...", false);
            return;
        }
        if (this.mAvmViewModel.isShowPreviewCover()) {
            CameraLog.e(TAG, "Camera preview is loading...", false);
            return;
        }
        CameraLog.d(TAG, "onClick", false);
        this.handleByOnClick = true;
        handleRecord();
        this.handleByOnClick = false;
    }

    public boolean isRecording() {
        return this.mAvmViewModel.isRecording();
    }

    public void handleRecord() {
        CameraLog.d(TAG, "CameraRecordShutterView, handleRecord, isRecording: " + isRecording(), false);
        if (isRecording()) {
            CameraLog.i(TAG, this.mAvmViewModel + "停止录像...", false);
            this.mAvmViewModel.stopRecord();
            stopShutterRecordingAnimation();
        } else if (this.mAvmViewModel.isPreviewOpen()) {
            this.mAvmViewModel.startRecord();
            startShutterDownAnimation();
            startShutterRecordingAnimation();
            if (this.handleByOnClick) {
                BIHelper.getInstance().uploadRecordBI(false, CarCameraHelper.getInstance().getCarCamera().get360CameraType());
            }
        }
    }

    public void onRecordStop() {
        this.mInnerCircleView.setImageDrawable(getContext().getDrawable(R.drawable.camera_inner_circle_record));
    }

    public void startShutterDownAnimation() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleX", 1.0f, 0.8f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleY", 1.0f, 0.8f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400L);
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.start();
    }

    public void onRecordStart() {
        AnimatorSet animatorSet = this.mStartRecordingSet;
        if (animatorSet == null) {
            startShutterDownAnimation();
            startShutterRecordingAnimation();
        } else if (animatorSet.isRunning()) {
        } else {
            resetInnerImageViewAnim();
            this.mInnerCircleView.setImageDrawable(getContext().getDrawable(R.drawable.camera_inner_circle_recording));
        }
    }

    public void startShutterRecordingAnimation() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleX", 0.8f, 0.6f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleY", 0.8f, 0.6f);
        AnimatorSet animatorSet = this.mStartRecordingSet;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mStartRecordingSet = animatorSet2;
        animatorSet2.setDuration(400L);
        this.mStartRecordingSet.addListener(this.mShutterRecordAnimatorListener);
        this.mStartRecordingSet.playTogether(ofFloat, ofFloat2);
        this.mStartRecordingSet.start();
    }

    private void stopShutterRecordingAnimation() {
        resetInnerImageViewAnim();
        this.mInnerCircleView.setImageDrawable(getContext().getDrawable(R.drawable.camera_inner_circle_record));
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleX", 0.6f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleY", 0.6f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400L);
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetInnerImageViewAnim() {
        this.mInnerCircleView.setScaleX(1.0f);
        this.mInnerCircleView.setScaleY(1.0f);
    }
}
