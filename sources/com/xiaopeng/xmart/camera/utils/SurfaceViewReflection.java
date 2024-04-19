package com.xiaopeng.xmart.camera.utils;

import android.os.Build;
import android.view.SurfaceView;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public class SurfaceViewReflection {
    public static void setRoundCorner(SurfaceView surfaceView) {
        if (Build.VERSION.SDK_INT < 29) {
            if (surfaceView.getResources().getConfiguration().orientation == 1) {
                try {
                    SurfaceView.class.getMethod("setRoundCorner", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(surfaceView, 5, 0, 20);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else if (surfaceView.getResources().getConfiguration().orientation == 2) {
            setCornerRadius(surfaceView, 20.0f);
        }
    }

    private static void setCornerRadius(final SurfaceView view, float cornerRadius) {
        try {
            SurfaceView.class.getMethod("setCornerRadius", Float.TYPE).invoke(view, Float.valueOf(cornerRadius));
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.getHolder().setFormat(-3);
    }
}
