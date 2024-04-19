package com.xiaopeng.drivingimageassist.statistic;

import android.text.TextUtils;
import com.xiaopeng.libtheme.ThemeManager;
import java.util.HashMap;
/* loaded from: classes.dex */
class MoleConfig {
    private static HashMap<String, String> sMolePageConfig = new HashMap<>();
    private static HashMap<String, String> sMoleButtonConfig = new HashMap<>();

    MoleConfig() {
    }

    static {
        sMolePageConfig.put(StatisticConstants.EVENT_DIG_ASSIST, "P10868");
        sMoleButtonConfig.put(StatisticConstants.DIG_OPEN, "B001");
        sMoleButtonConfig.put(StatisticConstants.DIG_CLOSE, "B002");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getMolePageConfig(String event) {
        String str = sMolePageConfig.get(event);
        return TextUtils.isEmpty(str) ? "page" : str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getMoleButtonConfig(String event) {
        String str = sMoleButtonConfig.get(event);
        return TextUtils.isEmpty(str) ? ThemeManager.AttributeSet.BUTTON : str;
    }
}
