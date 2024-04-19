package com.xiaopeng.drivingimageassist.utils;

import android.car.hardware.avm.CarAvmManager;
/* loaded from: classes.dex */
public class Constant {
    public static final int CAMERA_HARDWARE_ID = 2;
    public static final int CAMERA_TYPE_NULL = -1;
    public static final int WINDOW_TYPE = 2042;
    CarAvmManager c;

    /* loaded from: classes.dex */
    public static class NRACtrl {
        public static final int XPU_NAR_CTRL_STATUS_NOT_AVAILIABLE = 2;
        public static final int XPU_NAR_CTRL_STATUS_OFF = 0;
        public static final int XPU_NAR_CTRL_STATUS_ON = 1;
    }

    /* loaded from: classes.dex */
    public static class Time {
        public static final long NAPALOADED_CHECK_DELAYMILLIS = 10000;
        public static final long NapaLoaded_CHECK_MAX_TIME = 120000;
    }

    /* loaded from: classes.dex */
    public static class TurnScene {
        public static final String TURN_ASSISTANT_SW = "turn_assistant_sw";
        public static final long TURN_SCENE_SPEED_CHECK_DELAY = 200;
        public static final float TURN_SCENE_SPEED_LIMIT = 10.0f;
    }

    /* loaded from: classes.dex */
    public static class displayControl {
        public static final String ENTER_360CAMERA_SENCE = "enter_360camera_sence";
        public static final String ENTER_AP_SCENE = "enter_ap_scene";
    }
}
