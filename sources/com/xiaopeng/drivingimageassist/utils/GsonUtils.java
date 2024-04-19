package com.xiaopeng.drivingimageassist.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.lib.utils.LogUtils;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class GsonUtils {
    public static String toJson(Object obj, String from) {
        if (obj == null) {
            return null;
        }
        try {
            return new Gson().toJson(obj);
        } catch (Exception e) {
            LogUtils.i("GsonUtils", "from:" + from, e);
            return null;
        }
    }

    public static String obj2String(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        if (object instanceof Number) {
            return object.toString();
        }
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return (T) new Gson().fromJson(json, (Class<Object>) classOfT);
    }

    public static <T> T fromJson(String json, Type type) {
        return (T) new Gson().fromJson(json, type);
    }

    public static Map<String, Object> string2Map(String data) {
        return (Map) fromJson(data, new TypeToken<HashMap<String, Object>>() { // from class: com.xiaopeng.drivingimageassist.utils.GsonUtils.1
        }.getType());
    }
}
