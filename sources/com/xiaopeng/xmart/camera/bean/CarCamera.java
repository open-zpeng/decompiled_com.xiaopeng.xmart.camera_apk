package com.xiaopeng.xmart.camera.bean;

import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IAvmController;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
/* loaded from: classes.dex */
public class CarCamera {
    private static final String TAG = "CarCamera";
    private int cameraTop;
    private boolean isFallingTopCamera;
    private float mAngle;
    private boolean mHasTopCamera;
    public IAvmController mIAvmController;
    private boolean mIsUp;
    private int mCameraType = CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS;
    private int mLastNormalCameraType = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
    private Camera360 cameraStatus = new Camera360();

    public int get360CameraType() {
        return this.mCameraType;
    }

    public void set360CameraType(int cameraType) {
        this.mCameraType = cameraType;
    }

    public void reset360CameraType() {
        this.mCameraType = CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS;
        this.mLastNormalCameraType = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
    }

    public int getLastNormal360CameraType() {
        return this.mLastNormalCameraType;
    }

    public void setLastNormal360CameraType(int mLastCameraType) {
        this.mLastNormalCameraType = mLastCameraType;
    }

    public void setHasTopCamera(boolean hasTopCamera) {
        this.mHasTopCamera = hasTopCamera;
    }

    public CarCamera() {
        getCamera360();
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.bean.-$$Lambda$CarCamera$u40PQzXqfaUNG9CoNAysltz35SQ
            @Override // java.lang.Runnable
            public final void run() {
                CarCamera.this.lambda$new$0$CarCamera();
            }
        });
    }

    public /* synthetic */ void lambda$new$0$CarCamera() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
        }
    }

    private void initController() {
        CameraLog.i(TAG, "  initController ", false);
        this.mIAvmController = (IAvmController) CarClientWrapper.getInstance().getController(CarClientWrapper.XP_AVM_SERVICE);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0050, code lost:
        if (r4 == 90) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isUp(boolean r8) {
        /*
            r7 = this;
            int r0 = r7.getTopCameraState()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "isUp:"
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 0
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r3 = " state:"
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "CarCamera"
            com.xiaopeng.xmart.camera.utils.CameraLog.i(r3, r1)
            r1 = 1
            switch(r0) {
                case -1: goto L30;
                case 0: goto L2b;
                case 1: goto L2d;
                case 2: goto L52;
                case 3: goto L52;
                case 4: goto L2b;
                case 5: goto L52;
                case 6: goto L52;
                default: goto L2b;
            }
        L2b:
            r1 = r2
            goto L52
        L2d:
            r7.isFallingTopCamera = r2
            goto L2b
        L30:
            int r4 = r7.getTopCameraPos()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "TopCamera is up, position: "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r4)
            java.lang.String r5 = r5.toString()
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r3, r5, r2)
            r5 = -1
            if (r4 != r5) goto L4e
            return r2
        L4e:
            r5 = 90
            if (r4 != r5) goto L2b
        L52:
            if (r8 == 0) goto L74
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r4 = "TopCamera is up : "
            java.lang.StringBuilder r8 = r8.append(r4)
            java.lang.StringBuilder r8 = r8.append(r1)
            java.lang.String r4 = "  state: "
            java.lang.StringBuilder r8 = r8.append(r4)
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.String r8 = r8.toString()
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r3, r8, r2)
        L74:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xmart.camera.bean.CarCamera.isUp(boolean):boolean");
    }

    public boolean isFallingTopCamera() {
        return this.isFallingTopCamera;
    }

    public void setFallingTopCamera(boolean fallingTopCamera) {
        this.isFallingTopCamera = fallingTopCamera;
    }

    public boolean isUp() {
        boolean isUp = isUp(true);
        CameraLog.i(TAG, "isUp:" + isUp, false);
        return isUp;
    }

    public void setUp(boolean up) {
        if (up) {
            return;
        }
        setAngle(0.0f);
    }

    public boolean isOcuLearning(int moveState) {
        boolean z = true;
        if (moveState != 1 && moveState != 2) {
            z = false;
        }
        CameraLog.d(TAG, "isOcuLearning(): " + z, false);
        return z;
    }

    public int getTopCameraState() {
        int i = -1;
        try {
            IAvmController iAvmController = this.mIAvmController;
            if (iAvmController == null) {
                return -1;
            }
            i = iAvmController.getRoofCameraState();
            CameraLog.d(TAG, "getTopCameraState:" + i);
            return i;
        } catch (Exception e) {
            CameraLog.e(TAG, "getRoofCameraState:" + e.getMessage(), false);
            return i;
        }
    }

    public boolean isReadyforMagic() {
        int topCameraState = getTopCameraState();
        return topCameraState == 3 || topCameraState == 5 || topCameraState == 6;
    }

    public int getTopCameraPos() {
        try {
            IAvmController iAvmController = this.mIAvmController;
            if (iAvmController == null) {
                return -1;
            }
            return iAvmController.getRoofCameraPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public float getAngle() {
        return this.mAngle;
    }

    public void setAngle(float angle) {
        this.mAngle = angle;
    }

    public int getCameraTop() {
        if (!this.mHasTopCamera) {
            this.cameraTop = -2;
        } else if (isUp()) {
            this.cameraTop = 1;
        } else {
            this.cameraTop = 0;
        }
        return this.cameraTop;
    }

    public Camera360 getCamera360() {
        if (!CarCameraHelper.getInstance().hasAVM()) {
            this.cameraStatus.setCameraFront(0);
            this.cameraStatus.setCameraBack(1);
            this.cameraStatus.setCameraLeft(0);
            this.cameraStatus.setCameraRight(0);
        } else {
            this.cameraStatus.setCameraFront(1);
            this.cameraStatus.setCameraBack(1);
            this.cameraStatus.setCameraLeft(1);
            this.cameraStatus.setCameraRight(1);
        }
        return this.cameraStatus;
    }

    public boolean isTransparentChassis() {
        CameraLog.d(TAG, "isTransparentChassis cameraType:" + this.mCameraType);
        return CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS == this.mCameraType;
    }

    public int get360CameraSwitch3DDisplayMode(int curDisplayType) {
        int i = CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS;
        if (curDisplayType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
            i = 4;
        } else if (curDisplayType == 4) {
            i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT;
        } else if (curDisplayType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) {
            i = 5;
        } else if (curDisplayType == 5) {
            i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
        } else if (curDisplayType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
            i = 6;
        } else if (curDisplayType == 6) {
            i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT;
        } else if (curDisplayType == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
            i = 7;
        } else if (curDisplayType == 7) {
            i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT;
        }
        CameraLog.d(TAG, "get360CameraSwitch3DDisplayMode curDisplayType:" + curDisplayType + ",direction:" + i, false);
        return i;
    }
}
