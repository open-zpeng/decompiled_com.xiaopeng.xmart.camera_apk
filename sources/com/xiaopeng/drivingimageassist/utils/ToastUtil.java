package com.xiaopeng.drivingimageassist.utils;

import com.xiaopeng.xui.app.XToast;
/* loaded from: classes.dex */
public class ToastUtil {
    public static void showToast(final int resId) {
        com.xiaopeng.lib.utils.ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.drivingimageassist.utils.ToastUtil.1
            @Override // java.lang.Runnable
            public void run() {
                XToast.showShort(resId);
            }
        });
    }

    public static void showToast(final String toast) {
        com.xiaopeng.lib.utils.ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.drivingimageassist.utils.ToastUtil.2
            @Override // java.lang.Runnable
            public void run() {
                XToast.showShort(toast);
            }
        });
    }
}
