package com.xiaopeng.drivingimageassist.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import com.xiaopeng.drivingimageassist.event.ActivityEvent;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class WindowChangedReceiver extends BroadcastReceiver {
    public static final String ACTION_ACTIVITY_CHANGED = "com.xiaopeng.intent.action.ACTIVITY_CHANGED";
    public static final String AUTOPILOT_ACTIVITY = "com.xiaopeng.autopilot.base.BgActivity";
    public static final String EXTRA_COMPONENT = "android.intent.extra.COMPONENT";
    private static final String TAG = "WindowChangedReceiver";
    private boolean mRegistered;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.xiaopeng.intent.action.ACTIVITY_CHANGED".equals(intent.getAction())) {
            try {
                String stringExtra = intent.getStringExtra("android.intent.extra.COMPONENT");
                if (TextUtils.isEmpty(stringExtra)) {
                    return;
                }
                EventBus.getDefault().post(new ActivityEvent(ComponentName.unflattenFromString(stringExtra)));
            } catch (Exception unused) {
                Log.e(TAG, "componentInfo : error");
            }
        }
    }

    public void registerReceiver(Context context) {
        if (this.mRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xiaopeng.intent.action.ACTIVITY_CHANGED");
        context.registerReceiver(this, intentFilter);
        this.mRegistered = true;
    }

    public void unregisterReceiver(Context context) {
        if (this.mRegistered) {
            context.unregisterReceiver(this);
            this.mRegistered = false;
        }
    }
}
