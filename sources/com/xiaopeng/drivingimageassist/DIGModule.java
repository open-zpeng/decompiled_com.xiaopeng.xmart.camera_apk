package com.xiaopeng.drivingimageassist;

import android.content.Context;
import com.xiaopeng.drivingimageassist.event.IGStatusEvent;
import com.xiaopeng.drivingimageassist.scene.NRACtrlScene;
import com.xiaopeng.drivingimageassist.scene.TurnLampScene;
import com.xiaopeng.drivingimageassist.utils.ThreadUtils;
import com.xiaopeng.lib.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes.dex */
public class DIGModule {
    private static final String TAG = "DIGModule";
    private Context mContext;
    private boolean mIsXpuComponent = true;
    private NRACtrlScene mNarrowScene;
    private TurnLampScene mTurnScene;
    private Runnable mXpuComponentTask;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        private static final DIGModule Instance = new DIGModule();

        private Holder() {
        }
    }

    public static final DIGModule instance() {
        return Holder.Instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mNarrowScene = new NRACtrlScene();
        this.mTurnScene = new TurnLampScene(this.mContext);
        EventBus.getDefault().register(this);
    }

    public Context getContext() {
        return this.mContext;
    }

    public boolean checkTurnLamp() {
        return this.mTurnScene.checkWithNotNRA();
    }

    public boolean checkNRACtrl() {
        return this.mNarrowScene.checkNRACtrl();
    }

    public void resetNRACtrl() {
        this.mNarrowScene.reset();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(IGStatusEvent event) {
        LogUtils.i(TAG, "IGStatusEvent :" + event.isOff());
        Runnable runnable = this.mXpuComponentTask;
        if (runnable != null) {
            ThreadUtils.removeWorker(runnable);
        }
        if (event.isOn()) {
            Runnable runnable2 = new Runnable() { // from class: com.xiaopeng.drivingimageassist.DIGModule.1
                @Override // java.lang.Runnable
                public void run() {
                    DIGModule.this.mIsXpuComponent = true;
                    DIGModule.this.mXpuComponentTask = null;
                }
            };
            this.mXpuComponentTask = runnable2;
            ThreadUtils.postWorker(runnable2, 20000L);
            return;
        }
        this.mIsXpuComponent = false;
    }

    public boolean isXpuComponent() {
        return this.mIsXpuComponent;
    }
}
