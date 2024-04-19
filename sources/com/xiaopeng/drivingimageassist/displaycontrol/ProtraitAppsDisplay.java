package com.xiaopeng.drivingimageassist.displaycontrol;

import android.content.ComponentName;
import android.content.Context;
import com.xiaopeng.drivingimageassist.event.ActivityEvent;
import com.xiaopeng.drivingimageassist.event.ApDisplayEvent;
import com.xiaopeng.drivingimageassist.event.CameraDisplayEvent;
import com.xiaopeng.drivingimageassist.utils.AppUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes.dex */
public class ProtraitAppsDisplay implements IAppsDisplay {
    public static final String AUTOPILOT_ACTIVITY = "com.xiaopeng.autopilot.base.BgActivity";
    public static final String CAMERA_ACTIVITY = "com.xiaopeng.xmart.camera.MainActivity";
    private int mApDisplay;
    private int mCameraDisplay;

    public ProtraitAppsDisplay(Context context) {
        updateStatus(AppUtils.getCurrentRunningTask(context));
        EventBus.getDefault().register(this);
    }

    private void updateStatus(ComponentName componentName) {
        if (componentName.getClassName().equals("com.xiaopeng.autopilot.base.BgActivity")) {
            this.mApDisplay = 1;
            EventBus.getDefault().post(new ApDisplayEvent(this.mApDisplay));
        } else {
            this.mApDisplay = 0;
            EventBus.getDefault().post(new ApDisplayEvent(this.mApDisplay));
        }
        if (componentName.getClassName().equals("com.xiaopeng.xmart.camera.MainActivity")) {
            this.mCameraDisplay = 1;
            EventBus.getDefault().post(new CameraDisplayEvent(this.mCameraDisplay));
            return;
        }
        this.mCameraDisplay = 0;
        EventBus.getDefault().post(new CameraDisplayEvent(this.mCameraDisplay));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(ActivityEvent event) {
        updateStatus(event.getComponentName());
    }

    @Override // com.xiaopeng.drivingimageassist.displaycontrol.IAppsDisplay
    public boolean isApDisplay() {
        return this.mApDisplay == 1;
    }

    @Override // com.xiaopeng.drivingimageassist.displaycontrol.IAppsDisplay
    public boolean isCameraDisplay() {
        return this.mCameraDisplay == 1;
    }
}
