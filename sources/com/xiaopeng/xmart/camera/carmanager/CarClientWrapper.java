package com.xiaopeng.xmart.camera.carmanager;

import android.car.Car;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.xmart.camera.carmanager.impl.AvmController;
import com.xiaopeng.xmart.camera.carmanager.impl.CiuController;
import com.xiaopeng.xmart.camera.carmanager.impl.McuController;
import com.xiaopeng.xmart.camera.carmanager.impl.TBoxController;
import com.xiaopeng.xmart.camera.carmanager.impl.VcuController;
import com.xiaopeng.xmart.camera.carmanager.impl.XpuController;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import java.util.HashMap;
/* loaded from: classes.dex */
public class CarClientWrapper {
    private static final String TAG = "CarClientWrapper";
    private Car mCarClient;
    public static final String XP_AVM_SERVICE = "xp_avm";
    public static final String XP_CIU_SERVICE = "xp_ciu";
    public static final String XP_VCU_SERVICE = "xp_vcu";
    public static final String XP_MCU_SERVICE = "xp_mcu";
    public static final String XP_TBOX_SERVICE = "xp_tbox";
    public static final String XP_XPU_SERVICE = "xp_xpu";
    private static final String[] CAR_SVC_ARRAY = {XP_AVM_SERVICE, XP_CIU_SERVICE, XP_VCU_SERVICE, XP_MCU_SERVICE, XP_TBOX_SERVICE, XP_XPU_SERVICE};
    private static CarClientWrapper sInstance = null;
    private final Object mCarClientReady = new Object();
    private boolean mIsCarSvcConnected = false;
    private final Object mControllerLock = new Object();
    private final HashMap<String, BaseCarController<?, ?>> mControllers = new HashMap<>();
    private final ServiceConnection mCarConnectionCb = new ServiceConnection() { // from class: com.xiaopeng.xmart.camera.carmanager.CarClientWrapper.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            CameraLog.d(CarClientWrapper.TAG, "onCarServiceConnected", false);
            CarClientWrapper.this.initCarControllers();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            CameraLog.d(CarClientWrapper.TAG, "onCarServiceDisconnected", false);
            CarClientWrapper.this.mIsCarSvcConnected = false;
        }
    };
    private String mCarType = Config.getXpCduType();

    public static CarClientWrapper getInstance() {
        if (sInstance == null) {
            synchronized (CarClientWrapper.class) {
                if (sInstance == null) {
                    sInstance = new CarClientWrapper();
                }
            }
        }
        return sInstance;
    }

    private CarClientWrapper() {
    }

    public void connectToCar(Context context) {
        if (this.mIsCarSvcConnected) {
            return;
        }
        Car createCar = Car.createCar(context, this.mCarConnectionCb);
        this.mCarClient = createCar;
        createCar.connect();
        CameraLog.d(TAG, "Start to connect Car service", false);
    }

    private void reconnectToCar(final Context context) {
        ThreadPoolHelper.getInstance().postDelayedOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.carmanager.-$$Lambda$CarClientWrapper$dR1qOf_Vh-FoLVCN6jQt2GpS3lc
            @Override // java.lang.Runnable
            public final void run() {
                CarClientWrapper.this.lambda$reconnectToCar$0$CarClientWrapper(context);
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$reconnectToCar$0$CarClientWrapper(Context context) {
        CameraLog.d(TAG, "reconnect to car service", false);
        connectToCar(context);
    }

    public void disconnect() {
        Car car;
        if (!this.mIsCarSvcConnected || (car = this.mCarClient) == null) {
            return;
        }
        car.disconnect();
    }

    public boolean isCarServiceConnected() {
        synchronized (this.mCarClientReady) {
            while (!this.mIsCarSvcConnected) {
                try {
                    CameraLog.d(TAG, "Waiting car service connected", false);
                    this.mCarClientReady.wait();
                } catch (InterruptedException e) {
                    CameraLog.e(TAG, e.getMessage());
                }
            }
        }
        return true;
    }

    public BaseCarController getController(String serviceName) {
        BaseCarController<?, ?> baseCarController;
        Car carClient = getCarClient();
        synchronized (this.mControllerLock) {
            baseCarController = this.mControllers.get(serviceName);
            if (baseCarController == null) {
                baseCarController = createCarController(serviceName, carClient);
                this.mControllers.put(serviceName, baseCarController);
            }
        }
        return baseCarController;
    }

    private Car getCarClient() {
        Car car;
        synchronized (this.mCarClientReady) {
            while (!this.mIsCarSvcConnected) {
                try {
                    CameraLog.d(TAG, "Waiting car service connecting", false);
                    this.mCarClientReady.wait();
                } catch (InterruptedException e) {
                    CameraLog.e(TAG, e.getMessage());
                }
            }
            car = this.mCarClient;
        }
        return car;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initCarControllers() {
        ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.camera.carmanager.CarClientWrapper.2
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr;
                CameraLog.d(CarClientWrapper.TAG, "initCarControllers start");
                synchronized (CarClientWrapper.this.mControllerLock) {
                    for (String str : CarClientWrapper.CAR_SVC_ARRAY) {
                        BaseCarController baseCarController = (BaseCarController) CarClientWrapper.this.mControllers.get(str);
                        if (baseCarController != null) {
                            CameraLog.d(CarClientWrapper.TAG, "re-initCarControllers", false);
                            baseCarController.disconnect();
                            baseCarController.initCarManager(CarClientWrapper.this.mCarClient);
                        } else {
                            CarClientWrapper carClientWrapper = CarClientWrapper.this;
                            CarClientWrapper.this.mControllers.put(str, carClientWrapper.createCarController(str, carClientWrapper.mCarClient));
                        }
                    }
                }
                CameraLog.d(CarClientWrapper.TAG, "initCarControllers end");
                synchronized (CarClientWrapper.this.mCarClientReady) {
                    CarClientWrapper.this.mIsCarSvcConnected = true;
                    CarClientWrapper.this.mCarClientReady.notifyAll();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BaseCarController createCarController(String serviceName, final Car carClient) {
        BaseCarController tBoxController;
        serviceName.hashCode();
        char c = 65535;
        switch (serviceName.hashCode()) {
            case -1870955074:
                if (serviceName.equals(XP_TBOX_SERVICE)) {
                    c = 0;
                    break;
                }
                break;
            case -753107695:
                if (serviceName.equals(XP_AVM_SERVICE)) {
                    c = 1;
                    break;
                }
                break;
            case -753106168:
                if (serviceName.equals(XP_CIU_SERVICE)) {
                    c = 2;
                    break;
                }
                break;
            case -753096744:
                if (serviceName.equals(XP_MCU_SERVICE)) {
                    c = 3;
                    break;
                }
                break;
            case -753088095:
                if (serviceName.equals(XP_VCU_SERVICE)) {
                    c = 4;
                    break;
                }
                break;
            case -753085770:
                if (serviceName.equals(XP_XPU_SERVICE)) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                tBoxController = new TBoxController();
                break;
            case 1:
                tBoxController = AvmController.createCarController(this.mCarType);
                break;
            case 2:
                tBoxController = new CiuController();
                break;
            case 3:
                tBoxController = new McuController();
                break;
            case 4:
                tBoxController = new VcuController();
                break;
            case 5:
                tBoxController = new XpuController();
                break;
            default:
                throw new IllegalArgumentException("Can not create controller for " + serviceName);
        }
        tBoxController.initCarManager(carClient);
        return tBoxController;
    }
}
