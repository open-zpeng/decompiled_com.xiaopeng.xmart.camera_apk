package com.xiaopeng.xmart.livepush.utils;

import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class PrintResponseUtil {
    private static final String TAG = "PrintResponse";

    public static void printLiveRequestLog(IResponse response, boolean success) {
        String str;
        String str2;
        if (response != null) {
            String body = response.body();
            str2 = response.message();
            str = response.getException() != null ? response.getException().getMessage() : null;
            r0 = body;
        } else {
            str = null;
            str2 = null;
        }
        CameraLog.d(TAG, "getLiveUrl Success=" + success + ", body=" + r0, false);
        CameraLog.d(TAG, "getLiveUrl Success=" + success + ", message=" + str2, false);
        CameraLog.d(TAG, "getLiveUrl Success=" + success + ", excMsg=" + str, false);
    }
}
