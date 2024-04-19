package com.xiaopeng.xmart.camera.view.controlpanel;

import android.view.View;
/* loaded from: classes.dex */
public interface ICameraSwitchView {

    /* loaded from: classes.dex */
    public interface OnSwitchChangeListener {
        boolean onInterceptTabChange(View tabLayout, int index, boolean tabChange, boolean fromUser);

        void onTabChangeEnd(View tabLayout, int index, boolean tabChange, boolean fromUser);

        void onTabChangeStart(View tabLayout, int index, boolean tabChange, boolean fromUser);
    }

    void selectTab(boolean isPhoto);

    void setOnCameraModeSwitchListener(OnSwitchChangeListener listener);
}
