package com.alivc.live.pusher;
/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum ALIVC_FRAMEWORK_ERROR_THREAD_EXIT uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:444)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:391)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:320)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:258)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes.dex */
public final class AlivcLivePushError {
    private static final /* synthetic */ AlivcLivePushError[] $VALUES;
    public static final AlivcLivePushError ALIVC_COMMON_INVALID_PARAM;
    public static final AlivcLivePushError ALIVC_COMMON_RETURN_FAILED;
    public static final AlivcLivePushError ALIVC_COMMON_RETURN_SUCCESS;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_INPUT;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_INTERRUPT;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_STATE;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_WITHOUT_WORK;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_AUDIO_ENCODER_INIT_FAILED;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_AUDIO_ENCODER_WIDTHOUT_MATCH_ENCODER;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_DISPATCH_MSG_FAILED;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_ERROR_SERVICE_IS_ALREADY_EXIST;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_ERROR_THREAD_EXIT;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_IS_SYNC_MSG;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_RENDER_ERROR_EGL;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_RENDER_ERROR_GL;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_RENDER_FIRST_FRAME_PREVIEWED;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_CREATE_ENCODER_FAILED;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_ANDROID_API_LEVEL;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_INPUT;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_INTERRUPT;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_NO_BUFFER_AVAILABLE;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_STATE;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_WITHOUT_WORK;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_QUEUE_EMPTY_WARNING;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_QUEUE_FULL_WARNING;
    public static final AlivcLivePushError ALIVC_FRAMEWORK_VIDEO_ENCODER_WIDTHOUT_MATCH_ENCODER;
    public static final AlivcLivePushError ALIVC_LIVE_ERROR_SYSTEM_RTMP_OOM;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_BGM_OPEN_FAILED;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_BGM_TIMEOUT;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_CAPTURE_CAMERA_OPEN_FAILED;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_CAPTURE_INIT_FAILED;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_CAPTURE_INVALID_STATE;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_CAPTURE_MIC_OPEN_FAILED;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_CAPTURE_SCREEN_OPEN_FAILED;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_ADJUST_BITRATE_FAIL;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_LOW_PERFORMANCE;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_NETWORK_TOO_POOR;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_NATIVE_LIVE_PUSH_INVALID_STATE;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT_STREAM;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_INVALID_STATE;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_INVALID_URL;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_RECONNECT_FAIL;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_SEND_DATA_TIMEOUT;
    public static final AlivcLivePushError ALIVC_PUSHER_ERROR_SDK_RTMP_SETUPURL;
    public static final AlivcLivePushError ALIVC_PUSHER_EVENT_CAPTURE_AUDIO_START_FAIL;
    public static final AlivcLivePushError ALIVC_PUSHER_EVENT_CAPTURE_VIDEO_START_FAIL;
    private int mCode;
    private String mMsg;

    /* loaded from: classes.dex */
    private static class ErrorCode {
        protected static int ALIVC_FRAMEWORK_AUDIO_ENCODER_START = 268448258;
        protected static int ALIVC_FRAMEWORK_DECODER_ERROR_START = 268443648;
        protected static int ALIVC_FRAMEWORK_ERROR_START = 268435456;
        protected static int ALIVC_FRAMEWORK_MUXER_ERROR_START = 268439552;
        protected static int ALIVC_FRAMEWORK_RENDER_ERROR_START = 268451840;
        protected static int ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = 268447744;
        protected static int ALIVC_LIVE_ERROR_SYSTEM_RTMP_START = 805374208;
        protected static int ALIVC_PUSHER_ERROR_NETWORK_RTMP_START = 805439744;
        protected static int ALIVC_PUSHER_ERROR_SDK_BGM_START = 805373184;
        protected static int ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = 268455936;
        protected static int ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START = 805438208;
        protected static int ALIVC_PUSHER_ERROR_SDK_NATIVE_LIVE_PUSH_START = 805437952;
        protected static int ALIVC_PUSHER_ERROR_SDK_RTMP_START = 805439744;

        private ErrorCode() {
        }
    }

