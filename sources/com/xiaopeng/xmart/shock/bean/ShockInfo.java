package com.xiaopeng.xmart.shock.bean;

import com.google.gson.annotations.Expose;
/* loaded from: classes2.dex */
public class ShockInfo {
    public int gSensor;
    @Expose(deserialize = false, serialize = false)
    public String picPath;
    @Expose(deserialize = false, serialize = false)
    public String videoPath;
    public String picUrl = "";
    public String videoUrl = "";

    public void clear() {
        this.picUrl = null;
        this.videoUrl = null;
        this.gSensor = 0;
    }

    public String toString() {
        return "picPath: " + this.picPath + ", videoPath: " + this.videoPath + ", videoUrl: " + this.videoUrl;
    }
}
