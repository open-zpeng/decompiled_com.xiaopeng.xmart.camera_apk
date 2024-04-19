package com.xiaopeng.xmart.camera.view.controlcamera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.xiaopeng.lib.utils.view.UIUtils;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.speech.vui.utils.VuiUtils;
import com.xiaopeng.vui.commons.VuiAction;
import com.xiaopeng.vui.commons.VuiMode;
import com.xiaopeng.vui.commons.VuiUpdateType;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.R;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.BIHelper;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.ClickHelper;
import com.xiaopeng.xmart.camera.helper.UIResLoader;
import com.xiaopeng.xmart.camera.speech.VuiRadio3DButton;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.VuiManager;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
import com.xiaopeng.xmart.camera.vm.ViewModelManager;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xui.vui.floatinglayer.VuiFloatingLayerManager;
import com.xiaopeng.xui.widget.XFrameLayout;
import com.xiaopeng.xui.widget.XImageView;
/* loaded from: classes.dex */
public class Camera360ControlView extends XFrameLayout implements View.OnClickListener, View.OnTouchListener, ICameraAvmControllerView {
    private static final String TAG = "Camera360ControlView";
    private boolean isAddSceneElement;
    private AvmViewModel mAvmViewModel;
    private ImageView mBackCamera;
    private Drawable mCamera360CircleBgDrawable;
    private Drawable mCamera360CirclePressedBgDrawable;
    private Drawable mCameraLightDrawable;
    private Drawable mCarBodyDrawable;
    private XFrameLayout mDirectionLayout;
    private ImageView mFrontCamera;
    private boolean mIsPressed;
    private ImageView mLastCamera;
    private int mLastType;
    private ImageView mLeftCamera;
    private Rect mLightDrawableRect;
    private int mLightRotateAngle;
    private int mNewCameraType;
    private int mPressedRoteDegree;
    private ImageView mRightCamera;
    private int mRotateDegree;
    private XImageView mSwitch360Camera;
    private VuiRadio3DButton mSwitch3DCamera;

    public Camera360ControlView(Context context) {
        this(context, null);
    }

