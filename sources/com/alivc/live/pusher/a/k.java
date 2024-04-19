package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class k {

    /* loaded from: classes.dex */
    public static class a {
        public int a = 0;
        public int b = 0;
        public int c = 0;
        public int d = 0;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("vpiubbd", String.valueOf(aVar.a));
        hashMap.put("vpiubad", String.valueOf(aVar.b));
        hashMap.put("apiubbd", String.valueOf(aVar.c));
        hashMap.put("apiubad", String.valueOf(aVar.d));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2013, a(aVar));
    }
}
