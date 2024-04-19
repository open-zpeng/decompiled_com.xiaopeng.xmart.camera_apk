package com.aliyun.clientinforeport.util;

import java.util.List;
/* loaded from: classes.dex */
public class CollectionUtils {
    public static String toString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list == null || list.isEmpty()) {
            return sb.toString();
        }
        sb.append("[");
        for (String str : list) {
            sb.append(str).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
