package com.xiaopeng.xmart.camera;

import com.xiaopeng.drivingimageassist.DrivingImageAssistModule;
import com.xiaopeng.lib.bughunter.StartPerfUtils;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.UIResLoader;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.livepush.RemoteControlIPCHelper;
import com.xiaopeng.xmart.livepush.RemoteControlPresentFactory;
import com.xiaopeng.xmart.shock.ShockAlarmHelper;
/* loaded from: classes.dex */
public class CameraApp extends App {
    private static final String TAG = "com.xiaopeng.xmart.camera.CameraApp";

    @Override // com.xiaopeng.xmart.camera.App, android.app.Application
    public void onCreate() {
        StartPerfUtils.onCreateBegin();
        super.onCreate();
        DrivingImageAssistModule.instance().init(this);
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$CameraApp$Y9LjVGBGXVBGbc6VgynLeP3XOEc
            @Override // java.lang.Runnable
            public final void run() {
                CameraApp.lambda$onCreate$0();
            }
        });
        RemoteControlIPCHelper.getInstance().setRemoteControlHandler(RemoteControlPresentFactory.getInstance(this));
        StartPerfUtils.onCreateEnd();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onCreate$0() {
        if (CarCameraHelper.getInstance().isPortraitScreen()) {
            UIResLoader.getInstance();
        }
        ShockAlarmHelper.getInstance().init();
    }
}
