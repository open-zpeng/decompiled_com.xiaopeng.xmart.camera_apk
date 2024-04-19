package com.xiaopeng.xmart.camera.carmanager;

import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.lib.framework.ipcmodule.IpcModuleEntry;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.framework.moduleinterface.ipcmodule.IIpcService;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.IRemoteStorage;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficeStaFlagInterceptor;
import com.xiaopeng.lib.http.HttpsUtils;
import com.xiaopeng.xmart.camera.App;
/* loaded from: classes.dex */
public class Component {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int DNS_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 3000;
    private static final int WRITE_TIMEOUT = 3000;
    private static IRemoteStorage mRemoteStorage;
    private IHttp http;
    private IDataLog mDataLogService;
    private IIpcService mIpcService;

    public void destroy() {
    }

    private Component() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        private static Component sINSTANCE = new Component();

        private SingletonHolder() {
        }
    }

    public static Component getInstance() {
        return SingletonHolder.sINSTANCE;
    }

    public void initComponent() {
        initIpcService();
        initDataLogService();
        initHttpAndRemoteStorageService();
    }

    private void initDataLogService() {
        this.mDataLogService = (IDataLog) Module.get(DataLogModuleEntry.class).get(IDataLog.class);
    }

    private void initIpcService() {
        Module.register(IpcModuleEntry.class, new IpcModuleEntry(App.getInstance()));
        IIpcService iIpcService = (IIpcService) Module.get(IpcModuleEntry.class).get(IIpcService.class);
        this.mIpcService = iIpcService;
        iIpcService.init();
    }

    private void initHttpAndRemoteStorageService() {
        Module.register(NetworkChannelsEntry.class, new NetworkChannelsEntry());
        IHttp iHttp = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        this.http = iHttp;
        iHttp.config().connectTimeout(5000).addInterceptor(new TrafficeStaFlagInterceptor()).readTimeout(3000).writeTimeout(3000).dnsTimeout(5000).retryCount(1).applicationContext(App.getInstance()).apply();
        HttpsUtils.init(App.getInstance(), true);
        IRemoteStorage iRemoteStorage = (IRemoteStorage) Module.get(NetworkChannelsEntry.class).get(IRemoteStorage.class);
        mRemoteStorage = iRemoteStorage;
        try {
            iRemoteStorage.initWithContext(App.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IDataLog getDataLogService() {
        return this.mDataLogService;
    }

    public IHttp getHttp() {
        return this.http;
    }

    public IRemoteStorage getRemoteStorage() {
        return mRemoteStorage;
    }
}
