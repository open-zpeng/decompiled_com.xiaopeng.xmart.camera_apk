package com.xiaopeng.xmart.camera.utils;

import com.xiaopeng.xui.app.XToast;
/* loaded from: classes.dex */
public class ToastUtils {
    public static void showToast(final int res) {
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.utils.ToastUtils.1
            @Override // java.lang.Runnable
            public void run() {
                XToast.show(res);
            }
        });
    }

    public static void showToast(String str) {
        XToast.show(str);
    }
}
