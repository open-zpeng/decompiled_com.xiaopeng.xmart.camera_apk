package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcImageFormat {
    IMAGE_FORMAT_UNKNOW(-1),
    IMAGE_FORMAT_BGR(0),
    IMAGE_FORMAT_RGB(1),
    IMAGE_FORMAT_ARGB(2),
    IMAGE_FORMAT_BGRA(3),
    IMAGE_FORMAT_RGBA(4),
    IMAGE_FORMAT_YUV420P(5),
    IMAGE_FORMAT_YUVYV12(6),
    IMAGE_FORMAT_YUVNV21(7),
    IMAGE_FORMAT_YUVNV12(8),
    IMAGE_FORMAT_YUVJ420P(9),
    IMAGE_FORMAT_YUVJ420SP(10),
    IMAGE_FORMAT_YUVJ444P(11),
    IMAGE_FORMAT_YUV444P(12),
    IMAGE_FORMAT_EGL_IMG(13),
    IMAGE_FORMAT_TEXTURE_2D(14),
    IMAGE_FORMAT_TEXTURE_OES(15);
    
    private int mImageFormat;

    AlivcImageFormat(int i) {
        this.mImageFormat = i;
    }

    public int getAlivcImageFormat() {
        return this.mImageFormat;
    }
}
