package com.xiaopeng.drivingimageassist.carapi.event;

import android.car.hardware.CarPropertyValue;
import com.xiaopeng.drivingimageassist.carapi.ICarEvent;
import com.xiaopeng.drivingimageassist.event.IGStatusEvent;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class MCUEvent implements ICarEvent {
    static List<Integer> mEventIds;

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public String carManager() {
        return CarClientWrapper.XP_MCU_SERVICE;
    }

    public void onErrorEvent(int i, int i1) {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mEventIds = arrayList;
        arrayList.add(557847561);
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public List<Integer> eventIds() {
        return mEventIds;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() != 557847561) {
            return;
        }
        onIGStatusChanged(((Integer) carPropertyValue.getValue()).intValue());
    }

    private void onIGStatusChanged(int value) {
        LogUtils.i("MCUEvent", "onIGStatusChanged:" + value);
        EventBus.getDefault().post(new IGStatusEvent(value));
    }
}
