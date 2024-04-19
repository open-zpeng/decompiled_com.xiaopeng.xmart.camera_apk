package com.xiaopeng.drivingimageassist.carapi.event;

import android.car.hardware.CarPropertyValue;
import com.xiaopeng.drivingimageassist.carapi.ICarEvent;
import com.xiaopeng.drivingimageassist.event.GearLevelEvent;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class VCUEvent implements ICarEvent {
    static List<Integer> mEventIds;

    private void onSpeed(float values) {
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public String carManager() {
        return CarClientWrapper.XP_VCU_SERVICE;
    }

    public void onErrorEvent(int i, int i1) {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mEventIds = arrayList;
        arrayList.add(557847045);
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public List<Integer> eventIds() {
        return mEventIds;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        int propertyId = carPropertyValue.getPropertyId();
        if (propertyId == 557847045) {
            EventBus.getDefault().post(new GearLevelEvent(((Integer) carPropertyValue.getValue()).intValue()));
        } else if (propertyId != 559944229) {
        } else {
            onSpeed(((Float) carPropertyValue.getValue()).floatValue());
        }
    }
}
