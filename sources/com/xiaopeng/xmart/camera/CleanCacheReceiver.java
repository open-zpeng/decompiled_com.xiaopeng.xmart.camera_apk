package com.xiaopeng.xmart.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import java.io.File;
/* loaded from: classes.dex */
public class CleanCacheReceiver extends BroadcastReceiver {
    private static final String CACHE_PATH = "/cache/image_manager_disk_cache";
    private static final String PACKAGE_DATA_PATH = "/data/data/";
    private static final String TAG = "CleanCacheReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String str = PACKAGE_DATA_PATH + App.getInstance().getApplicationContext().getPackageName();
        LogUtils.d(TAG, "RootPath: " + str);
        final File file = new File(str + CACHE_PATH);
        LogUtils.d(TAG, "FinalPath: " + file.getPath());
        ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.xmart.camera.CleanCacheReceiver.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (file.exists()) {
                        File[] listFiles = file.listFiles();
                        for (int i = 0; i < listFiles.length; i++) {
                            LogUtils.d(CleanCacheReceiver.TAG, "Deleting: " + listFiles[i].getName());
                            listFiles[i].delete();
                        }
                    }
                } catch (Exception e) {
                    LogUtils.w(CleanCacheReceiver.TAG, e.getMessage());
                }
            }
        });
    }
}
