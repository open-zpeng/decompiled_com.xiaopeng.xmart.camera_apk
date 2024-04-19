package com.xiaopeng.xmart.camera.helper;

import android.text.TextUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.bean.CarCamera;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.CarFunction;
/* loaded from: classes.dex */
public abstract class CarCameraHelper {
    public static final int CAMERA_360_DISABLE = 0;
    public static final int CAMERA_360_ENABLE = 1;
    public static final int CAMERA_STATUS_CLOSE = 0;
    public static final int CAMERA_STATUS_ERROR = -1;
    public static final int CAMERA_STATUS_NO_CAMERA_DEVICE = -2;
    public static final int CAMERA_STATUS_OPEN = 1;
    public static final int CAMERA_TOP_NONE = -1;
    public static final int CAMERA_TOP_STATE_DOWN = 1;
    public static final int CAMERA_TOP_STATE_ERROR = 7;
    public static final int CAMERA_TOP_STATE_FALLING_DOWN = 4;
    public static final int CAMERA_TOP_STATE_INVALID = 0;
    public static final int CAMERA_TOP_STATE_RISED_UP = 3;
    public static final int CAMERA_TOP_STATE_RISING_UP = 2;
    public static final int CAMERA_TOP_STATE_ROTATED = 6;
    public static final int CAMERA_TOP_STATE_ROTATING = 5;
    public static final int OCU_STATE_INITALIZE = 2;
    public static final int OCU_STATE_NORMAL = 0;
    public static final int OCU_STATE_SELF_LEARNING = 1;
    public static final int PARKING_TYPE_AUTO_PARK = 1;
    public static final int PARKING_TYPE_IDLE = -1;
    private static final String TAG = "CameraHelper";
    public static final int TOP_CAMERA_CAPTURE = 1;
    public static final int TOP_CAMERA_NONE = 3;
    public static final int TOP_CAMERA_RECORD = 2;
    public static final int TYPE_NAVIGATION_360 = 100;
    public static final int TYPE_NAVIGATION_DVR = 102;
    public static final int TYPE_NAVIGATION_TOP = 101;
    private CarCamera carCamera;
    private boolean mIs3DMode;
    private int mCurParkingType = -1;
    private int mCameraNavigationTab = 100;
    private boolean isDvrFormat = false;
    private boolean isDvrLock = false;
    private boolean mIsCameraRecording = false;
    private boolean isTopTemplateAdjustAngle = false;

    public float getCarSpeed() {
        return 0.0f;
    }

    public long getTopCameraRotateTime() {
        return 0L;
    }

    public double getWHRate() {
        return 1.7777777777777777d;
    }

    public abstract boolean hasAVM();

    public boolean hasDvrCamera() {
        return false;
    }

    public boolean hasTopCamera() {
        return false;
    }

    public abstract void init();

    public boolean isPersistent() {
        return true;
    }

    public abstract boolean isSupportAvmPipTouch();

    public abstract boolean isSupportDvr();

    public abstract boolean isSupportTopCamera();

    public boolean isSupportUpLoadBI() {
        return true;
    }

    public void setHasTopCamera(boolean hasTopCamera) {
    }

    public abstract boolean shouldWaitAvmReady();

    static /* synthetic */ CarCameraHelper access$000() {
        return createMulCarCamera();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        static CarCameraHelper sInstance = CarCameraHelper.access$000();

        private SingletonHolder() {
        }
    }

