package com.xiaopeng.xmart.camera;

import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.xiaopeng.speech.vui.Helper.IVuiSceneHelper;
import com.xiaopeng.vui.commons.IVuiElementChangedListener;
import com.xiaopeng.vui.commons.VuiUpdateType;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.VuiManager;
import java.util.List;
/* loaded from: classes.dex */
public class VuiActivity extends AppCompatActivity implements IVuiSceneHelper {
    private static final String TAG = "VuiManager";
    public IVuiElementChangedListener mElementChangedListener = new IVuiElementChangedListener() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$VuiActivity$xM9LM6rOIDDhCcdNkE5LkslteN4
        @Override // com.xiaopeng.vui.commons.IVuiElementChangedListener
        public final void onVuiElementChaned(View view, VuiUpdateType vuiUpdateType) {
            VuiActivity.this.lambda$new$0$VuiActivity(view, vuiUpdateType);
        }
    };
    public ViewGroup mRootView;

    @Override // com.xiaopeng.speech.vui.Helper.IVuiSceneHelper
    public List<View> getBuildViews() {
        return null;
    }

    @Override // com.xiaopeng.speech.vui.Helper.IVuiSceneHelper
    public String getSceneId() {
        return VuiManager.SCENE_MAIN;
    }

    public /* synthetic */ void lambda$new$0$VuiActivity(View view, VuiUpdateType vuiUpdateType) {
        CameraLog.i(TAG, "listener Type:" + vuiUpdateType + ",update:" + VuiUpdateType.UPDATE_VIEW + ",view:" + view);
        VuiManager.getInstance().updateVuiScene(getSceneId(), view, vuiUpdateType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initScene() {
        VuiManager.getInstance().initVuiScene(getLifecycle(), getSceneId(), this.mRootView, this, this.mElementChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void vuiFeedback(int content, View view) {
        VuiManager.getInstance().vuiFeedback(content, view);
    }
}
