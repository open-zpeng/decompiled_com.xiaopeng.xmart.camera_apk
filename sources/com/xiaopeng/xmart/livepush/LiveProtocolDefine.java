package com.xiaopeng.xmart.livepush;
/* loaded from: classes.dex */
interface LiveProtocolDefine {

    /* loaded from: classes.dex */
    public interface PushStatus {
        public static final int LIVE_PUSH_STATUS_ERROR = -1;
        public static final int LIVE_PUSH_STATUS_NORMAL = 0;
        public static final int LIVE_PUSH_STATUS_PAUSE = -2;
    }

    /* loaded from: classes.dex */
    public interface RemoteCmdValue {
        public static final int REMOTE_CMD_VALUE_OPEN_360_B = 3;
        public static final int REMOTE_CMD_VALUE_OPEN_360_B_3D = 7;
        public static final int REMOTE_CMD_VALUE_OPEN_360_F = 2;
        public static final int REMOTE_CMD_VALUE_OPEN_360_F_3D = 6;
        public static final int REMOTE_CMD_VALUE_OPEN_360_L = 4;
        public static final int REMOTE_CMD_VALUE_OPEN_360_L_3D = 8;
        public static final int REMOTE_CMD_VALUE_OPEN_360_R = 5;
        public static final int REMOTE_CMD_VALUE_OPEN_360_R_3D = 9;
        public static final int REMOTE_CMD_VALUE_OPEN_360_TRANSPARENT_CHASSIS = 10;
        public static final int REMOTE_CMD_VALUE_OPEN_TOP = 1;
        public static final int REMOTE_CMD_VALUE_REQUEST_LIVE = 11;
    }
}
