package com.xiaopeng.xmart.camera.manager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.bumptech.glide.Glide;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.define.Config;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import java.util.concurrent.ExecutionException;
/* loaded from: classes.dex */
public class LoginManager {
    public static final String ACCOUNT_TYPE_XP_VEHICLE = "com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE";
    public static final String ACCOUNT_USER_TYPE = "user_type";
    public static final String APP_ID = "xp_car_camera_biz";
    private static final String CARACCOUNT_NAME = "com.xiaopeng.caraccount";
    private static final String DIALOG_QR_NAME = "com.xiaopeng.xvs.account.ACTION_ACCOUNT_DIALOG_QR_REQUEST";
    private static LoginManager INSTANCE = null;
    private static final String TAG = "LoginManager";
    public static final String USER_DATA_EXTRA_AVATAR = "avatar";
    public static final String USER_DATA_EXTRA_UID = "uid";
    public static final String USER_DATA_EXTRA_UPDATE = "update";
    private String avatarFilePath;
    private AccountManager mAccountManager;
    private String userName;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LoginManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoginManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.mAccountManager = AccountManager.get(context);
    }

    public void register(OnAccountsUpdateListener listener) {
        if (this.mAccountManager == null) {
            CameraLog.d(TAG, "mAccountManager = null, you should init first! ");
        } else if (Build.VERSION.SDK_INT >= 26) {
            this.mAccountManager.addOnAccountsUpdatedListener(listener, null, true, new String[]{"com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE"});
        }
    }

    public void unregister(OnAccountsUpdateListener listener) {
        if (this.mAccountManager == null) {
            CameraLog.d(TAG, "mAccountManager = null, you should init first! ");
        } else if (Build.VERSION.SDK_INT >= 26) {
            this.mAccountManager.removeOnAccountsUpdatedListener(listener);
        }
    }

    private void login() {
        Intent intent = new Intent();
        intent.setPackage("com.xiaopeng.caraccount");
        intent.setAction("com.xiaopeng.xvs.account.ACTION_ACCOUNT_DIALOG_QR_REQUEST");
        App.getInstance().startService(intent);
    }

    public Account getCurrentAccountInfo() {
        AccountManager accountManager = this.mAccountManager;
        if (accountManager == null) {
            CameraLog.d(TAG, "mAccountManager = null, you should init first! ");
            return null;
        }
        Account[] accountsByType = accountManager.getAccountsByType("com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE");
        if (accountsByType.length > 0) {
            Account account = accountsByType[0];
            CameraLog.d(TAG, "getCurrentAccountInfo accounts.length=" + accountsByType.length + ";account[0].name=" + account.name, false);
            try {
                String userData = this.mAccountManager.getUserData(account, "avatar");
                String userData2 = this.mAccountManager.getUserData(account, "update");
                String userData3 = this.mAccountManager.getUserData(account, "uid");
                String userData4 = this.mAccountManager.getUserData(account, "user_type");
                updateAvatar(userData);
                this.userName = account.name;
                CameraLog.d(TAG, "getCurrentAccountInfo name=" + account.name + ";头像=" + userData + ";是否是更新=" + userData2 + ";账号类型=" + userData4 + ";uid=" + userData3, false);
            } catch (Exception e) {
                CameraLog.d(TAG, "getCurrentAccountInfo Exception=" + e.getMessage(), false);
            }
            return account;
        }
        CameraLog.d(TAG, "getCurrentAccountInfo account is empty", false);
        return null;
    }

    private void updateAvatar(final String avatarPath) {
        ThreadPoolHelper.getInstance().execute(new Runnable() { // from class: com.xiaopeng.xmart.camera.manager.-$$Lambda$LoginManager$whhWdRxHpD49dbZDjXxfQNbNtkc
            @Override // java.lang.Runnable
            public final void run() {
                LoginManager.this.lambda$updateAvatar$0$LoginManager(avatarPath);
            }
        });
    }

    public /* synthetic */ void lambda$updateAvatar$0$LoginManager(String str) {
        try {
            this.avatarFilePath = Glide.with(App.getInstance()).load(str + "?x-oss-process=image/resize,h_72").downloadOnly(10, 10).get().getAbsolutePath();
            CameraLog.d(TAG, "filePath=" + this.avatarFilePath, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e2) {
            e2.printStackTrace();
        }
    }

    public boolean isLogin() {
        if (Config.isSupportAuth() && getCurrentAccountInfo() == null) {
            login();
            return false;
        }
        return true;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getAvatarPath() {
        CameraLog.d(TAG, this.avatarFilePath + "", false);
        return this.avatarFilePath;
    }

    public void clearAccount() {
        this.avatarFilePath = null;
        this.userName = null;
    }
}
