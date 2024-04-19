package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class NRACtrlEvent {
    private boolean fromKey;
    private int value;

    public NRACtrlEvent(int value) {
        this.value = value;
    }

    public NRACtrlEvent(int value, boolean fromKey) {
        this(value);
        this.fromKey = fromKey;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return "NRACtrlEvent{value=" + this.value + '}';
    }

    public boolean isFromKey() {
        return this.fromKey;
    }

    public void setFromKey(boolean fromKey) {
        this.fromKey = fromKey;
    }
}
