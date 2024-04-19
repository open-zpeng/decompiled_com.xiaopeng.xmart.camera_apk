package com.xiaopeng.xmart.camera.utils;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class ThreadPoolHelper {
    private static final String TAG = "ThreadPoolHelper";
    private ExecutorService mFixedThreadPool;
    private Handler mMainHandler;
    private ExecutorService mSingleThreadPool;

    private ThreadPoolHelper() {
        this.mFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        this.mSingleThreadPool = Executors.newSingleThreadExecutor();
        this.mMainHandler = new Handler(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
    }

    public static ThreadPoolHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        private static ThreadPoolHelper INSTANCE = new ThreadPoolHelper();

        private SingletonHolder() {
        }
    }

    public void execute(Runnable runnable) {
        this.mFixedThreadPool.execute(runnable);
    }

    public void executeForLongTask(Runnable runnable) {
        this.mFixedThreadPool.execute(runnable);
    }

    public void executeBySequence(Runnable runnable) {
        this.mSingleThreadPool.execute(runnable);
    }

    public void postOnMainThread(Runnable runnable) {
        this.mMainHandler.post(runnable);
    }

    public void postDelayedOnMainThread(Runnable runnable, long duration) {
        this.mMainHandler.postDelayed(runnable, duration);
    }

    public void removeCallbacksOnMainThread(Runnable runnable) {
        this.mMainHandler.removeCallbacks(runnable);
    }
}
