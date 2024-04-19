package com.xiaopeng.drivingimageassist.carapi;

import android.car.hardware.CarEcuManager;
import java.util.List;
/* loaded from: classes.dex */
public interface ICarEvent extends CarEcuManager.CarEcuEventCallback {
    String carManager();

    List<Integer> eventIds();
}
