package com.xiaopeng.drivingimageassist;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import com.xiaopeng.drivingimageassist.view.UIConfigManger;
/* loaded from: classes.dex */
public class BaseUIActivity extends Activity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getWindow().setAttributes(getLayoutParams());
    }

    protected int getLayoutId() {
        return UIConfigManger.instance().getUIConfig().getLayout();
    }

    protected WindowManager.LayoutParams getLayoutParams() {
        return UIConfigManger.instance().getUIConfig().getLayoutParams(this);
    }
}
