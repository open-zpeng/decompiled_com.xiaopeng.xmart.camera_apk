package com.xiaopeng.drivingimageassist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import com.xiaopeng.drivingimageassist.event.NRACtrlEvent;
import com.xiaopeng.drivingimageassist.event.TurnLampEvent;
import com.xiaopeng.drivingimageassist.statistic.StatisticConstants;
import com.xiaopeng.drivingimageassist.statistic.StatisticManager;
import com.xiaopeng.drivingimageassist.ui.CameraView;
import com.xiaopeng.drivingimageassist.utils.ThreadUtils;
import com.xiaopeng.lib.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class DIGActivity extends BaseUIActivity implements View.OnClickListener {
    private static final String CAMERA_TYPE = "camera_type";
    private static final String TAG = "DIGActivity";
    public static Runnable sCheckShow = new Runnable() { // from class: com.xiaopeng.drivingimageassist.DIGActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (DIGActivity.sInstance == null) {
                DIGModule.instance().resetNRACtrl();
                EventBus.getDefault().post(new TurnLampEvent(new Integer[]{0, 0}));
                Intent unused = DIGActivity.sIntent = null;
            }
        }
    };
    public static int sDisplayType = -1;
    private static DIGActivity sInstance;
    private static Intent sIntent;
    private Rect mCameraRect;
    private CameraView mCameraView;
    private ImageButton mCloseBtn;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.drivingimageassist.BaseUIActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG, "onCreate:" + this);
        sInstance = this;
        sIntent = getIntent();
        sDisplayType = getIntent().getIntExtra(CAMERA_TYPE, sDisplayType);
        CameraView cameraView = (CameraView) findViewById(R.id.ly_camera_content);
        this.mCameraView = cameraView;
        cameraView.switchCamera(sDisplayType);
        ImageButton imageButton = (ImageButton) findViewById(R.id.btn_close);
        this.mCloseBtn = imageButton;
        imageButton.setOnClickListener(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setStartOffset(400L);
        this.mCloseBtn.startAnimation(alphaAnimation);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        LogUtils.i(TAG, "onPause:" + this + " sInstance: " + sInstance);
        finish();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy:" + this + " sInstance: " + sInstance);
        sInstance = null;
        sIntent = null;
        EventBus.getDefault().post(new NRACtrlEvent(0));
        EventBus.getDefault().post(new TurnLampEvent(new Integer[]{0, 0}));
        DIGModule.instance().resetNRACtrl();
        sDisplayType = -1;
    }

    public static void show(Context context, int displayType) {
        DIGActivity dIGActivity;
        int i = sDisplayType;
        sDisplayType = displayType;
        if (sIntent != null) {
            if (displayType == i || (dIGActivity = sInstance) == null) {
                return;
            }
            dIGActivity.switchCamera(displayType);
            return;
        }
        Intent intent = new Intent(context, DIGActivity.class);
        sIntent = intent;
        intent.addFlags(335544320);
        sIntent.putExtra(CAMERA_TYPE, sDisplayType);
        context.startActivity(sIntent);
        ThreadUtils.removeWorker(sCheckShow);
        ThreadUtils.postWorker(sCheckShow, 2000L);
        LogUtils.i(TAG, "show");
    }

    public static void hide() {
        DIGActivity dIGActivity = sInstance;
        if (dIGActivity != null && !dIGActivity.isFinishing()) {
            sInstance.finish();
            return;
        }
        if (sIntent != null) {
            LogUtils.i(TAG, "hide :" + sDisplayType);
        }
        sIntent = null;
        sDisplayType = -1;
    }

    public static boolean isShowing() {
        return sIntent != null;
    }

    public void switchCamera(int displayType) {
        CameraView cameraView = this.mCameraView;
        if (cameraView != null) {
            cameraView.switchCamera(displayType);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close) {
            StatisticManager.logWithOneParam(StatisticConstants.EVENT_DIG_ASSIST, StatisticConstants.DIG_CLOSE, "value", String.valueOf(sDisplayType));
            finish();
        }
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (this.mCameraView != null) {
            if (this.mCameraRect == null) {
                Rect rect = new Rect();
                this.mCameraRect = rect;
                this.mCameraView.getGlobalVisibleRect(rect);
            }
            Rect rect2 = this.mCameraRect;
            if (rect2 != null && !rect2.contains(x, y)) {
                StatisticManager.logWithOneParam(StatisticConstants.EVENT_DIG_ASSIST, StatisticConstants.DIG_CLOSE, "value", String.valueOf(sDisplayType));
                finish();
            }
        }
        return super.onTouchEvent(event);
    }
}
