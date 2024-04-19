package com.xiaopeng.xmart.shock.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
/* loaded from: classes2.dex */
public class DateUtils {
    private static SimpleDateFormat sSimpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static synchronized String formatDate(long date) {
        String format;
        synchronized (DateUtils.class) {
            format = sSimpleFormat.format(Long.valueOf(date));
        }
        return format;
    }
}
