package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import com.aliyun.clientinforeport.core.LogSender;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class u {

    /* loaded from: classes.dex */
    public static class a {
        public long a = 0;
        public long b = 0;
        public long c = 0;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("ts", String.valueOf(aVar.a));
        hashMap.put(LogSender.KEY_TERMINAL_TYPE, String.valueOf(aVar.b));
        hashMap.put("cost", String.valueOf(aVar.c));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2010, a(aVar));
    }
}
