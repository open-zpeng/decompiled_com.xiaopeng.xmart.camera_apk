package com.xiaopeng.xmart.camera.view.controlcamera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.xiaopeng.xmart.camera.speech.VuiRadio3DButton;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ToastUtils;
import com.xiaopeng.xmart.camera.utils.VuiManager;
import com.xiaopeng.xmart.camera.vm.IAvmViewModel;
import com.xiaopeng.xmart.camera.vm.ViewModelManager;
import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import com.xiaopeng.xui.vui.floatinglayer.VuiFloatingLayerManager;
import com.xiaopeng.xui.widget.XImageView;
import com.xiaopeng.xui.widget.XRelativeLayout;
/* loaded from: classes.dex */
public class CameraControllerView extends XRelativeLayout implements View.OnClickListener, ICameraAvmControllerView {
    private static final int AREA_BOTTOM = 2;
    private static final int AREA_CENTER = 5;
    private static final int AREA_LEFT = 3;
    private static final int AREA_NONE = 0;
    private static final int AREA_RIGHT = 4;
    private static final int AREA_TOP = 1;
    private static final double CENTER_IMG_RADIUS = 38.0d;
    private static final double CENTER_IMG_WIDTH = 76.0d;
    private static final String TAG = "CameraControllerView";
    private boolean isAddSceneElement;
    private AvmViewModel mAvmViewModel;
    private int mCameraDirection;
    private XImageView mCameraDirectionImg;
    private double mCenterX;
    private double mCenterY;
    private XImageView mSwitch360Camera;
    private VuiRadio3DButton mSwitch3DCamera;
    private int mTransparentX;
    private int mTransparentY;
    private int mWidthX;

    public CameraControllerView(Context context) {
        this(context, null);
    }

