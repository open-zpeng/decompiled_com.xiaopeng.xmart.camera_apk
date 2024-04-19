package com.xiaopeng.drivingimageassist.carapi.event;

import android.car.hardware.CarPropertyValue;
import com.xiaopeng.drivingimageassist.carapi.ICarEvent;
import com.xiaopeng.drivingimageassist.event.TurnLampEvent;
import com.xiaopeng.drivingimageassist.utils.ThreadUtils;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class BCMEvent implements ICarEvent {
    private static final String TAG = "BCMEvent";
    static List<Integer> mEventIds;
    private Runnable mTurnLampTask;

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public String carManager() {
        return "xp_bcm";
    }

    public void onErrorEvent(int i, int i1) {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mEventIds = arrayList;
        arrayList.add(557915328);
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public List<Integer> eventIds() {
        return mEventIds;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() != 557915328) {
            return;
        }
        onTurnLamp((Integer[]) carPropertyValue.getValue());
    }

    private void onTurnLamp(Integer[] values) {
        for (int i = 0; i < values.length; i++) {
            LogUtils.i(TAG, "onTurnLamp[%d]:%d", Integer.valueOf(i), values[i]);
        }
        final TurnLampEvent turnLampEvent = new TurnLampEvent(values);
        Runnable runnable = this.mTurnLampTask;
        if (runnable != null) {
            ThreadUtils.removeWorker(runnable);
            this.mTurnLampTask = null;
        }
        if ((turnLampEvent.isLeftLamp() && turnLampEvent.isRightLamp()) || (!turnLampEvent.isLeftLamp() && !turnLampEvent.isRightLamp())) {
            turnLampEvent.setValues(new Integer[]{0, 0});
            Runnable runnable2 = new Runnable() { // from class: com.xiaopeng.drivingimageassist.carapi.event.BCMEvent.1
                @Override // java.lang.Runnable
                public void run() {
                    EventBus.getDefault().post(turnLampEvent);
                }
            };
            this.mTurnLampTask = runnable2;
            ThreadUtils.postWorker(runnable2, 1500L);
            return;
        }
        EventBus.getDefault().post(turnLampEvent);
    }
}
