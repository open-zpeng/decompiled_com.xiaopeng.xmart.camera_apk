package com.xiaopeng.drivingimageassist.displaycontrol;

import android.content.Context;
import com.xiaopeng.drivingimageassist.config.Config;
/* loaded from: classes.dex */
public class DisPlayControlHelper {
    private static final String TAG = "DisPlayControlHelper";
    private IAppsDisplay mAppsDisplay;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        private static final DisPlayControlHelper Instance = new DisPlayControlHelper();

        private Holder() {
        }
    }

    public static final DisPlayControlHelper instance() {
        return Holder.Instance;
    }

    public void init(Context context, Config.Params params) {
        if (params.isLandscape()) {
            this.mAppsDisplay = new LandscapeAppsDisplay(context);
        } else {
            this.mAppsDisplay = new ProtraitAppsDisplay(context);
        }
    }

    public boolean isApDisplay() {
        return this.mAppsDisplay.isApDisplay();
    }

    public boolean isCameraDisplay() {
        return this.mAppsDisplay.isCameraDisplay();
    }
}
