package com.xiaopeng.xmart.camera.bean;

import java.io.Serializable;
/* loaded from: classes.dex */
public class ControlMsg implements Serializable {
    private String action;
    private float cmdValue;
    private String fileUrl;

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public float getCmdValue() {
        return this.cmdValue;
    }

    public void setCmdValue(float cmdValue) {
        this.cmdValue = cmdValue;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
