package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class j {

    /* loaded from: classes.dex */
    public static class a {
        public int a = 70;
        public int b = 40;
        public int c = 40;
        public int d = 30;
        public int e = 50;
        public int f = 15;
        public int g = 40;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("bcp", String.valueOf(aVar.f));
        hashMap.put("br", String.valueOf(aVar.g));
        hashMap.put("bsf", String.valueOf(aVar.e));
        hashMap.put("bbe", String.valueOf(aVar.d));
        hashMap.put("btf", String.valueOf(aVar.c));
        hashMap.put("bbu", String.valueOf(aVar.b));
        hashMap.put("bw", String.valueOf(aVar.a));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2005, a(aVar));
    }
}
