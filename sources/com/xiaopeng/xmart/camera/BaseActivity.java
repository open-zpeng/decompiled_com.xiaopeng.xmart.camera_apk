package com.xiaopeng.xmart.camera;

import android.content.Intent;
import com.bumptech.glide.Glide;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmart.camera.helper.BIHelper;
import com.xiaopeng.xmart.camera.helper.CameraStorageCheckHelper;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
/* loaded from: classes.dex */
public abstract class BaseActivity extends VuiActivity {
    private static final String ACTION_STORAGE_CLEAN = "com.xiaopeng.intent.action.ACTION_STORAGE_OPEN";
    private static final String TAG = "CameraBaseActivity";
    private XDialog mSpaceDialog;
    protected long mStartRecordTime;

    protected abstract void checkAction(long delayTime);

    protected abstract void gotoGallery();

    protected abstract void initView();

    protected boolean isTopPresenter() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$BaseActivity$pI2FO-Su0vllgbPXkgVxhRTy1nU
            @Override // java.lang.Runnable
            public final void run() {
                BaseActivity.this.lambda$onResume$0$BaseActivity();
            }
        });
    }

    public /* synthetic */ void lambda$onResume$0$BaseActivity() {
        if (CameraStorageCheckHelper.getInstance().checkAvailableStorage()) {
            CameraLog.d(TAG, "storage not enough:", false);
            showStorageWarningDialog();
            return;
        }
        CameraLog.d(TAG, "storage enough:", false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        CameraLog.d(TAG, "onNewIntent...", false);
        checkAction(1000L);
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        uploadStartBI();
    }

    @Override // android.app.Activity
    public void recreate() {
        super.recreate();
        CameraLog.d(TAG, "recreate", false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        XDialog xDialog = this.mSpaceDialog;
        if (xDialog == null || xDialog.getDialog() == null || !this.mSpaceDialog.getDialog().isShowing()) {
            return;
        }
        this.mSpaceDialog.getDialog().dismiss();
        this.mSpaceDialog = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        Glide.get(this).clearMemory();
    }

    public void showStorageWarningDialog() {
        ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$BaseActivity$IrYOuRLaLg-uEN4-KONtEa0Eq20
            @Override // java.lang.Runnable
            public final void run() {
                BaseActivity.this.lambda$showStorageWarningDialog$2$BaseActivity();
            }
        });
    }

    public /* synthetic */ void lambda$showStorageWarningDialog$2$BaseActivity() {
        if (this.mSpaceDialog == null) {
            this.mSpaceDialog = new XDialog(this);
        }
        if (this.mSpaceDialog.getDialog().isShowing()) {
            return;
        }
        this.mSpaceDialog.setTitle(com.xiaopeng.xmart.camerabase.R.string.dialog_storage_low_warning_title);
        this.mSpaceDialog.setMessage(com.xiaopeng.xmart.camerabase.R.string.dialog_storage_low_warning_content);
        this.mSpaceDialog.setNegativeButton(com.xiaopeng.xmart.camerabase.R.string.dialog_btn_cancel, (XDialogInterface.OnClickListener) null);
        this.mSpaceDialog.setPositiveButton(com.xiaopeng.xmart.camerabase.R.string.dialog_btn_clean, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xmart.camera.-$$Lambda$BaseActivity$ZuSdcq3ZfRMfOpGqJwFI2zjjGZ4
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public final void onClick(XDialog xDialog, int i) {
                BaseActivity.this.lambda$showStorageWarningDialog$1$BaseActivity(xDialog, i);
            }
        });
        this.mSpaceDialog.getDialog().setCanceledOnTouchOutside(false);
        this.mSpaceDialog.show();
    }

    public /* synthetic */ void lambda$showStorageWarningDialog$1$BaseActivity(XDialog xDialog, int i) {
        jump2StorageCleanPage();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void uploadStartBI() {
        CameraLog.d(TAG, "uploadStartBI", false);
        Intent intent = getIntent();
        if (intent == null) {
            CameraLog.e(TAG, "uploadStartBI, intent is null", false);
        } else {
            BIHelper.getInstance().uploadStartFrom(intent.getAction());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void uploadRecordBI(boolean start, int duration, int source) {
        BIHelper.getInstance().uploadRecordBI(isTopPresenter(), start, duration, source, CarCameraHelper.getInstance().getCarCamera().get360CameraType());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void uploadCaptureBI(int source) {
        BIHelper.getInstance().uploadCaptureBI(isTopPresenter(), CarCameraHelper.getInstance().getCarCamera().get360CameraType(), source);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    private void jump2StorageCleanPage() {
        Intent intent = new Intent();
        intent.setAction(ACTION_STORAGE_CLEAN);
        intent.setPackage("com.xiaopeng.car.settings");
        intent.setFlags(16777216);
        sendBroadcast(intent);
    }
}
