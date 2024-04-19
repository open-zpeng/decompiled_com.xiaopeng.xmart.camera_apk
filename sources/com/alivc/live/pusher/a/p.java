package com.alivc.live.pusher.a;

import android.content.Context;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.AlivcEventReporter;
import com.xiaopeng.xui.app.XDialogSystemType;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class p {

    /* loaded from: classes.dex */
    public static class a {
    }

    private static Map<String, String> a(a aVar) {
        return new HashMap();
    }

    public static void a(AlivcEventPublicParam alivcEventPublicParam, a aVar, Context context) {
        AlivcEventReporter.report(alivcEventPublicParam, XDialogSystemType.TYPE_SYSTEM_DIALOG, a(aVar));
    }
}
