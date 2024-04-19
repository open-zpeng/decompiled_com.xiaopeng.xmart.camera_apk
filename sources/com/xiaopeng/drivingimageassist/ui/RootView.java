package com.xiaopeng.drivingimageassist.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.xiaopeng.drivingimageassist.R;
/* loaded from: classes.dex */
public class RootView extends FrameLayout {
    private ViewGroup mLyCameraContent;
    private Rect mTouchRect;

    public RootView(Context context) {
        super(context);
        this.mTouchRect = new Rect();
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTouchRect = new Rect();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLyCameraContent = (ViewGroup) findViewById(R.id.ly_camera_content);
    }
}
