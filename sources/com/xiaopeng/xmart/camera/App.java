package com.xiaopeng.xmart.camera;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.xiaopeng.lib.apirouter.server.ApiPublisherProvider;
import com.xiaopeng.lib.apirouter.server.CameraAppVuiObserver_Manifest;
import com.xiaopeng.lib.apirouter.server.IManifestHandler;
import com.xiaopeng.lib.apirouter.server.IManifestHelper;
import com.xiaopeng.lib.apirouter.server.ManifestHelper_CarCameraApp;
import com.xiaopeng.lib.framework.moduleinterface.ipcmodule.IIpcService;
import com.xiaopeng.lib.utils.view.UIUtils;
import com.xiaopeng.speech.vui.VuiEngine;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import com.xiaopeng.xmart.camera.carmanager.Component;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.helper.BIHelper;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.SoundPoolHelper;
import com.xiaopeng.xmart.camera.helper.SpeechHelper;
import com.xiaopeng.xmart.camera.settings.SettingsModel;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xui.Xui;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes.dex */
public abstract class App extends Application {
    protected static App INSTANCE = null;
    private static final String TAG = "360CameraApp";
    private List<Activity> activities = new ArrayList();

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CameraLog.i(TAG, "attachBaseContext", false);
        addManifestHelperInUiThread();
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        CameraLog.i(TAG, "onCreate", false);
        init();
    }

    private void addManifestHelperInUiThread() {
        INSTANCE = this;
        if (CarCameraHelper.getInstance().is3DProject()) {
            ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$App$ej0Fr-z1VY9IJHVa7D2CJI9yqIw
                @Override // java.lang.Runnable
                public final void run() {
                    App.this.lambda$addManifestHelperInUiThread$0$App();
                }
            });
        } else {
            lambda$addManifestHelperInUiThread$0$App();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: addManifestHelper */
    public void lambda$addManifestHelperInUiThread$0$App() {
        ApiPublisherProvider.addManifestHandler(new IManifestHandler() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$App$mr-rPJ6eSK793oaQLnxzs8R-mno
            @Override // com.xiaopeng.lib.apirouter.server.IManifestHandler
            public final IManifestHelper[] getManifestHelpers() {
                return App.lambda$addManifestHelper$1();
            }
        });
        CameraLog.i(TAG, "addManifestHelper ManifestHelper_CarCameraApp " + INSTANCE, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IManifestHelper[] lambda$addManifestHelper$1() {
        return new IManifestHelper[]{new ManifestHelper_CarCameraApp()};
    }

    protected void init() {
        CameraLog.i(TAG, "init", false);
        INSTANCE = this;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        CarClientWrapper.getInstance().connectToCar(this);
        UIUtils.init(this);
        Xui.init(this);
        Xui.setVuiEnable(true);
        VuiEngine.getInstance(this).subscribe(CameraAppVuiObserver_Manifest.DESCRIPTOR);
        Xui.setFontScaleDynamicChangeEnable(true);
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$App$3SQp4q_DlRtoVE6n11kHscfCIAA
            @Override // java.lang.Runnable
            public final void run() {
                App.lambda$init$2();
            }
        });
        SettingsModel.getInstance().init(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$init$2() {
        BIHelper.getInstance().init(INSTANCE);
        Component.getInstance().initComponent();
        SoundPoolHelper.getInstance().loadSound();
        Config.printVersion();
        SpeechHelper.getInstance().initSpeechService();
    }

    public static App getInstance() {
        return INSTANCE;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IIpcService.IpcMessageEvent event) {
        int msgID = event.getMsgID();
        CameraLog.d(TAG, "ipc msgId: " + msgID, false);
        switch (msgID) {
            case 90006:
                CarCameraHelper.getInstance().setCurParkingType(1);
                return;
            case 90007:
                CarCameraHelper.getInstance().setCurParkingType(-1);
                return;
            default:
                return;
        }
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : this.activities) {
            activity.finish();
        }
        System.exit(0);
    }
}
