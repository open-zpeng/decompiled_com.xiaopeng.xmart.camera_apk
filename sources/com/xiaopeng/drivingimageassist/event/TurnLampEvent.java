package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class TurnLampEvent {
    private Integer[] values;

    public TurnLampEvent(Integer[] values) {
        this.values = values;
    }

    public Integer[] getValues() {
        return this.values;
    }

    public void setValues(Integer[] values) {
        this.values = values;
    }

    public boolean isLeftLamp() {
        Integer[] numArr = this.values;
        return numArr != null && numArr.length >= 2 && numArr[0].intValue() == 1;
    }

    public boolean isRightLamp() {
        Integer[] numArr = this.values;
        return numArr != null && numArr.length >= 2 && numArr[1].intValue() == 1;
    }
}
