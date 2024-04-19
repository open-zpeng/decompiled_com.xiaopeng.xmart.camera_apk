package com.xiaopeng.xmart.camera.settings;

import android.app.Application;
import android.text.TextUtils;
import com.xiaopeng.lib.utils.config.EnvConfig;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.lib.utils.info.DeviceInfoUtils;
import com.xiaopeng.xmart.camera.speech.ISpeechContants;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xvs.xid.XId;
import com.xiaopeng.xvs.xid.sync.api.OnSyncChangedListener;
import com.xiaopeng.xvs.xid.sync.api.SyncType;
import java.util.HashMap;
/* loaded from: classes.dex */
public class SettingsModel implements OnSyncChangedListener {
    private static final String TAG = "SettingsModel";
    private SettngsChangedListener mListener;
    private HashMap<String, String> mSettingValues;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Holder {
        private static final SettingsModel INSTANCE = new SettingsModel();

        private Holder() {
        }
    }

    public static SettingsModel getInstance() {
        return Holder.INSTANCE;
    }

    private SettingsModel() {
        this.mSettingValues = new HashMap<>();
    }

    public void init(Application context) {
        XId.init(context, XId.APP_ID_CAR_CAMERA, getAppSecret(), getCarType());
        XId.getSyncApi().init();
        XId.getSyncApi().setSyncChangedListener(this, true);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.OnSyncChangedListener
    public void OnSyncChanged(final long uid, SyncType syncType) {
        try {
            CameraLog.i(TAG, "OnSyncChanged event:" + uid, false);
            String transBody = XId.getSyncApi().getCarCameraSync().getTransBody("1");
            CameraLog.i(TAG, "OnSyncChanged transBody:" + transBody, false);
            String transChassis = XId.getSyncApi().getCarCameraSync().getTransChassis("1");
            CameraLog.i(TAG, "OnSyncChanged transClassis:" + transChassis, false);
            String bevMode = XId.getSyncApi().getCarCameraSync().getBevMode("1");
            CameraLog.i(TAG, "OnSyncChanged bevModel:" + bevMode, false);
            this.mSettingValues.put("CAMERA_KEY_TRANS_BODY", transBody);
            this.mSettingValues.put("CAMERA_KEY_TRANS_CHASSIS", transChassis);
            this.mSettingValues.put("CAMERA_KEY_BEV_MODE", bevMode);
            notifyChanged();
        } catch (Exception e) {
            CameraLog.e(TAG, "OnSyncChanged error:" + e, false);
        }
    }

    public void notifyChanged() {
        if (this.mListener != null) {
            this.mListener.onSettngsChanged(getBooleanValue("CAMERA_KEY_TRANS_CHASSIS"), getBooleanValue("CAMERA_KEY_TRANS_BODY"), getBooleanValue("CAMERA_KEY_BEV_MODE"));
        }
    }

    public void setSettingsChangedListener(SettngsChangedListener listener) {
        this.mListener = listener;
    }

    public void setBooleanValue(String key, boolean value) {
        String str = value ? "1" : ISpeechContants.SCREEN_ID_MAIN;
        this.mSettingValues.put(key, str);
        key.hashCode();
        char c = 65535;
        switch (key.hashCode()) {
            case -2100374701:
                if (key.equals("CAMERA_KEY_TRANS_BODY")) {
                    c = 0;
                    break;
                }
                break;
            case 1746573001:
                if (key.equals("CAMERA_KEY_BEV_MODE")) {
                    c = 1;
                    break;
                }
                break;
            case 1800050357:
                if (key.equals("CAMERA_KEY_TRANS_CHASSIS")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                XId.getSyncApi().getCarCameraSync().putTransBody(str);
                return;
            case 1:
                XId.getSyncApi().getCarCameraSync().putBevMode(str);
                return;
            case 2:
                XId.getSyncApi().getCarCameraSync().putTransChassis(str);
                return;
            default:
                return;
        }
    }

    public boolean getBooleanValue(String key) {
        return "1".equals(this.mSettingValues.get(key));
    }

    private String getAppSecret() {
        return (EnvConfig.getString("main_host", null) == null && !BuildInfoUtils.isLanVersion()) ? SettingsConstants.SECRET_PUB : "63pKXfSuc8QLvuno";
    }

    public String getCarType() {
        String productModel = DeviceInfoUtils.getProductModel();
        return TextUtils.isEmpty(productModel) ? "" : productModel.toUpperCase().replaceAll(" ", "");
    }
}
