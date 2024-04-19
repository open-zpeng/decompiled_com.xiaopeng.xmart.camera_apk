package com.xiaopeng.drivingimageassist;

import android.content.Context;
import android.content.Intent;
import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.drivingimageassist.config.Config;
import com.xiaopeng.drivingimageassist.utils.CarHardwareUtils;
import com.xiaopeng.lib.framework.module.Module;
/* loaded from: classes.dex */
public class DrivingImageAssistModule {
    private static final String TAG = "Drivingimageassist_Module";
    private Config mConfig;
    private Context mContext;
    private boolean mIsNapaLoaded;
    private long mStartTime;

    /* loaded from: classes.dex */
    private static class Holder {
        private static final DrivingImageAssistModule Instance = new DrivingImageAssistModule();

        private Holder() {
        }
    }

    public static final DrivingImageAssistModule instance() {
        return Holder.Instance;
    }

    public void init(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        if (CarHardwareUtils.isH93()) {
            this.mConfig = new Config.Builder().setLandscape(CarHardwareUtils.isLandscape()).setSupportNRACtrl(CarHardwareUtils.isSupportNRACtrl()).setUseCarServiceNRACtrl(CarHardwareUtils.isUseCarServiceNRACtrl()).setSupportTurnLamp(CarHardwareUtils.isSupportTurnLamp()).build(this.mContext);
            Module.register(DataLogModuleEntry.class, new DataLogModuleEntry(this.mContext));
            startForeground();
        }
    }

    private void startForeground() {
        this.mContext.startForegroundService(new Intent(this.mContext, ForegroundService.class));
    }
}
