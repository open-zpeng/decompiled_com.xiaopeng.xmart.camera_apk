package com.alivc.component.capture.video;

import android.content.Context;
import android.hardware.Camera;
import com.alivc.component.capture.video.VideoPusher;
import com.alivc.live.pusher.LogUtil;
import com.alivc.live.pusher.b;
/* loaded from: classes.dex */
public class VideoPusherJNI {
    private long mNativeHandler;
    private VideoPusher mVideoPusher;
    private VideoPusher.VideoSourceListener mVideoPusherDataListener = new VideoPusher.VideoSourceListener() { // from class: com.alivc.component.capture.video.VideoPusherJNI.1
        @Override // com.alivc.component.capture.video.VideoPusher.VideoSourceListener
        public void onVideoFrame(byte[] bArr, long j, int i, int i2, int i3, int i4, int i5) {
            VideoPusherJNI.this.onData(bArr, j, i, i2, i3, i4, i5);
        }
    };
    private VideoPusher.VideoSourceTextureListener mVideoPusherTextureListener = new VideoPusher.VideoSourceTextureListener() { // from class: com.alivc.component.capture.video.VideoPusherJNI.2
        @Override // com.alivc.component.capture.video.VideoPusher.VideoSourceTextureListener
        public void onVideoFrame(long j, int i, int i2, int i3, int i4, int i5) {
            VideoPusherJNI.this.onTexture(j, i, i2, i3, i4, i5);
        }
    };

    public VideoPusherJNI(long j) {
        this.mNativeHandler = 0L;
        this.mVideoPusher = null;
        LogUtil.d("VideoPusherJNI", "ME ME ME, VideoPusherJNI construct");
        if (this.mVideoPusher == null) {
            VideoPusher videoPusher = new VideoPusher();
            this.mVideoPusher = videoPusher;
            videoPusher.setVideoSourceListener(this.mVideoPusherDataListener);
            this.mVideoPusher.setVideoSourceTextureListener(this.mVideoPusherTextureListener);
        }
        this.mNativeHandler = j;
    }

    public static String getSupportedFormats() {
        String str = null;
        for (Integer num : VideoPusher.getSupportedFormats()) {
            Integer valueOf = Integer.valueOf(b.e(num.intValue()));
            str = str == null ? valueOf.toString() : str + "," + valueOf.toString();
        }
        return str;
    }

    public static String getSupportedResolutions(int i) {
        String str = null;
        for (Camera.Size size : VideoPusher.getSupportedResolutions(i)) {
            str = (str == null ? new StringBuilder() : new StringBuilder().append(str).append(",")).append(size.width).append(",").append(size.height).toString();
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public native int onData(byte[] bArr, long j, int i, int i2, int i3, int i4, int i5);

    private native int onStarted();

    private native int onStopped();

    /* JADX INFO: Access modifiers changed from: private */
    public native int onTexture(long j, int i, int i2, int i3, int i4, int i5);

    public void destroy() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI destroy");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.destroy();
            this.mVideoPusher = null;
        }
        this.mNativeHandler = 0L;
    }

    public int getCurrentExposureCompensation() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI getCurrentExposureCompensation ");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.getCurrentExposure();
        }
        return 0;
    }

    public int getCurrentZoom() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI getCurrentZoom ");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.getCurrentZoom();
        }
        return 0;
    }

    public int getMaxExposure() {
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.getMaxExposure();
        }
        return 0;
    }

    public int getMaxZoom() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI getMaxZoom ");
        if (this.mVideoPusher != null) {
            LogUtil.d("VideoPusherJNI", "VideoPusherJNI getMaxZoom " + this.mVideoPusher.getMaxZoom());
            return this.mVideoPusher.getMaxZoom();
        }
        return 0;
    }

    public int getMinExposure() {
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.getMinExposure();
        }
        return 0;
    }

    public void getTransformMatrix(float[] fArr) {
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.getTransformMatrix(fArr);
        }
    }

    public long getVideoHandler() {
        return this.mNativeHandler;
    }

    public void init(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, Context context) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI init source " + i + ", widht " + i2 + ",height " + i3 + ", fps " + i4 + ", rotation " + i5);
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.init(i, i2, i3, i4, i5, i6, z, z2, context);
        }
    }

    public boolean isCapturing() {
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.isPreviewRunning();
        }
        return false;
    }

    public boolean isSupportAutoFocus() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI isSupportAutoFocus ");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.isSupportAutoFocus();
        }
        return false;
    }

    public boolean isSupportFlash() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI isSupportFlash ");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.isSupportFlash();
        }
        return false;
    }

    public void pause(boolean z) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI pause " + z);
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.pause(z);
        }
    }

    public int resume() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI resume");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            try {
                videoPusher.resume();
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void setAutoFocus(boolean z, float f, float f2) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI setAutoFocus " + z + ", x" + f + ", y" + f2);
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.setAutoFocus(z);
            if (f > 0.0f || f2 > 0.0f) {
                this.mVideoPusher.setFocus(f, f2);
            }
        }
    }

    public void setExposureCompensation(int i) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI setExposureCompensation " + i);
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.setExposure(i);
        }
    }

    public void setOrientation(int i) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI setOrientation");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            try {
                videoPusher.setOrientation(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setTorch(boolean z) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI setTorch " + z);
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.setFlashOn(z);
        }
    }

    public void setZoom(int i) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI setzoom " + i);
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.setZoom(i);
        }
    }

    public int start(int i) {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI start");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            try {
                videoPusher.start(i);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void stop() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI stop");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            videoPusher.stop();
        }
    }

    public void switchCamera() {
        LogUtil.d("VideoPusherJNI", "VideoPusherJNI switchCamera");
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            try {
                videoPusher.switchCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int updateTexImage() {
        VideoPusher videoPusher = this.mVideoPusher;
        if (videoPusher != null) {
            return videoPusher.updateTexImage();
        }
        return -1;
    }
}
