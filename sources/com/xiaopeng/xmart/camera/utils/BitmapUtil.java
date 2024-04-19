package com.xiaopeng.xmart.camera.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/* loaded from: classes.dex */
public class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    public static Bitmap createWatermarkLeftBottom(Bitmap bitmapOrig, Bitmap bitmapWatermark, int paddingLeft, int paddingBottom) {
        return createWatermark(bitmapOrig, bitmapWatermark, paddingLeft, (bitmapOrig.getHeight() - bitmapWatermark.getHeight()) - paddingBottom);
    }

    private static Bitmap createWatermark(Bitmap bitmapOrig, Bitmap bitmapWatermark, int left, int top) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmapOrig.getWidth(), bitmapOrig.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmapOrig, 0.0f, 0.0f, paint);
        CameraLog.d(TAG, "waterMark width: " + bitmapWatermark.getWidth() + "  height: " + bitmapWatermark.getHeight(), false);
        canvas.drawBitmap(bitmapWatermark, left, top, paint);
        canvas.save();
        canvas.restore();
        return createBitmap;
    }
}
