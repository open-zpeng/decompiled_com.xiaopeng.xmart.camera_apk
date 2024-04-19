package com.xiaopeng.xmart.camera.carmanager;

import com.xiaopeng.xmart.camera.carmanager.IBaseCallback;
/* loaded from: classes.dex */
public interface IBaseCarController<T extends IBaseCallback> {
    void registerCallback(T callback);

    void unregisterCallback(T callback);
}
