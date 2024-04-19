package com.xiaopeng.xmart.camera.bean;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class CameraFeedback extends FeedbackContent {
    @SerializedName("camera_360")
    private Camera360 camera360;
    @SerializedName("camera_status")
    private int cameraTop;
    private float camera_degree;
    private String camera_live_url;
    private int camera_os_switch;
    private int camera_type;
    private int fileStatus;
    private int live_push_status;

    public int getCamera_type() {
        return this.camera_type;
    }

    public void setCamera_type(int camera_type) {
        this.camera_type = camera_type;
    }

    public float getCamera_degree() {
        return this.camera_degree;
    }

    public void setCamera_degree(float camera_degree) {
        this.camera_degree = camera_degree;
    }

    public String getCamera_live_url() {
        return this.camera_live_url;
    }

    public void setCamera_live_url(String camera_live_url) {
        this.camera_live_url = camera_live_url;
    }

    public int getLive_push_status() {
        return this.live_push_status;
    }

    public void setLive_push_status(int live_push_status) {
        this.live_push_status = live_push_status;
    }

    public Camera360 getCamera360() {
        return this.camera360;
    }

    public void setCamera360(Camera360 camera360) {
        this.camera360 = camera360;
    }

    public void setCameraTop(int cameraTop) {
        this.cameraTop = cameraTop;
    }

    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }

    public int getCamera_os_switch() {
        return this.camera_os_switch;
    }

    public void setCamera_os_switch(int camera_os_switch) {
        this.camera_os_switch = camera_os_switch;
    }
}
