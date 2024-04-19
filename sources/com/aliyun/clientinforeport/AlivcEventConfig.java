package com.aliyun.clientinforeport;

import android.content.Context;
import com.aliyun.clientinforeport.util.RLog;
/* loaded from: classes.dex */
public class AlivcEventConfig {
    private static final String SDK_VERSION = "V1.0.0_alpha";
    private Context context;

    public static String getSdkVersion() {
        return SDK_VERSION;
    }

    public Context getContext() {
        Context context = this.context;
        if (context != null) {
            return context;
        }
        throw new IllegalAccessError("context is Empty!");
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void enableLog() {
        RLog.setOpen(true);
    }

    public void disableLog() {
        RLog.setOpen(false);
    }
}
