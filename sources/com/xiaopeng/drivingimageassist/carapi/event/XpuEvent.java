package com.xiaopeng.drivingimageassist.carapi.event;

import android.car.hardware.CarPropertyValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaopeng.drivingimageassist.carapi.ICarEvent;
import com.xiaopeng.drivingimageassist.event.NEDCEvent;
import com.xiaopeng.drivingimageassist.event.NRABtnEvent;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.carmanager.CarClientWrapper;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class XpuEvent implements ICarEvent {
    private static final String TAG = "XpuEvent";
    static List<Integer> mEventIds;
    private static Gson sGson;
    private int mLastApnState = 0;

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public String carManager() {
        return CarClientWrapper.XP_XPU_SERVICE;
    }

    public void onErrorEvent(int i, int i1) {
    }

    static {
        ArrayList arrayList = new ArrayList();
        mEventIds = arrayList;
        arrayList.add(557856802);
        mEventIds.add(557856801);
        mEventIds.add(557856775);
        sGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override // com.xiaopeng.drivingimageassist.carapi.ICarEvent
    public List<Integer> eventIds() {
        return mEventIds;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        switch (carPropertyValue.getPropertyId()) {
            case 557856775:
                onNEDC(((Integer) carPropertyValue.getValue()).intValue());
                return;
            case 557856801:
                onNRABtn(((Integer) carPropertyValue.getValue()).intValue());
                return;
            case 557856802:
                LogUtils.i(TAG, "ID_XPU_NRA_CTRL" + ((Integer) carPropertyValue.getValue()).intValue());
                onNRACtrl(((Integer) carPropertyValue.getValue()).intValue());
                return;
            default:
                return;
        }
    }

    public void onNRACtrl(int value) {
        EventBus.getDefault().post(new NRACtrlEvent(value));
    }

    public void onNRABtn(int value) {
        EventBus.getDefault().post(new NRABtnEvent(value));
    }

    public void onNEDC(int value) {
        LogUtils.i(TAG, "ID_XPU_NEDC_STATUS:" + value);
        EventBus.getDefault().post(new NEDCEvent(value));
    }
}
