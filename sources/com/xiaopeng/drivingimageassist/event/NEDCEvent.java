package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class NEDCEvent {
    private int value;

    public NEDCEvent(int value) {
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
        return "NEDCEvent{value=" + this.value + '}';
    }
}
