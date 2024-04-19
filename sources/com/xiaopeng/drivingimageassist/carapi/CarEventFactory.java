package com.xiaopeng.drivingimageassist.carapi;

import com.xiaopeng.drivingimageassist.carapi.event.AVMEvent;
import com.xiaopeng.drivingimageassist.carapi.event.BCMEvent;
import com.xiaopeng.drivingimageassist.carapi.event.IntellIgentEvent;
import com.xiaopeng.drivingimageassist.carapi.event.MCUEvent;
import com.xiaopeng.drivingimageassist.carapi.event.VCUEvent;
import com.xiaopeng.drivingimageassist.carapi.event.XpuEvent;
import com.xiaopeng.drivingimageassist.config.Config;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class CarEventFactory {
    public static List<ICarEvent> createCarEvents(Config.Params params) {
        ArrayList arrayList = new ArrayList();
        if (params.isSupportNRACtrl()) {
            if (params.isUseCarServiceNRACtrl()) {
                arrayList.add(new IntellIgentEvent());
            } else {
                arrayList.add(new XpuEvent());
            }
        }
        if (params.isSupportTurnLamp()) {
            arrayList.add(new BCMEvent());
        }
        arrayList.add(new VCUEvent());
        arrayList.add(new MCUEvent());
        arrayList.add(new AVMEvent());
        return arrayList;
    }
}
