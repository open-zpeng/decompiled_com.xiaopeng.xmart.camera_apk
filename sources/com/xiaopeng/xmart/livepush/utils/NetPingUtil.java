package com.xiaopeng.xmart.livepush.utils;
/* loaded from: classes.dex */
public class NetPingUtil {
    private static final String PING_FORMAT_COMMEND = "ping -c %d -w %d %s";
    private static String TAG = "NetPingUtil";

    public static void ping(String address) {
        ping(address, 3, 5);
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x009b A[Catch: Exception -> 0x0097, TRY_LEAVE, TryCatch #5 {Exception -> 0x0097, blocks: (B:43:0x0093, B:47:0x009b), top: B:53:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0093 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void ping(java.lang.String r5, int r6, int r7) {
        /*
            r0 = 0
            r1 = 0
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.lang.String r3 = "ping -c %d -w %d %s"
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            r4[r1] = r6     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            r6 = 1
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            r4[r6] = r7     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            r6 = 2
            r4[r6] = r5     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.lang.String r5 = java.lang.String.format(r3, r4)     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.lang.Process r5 = r2.exec(r5)     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            if (r5 != 0) goto L26
            return
        L26:
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.io.InputStream r2 = r5.getInputStream()     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            r7.<init>(r2)     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L6d java.lang.Exception -> L70
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L69
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L69
            java.io.InputStream r5 = r5.getErrorStream()     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L69
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L69
            r7.<init>(r2)     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L69
        L42:
            java.lang.String r5 = r6.readLine()     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L63
            if (r5 == 0) goto L4e
            java.lang.String r0 = com.xiaopeng.xmart.livepush.utils.NetPingUtil.TAG     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L63
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r0, r5, r1)     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L63
            goto L42
        L4e:
            java.lang.String r5 = r7.readLine()     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L63
            if (r5 == 0) goto L5a
            java.lang.String r0 = com.xiaopeng.xmart.livepush.utils.NetPingUtil.TAG     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L63
            com.xiaopeng.xmart.camera.utils.CameraLog.d(r0, r5, r1)     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L63
            goto L4e
        L5a:
            r6.close()     // Catch: java.lang.Exception -> L84
            r7.close()     // Catch: java.lang.Exception -> L84
            goto L8f
        L61:
            r5 = move-exception
            goto L67
        L63:
            r5 = move-exception
            goto L6b
        L65:
            r5 = move-exception
            r7 = r0
        L67:
            r0 = r6
            goto L91
        L69:
            r5 = move-exception
            r7 = r0
        L6b:
            r0 = r6
            goto L72
        L6d:
            r5 = move-exception
            r7 = r0
            goto L91
        L70:
            r5 = move-exception
            r7 = r0
        L72:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L90
            java.lang.String r6 = com.xiaopeng.xmart.livepush.utils.NetPingUtil.TAG     // Catch: java.lang.Throwable -> L90
            java.lang.String r5 = r5.getMessage()     // Catch: java.lang.Throwable -> L90
            com.xiaopeng.xmart.camera.utils.CameraLog.e(r6, r5, r1)     // Catch: java.lang.Throwable -> L90
            if (r0 == 0) goto L86
            r0.close()     // Catch: java.lang.Exception -> L84
            goto L86
        L84:
            r5 = move-exception
            goto L8c
        L86:
            if (r7 == 0) goto L8f
            r7.close()     // Catch: java.lang.Exception -> L84
            goto L8f
        L8c:
            r5.printStackTrace()
        L8f:
            return
        L90:
            r5 = move-exception
        L91:
            if (r0 == 0) goto L99
            r0.close()     // Catch: java.lang.Exception -> L97
            goto L99
        L97:
            r6 = move-exception
            goto L9f
        L99:
            if (r7 == 0) goto La2
            r7.close()     // Catch: java.lang.Exception -> L97
            goto La2
        L9f:
            r6.printStackTrace()
        La2:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xmart.livepush.utils.NetPingUtil.ping(java.lang.String, int, int):void");
    }
}
