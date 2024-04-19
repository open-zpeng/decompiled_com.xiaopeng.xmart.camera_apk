package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class a {

    /* renamed from: com.alivc.live.pusher.a.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0024a {
        public int a = 0;
        public int b = 0;
        public int c = 0;
        public int d = 0;
    }

    private static Map<String, String> a(C0024a c0024a) {
        HashMap hashMap = new HashMap();
        hashMap.put("oeb", String.valueOf(c0024a.a));
        hashMap.put("neb", String.valueOf(c0024a.b));
        hashMap.put("ceb", String.valueOf(c0024a.c));
        hashMap.put("cub", String.valueOf(c0024a.d));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, C0024a c0024a, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2014, a(c0024a));
    }
}
