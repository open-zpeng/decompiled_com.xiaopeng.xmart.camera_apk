package com.xiaopeng.xmart.camera.view.controlpanel;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class CameraShutterView extends XFrameLayout implements View.OnClickListener {
    private static final long DURATION = 400;
    private static final String TAG = "CameraShutterView";
    private AvmViewModel mAvmViewModel;
    private XImageView mInnerCircleView;
    private long mLastCaptureTime;
    private XImageView mOutCircleView;

    public CameraShutterView(Context context) {
        this(context, null);
    }

    public CameraShutterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setOnClickListener(this);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.take_pic_view, this);
        this.mInnerCircleView = (XImageView) findViewById(R.id.take_pic_inner_circle);
        this.mOutCircleView = (XImageView) findViewById(R.id.take_pic_outer_circle);
        setSoundEffectsEnabled(false);
        setTheme();
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
    }

    private void setTheme() {
        this.mInnerCircleView.setImageDrawable(getContext().getDrawable(R.drawable.camera_inner_circle));
        this.mOutCircleView.setImageDrawable(getContext().getDrawable(R.drawable.camera_outer_circle));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XFrameLayout, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ThemeManager.isThemeChanged(newConfig)) {
            setTheme();
        }
    }

    public void takePicture() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mLastCaptureTime < 1000) {
            return;
        }
        this.mLastCaptureTime = elapsedRealtime;
        if (this.mAvmViewModel != null) {
            CameraLog.i(TAG, this.mAvmViewModel + " takePicture...", false);
            this.mAvmViewModel.takePicture();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        CameraLog.d(TAG, "开始手动拍照.", false);
        if (!this.mAvmViewModel.isTakingPic()) {
            takePicture();
            BIHelper.getInstance().uploadCaptureBI(false, CarCameraHelper.getInstance().getCarCamera().get360CameraType(), 0);
            return;
        }
        CameraLog.d(TAG, "Can not takePicture While Recording", false);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != 0) {
            if (action == 1 || action == 3) {
                startShutterUpAnimation();
            }
        } else if (ClickHelper.getInstance().isTooQuickClick()) {
            CameraLog.e(TAG, "Action down, Capture time < 1s, return", false);
            return false;
        } else if (this.mAvmViewModel.isShowPreviewCover()) {
            CameraLog.e(TAG, "Camera preview is loading...", false);
            return false;
        } else {
            startShutterDownAnimation();
        }
        super.onTouchEvent(event);
        return true;
    }

    public void startShutterDownAnimation() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleX", 1.0f, 0.8f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleY", 1.0f, 0.8f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400L);
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.start();
    }

    public void startShutterUpAnimation() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleX", 0.8f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mInnerCircleView, "scaleY", 0.8f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400L);
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.start();
    }
}
