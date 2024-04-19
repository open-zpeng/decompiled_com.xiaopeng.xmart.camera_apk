package com.xiaopeng.drivingimageassist.view;

import android.app.Activity;
import android.view.WindowManager;
/* loaded from: classes.dex */
public interface UIConfig {
    int getLayout();

    WindowManager.LayoutParams getLayoutParams(Activity activity);

    int getNRACtrlCameraType();
}
