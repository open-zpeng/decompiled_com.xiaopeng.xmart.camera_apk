package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class AvmWorkEvent {
    private int value;

    public AvmWorkEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isWork() {
        return this.value == 1;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return "AvmWorkEvent{value=" + this.value + '}';
    }
}
