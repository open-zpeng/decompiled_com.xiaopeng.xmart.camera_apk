package com.xiaopeng.drivingimageassist.carapi.event;

import android.car.hardware.CarPropertyValue;
import android.car.intelligent.CarIntelligentEngineManager;
import android.car.intelligent.CarSceneEvent;
import com.xiaopeng.drivingimageassist.carapi.ICarEvent;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class IntellIgentEvent implements ICarEvent, CarIntelligentEngineManager.CarDrivingSceneListener {
    private static final String TAG = "IntellIgentEvent";
    static List<Integer> mEventIds;

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public String carManager() {
        return "intelligent";
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
    }

    public void onErrorEvent(int i, int i1) {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mEventIds = arrayList;
        arrayList.add(0);
        mEventIds.add(1);
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public List<Integer> eventIds() {
        return mEventIds;
    }

    public void onNRACtrl(int value) {
        EventBus.getDefault().post(new NRACtrlEvent(value));
    }

    public void onCarDrivingSceneChanged(CarSceneEvent carSceneEvent) {
        LogUtils.i(TAG, "onCarDrivingSceneChanged: " + carSceneEvent.getSceneAction());
        int sceneAction = carSceneEvent.getSceneAction();
        if (sceneAction == 0) {
            onNRACtrl(1);
        } else if (sceneAction != 1) {
        } else {
            onNRACtrl(0);
        }
    }
}
