package com.xiaopeng.drivingimageassist.statistic;
/* loaded from: classes.dex */
public class StatisticConstants {
    public static final long BTN_ANIM_DURATION = 300;
    public static final long BTN_ANIM_START_OFFSET = 400;
    public static final String DIG_CLOSE = "driving_close";
    public static final String DIG_OPEN = "driving_open";
    public static final String EVENT_DIG_ASSIST = "driving_image_assist";
    public static final String KEY_EVENT = "event";
    public static final String KEY_VALUE = "value";
    public static final String MODEL_DIG = "driving";
    public static final String NRA_CTRL_SHOW = "nra_ctrl_show";
    public static final String NRA_CTRL_SW_ST = "nra_ctrl_sw_st";
    public static final String SYS_CONFIG_AVM_CFC = "persist.sys.xiaopeng.AVM";
    public static final String TURN_LAMP_SHOW = "turn_lamp_show";
    public static final String TURN_LAMP_SW_ST = "turn_lamp_sw_st";

    /* loaded from: classes.dex */
    public enum TurnLampError {
        TURN_LAMP_ERROR,
        R_LEVEL_ERROR,
        SPEED_LIMIT,
        TURN_OFF,
        AP_DISPLAY,
        CAMERA_DISPLAY,
        NRA_CTRL_DISPLAY,
        AVM_WORK_ERROR,
        NEDC_ERROR,
        XPU_ERROR
    }
}
