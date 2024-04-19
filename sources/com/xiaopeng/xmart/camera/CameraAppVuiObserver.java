package com.xiaopeng.xmart.camera;

import com.xiaopeng.lib.apirouter.server.IServicePublisher;
import com.xiaopeng.speech.vui.VuiEngine;
import com.xiaopeng.xmart.camera.utils.CameraLog;
/* loaded from: classes.dex */
public class CameraAppVuiObserver implements IServicePublisher {
    private static final String TAG = "VuiObserver";

    public void onEvent(String event, String data) {
        CameraLog.i(TAG, "event==" + event + " data=" + data, false);
        VuiEngine.getInstance(App.getInstance()).dispatchVuiEvent(event, data);
    }

    public String getElementState(String sceneId, String elementId) {
        CameraLog.i(TAG, "getElementState" + sceneId + " elementId=" + elementId, false);
        String elementState = VuiEngine.getInstance(App.getInstance()).getElementState(sceneId, elementId);
        CameraLog.i(TAG, "result == " + elementState, false);
        return elementState;
    }
}
