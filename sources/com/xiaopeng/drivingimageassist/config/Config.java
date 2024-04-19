package com.xiaopeng.drivingimageassist.config;

import android.content.Context;
import android.util.Log;
import com.xiaopeng.drivingimageassist.DIGModule;
import com.xiaopeng.drivingimageassist.carapi.CarApiHelper;
import com.xiaopeng.drivingimageassist.displaycontrol.DisPlayControlHelper;
import com.xiaopeng.drivingimageassist.receiver.ReceiverHelper;
import com.xiaopeng.drivingimageassist.view.UIConfigManger;
/* loaded from: classes.dex */
public class Config {
    private static final String TAG = "DrivingImage_Config";
    private Params mParams;

    public Config(Params params, Context context) {
        this.mParams = params;
        CarApiHelper.instance().init(context, this.mParams);
        DisPlayControlHelper.instance().init(context, this.mParams);
        ReceiverHelper.instance().registerReceiver(context, this.mParams);
        DIGModule.instance().init(context);
        UIConfigManger.instance().init(this.mParams);
    }

    /* loaded from: classes.dex */
    public static final class Params {
        private boolean isLandscape;
        private boolean isSupportNRACtrl;
        private boolean isSupportTurnLamp;
        private boolean isUseCarServiceNRACtrl;

        public boolean isLandscape() {
            return this.isLandscape;
        }

        public boolean isSupportNRACtrl() {
            return this.isSupportNRACtrl;
        }

        public boolean isUseCarServiceNRACtrl() {
            return this.isUseCarServiceNRACtrl;
        }

        public boolean isSupportTurnLamp() {
            return this.isSupportTurnLamp;
        }

        public String toString() {
            return "Params{isLandscape=" + this.isLandscape + ", isSupportNRACtrl=" + this.isSupportNRACtrl + ", isSupportTurnLamp=" + this.isSupportTurnLamp + ", isUseCarServiceNRACtrl=" + this.isUseCarServiceNRACtrl + '}';
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        Params params = new Params();

        public Builder setLandscape(boolean landscape) {
            this.params.isLandscape = landscape;
            return this;
        }

        public Builder setSupportNRACtrl(boolean isSupportNRACtrl) {
            this.params.isSupportNRACtrl = isSupportNRACtrl;
            return this;
        }

        public Builder setUseCarServiceNRACtrl(boolean isUseCarServiceNRACtrl) {
            this.params.isUseCarServiceNRACtrl = isUseCarServiceNRACtrl;
            return this;
        }

        public Builder setSupportTurnLamp(boolean isSupportTurnLamp) {
            this.params.isSupportTurnLamp = isSupportTurnLamp;
            return this;
        }

        public Config build(Context context) {
            Log.i(Config.TAG, "build:" + this.params.toString());
            return new Config(this.params, context);
        }
    }
}
