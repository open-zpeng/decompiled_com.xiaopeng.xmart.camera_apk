package com.xiaopeng.xmart.camera.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;
/* loaded from: classes.dex */
public class MediaStoreUtils {
    private static final String TAG = "MediaStoreUtils";

    public static void scanFileAsync(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            Uri fromFile = Uri.fromFile(file);
            CameraLog.d(TAG, "scanFileAsync: " + fromFile);
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", fromFile));
        }
    }

    public static boolean insertMedia(Context context, String mediaPath, boolean isVideo) {
        ContentValues ensureFile = ensureFile(mediaPath, isVideo);
        if (ensureFile != null) {
            Uri insert = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ensureFile);
            CameraLog.d(TAG, "insertIntoMediaStore result: " + insert);
            if (insert != null) {
                scanFileAsync(context, mediaPath);
                return true;
            }
            return false;
        }
        return false;
    }

    private static ContentValues ensureFile(String mediaPath, boolean isVideo) {
        if (mediaPath != null) {
            File file = new File(mediaPath);
            if (file.exists()) {
                ContentValues contentValues = new ContentValues();
                if (isVideo) {
                    contentValues.put("_data", file.getAbsolutePath());
                    contentValues.put("title", file.getName());
                    contentValues.put("mime_type", "video/mp4");
                    return contentValues;
                }
                contentValues.put("_data", file.getAbsolutePath());
                contentValues.put("title", file.getName());
                contentValues.put("mime_type", "image/jpeg");
                return contentValues;
            }
        }
        return null;
    }
}
