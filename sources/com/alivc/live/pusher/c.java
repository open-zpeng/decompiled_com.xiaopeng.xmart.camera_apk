package com.alivc.live.pusher;
/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum a uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:444)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:391)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:320)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:258)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes.dex */
public final class c {
    public static final c A;
    public static final c B;
    public static final c C;
    public static final c D;
    public static final c E;
    public static final c F;
    public static final c G;
    public static final c H;
    private static final /* synthetic */ c[] K;
    public static final c a;
    public static final c b;
    public static final c c;
    public static final c d;
    public static final c e;
    public static final c f;
    public static final c g;
    public static final c h;
    public static final c i;
    public static final c j;
    public static final c k;
    public static final c l;
    public static final c m;
    public static final c n;
    public static final c o;
    public static final c p;
    public static final c q;
    public static final c r;
    public static final c s;
    public static final c t;
    public static final c u;
    public static final c v;
    public static final c w;
    public static final c x;
    public static final c y;
    public static final c z;
    private int I;
    private String J;

    /* loaded from: classes.dex */
    private static class a {
        protected static int a = -268238848;
        protected static int b = -268238336;
        protected static int c = 268457216;
        protected static int d = -268236544;
        protected static int e = -268238080;
    }

    static {
        int i2 = a.b + 1;
        a.b = i2;
        c cVar = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_INIT_SUCCESS", 0, i2, "");
        a = cVar;
        int i3 = a.b + 1;
        a.b = i3;
        c cVar2 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_PREVIEW_STARTED", 1, i3, "");
        b = cVar2;
        int i4 = a.b + 1;
        a.b = i4;
        c cVar3 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_PREVIEW_STOPED", 2, i4, "");
        c = cVar3;
        int i5 = a.b + 1;
        a.b = i5;
        c cVar4 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_PUSH_PAUSED", 3, i5, "");
        d = cVar4;
        int i6 = a.b + 1;
        a.b = i6;
        c cVar5 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_PUSH_RESUMED", 4, i6, "");
        e = cVar5;
        int i7 = a.b + 1;
        a.b = i7;
        c cVar6 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_PUSH_RESTARTED", 5, i7, "");
        f = cVar6;
        int i8 = a.b + 1;
        a.b = i8;
        c cVar7 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_OPEN_VIDEO_ENCODER_SUCCESS", 6, i8, "");
        g = cVar7;
        int i9 = a.b + 1;
        a.b = i9;
        c cVar8 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_CAPTURE_VIDEO_SAMPLES_OVERFLOW", 7, i9, "");
        h = cVar8;
        int i10 = a.b + 1;
        a.b = i10;
        c cVar9 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_ADJUST_BITRATE", 8, i10, "live pusher adjust bit.");
        i = cVar9;
        int i11 = a.b + 1;
        a.b = i11;
        c cVar10 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_CHANGE_FPS", 9, i11, "live pusher change fps.");
        j = cVar10;
        int i12 = a.b + 1;
        a.b = i12;
        c cVar11 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_CHANGE_RESOLUTION", 10, i12, "live pusher change resolution.");
        k = cVar11;
        int i13 = a.b + 1;
        a.b = i13;
        c cVar12 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_GL_CONTEXT_CREATED", 11, i13, "");
        l = cVar12;
        int i14 = a.b + 1;
        a.b = i14;
        c cVar13 = new c("ALIVC_PUSHER_EVENT_NATIVE_LIVE_PUSH_GL_CONTEXT_DESTROYED", 12, i14, "");
        m = cVar13;
        int i15 = a.c + 1;
        a.c = i15;
        c cVar14 = new c("ALIVC_PUSHER_EVENT_CAPTURE_OPEN_CAMERA_SUCCESS", 13, i15, "open camera success.");
        n = cVar14;
        int i16 = a.c + 1;
        a.c = i16;
        c cVar15 = new c("ALIVC_PUSHER_EVENT_CAPTURE_OPEN_MIC_SUCCESS", 14, i16, "open mic success.");
        o = cVar15;
        int i17 = a.c + 1;
        a.c = i17;
        c cVar16 = new c("ALIVC_PUSHER_EVENT_CAPTURE_CLOSE_CAMERA_SUCCESS", 15, i17, "close camera success.");
        p = cVar16;
        int i18 = a.c + 1;
        a.c = i18;
        c cVar17 = new c("ALIVC_PUSHER_EVENT_CAPTURE_OPEN_SCREENCAPTURE_SUCCESS", 16, i18, "screen capture open suc");
        q = cVar17;
        int i19 = a.c + 1;
        a.c = i19;
        c cVar18 = new c("ALIVC_PUSHER_EVENT_CAPTURE_CLOSE_SCREENCAPTURE_SUCCESS", 17, i19, "screen capture close suc");
        r = cVar18;
        int i20 = a.d + 1;
        a.d = i20;
        c cVar19 = new c("ALIVC_PUSHER_EVENT_RTMP_PUSH_STARTED", 18, i20, "");
        s = cVar19;
        int i21 = a.d + 1;
        a.d = i21;
        c cVar20 = new c("ALIVC_PUSHER_EVENT_RTMP_PUSH_STOPED", 19, i21, "");
        t = cVar20;
        int i22 = a.d + 1;
        a.d = i22;
        c cVar21 = new c("ALIVC_PUSHER_EVENT_RTMP_NETWORK_POOR", 20, i22, "");
        u = cVar21;
        int i23 = a.d + 1;
        a.d = i23;
        c cVar22 = new c("ALIVC_PUSHER_EVENT_RTMP_NETWORK_RECOVERY", 21, i23, "");
        v = cVar22;
        int i24 = a.d + 1;
        a.d = i24;
        c cVar23 = new c("ALIVC_PUSHER_EVENT_RTMP_RECONNECT_START", 22, i24, "");
        w = cVar23;
        int i25 = a.d + 1;
        a.d = i25;
        c cVar24 = new c("ALIVC_PUSHER_EVENT_RTMP_RECONNECT_SUCCESS", 23, i25, "");
        x = cVar24;
        int i26 = a.d + 1;
        a.d = i26;
        c cVar25 = new c("ALIVC_PUSHER_EVENT_RTMP_DROP_FRAME", 24, i26, "drop frame.");
        y = cVar25;
        int i27 = a.d + 1;
        a.d = i27;
        c cVar26 = new c("ALIVC_PUSHER_EVENT_RTMP_SENDED_FIRST_AV", 25, i27, "");
        z = cVar26;
        int i28 = a.d + 1;
        a.d = i28;
        c cVar27 = new c("ALIVC_PUSHER_EVENT_RTMP_SENDED_SEI", 26, i28, "send sei");
        A = cVar27;
        int i29 = a.d + 1;
        a.d = i29;
        c cVar28 = new c("ALIVC_PUSHER_EVENT_RTMP_CONNECTION_LOST", 27, i29, "connection lost");
        B = cVar28;
        int i30 = a.e + 1;
        a.e = i30;
        c cVar29 = new c("ALIVC_PUSHER_EVENT_BGM_OPEN_SUCCESS", 28, i30, "bgm open success.");
        C = cVar29;
        int i31 = a.e + 1;
        a.e = i31;
        c cVar30 = new c("ALIVC_PUSHER_EVENT_BGM_STOP_SUCCESS", 29, i31, "bgm stop success.");
        D = cVar30;
        int i32 = a.e + 1;
        a.e = i32;
        c cVar31 = new c("ALIVC_PUSHER_EVENT_BGM_PAUSE_SUCCESS", 30, i32, "");
        E = cVar31;
        int i33 = a.e + 1;
        a.e = i33;
        c cVar32 = new c("ALIVC_PUSHER_EVENT_BGM_RESUME_SUCCESS", 31, i33, "");
        F = cVar32;
        int i34 = a.e + 1;
        a.e = i34;
        c cVar33 = new c("ALIVC_PUSHER_EVENT_BGM_COMPLETED", 32, i34, "bgm completed.");
        G = cVar33;
        int i35 = a.e + 1;
        a.e = i35;
        c cVar34 = new c("ALIVC_PUSHER_EVENT_BGM_PROGRESS", 33, i35, "");
        H = cVar34;
        K = new c[]{cVar, cVar2, cVar3, cVar4, cVar5, cVar6, cVar7, cVar8, cVar9, cVar10, cVar11, cVar12, cVar13, cVar14, cVar15, cVar16, cVar17, cVar18, cVar19, cVar20, cVar21, cVar22, cVar23, cVar24, cVar25, cVar26, cVar27, cVar28, cVar29, cVar30, cVar31, cVar32, cVar33, cVar34};
    }

    private c(String str, int i2, int i3, String str2) {
        this.I = i3;
        this.J = str2;
    }

    public static c valueOf(String str) {
        return (c) Enum.valueOf(c.class, str);
    }

    public static c[] values() {
        return (c[]) K.clone();
    }

    public int a() {
        return this.I;
    }
}
