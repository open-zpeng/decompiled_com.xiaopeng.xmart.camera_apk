package com.xiaopeng.xmart.camera.helper;

import android.car.Car;
import android.graphics.drawable.Drawable;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camerabase.R;
import com.xiaopeng.xui.theme.XThemeManager;
/* loaded from: classes.dex */
public class UIResLoader {
    private static final String CAR_IMG_PREFIX;
    public static final String TAG = "UIResLoader";
    private Drawable mAppBgDrawableDay;
    private Drawable mAppBgDrawableNight;
    private Drawable mCamera3602DDrawableDay;
    private Drawable mCamera3602DDrawableNight;
    private Drawable mCamera3603DDrawableDay;
    private Drawable mCamera3603DDrawableNight;
    private Drawable mCamera360CircleBgDay;
    private Drawable mCamera360CircleBgNight;
    private Drawable mCamera360CirclePressedBgDay;
    private Drawable mCamera360CirclePressedBgNight;
    private Drawable mCamera360CircleTransBgDay;
    private Drawable mCamera360CircleTransBgNight;
    private Drawable mCamera360OffDrawableDay;
    private Drawable mCamera360OffDrawableNight;
    private Drawable mCamera360OnDrawableDay;
    private Drawable mCamera360OnDrawableNight;
    private Drawable mCamera360SelectedDownDrawableDay;
    private Drawable mCamera360SelectedDownDrawableNight;
    private Drawable mCamera360SelectedLeftDrawableDay;
    private Drawable mCamera360SelectedLeftDrawableNight;
    private Drawable mCamera360SelectedRightDrawableDay;
    private Drawable mCamera360SelectedRightDrawableNight;
    private Drawable mCamera360SelectedUpDrawableDay;
    private Drawable mCamera360SelectedUpDrawableNight;
    private Drawable mCamera360UnSelectedDownDrawableDay;
    private Drawable mCamera360UnSelectedDownDrawableNight;
    private Drawable mCamera360UnSelectedLeftDrawableDay;
    private Drawable mCamera360UnSelectedLeftDrawableNight;
    private Drawable mCamera360UnSelectedRightDrawableDay;
    private Drawable mCamera360UnSelectedRightDrawableNight;
    private Drawable mCamera360UnSelectedUpDrawableDay;
    private Drawable mCamera360UnSelectedUpDrawableNight;
    private Drawable mCameraInnerCircleDay;
    private Drawable mCameraInnerCircleNight;
    private Drawable mCameraInnerCircleRecord;
    private Drawable mCameraInnerCircleRecording;
    private Drawable mCameraLightDrawableDay;
    private Drawable mCameraLightDrawableNight;
    private Drawable mCameraOuterCircleDay;
    private Drawable mCameraOuterCircleNight;
    protected Drawable mCarBodyDrawable;

    static {
        String xpCduType = Car.getXpCduType();
        LogUtils.i(TAG, "carType = " + xpCduType);
        xpCduType.hashCode();
        if (xpCduType.equals("Q3A")) {
            CAR_IMG_PREFIX = "d55a_img_carbody_color_";
        } else {
            CAR_IMG_PREFIX = "img_carbody_color_";
        }
    }

    public UIResLoader() {
        init();
    }

    protected void init() {
        getCarBodyDrawable();
        getCamera360SelectedUpDrawable();
        getCamera360UnSelectedUpDrawable();
        getCamera360SelectedDownDrawable();
        getCamera360UnSelectedDownDrawable();
        getCamera360SelectedLeftDrawable();
        getCamera360UnSelectedLeftDrawable();
        getCamera360SelectedRightDrawable();
        getCamera360UnSelectedRightDrawable();
        getCamera3602DDrawable();
        getCamera3603DDrawable();
        getCamera360OnDrawable();
        getCamera360OffDrawable();
        getCamera360CircleTransBgDrawable();
        getCamera360CircleBgDrawable();
        getCameraLightDrawable();
        getCamera360CirclePressedBgDrawable();
        getCameraInnerCircleRecordDrawable();
        getCameraInnerCircleRecordingDrawable();
        getCameraOuterCircleDrawable();
        getCameraInnerCircleDrawable();
    }

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        private static final UIResLoader sINSTANCE = new UIResLoader();

