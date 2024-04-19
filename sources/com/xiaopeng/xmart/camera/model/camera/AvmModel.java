package com.xiaopeng.xmart.camera.model.camera;

import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.manager.CameraManager;
import com.xiaopeng.xmart.camera.model.camera.ICameraModel;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.FileUtils;
import java.io.File;
/* loaded from: classes.dex */
public class AvmModel extends CameraModel {
    private static final String TAG = "com.xiaopeng.xmart.camera.model.camera.AvmModel";
    protected int mLastMode;
    protected int mLastNormalMode;

    public AvmModel(ICameraModel.Callback callback) {
        super(callback);
        this.mLastNormalMode = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        this.mLastMode = CarCameraHelper.getInstance().hasAVM() ? CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        CameraLog.d(TAG, "callback", false);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public String getGalleryTab() {
        return CarCameraHelper.getInstance().hasAVM() ? CameraDefine.TAB_CAMERA_360 : CameraDefine.TAB_CAMERA_BACK;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public String getFileDir() {
        String str = FileUtils.DIR_360_FULL_PATH;
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str + File.separator;
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public int getBucketId() {
        return FileUtils.BUCKET_ID_360;
    }

    public void onCameraOpen() {
        CameraLog.i(TAG, "onCameraOpen mLastNormalMode:" + this.mLastNormalMode + ",mLastMode:" + this.mLastMode, false);
        switchCamera(this.mLastMode);
    }

    public void changeToRear() {
        if (this.mLastMode == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR || !CarCameraHelper.getInstance().hasAVM()) {
            return;
        }
        setCameraDisplayMode(this.mLastMode);
    }

    public void changeTo360() {
        if (this.mLastMode == CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS || !CarCameraHelper.getInstance().hasAVM()) {
            return;
        }
        setCameraDisplayMode(this.mLastMode);
    }

    public void switchCamera(int direction) {
        CameraLog.d(TAG, "switchCamera, direction:" + direction + " mLastNormalMode:" + this.mLastNormalMode, false);
        if (direction != CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS && direction != 8) {
            this.mLastNormalMode = direction;
        }
        setCameraDisplayMode(direction);
    }

    public void changeToNormal() {
        CameraLog.d(TAG, "changeToNormal, mLastNormalMode:" + this.mLastNormalMode, false);
        switchCamera(this.mLastNormalMode);
    }

    public void changeToTransparent() {
        String str = TAG;
        CameraLog.d(str, "changeToTransparent mLastNormalMode:" + this.mLastNormalMode, false);
        switchCamera(CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS);
        CameraLog.d(str, "setTransparentChassis cameraType:" + CarCameraHelper.getInstance().getCarCamera().get360CameraType(), false);
    }

    public void changeToFourCamera() {
        CameraLog.d(TAG, "changeToFourCamera mLastNormalMode:" + this.mLastNormalMode, false);
        switchCamera(8);
    }

    public void changeSmallLargePreview() {
        int i = CarCameraHelper.getInstance().getCarCamera().get360CameraSwitch3DDisplayMode(this.mLastNormalMode);
        this.mLastNormalMode = i;
        switchCamera(i);
        CameraLog.d(TAG, "changeSmallLargePreview lastCameraType:" + this.mLastNormalMode + " cameraType:" + i, false);
    }

    public void setCameraDisplayMode(final int direction) {
        this.mLastMode = direction;
        CarCameraHelper.getInstance().getCarCamera().setLastNormal360CameraType(this.mLastNormalMode);
        if (CarCameraHelper.getInstance().is360Camera(direction)) {
            CarCameraHelper.getInstance().update3DMode(direction);
            try {
                try {
                    CameraManager.getInstance().setAVMDisplayMode(direction);
                    if (this.mCallback == null) {
                        return;
                    }
                } catch (Exception e) {
                    if (this.mCallback != null) {
                        ((IAvmCallBack) this.mCallback).onAvmSwitchFail();
                    }
                    e.printStackTrace();
                    CameraLog.d(TAG, "setCameraDisplayMode exception:" + e.getMessage(), false);
                    if (this.mCallback == null) {
                        return;
                    }
                }
                ((IAvmCallBack) this.mCallback).onCamera360Change();
            } catch (Throwable th) {
                if (this.mCallback != null) {
                    ((IAvmCallBack) this.mCallback).onCamera360Change();
                }
                throw th;
            }
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel, com.xiaopeng.xmart.camera.model.camera.ICameraModel
    public void resetData() {
        this.mLastNormalMode = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        this.mLastMode = CarCameraHelper.getInstance().hasAVM() ? CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
    }

    public void setAVM3DAngle(int value) {
        CameraManager.getInstance().setAvm3603DAngelApi2(value);
    }

    public void setAvmTransparentChassisWorkSt(boolean openTransBody) {
        CameraManager.getInstance().setAvmTransparentChassisWorkSt(openTransBody);
    }
}
