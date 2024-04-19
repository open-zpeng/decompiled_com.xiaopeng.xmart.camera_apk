package com.xiaopeng.xmart.camera.view.controlpanel;

import android.content.Context;
import android.util.AttributeSet;
import com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView;
import com.xiaopeng.xui.widget.XTabLayout;
/* loaded from: classes.dex */
public class PortraitCameraSwitchView extends XTabLayout implements ICameraSwitchView, XTabLayout.OnTabChangeListener {
    private ICameraSwitchView.OnSwitchChangeListener mOnCameraModeSwitchListener;

    public PortraitCameraSwitchView(Context context) {
        super(context);
        init();
    }

    public PortraitCameraSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PortraitCameraSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PortraitCameraSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        setOnTabChangeListener(this);
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView
    public void selectTab(boolean isPhoto) {
        selectTab(!isPhoto ? 1 : 0, true);
    }

    @Override // com.xiaopeng.xmart.camera.view.controlpanel.ICameraSwitchView
    public void setOnCameraModeSwitchListener(ICameraSwitchView.OnSwitchChangeListener listener) {
        this.mOnCameraModeSwitchListener = listener;
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public boolean onInterceptTabChange(XTabLayout xTabLayout, int index, boolean tabChange, boolean fromUser) {
        ICameraSwitchView.OnSwitchChangeListener onSwitchChangeListener = this.mOnCameraModeSwitchListener;
        if (onSwitchChangeListener != null) {
            return onSwitchChangeListener.onInterceptTabChange(xTabLayout, index, tabChange, fromUser);
        }
        return false;
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public void onTabChangeStart(XTabLayout xTabLayout, int index, boolean tabChange, boolean fromUser) {
        ICameraSwitchView.OnSwitchChangeListener onSwitchChangeListener = this.mOnCameraModeSwitchListener;
        if (onSwitchChangeListener != null) {
            onSwitchChangeListener.onTabChangeStart(xTabLayout, index, tabChange, fromUser);
        }
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public void onTabChangeEnd(XTabLayout xTabLayout, int index, boolean tabChange, boolean fromUser) {
        ICameraSwitchView.OnSwitchChangeListener onSwitchChangeListener = this.mOnCameraModeSwitchListener;
        if (onSwitchChangeListener != null) {
            onSwitchChangeListener.onTabChangeEnd(xTabLayout, index, tabChange, fromUser);
        }
    }
}
