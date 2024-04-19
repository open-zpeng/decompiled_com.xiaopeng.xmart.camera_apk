package com.aliyun.clientinforeport;

import com.aliyun.clientinforeport.core.LogSender;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class AlivcEventReporter {
    static ExecutorService threadService = Executors.newFixedThreadPool(5);

    public static String getSdkVersion() {
        return BuildConfig.SDK_VERSION;
    }

    public static void report(final AlivcEventPublicParam alivcEventPublicParam, final int i, final Map<String, String> map) {
        threadService.execute(new Runnable() { // from class: com.aliyun.clientinforeport.AlivcEventReporter.1
            @Override // java.lang.Runnable
            public void run() {
                LogSender.sendActually(AlivcEventPublicParam.this, i, map);
            }
        });
    }
}
