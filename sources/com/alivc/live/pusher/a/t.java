package com.alivc.live.pusher.a;

import android.content.Context;
import com.alivc.live.pusher.AlivcLivePushConstants;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class t {

    /* loaded from: classes.dex */
    public static class a {
        public long a = 0;
        public long b = 0;
        public String c = null;
        public String d = "dual";
        public boolean e = false;
        public boolean f = false;
        public int g = 0;
        public boolean h = false;
        public int i = 20;
        public int j = 0;
        public int k = 0;
        public int l = 0;
        public int m = AlivcLivePushConstants.DEFAULT_VALUE_INT_AUDIO_SAMPLE_RATE;
        public int n = 0;
        public int o = 0;
        public boolean p = true;
        public int q = 70;
        public int r = 40;
        public int s = 40;
        public int t = 40;
        public int u = 50;
        public int v = 30;
        public int w = 15;
        public boolean x = true;
        public int y = 5;
        public int z = 0;
        public boolean A = true;
        public int B = 3;
        public int C = 5;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("aut", String.valueOf(aVar.a));
        hashMap.put("vut", String.valueOf(aVar.b));
        hashMap.put("resolution", String.valueOf(aVar.c));
        hashMap.put("st", String.valueOf(aVar.d));
        hashMap.put("ao", String.valueOf(aVar.e));
        hashMap.put("he", String.valueOf(aVar.f));
        hashMap.put("wc", String.valueOf(aVar.g));
        hashMap.put("fps", String.valueOf(aVar.i));
        hashMap.put("ivb", String.valueOf(aVar.j));
        hashMap.put("mavb", String.valueOf(aVar.k));
        hashMap.put("mivb", String.valueOf(aVar.l));
        hashMap.put("asr", String.valueOf(aVar.m));
        hashMap.put("pum", String.valueOf(aVar.h));
        hashMap.put("po", String.valueOf(aVar.n));
        hashMap.put("ct", String.valueOf(aVar.o));
        hashMap.put("beauty", String.valueOf(aVar.p));
        hashMap.put("bw", String.valueOf(aVar.q));
        hashMap.put("bbu", String.valueOf(aVar.r));
        hashMap.put("br", String.valueOf(aVar.s));
        hashMap.put("bcp", String.valueOf(aVar.w));
        hashMap.put("btf", String.valueOf(aVar.t));
        hashMap.put("bsf", String.valueOf(aVar.u));
        hashMap.put("bbe", String.valueOf(aVar.v));
        hashMap.put("flash", String.valueOf(aVar.x));
        hashMap.put("crmc", String.valueOf(aVar.y));
        hashMap.put("cri", String.valueOf(aVar.z));
        hashMap.put("prm", String.valueOf(aVar.A));
        hashMap.put("gop", String.valueOf(aVar.B));
        hashMap.put("utm", String.valueOf(aVar.C));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2004, a(aVar));
    }
}
