package com.xiaopeng.drivingimageassist.statistic;

import com.xiaopeng.drivingimageassist.utils.GsonUtils;
/* loaded from: classes.dex */
public class StatisticDataBean {
    private String eventName;
    private String statisticProperties;

    public StatisticDataBean(String eventName, String statisticProperties) {
        this.eventName = eventName;
        this.statisticProperties = statisticProperties;
    }

    public String toString() {
        return GsonUtils.obj2String(new Object[]{this.eventName, this.statisticProperties});
    }
}
