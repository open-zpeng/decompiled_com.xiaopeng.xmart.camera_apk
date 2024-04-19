package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class x {

    /* loaded from: classes.dex */
    public static class a {
        public String a = AccountConfig.FaceIDRegisterAction.ORIENTATION_FRONT;
    }

    private static Map<String, String> a(a aVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("ct", String.valueOf(aVar.a.equals(AccountConfig.FaceIDRegisterAction.ORIENTATION_FRONT) ? 1 : 0));
        return hashMap;
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, 2012, a(aVar));
    }
}
