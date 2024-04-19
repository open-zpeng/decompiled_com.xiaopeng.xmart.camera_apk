package com.xiaopeng.xmart.camera.view.controlpanel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.speech.vui.utils.VuiUtils;
import com.xiaopeng.vui.commons.VuiAction;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.R;
import com.xiaopeng.xmart.camera.helper.ClickHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView;
import com.xiaopeng.xui.widget.XImageView;
import com.xiaopeng.xui.widget.XLinearLayout;
/* loaded from: classes.dex */
public class CameraSwitchView extends XLinearLayout implements ICameraSwitchView {
    public static final int CAMERA_MODE_CAPTURE = 0;
    public static final int CAMERA_MODE_RECORD = 1;
    private static final long DURATION = 300;
    private static final String TAG = "CameraSwitchView";
    private XImageView mCameraCaptureImg;
    private XImageView mCameraRecordImg;
    private Drawable mCoverDrawable;
    private int mLastMode;
    private int mNewMode;
    private ICameraSwitchView.OnSwitchChangeListener mOnCameraModeSwitchListener;
    private View.OnClickListener mOnClickListener;
    private float mTranslate;
    private String[] mVuiLabels;

    /* loaded from: classes.dex */
    public interface OnCameraModeSwitchListener {
        void onCameraModeSwitch(int mode);
    }

    public CameraSwitchView(Context context) {
        this(context, null);
    }

    public CameraSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLastMode = -1;
        this.mNewMode = -2;
        this.mVuiLabels = new String[]{App.getInstance().getString(R.string.vui_switch_pic), App.getInstance().getString(R.string.vui_switch_record)};
        this.mOnClickListener = new View.OnClickListener() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraSwitchView$PsO-bMaCk9GCR79SJm9NrQlQ4RY
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CameraSwitchView.this.lambda$new$1$CameraSwitchView(view);
            }
        };
        init(context);
        setClipChildren(false);
    }

    private void init(Context context) {
        initData();
        LayoutInflater.from(context).inflate(R.layout.camera_mode_switch_layout, this);
        this.mCameraCaptureImg = (XImageView) findViewById(R.id.img_camera_mode_capture);
        this.mCameraRecordImg = (XImageView) findViewById(R.id.img_camera_mode_record);
        this.mCameraCaptureImg.setOnClickListener(this.mOnClickListener);
        this.mCameraRecordImg.setOnClickListener(this.mOnClickListener);
        CameraLog.e(TAG, "init   return,  : " + this.mLastMode, false);
        setCameraImageResource(0);
        setOrientation(1);
        setGravity(1);
        setBackgroundResource(R.drawable.bg_camera_mode_switch_view);
        VuiUtils.setStatefulButtonAttr(this, 0, this.mVuiLabels, VuiAction.SETVALUE.getName() + "|" + VuiAction.CLICK.getName());
    }

    private void initData() {
        this.mNewMode = 0;
        this.mLastMode = 0;
        this.mCoverDrawable = getContext().getDrawable(R.drawable.camera_mode_switch_cover);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        this.mCoverDrawable.draw(canvas);
        super.dispatchDraw(canvas);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.mTranslate = getViewParent(this.mNewMode).getTop();
            calculateCoverBounds();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ThemeManager.isThemeChanged(newConfig)) {
            invalidate();
        }
    }

    private View getViewParent(int mode) {
        return mode == 1 ? this.mCameraRecordImg : this.mCameraCaptureImg;
    }

    private void calculateCoverBounds() {
        int left = getViewParent(this.mNewMode).getLeft();
        int i = (int) this.mTranslate;
        this.mCoverDrawable.setBounds(left, i, this.mCoverDrawable.getIntrinsicWidth() + left, this.mCoverDrawable.getIntrinsicHeight() + i);
        invalidate();
    }

    protected void startSliderAnimation(int start, int end) {
        ValueAnimator ofInt = ValueAnimator.ofInt(start, end);
        ofInt.setDuration(300L);
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.xiaopeng.xmart.camera.view.controlpanel.-$$Lambda$CameraSwitchView$PSHyYXqFKy_g5_iqRuyqDBMLVIQ
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CameraSwitchView.this.lambda$startSliderAnimation$0$CameraSwitchView(valueAnimator);
            }
        });
        ofInt.start();
    }

    public /* synthetic */ void lambda$startSliderAnimation$0$CameraSwitchView(ValueAnimator valueAnimator) {
        this.mTranslate = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        calculateCoverBounds();
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView
    public void selectTab(boolean isPhoto) {
        CameraLog.e(TAG, "setCaptureMode, mode ?:" + isPhoto, false);
        handleItemChange(isPhoto ? this.mCameraCaptureImg : this.mCameraRecordImg);
    }

    private void setCameraImageResource(int mode) {
        CameraLog.e(TAG, "setCameraImageResource, mode ?:" + mode, false);
        this.mCameraCaptureImg.setImageResource(mode == 0 ? R.drawable.ic_switch_photo_pressed : R.drawable.ic_switch_photo_normal);
        this.mCameraRecordImg.setImageResource(mode == 0 ? R.drawable.ic_switch_record_normal : R.drawable.ic_switch_record_pressed);
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView
    public void setOnCameraModeSwitchListener(ICameraSwitchView.OnSwitchChangeListener listener) {
        this.mOnCameraModeSwitchListener = listener;
    }

    public /* synthetic */ void lambda$new$1$CameraSwitchView(View view) {
        if (ClickHelper.getInstance().isTooQuickClick()) {
            CameraLog.e(TAG, "CameraNavigationView, too quick click...", false);
        } else {
            handleItemChange(view);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void handleItemChange(View v) {
        int i = v.getId() == R.id.img_camera_mode_record ? 1 : 0;
        if (this.mLastMode == i) {
            CameraLog.e(TAG, "HandleItemChange return, mLastType: " + this.mLastMode, false);
            return;
        }
        CameraLog.d(TAG, "Switch camera mode: " + i, false);
        this.mNewMode = i;
        View viewParent = getViewParent(this.mLastMode);
        View viewParent2 = getViewParent(this.mNewMode);
        int top = viewParent.getTop();
        int top2 = viewParent2.getTop();
        if (top > 0 && top2 > 0) {
            startSliderAnimation(top, top2);
        }
        this.mLastMode = i;
        setCameraImageResource(i);
        ICameraSwitchView.OnSwitchChangeListener onSwitchChangeListener = this.mOnCameraModeSwitchListener;
        if (onSwitchChangeListener != null) {
            onSwitchChangeListener.onTabChangeEnd(this, i, true, true);
        }
        CameraLog.d(TAG, "updateSwitchScene mIsPhotoMode:" + (i ^ 1), false);
        VuiUtils.setStatefulButtonValue(this, i);
    }
}
