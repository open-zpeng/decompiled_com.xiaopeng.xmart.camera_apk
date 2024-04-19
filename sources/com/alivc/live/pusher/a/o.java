package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import com.aliyun.clientinforeport.core.LogSender;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class o {

    /* loaded from: classes.dex */
    public static class a {
        public long P;
        public long Q;
        public long R;
        public long S;
        public long a = 0;
        public long b = 0;
        public float c = 0.0f;
        public float d = 0.0f;
        public int e = 0;
        public int f = 0;
        public int g = 0;
        public int h = 0;
        public int i = 0;
        public long j = 0;
        public long k = 0;
        public long l = 0;
        public long m = 0;
        public long n = 0;
        public long o = 0;
        public long p = 0;
        public long q = 0;
        public long r = 0;
        public long s = 0;
        public long t = 0;
        public long u = 0;
        public long v = 0;
        public long w = 0;
        public long x = 0;
        public long y = 0;
        public long z = 0;
        public long A = 0;
        public long B = 0;
        public long C = 0;
        public long D = 0;
        public long E = 0;
        public long F = 0;
        public long G = 0;
        public long H = 0;
        public long I = 0;
        public long J = 0;
        public int K = 0;
        public int L = 0;
        public int M = 0;
        public int N = 0;
        public int O = 0;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("ts", String.valueOf(aVar.a));
        hashMap.put(LogSender.KEY_TERMINAL_TYPE, String.valueOf(aVar.b));
        hashMap.put("cu", String.valueOf(aVar.c));
        hashMap.put("mu", String.valueOf(aVar.d));
        hashMap.put("vfirb", String.valueOf(aVar.e));
        hashMap.put("vfieb", String.valueOf(aVar.f));
        hashMap.put("vpiub", String.valueOf(aVar.g));
        hashMap.put("afieb", String.valueOf(aVar.h));
        hashMap.put("apiub", String.valueOf(aVar.i));
        hashMap.put("avpd", String.valueOf(aVar.j));
        hashMap.put("vef", String.valueOf(aVar.k));
        hashMap.put("vuf", String.valueOf(aVar.l));
        hashMap.put("vcf", String.valueOf(aVar.m));
        hashMap.put("tf", String.valueOf(aVar.n));
        hashMap.put("df", String.valueOf(aVar.o));
        hashMap.put("abf", String.valueOf(aVar.p));
        hashMap.put("vbf", String.valueOf(aVar.q));
        hashMap.put("auf", String.valueOf(aVar.r));
        hashMap.put("daf", String.valueOf(aVar.s));
        hashMap.put("aeb", String.valueOf(aVar.t));
        hashMap.put("veb", String.valueOf(aVar.u));
        hashMap.put("aub", String.valueOf(aVar.v));
        hashMap.put("vub", String.valueOf(aVar.w));
        hashMap.put("vepb", String.valueOf(aVar.x));
        hashMap.put("aut", String.valueOf(aVar.y));
        hashMap.put("vut", String.valueOf(aVar.z));
        hashMap.put("vpts", String.valueOf(aVar.G));
        hashMap.put("apts", String.valueOf(aVar.H));
        hashMap.put("vbpts", String.valueOf(aVar.I));
        hashMap.put("abpts", String.valueOf(aVar.J));
        hashMap.put("cvpts", String.valueOf(aVar.A));
        hashMap.put("capts", String.valueOf(aVar.B));
        hashMap.put("bavpts", String.valueOf(aVar.C));
        hashMap.put("baapts", String.valueOf(aVar.D));
        hashMap.put("aavpts", String.valueOf(aVar.E));
        hashMap.put("aaapts", String.valueOf(aVar.F));
        hashMap.put("ms", String.valueOf(aVar.K));
        hashMap.put("bmvb", String.valueOf(aVar.L));
        hashMap.put("bmab", String.valueOf(aVar.M));
        hashMap.put("bmsb", String.valueOf(aVar.N));
        hashMap.put("bmst", String.valueOf(aVar.O));
        hashMap.put("audioR", String.valueOf(aVar.P));
        hashMap.put("audioS", String.valueOf(aVar.Q));
        hashMap.put("audioSS", String.valueOf(aVar.R));
        hashMap.put("audioT", String.valueOf(aVar.S));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 9001, a(aVar));
    }
}
