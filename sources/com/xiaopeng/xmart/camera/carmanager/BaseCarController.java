package com.xiaopeng.xmart.camera.carmanager;

import android.car.Car;
import android.car.hardware.CarEcuManager;
import android.car.hardware.CarPropertyValue;
import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public abstract class BaseCarController<C extends CarEcuManager, T extends IBaseCallback> implements IBaseCarController<T> {
    protected C mCarManager;
    protected final Object mCallbackLock = new Object();
    protected final CopyOnWriteArrayList<T> mCallbacks = new CopyOnWriteArrayList<>();
    protected final ConcurrentHashMap<Integer, CarPropertyValue<?>> mCarPropertyMap = new ConcurrentHashMap<>();
    protected final List<Integer> mPropertyIds = getRegisterPropertyIds();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void disconnect();

    protected abstract List<Integer> getRegisterPropertyIds();

    protected abstract void handleEventsUpdate(CarPropertyValue<?> value);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void initCarManager(Car carClient);

    @Override // com.xiaopeng.xmart.camera.carmanager.IBaseCarController
    public final void registerCallback(T callback) {
        if (callback != null) {
            this.mCallbacks.add(callback);
        }
    }

    @Override // com.xiaopeng.xmart.camera.carmanager.IBaseCarController
    public final void unregisterCallback(T callback) {
        this.mCallbacks.remove(callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleCarEventsUpdate(final CarPropertyValue<?> value) {
        this.mCarPropertyMap.put(Integer.valueOf(value.getPropertyId()), value);
        handleEventsUpdate(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int getIntProperty(int propertyId) throws Exception {
        return ((Integer) getValue(getCarProperty(propertyId))).intValue();
    }

    protected final int[] getIntArrayProperty(int propertyId) throws Exception {
        return getIntArrayProperty(getCarProperty(propertyId));
    }

    protected final int[] getIntArrayProperty(CarPropertyValue<?> value) {
        Object[] objArr = (Object[]) getValue(value);
        if (objArr != null) {
            int[] iArr = new int[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                Object obj = objArr[i];
                if (obj instanceof Integer) {
                    iArr[i] = ((Integer) obj).intValue();
                }
            }
            return iArr;
        }
        return null;
    }

    protected final float getFloatProperty(int propertyId) throws Exception {
        return ((Float) getValue(getCarProperty(propertyId))).floatValue();
    }

    protected final float[] getFloatArrayProperty(int propertyId) throws Exception {
        return getFloatArrayProperty(getCarProperty(propertyId));
    }

    protected final float[] getFloatArrayProperty(CarPropertyValue<?> value) {
        Object[] objArr = (Object[]) getValue(value);
        if (objArr != null) {
            float[] fArr = new float[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                Object obj = objArr[i];
                if (obj instanceof Float) {
                    fArr[i] = ((Float) obj).floatValue();
                }
            }
            return fArr;
        }
        return null;
    }

    private CarPropertyValue<?> getCarProperty(int propertyId) throws Exception {
        CarPropertyValue<?> carPropertyValue = this.mCarPropertyMap.get(Integer.valueOf(propertyId));
        if (carPropertyValue != null) {
            return carPropertyValue;
        }
        throw new Exception("Car property not found");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final <E> E getValue(CarPropertyValue<?> value) {
        return (E) value.getValue();
    }
}
