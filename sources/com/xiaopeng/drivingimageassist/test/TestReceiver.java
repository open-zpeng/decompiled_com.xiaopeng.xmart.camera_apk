package com.xiaopeng.drivingimageassist.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import com.xiaopeng.lib.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class TestReceiver extends BroadcastReceiver {
    public static final String ACTION_DIG_TEST = "com.xiaopeng.drivingimageassist.DIG_TEST";
    private boolean mRegistered;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Prams prams = new Prams();
        if (intent != null) {
            prams.mNRACtrlStatus = intent.getIntExtra("nra_ctrl", 0);
        }
        LogUtils.i("TestReceiver", "onReceive data : " + prams.toString());
        EventBus.getDefault().post(new NRACtrlEvent(prams.mNRACtrlStatus));
    }

    /* loaded from: classes.dex */
    class Prams {
        int mNRACtrlStatus = 0;

        Prams() {
        }

        public String toString() {
            return "Prams{mNRACtrlStatus=" + this.mNRACtrlStatus + '}';
        }
    }
}
