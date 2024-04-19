package com.xiaopeng.xmart.camera.speech;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaopeng.speech.vui.utils.VuiUtils;
import com.xiaopeng.vui.commons.IVuiElementBuilder;
import com.xiaopeng.vui.commons.IVuiElementListener;
import com.xiaopeng.vui.commons.VuiAction;
import com.xiaopeng.vui.commons.VuiElementType;
import com.xiaopeng.vui.commons.model.VuiElement;
import com.xiaopeng.vui.commons.model.VuiEvent;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camerabase.R;
import com.xiaopeng.xui.vui.floatinglayer.VuiFloatingLayerManager;
import com.xiaopeng.xui.widget.XImageView;
import java.util.ArrayList;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class VuiRadio3DButton extends XImageView implements IVuiElementListener {
    public static final int SELECT2D = 1;
    public static final int SELECT3D = 2;
    private static final String TAG = "VuiRadio3DButton";
    public int mCheckIndex;

    public VuiRadio3DButton(Context context) {
        super(context);
        this.mCheckIndex = 1;
    }

    public VuiRadio3DButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCheckIndex = 1;
    }

    public VuiRadio3DButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCheckIndex = 1;
    }

    @Override // com.xiaopeng.vui.commons.IVuiElementListener
    public VuiElement onBuildVuiElement(String s, IVuiElementBuilder iVuiElementBuilder) {
        CameraLog.i(TAG, "onBuildVuiElement s:" + s);
        VuiElement build = new VuiElement.Builder().id(String.valueOf(getId())).type(VuiElementType.CUSTOM.getType()).build();
        VuiElement vuiElement = new VuiElement();
        vuiElement.setId(String.valueOf(getId() + "_0_1"));
        vuiElement.setType(VuiElementType.RADIOGROUP.getType());
        ArrayList arrayList = new ArrayList();
        arrayList.add(creatVuiRadioButton(App.getInstance().getString(R.string.vui_switch_3d), 2, this.mCheckIndex == 2));
        arrayList.add(creatVuiRadioButton(App.getInstance().getString(R.string.vui_switch_2d), 1, this.mCheckIndex == 1));
        vuiElement.setElements(arrayList);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(vuiElement);
        build.setElements(arrayList2);
        return build;
    }

    public void setCheckIndex(int index) {
        this.mCheckIndex = index;
    }

    @Override // com.xiaopeng.vui.commons.IVuiElementListener
    public boolean onVuiElementEvent(View view, VuiEvent vuiEvent) {
        boolean isShow3DButton = CarCameraHelper.getInstance().isShow3DButton();
        String id = vuiEvent.getHitVuiElement().getId();
        CameraLog.i(TAG, "onVuiElementEvent is3DState:" + isShow3DButton + ",vId:" + id + ",id:" + view.getId() + ",view：" + view + ",vuiEvent:" + vuiEvent, false);
        if (TextUtils.equals(id, view.getId() + "_1")) {
            CameraLog.i(TAG, "onVuiElementEvent 2D:", false);
        } else if (TextUtils.equals(id, view.getId() + "_2")) {
            CameraLog.i(TAG, "onVuiElementEvent 3D:", false);
        }
        post(new Runnable() { // from class: com.xiaopeng.xmart.camera.speech.-$$Lambda$VuiRadio3DButton$dB0ci22LjJRUaD67tx-80VXKBeE
            @Override // java.lang.Runnable
            public final void run() {
                VuiRadio3DButton.this.lambda$onVuiElementEvent$0$VuiRadio3DButton();
            }
        });
        return true;
    }

    public /* synthetic */ void lambda$onVuiElementEvent$0$VuiRadio3DButton() {
        performClick();
        VuiFloatingLayerManager.show(this);
    }

    private VuiElement creatVuiRadioButton(String label, int index, boolean value) {
        VuiElement vuiElement = new VuiElement();
        vuiElement.setType(VuiElementType.RADIOBUTTON.getType());
        vuiElement.setId(getId() + "_" + index);
        vuiElement.setLabel(label);
        vuiElement.setActions(VuiAction.SETCHECK.getName());
        JSONObject jSONObject = new JSONObject();
        VuiUtils.generateElementValueJSON(jSONObject, VuiAction.SETCHECK.getName(), Boolean.valueOf(value));
        vuiElement.setValues(new Gson().fromJson(jSONObject.toString(), (Class<Object>) JsonObject.class));
        return vuiElement;
    }
}