    public static CarCameraHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static CarCameraHelper createMulCarCamera() {
        char c;
        String xpCduType = Config.getXpCduType();
        int hashCode = xpCduType.hashCode();
        if (hashCode == 2577) {
            if (xpCduType.equals("QB")) {
                c = 6;
            }
            c = 65535;
        } else if (hashCode != 79487) {
            switch (hashCode) {
                case 2560:
                    if (xpCduType.equals("Q1")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 2561:
                    if (xpCduType.equals("Q2")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 2562:
                    if (xpCduType.equals("Q3")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    switch (hashCode) {
                        case 2564:
                            if (xpCduType.equals("Q5")) {
                                c = 7;
                                break;
                            }
                            c = 65535;
                            break;
                        case 2565:
                            if (xpCduType.equals("Q6")) {
                                c = '\t';
                                break;
                            }
                            c = 65535;
                            break;
                        case 2566:
                            if (xpCduType.equals("Q7")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case 2567:
                            if (xpCduType.equals("Q8")) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 2568:
                            if (xpCduType.equals(CarFunction.F30)) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
            }
        } else {
            if (xpCduType.equals("Q3A")) {
                c = 1;
            }
            c = 65535;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return new E28CarCameraHelper();
            default:
                return new D2xCarCameraHelper();
        }
    }

    public CarCamera getCarCamera() {
        if (this.carCamera == null) {
            this.carCamera = new CarCamera();
            CameraLog.d(TAG, "getCarCamera carCamera:" + this.carCamera, false);
        }
        return this.carCamera;
    }

    public int getCameraTypeForFeedbackToMobileApp() {
        int i;
        int cameraNavigationTab = getInstance().getCameraNavigationTab();
        CameraLog.d(TAG, "getCameraTypeForApp selectTab:" + cameraNavigationTab, false);
        if (cameraNavigationTab == 100) {
            int i2 = getInstance().getCarCamera().get360CameraType();
            if (i2 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
                i = 1;
            } else if (i2 != CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) {
                if (i2 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
                    return 5;
                }
                if (i2 == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
                    return 7;
                }
                if (i2 == 4) {
                    i = 2;
                } else if (i2 == 5) {
                    return 4;
                } else {
                    if (i2 == 6) {
                        return 6;
                    }
                    if (i2 == 7) {
                        i = 8;
                    } else if (i2 == CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS) {
                        i = 9;
                    }
                }
            }
            return i;
        } else if (cameraNavigationTab == 101) {
            return 0;
        }
        return 3;
    }

    public void setIs3dMode(boolean is3DMode) {
        this.mIs3DMode = is3DMode;
    }

    public boolean is3DMode() {
        return this.mIs3DMode;
    }

    public boolean isShow3DButton() {
        return !getCarCamera().isTransparentChassis() && this.mIs3DMode;
    }

    public boolean is360Camera(int type) {
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT || type == 4 || type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR || type == 5 || type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT || type == 6 || type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT || type == 7 || type == CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS || type == 8) {
            CameraLog.d(TAG, "is360Camera type:" + type + " true", true);
            return true;
        }
        CameraLog.d(TAG, "is360Camera type:" + type, true);
        return false;
    }

    public void update3DMode(int type) {
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS || type == 8) {
            return;
        }
        setIs3dMode(is3DCameraType(type));
    }

    public boolean is3DCameraType(int type) {
        if (type == 4 || type == 5 || type == 6 || type == 7) {
            CameraLog.d(TAG, "is360Camera type:" + type + " true", true);
            return true;
        }
        return false;
    }

    public int getCameraNavigationTab() {
        return this.mCameraNavigationTab;
    }

    public void setCameraNavigationTab(int cameraNavigationTab) {
        this.mCameraNavigationTab = cameraNavigationTab;
    }

    public int getCurParkingType() {
        return this.mCurParkingType;
    }

    public void setCurParkingType(int parkingType) {
        this.mCurParkingType = parkingType;
    }

    public boolean isDvrFormat() {
        return this.isDvrFormat;
    }

    public void setDvrFormat(boolean dvrFormat) {
        this.isDvrFormat = dvrFormat;
    }

    public void setCameraRecording(boolean isRecording) {
        this.mIsCameraRecording = isRecording;
    }

    public boolean isCameraRecording() {
        return this.isDvrLock || this.mIsCameraRecording;
    }

    public boolean isDvrLock() {
        return this.isDvrLock;
    }

    public void setDvrLock(boolean dvrLock) {
        this.isDvrLock = dvrLock;
    }

    public boolean isTopTemplateAdjustAngle() {
        return this.isTopTemplateAdjustAngle;
    }

    public void setTopTemplateAdjustAngle(boolean topTemplateAdjustAngle) {
        this.isTopTemplateAdjustAngle = topTemplateAdjustAngle;
    }

    public boolean isCurSpeedMoreThan80() {
        float carSpeed = getCarSpeed();
        CameraLog.d(TAG, "Current Speed is " + carSpeed, false);
        return carSpeed >= 80.0f;
    }

    public boolean is3DProject() {
        return !TextUtils.equals(App.getInstance().getPackageName(), "com.xiaopeng.xmart.camera");
    }

    public boolean hasAvmTopRightSoundEffects() {
        return getInstance().isSupportAvmPipTouch() && getInstance().hasAVM() && !getInstance().is3DMode() && !getInstance().getCarCamera().isTransparentChassis();
    }

    public boolean isPortraitScreen() {
        return App.getInstance().getResources().getConfiguration().orientation == 1;
    }
}
