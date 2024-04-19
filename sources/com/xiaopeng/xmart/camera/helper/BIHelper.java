package com.xiaopeng.xmart.camera.helper;

import android.content.Context;
import android.text.TextUtils;
import com.xiaopeng.lib.bughunter.BugHunter;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IMoleEventBuilder;
import com.xiaopeng.lib.utils.config.RemoteControlConfig;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.carmanager.Component;
import com.xiaopeng.xmart.camera.define.BIConfig;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camerabase.R;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class BIHelper {
    private static final String TAG = "BIHelper";

    public static BIHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public void init(Context context) {
        BugHunter.init(context);
    }

    public void uploadCameraBI(int scene, int eventId, Map<String, Number> params) {
        if (CarCameraHelper.getInstance().isSupportUpLoadBI()) {
            CameraLog.d(TAG, "uploadCameraBI, scene : " + scene + ", eventId: " + eventId, false);
            String str = "B004";
            String str2 = "P00003";
            switch (scene) {
                case 1:
                    if (eventId == 1) {
                        str = BIConfig.ButtonID.SPEECH_OPEN_TOP_CAMERA_BUTTON_ID;
                    } else if (eventId != 2) {
                        if (eventId == 3) {
                            str = BIConfig.ButtonID.SPEECH_TOP_CAMERA_RECORD_BUTTON_ID;
                        }
                        str = "";
                    } else {
                        str = BIConfig.ButtonID.SPEECH_TOP_CAMERA_PHOTO_BUTTON_ID;
                    }
                    str2 = "P00002";
                    break;
                case 2:
                    if (eventId == 4) {
                        str = "B006";
                        break;
                    } else if (eventId == 5) {
                        str = "B007";
                        break;
                    } else {
                        if (eventId == 6) {
                            str = "B008";
                            break;
                        }
                        str = "";
                        break;
                    }
                case 3:
                    if (eventId == 27) {
                        str2 = "P00002";
                        str = "B005";
                        break;
                    }
                    str = "";
                    str2 = "P00002";
                    break;
                case 4:
                    str = eventId != 11 ? "" : "B001";
                    str2 = BIConfig.PageId.LAUNCH_CAMERA_PAGE_ID;
                    break;
                case 5:
                    if (eventId == 12) {
                        str2 = "P00002";
                        str = "B001";
                        break;
                    } else {
                        if (eventId == 13) {
                            str2 = "P00002";
                        } else if (eventId != 24) {
                            if (eventId == 30) {
                                str2 = BIConfig.PageId.PANORAMA_VIEW_OPERATION_PAGE_ID;
                            }
                            str = "";
                            str2 = "P00002";
                            break;
                        } else {
                            str = "B003";
                            str2 = "P00002";
                        }
                        str = "B002";
                        break;
                    }
                case 6:
                    if (eventId != 28) {
                        switch (eventId) {
                            case 14:
                                break;
                            case 15:
                                str = "B003";
                                break;
                            case 16:
                                str = "B002";
                                break;
                            case 17:
                                str = "B001";
                                break;
                            default:
                                str = "";
                                break;
                        }
                    }
                    str = "B005";
                    break;
                case 7:
                    if (eventId == 25) {
                        str = "B005";
                    } else if (eventId == 26) {
                        str = "B006";
                    } else if (eventId != 29) {
                        switch (eventId) {
                            case 7:
                                str = BIConfig.ButtonID.TEMPLATE_SHOOT_SELECT_PHOTO_BUTTON_ID;
                                break;
                            case 8:
                                str = BIConfig.ButtonID.TEMPLATE_SHOOT_SELECT_RECORD_BUTTON_ID;
                                break;
                            case 9:
                                str = "B008";
                                break;
                            case 10:
                                str = "B007";
                                break;
                            default:
                                switch (eventId) {
                                    case 18:
                                        str = "B003";
                                        break;
                                    case 19:
                                        str = "B001";
                                        break;
                                    case 20:
                                        str = "B002";
                                        break;
                                    default:
                                        str = "";
                                        break;
                                }
                        }
                    }
                    str2 = BIConfig.PageId.TOP_OPERATION_PAGE_ID;
                    break;
                case 8:
                    switch (eventId) {
                        case 21:
                            str = "B001";
                            break;
                        case 22:
                            str = "B005";
                            break;
                        case 23:
                            str = "B006";
                            break;
                        default:
                            str = "";
                            break;
                    }
                    str2 = BIConfig.PageId.GALLERY_OPERATION_PAGE_ID;
                    break;
                case 9:
                    if (eventId == 31) {
                        str2 = BIConfig.PageId.AVM_SWITCH_DISPLAY_MODE;
                        str = "B001";
                        break;
                    } else if (eventId == 32) {
                        str2 = BIConfig.PageId.AVM_SWITCH_DISPLAY_MODE;
                        str = "B002";
                        break;
                    } else {
                        str = "";
                        str2 = BIConfig.PageId.AVM_SWITCH_DISPLAY_MODE;
                        break;
                    }
                default:
                    str = "";
                    str2 = str;
                    break;
            }
            if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str)) {
                CameraLog.d(TAG, "uploadTopCameraBI,  pageId is null ");
            } else {
                sendMqttDataLog(str2, str, params);
            }
        }
    }

    private void sendMqttDataLog(final String pageId, final String buttonId, final Map<String, Number> params) {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.helper.-$$Lambda$BIHelper$Q6SrwOQyFhm5TnvU-AjwTHI7K8o
            @Override // java.lang.Runnable
            public final void run() {
                BIHelper.lambda$sendMqttDataLog$0(pageId, buttonId, params);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendMqttDataLog$0(String str, String str2, Map map) {
        IDataLog dataLogService = Component.getInstance().getDataLogService();
        IMoleEventBuilder buttonId = dataLogService.buildMoleEvent().setEvent(BIConfig.EVENT_CAMERA).setModule(BIConfig.MODULE_CAMERA).setPageId(str).setButtonId(str2);
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                buttonId.setProperty((String) entry.getKey(), (Number) entry.getValue());
                sb.append((String) entry.getKey()).append(":").append(entry.getValue()).append(";");
            }
        }
        dataLogService.sendStatData(buttonId.build());
        CameraLog.d(TAG, "sendMqttDataLog, pageId : " + str + ", buttonId: " + str2 + ", params: " + sb.toString(), false);
    }

    public void uploadTransparentSwitchBI(boolean open) {
        HashMap hashMap = new HashMap();
        hashMap.put(BIConfig.PROPERTY.DATA_RESULT, Integer.valueOf(!open ? 1 : 0));
        uploadCameraBI(5, 24, hashMap);
    }

    public void uploadDisplayModeSwitchBI(int displayMode) {
        HashMap hashMap = new HashMap();
        hashMap.put("type", Integer.valueOf(getDisplayModeType(displayMode)));
        uploadCameraBI(9, 31, hashMap);
    }

    private void uploadRecordBI(int avmType) {
        HashMap hashMap = new HashMap();
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, 0);
        hashMap.put(BIConfig.PROPERTY.DATA_RESULT, 0);
        hashMap.put(BIConfig.PROPERTY.DATA_COUNT, 0);
        hashMap.put("type", Integer.valueOf(transAvmCameraType(avmType)));
        uploadCameraBI(5, 13, hashMap);
    }

    public void uploadDvrRecordBi() {
        HashMap hashMap = new HashMap();
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, 0);
        hashMap.put(BIConfig.PROPERTY.DATA_RESULT, 0);
        hashMap.put(BIConfig.PROPERTY.DATA_COUNT, 0);
        uploadCameraBI(6, 17, hashMap);
    }

    public void uploadRecordBI(boolean isTop, int avmType) {
        if (isTop) {
            HashMap hashMap = new HashMap();
            hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, 0);
            hashMap.put(BIConfig.PROPERTY.DATA_RESULT, 0);
            hashMap.put(BIConfig.PROPERTY.DATA_COUNT, 0);
            uploadCameraBI(7, 20, hashMap);
            return;
        }
        uploadRecordBI(avmType);
    }

    private void uploadCaptureBI(int avmType, int source) {
        HashMap hashMap = new HashMap();
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, Integer.valueOf(source));
        hashMap.put("type", Integer.valueOf(transAvmCameraType(avmType)));
        uploadCameraBI(5, 12, hashMap);
    }

    public void uploadCaptureBI(boolean isDvr, boolean isTop, int type, int source) {
        HashMap hashMap = new HashMap();
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, Integer.valueOf(source));
        if (isDvr) {
            uploadCameraBI(6, 16, hashMap);
        } else if (isTop) {
            uploadCameraBI(7, 19, hashMap);
        } else {
            uploadCaptureBI(type, source);
        }
    }

    public void uploadCaptureBI(boolean isTop, int type, int source) {
        if (isTop) {
            HashMap hashMap = new HashMap();
            hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, Integer.valueOf(source));
            uploadCameraBI(7, 19, hashMap);
            return;
        }
        uploadCaptureBI(type, source);
    }

    public void uploadTopSwitch(boolean open, int source) {
        HashMap hashMap = new HashMap();
        if (open) {
            hashMap.put(BIConfig.PROPERTY.DATA_RESULT, 0);
        } else {
            hashMap.put(BIConfig.PROPERTY.DATA_RESULT, 1);
        }
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, Integer.valueOf(source));
        uploadCameraBI(7, 18, hashMap);
    }

    public void uploadRecordBI(boolean isDvr, boolean isTop, boolean start, int time, int source, int avmType) {
        HashMap hashMap = new HashMap();
        if (start) {
            time = 0;
        }
        int i = !start ? 1 : 0;
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, Integer.valueOf(source));
        hashMap.put(BIConfig.PROPERTY.DATA_RESULT, Integer.valueOf(i));
        hashMap.put(BIConfig.PROPERTY.DATA_COUNT, Integer.valueOf(time));
        if (isDvr) {
            uploadCameraBI(6, 17, hashMap);
        } else if (isTop) {
            uploadCameraBI(7, 20, hashMap);
        } else {
            hashMap.put("type", Integer.valueOf(transAvmCameraType(avmType)));
            uploadCameraBI(5, 13, hashMap);
        }
    }

    public void uploadRecordBI(boolean isTop, boolean start, int time, int source, int avmType) {
        uploadRecordBI(false, isTop, start, time, source, avmType);
    }

    public void uploadClickPreviewTemplate(String templateName, boolean photo) {
        int i;
        CameraLog.i(TAG, "uploadClickPreviewTemplate,templateName: " + templateName + ",photo:" + photo);
        if (TextUtils.isEmpty(templateName) || TextUtils.equals(templateName, App.getInstance().getString(R.string.shot_pic_filter_white_origin))) {
            return;
        }
        HashMap hashMap = new HashMap();
        if (photo) {
            i = 9;
            hashMap.put("type", Integer.valueOf(transTakePicTemplateType(templateName)));
        } else {
            i = 10;
            hashMap.put("type", Integer.valueOf(transRecordTemplateType(templateName)));
        }
        uploadCameraBI(7, i, hashMap);
    }

    public void uploadClickTemplateItem(String templateName, boolean photo) {
        int i;
        CameraLog.i(TAG, "uploadClickTemplateItem,templateName: " + templateName);
        if (TextUtils.isEmpty(templateName)) {
            templateName = App.getInstance().getString(R.string.shot_pic_filter_white_origin);
        }
        HashMap hashMap = new HashMap();
        if (photo) {
            hashMap.put("type", Integer.valueOf(transTakePicTemplateType(templateName)));
            i = 7;
        } else {
            i = 8;
            hashMap.put("type", Integer.valueOf(transRecordTemplateType(templateName)));
        }
        uploadCameraBI(7, i, hashMap);
    }

    public void uploadClickTemplateEntrance(boolean record) {
        CameraLog.i(TAG, "uploadClickTemplateEntrance,record: " + record);
        uploadCameraBI(7, record ? 26 : 25, new HashMap());
    }

    public void uploadShockTakePic() {
        CameraLog.i(TAG, "uploadShockTakePic");
        uploadCameraBI(3, 27, new HashMap());
    }

    public void uploadStartFrom(String action) {
        int i = 0;
        CameraLog.e(TAG, "uploadStartBI, action is : " + action, false);
        HashMap hashMap = new HashMap();
        if (action != null) {
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1173447682:
                    if (action.equals("android.intent.action.MAIN")) {
                        c = 0;
                        break;
                    }
                    break;
                case -152431137:
                    if (action.equals(CameraDefine.Actions.ACTION_OPEN_TOP_CAMERA)) {
                        c = 1;
                        break;
                    }
                    break;
                case 43912679:
                    if (action.equals(CameraDefine.Actions.ACTION_OPEN_360_CAMERA)) {
                        c = 2;
                        break;
                    }
                    break;
                case 78628423:
                    if (action.equals("com.xiaopeng.speech.topcamera.rotate")) {
                        c = 3;
                        break;
                    }
                    break;
                case 286779030:
                    if (action.equals(CameraDefine.Actions.ACTION_RECORD)) {
                        c = 4;
                        break;
                    }
                    break;
                case 310865109:
                    if (action.equals(RemoteControlConfig.REMOTE_ACTION_TOP_CAR_CAMERA_CHANGE_HEIGHT)) {
                        c = 5;
                        break;
                    }
                    break;
                case 1473222271:
                    if (action.equals(BIConfig.ACTION_OPEN_CAMERA_GESTURE_SHOOT)) {
                        c = 6;
                        break;
                    }
                    break;
                case 1968182304:
                    if (action.equals(CameraDefine.Actions.ACTION_TAKE_PICTURE)) {
                        c = 7;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 7:
                    i = 1;
                    break;
            }
        }
        hashMap.put(BIConfig.PROPERTY.DATA_SOURCE, Integer.valueOf(i));
        uploadCameraBI(4, 11, hashMap);
    }

    public void uploadAVM3DSwitchBI() {
        CameraLog.i(TAG, "uploadAVM3DSwitchBI");
        uploadCameraBI(9, 32, new HashMap());
    }

    private int transAvmCameraType(int type) {
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
            return 0;
        }
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
            return 1;
        }
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) {
            return 2;
        }
        if (type == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
            return 3;
        }
        if (type == 4) {
            return 4;
        }
        if (type == 7) {
            return 5;
        }
        if (type == 5) {
            return 6;
        }
        if (type == 6 || type == CameraDefine.CAMERA_PANO.DIRECTION.CAMERA_TYPE_TRANSPARENT_CHASSIS) {
            return 7;
        }
        return type;
    }

    private int transTakePicTemplateType(String templateName) {
        return Arrays.asList(App.getInstance().getResources().getStringArray(R.array.template_photos)).indexOf(templateName);
    }

    private int transRecordTemplateType(String templateName) {
        return Arrays.asList(App.getInstance().getResources().getStringArray(R.array.template_records)).indexOf(templateName);
    }

    private int getDisplayModeType(int displayMode) {
        if (displayMode != CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR) {
            if (displayMode == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT) {
                return 1;
            }
            if (displayMode == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT) {
                return 2;
            }
            if (displayMode == CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT) {
                return 3;
            }
            if (displayMode == 5) {
                return 4;
            }
            if (displayMode == 4) {
                return 5;
            }
            if (displayMode == 6) {
                return 6;
            }
            if (displayMode == 7) {
                return 7;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        static BIHelper sInstance = new BIHelper();

        private SingletonHolder() {
        }
    }
}
