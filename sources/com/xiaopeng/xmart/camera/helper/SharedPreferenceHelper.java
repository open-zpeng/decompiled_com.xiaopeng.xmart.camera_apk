package com.xiaopeng.xmart.camera.helper;

import android.content.Context;
import android.preference.PreferenceManager;
import com.xiaopeng.xmart.camera.App;
/* loaded from: classes.dex */
public class SharedPreferenceHelper {
    public static final String SHARED_SD_ERROR_NOT_REMIND = "shared_sd_error_not_remind";
    private Context mContext;

    private SharedPreferenceHelper() {
        this.mContext = App.getInstance();
    }

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        private static SharedPreferenceHelper INSTANCE = new SharedPreferenceHelper();

        private SingletonHolder() {
        }
    }

    public static SharedPreferenceHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void putString(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(key, defaultValue);
    }
}
