package com.xiaopeng.xmart.camera.helper;

import android.os.SystemClock;
/* loaded from: classes.dex */
public class ClickHelper {
    private static final long CAPTURE_INTERVAL = 1000;
    private static final long EXPORT_INTERVAL = 2000;
    private static final long JUMPER_INTERVAL = 300;
    private static final long NEWINTENT_INTERVAL = 800;
    public static final int OPERATION_CAPTURE = 1;
    public static final int OPERATION_EXPORT = 6;
    public static final int OPERATION_JUMPER = 4;
    public static final int OPERATION_NEWINTENT = 7;
    public static final int OPERATION_RECORD_END = 3;
    public static final int OPERATION_RECORD_START = 2;
    public static final int OPERATION_SWITCH = 5;
    public static final int OPERATION_TEMPLATE_RECORD_END_DELAY = 8;
    private static final long RECORD_END_INTERVAL = 2000;
    private static final long RECORD_START_INTERVAL = 1000;
    private static final long RECORD_TEMPLATE_END_INTERVAL = 3000;
    private static final long SWITCH_INTERVAL = 500;
    private final String TAG;
    private long mLastClickTime;

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        static ClickHelper sInstance = new ClickHelper();

        private SingletonHolder() {
        }
    }

    private ClickHelper() {
        this.TAG = ClickHelper.class.getName();
    }

    public static ClickHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public boolean isTooQuickClick() {
        return isQuickClick(1000L);
    }

    public boolean isQuickClick(int operation) {
        switch (operation) {
            case 1:
                return isQuickClick(1000L);
            case 2:
                return isQuickClick(1000L);
            case 3:
                return isQuickClick(2000L);
            case 4:
                return isQuickClick(300L);
            case 5:
                return isQuickClick(SWITCH_INTERVAL);
            case 6:
                return isQuickClick(2000L);
            case 7:
                return isQuickClick(NEWINTENT_INTERVAL);
            case 8:
                return isQuickClickDelay(0L, RECORD_TEMPLATE_END_INTERVAL);
            default:
                return false;
        }
    }

    private boolean isQuickClick(long duration) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mLastClickTime < duration) {
            return true;
        }
        this.mLastClickTime = elapsedRealtime;
        return false;
    }

    private boolean isQuickClickDelay(long duration, long delay) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mLastClickTime < duration) {
            return true;
        }
        this.mLastClickTime = elapsedRealtime + delay;
        return false;
    }

    public void resetTooQuickClick() {
        this.mLastClickTime = SystemClock.elapsedRealtime();
    }
}
