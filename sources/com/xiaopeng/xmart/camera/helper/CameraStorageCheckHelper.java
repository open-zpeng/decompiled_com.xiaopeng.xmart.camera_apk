package com.xiaopeng.xmart.camera.helper;

import android.os.Environment;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import java.io.File;
/* loaded from: classes.dex */
public class CameraStorageCheckHelper {
    private static final String TAG = "CameraStorageCheckHelper";

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        static CameraStorageCheckHelper sInstance = new CameraStorageCheckHelper();

        private SingletonHolder() {
        }
    }

    private CameraStorageCheckHelper() {
    }

    public static CameraStorageCheckHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public boolean checkAvailableStorage() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        CameraLog.d(TAG, "checkAvailableStorage path: " + externalStorageDirectory);
        if (externalStorageDirectory != null) {
            long freeSpace = externalStorageDirectory.getFreeSpace();
            CameraLog.i(TAG, "checkAvailableStorage free:" + freeSpace + ",limit is:" + CameraDefine.STORAGE_FREE_LIMIT, false);
            return freeSpace <= CameraDefine.STORAGE_FREE_LIMIT;
        }
        return false;
    }
}
