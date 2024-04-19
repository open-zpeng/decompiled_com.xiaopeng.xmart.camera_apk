package com.xiaopeng.xmart.camera.define;
/* loaded from: classes.dex */
public interface DVRDefine {

    /* loaded from: classes.dex */
    public interface CiuAutoLockFb {
        public static final int AUTO_LOCK_FAIL = 3;
        public static final int AUTO_LOCK_PROCESS = 1;
        public static final int AUTO_LOCK_SUCCESS = 2;
        public static final int NO_REQUEST = 0;
    }

    /* loaded from: classes.dex */
    public interface CiuValid {
        public static final int CIU_INVALID = 0;
        public static final int CIU_NORMAL = 1;
    }

    /* loaded from: classes.dex */
    public interface DVREnableStatus {
        public static final int DVR_OFF = 0;
        public static final int DVR_ON = 1;
    }

    /* loaded from: classes.dex */
    public interface DVRMode {
        public static final int DATA_UPLOAD_MODE = 3;
        public static final int OTA_MODE = 2;
        public static final int RECORD_MODE = 0;
        public static final int REPLAY_MODE = 1;
    }

    /* loaded from: classes.dex */
    public interface DVRStatusFd {
        public static final int DVR_STATUS_FAULT = 1;
        public static final int DVR_STATUS_NORMAL = 0;
    }

    /* loaded from: classes.dex */
    public interface DvrLockMode {
        public static final int LOCK = 1;
        public static final int UNLOCK = 2;
    }

    /* loaded from: classes.dex */
    public interface DvrLockModeFb {
        public static final int LOCK_FAILURE = 3;
        public static final int LOCK_PROCESS = 1;
        public static final int LOCK_SUCCESS = 2;
        public static final int NO_REQUEST = 0;
    }

    /* loaded from: classes.dex */
    public interface DvrLockStorageFb {
        public static final int LOCK_STORAGE_ALMOST_FULL = 1;
        public static final int LOCK_STORAGE_ENOUTH = 0;
    }

    /* loaded from: classes.dex */
    public interface FormatFb {
        public static final int FORMAT_FAILURE = 3;
        public static final int FORMAT_PROCESS = 1;
        public static final int FORMAT_SUCCESS = 2;
        public static final int NO_REQUEST = 0;
    }

    /* loaded from: classes.dex */
    public interface FormatMode {
        public static final int FORMAT = 1;
        public static final int NO_FORMAT = 0;
    }

    /* loaded from: classes.dex */
    public interface PhotoProcessFb {
        public static final int FAILURE = 1;
        public static final int NO_REQUEST = 0;
        public static final int SUCCESS = 2;
    }

    /* loaded from: classes.dex */
    public interface SDCARDStatusFd {
        public static final int SDCARD_STATUS_ABNORMAL = 1;
        public static final int SDCARD_STATUS_NORMAL = 0;
    }

    /* loaded from: classes.dex */
    public interface VideoOutputMode {
        public static final int AVM_OUTPUT = 0;
        public static final int DVR_OUTPUT = 2;
        public static final int FACE_IDENTITY = 1;
    }
}
