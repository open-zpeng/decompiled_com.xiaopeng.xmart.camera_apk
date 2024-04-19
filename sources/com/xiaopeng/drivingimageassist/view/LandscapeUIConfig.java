package com.xiaopeng.drivingimageassist.view;

import android.app.Activity;
import android.view.WindowManager;
import com.xiaopeng.drivingimageassist.R;
import com.xiaopeng.drivingimageassist.utils.CarHardwareUtils;
/* loaded from: classes.dex */
public class LandscapeUIConfig implements UIConfig {
    @Override // com.xiaopeng.drivingimageassist.view.UIConfig
    public int getLayout() {
        return R.layout.gid_main_layout;
    }

    @Override // com.xiaopeng.drivingimageassist.view.UIConfig
    public WindowManager.LayoutParams getLayoutParams(Activity activity) {
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.gravity = 21;
        attributes.dimAmount = 0.0f;
        return attributes;
    }

    @Override // com.xiaopeng.drivingimageassist.view.UIConfig
    public int getNRACtrlCameraType() {
        return CarHardwareUtils.isE28() ? 21 : 10;
    }
}
