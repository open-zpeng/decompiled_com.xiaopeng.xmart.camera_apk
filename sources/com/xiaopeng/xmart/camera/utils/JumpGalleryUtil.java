package com.xiaopeng.xmart.camera.utils;

import android.content.Intent;
import android.text.TextUtils;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.CameraDefine;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.helper.ClickHelper;
import com.xiaopeng.xmart.camerabase.R;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class JumpGalleryUtil {
    private static final String TAG = "JumpGalleryUtil";

    public static void templateGotoGalleryDetail(String filePath) {
        gotoGalleryDetail(filePath, CameraDefine.TAB_CAMERA_TOP);
    }

    public static void gotoGalleryDetail(String filePath, String currentTab) {
        Intent intent;
        if (ClickHelper.getInstance().isQuickClick(4)) {
            CameraLog.i(TAG, "gotoGalleryDetail too quick click " + filePath + " currentTab:" + currentTab, false);
            return;
        }
        try {
            if (TextUtils.isEmpty(filePath)) {
                intent = new Intent("com.xiaopeng.camera.entry");
                intent.setPackage(IpcConfig.App.CAR_GALLERY);
            } else {
                intent = new Intent("com.xiaopeng.camera.action.VIEW");
                intent.setClassName(IpcConfig.App.CAR_GALLERY, "com.xiaopeng.xmart.cargallery.view.DetailActivity");
                intent.putExtra(CameraDefine.INTENT_EXTRA_ITEM_PATH, filePath);
            }
            CameraLog.i(TAG, "gotoGalleryDetail " + filePath + " currentTab:" + currentTab, false);
            intent.putExtra(CameraDefine.INTENT_EXTRA_TAB, currentTab);
            ArrayList<String> galleryTabList = getGalleryTabList();
            if (!galleryTabList.contains(currentTab)) {
                galleryTabList.add(currentTab);
            }
            intent.putStringArrayListExtra(CameraDefine.INTENT_EXTRA_TAB_LIST, galleryTabList);
            intent.addFlags(335544320);
            App.getInstance().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(R.string.no_gallery);
        }
    }

    private static ArrayList<String> getGalleryTabList() {
        ArrayList<String> arrayList = new ArrayList<>();
        if (CarCameraHelper.getInstance().hasAVM()) {
            arrayList.add(CameraDefine.TAB_CAMERA_360);
        } else {
            arrayList.add(CameraDefine.TAB_CAMERA_BACK);
        }
        if (CarCameraHelper.getInstance().hasDvrCamera()) {
            arrayList.add(CameraDefine.TAB_CAMERA_DVR);
        }
        if (CarCameraHelper.getInstance().hasTopCamera()) {
            arrayList.add(CameraDefine.TAB_CAMERA_TOP);
        }
        return arrayList;
    }
}
