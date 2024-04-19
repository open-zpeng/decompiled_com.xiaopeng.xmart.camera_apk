package com.xiaopeng.drivingimageassist.carapi;

import android.car.Car;
import android.car.hardware.CarEcuManager;
import android.car.hardware.avm.CarAvmManager;
import android.car.hardware.vcu.CarVcuManager;
import android.car.hardware.xpu.CarXpuManager;
import android.car.intelligent.CarIntelligentEngineManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.xiaopeng.drivingimageassist.config.Config;
import com.xiaopeng.drivingimageassist.event.NRABtnEvent;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class CarApiHelper {
    private static String TAG = "CarApiHelper";
    private CarAvmManager mCarAvmManager;
    private Car mCarClient;
    private CarIntelligentEngineManager mCarIntelligentEngineManager;
    private CarVcuManager mCarManager;
    private CarXpuManager mCarXpuManager;
    private List<ICarEvent> mCarEventList = new ArrayList();
    private final ServiceConnection mCarConnectionCb = new ServiceConnection() { // from class: com.xiaopeng.drivingimageassist.carapi.CarApiHelper.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i(CarApiHelper.TAG, "onServiceConnected");
            for (ICarEvent iCarEvent : CarApiHelper.this.mCarEventList) {
                CarApiHelper.this.register(iCarEvent);
            }
            EventBus.getDefault().post(new NRABtnEvent(CarApiHelper.this.getNraSwitchStatus()));
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i(CarApiHelper.TAG, "onServiceDisconnected");
            CarApiHelper.this.unregister();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        private static final CarApiHelper Instance = new CarApiHelper();

        private Holder() {
        }
    }

    public static final CarApiHelper instance() {
        return Holder.Instance;
    }

    public void init(Context context, Config.Params params) {
        Car createCar = Car.createCar(context, this.mCarConnectionCb);
        this.mCarClient = createCar;
        createCar.connect();
        for (ICarEvent iCarEvent : CarEventFactory.createCarEvents(params)) {
            addCarEvent(iCarEvent);
        }
    }

    public void addCarEvent(ICarEvent carEvent) {
        this.mCarEventList.add(carEvent);
        if (this.mCarClient.isConnected()) {
            register(carEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void register(final ICarEvent carEvent) {
        try {
            Object carManager = this.mCarClient.getCarManager(carEvent.carManager());
            if (carManager instanceof CarEcuManager) {
                ((CarEcuManager) carManager).registerPropCallback(carEvent.eventIds(), carEvent);
            } else if (carManager instanceof CarIntelligentEngineManager) {
                ((CarIntelligentEngineManager) carManager).registerCarDrivingSceneListener((CarIntelligentEngineManager.CarDrivingSceneListener) carEvent);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "register:" + e.getLocalizedMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregister() {
        ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.drivingimageassist.carapi.CarApiHelper.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    for (ICarEvent iCarEvent : CarApiHelper.this.mCarEventList) {
                        ((CarEcuManager) CarApiHelper.this.mCarClient.getCarManager(iCarEvent.carManager())).unregisterCallback(iCarEvent);
                    }
                } catch (Exception e) {
                    Log.e(CarApiHelper.TAG, "unregister:" + e.getLocalizedMessage());
                }
            }
        });
    }

    public float getRawCarSpeed() {
        try {
            if (this.mCarManager == null) {
                this.mCarManager = (CarVcuManager) this.mCarClient.getCarManager(CarClientWrapper.XP_VCU_SERVICE);
            }
            return this.mCarManager.getRawCarSpeed();
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public int getNraSwitchStatus() {
        try {
            if (this.mCarXpuManager == null) {
                this.mCarXpuManager = (CarXpuManager) this.mCarClient.getCarManager(CarClientWrapper.XP_XPU_SERVICE);
            }
            return this.mCarXpuManager.getNraSwitchStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getAvmWorkState() {
        try {
            if (this.mCarAvmManager == null) {
                this.mCarAvmManager = (CarAvmManager) this.mCarClient.getCarManager(CarClientWrapper.XP_AVM_SERVICE);
            }
            return this.mCarAvmManager.getAvmWorkState() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNedcStatus() {
        try {
            if (this.mCarXpuManager == null) {
                this.mCarXpuManager = (CarXpuManager) this.mCarClient.getCarManager(CarClientWrapper.XP_XPU_SERVICE);
            }
            return this.mCarXpuManager.getNedcSwitchStatus() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
