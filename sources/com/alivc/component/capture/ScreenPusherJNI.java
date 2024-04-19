package com.alivc.component.capture;

import android.content.Context;
import android.content.Intent;
import android.opengl.Matrix;
import android.view.Display;
import android.view.WindowManager;
import com.alivc.component.capture.ScreenPusher;
import com.alivc.live.pusher.LogUtil;
/* loaded from: classes.dex */
public class ScreenPusherJNI {
    private Context mContext;
    private long mNativeHandler;
    private int mRotation;
    private ScreenPusher mScreenPusher;
    private ScreenPusher.VideoSourceTextureListener mVideoPusherTextureListener = new ScreenPusher.VideoSourceTextureListener() { // from class: com.alivc.component.capture.ScreenPusherJNI.1
        @Override // com.alivc.component.capture.ScreenPusher.VideoSourceTextureListener
        public void onVideoFrame(long j, int i, int i2, int i3) {
            ScreenPusherJNI screenPusherJNI = ScreenPusherJNI.this;
            screenPusherJNI.onTexture(j, i, i2, screenPusherJNI.mRotation, i3);
        }
    };

    public ScreenPusherJNI(long j) {
        this.mNativeHandler = 0L;
        this.mScreenPusher = null;
        LogUtil.d("ScreenPusherJNI", "ME ME ME, ScreenPusherJNI construct");
        if (this.mScreenPusher == null) {
            ScreenPusher screenPusher = new ScreenPusher();
            this.mScreenPusher = screenPusher;
            screenPusher.setVideoSourceTextureListener(this.mVideoPusherTextureListener);
        }
        this.mNativeHandler = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public native int onTexture(long j, int i, int i2, int i3, int i4);

    public void destroy() {
        LogUtil.d("ScreenPusherJNI", "ScreenPusherJNI destroy");
        ScreenPusher screenPusher = this.mScreenPusher;
        if (screenPusher != null) {
            screenPusher.destroy();
            this.mScreenPusher = null;
        }
        this.mContext = null;
        this.mNativeHandler = 0L;
    }

    public long getScreenHandler() {
        return this.mNativeHandler;
    }

    public void getTransformMatrix(float[] fArr) {
        Display defaultDisplay;
        int rotation;
        ScreenPusher screenPusher = this.mScreenPusher;
        if (screenPusher == null || fArr == null) {
            return;
        }
        screenPusher.getTransformMatrix(fArr);
        Context context = this.mContext;
        if (context == null || (defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay()) == null || !((rotation = defaultDisplay.getRotation()) == 1 || rotation == 3)) {
            int i = this.mRotation;
            if (i == 90) {
                Matrix.rotateM(fArr, 0, 90.0f, 0.0f, 0.0f, 1.0f);
                Matrix.translateM(fArr, 0, 0.0f, -1.0f, 0.0f);
            } else if (i == 270) {
                Matrix.rotateM(fArr, 0, 270.0f, 0.0f, 0.0f, 1.0f);
                Matrix.translateM(fArr, 0, -1.0f, 0.0f, 0.0f);
            }
        }
    }

    public void init(int i, int i2, int i3, int i4, int i5, Intent intent, Context context) {
        LogUtil.d("ScreenPusherJNI", "ScreenPusherJNI init source " + i + ", widht " + i2 + ",height " + i3 + ", fps " + i4);
        ScreenPusher screenPusher = this.mScreenPusher;
        if (screenPusher != null) {
            this.mRotation = i5;
            this.mContext = context;
            screenPusher.init(i, i2, i3, i4, i5, intent, context);
        }
    }

    public boolean isCapturing() {
        return this.mScreenPusher != null;
    }

    public int start(int i) {
        LogUtil.d("ScreenPusherJNI", "ScreenPusherJNI start");
        ScreenPusher screenPusher = this.mScreenPusher;
        if (screenPusher != null) {
            try {
                screenPusher.start(i);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void stop() {
        LogUtil.d("ScreenPusherJNI", "ScreenPusherJNI stop");
        ScreenPusher screenPusher = this.mScreenPusher;
        if (screenPusher != null) {
            screenPusher.stop();
        }
    }

    public int updateTexImage() {
        ScreenPusher screenPusher = this.mScreenPusher;
        if (screenPusher != null) {
            return screenPusher.updateTexImage();
        }
        return -1;
    }
}
