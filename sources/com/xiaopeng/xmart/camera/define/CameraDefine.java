package com.xiaopeng.xmart.camera.define;

import com.xiaopeng.xmart.camera.utils.CarFunction;
/* loaded from: classes.dex */
public class CameraDefine {
    public static final String ACTION_LOCAL_CAMERA_SERVICE = "com.xiaopeng.xmart.cameraservice";
    public static final String CLZ_NAME_OF_MAIN = "com.xiaopeng.xmart.camera.MainActivity";
    public static final String INTENT_EXTRA_ITEM_PATH = "intent_extra_item_path";
    public static final String INTENT_EXTRA_TAB = "intent_extra_tab";
    public static final String INTENT_EXTRA_TAB_LIST = "intent_extra_tab_list";
    public static final String PIC_EXTENSION = ".jpg";
    public static final long STORAGE_FREE_LIMIT = 1610612736;
    public static final String TAB_CAMERA_360 = "tab_camera_360";
    public static final String TAB_CAMERA_BACK = "tab_camera_back";
    public static final String TAB_CAMERA_DVR = "tab_camera_dvr";
    public static final String TAB_CAMERA_TOP = "tab_camera_top";
    public static final String TAG = "CameraDefine";
    public static final String VIDEO_EXTENSION = ".mp4";
    public static final String XUI_SERVICE_PACKAGE = "com.xiaopeng.xuiservice";
    public static final long HIDE_PREVIEW_COVER_DELAY = getHidePreviewCoverDelay();
    public static final String KEY_IG_STATUS = String.valueOf(557847561);
    public static final String KEY_TBOX_CAMERA_CTRL = String.valueOf(554700825);
    public static final String KEY_TBOX_TBOX_GUARD = String.valueOf(557912117);
    public static final String KEY_VCU_BAT_BUMP_RECRD = String.valueOf(557847161);

    /* loaded from: classes.dex */
    public static class Actions {
        public static final String ACTION_CLOSE_TOP_CAMERA = "com.xiaopeng.action.ACTION_CLOSE_TOP_CAMERA";
        public static final String ACTION_EXTRA_CAMERA_TYPE = "extra_camera_type";
        public static final String ACTION_OPEN_360_CAMERA = "com.xiaopeng.action.ACTION_OPEN_360_CAMERA";
        public static final String ACTION_OPEN_TOP_CAMERA = "com.xiaopeng.action.ACTION_OPEN_TOP_CAMERA";
        public static final String ACTION_RECORD = "com.xiaopeng.action.ACTION_RECORD";
        public static final String ACTION_RECORD_END = "com.xiaopeng.action.ACTION_RECORD_END";
        public static final String ACTION_TAKE_PICTURE = "com.xiaopeng.action.ACTION_CAPTURE_PICTURE";
        public static final String ACTION_TOP_CAMERA_ROTATE = "com.xiaopeng.speech.topcamera.rotate";
        public static final String BUNDLE_KEY_CONTROL_MSG = "key_control_msg";
    }

    /* loaded from: classes.dex */
    public static class CAMERA_TYPE_PROTOCOL {
        public static final int CAMERA_TYPE_AVM_2D_FRONT = 1;
        public static final int CAMERA_TYPE_AVM_2D_LEFT = 5;
        public static final int CAMERA_TYPE_AVM_2D_REAR = 3;
        public static final int CAMERA_TYPE_AVM_2D_RIGHT = 7;
        public static final int CAMERA_TYPE_AVM_3D_FRONT = 2;
        public static final int CAMERA_TYPE_AVM_3D_LEFT = 6;
        public static final int CAMERA_TYPE_AVM_3D_REAR = 4;
        public static final int CAMERA_TYPE_AVM_3D_RIGHT = 8;
        public static final int CAMERA_TYPE_AVM_TRANSPARENT_CHASSIS = 9;
        public static final int CAMERA_TYPE_OCU = 0;
    }

