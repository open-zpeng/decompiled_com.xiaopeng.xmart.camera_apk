package com.xiaopeng.drivingimageassist.statistic;

import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.lib.framework.module.IModuleEntry;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IMoleEvent;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IMoleEventBuilder;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import java.util.HashMap;
/* loaded from: classes.dex */
public class StatisticUtils {
    private static final String TAG = "StatisticUtils";

    public static void log(final String eventName, final HashMap<String, Object> properties) {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.drivingimageassist.statistic.StatisticUtils.1
            @Override // java.lang.Runnable
            public void run() {
                IModuleEntry iModuleEntry = Module.get(DataLogModuleEntry.class);
                if (iModuleEntry == null) {
                    return;
                }
                IDataLog iDataLog = (IDataLog) iModuleEntry.get(IDataLog.class);
                IMoleEventBuilder buildMoleEvent = iDataLog.buildMoleEvent();
                buildMoleEvent.setModule(StatisticConstants.MODEL_DIG);
                buildMoleEvent.setEvent(eventName);
                buildMoleEvent.setPageId(MoleConfig.getMolePageConfig(eventName));
                buildMoleEvent.setButtonId(MoleConfig.getMoleButtonConfig((String) properties.get("event")));
                for (String str : properties.keySet()) {
                    StatisticUtils.putProperty(buildMoleEvent, str, properties.get(str));
                }
                IMoleEvent build = buildMoleEvent.build();
                iDataLog.sendStatData(build);
                LogUtils.i(StatisticUtils.TAG, "data log event(hashMap) : %s", build.toJson());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void putProperty(IMoleEventBuilder builder, String key, Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof String) {
            builder.setProperty(key, (String) object);
        } else if (object instanceof Number) {
            builder.setProperty(key, (Number) object);
        } else if (object instanceof Boolean) {
            builder.setProperty(key, ((Boolean) object).booleanValue());
        } else if (object instanceof Character) {
            builder.setProperty(key, ((Character) object).charValue());
        }
    }
}
