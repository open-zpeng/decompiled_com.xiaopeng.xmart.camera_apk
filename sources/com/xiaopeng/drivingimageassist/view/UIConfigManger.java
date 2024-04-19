package com.xiaopeng.drivingimageassist.view;

import com.xiaopeng.drivingimageassist.config.Config;
/* loaded from: classes.dex */
public class UIConfigManger {
    private UIConfig mUIConfig;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        private static final UIConfigManger Instance = new UIConfigManger();

        private Holder() {
        }
    }

    public static final UIConfigManger instance() {
        return Holder.Instance;
    }

    public void init(Config.Params params) {
        if (params.isLandscape()) {
            this.mUIConfig = new LandscapeUIConfig();
        } else {
            this.mUIConfig = new PortraitUIConfig();
        }
    }

    public UIConfig getUIConfig() {
        return this.mUIConfig;
    }
}
