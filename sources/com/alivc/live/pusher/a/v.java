package com.alivc.live.pusher.a;

import android.content.Context;
import com.alivc.live.pusher.AlivcLivePushConstants;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class v {

    /* loaded from: classes.dex */
    public static class a {
        public long a = 0;
        public long b = 0;
        public String c = null;
        public String d = "dual";
        public boolean e = false;
        public boolean f = false;
        public boolean g = false;
        public int h = 0;
        public boolean i = false;
        public int j = 20;
        public int k = 0;
        public int l = 0;
        public int m = 0;
        public int n = AlivcLivePushConstants.DEFAULT_VALUE_INT_AUDIO_SAMPLE_RATE;
        public int o = 0;
        public int p = 0;
        public boolean q = true;
        public int r = 15;
        public int s = 70;
        public int t = 40;
        public int u = 40;
        public int v = 40;
        public int w = 50;
        public int x = 30;
        public boolean y = true;
        public int z = 5;
        public int A = 0;
        public boolean B = true;
        public int C = 3;
        public int D = 5;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("aut", String.valueOf(aVar.a));
        hashMap.put("vut", String.valueOf(aVar.b));
        hashMap.put("resolution", String.valueOf(aVar.c));
        hashMap.put("st", String.valueOf(aVar.d));
        hashMap.put("ao", String.valueOf(aVar.e ? 1 : 0));
        hashMap.put("vo", String.valueOf(aVar.f ? 1 : 0));
        hashMap.put("he", String.valueOf(aVar.g ? 1 : 0));
        hashMap.put("wc", String.valueOf(aVar.h));
        hashMap.put("pum", String.valueOf(aVar.i ? 1 : 0));
        hashMap.put("fps", String.valueOf(aVar.j));
        hashMap.put("ivb", String.valueOf(aVar.k));
        hashMap.put("mavb", String.valueOf(aVar.l));
        hashMap.put("mivb", String.valueOf(aVar.m));
        hashMap.put("asr", String.valueOf(aVar.n));
        hashMap.put("po", String.valueOf(aVar.o));
        hashMap.put("ct", String.valueOf(aVar.p));
        hashMap.put("beauty", String.valueOf(aVar.q ? 1 : 0));
        hashMap.put("bw", String.valueOf(aVar.s));
        hashMap.put("bbu", String.valueOf(aVar.t));
        hashMap.put("br", String.valueOf(aVar.u));
        hashMap.put("bcp", String.valueOf(aVar.r));
        hashMap.put("btf", String.valueOf(aVar.v));
        hashMap.put("bsf", String.valueOf(aVar.w));
        hashMap.put("bbe", String.valueOf(aVar.x));
        hashMap.put("flash", String.valueOf(aVar.y ? 1 : 0));
        hashMap.put("crmc", String.valueOf(aVar.z));
        hashMap.put("cri", String.valueOf(aVar.A));
        hashMap.put("prm", String.valueOf(aVar.B ? 1 : 0));
        hashMap.put("gop", String.valueOf(aVar.C));
        hashMap.put("utm", String.valueOf(aVar.D));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2001, a(aVar));
    }
}
