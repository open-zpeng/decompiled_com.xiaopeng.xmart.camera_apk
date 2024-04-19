package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class s {

    /* loaded from: classes.dex */
    public static class a {
        public long a = 0;
        public long b = 0;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("aut", String.valueOf(aVar.a));
        hashMap.put("vut", String.valueOf(aVar.b));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2001, a(aVar));
    }
}
