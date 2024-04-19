package com.xiaopeng.xmart.livepush;

import android.content.Context;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
/* loaded from: classes.dex */
public class RemoteControlPresentFactory {
    private static final String TAG = "LivePush";
    private static RemoteControlPresenter sRemoteControlHandler;

    public static RemoteControlPresenter getInstance(Context context) {
        if (sRemoteControlHandler == null) {
            if (CarCameraHelper.getInstance().shouldWaitAvmReady()) {
                sRemoteControlHandler = new DelayRemoteControlPresenter(context);
            } else {
                sRemoteControlHandler = new RemoteControlPresenter(context);
            }
        }
        return sRemoteControlHandler;
    }
}
