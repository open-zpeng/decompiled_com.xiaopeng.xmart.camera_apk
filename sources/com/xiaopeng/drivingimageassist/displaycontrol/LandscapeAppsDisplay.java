package com.xiaopeng.drivingimageassist.displaycontrol;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.xiaopeng.drivingimageassist.event.ApDisplayEvent;
import com.xiaopeng.drivingimageassist.event.CameraDisplayEvent;
import com.xiaopeng.drivingimageassist.utils.Constant;
import com.xiaopeng.lib.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class LandscapeAppsDisplay implements IAppsDisplay {
    private static final String TAG = "LandscapeAppsDisplay";
    private int mApDisplay;
    private ContentObserver mApSceneObserver;
    private int mCameraDisplay;
    private ContentObserver mCameraSceneObserver;
    private Context mContext;

    public LandscapeAppsDisplay(Context context) {
        this.mContext = context;
        registerApListener();
        registerCameraListener();
        updateApStatus();
        updateCameraStatus();
    }

    @Override // com.xiaopeng.drivingimageassist.displaycontrol.IAppsDisplay
    public boolean isApDisplay() {
        return this.mApDisplay == 1;
    }

    @Override // com.xiaopeng.drivingimageassist.displaycontrol.IAppsDisplay
    public boolean isCameraDisplay() {
        return this.mCameraDisplay == 1;
    }

    private void registerApListener() {
        this.mApSceneObserver = new ContentObserver(new Handler()) { // from class: com.xiaopeng.drivingimageassist.displaycontrol.LandscapeAppsDisplay.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                LogUtils.i(LandscapeAppsDisplay.TAG, "ApScene");
                LandscapeAppsDisplay.this.updateApStatus();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Constant.displayControl.ENTER_AP_SCENE), true, this.mApSceneObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateApStatus() {
        int i = Settings.System.getInt(this.mContext.getContentResolver(), Constant.displayControl.ENTER_AP_SCENE, 0);
        this.mApDisplay = i;
        EventBus.getDefault().post(new ApDisplayEvent(i));
    }

    private void registerCameraListener() {
        this.mCameraSceneObserver = new ContentObserver(new Handler()) { // from class: com.xiaopeng.drivingimageassist.displaycontrol.LandscapeAppsDisplay.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                LogUtils.i(LandscapeAppsDisplay.TAG, "CameraScene");
                LandscapeAppsDisplay.this.updateCameraStatus();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Constant.displayControl.ENTER_360CAMERA_SENCE), true, this.mCameraSceneObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCameraStatus() {
        int i = Settings.System.getInt(this.mContext.getContentResolver(), Constant.displayControl.ENTER_360CAMERA_SENCE, 0);
        this.mCameraDisplay = i;
        EventBus.getDefault().post(new CameraDisplayEvent(i));
    }
}
