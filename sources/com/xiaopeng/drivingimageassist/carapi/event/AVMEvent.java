package com.xiaopeng.drivingimageassist.carapi.event;

import android.car.hardware.CarPropertyValue;
import com.xiaopeng.drivingimageassist.carapi.ICarEvent;
import com.xiaopeng.drivingimageassist.event.AvmWorkEvent;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class AVMEvent implements ICarEvent {
    private static final String TAG = "AVMEvent";
    static List<Integer> mEventIds;
    private Runnable mTurnLampTask;

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public String carManager() {
        return CarClientWrapper.XP_AVM_SERVICE;
    }

    public void onErrorEvent(int i, int i1) {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mEventIds = arrayList;
        arrayList.add(557855760);
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public List<Integer> eventIds() {
        return mEventIds;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() != 557855760) {
            return;
        }
        int intValue = ((Integer) carPropertyValue.getValue()).intValue();
        LogUtils.i(TAG, "value:" + intValue);
        onAvmWork(intValue);
    }

    private void onAvmWork(int value) {
        EventBus.getDefault().post(new AvmWorkEvent(value));
    }
}
