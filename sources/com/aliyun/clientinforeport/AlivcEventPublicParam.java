package com.aliyun.clientinforeport;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.clientinforeport.util.AppUtils;
import com.aliyun.clientinforeport.util.DeviceUtils;
import com.aliyun.clientinforeport.util.NetUtils;
import com.xiaopeng.xmart.camera.speech.ISpeechContants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
/* loaded from: classes.dex */
public class AlivcEventPublicParam {
    private String appVersion;
    private String applicationId;
    private String applicationName;
    private String businessId;
    private String cdnIp;
    private String connection;
    private String deviceBrand;
    private String deviceManufacture;
    private String deviceModel;
    private String logLevel;
    private String logStore;
    private String module;
    private String operationSystem;
    private String osVersion;
    private String product;
    private String referer;
    private String requestId;
    private String subModule;
    private String terminalType;
    private String ui;
    private String userAccount;
    private String userAgent;
    private String uuid;
    private String videoType;
    private String videoUrl;
    private long time = System.currentTimeMillis();
    private String logVersion = BuildConfig.SDK_VERSION;
    private String hostName = NetUtils.getHostIp();

    /* loaded from: classes.dex */
    public enum LogLevel {
        debug,
        info,
        warn,
        error
    }

    /* loaded from: classes.dex */
    public enum TerminalType {
        pc,
        phone,
        pad
    }

    /* loaded from: classes.dex */
    public enum Ui {
        saas_player
    }

    /* loaded from: classes.dex */
    public enum VideoType {
        live,
        vod
    }

    public AlivcEventPublicParam(Context context) {
        this.terminalType = (DeviceUtils.isPad(context) ? TerminalType.pad : TerminalType.phone).name();
        this.deviceModel = DeviceUtils.getDeviceModel();
        this.deviceBrand = DeviceUtils.getDeviceBrand();
        this.deviceManufacture = DeviceUtils.getDeviceManufacture();
        this.operationSystem = DeviceUtils.getOperationSystem(context);
        this.osVersion = DeviceUtils.getOsVersion();
        this.uuid = DeviceUtils.getUuid(context);
        this.connection = DeviceUtils.getConnection(context);
        this.applicationId = AppUtils.getPackageName(context) + "|Android";
        this.applicationName = AppUtils.getAppName(context);
        this.requestId = UUID.randomUUID().toString().toUpperCase();
    }

    public void setLogStore(String str) {
        this.logStore = str;
    }

    public void setLogLevel(LogLevel logLevel) {
        if (logLevel == null) {
            this.logLevel = LogLevel.debug.name();
        } else {
            this.logLevel = logLevel.name();
        }
    }

    public void setProduct(String str) {
        this.product = str;
    }

    public void setModule(String str) {
        this.module = str;
    }

    public void setSubModule(String str) {
        this.subModule = str;
    }

    public void setBusinessId(String str) {
        this.businessId = str;
    }

    public void refreshRequestId() {
        this.requestId = UUID.randomUUID().toString().toUpperCase();
    }

    public void setRequestId(String str) {
        this.requestId = str;
    }

    public void setVideoType(VideoType videoType) {
        if (videoType == null) {
            this.videoType = "";
        } else {
            this.videoType = videoType.name();
        }
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
    }

    public void setVideoUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            this.videoUrl = "";
            return;
        }
        try {
            this.videoUrl = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            this.videoUrl = URLEncoder.encode(str);
        }
    }

    public void setUserAccount(String str) {
        this.userAccount = str;
    }

    public void setUserAgent(String str) {
        this.userAgent = str;
    }

    public void setUi(Ui ui) {
        if (ui == null) {
            this.ui = "";
        } else {
            this.ui = ui.name();
        }
    }

    public void setCdnIp(String str) {
        this.cdnIp = str;
    }

    public void setReferer(String str) {
        this.referer = str;
    }

    public String getTime() {
        return System.currentTimeMillis() + "";
    }

    public String getLogStore() {
        if (TextUtils.isEmpty(this.logStore)) {
            throw new IllegalAccessError("logStore is Empty!");
        }
        return this.logStore;
    }

    public String getLogLevel() {
        if (TextUtils.isEmpty(this.logLevel)) {
            this.logLevel = LogLevel.info.name();
        }
        return this.logLevel;
    }

    public String getLogVersion() {
        if (TextUtils.isEmpty(this.logVersion)) {
            this.logVersion = "1.0";
        }
        return this.logVersion;
    }

    public String getProduct() {
        if (TextUtils.isEmpty(this.product)) {
            throw new IllegalAccessError("product is Empty!");
        }
        return this.product;
    }

    public String getModule() {
        if (TextUtils.isEmpty(this.module)) {
            throw new IllegalAccessError("module is Empty!");
        }
        return this.module;
    }

    public String getSubModule() {
        if (TextUtils.isEmpty(this.subModule)) {
            throw new IllegalAccessError("subModule is Empty!");
        }
        return this.subModule;
    }

    public String getHostName() {
        if (TextUtils.isEmpty(this.hostName)) {
            this.hostName = "";
        }
        return this.hostName;
    }

    public String getBusinessId() {
        if (TextUtils.isEmpty(this.businessId)) {
            this.businessId = "";
        }
        return this.businessId;
    }

    public String getRequestId() {
        if (TextUtils.isEmpty(this.requestId)) {
            refreshRequestId();
        }
        return this.requestId;
    }

    public String getTerminalType() {
        return this.terminalType;
    }

    public String getOperationSystem() {
        return this.operationSystem;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getAppVersion() {
        if (TextUtils.isEmpty(this.appVersion)) {
            throw new IllegalAccessError("appVersion is Empty !");
        }
        return this.appVersion;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getConnection() {
        if (TextUtils.isEmpty(this.connection)) {
            this.connection = "";
        }
        return this.connection;
    }

    public String getUserAgent() {
        if (TextUtils.isEmpty(this.userAgent)) {
            this.userAgent = "";
        }
        return this.userAgent;
    }

    public String getUi() {
        if (TextUtils.isEmpty(this.ui)) {
            this.ui = "";
        }
        return this.ui;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getCdnIp() {
        if (TextUtils.isEmpty(this.cdnIp)) {
            this.cdnIp = "0.0.0.0";
        }
        return this.cdnIp;
    }

    public String getReferer() {
        if (TextUtils.isEmpty(this.referer)) {
            this.referer = "";
        }
        return this.referer;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public String getVideoType() {
        if (TextUtils.isEmpty(this.videoType)) {
            this.videoType = "";
        }
        return this.videoType;
    }

    public String getVideoUrl() {
        if (TextUtils.isEmpty(this.videoUrl)) {
            this.videoUrl = "";
        }
        return this.videoUrl;
    }

    public String getUserAccount() {
        if (TextUtils.isEmpty(this.userAccount)) {
            this.userAccount = ISpeechContants.SCREEN_ID_MAIN;
        }
        return this.userAccount;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public String getDeviceBrand() {
        return this.deviceBrand;
    }

    public String getDeviceManufacture() {
        return this.deviceManufacture;
    }
}
