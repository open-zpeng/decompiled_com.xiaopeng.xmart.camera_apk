package com.alivc.component.capture;
/* loaded from: classes.dex */
public class ScreenVideoParam {
    private int cameraId;
    private int fps;
    private int height;
    private int maxZoom;
    private int pushHeight;
    private int pushWidth;
    private int rotation;
    private int width;
    private int minZoom = 0;
    private int currentZoom = 1;

    public ScreenVideoParam(int i, int i2, int i3, int i4, int i5) {
        this.rotation = 0;
        this.width = i;
        this.height = i2;
        this.fps = i3;
        this.cameraId = i4;
        this.rotation = i5;
    }

    public int getCameraId() {
        return this.cameraId;
    }

    public int getCurrentZoom() {
        return this.currentZoom;
    }

    public int getFps() {
        return this.fps;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMaxZoom() {
        return this.maxZoom;
    }

    public int getMinZoom() {
        return this.minZoom;
    }

    public int getPushHeight() {
        return this.pushHeight;
    }

    public int getPushWidth() {
        return this.pushWidth;
    }

    public int getRotation() {
        return this.rotation;
    }

    public int getWidth() {
        return this.width;
    }

    public void setCameraId(int i) {
        this.cameraId = i;
    }

    public void setCurrentZoom(int i) {
        this.currentZoom = i;
    }

    public void setFps(int i) {
        this.fps = i;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setMaxZoom(int i) {
        this.maxZoom = i;
    }

    public void setMinZoom(int i) {
        this.minZoom = i;
    }

    public void setPushHeight(int i) {
        this.pushHeight = i;
    }

    public void setPushWidth(int i) {
        this.pushWidth = i;
    }

    public void setRotation(int i) {
        this.rotation = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }
}
