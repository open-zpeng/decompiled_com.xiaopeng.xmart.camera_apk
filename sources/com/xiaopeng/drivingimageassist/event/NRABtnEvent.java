package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class NRABtnEvent {
    private int value;

    public NRABtnEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isOn() {
        return this.value == 1;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return "NRABtnEvent{value=" + this.value + '}';
    }
}
