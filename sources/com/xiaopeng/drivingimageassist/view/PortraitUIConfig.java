package com.xiaopeng.drivingimageassist.view;

import android.app.Activity;
import android.view.WindowManager;
import com.xiaopeng.drivingimageassist.R;
/* loaded from: classes.dex */
public class PortraitUIConfig implements UIConfig {
    @Override // com.xiaopeng.drivingimageassist.view.UIConfig
    public int getNRACtrlCameraType() {
        return 21;
    }

    @Override // com.xiaopeng.drivingimageassist.view.UIConfig
    public int getLayout() {
        return R.layout.protrait_gid_main_layout;
    }

    @Override // com.xiaopeng.drivingimageassist.view.UIConfig
    public WindowManager.LayoutParams getLayoutParams(Activity activity) {
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.gravity = 48;
        attributes.dimAmount = 0.0f;
        return attributes;
    }
}
