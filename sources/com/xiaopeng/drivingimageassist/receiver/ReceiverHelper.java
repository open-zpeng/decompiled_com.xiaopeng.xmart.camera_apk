package com.xiaopeng.drivingimageassist.receiver;

import android.content.Context;
import com.xiaopeng.drivingimageassist.config.Config;
import com.xiaopeng.drivingimageassist.utils.CarHardwareUtils;
/* loaded from: classes.dex */
public class ReceiverHelper {
    private ApBrodcastReceiver mApBrodcastReceiver;
    private WindowChangedReceiver mWindowChangedReceiver;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        private static final ReceiverHelper Instance = new ReceiverHelper();

        private Holder() {
        }
    }

    public static final ReceiverHelper instance() {
        return Holder.Instance;
    }

    public void registerReceiver(Context context, Config.Params params) {
        ApBrodcastReceiver apBrodcastReceiver = new ApBrodcastReceiver();
        this.mApBrodcastReceiver = apBrodcastReceiver;
        apBrodcastReceiver.registerReceiver(context);
        if (CarHardwareUtils.isUseWindowReceiver()) {
            WindowChangedReceiver windowChangedReceiver = new WindowChangedReceiver();
            this.mWindowChangedReceiver = windowChangedReceiver;
            windowChangedReceiver.registerReceiver(context);
        }
    }
}
