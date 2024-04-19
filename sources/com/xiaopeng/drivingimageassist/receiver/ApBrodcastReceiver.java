package com.xiaopeng.drivingimageassist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import com.xiaopeng.drivingimageassist.DIGActivity;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class ApBrodcastReceiver extends BroadcastReceiver {
    public static final String ACTION_XPENG_KEY = "com.xiaopeng.intent.action.xkey";
    public static int NAR_FUNCTION = 10;
    private static final String TAG = "ApBrodcastReceiver";
    private boolean mRegistered;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        String action = intent.getAction();
        if (TextUtils.isEmpty(action) || !action.equals("com.xiaopeng.intent.action.xkey")) {
            return;
        }
        int intExtra = intent.getIntExtra("keyfunc", -1);
        Log.i(TAG, "function:" + intExtra);
        if (intExtra == NAR_FUNCTION) {
            if (DIGActivity.isShowing()) {
                EventBus.getDefault().post(new NRACtrlEvent(0, true));
                DIGActivity.hide();
                return;
            }
            EventBus.getDefault().post(new NRACtrlEvent(1, true));
        }
    }

    public void registerReceiver(Context context) {
        if (this.mRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xiaopeng.intent.action.xkey");
        context.registerReceiver(this, intentFilter);
        this.mRegistered = true;
    }
}
