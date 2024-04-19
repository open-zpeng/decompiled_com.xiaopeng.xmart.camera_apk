package com.xiaopeng.xmart.camera.vm;
/* loaded from: classes.dex */
public interface IAvmViewModel {
    void changeSmallLargePreview();

    void changeToNormal();

    void changeToTransparent();

    boolean isAVMActived();

    void switchCamera(int direction);
}
