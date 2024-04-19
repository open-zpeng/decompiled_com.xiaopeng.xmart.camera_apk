package com.xiaopeng.xmart.camera.speech;

import android.text.TextUtils;
import com.xiaopeng.speech.vui.actor.BaseVuiEventActor;
import com.xiaopeng.vui.commons.VuiAction;
import com.xiaopeng.vui.commons.model.VuiElement;
import com.xiaopeng.vui.commons.model.VuiEvent;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.IVuiCameraView;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camerabase.R;
/* loaded from: classes.dex */
public class AvmControllerVuiEventActor extends BaseVuiEventActor {
    private static final String TAG = "AvmControllerVuiEventActor";
    private String mAction;
    private IVuiCameraView mIVuiCameraView;
    private VuiEvent mVuiEvent;

    public AvmControllerVuiEventActor(IVuiCameraView vuiCameraView, VuiEvent vuiEvent) {
        this.mVuiEvent = vuiEvent;
        this.mIVuiCameraView = vuiCameraView;
        VuiElement hitVuiElement = vuiEvent.getHitVuiElement();
        if (hitVuiElement == null || hitVuiElement.resultActions == null || hitVuiElement.resultActions.isEmpty()) {
            return;
        }
        this.mAction = hitVuiElement.resultActions.get(0);
    }

    @Override // com.xiaopeng.speech.vui.actor.IVuiEventActor
    public void execute() {
        if (VuiAction.SETVALUE.getName().equals(this.mAction)) {
            VuiEvent vuiEvent = this.mVuiEvent;
            String str = (String) vuiEvent.getEventValue(vuiEvent);
            CameraLog.i(TAG, "execute currState:" + str, false);
            int i = CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
            if (TextUtils.equals(str, App.getInstance().getString(R.string.vui_left_mode))) {
                i = CarCameraHelper.getInstance().is3DMode() ? 6 : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_LEFT;
            } else if (TextUtils.equals(str, App.getInstance().getString(R.string.vui_right_mode))) {
                i = CarCameraHelper.getInstance().is3DMode() ? 7 : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_RIGHT;
            } else if (TextUtils.equals(str, App.getInstance().getString(R.string.vui_front_mode))) {
                i = CarCameraHelper.getInstance().is3DMode() ? 4 : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_FRONT;
            } else if (TextUtils.equals(str, App.getInstance().getString(R.string.vui_rear_mode))) {
                i = CarCameraHelper.getInstance().is3DMode() ? 5 : CameraDefine.CAMERA_PANO.DIRECTION.DIRECTION_2D_REAR;
            }
            IVuiCameraView iVuiCameraView = this.mIVuiCameraView;
            if (iVuiCameraView != null) {
                iVuiCameraView.vuiAvmSwitchHandle(i);
            }
        }
    }
}
