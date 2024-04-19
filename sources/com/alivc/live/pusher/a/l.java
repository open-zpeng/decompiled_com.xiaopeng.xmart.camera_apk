package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import com.aliyun.clientinforeport.core.LogSender;
import com.xiaopeng.lib.framework.moduleinterface.appresourcemodule.IAppResourceException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class l {

    /* loaded from: classes.dex */
    public static class a {
        public long a = 0;
        public String b = "";
        public long c = 0;
        public long d = 0;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("error_code", String.valueOf(aVar.a));
        hashMap.put("error_msg", String.valueOf(aVar.b));
        hashMap.put("ts", String.valueOf(aVar.c));
        hashMap.put(LogSender.KEY_TERMINAL_TYPE, String.valueOf(aVar.d));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, IAppResourceException.REASON_BINDER_FAILED, a(aVar));
    }
}