        private SingletonHolder() {
        }
    }

    public static UIResLoader getInstance() {
        return SingletonHolder.sINSTANCE;
    }

    public Drawable getCarBodyDrawable() {
        if (this.mCarBodyDrawable == null) {
            int bodyColor = Config.getBodyColor();
            int carBodyDrawableId = getCarBodyDrawableId(bodyColor);
            if (carBodyDrawableId == 0 && (carBodyDrawableId = getCarBodyDrawableId(1)) == 0) {
                CameraLog.d(TAG, "defaultCarBodyId is not exist ", false);
                return null;
            }
            CameraLog.d(TAG, "colorCode: " + bodyColor + "cameraBodyDrawableId: " + carBodyDrawableId, false);
            this.mCarBodyDrawable = App.getInstance().getDrawable(carBodyDrawableId);
        }
        return this.mCarBodyDrawable;
    }

    public int getCarBodyDrawableId(int colorCode) {
        return App.getInstance().getResources().getIdentifier(CAR_IMG_PREFIX + colorCode, "drawable", App.getInstance().getPackageName());
    }

    public Drawable getCamera360SelectedUpDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360SelectedUpDrawableNight == null) {
                this.mCamera360SelectedUpDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_selected_up);
            }
            return this.mCamera360SelectedUpDrawableNight;
        }
        if (this.mCamera360SelectedUpDrawableDay == null) {
            this.mCamera360SelectedUpDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_selected_up);
        }
        return this.mCamera360SelectedUpDrawableDay;
    }

    public Drawable getCamera360UnSelectedUpDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360UnSelectedUpDrawableNight == null) {
                this.mCamera360UnSelectedUpDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_up);
            }
            return this.mCamera360UnSelectedUpDrawableNight;
        }
        if (this.mCamera360UnSelectedUpDrawableDay == null) {
            this.mCamera360UnSelectedUpDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_up);
        }
        return this.mCamera360UnSelectedUpDrawableDay;
    }

    public Drawable getCamera360SelectedDownDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360SelectedDownDrawableNight == null) {
                this.mCamera360SelectedDownDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_selected_down);
            }
            return this.mCamera360SelectedDownDrawableNight;
        }
        if (this.mCamera360SelectedDownDrawableDay == null) {
            this.mCamera360SelectedDownDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_selected_down);
        }
        return this.mCamera360SelectedDownDrawableDay;
    }

    public Drawable getCamera360UnSelectedDownDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360UnSelectedDownDrawableNight == null) {
                this.mCamera360UnSelectedDownDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_down);
            }
            return this.mCamera360UnSelectedDownDrawableNight;
        }
        if (this.mCamera360UnSelectedDownDrawableDay == null) {
            this.mCamera360UnSelectedDownDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_down);
        }
        return this.mCamera360UnSelectedDownDrawableDay;
    }

    public Drawable getCamera360SelectedLeftDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360SelectedLeftDrawableNight == null) {
                this.mCamera360SelectedLeftDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_selected_left);
            }
            return this.mCamera360SelectedLeftDrawableNight;
        }
        if (this.mCamera360SelectedLeftDrawableDay == null) {
            this.mCamera360SelectedLeftDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_selected_left);
        }
        return this.mCamera360SelectedLeftDrawableDay;
    }

    public Drawable getCamera360UnSelectedLeftDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360UnSelectedLeftDrawableNight == null) {
                this.mCamera360UnSelectedLeftDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_left);
            }
            return this.mCamera360UnSelectedLeftDrawableNight;
        }
        if (this.mCamera360UnSelectedLeftDrawableDay == null) {
            this.mCamera360UnSelectedLeftDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_left);
        }
        return this.mCamera360UnSelectedLeftDrawableDay;
    }

    public Drawable getCamera360SelectedRightDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360SelectedRightDrawableNight == null) {
                this.mCamera360SelectedRightDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_selected_right);
            }
            return this.mCamera360SelectedRightDrawableNight;
        }
        if (this.mCamera360SelectedRightDrawableDay == null) {
            this.mCamera360SelectedRightDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_selected_right);
        }
        return this.mCamera360SelectedRightDrawableDay;
    }

    public Drawable getCamera360UnSelectedRightDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360UnSelectedRightDrawableNight == null) {
                this.mCamera360UnSelectedRightDrawableNight = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_right);
            }
            return this.mCamera360UnSelectedRightDrawableNight;
        }
        if (this.mCamera360UnSelectedRightDrawableDay == null) {
            this.mCamera360UnSelectedRightDrawableDay = App.getInstance().getDrawable(R.drawable.camera_pano_unselected_right);
        }
        return this.mCamera360UnSelectedRightDrawableDay;
    }

    public Drawable getCamera3602DDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera3602DDrawableNight == null) {
                this.mCamera3602DDrawableNight = App.getInstance().getDrawable(R.drawable.pano_2d_btn);
            }
            return this.mCamera3602DDrawableNight;
        }
        if (this.mCamera3602DDrawableDay == null) {
            this.mCamera3602DDrawableDay = App.getInstance().getDrawable(R.drawable.pano_2d_btn);
        }
        return this.mCamera3602DDrawableDay;
    }

    public Drawable getCamera3603DDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera3603DDrawableNight == null) {
                this.mCamera3603DDrawableNight = App.getInstance().getDrawable(R.drawable.pano_3d_btn);
            }
            return this.mCamera3603DDrawableNight;
        }
        if (this.mCamera3603DDrawableDay == null) {
            this.mCamera3603DDrawableDay = App.getInstance().getDrawable(R.drawable.pano_3d_btn);
        }
        return this.mCamera3603DDrawableDay;
    }

    public Drawable getCamera360OnDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360OnDrawableNight == null) {
                this.mCamera360OnDrawableNight = App.getInstance().getDrawable(R.drawable.ic_switch_360_on);
            }
            return this.mCamera360OnDrawableNight;
        }
        if (this.mCamera360OnDrawableDay == null) {
            this.mCamera360OnDrawableDay = App.getInstance().getDrawable(R.drawable.ic_switch_360_on);
        }
        return this.mCamera360OnDrawableDay;
    }

    public Drawable getCamera360OffDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360OffDrawableNight == null) {
                this.mCamera360OffDrawableNight = App.getInstance().getDrawable(R.drawable.ic_switch_360_off);
            }
            return this.mCamera360OffDrawableNight;
        }
        if (this.mCamera360OffDrawableDay == null) {
            this.mCamera360OffDrawableDay = App.getInstance().getDrawable(R.drawable.ic_switch_360_off);
        }
        return this.mCamera360OffDrawableDay;
    }

    public Drawable getCamera360CircleTransBgDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360CircleTransBgNight == null) {
                this.mCamera360CircleTransBgNight = App.getInstance().getDrawable(R.drawable.camera_pano_circle_bg_4_trans);
            }
            return this.mCamera360CircleTransBgNight;
        }
        if (this.mCamera360CircleTransBgDay == null) {
            this.mCamera360CircleTransBgDay = App.getInstance().getDrawable(R.drawable.camera_pano_circle_bg_4_trans);
        }
        return this.mCamera360CircleTransBgDay;
    }

    public Drawable getCamera360CircleBgDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360CircleBgNight == null) {
                int i = R.drawable.camera_pano_circle_bg_4;
                if (!CarCameraHelper.getInstance().hasAVM()) {
                    i = R.drawable.camera_pano_circle_bg_1;
                }
                this.mCamera360CircleBgNight = App.getInstance().getDrawable(i);
            }
            return this.mCamera360CircleBgNight;
        }
        if (this.mCamera360CircleBgDay == null) {
            int i2 = R.drawable.camera_pano_circle_bg_4;
            if (!CarCameraHelper.getInstance().hasAVM()) {
                i2 = R.drawable.camera_pano_circle_bg_1;
            }
            this.mCamera360CircleBgDay = App.getInstance().getDrawable(i2);
        }
        return this.mCamera360CircleBgDay;
    }

    public Drawable getCameraLightDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCameraLightDrawableNight == null) {
                this.mCameraLightDrawableNight = App.getInstance().getDrawable(R.drawable.camera_light);
            }
            return this.mCameraLightDrawableNight;
        }
        if (this.mCameraLightDrawableDay == null) {
            this.mCameraLightDrawableDay = App.getInstance().getDrawable(R.drawable.camera_light);
        }
        return this.mCameraLightDrawableDay;
    }

    public Drawable getCamera360CirclePressedBgDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCamera360CirclePressedBgNight == null) {
                this.mCamera360CirclePressedBgNight = App.getInstance().getDrawable(R.drawable.camera_pano_circle_pressed_bg);
            }
            return this.mCamera360CirclePressedBgNight;
        }
        if (this.mCamera360CirclePressedBgDay == null) {
            this.mCamera360CirclePressedBgDay = App.getInstance().getDrawable(R.drawable.camera_pano_circle_pressed_bg);
        }
        return this.mCamera360CirclePressedBgDay;
    }

    public Drawable getCameraInnerCircleRecordDrawable() {
        if (this.mCameraInnerCircleRecord == null) {
            this.mCameraInnerCircleRecord = App.getInstance().getDrawable(R.drawable.camera_inner_circle_record);
        }
        return this.mCameraInnerCircleRecord;
    }

    public Drawable getCameraInnerCircleRecordingDrawable() {
        if (this.mCameraInnerCircleRecording == null) {
            this.mCameraInnerCircleRecording = App.getInstance().getDrawable(R.drawable.camera_inner_circle_recording);
        }
        return this.mCameraInnerCircleRecording;
    }

    public Drawable getCameraOuterCircleDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCameraOuterCircleNight == null) {
                this.mCameraOuterCircleNight = App.getInstance().getDrawable(R.drawable.camera_outer_circle);
            }
            return this.mCameraOuterCircleNight;
        }
        if (this.mCameraOuterCircleDay == null) {
            this.mCameraOuterCircleDay = App.getInstance().getDrawable(R.drawable.camera_outer_circle);
        }
        return this.mCameraOuterCircleDay;
    }

    public Drawable getCameraInnerCircleDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mCameraInnerCircleNight == null) {
                this.mCameraInnerCircleNight = App.getInstance().getDrawable(R.drawable.camera_inner_circle);
            }
            return this.mCameraInnerCircleNight;
        }
        if (this.mCameraInnerCircleDay == null) {
            this.mCameraInnerCircleDay = App.getInstance().getDrawable(R.drawable.camera_inner_circle);
        }
        return this.mCameraInnerCircleDay;
    }

    public Drawable getAppBgDrawable() {
        if (XThemeManager.isNight(App.getInstance())) {
            if (this.mAppBgDrawableNight == null) {
                this.mAppBgDrawableNight = App.getInstance().getDrawable(R.drawable.x_bg_app);
            }
            return this.mAppBgDrawableNight;
        }
        if (this.mAppBgDrawableDay == null) {
            this.mAppBgDrawableDay = App.getInstance().getDrawable(R.drawable.x_bg_app);
        }
        return this.mAppBgDrawableDay;
    }
}
