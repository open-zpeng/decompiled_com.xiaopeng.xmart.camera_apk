package com.xiaopeng.drivingimageassist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class NRACtrlReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.xiaopeng.drivingimageassist.NRA";
    private static final String TAG = "NRACtrlReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        String action = intent.getAction();
        if (TextUtils.isEmpty(action) || !action.equals(ACTION)) {
            return;
        }
        EventBus.getDefault().post(new NRACtrlEvent(1, true));
    }
}