    /* loaded from: classes.dex */
    public static class CarCamera {
        public static final int CAMERA_ID_360_OR_SINGLE_BACK = 2;
        public static final int CAMERA_ID_360_OR_SINGLE_BACK_H93 = 0;
        public static final int CAMERA_ID_360_OR_SINGLE_BACK_PREVIEW_HEIGHT = 720;
        public static final int CAMERA_ID_360_OR_SINGLE_BACK_PREVIEW_WIDTH = 1280;
        public static final int CAMERA_ID_TOP = 3;
        public static final int PREVIEW_HEIGHT_TOP_CAMERA = 1080;
        public static final int PREVIEW_WIDTH_TOP_CAMERA = 1920;
        public static final int VIDEO_RECORD_MAX_DURATION = 300000;
    }

    /* loaded from: classes.dex */
    public static class Events {
        public static final String FINISH_PREVIEW_ACTIVITY_EVENT_FUNCTION_NAME = "finishPreviewActivity";
    }

    static /* synthetic */ boolean access$000() {
        return isAVMDBCBaseE28();
    }

    private static long getHidePreviewCoverDelay() {
        String xpCduType = Config.getXpCduType();
        xpCduType.hashCode();
        char c = 65535;
        switch (xpCduType.hashCode()) {
            case 2560:
                if (xpCduType.equals("Q1")) {
                    c = 0;
                    break;
                }
                break;
            case 2562:
                if (xpCduType.equals("Q3")) {
                    c = 1;
                    break;
                }
                break;
            case 2566:
                if (xpCduType.equals("Q7")) {
                    c = 2;
                    break;
                }
                break;
            case 2567:
                if (xpCduType.equals("Q8")) {
                    c = 3;
                    break;
                }
                break;
            case 2568:
                if (xpCduType.equals(CarFunction.F30)) {
                    c = 4;
                    break;
                }
                break;
            case 79487:
                if (xpCduType.equals("Q3A")) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 3:
                return 80L;
            case 1:
            case 2:
            case 4:
            case 5:
                return 300L;
            default:
                return 600L;
        }
    }

    /* loaded from: classes.dex */
    public static class CAMERA_PANO {

        /* loaded from: classes.dex */
        public static class DIRECTION {
            public static final int CAMERA_TYPE_FOUR_CAMERA = 8;
            public static final int CAMERA_TYPE_TRANSPARENT_CHASSIS;
            public static final int DIRECTION_2D_FRONT;
            public static final int DIRECTION_2D_LEFT;
            public static final int DIRECTION_2D_REAR;
            public static final int DIRECTION_2D_RIGHT;
            public static final int DIRECTION_3D_FRONT = 4;
            public static final int DIRECTION_3D_LEFT = 6;
            public static final int DIRECTION_3D_REAR = 5;
            public static final int DIRECTION_3D_RIGHT = 7;

            static {
                DIRECTION_2D_FRONT = CameraDefine.access$000() ? 16 : 40;
                DIRECTION_2D_REAR = CameraDefine.access$000() ? 17 : 49;
                DIRECTION_2D_LEFT = CameraDefine.access$000() ? 18 : 42;
                DIRECTION_2D_RIGHT = CameraDefine.access$000() ? 19 : 43;
                CAMERA_TYPE_TRANSPARENT_CHASSIS = CameraDefine.access$000() ? 21 : 52;
            }
        }
    }

    private static boolean isAVMDBCBaseE28() {
        return Config.getXpCduType().equals("Q1") || Config.getXpCduType().equals(CarFunction.F30) || Config.getXpCduType().equals("Q3") || Config.getXpCduType().equals("Q3A") || Config.getXpCduType().equals("Q7") || Config.getXpCduType().equals("Q8") || Config.getXpCduType().equals("QB");
    }

    public static boolean isAVMSupport4Camera() {
        return Config.getXpCduType().equals(CarFunction.F30) || Config.getXpCduType().equals("Q7") || Config.getXpCduType().equals("Q8");
    }

    /* loaded from: classes.dex */
    public static class CAMERA_OCU {
        public static final int ANGLE_LARGE = 10;
        public static final int CAMERA_ANGLE_REMAINED = 5;
        public static final int CAMERA_ANGLE_START_UP = 360;
        public static final int ORIGINAL_PROGRESS = 180;

        public static float getTopCameraRotateRange() {
            return Config.getHardwareVersion() >= 4 ? 177.5f : 60.0f;
        }
    }
}