    public Camera360ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLastType = CarCameraHelper.getInstance().hasAVM() ? CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        this.mNewCameraType = CarCameraHelper.getInstance().hasAVM() ? CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        this.mLightDrawableRect = new Rect();
        init(context);
    }

    private void init(Context context) {
        this.mCarBodyDrawable = UIResLoader.getInstance().getCarBodyDrawable();
        this.mCamera360CircleBgDrawable = UIResLoader.getInstance().getCamera360CircleBgDrawable();
        this.mCamera360CirclePressedBgDrawable = UIResLoader.getInstance().getCamera360CirclePressedBgDrawable();
        LayoutInflater.from(context).inflate(R.layout.camera_360_control_view, this);
        this.mDirectionLayout = (XFrameLayout) findViewById(R.id.camera_direction_switch);
        this.mBackCamera = (ImageView) findViewById(R.id.camera_back);
        this.mFrontCamera = (ImageView) findViewById(R.id.camera_front);
        this.mLeftCamera = (ImageView) findViewById(R.id.camera_left);
        this.mRightCamera = (ImageView) findViewById(R.id.camera_right);
        this.mSwitch3DCamera = (VuiRadio3DButton) findViewById(R.id.pano_2d_3d_switch);
        this.mSwitch360Camera = (XImageView) findViewById(R.id.avm_360_switch);
        this.mBackCamera.setOnClickListener(this);
        this.mFrontCamera.setOnClickListener(this);
        this.mLeftCamera.setOnClickListener(this);
        this.mRightCamera.setOnClickListener(this);
        this.mSwitch3DCamera.setOnClickListener(this);
        this.mSwitch360Camera.setOnClickListener(this);
        this.mBackCamera.setOnTouchListener(this);
        this.mFrontCamera.setOnTouchListener(this);
        this.mLeftCamera.setOnTouchListener(this);
        this.mRightCamera.setOnTouchListener(this);
        if (CarCameraHelper.getInstance().hasAVM()) {
            this.mSwitch3DCamera.setVisibility(0);
            this.mSwitch360Camera.setVisibility(0);
        } else {
            this.mFrontCamera.setVisibility(8);
            this.mLeftCamera.setVisibility(8);
            this.mRightCamera.setVisibility(8);
            this.mSwitch3DCamera.setVisibility(8);
            this.mSwitch360Camera.setVisibility(8);
        }
        setTheme();
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XFrameLayout, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ThemeManager.isThemeChanged(newConfig)) {
            setTheme();
        }
    }

    private void setTheme() {
        int i;
        int i2;
        int i3;
        CameraLog.d(TAG, "setTheme", false);
        setPanoSwitchTheme();
        update360Switch();
        this.mCameraLightDrawable = UIResLoader.getInstance().getCameraLightDrawable();
        update360CameraCircleBg();
        this.mCamera360CirclePressedBgDrawable = UIResLoader.getInstance().getCamera360CirclePressedBgDrawable();
        this.mFrontCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedUpDrawable());
        this.mBackCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedDownDrawable());
        this.mLeftCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedLeftDrawable());
        this.mRightCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedRightDrawable());
        if (this.mNewCameraType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT || (i = this.mNewCameraType) == 4) {
            this.mFrontCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedUpDrawable());
            this.mFrontCamera.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_up));
        } else if (i == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR || (i2 = this.mNewCameraType) == 5) {
            this.mBackCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedDownDrawable());
            this.mBackCamera.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_down));
        } else if (i2 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT || (i3 = this.mNewCameraType) == 6) {
            this.mLeftCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedLeftDrawable());
            this.mLeftCamera.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_left));
        } else if (i3 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT || this.mNewCameraType == 7) {
            this.mRightCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedRightDrawable());
            this.mRightCamera.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_right));
        }
        restoreLightDrawableRect();
        calculateBounds();
    }

    private void setPanoSwitchTheme() {
        this.mSwitch3DCamera.setImageDrawable(CarCameraHelper.getInstance().isShow3DButton() ? UIResLoader.getInstance().getCamera3603DDrawable() : UIResLoader.getInstance().getCamera3602DDrawable());
    }

    private void update360Switch() {
        this.mSwitch360Camera.setImageDrawable(CarCameraHelper.getInstance().getCarCamera().isTransparentChassis() ? UIResLoader.getInstance().getCamera360OnDrawable() : UIResLoader.getInstance().getCamera360OffDrawable());
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0026, code lost:
        if (r6 != 3) goto L9;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouch(android.view.View r5, android.view.MotionEvent r6) {
        /*
            r4 = this;
            java.lang.String r0 = com.xiaopeng.xmart.camera.view.controlcamera.Camera360ControlView.TAG
            java.lang.String r1 = "onTouch  Camera360ControlView "
            r2 = 0
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r0, r1, r2)
            android.widget.ImageView r0 = r4.mBackCamera
            if (r5 == r0) goto L18
            android.widget.ImageView r0 = r4.mFrontCamera
            if (r5 == r0) goto L18
            android.widget.ImageView r0 = r4.mLeftCamera
            if (r5 == r0) goto L18
            android.widget.ImageView r0 = r4.mRightCamera
            if (r5 != r0) goto L61
        L18:
            r0 = -1
            int r6 = r6.getAction()
            r1 = 1
            if (r6 == 0) goto L2f
            if (r6 == r1) goto L29
            r3 = 2
            if (r6 == r3) goto L2f
            r5 = 3
            if (r6 == r5) goto L29
            goto L61
        L29:
            r4.mIsPressed = r2
            r4.invalidate()
            goto L61
        L2f:
            android.widget.ImageView r6 = r4.mBackCamera
            if (r5 != r6) goto L38
            int r0 = com.xiaopeng.xmart.camera.define.CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR
            r4.mPressedRoteDegree = r2
            goto L58
        L38:
            android.widget.ImageView r6 = r4.mFrontCamera
            if (r5 != r6) goto L43
            int r0 = com.xiaopeng.xmart.camera.define.CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT
            r5 = 180(0xb4, float:2.52E-43)
            r4.mPressedRoteDegree = r5
            goto L58
        L43:
            android.widget.ImageView r6 = r4.mLeftCamera
            if (r5 != r6) goto L4e
            int r0 = com.xiaopeng.xmart.camera.define.CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT
            r5 = 90
            r4.mPressedRoteDegree = r5
            goto L58
        L4e:
            android.widget.ImageView r6 = r4.mRightCamera
            if (r5 != r6) goto L58
            int r0 = com.xiaopeng.xmart.camera.define.CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT
            r5 = 270(0x10e, float:3.78E-43)
            r4.mPressedRoteDegree = r5
        L58:
            int r5 = r4.mLastType
            if (r0 == r5) goto L61
            r4.mIsPressed = r1
            r4.invalidate()
        L61:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xmart.camera.view.controlcamera.Camera360ControlView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int i = 5;
        if (ClickHelper.getInstance().isQuickClick(5)) {
            CameraLog.d(TAG, "click too fast,ignore", false);
            return;
        }
        boolean isTransparentChassis = CarCameraHelper.getInstance().getCarCamera().isTransparentChassis();
        switch (v.getId()) {
            case R.id.avm_360_switch /* 2131296372 */:
                if (!isTransparentChassis) {
                    this.mAvmViewModel.changeToTransparent();
                    i = -1;
                } else {
                    i = CarCameraHelper.getInstance().getCarCamera().getLastNormal360CameraType();
                }
                CameraLog.d(TAG, "  avm_360_switch", false);
                break;
            case R.id.camera_back /* 2131296395 */:
                if (!CarCameraHelper.getInstance().is3DMode()) {
                    i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
                    break;
                }
                break;
            case R.id.camera_front /* 2131296399 */:
                if (!CarCameraHelper.getInstance().is3DMode()) {
                    i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT;
                    break;
                } else {
                    i = 4;
                    break;
                }
            case R.id.camera_left /* 2131296401 */:
                if (!CarCameraHelper.getInstance().is3DMode()) {
                    i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT;
                    break;
                } else {
                    i = 6;
                    break;
                }
            case R.id.camera_right /* 2131296407 */:
                if (!CarCameraHelper.getInstance().is3DMode()) {
                    i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT;
                    break;
                } else {
                    i = 7;
                    break;
                }
            case R.id.pano_2d_3d_switch /* 2131296570 */:
                if (isTransparentChassis && CarCameraHelper.getInstance().is3DMode()) {
                    i = CarCameraHelper.getInstance().getCarCamera().getLastNormal360CameraType();
                } else {
                    this.mAvmViewModel.changeSmallLargePreview();
                    i = -1;
                }
                CameraLog.d(TAG, "onClick, isTransparent: " + isTransparentChassis + ",is3DMode:" + CarCameraHelper.getInstance().is3DMode(), false);
                break;
            default:
                i = -1;
                break;
        }
        CameraLog.d(TAG, "Camera360ControlView, onClick change 360 direction, cameraType : " + i + " mLastType:" + this.mLastType + " isTransparent:" + isTransparentChassis, false);
        if ((this.mLastType != i || isTransparentChassis) && i != -1) {
            changeCamera(i);
            BIHelper.getInstance().uploadDisplayModeSwitchBI(i);
        }
    }

    @Override // com.xiaopeng.xmart.camera.view.controlcamera.ICameraAvmControllerView
    public void onCamera360Change() {
        int i = CarCameraHelper.getInstance().getCarCamera().get360CameraType();
        boolean isTransparentChassis = CarCameraHelper.getInstance().getCarCamera().isTransparentChassis();
        CameraLog.d(TAG, "onCamera360Change, cameraType: " + i + ",transparent:" + isTransparentChassis, false);
        updateScene(i);
        update360CameraCircleBg();
        if (isTransparentChassis) {
            if (this.mNewCameraType != CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS) {
                this.mLastType = this.mNewCameraType;
                this.mNewCameraType = i;
            }
            ImageView imageView = this.mLastCamera;
            if (imageView != null) {
                imageView.setImageDrawable(getContext().getDrawable(((Integer) this.mLastCamera.getTag()).intValue()));
            }
        } else {
            if (this.mNewCameraType != CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS) {
                this.mLastType = this.mNewCameraType;
            }
            this.mNewCameraType = i;
            updateCamera360Ui(i);
        }
        setPanoSwitchTheme();
        update360Switch();
        calculateCameraLightBounds();
        calculateBounds();
    }

    private View getDirectionView(int type) {
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT || type == 4) {
            return this.mFrontCamera;
        }
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR || type == 5) {
            return this.mBackCamera;
        }
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT || type == 6) {
            return this.mLeftCamera;
        }
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT || type == 7) {
            return this.mRightCamera;
        }
        return null;
    }

    private void changeCamera(int type) {
        CameraLog.i(TAG, "changeCamera type:" + type + " mNewCamera:" + this.mNewCameraType + " mAvmViewModel:" + this.mAvmViewModel, false);
        ImageView imageView = this.mLastCamera;
        if (imageView != null) {
            imageView.setImageDrawable(getContext().getDrawable(((Integer) this.mLastCamera.getTag()).intValue()));
        }
        AvmViewModel avmViewModel = this.mAvmViewModel;
        if (avmViewModel != null) {
            avmViewModel.switchCamera(type);
        }
    }

    private void updateCamera360Ui(int type) {
        this.mLastType = type;
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT || type == 4) {
            this.mFrontCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedUpDrawable());
            this.mBackCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedDownDrawable());
            this.mLeftCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedLeftDrawable());
            this.mRightCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedRightDrawable());
            ImageView imageView = this.mFrontCamera;
            this.mLastCamera = imageView;
            imageView.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_up));
        } else if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR || type == 5) {
            this.mBackCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedDownDrawable());
            this.mFrontCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedUpDrawable());
            this.mLeftCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedLeftDrawable());
            this.mRightCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedRightDrawable());
            ImageView imageView2 = this.mBackCamera;
            this.mLastCamera = imageView2;
            imageView2.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_down));
        } else if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT || type == 6) {
            this.mLeftCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedLeftDrawable());
            this.mFrontCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedUpDrawable());
            this.mBackCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedDownDrawable());
            this.mRightCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedRightDrawable());
            ImageView imageView3 = this.mLeftCamera;
            this.mLastCamera = imageView3;
            imageView3.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_left));
        } else if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT || type == 7) {
            this.mRightCamera.setImageDrawable(UIResLoader.getInstance().getCamera360SelectedRightDrawable());
            this.mFrontCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedUpDrawable());
            this.mBackCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedDownDrawable());
            this.mLeftCamera.setImageDrawable(UIResLoader.getInstance().getCamera360UnSelectedLeftDrawable());
            ImageView imageView4 = this.mRightCamera;
            this.mLastCamera = imageView4;
            imageView4.setTag(Integer.valueOf((int) R.drawable.camera_pano_unselected_right));
        }
    }

    private void update360CameraCircleBg() {
        if (CarCameraHelper.getInstance().getCarCamera().isTransparentChassis()) {
            this.mCamera360CircleBgDrawable = UIResLoader.getInstance().getCamera360CircleTransBgDrawable();
        } else {
            this.mCamera360CircleBgDrawable = UIResLoader.getInstance().getCamera360CircleBgDrawable();
        }
    }

    private void calculateCarBodyBounds() {
        if (this.mCarBodyDrawable != null) {
            int width = (getWidth() - this.mCarBodyDrawable.getIntrinsicWidth()) / 2;
            int height = (getHeight() - this.mCarBodyDrawable.getIntrinsicHeight()) / 2;
            this.mCarBodyDrawable.setBounds(width, height, this.mCarBodyDrawable.getIntrinsicWidth() + width, this.mCarBodyDrawable.getIntrinsicHeight() + height);
        }
    }

    private void calculateCameraLightBounds() {
        int width = (getWidth() - this.mCameraLightDrawable.getIntrinsicWidth()) / 2;
        int intrinsicWidth = this.mCameraLightDrawable.getIntrinsicWidth() + width;
        int dip2px = UIUtils.dip2px(getContext(), 0.0f);
        int intrinsicHeight = this.mCameraLightDrawable.getIntrinsicHeight() + dip2px;
        this.mCameraLightDrawable.setBounds(width, dip2px, intrinsicWidth, intrinsicHeight);
        this.mLightDrawableRect.left = width;
        this.mLightDrawableRect.right = intrinsicWidth;
        this.mLightDrawableRect.top = dip2px;
        this.mLightDrawableRect.bottom = intrinsicHeight;
    }

    private void restoreLightDrawableRect() {
        if (getVisibility() == 0) {
            Rect bounds = UIResLoader.getInstance().getCameraLightDrawable().getBounds();
            bounds.left = this.mLightDrawableRect.left;
            bounds.right = this.mLightDrawableRect.right;
            bounds.top = this.mLightDrawableRect.top;
            bounds.bottom = this.mLightDrawableRect.bottom;
        }
    }

    private void calculateBounds() {
        int i;
        int i2;
        int i3;
        int width = (getWidth() - this.mCamera360CircleBgDrawable.getIntrinsicWidth()) / 2;
        int intrinsicWidth = this.mCamera360CircleBgDrawable.getIntrinsicWidth() + width;
        int height = (getHeight() - this.mCamera360CircleBgDrawable.getIntrinsicHeight()) / 2;
        int intrinsicHeight = this.mCamera360CircleBgDrawable.getIntrinsicHeight() + height;
        this.mCamera360CircleBgDrawable.setBounds(width, height, intrinsicWidth, intrinsicHeight);
        this.mCamera360CirclePressedBgDrawable.setBounds(width, height, intrinsicWidth, intrinsicHeight);
        if (this.mNewCameraType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT || (i = this.mNewCameraType) == 4) {
            this.mRotateDegree = 0;
            this.mLightRotateAngle = CarCameraHelper.getInstance().is3DMode() ? 180 : 0;
        } else if (i == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR || (i2 = this.mNewCameraType) == 5) {
            this.mRotateDegree = 180;
            this.mLightRotateAngle = CarCameraHelper.getInstance().is3DMode() ? 0 : 180;
        } else {
            if (i2 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT || (i3 = this.mNewCameraType) == 6) {
                this.mRotateDegree = 270;
                this.mLightRotateAngle = CarCameraHelper.getInstance().is3DMode() ? 90 : 270;
            } else if (i3 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT || this.mNewCameraType == 7) {
                this.mRotateDegree = 90;
                this.mLightRotateAngle = CarCameraHelper.getInstance().is3DMode() ? 270 : 90;
            }
        }
        CameraLog.i(TAG, "calculateBounds rotatedegree:" + this.mRotateDegree + " light:" + this.mLightRotateAngle + " mIsPressed:" + this.mIsPressed + " cameraType:" + this.mNewCameraType, false);
        invalidate();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        calculateCarBodyBounds();
        calculateCameraLightBounds();
        calculateBounds();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(this.mRotateDegree, (float) (getWidth() / 2.0d), (float) (getHeight() / 2.0d));
        this.mCamera360CircleBgDrawable.draw(canvas);
        canvas.restore();
        if (this.mIsPressed) {
            canvas.save();
            canvas.rotate(this.mPressedRoteDegree, (float) (getWidth() / 2.0d), (float) (getHeight() / 2.0d));
            this.mCamera360CirclePressedBgDrawable.draw(canvas);
            canvas.restore();
        }
        CameraLog.i(TAG, "dispatchDraw rotatedegree:" + this.mRotateDegree + " light:" + this.mLightRotateAngle + " mIsPressed:" + this.mIsPressed, false);
        canvas.save();
        canvas.rotate(this.mLightRotateAngle, (float) (getWidth() / 2.0d), (float) (getHeight() / 2.0d));
        if (CarCameraHelper.getInstance().is3DMode()) {
            canvas.translate(0.0f, getHeight() - this.mCameraLightDrawable.getIntrinsicHeight());
        }
        if (!CarCameraHelper.getInstance().getCarCamera().isTransparentChassis()) {
            this.mCameraLightDrawable.draw(canvas);
        }
        canvas.restore();
        Drawable drawable = this.mCarBodyDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        canvas.save();
        int[] viewCenter = getViewCenter(this.mFrontCamera);
        float width = (getWidth() - this.mDirectionLayout.getWidth()) / 2;
        canvas.translate(width, 0.0f);
        canvas.rotate(CarCameraHelper.getInstance().isShow3DButton() ? 180.0f : 0.0f, viewCenter[0], viewCenter[1]);
        drawChild(canvas, this.mFrontCamera, getDrawingTime());
        canvas.restore();
        canvas.save();
        int[] viewCenter2 = getViewCenter(this.mBackCamera);
        canvas.translate(width, 0.0f);
        canvas.rotate(CarCameraHelper.getInstance().isShow3DButton() ? 180.0f : 0.0f, viewCenter2[0], viewCenter2[1]);
        drawChild(canvas, this.mBackCamera, getDrawingTime());
        canvas.restore();
        canvas.save();
        int[] viewCenter3 = getViewCenter(this.mLeftCamera);
        canvas.translate(width, 0.0f);
        canvas.rotate(CarCameraHelper.getInstance().isShow3DButton() ? 180.0f : 0.0f, viewCenter3[0], viewCenter3[1]);
        drawChild(canvas, this.mLeftCamera, getDrawingTime());
        canvas.restore();
        canvas.save();
        int[] viewCenter4 = getViewCenter(this.mRightCamera);
        canvas.translate(width, 0.0f);
        canvas.rotate(CarCameraHelper.getInstance().isShow3DButton() ? 180.0f : 0.0f, viewCenter4[0], viewCenter4[1]);
        drawChild(canvas, this.mRightCamera, getDrawingTime());
        canvas.restore();
        drawChild(canvas, this.mSwitch3DCamera, getDrawingTime());
        drawChild(canvas, this.mSwitch360Camera, getDrawingTime());
    }

    private int[] getViewCenter(View view) {
        return new int[]{(view.getLeft() + view.getRight()) / 2, (view.getTop() + view.getBottom()) / 2};
    }

    @Override // com.xiaopeng.xmart.camera.view.controlcamera.ICameraAvmControllerView
    public void vuiAvmSwitchHandle(int displayMode) {
        View directionView = getDirectionView(displayMode);
        if (directionView != null) {
            VuiFloatingLayerManager.show(directionView);
            AvmViewModel avmViewModel = this.mAvmViewModel;
            if (avmViewModel != null) {
                avmViewModel.switchCamera(displayMode);
            }
        }
    }

    public void updateScene(int cameraType) {
        if (CarCameraHelper.getInstance().hasAVM()) {
            setVuiMode(VuiMode.NORMAL);
            int avmVuiSelectId = getAvmVuiSelectId(cameraType);
            CameraLog.d(TAG, "updateScene selectId:" + avmVuiSelectId, false);
            if (!this.isAddSceneElement) {
                this.isAddSceneElement = true;
                VuiUtils.addHasFeedbackProp(this.mDirectionLayout);
                VuiUtils.setStatefulButtonAttr(this.mDirectionLayout, 0, App.getInstance().getResources().getStringArray(R.array.avm_state_labels), VuiAction.SETVALUE.getName());
            } else {
                VuiUtils.setStatefulButtonValue(this.mDirectionLayout, avmVuiSelectId);
            }
            this.mSwitch3DCamera.setCheckIndex(CarCameraHelper.getInstance().isShow3DButton() ? 2 : 1);
            this.mSwitch360Camera.setSelected(CarCameraHelper.getInstance().getCarCamera().isTransparentChassis());
            VuiManager.getInstance().updateVuiScene(VuiManager.SCENE_MAIN, this, VuiUpdateType.UPDATE_VIEW);
        }
    }

    private int getAvmVuiSelectId(int cameraType) {
        int i;
        if (cameraType == 6 || cameraType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
            i = 2;
        } else if (cameraType == 7 || cameraType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
            i = 3;
        } else if (cameraType == 4 || cameraType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
            i = 1;
        } else {
            i = (cameraType == 5 || cameraType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) ? 0 : -1;
        }
        if (i < 0) {
            return 4;
        }
        return i;
    }
}