    public CameraControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_camera_controller, this);
        this.mCameraDirectionImg = (XImageView) inflate.findViewById(R.id.camera_direction_switch);
        this.mSwitch3DCamera = (VuiRadio3DButton) inflate.findViewById(R.id.camera_pano_switch_img);
        this.mSwitch360Camera = (XImageView) inflate.findViewById(R.id.camera_tranc);
        this.mSwitch3DCamera.setOnClickListener(this);
        this.mSwitch360Camera.setOnClickListener(this);
    }

    private void initData() {
        this.mAvmViewModel = (AvmViewModel) ViewModelManager.getInstance().getViewModelImpl(IAvmViewModel.class);
        this.mCameraDirection = CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS;
        BIHelper.getInstance().uploadDisplayModeSwitchBI(CarCameraHelper.getInstance().getCarCamera().get360CameraType());
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mCenterX = this.mCameraDirectionImg.getMeasuredWidth() / 2.0d;
        this.mCenterY = this.mCameraDirectionImg.getMeasuredHeight() / 2.0d;
        this.mWidthX = getMeasuredWidth();
        this.mTransparentX = this.mSwitch360Camera.getMeasuredWidth();
        this.mTransparentY = this.mSwitch360Camera.getMeasuredHeight();
    }

    @Override // com.xiaopeng.xmart.camera.view.controlcamera.ICameraAvmControllerView
    public void onCamera360Change() {
        int i = CarCameraHelper.getInstance().getCarCamera().get360CameraType();
        boolean isTransparentChassis = CarCameraHelper.getInstance().getCarCamera().isTransparentChassis();
        CameraLog.d(TAG, "onCamera360Change cameraType:" + i + ", is3DMode:" + CarCameraHelper.getInstance().is3DMode(), false);
        changeToTransparent(isTransparentChassis);
        if (!isTransparentChassis) {
            this.mCameraDirection = i;
            setCameraDirectionView(i);
            setCamera3dModeView();
        }
        updateScene(i);
    }

    public void setCameraDirectionView(int direction) {
        if (direction == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_front);
        } else if (direction == 4) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_front_3d);
        } else if (direction == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_rear);
        } else if (direction == 5) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_rear_3d);
        } else if (direction == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_left);
        } else if (direction == 6) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_left_3d);
        } else if (direction == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_right);
        } else if (direction == 7) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_right_3d);
        } else {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_rear);
        }
    }

    private void setCamera3dModeView() {
        if (CarCameraHelper.getInstance().isShow3DButton()) {
            this.mSwitch3DCamera.setImageResource(R.drawable.ic_camera_circular_3d);
        } else {
            this.mSwitch3DCamera.setImageResource(R.drawable.ic_camera_circular_2d);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01c4  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r14) {
        /*
            Method dump skipped, instructions count: 475
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xmart.camera.view.controlcamera.CameraControllerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean isTransparentChassisEvent(MotionEvent event) {
        return event.getY() < ((float) this.mTransparentY) && event.getX() > ((float) (this.mWidthX - this.mTransparentX));
    }

    private int getTouchEventArea(MotionEvent event) {
        double x = event.getX();
        double y = event.getY();
        double d = x - this.mCenterX;
        double d2 = y - this.mCenterY;
        double sqrt = Math.sqrt((d * d) + (d2 * d2));
        int i = 1;
        if (sqrt <= CENTER_IMG_RADIUS) {
            i = 5;
        } else {
            double acos = Math.acos(d / sqrt) * (y < this.mCenterY ? -1 : 1);
            CameraLog.d(TAG, "radian is " + acos);
            if (acos > 0.7853981633974483d && acos < 2.356194490192345d) {
                i = 2;
            } else if ((acos > 0.0d && acos < 0.7853981633974483d) || (acos < 0.0d && acos > -0.7853981633974483d)) {
                i = 4;
            } else if (acos > 2.356194490192345d || acos < -2.356194490192345d) {
                i = 3;
            } else if (acos >= -0.7853981633974483d || acos <= -2.356194490192345d) {
                i = 0;
            }
        }
        CameraLog.d(TAG, "Touch area is: " + i + ",x:" + x + ",y:" + y, false);
        return i;
    }

    private void changeToTransparent(boolean state) {
        if (state) {
            this.mCameraDirectionImg.setImageResource(R.drawable.ic_camera_direction_centent);
            this.mSwitch3DCamera.setImageResource(R.drawable.ic_camera_circular_2d);
            this.mSwitch360Camera.setImageResource(R.drawable.ic_mid_switch_360_open);
            return;
        }
        this.mSwitch360Camera.setImageResource(R.drawable.ic_mid_switch_360);
    }

    @Override // com.xiaopeng.xmart.camera.view.controlcamera.ICameraAvmControllerView
    public void vuiAvmSwitchHandle(int displayMode) {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (!this.mAvmViewModel.isAVMActived()) {
            playSoundEffect(0);
            CameraLog.d(TAG, "AVM is not actived", false);
            ToastUtils.showToast((int) R.string.camera_avm_preparing);
            return;
        }
        if (displayMode == 6 || displayMode == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
            i = -130;
        } else if (displayMode != 7 && displayMode != CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
            if (displayMode == 4 || displayMode == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
                i3 = -80;
            } else if (displayMode != 5 && displayMode != CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) {
                i2 = 0;
                VuiFloatingLayerManager.show(this, i4, i2);
                this.mAvmViewModel.switchCamera(displayMode);
            } else {
                i3 = 80;
            }
            i2 = i3;
            i4 = -50;
            VuiFloatingLayerManager.show(this, i4, i2);
            this.mAvmViewModel.switchCamera(displayMode);
        } else {
            i = 30;
        }
        i4 = i;
        i2 = 0;
        VuiFloatingLayerManager.show(this, i4, i2);
        this.mAvmViewModel.switchCamera(displayMode);
    }

    public void updateScene(int cameraType) {
        setVuiMode(VuiMode.NORMAL);
        int avmVuiSelectId = getAvmVuiSelectId(cameraType);
        CameraLog.d(TAG, "updateScene selectId:" + avmVuiSelectId, false);
        if (!this.isAddSceneElement) {
            this.isAddSceneElement = true;
            VuiUtils.addHasFeedbackProp(this.mCameraDirectionImg);
            VuiUtils.setStatefulButtonAttr(this.mCameraDirectionImg, 0, App.getInstance().getResources().getStringArray(R.array.avm_state_labels), VuiAction.SETVALUE.getName());
        } else {
            VuiUtils.setStatefulButtonValue(this.mCameraDirectionImg, avmVuiSelectId);
        }
        this.mSwitch3DCamera.setCheckIndex(CarCameraHelper.getInstance().isShow3DButton() ? 2 : 1);
        this.mSwitch360Camera.setSelected(CarCameraHelper.getInstance().getCarCamera().isTransparentChassis());
        VuiManager.getInstance().updateVuiScene(VuiManager.SCENE_MAIN, this, VuiUpdateType.UPDATE_VIEW);
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

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        CameraLog.d(TAG, "onClick,", false);
        if (!this.mAvmViewModel.isAVMActived()) {
            CameraLog.d(TAG, "AVM is not actived", false);
            ToastUtils.showToast((int) R.string.camera_avm_preparing);
        } else if (ClickHelper.getInstance().isQuickClick(5)) {
            CameraLog.d(TAG, "click too fast,ignore", false);
        } else {
            boolean isTransparentChassis = CarCameraHelper.getInstance().getCarCamera().isTransparentChassis();
            int id = v.getId();
            if (id != R.id.camera_pano_switch_img) {
                if (id != R.id.camera_tranc) {
                    return;
                }
                CameraLog.d(TAG, "onClick camera_tranc,", false);
                clickTransparentButton(isTransparentChassis);
                return;
            }
            int i = this.mCameraDirection;
            if (!isTransparentChassis || !CarCameraHelper.getInstance().is3DMode()) {
                i = CarCameraHelper.getInstance().getCarCamera().get360CameraSwitch3DDisplayMode(this.mCameraDirection);
            }
            this.mCameraDirection = i;
            this.mAvmViewModel.switchCamera(i);
            BIHelper.getInstance().uploadDisplayModeSwitchBI(this.mCameraDirection);
        }
    }

    private void clickTransparentButton(boolean transparentState) {
        if (transparentState) {
            this.mAvmViewModel.changeToNormal();
            BIHelper.getInstance().uploadTransparentSwitchBI(false);
            BIHelper.getInstance().uploadDisplayModeSwitchBI(CarCameraHelper.getInstance().getCarCamera().getLastNormal360CameraType());
            return;
        }
        this.mAvmViewModel.changeToTransparent();
        BIHelper.getInstance().uploadTransparentSwitchBI(true);
    }
}
