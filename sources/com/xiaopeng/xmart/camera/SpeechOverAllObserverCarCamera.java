package com.xiaopeng.xmart.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.xiaopeng.app.ActivityManagerFactory;
import com.xiaopeng.lib.apirouter.ApiRouter;
import com.xiaopeng.lib.apirouter.server.IServicePublisher;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.speech.overall.SpeechResult;
import com.xiaopeng.view.WindowManagerFactory;
import com.xiaopeng.xmart.camera.bean.ChangeValue;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.carcontroller.IXpuController;
import com.xiaopeng.xmart.camera.define.BIConfig;
import com.xiaopeng.xmart.camera.define.CameraConstant;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CameraStorageCheckHelper;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.SpeechOperationHelper;
import com.xiaopeng.xmart.camera.speech.PageDirectParams;
import com.xiaopeng.xmart.camera.utils.Calculator;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.CarFunction;
import com.xiaopeng.xmart.camera.utils.JumpGalleryUtil;
import com.xiaopeng.xmart.camera.utils.SystemUtils;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class SpeechOverAllObserverCarCamera implements IServicePublisher {
    private static final int CAMERA_3D_NOT_SUPPORT = 0;
    private static final int CAMERA_3D_SUPPORT = 1;
    private static final int CAMERA_TRANSPARENT_CHASSIS_NOT_SUPPORT = 0;
    private static final int CAMERA_TRANSPARENT_CHASSIS_SUPPORT = 1;
    private static final String PARAMS_KEY = "isTTS";
    private static final String TAG = "SpeechOverAllObserverCarCamera";
    private final Gson gson = new Gson();
    public IXpuController mIXpuController;

    public SpeechOverAllObserverCarCamera() {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$SpeechOverAllObserverCarCamera$V4xdYX_rN6Prx3qhIIdFqRQgkXM
            @Override // java.lang.Runnable
            public final void run() {
                SpeechOverAllObserverCarCamera.this.lambda$new$0$SpeechOverAllObserverCarCamera();
            }
        });
    }

    public /* synthetic */ void lambda$new$0$SpeechOverAllObserverCarCamera() {
        if (CarClientWrapper.getInstance().isCarServiceConnected()) {
            CameraLog.d(TAG, "Car service connected", false);
            initController();
        }
    }

    public void initController() {
        CameraLog.i(TAG, "  initController ", false);
        this.mIXpuController = (IXpuController) CarClientWrapper.getInstance().getController(CarClientWrapper.XP_XPU_SERVICE);
    }

    public void onEvent(String event, String data) {
        CameraLog.i(TAG, "onEvent camera 消息接收 event== " + event + ",data:" + data, false);
        if (TextUtils.equals(CameraConstant.SpeechEvent.OVERALL_ON, event)) {
            SpeechOperationHelper.getInstance().open360CameraFromSpeech(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR);
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.REAR_TAKE, event)) {
            SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.REAR_RECORD, event)) {
            SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.FRONT_TAKE, event)) {
            SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.FRONT_RECORD, event)) {
            SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.LEFT_TAKE, event)) {
            SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.LEFT_RECORD, event)) {
            SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.RIGHT_TAKE, event)) {
            SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.RIGHT_RECORD, event)) {
            SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.LEFT_ON, event)) {
            SpeechOperationHelper.getInstance().open360CameraFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.RIGHT_ON, event)) {
            SpeechOperationHelper.getInstance().open360CameraFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT));
        } else if (TextUtils.equals(CameraConstant.SpeechEvent.REAR_ON, event)) {
        } else {
            if (TextUtils.equals(CameraConstant.SpeechEvent.REAR_ON_NEW, event)) {
                SpeechOperationHelper.getInstance().open360CameraFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR));
            } else if (TextUtils.equals(CameraConstant.SpeechEvent.FRONT_ON, event)) {
                SpeechOperationHelper.getInstance().open360CameraFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT));
            } else if (TextUtils.equals(CameraConstant.SpeechEvent.REAR_OFF, event)) {
            } else {
                if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_OFF, event)) {
                    SpeechOperationHelper.getInstance().closeTopCameraFromSpeech();
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_ON, event)) {
                    SpeechOperationHelper.getInstance().openTopCameraFromSpeech();
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_TAKE, event)) {
                    SpeechOperationHelper.getInstance().takePicFromSpeech(101);
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_ROTATE_LEFT, event)) {
                    boolean isTTS = isTTS(data);
                    ChangeValue fromJson = ChangeValue.fromJson(data);
                    if (isTTS) {
                        speechRotateTopCamera(fromJson.getValue(), fromJson.isScale(), true);
                    }
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_ROTATE_RIGHT, event)) {
                    boolean isTTS2 = isTTS(data);
                    ChangeValue fromJson2 = ChangeValue.fromJson(data);
                    if (isTTS2) {
                        speechRotateTopCamera(fromJson2.getValue(), fromJson2.isScale(), false);
                    }
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_ROTATE_FRONT, event)) {
                    if (isTTS(data)) {
                        speechRotateTopCamera(0.0f, false, false);
                    }
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TOP_ROTATE_REAR, event)) {
                    boolean isTTS3 = isTTS(data);
                    CameraLog.d(TAG, "onTopRotateRear(), angle: " + Calculator.calculateRotateAngle() + ",targetAngle:175.0", false);
                    if (isTTS3) {
                        speechRotateTopCamera(175.0f, false, false);
                    }
                } else if (TextUtils.equals(CameraConstant.SpeechEvent.TRANSPARENT_CHASSIS, event)) {
                } else {
                    if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_ON, event)) {
                        CameraLog.d(TAG, "onCameraThreeDOn,3d:" + CarCameraHelper.getInstance().is3DMode(), false);
                        int lastNormal360CameraType = CarCameraHelper.getInstance().getCarCamera().getLastNormal360CameraType();
                        if (!CarCameraHelper.getInstance().is3DMode()) {
                            lastNormal360CameraType = getSwitch3DDisplayMode();
                        }
                        SpeechOperationHelper.getInstance().open360CameraFromSpeech(lastNormal360CameraType);
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_OFF, event)) {
                        CameraLog.d(TAG, "onCameraThreeDOff,3d:" + CarCameraHelper.getInstance().is3DMode(), false);
                        if (CarCameraHelper.getInstance().is3DMode()) {
                            SpeechOperationHelper.getInstance().open360CameraFromSpeech(getSwitch3DDisplayMode());
                        }
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.TRANSPARENT_CHASSIS_ON, event)) {
                        CameraLog.d(TAG, "onCameraTransparentChassisOn,", false);
                        SpeechOperationHelper.getInstance().open360CameraFromSpeech(CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS);
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.TRANSPARENT_CHASSIS_OFF, event)) {
                        boolean isTransparentChassis = CarCameraHelper.getInstance().getCarCamera().isTransparentChassis();
                        CameraLog.d(TAG, "onCameraTransparentChassisOff,isTransparent:" + isTransparentChassis, false);
                        if (isTransparentChassis) {
                            SpeechOperationHelper.getInstance().open360CameraFromSpeech(CarCameraHelper.getInstance().getCarCamera().getLastNormal360CameraType());
                        }
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.PHOTOALBUM_OPEN, event)) {
                        JumpGalleryUtil.gotoGalleryDetail(null, CarCameraHelper.getInstance().hasAVM() ? CameraDefine.TAB_CAMERA_360 : CameraDefine.TAB_CAMERA_BACK);
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.Record_End, event)) {
                        CameraLog.d(TAG, "onCameraRecordEnd", false);
                        SpeechOperationHelper.getInstance().recordEndFromSpeech();
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Front_Take, event)) {
                        CameraLog.d(TAG, "onCameraThreeDFrontTake", false);
                        SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(4));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Front_Record, event)) {
                        CameraLog.d(TAG, "onCameraThreeDFrontRecord", false);
                        SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(4));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Rear_Take, event)) {
                        CameraLog.d(TAG, "onCameraThreeDRearTake", false);
                        SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(5));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Rear_Record, event)) {
                        CameraLog.d(TAG, "onCameraThreeDRearRecord", false);
                        SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(5));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Left_Take, event)) {
                        CameraLog.d(TAG, "onCameraThreeDLeftTake", false);
                        SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(6));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Left_Record, event)) {
                        CameraLog.d(TAG, "onCameraThreeDLeftRecord", false);
                        SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(6));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Right_Take, event)) {
                        CameraLog.d(TAG, "onCameraThreeDRightTake", false);
                        SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(7));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.THREE_D_Right_Record, event)) {
                        CameraLog.d(TAG, "onCameraThreeDRightRecord", false);
                        SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(6));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.TRANSPARENT_CHASSIS_TAKE, event)) {
                        CameraLog.d(TAG, "onCameraTansparentChassisTake", false);
                        SpeechOperationHelper.getInstance().takePicFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS));
                    } else if (TextUtils.equals(CameraConstant.SpeechEvent.TRANSPARENT_CHASSIS_RECORD, event)) {
                        CameraLog.d(TAG, "onCameraTansparentChassisRecord", false);
                        SpeechOperationHelper.getInstance().recordFromSpeech(getCameraType(CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS));
                    }
                }
            }
        }
    }

    public void onQuery(String event, String data, String callback) {
        CameraLog.i(TAG, "onQuery 消息接收event=" + event + ",data:" + data + ",callback:" + callback, false);
        if (TextUtils.equals(CameraConstant.SpeechQueryModel.TOP_STATUS, event)) {
            aprRouter(event, callback, Integer.valueOf(getSupportTopStatus()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.PANORAMIC_STATUS, event)) {
            aprRouter(event, callback, Integer.valueOf(getSupportPanoramicStatus()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.REAR_STATUS, event)) {
            aprRouter(event, callback, Integer.valueOf(getSupportRearStatus()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.BUSINESS_STATUS, event)) {
            aprRouter(event, callback, 0);
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.THREE_D_SUPPORT, event)) {
            aprRouter(event, callback, Integer.valueOf(getCameraThreeDSupport()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.TRANSPARENT_CHASSIS_SUPPORT, event)) {
            aprRouter(event, callback, Integer.valueOf(getCameraTransparentChassisSupport()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.IS_RECORDING, event)) {
            aprRouter(event, callback, Boolean.valueOf(isCameraRecording()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.GUI_OPEN, event)) {
            aprRouter(event, callback, Boolean.valueOf(handleGuiOpen(data)));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.LOW_POWER, event)) {
            aprRouter(event, callback, Integer.valueOf(isLowPower()));
        } else if (TextUtils.equals(CameraConstant.SpeechQueryModel.Display_Mode, event)) {
            aprRouter(event, callback, Integer.valueOf(getCameraDisplayMode()));
        }
    }

    private boolean isTTS(String data) {
        boolean z = true;
        try {
            z = new JSONObject(data).optBoolean(PARAMS_KEY, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CameraLog.i(TAG, "isTTS:" + z, false);
        return z;
    }

    private void speechRotateTopCamera(float value, boolean isScale, boolean rotateLeft) {
        double d;
        CameraLog.i(TAG, "speechRotateTopCamera().", false);
        if (!isScale) {
            d = rotateLeft ? value - 180.0d : value;
        } else if (rotateLeft) {
            d = Calculator.calculateRotateAngle() - value;
        } else {
            d = Calculator.calculateRotateAngle() + value;
        }
        float f = (float) (d / 180.0d);
        CameraLog.d(TAG, "onTopRotateLeft(), viewRotateAngle: " + d + " rate: " + f, false);
        SpeechOperationHelper.getInstance().rotateTopCameraFromSpeech(f);
    }

    private int getSwitch3DDisplayMode() {
        return CarCameraHelper.getInstance().getCarCamera().get360CameraSwitch3DDisplayMode(CarCameraHelper.getInstance().getCarCamera().getLastNormal360CameraType());
    }

    private int getCameraType(int type) {
        return (CarCameraHelper.getInstance().is360Camera(type) && CarCameraHelper.getInstance().is3DMode()) ? CarCameraHelper.getInstance().getCarCamera().get360CameraSwitch3DDisplayMode(type) : type;
    }

    private void aprRouter(String event, String callback, Object result) {
        if (TextUtils.isEmpty(callback)) {
            return;
        }
        try {
            ApiRouter.route(Uri.parse(callback).buildUpon().appendQueryParameter(BIConfig.PROPERTY.DATA_RESULT, new SpeechResult(event, result).toString()).build());
        } catch (RemoteException e) {
            CameraLog.i(TAG, "remote exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getSupportTopStatus() {
        int i;
        if (!CarCameraHelper.getInstance().hasTopCamera()) {
            i = 2;
        } else if (CarCameraHelper.getInstance().getCurParkingType() != -1) {
            i = 3;
        } else if (CarCameraHelper.getInstance().isCurSpeedMoreThan80()) {
            i = 4;
        } else if (CarCameraHelper.getInstance().getCarCamera().isUp()) {
            i = 5;
        } else if (isNotSupportOther()) {
            i = 6;
        } else {
            i = CameraStorageCheckHelper.getInstance().checkAvailableStorage() ? 7 : 1;
        }
        CameraLog.d(TAG, "getSupportTopStatus,status:" + i, false);
        return i;
    }

    public int getSupportPanoramicStatus() {
        int i;
        CameraLog.d(TAG, "getSupportPanoramicStatus:");
        if (!CarCameraHelper.getInstance().hasAVM()) {
            i = 2;
        } else if (CarCameraHelper.getInstance().getCurParkingType() != -1) {
            i = 3;
        } else if (isNotSupportOther()) {
            i = 6;
        } else {
            i = CameraStorageCheckHelper.getInstance().checkAvailableStorage() ? 7 : 1;
        }
        CameraLog.d(TAG, "getSupportPanoramicStatus,status:" + i, false);
        return i;
    }

    public int getSupportRearStatus() {
        CameraLog.d(TAG, "getSupportRearStatus,status:1", false);
        if (CarCameraHelper.getInstance().getCurParkingType() != -1) {
            return 3;
        }
        if (isNotSupportOther()) {
            return 6;
        }
        return CameraStorageCheckHelper.getInstance().checkAvailableStorage() ? 7 : 1;
    }

    private boolean isNotSupportOther() {
        return CarCameraHelper.getInstance().isDvrFormat() || CarCameraHelper.getInstance().isDvrLock() || CarCameraHelper.getInstance().isTopTemplateAdjustAngle();
    }

    public int getCameraThreeDSupport() {
        CameraLog.d(TAG, "getCameraThreeDSupport, avm:" + CarCameraHelper.getInstance().hasAVM(), false);
        return (CarCameraHelper.getInstance().hasAVM() && CarCameraHelper.getInstance().getCurParkingType() == -1) ? 1 : 0;
    }

    public int getCameraTransparentChassisSupport() {
        CameraLog.d(TAG, "getCameraTransparentChassisSupport, avm:" + CarCameraHelper.getInstance().hasAVM(), false);
        return (CarCameraHelper.getInstance().hasAVM() && CarCameraHelper.getInstance().getCurParkingType() == -1) ? 1 : 0;
    }

    public boolean isCameraRecording() {
        boolean isCameraRecording = CarCameraHelper.getInstance().isCameraRecording();
        CameraLog.i(TAG, "isCameraRecording:" + isCameraRecording, false);
        return isCameraRecording;
    }

    public int isLowPower() {
        int nEDCStatus = this.mIXpuController.getNEDCStatus();
        CameraLog.i(TAG, "isloewpower:" + nEDCStatus, false);
        return nEDCStatus;
    }

    public int getCameraDisplayMode() {
        return CarCameraHelper.getInstance().getCameraTypeForFeedbackToMobileApp();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean handleGuiOpen(String data) {
        if (CarFunction.isSupportPageDirectMultiParams()) {
            CameraLog.i(TAG, "isSupportPageDirectMultiParams: " + data, false);
            PageDirectParams pageDirectParams = (PageDirectParams) this.gson.fromJson(data, (Class<Object>) PageDirectParams.class);
            final int i = -1;
            i = -1;
            if (pageDirectParams.getDisplay_location() != null && !pageDirectParams.getDisplay_location().isEmpty()) {
                i = Integer.parseInt(pageDirectParams.getDisplay_location());
            } else if (pageDirectParams.getSound_location() != null && !pageDirectParams.getSound_location().isEmpty()) {
                i = "2".equals(pageDirectParams.getSound_location());
            }
            final App app = App.getInstance();
            if (pageDirectParams.getPageUrl() == null || pageDirectParams.getPageUrl().isEmpty()) {
                WindowManagerFactory create = WindowManagerFactory.create(app);
                if (!(create.getTopActivity(1, 0).contains(new StringBuilder().append(app.getPackageName()).append(MqttTopic.TOPIC_LEVEL_SEPARATOR).toString()) || create.getTopActivity(1, 1).contains(new StringBuilder().append(app.getPackageName()).append(MqttTopic.TOPIC_LEVEL_SEPARATOR).toString()))) {
                    CameraLog.i(TAG, "handleGuiOpen supportMultiParams: direct start at screen:" + i, false);
                    Intent launchIntentForPackage = app.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                    if (i >= 0) {
                        ActivityManagerFactory.create(app).startActivity(app, launchIntentForPackage, i);
                    } else {
                        app.startActivity(launchIntentForPackage);
                    }
                } else if (i >= 0) {
                    LogUtils.d("getGuiPageOpenState supportMultiParams, transfer to screen: " + i);
                    ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$SpeechOverAllObserverCarCamera$Tc1Qbg0ncYznhyVJ63XJH4kCAs8
                        @Override // java.lang.Runnable
                        public final void run() {
                            SystemUtils.slideCurrentAppToScreen(app, i);
                        }
                    }, 1L);
                } else {
                    CameraLog.i(TAG, "handleGuiOpen supportMultiParams: no need transfer " + data, false);
                }
                return true;
            }
        }
        return false;
    }
}
