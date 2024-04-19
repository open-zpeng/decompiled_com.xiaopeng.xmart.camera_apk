package com.alivc.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.alivc.videochat.resource.R;
/* loaded from: classes.dex */
public class DebugSmallView extends LinearLayout {
    private static int statusBarHeight;
    public static int viewHeight;
    public static int viewWidth;
    private WindowManager.LayoutParams mParams;
    private WindowManager windowManager;
    private float xDownInScreen;
    private float xInScreen;
    private float xInView;
    private float yDownInScreen;
    private float yInScreen;
    private float yInView;

    public DebugSmallView(Context context) {
        super(context);
        this.windowManager = (WindowManager) context.getSystemService("window");
        LayoutInflater.from(context).inflate(R.layout.debug_view_small, this);
        View findViewById = findViewById(R.id.small_window_layout);
        viewWidth = findViewById.getLayoutParams().width;
        viewHeight = findViewById.getLayoutParams().height;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.xInView = motionEvent.getX();
            this.yInView = motionEvent.getY();
            this.xDownInScreen = motionEvent.getRawX();
            this.yDownInScreen = motionEvent.getRawY();
            this.xInScreen = motionEvent.getRawX();
            this.yInScreen = motionEvent.getRawY();
        } else if (action != 1) {
            if (action == 2) {
                this.xInScreen = motionEvent.getRawX();
                this.yInScreen = motionEvent.getRawY();
                updateViewPosition();
            }
        } else if (Math.abs(this.xDownInScreen - this.xInScreen) < 5.0f && Math.abs(this.yDownInScreen - this.yInScreen) < 5.0f) {
            openBigWindow();
        }
        return true;
    }

    public void setParams(WindowManager.LayoutParams layoutParams) {
        this.mParams = layoutParams;
    }

    private void updateViewPosition() {
        this.mParams.x = (int) (this.xInScreen - this.xInView);
        this.mParams.y = (int) (this.yInScreen - this.yInView);
        this.windowManager.updateViewLayout(this, this.mParams);
    }

    private void openBigWindow() {
        DebugViewManager.openBigWindow(getContext());
        DebugViewManager.removeSmallWindow(getContext());
    }

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> cls = Class.forName("com.android.internal.R$dimen");
                statusBarHeight = getResources().getDimensionPixelSize(((Integer) cls.getField("status_bar_height").get(cls.newInstance())).intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
