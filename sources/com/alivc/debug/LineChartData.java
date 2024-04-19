package com.alivc.debug;
/* loaded from: classes.dex */
public class LineChartData {
    private String time;
    private long value;

    public LineChartData(String str, long j) {
        this.time = str;
        this.value = j;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(int i) {
        this.value = i;
    }
}
