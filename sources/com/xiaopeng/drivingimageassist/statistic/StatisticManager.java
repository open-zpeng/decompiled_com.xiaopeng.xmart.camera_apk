package com.xiaopeng.drivingimageassist.statistic;

import android.text.TextUtils;
import com.xiaopeng.drivingimageassist.utils.GsonUtils;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.HashMap;
/* loaded from: classes.dex */
public class StatisticManager {
    private static final String TAG = "StatisticManager";

    public static void logWithNoParam(String eventName, String subEvent) {
        LogUtils.i(TAG, "LogWithNoParam {subEvent}:{subEvent}", eventName, subEvent);
        new StatisticDataBuilder(eventName).setSubEvent(subEvent).sendData();
    }

    public static void logWithOneParam(String eventName, String subEvent, String paramName, String paramValue) {
        LogUtils.i(TAG, "LogWithOneParam {subEvent}:{subEvent}", eventName, subEvent);
        new StatisticDataBuilder(eventName).setSubEvent(subEvent).addParam(paramName, paramValue).sendData();
    }

    /* loaded from: classes.dex */
    public static class StatisticDataBuilder {
        private static final String TAG = "StatisticDataBuilder";
        private String eventName;
        private HashMap<String, Object> mDataBuilder;

        public StatisticDataBuilder(String eventName) {
            this.mDataBuilder = new HashMap<>();
            this.eventName = eventName;
        }

        public StatisticDataBuilder(String eventName, HashMap<String, Object> dataBuilder) {
            this.mDataBuilder = new HashMap<>();
            this.eventName = eventName;
            this.mDataBuilder = dataBuilder;
        }

        public StatisticDataBuilder addParam(String key, String value) {
            this.mDataBuilder.put(key, value);
            return this;
        }

        public StatisticDataBuilder setSubEvent(String eventString) {
            this.mDataBuilder.put("event", eventString);
            return this;
        }

        public void sendData() {
            if (TextUtils.isEmpty(this.eventName)) {
                LogUtils.i(TAG, "SendData eventName is null!");
            } else if (!this.mDataBuilder.containsKey("event")) {
                LogUtils.i(TAG, "SendData 没有配置子事件名!");
            } else {
                StatisticUtils.log(this.eventName, this.mDataBuilder);
            }
        }

        private String getDataString() {
            return new StatisticDataBean(this.eventName, GsonUtils.obj2String(this.mDataBuilder)).toString();
        }
    }
}
