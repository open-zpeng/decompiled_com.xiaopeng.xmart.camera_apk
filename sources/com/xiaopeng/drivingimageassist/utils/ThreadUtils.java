package com.xiaopeng.drivingimageassist.utils;

import android.os.Handler;
import android.os.HandlerThread;
/* loaded from: classes.dex */
public class ThreadUtils {
    private static final String TAG = "ThreadUtils";
    private static Handler sWorkerHandler;
    private static HandlerThread sWorkerThread;

    static {
        HandlerThread handlerThread = new HandlerThread("work_thread");
        sWorkerThread = handlerThread;
        handlerThread.start();
        sWorkerHandler = new Handler(sWorkerThread.getLooper());
    }

    public static void postWorker(Runnable task) {
        if (task != null) {
            sWorkerHandler.post(task);
        }
    }

    public static void postWorker(Runnable task, long delay) {
        if (task != null) {
            sWorkerHandler.postDelayed(task, delay);
        }
    }

    public static void removeWorker(Runnable task) {
        if (task != null) {
            sWorkerHandler.removeCallbacks(task);
        }
    }
}
