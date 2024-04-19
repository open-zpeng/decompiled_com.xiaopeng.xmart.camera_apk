package com.xiaopeng.xmart.livepush;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.lib.framework.moduleinterface.ipcmodule.IIpcService;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import com.xiaopeng.libconfig.ipc.bean.MqttMsgBase;
import com.xiaopeng.libconfig.remotecontrol.CommandItem;
import com.xiaopeng.xmart.camera.bean.ControlMsg;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes.dex */
public class RemoteControlIPCHelper {
    private static final String TAG = "RemoteControlCamera";
    private volatile String mMsgIdFromServer;
    RemoteControlHandler mRemoteControlHandler;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        static RemoteControlIPCHelper sInstance = new RemoteControlIPCHelper();

        private SingletonHolder() {
        }
    }

    private RemoteControlIPCHelper() {
        init();
    }

    public static RemoteControlIPCHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public void init() {
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IIpcService.IpcMessageEvent event) {
        int msgID = event.getMsgID();
        Bundle payloadData = event.getPayloadData();
        CameraLog.d(TAG, "ipc msgId: " + msgID, false);
        if (msgID != 150003 || payloadData == null) {
            return;
        }
        String string = payloadData.getString(IpcConfig.IPCKey.STRING_MSG);
        if (TextUtils.isEmpty(string)) {
            return;
        }
        doControlCommandIPC(string);
    }

    public void setRemoteControlHandler(RemoteControlPresenter handler) {
        this.mRemoteControlHandler = handler;
    }

    public synchronized String getMsgIdFromServer() {
        return this.mMsgIdFromServer;
    }

    public synchronized void setMsgIdFromServer(String msgIdFromServer) {
        this.mMsgIdFromServer = msgIdFromServer;
    }

    private void doControlCommandIPC(String commandJson) {
        MqttMsgBase mqttMsgBase = (MqttMsgBase) new Gson().fromJson(commandJson, new TypeToken<MqttMsgBase<CommandItem>>() { // from class: com.xiaopeng.xmart.livepush.RemoteControlIPCHelper.1
        }.getType());
        String msgId = mqttMsgBase.getMsgId();
        int msgType = mqttMsgBase.getMsgType();
        int serviceType = mqttMsgBase.getServiceType();
        CameraLog.d(TAG, "msgId: " + msgId, false);
        CameraLog.d(TAG, "msgType: " + msgType, false);
        CameraLog.d(TAG, "serviceType: " + serviceType, false);
        setMsgIdFromServer(msgId);
        if (this.mRemoteControlHandler.getIgStatus() == 1) {
            this.mRemoteControlHandler.feedbackCameraNotAllowed(0L);
        } else if (msgType == 4 && serviceType == 11) {
            CameraLog.d(TAG, "receive heart beat msg, set client live exit flase", false);
            this.mRemoteControlHandler.onLiveHeartBeat();
        } else {
            CommandItem commandItem = (CommandItem) mqttMsgBase.getMsgContent();
            CameraLog.d(TAG, "commandItem: " + commandItem.toString(), false);
            int cmd_type = commandItem.getCmd_type();
            CameraLog.d(TAG, "cmdType: " + cmd_type, false);
            if (serviceType == 11) {
                ControlMsg controlMsg = new ControlMsg();
                controlMsg.setCmdValue(commandItem.getCmd_value());
                if (cmd_type == 1) {
                    CameraLog.d(TAG, "RemoteControlConfig.REMOTE_COMMAND_TYPE_LIVE_STATE", false);
                    CarCameraHelper.getInstance().hasTopCamera();
                    this.mRemoteControlHandler.onLiveStateCmd(controlMsg);
                } else if (cmd_type == 2) {
                    CameraLog.d(TAG, "RemoteControlConfig.REMOTE_COMMAND_TYPE_LIVE_OPEN_OR_CLOSE", false);
                    this.mRemoteControlHandler.onLiveOpenOrCloseCmd(controlMsg);
                } else if (cmd_type != 3) {
                } else {
                    CameraLog.d(TAG, "RemoteControlConfig.REMOTE_COMMAND_TYPE_LIVE_MOVE", false);
                    this.mRemoteControlHandler.onLiveRotateCameraCmd(controlMsg);
                }
            }
        }
    }
}