    static {
        AlivcLivePushError alivcLivePushError = new AlivcLivePushError("ALIVC_COMMON_INVALID_PARAM", 0, -2, "");
        ALIVC_COMMON_INVALID_PARAM = alivcLivePushError;
        AlivcLivePushError alivcLivePushError2 = new AlivcLivePushError("ALIVC_COMMON_RETURN_FAILED", 1, -1, "");
        ALIVC_COMMON_RETURN_FAILED = alivcLivePushError2;
        AlivcLivePushError alivcLivePushError3 = new AlivcLivePushError("ALIVC_COMMON_RETURN_SUCCESS", 2, 0, "");
        ALIVC_COMMON_RETURN_SUCCESS = alivcLivePushError3;
        int i = ErrorCode.ALIVC_FRAMEWORK_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_ERROR_START = i;
        AlivcLivePushError alivcLivePushError4 = new AlivcLivePushError("ALIVC_FRAMEWORK_ERROR_THREAD_EXIT", 3, i, "");
        ALIVC_FRAMEWORK_ERROR_THREAD_EXIT = alivcLivePushError4;
        int i2 = ErrorCode.ALIVC_FRAMEWORK_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_ERROR_START = i2;
        AlivcLivePushError alivcLivePushError5 = new AlivcLivePushError("ALIVC_FRAMEWORK_DISPATCH_MSG_FAILED", 4, i2, "");
        ALIVC_FRAMEWORK_DISPATCH_MSG_FAILED = alivcLivePushError5;
        int i3 = ErrorCode.ALIVC_FRAMEWORK_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_ERROR_START = i3;
        AlivcLivePushError alivcLivePushError6 = new AlivcLivePushError("ALIVC_FRAMEWORK_IS_SYNC_MSG", 5, i3, "");
        ALIVC_FRAMEWORK_IS_SYNC_MSG = alivcLivePushError6;
        int i4 = ErrorCode.ALIVC_FRAMEWORK_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_ERROR_START = i4;
        AlivcLivePushError alivcLivePushError7 = new AlivcLivePushError("ALIVC_FRAMEWORK_ERROR_SERVICE_IS_ALREADY_EXIST", 6, i4, "");
        ALIVC_FRAMEWORK_ERROR_SERVICE_IS_ALREADY_EXIST = alivcLivePushError7;
        int i5 = ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = i5;
        AlivcLivePushError alivcLivePushError8 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_QUEUE_EMPTY_WARNING", 7, i5, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_QUEUE_EMPTY_WARNING = alivcLivePushError8;
        int i6 = ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = i6;
        AlivcLivePushError alivcLivePushError9 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_QUEUE_FULL_WARNING", 8, i6, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_QUEUE_FULL_WARNING = alivcLivePushError9;
        int i7 = ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = i7;
        AlivcLivePushError alivcLivePushError10 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_CREATE_ENCODER_FAILED", 9, i7, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_CREATE_ENCODER_FAILED = alivcLivePushError10;
        int i8 = ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = i8;
        AlivcLivePushError alivcLivePushError11 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_WIDTHOUT_MATCH_ENCODER", 10, i8, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_WIDTHOUT_MATCH_ENCODER = alivcLivePushError11;
        int i9 = ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = i9;
        AlivcLivePushError alivcLivePushError12 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_WITHOUT_WORK", 11, i9, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_WITHOUT_WORK = alivcLivePushError12;
        int i10 = ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_START = i10;
        AlivcLivePushError alivcLivePushError13 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_INTERRUPT", 12, i10, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_INTERRUPT = alivcLivePushError13;
        AlivcLivePushError alivcLivePushError14 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_ANDROID_API_LEVEL", 13, 268448000, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_ANDROID_API_LEVEL = alivcLivePushError14;
        AlivcLivePushError alivcLivePushError15 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_STATE", 14, 268448001, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_STATE = alivcLivePushError15;
        AlivcLivePushError alivcLivePushError16 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_INPUT", 15, 268448002, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_INPUT = alivcLivePushError16;
        AlivcLivePushError alivcLivePushError17 = new AlivcLivePushError("ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_NO_BUFFER_AVAILABLE", 16, 268448003, "");
        ALIVC_FRAMEWORK_VIDEO_ENCODER_ERROR_NO_BUFFER_AVAILABLE = alivcLivePushError17;
        int i11 = ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START = i11;
        AlivcLivePushError alivcLivePushError18 = new AlivcLivePushError("ALIVC_FRAMEWORK_AUDIO_ENCODER_INIT_FAILED", 17, i11, "");
        ALIVC_FRAMEWORK_AUDIO_ENCODER_INIT_FAILED = alivcLivePushError18;
        int i12 = ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START = i12;
        AlivcLivePushError alivcLivePushError19 = new AlivcLivePushError("ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_STATE", 18, i12, "");
        ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_STATE = alivcLivePushError19;
        int i13 = ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START = i13;
        AlivcLivePushError alivcLivePushError20 = new AlivcLivePushError("ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_INPUT", 19, i13, "");
        ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_INPUT = alivcLivePushError20;
        int i14 = ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START = i14;
        AlivcLivePushError alivcLivePushError21 = new AlivcLivePushError("ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_INTERRUPT", 20, i14, "");
        ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_INTERRUPT = alivcLivePushError21;
        int i15 = ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START = i15;
        AlivcLivePushError alivcLivePushError22 = new AlivcLivePushError("ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_WITHOUT_WORK", 21, i15, "");
        ALIVC_FRAMEWORK_AUDIO_ENCODER_ERROR_WITHOUT_WORK = alivcLivePushError22;
        int i16 = ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_AUDIO_ENCODER_START = i16;
        AlivcLivePushError alivcLivePushError23 = new AlivcLivePushError("ALIVC_FRAMEWORK_AUDIO_ENCODER_WIDTHOUT_MATCH_ENCODER", 22, i16, "");
        ALIVC_FRAMEWORK_AUDIO_ENCODER_WIDTHOUT_MATCH_ENCODER = alivcLivePushError23;
        int i17 = ErrorCode.ALIVC_FRAMEWORK_RENDER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_RENDER_ERROR_START = i17;
        AlivcLivePushError alivcLivePushError24 = new AlivcLivePushError("ALIVC_FRAMEWORK_RENDER_FIRST_FRAME_PREVIEWED", 23, i17, "");
        ALIVC_FRAMEWORK_RENDER_FIRST_FRAME_PREVIEWED = alivcLivePushError24;
        int i18 = ErrorCode.ALIVC_FRAMEWORK_RENDER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_RENDER_ERROR_START = i18;
        AlivcLivePushError alivcLivePushError25 = new AlivcLivePushError("ALIVC_FRAMEWORK_RENDER_ERROR_EGL", 24, i18, "");
        ALIVC_FRAMEWORK_RENDER_ERROR_EGL = alivcLivePushError25;
        int i19 = ErrorCode.ALIVC_FRAMEWORK_RENDER_ERROR_START + 1;
        ErrorCode.ALIVC_FRAMEWORK_RENDER_ERROR_START = i19;
        AlivcLivePushError alivcLivePushError26 = new AlivcLivePushError("ALIVC_FRAMEWORK_RENDER_ERROR_GL", 25, i19, "");
        ALIVC_FRAMEWORK_RENDER_ERROR_GL = alivcLivePushError26;
        int i20 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i20;
        AlivcLivePushError alivcLivePushError27 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_CAPTURE_INVALID_STATE", 26, i20, "");
        ALIVC_PUSHER_ERROR_SDK_CAPTURE_INVALID_STATE = alivcLivePushError27;
        int i21 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i21;
        AlivcLivePushError alivcLivePushError28 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_CAPTURE_INIT_FAILED", 27, i21, "");
        ALIVC_PUSHER_ERROR_SDK_CAPTURE_INIT_FAILED = alivcLivePushError28;
        int i22 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i22;
        AlivcLivePushError alivcLivePushError29 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_CAPTURE_CAMERA_OPEN_FAILED", 28, i22, "camrea open failed");
        ALIVC_PUSHER_ERROR_SDK_CAPTURE_CAMERA_OPEN_FAILED = alivcLivePushError29;
        int i23 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i23;
        AlivcLivePushError alivcLivePushError30 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_CAPTURE_MIC_OPEN_FAILED", 29, i23, "capture failed");
        ALIVC_PUSHER_ERROR_SDK_CAPTURE_MIC_OPEN_FAILED = alivcLivePushError30;
        int i24 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i24;
        AlivcLivePushError alivcLivePushError31 = new AlivcLivePushError("ALIVC_PUSHER_EVENT_CAPTURE_AUDIO_START_FAIL", 30, i24, "");
        ALIVC_PUSHER_EVENT_CAPTURE_AUDIO_START_FAIL = alivcLivePushError31;
        int i25 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i25;
        AlivcLivePushError alivcLivePushError32 = new AlivcLivePushError("ALIVC_PUSHER_EVENT_CAPTURE_VIDEO_START_FAIL", 31, i25, "");
        ALIVC_PUSHER_EVENT_CAPTURE_VIDEO_START_FAIL = alivcLivePushError32;
        int i26 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_CAPTURE_START = i26;
        AlivcLivePushError alivcLivePushError33 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_CAPTURE_SCREEN_OPEN_FAILED", 32, i26, "");
        ALIVC_PUSHER_ERROR_SDK_CAPTURE_SCREEN_OPEN_FAILED = alivcLivePushError33;
        int i27 = ErrorCode.ALIVC_LIVE_ERROR_SYSTEM_RTMP_START + 1;
        ErrorCode.ALIVC_LIVE_ERROR_SYSTEM_RTMP_START = i27;
        AlivcLivePushError alivcLivePushError34 = new AlivcLivePushError("ALIVC_LIVE_ERROR_SYSTEM_RTMP_OOM", 33, i27, "");
        ALIVC_LIVE_ERROR_SYSTEM_RTMP_OOM = alivcLivePushError34;
        int i28 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_NATIVE_LIVE_PUSH_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_NATIVE_LIVE_PUSH_START = i28;
        AlivcLivePushError alivcLivePushError35 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_NATIVE_LIVE_PUSH_INVALID_STATE", 34, i28, "");
        ALIVC_PUSHER_ERROR_SDK_NATIVE_LIVE_PUSH_INVALID_STATE = alivcLivePushError35;
        int i29 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START = i29;
        AlivcLivePushError alivcLivePushError36 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_NETWORK_TOO_POOR", 35, i29, "live pusher network to poor.");
        ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_NETWORK_TOO_POOR = alivcLivePushError36;
        int i30 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START = i30;
        AlivcLivePushError alivcLivePushError37 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_ADJUST_BITRATE_FAIL", 36, i30, "live pusher bit adjustment failed.");
        ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_ADJUST_BITRATE_FAIL = alivcLivePushError37;
        int i31 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_START = i31;
        AlivcLivePushError alivcLivePushError38 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_LOW_PERFORMANCE", 37, i31, "live pusher low performance.");
        ALIVC_PUSHER_ERROR_SDK_LIVE_PUSH_LOW_PERFORMANCE = alivcLivePushError38;
        int i32 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i32;
        AlivcLivePushError alivcLivePushError39 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_INVALID_STATE", 38, i32, "");
        ALIVC_PUSHER_ERROR_SDK_RTMP_INVALID_STATE = alivcLivePushError39;
        int i33 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i33;
        AlivcLivePushError alivcLivePushError40 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_SETUPURL", 39, i33, "rtmp setup url.");
        ALIVC_PUSHER_ERROR_SDK_RTMP_SETUPURL = alivcLivePushError40;
        int i34 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i34;
        AlivcLivePushError alivcLivePushError41 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT", 40, i34, "rtmp connect error.");
        ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT = alivcLivePushError41;
        int i35 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i35;
        AlivcLivePushError alivcLivePushError42 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT_STREAM", 41, i35, "rtmp connect stream.");
        ALIVC_PUSHER_ERROR_SDK_RTMP_CONNECT_STREAM = alivcLivePushError42;
        int i36 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i36;
        AlivcLivePushError alivcLivePushError43 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_INVALID_URL", 42, i36, "rtmp invalid url.");
        ALIVC_PUSHER_ERROR_SDK_RTMP_INVALID_URL = alivcLivePushError43;
        int i37 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i37;
        AlivcLivePushError alivcLivePushError44 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_RECONNECT_FAIL", 43, i37, "rtmp reconnect fail.");
        ALIVC_PUSHER_ERROR_SDK_RTMP_RECONNECT_FAIL = alivcLivePushError44;
        int i38 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_RTMP_START = i38;
        AlivcLivePushError alivcLivePushError45 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_SDK_RTMP_SEND_DATA_TIMEOUT", 44, i38, "send data timeout.");
        ALIVC_PUSHER_ERROR_SDK_RTMP_SEND_DATA_TIMEOUT = alivcLivePushError45;
        int i39 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_BGM_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_BGM_START = i39;
        AlivcLivePushError alivcLivePushError46 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_BGM_OPEN_FAILED", 45, i39, "bgm file open failed.");
        ALIVC_PUSHER_ERROR_BGM_OPEN_FAILED = alivcLivePushError46;
        int i40 = ErrorCode.ALIVC_PUSHER_ERROR_SDK_BGM_START + 1;
        ErrorCode.ALIVC_PUSHER_ERROR_SDK_BGM_START = i40;
        AlivcLivePushError alivcLivePushError47 = new AlivcLivePushError("ALIVC_PUSHER_ERROR_BGM_TIMEOUT", 46, i40, "bgm file load time out.");
        ALIVC_PUSHER_ERROR_BGM_TIMEOUT = alivcLivePushError47;
        $VALUES = new AlivcLivePushError[]{alivcLivePushError, alivcLivePushError2, alivcLivePushError3, alivcLivePushError4, alivcLivePushError5, alivcLivePushError6, alivcLivePushError7, alivcLivePushError8, alivcLivePushError9, alivcLivePushError10, alivcLivePushError11, alivcLivePushError12, alivcLivePushError13, alivcLivePushError14, alivcLivePushError15, alivcLivePushError16, alivcLivePushError17, alivcLivePushError18, alivcLivePushError19, alivcLivePushError20, alivcLivePushError21, alivcLivePushError22, alivcLivePushError23, alivcLivePushError24, alivcLivePushError25, alivcLivePushError26, alivcLivePushError27, alivcLivePushError28, alivcLivePushError29, alivcLivePushError30, alivcLivePushError31, alivcLivePushError32, alivcLivePushError33, alivcLivePushError34, alivcLivePushError35, alivcLivePushError36, alivcLivePushError37, alivcLivePushError38, alivcLivePushError39, alivcLivePushError40, alivcLivePushError41, alivcLivePushError42, alivcLivePushError43, alivcLivePushError44, alivcLivePushError45, alivcLivePushError46, alivcLivePushError47};
    }

    private AlivcLivePushError(String str, int i, int i2, String str2) {
        this.mCode = i2;
        this.mMsg = str2;
    }

    public static AlivcLivePushError getErrorByCode(int i) {
        for (int i2 = 0; i2 < values().length; i2++) {
            if (i == values()[i2].getCode()) {
                return values()[i2];
            }
        }
        return ALIVC_COMMON_RETURN_FAILED;
    }

    public static AlivcLivePushError valueOf(String str) {
        return (AlivcLivePushError) Enum.valueOf(AlivcLivePushError.class, str);
    }

    public static AlivcLivePushError[] values() {
        return (AlivcLivePushError[]) $VALUES.clone();
    }

    public int getCode() {
        return this.mCode;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public void setCode(int i) {
        this.mCode = i;
    }

    public void setMsg(String str) {
        this.mMsg = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return "AlivcLivePushError{mCode=" + Integer.toHexString(this.mCode) + ", mMsg='" + this.mMsg + "'}";
    }
}
