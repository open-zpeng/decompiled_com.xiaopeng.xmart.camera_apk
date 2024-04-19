package com.aliyun.clientinforeport.core;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
/* loaded from: classes.dex */
public class LogService {
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    public LogService(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
    }

    public void execute(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public void quit() {
        if (this.mHandlerThread != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.mHandlerThread.quitSafely();
            } else {
                this.mHandlerThread.quit();
            }
        }
    }
}
