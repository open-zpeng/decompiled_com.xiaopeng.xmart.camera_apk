package com.aliyun.clientinforeport.core;

import android.text.TextUtils;
import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.util.CheckLog;
import com.aliyun.clientinforeport.util.NetUtils;
import com.aliyun.clientinforeport.util.RLog;
import com.xiaopeng.lib.framework.moduleinterface.appresourcemodule.IAppResourceException;
import com.xiaopeng.xui.app.XDialogSystemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class PublicParamChecker {
    private static final List<String> connectionList;
    private static final List<String> moduleList;
    private static final Map<Integer, String> playerEventDesc;
    private static final List<String> productList;
    private static final List<String> submoduleList;
    private static final List<String> terminalTypeList;
    private static final List<String> videoTypeList;

    static {
        ArrayList arrayList = new ArrayList();
        productList = arrayList;
        arrayList.add("player");
        arrayList.add("pusher");
        arrayList.add("mixer");
        arrayList.add("svideo");
        ArrayList arrayList2 = new ArrayList();
        moduleList = arrayList2;
        arrayList2.add("saas_player");
        arrayList2.add("player");
        arrayList2.add("mixer");
        arrayList2.add("publisher");
        arrayList2.add("svideo_basic");
        arrayList2.add("svideo_standard");
        arrayList2.add("svideo_pro");
        ArrayList arrayList3 = new ArrayList();
        submoduleList = arrayList3;
        arrayList3.add("play");
        arrayList3.add("download");
        arrayList3.add("record");
        arrayList3.add("cut");
        arrayList3.add("edit");
        arrayList3.add("pusher");
        ArrayList arrayList4 = new ArrayList();
        videoTypeList = arrayList4;
        arrayList4.add("live");
        arrayList4.add("vod");
        ArrayList arrayList5 = new ArrayList();
        terminalTypeList = arrayList5;
        arrayList5.add("pc");
        arrayList5.add("phone");
        arrayList5.add("pad");
        ArrayList arrayList6 = new ArrayList();
        connectionList = arrayList6;
        arrayList6.add("wifi");
        arrayList6.add("cellnetwork");
        HashMap hashMap = new HashMap();
        playerEventDesc = hashMap;
        hashMap.put(1001, "播放器初始化");
        hashMap.put(1002, "关闭/释放播放器");
        hashMap.put(1003, "开始获取URL");
        hashMap.put(1004, "结束获取URL");
        hashMap.put(1005, "开始播放 播放之前汇报");
        hashMap.put(2001, "播放");
        hashMap.put(Integer.valueOf((int) IAppResourceException.REASON_MGR_DB_ERROR), "完成");
        hashMap.put(2012, "停止");
        hashMap.put(2003, "暂停");
        hashMap.put(2010, "暂停恢复");
        hashMap.put(2004, "拖动");
        hashMap.put(2005, "全屏");
        hashMap.put(2006, "退出全屏");
        hashMap.put(2007, "切换清晰度");
        hashMap.put(Integer.valueOf((int) XDialogSystemType.TYPE_SYSTEM_DIALOG), "清晰度切换完成");
        hashMap.put(2009, "重播");
        hashMap.put(2023, "开始缓存");
        hashMap.put(2024, "缓存成功");
        hashMap.put(2025, "开启循环播放");
        hashMap.put(2026, "关闭循环播放");
        hashMap.put(2027, "使用截图");
        hashMap.put(2028, "设置旋转角度");
        hashMap.put(2029, "设置镜像");
        hashMap.put(2030, "读取SEI");
        hashMap.put(2011, "拖动完成");
        hashMap.put(3002, "卡顿");
        hashMap.put(Integer.valueOf((int) IAppResourceException.REASON_NO_PERMISSION), "卡顿恢复");
        hashMap.put(3005, "周期性下载");
        hashMap.put(9001, "周期性汇报");
        hashMap.put(9004, "延时信息");
        hashMap.put(Integer.valueOf((int) IAppResourceException.REASON_BINDER_FAILED), "发生错误");
    }

    public static void checkParam(AlivcEventPublicParam alivcEventPublicParam, int i) {
        if (RLog.isOpen()) {
            if (alivcEventPublicParam == null) {
                CheckLog.empty("AlivcEventPublicParam");
            } else {
                if (alivcEventPublicParam.getProduct().equals("player")) {
                    CheckLog.ok("==================== AlivcEventPublicParam ", "check Start  eventid = " + i + "(" + playerEventDesc.get(Integer.valueOf(i)) + ") =====================");
                }
                checkTime(alivcEventPublicParam.getTime());
                checkLogLevel(alivcEventPublicParam.getLogLevel());
                checkLogVersion(alivcEventPublicParam.getLogVersion());
                checkProduct(alivcEventPublicParam.getProduct());
                checkModule(alivcEventPublicParam.getModule());
                checkSubModule(alivcEventPublicParam.getSubModule());
                checkHostName(alivcEventPublicParam.getHostName());
                checkBusinessId(alivcEventPublicParam.getBusinessId());
                checkRequestId(alivcEventPublicParam.getRequestId());
                checkVideoType(alivcEventPublicParam.getVideoType());
                checkTerminalType(alivcEventPublicParam.getTerminalType());
                checkDeviceModel(alivcEventPublicParam.getDeviceModel());
                checkDeviceBrand(alivcEventPublicParam.getDeviceBrand());
                checkDeviceManufacture(alivcEventPublicParam.getDeviceManufacture());
                checkOperationSystem(alivcEventPublicParam.getOperationSystem());
                checkOsVersion(alivcEventPublicParam.getOsVersion());
                checkAppVersion(alivcEventPublicParam.getAppVersion());
                checkUuid(alivcEventPublicParam.getUuid());
                checkVideoUrl(alivcEventPublicParam.getVideoUrl());
                checkConnection(alivcEventPublicParam.getConnection());
                checkUserAgent(alivcEventPublicParam.getUserAgent());
                checkAppId(alivcEventPublicParam.getApplicationId());
                checkAppName(alivcEventPublicParam.getApplicationName());
                checkCdnIp(alivcEventPublicParam.getCdnIp());
                checkRefer(alivcEventPublicParam.getReferer());
            }
            CheckLog.ok("==================== AlivcEventPublicParam ", "check End =====================");
        }
    }

    private static void checkRefer(String str) {
        emptyCheck("r(referrer)", str);
    }

    private static void checkCdnIp(String str) {
        if (TextUtils.isEmpty(str)) {
            CheckLog.empty("cdn_ip(cdnIp)");
        } else if (!NetUtils.isIp(str)) {
            CheckLog.error("ERROR: cdn_ip(cdnIp) is  " + str + " , should be IP address");
        } else {
            CheckLog.ok("cdn_ip(cdnIp)", str);
        }
    }

    private static void checkAppName(String str) {
        emptyCheck("app_n(applicationName)", str);
    }

    private static void checkAppId(String str) {
        if (TextUtils.isEmpty(str)) {
            CheckLog.empty("app_id(applicationId)");
        } else if (str.contains(" ")) {
            CheckLog.space("app_id(applicationId)", str);
        } else if (!str.endsWith("|Android") && !str.endsWith("|iOS")) {
            CheckLog.error("ERROR: app_id(applicationId) is  " + str + " , should be endWith [|Android , |iOS]");
        } else {
            CheckLog.ok("app_id(applicationId)", str);
        }
    }

    private static void checkUserAgent(String str) {
        emptyCheck("uat(userAgent)", str);
    }

    private static void checkConnection(String str) {
        rangeCheck("co(connection)", str, connectionList);
    }

    private static void checkVideoUrl(String str) {
        emptyCheck("vu(videoUrl)", str);
    }

    private static void checkUuid(String str) {
        emptyCheck("uuid(uuid)", str);
    }

    private static void checkAppVersion(String str) {
        emptyCheck("av(appVersion)", str);
    }

    private static void checkOsVersion(String str) {
        emptyCheck("ov(osVersion)", str);
    }

    private static void checkOperationSystem(String str) {
        emptyCheck("os(operationSystem)", str);
    }

    private static void checkDeviceManufacture(String str) {
        emptyCheck("dma(deviceManufacture)", str);
    }

    private static void checkDeviceBrand(String str) {
        emptyCheck("db(deviceBrand)", str);
    }

    private static void checkDeviceModel(String str) {
        emptyCheck("dm(deviceModel)", str);
    }

    private static void checkTerminalType(String str) {
        rangeCheck("tt(terminalType)", str, terminalTypeList);
    }

    private static void checkVideoType(String str) {
        rangeCheck("vt(videoType)", str, videoTypeList);
    }

    private static void checkRequestId(String str) {
        emptyCheck("ri(requestId)", str);
    }

    private static void checkBusinessId(String str) {
        emptyCheck("bi(businessId)", str);
    }

    private static void checkHostName(String str) {
        if (TextUtils.isEmpty(str)) {
            CheckLog.empty("hn(hostName)");
        } else if (!NetUtils.isIp(str)) {
            CheckLog.error("ERROR: hn(hostName) is  " + str + " , should be IP address");
        } else {
            CheckLog.ok("hn(hostName)", str);
        }
    }

    private static void checkSubModule(String str) {
        rangeCheck("sm(subModule)", str, submoduleList);
    }

    private static void checkModule(String str) {
        rangeCheck("md(module)", str, moduleList);
    }

    private static void checkProduct(String str) {
        rangeCheck("pd(product)", str, productList);
    }

    private static void checkLogLevel(String str) {
        emptyCheck("ll(logLevel)", str);
    }

    private static void checkLogVersion(String str) {
        emptyCheck("lv(logVersion)", str);
    }

    private static void checkTime(String str) {
        if (TextUtils.isEmpty(str)) {
            CheckLog.empty("t(time)");
        } else if (str.length() != 13) {
            CheckLog.error("ERROR: t(time) is  " + str + " , should be MS");
        } else {
            CheckLog.ok("t(time)", str);
        }
    }

    private static void emptyCheck(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            CheckLog.empty(str);
        } else if (str.contains(" ")) {
            CheckLog.space(str, str2);
        } else {
            CheckLog.ok(str, str2);
        }
    }

    private static void rangeCheck(String str, String str2, List<String> list) {
        if (TextUtils.isEmpty(str2)) {
            CheckLog.empty(str);
        } else if (!list.contains(str2)) {
            CheckLog.outRange(str, str2, list);
        } else {
            CheckLog.ok(str, str2);
        }
    }
}
