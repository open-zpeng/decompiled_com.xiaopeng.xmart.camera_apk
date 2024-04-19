package com.aliyun.clientinforeport.core;

import com.aliyun.clientinforeport.AlivcEventPublicParam;
import com.aliyun.clientinforeport.util.RLog;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class LogSender {
    public static final String KEY_APPLICATION_ID = "app_id";
    public static final String KEY_APPLICATION_NAME = "app_n";
    public static final String KEY_APP_VERSION = "av";
    public static final String KEY_ARGS = "args";
    public static final String KEY_BUSINESS_ID = "bi";
    public static final String KEY_CDN_IP = "cdn_ip";
    public static final String KEY_CONNECTION = "co";
    public static final String KEY_DEVICE_BRAND = "db";
    public static final String KEY_DEVICE_MANUFACTURER = "dma";
    public static final String KEY_DEVICE_MODEL = "dm";
    public static final String KEY_EVENT = "e";
    public static final String KEY_HOSTNAME = "hn";
    public static final String KEY_LOG_LEVEL = "ll";
    public static final String KEY_LOG_VERSION = "lv";
    public static final String KEY_MODULE = "md";
    public static final String KEY_OPERATION_SYSTEM = "os";
    public static final String KEY_OSVERSION = "ov";
    public static final String KEY_PRODUCT = "pd";
    public static final String KEY_REFER = "r";
    public static final String KEY_REQUEST_ID = "ri";
    public static final String KEY_SUB_MODULE = "sm";
    public static final String KEY_TERMINAL_TYPE = "tt";
    public static final String KEY_TIME = "t";
    public static final String KEY_USER_AGENT = "uat";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_VIDEO_TYPE = "vt";
    public static final String KEY_VIDEO_URL = "vu";
    private static final String TAG = "LogSender";
    private static String logPushUrl = "http://videocloud.cn-hangzhou.log.aliyuncs.com/logstores/";
    private static String trackVersion = "/track?APIVersion=0.6.0";

    public static void sendActually(AlivcEventPublicParam alivcEventPublicParam, int i, Map<String, String> map) {
        if (alivcEventPublicParam == null) {
            throw new IllegalAccessError("report log's publicparam can NOT be null!");
        }
        PublicParamChecker.checkParam(alivcEventPublicParam, i);
        String formatFinalUrl = formatFinalUrl(logPushUrl + alivcEventPublicParam.getLogStore() + trackVersion, alivcEventPublicParam, i, formatEventArgs(map));
        RLog.d(TAG, "logFinalUrl " + formatFinalUrl);
        RLog.d(TAG, "requestId =  " + alivcEventPublicParam.getRequestId());
        try {
            RLog.i(TAG, "onResponse " + doHttpGet(formatFinalUrl));
        } catch (Exception e) {
            RLog.e(TAG, e.getMessage());
        }
    }

    private static String doHttpGet(String str) throws Exception {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            sb.append(readLine);
        }
        int responseCode = httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
        if (responseCode == 200) {
            return sb.toString();
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("StatusCode", responseCode);
        jSONObject.put("ResponseStr", sb.toString());
        return jSONObject.toString();
    }

    private static String formatEventArgs(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
            try {
                return URLEncoder.encode(sb.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                RLog.e(TAG, e.toString());
            }
        }
        return "";
    }

    private static String formatFinalUrl(String str, AlivcEventPublicParam alivcEventPublicParam, int i, String str2) {
        StringBuilder append = new StringBuilder(str).append("&");
        append.append(KEY_TIME).append("=").append(alivcEventPublicParam.getTime()).append("&");
        append.append(KEY_LOG_LEVEL).append("=").append(alivcEventPublicParam.getLogLevel()).append("&");
        append.append(KEY_LOG_VERSION).append("=").append(alivcEventPublicParam.getLogVersion()).append("&");
        append.append(KEY_PRODUCT).append("=").append(alivcEventPublicParam.getProduct()).append("&");
        append.append(KEY_MODULE).append("=").append(alivcEventPublicParam.getModule()).append("&");
        append.append(KEY_SUB_MODULE).append("=").append(alivcEventPublicParam.getSubModule()).append("&");
        append.append(KEY_HOSTNAME).append("=").append(alivcEventPublicParam.getHostName()).append("&");
        append.append(KEY_BUSINESS_ID).append("=").append(alivcEventPublicParam.getBusinessId()).append("&");
        append.append(KEY_REQUEST_ID).append("=").append(alivcEventPublicParam.getRequestId()).append("&");
        append.append(KEY_EVENT).append("=").append(i).append("&");
        append.append(KEY_ARGS).append("=").append(str2).append("&");
        append.append(KEY_VIDEO_TYPE).append("=").append(alivcEventPublicParam.getVideoType()).append("&");
        append.append(KEY_TERMINAL_TYPE).append("=").append(alivcEventPublicParam.getTerminalType()).append("&");
        append.append(KEY_DEVICE_MODEL).append("=").append(alivcEventPublicParam.getDeviceModel()).append("&");
        append.append(KEY_DEVICE_BRAND).append("=").append(alivcEventPublicParam.getDeviceBrand()).append("&");
        append.append(KEY_DEVICE_MANUFACTURER).append("=").append(alivcEventPublicParam.getDeviceManufacture()).append("&");
        append.append(KEY_OPERATION_SYSTEM).append("=").append(alivcEventPublicParam.getOperationSystem()).append("&");
        append.append(KEY_OSVERSION).append("=").append(alivcEventPublicParam.getOsVersion()).append("&");
        append.append(KEY_APP_VERSION).append("=").append(alivcEventPublicParam.getAppVersion()).append("&");
        append.append(KEY_UUID).append("=").append(alivcEventPublicParam.getUuid()).append("&");
        append.append(KEY_VIDEO_URL).append("=").append(alivcEventPublicParam.getVideoUrl()).append("&");
        append.append(KEY_CONNECTION).append("=").append(alivcEventPublicParam.getConnection()).append("&");
        append.append(KEY_USER_AGENT).append("=").append(alivcEventPublicParam.getUserAgent()).append("&");
        append.append("app_id").append("=").append(alivcEventPublicParam.getApplicationId()).append("&");
        append.append(KEY_APPLICATION_NAME).append("=").append(alivcEventPublicParam.getApplicationName()).append("&");
        append.append(KEY_CDN_IP).append("=").append(alivcEventPublicParam.getCdnIp()).append("&");
        append.append(KEY_REFER).append("=").append(alivcEventPublicParam.getReferer()).append("&");
        append.deleteCharAt(append.lastIndexOf("&"));
        return append.toString();
    }
}
