package com.xiaopeng.xmart.camera.utils;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import com.xiaopeng.speech.vui.Helper.IVuiSceneHelper;
import com.xiaopeng.speech.vui.VuiEngine;
import com.xiaopeng.speech.vui.model.VuiFeedback;
import com.xiaopeng.speech.vui.utils.VuiUtils;
import com.xiaopeng.vui.commons.IVuiElementChangedListener;
import com.xiaopeng.vui.commons.VuiUpdateType;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xui.Xui;
import com.xiaopeng.xui.vui.VuiView;
/* loaded from: classes.dex */
public class VuiManager {
    public static final String SCENE_MAIN = "MainCameraApp";
    private String TAG;

    public static VuiManager getInstance() {
        return Holder.sInstance;
    }

    /* loaded from: classes.dex */
    private static class Holder {
        private static VuiManager sInstance = new VuiManager();

        private Holder() {
        }
    }

    private VuiManager() {
        this.TAG = VuiManager.class.getName();
    }

    public void initVuiScene(Lifecycle lifecycle, String sceneId, View rootView, IVuiSceneHelper listener, IVuiElementChangedListener elementChangedListener) {
        if (sceneId == null || rootView == null || listener == null || !Xui.isVuiEnable()) {
            return;
        }
        VuiEngine.getInstance(App.getInstance()).initScene(lifecycle, sceneId, rootView, listener, elementChangedListener);
    }

    public void updateVuiScene(String sceneId, View view, VuiUpdateType type) {
        CameraLog.i(this.TAG, "updateVuiScene " + view, false);
        if (VuiUpdateType.UPDATE_VIEW.equals(type)) {
            VuiEngine.getInstance(App.getInstance()).updateScene(sceneId, view);
        } else {
            VuiEngine.getInstance(App.getInstance()).updateElementAttribute(sceneId, view);
        }
    }

    public void vuiFeedback(int resContent, View view) {
        if (view == null || resContent == 0) {
            return;
        }
        VuiEngine.getInstance(App.getInstance()).vuiFeedback(view, new VuiFeedback.Builder().state(1).content(App.getInstance().getString(resContent)).build());
    }

    public void addSceneElement(VuiView view, View parentView) {
        if (Xui.isVuiEnable()) {
            VuiView vuiView = (VuiView) parentView;
            CameraLog.e(this.TAG, "addSceneElement view:" + view + ",id=" + view.getVuiElementId() + ",vuiID:" + vuiView.getVuiElementId(), false);
            VuiEngine.getInstance(App.getInstance()).addSceneElement((View) view, vuiView.getVuiElementId(), SCENE_MAIN);
        }
    }

    public void vuiInterceptBack(VuiView view) {
        VuiUtils.addGeneralActProp(view, "System.Close|System.Return");
    }
}
