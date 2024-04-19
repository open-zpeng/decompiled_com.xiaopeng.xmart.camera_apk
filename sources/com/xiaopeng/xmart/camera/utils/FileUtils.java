package com.xiaopeng.xmart.camera.utils;

import android.os.Environment;
import android.text.TextUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import java.io.File;
import java.io.FileOutputStream;
/* loaded from: classes.dex */
public class FileUtils {
    public static final String BASE_PATH;
    public static final int BUCKET_ID_360;
    public static final int BUCKET_ID_FRONT;
    public static final int BUCKET_ID_SHOCK;
    public static final int BUCKET_ID_TOP;
    public static final String CAMERA_DRIVE_RECORDER = "car_recorder";
    public static final String CAMERA_DRIVE_RECORDER_CACHE = "car_recorder/cache";
    public static final String CAMERA_TOP = "camera_top";
    public static final String DIR_360 = "360Camera";
    public static final String DIR_360_FULL_PATH;
    public static final String DIR_CHASSISBUMP = "chassisbump";
    public static final String DIR_CHASSISBUMP_FULL_PATH;
    public static final String DIR_FRONT = "FrontCamera";
    public static final String DIR_FRONT_FULL_PATH;
    public static final String DIR_SHOCK = "shockCamera";
    public static final String DIR_SHOCK_CACHE = ".cache";
    public static final String DIR_SHOCK_CACHE_FULL_PATH;
    public static final String DIR_SHOCK_FULL_PATH;
    public static final String DIR_TOP_FULL_PATH;
    public static final String SENTRY_MODE_CHASSISBUMP_SUFFIX = "_chassisbump";
    public static final String SENTRY_MODE_COLLISION_SUFFIX = "_collision";
    private static final String TAG = "FileUtils";

    static {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        BASE_PATH = path;
        String str = path + File.separator + DIR_360;
        DIR_360_FULL_PATH = str;
        BUCKET_ID_360 = str.toLowerCase().hashCode();
        String str2 = path + File.separator + DIR_SHOCK;
        DIR_SHOCK_FULL_PATH = str2;
        BUCKET_ID_SHOCK = str2.toLowerCase().hashCode();
        DIR_SHOCK_CACHE_FULL_PATH = str2 + File.separator + DIR_SHOCK_CACHE;
        DIR_CHASSISBUMP_FULL_PATH = App.getInstance().getDataDir().getPath() + File.separator + DIR_CHASSISBUMP;
        String str3 = path + File.separator + CAMERA_TOP;
        DIR_TOP_FULL_PATH = str3;
        BUCKET_ID_TOP = str3.toLowerCase().hashCode();
        String str4 = path + File.separator + DIR_FRONT;
        DIR_FRONT_FULL_PATH = str4;
        BUCKET_ID_FRONT = str4.toLowerCase().hashCode();
    }

    public static boolean saveImage(byte[] data, String path) {
        FileOutputStream fileOutputStream;
        File file = new File(path);
        boolean z = false;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    fileOutputStream.write(data);
                    fileOutputStream.flush();
                } catch (Exception e) {
                    e = e;
                }
                try {
                    CameraLog.d(TAG, "save image to " + path, true);
                    com.xiaopeng.lib.utils.FileUtils.closeQuietly(fileOutputStream);
                    return true;
                } catch (Exception e2) {
                    e = e2;
                    z = true;
                    fileOutputStream2 = fileOutputStream;
                    e.printStackTrace();
                    if (file.exists()) {
                        file.delete();
                    }
                    CameraLog.d(TAG, "save image exception:" + e.getMessage(), true);
                    com.xiaopeng.lib.utils.FileUtils.closeQuietly(fileOutputStream2);
                    return z;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream2 = fileOutputStream;
                com.xiaopeng.lib.utils.FileUtils.closeQuietly(fileOutputStream2);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CameraLog.e(TAG, "deleteFile exception:" + e.getMessage());
        }
    }

    public static boolean invalidFile(String fileDir) {
        if (TextUtils.isEmpty(fileDir)) {
            return true;
        }
        return (fileDir.endsWith(CameraDefine.PIC_EXTENSION) || fileDir.endsWith(CameraDefine.VIDEO_EXTENSION)) ? false : true;
    }

    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }
}
