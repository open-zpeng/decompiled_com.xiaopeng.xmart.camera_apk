package com.ta.utdid2.a;

import android.content.Context;
import android.util.Log;
import com.ta.utdid2.b.a.f;
import com.ta.utdid2.b.a.i;
import com.ta.utdid2.b.a.j;
/* compiled from: AidManager.java */
/* loaded from: classes.dex */
public class a {
    private static final String TAG = "com.ta.utdid2.a.a";
    private static a a;
    private Context mContext;

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (a == null) {
                a = new a(context);
            }
            aVar = a;
        }
        return aVar;
    }

    private a(Context context) {
        this.mContext = context;
    }

    public void a(String str, String str2, String str3, com.ut.device.a aVar) {
        if (aVar == null) {
            Log.e(TAG, "callback is null!");
        } else if (this.mContext == null || i.m90a(str) || i.m90a(str2)) {
            Log.e(TAG, "mContext:" + this.mContext + "; callback:" + aVar + "; has appName:" + (!i.m90a(str)) + "; has token:" + (!i.m90a(str2)));
            aVar.a(1002, "");
        } else {
            String m87a = c.m87a(this.mContext, str, str2);
            if (!i.m90a(m87a) && j.a(c.a(this.mContext, str, str2), 1)) {
                aVar.a(1001, m87a);
            } else if (f.m89a(this.mContext)) {
                b.a(this.mContext).a(str, str2, str3, m87a, aVar);
            } else {
                aVar.a(1003, m87a);
            }
        }
    }

    public String a(String str, String str2, String str3) {
        if (this.mContext == null || i.m90a(str) || i.m90a(str2)) {
            Log.e(TAG, "mContext:" + this.mContext + "; has appName:" + (!i.m90a(str)) + "; has token:" + (!i.m90a(str2)));
            return "";
        }
        String m87a = c.m87a(this.mContext, str, str2);
        return ((i.m90a(m87a) || !j.a(c.a(this.mContext, str, str2), 1)) && f.m89a(this.mContext)) ? b(str, str2, str3) : m87a;
    }

    private synchronized String b(String str, String str2, String str3) {
        Context context = this.mContext;
        if (context == null) {
            Log.e(TAG, "no context!");
            return "";
        }
        String a2 = f.m89a(context) ? b.a(this.mContext).a(str, str2, str3, c.m87a(this.mContext, str, str2)) : "";
        c.a(this.mContext, str, a2, str2);
        return a2;
    }
}
