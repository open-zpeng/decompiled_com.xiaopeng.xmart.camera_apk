package com.alivc.debug;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import com.xiaopeng.lib.framework.moduleinterface.appresourcemodule.IAppResourceException;
import java.io.File;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
/* loaded from: classes.dex */
public class DebugViewManager {
    private static final long REFRESH_INTERVAL = 2000;
    private static DebugBigView bigWindow;
    private static WindowManager.LayoutParams bigWindowParams;
    private static Context mContext;
    private static WindowManager mWindowManager;
    private static PushDataView pushDataWindow;
    private static WindowManager.LayoutParams pushDataWindowParams;
    private static PushDiagramView pushDiagramWindow;
    private static WindowManager.LayoutParams pushDiagramWindowParams;
    private static PushLogView pushLogWindow;
    private static WindowManager.LayoutParams pushLogWindowParams;
    private static DebugSmallView smallWindow;
    private static WindowManager.LayoutParams smallWindowParams;
    private static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private static Handler mHandler = new Handler();
    private static AlivcLivePushDebugInfo mAlivcLivePushDebugInfo = null;
    private static Runnable mRunnable = new Runnable() { // from class: com.alivc.debug.DebugViewManager.2
        /* JADX WARN: Type inference failed for: r0v4, types: [com.alivc.debug.DebugViewManager$2$1] */
        @Override // java.lang.Runnable
        public void run() {
            Log.i("DebugView", "====== mRunnable run ======SD_DIR:" + DebugViewManager.SD_DIR);
            new AsyncTask<AlivcLivePushDebugInfo, Void, AlivcLivePushDebugInfo>() { // from class: com.alivc.debug.DebugViewManager.2.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public AlivcLivePushDebugInfo doInBackground(AlivcLivePushDebugInfo... alivcLivePushStatsInfos) {
                    try {
                        if (DebugViewManager.mUpdateDebugInfo != null) {
                            AlivcLivePushDebugInfo unused = DebugViewManager.mAlivcLivePushDebugInfo = DebugViewManager.mUpdateDebugInfo.updateInfo();
                        }
                        if (DebugViewManager.mAlivcLivePushDebugInfo != null) {
                            DebugViewManager.mAlivcLivePushDebugInfo.setCpu(ParameterUtil.getProcessCpuRate());
                            DebugViewManager.mAlivcLivePushDebugInfo.setMemory(ParameterUtil.getRunningAppProcessInfo(DebugViewManager.mContext));
                        }
                    } catch (IllegalStateException unused2) {
                    }
                    return DebugViewManager.mAlivcLivePushDebugInfo;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(AlivcLivePushDebugInfo alivcLivePushDebugInfo) {
                    super.onPostExecute((AnonymousClass1) alivcLivePushDebugInfo);
                    if (DebugViewManager.bigWindow != null) {
                        DebugViewManager.bigWindow.updateInfo(alivcLivePushDebugInfo.getCpu(), alivcLivePushDebugInfo.getMemory());
                    }
                    if (alivcLivePushDebugInfo != null) {
                        if (DebugViewManager.pushDataWindow != null) {
                            DebugViewManager.pushDataWindow.updataData(alivcLivePushDebugInfo);
                        }
                        if (DebugViewManager.pushDiagramWindow != null && alivcLivePushDebugInfo.isPush()) {
                            DebugViewManager.pushDiagramWindow.updateData(alivcLivePushDebugInfo, ParameterUtil.getSecondTime());
                        }
                    }
                    if (DebugViewManager.mHandler != null) {
                        DebugViewManager.mHandler.postDelayed(DebugViewManager.mRunnable, DebugViewManager.REFRESH_INTERVAL);
                    }
                }
            }.execute(new AlivcLivePushDebugInfo[0]);
        }
    };
    private static UpdateDebugInfo mUpdateDebugInfo = null;

    /* loaded from: classes.dex */
    public interface UpdateDebugInfo {
        AlivcLivePushDebugInfo updateInfo();
    }

    public static void copyFaceAsset(Context context) {
    }

    public static void createSmallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new DebugSmallView(context);
            if (smallWindowParams == null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                smallWindowParams = layoutParams;
                layoutParams.type = IAppResourceException.REASON_MGR_DB_ERROR;
                smallWindowParams.format = 1;
                smallWindowParams.flags = 40;
                smallWindowParams.gravity = 51;
                smallWindowParams.width = DebugSmallView.viewWidth;
                smallWindowParams.height = DebugSmallView.viewHeight;
                smallWindowParams.x = width;
                smallWindowParams.y = height / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
        Handler handler = mHandler;
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
        }
    }

    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        if (bigWindow == null) {
            bigWindow = new DebugBigView(context);
            if (bigWindowParams == null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                bigWindowParams = layoutParams;
                layoutParams.x = (width / 2) - (DebugBigView.viewWidth / 2);
                bigWindowParams.y = (height / 2) - (DebugBigView.viewHeight / 2);
                bigWindowParams.type = IAppResourceException.REASON_MGR_DB_ERROR;
                bigWindowParams.format = 1;
                bigWindowParams.flags = 40;
                bigWindowParams.gravity = 51;
                bigWindowParams.width = DebugBigView.viewWidth;
                bigWindowParams.height = DebugBigView.viewHeight;
            }
            bigWindow.setParams(bigWindowParams);
            windowManager.addView(bigWindow, bigWindowParams);
        }
    }

    public static void createPushLogWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (pushLogWindow == null) {
            pushLogWindow = new PushLogView(context);
            if (pushLogWindowParams == null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                pushLogWindowParams = layoutParams;
                layoutParams.type = IAppResourceException.REASON_MGR_DB_ERROR;
                pushLogWindowParams.format = 1;
                pushLogWindowParams.gravity = 17;
            }
        }
        windowManager.addView(pushLogWindow, pushLogWindowParams);
    }

    private static void createPushLogView(Context context) {
        if (pushLogWindow == null) {
            pushLogWindow = new PushLogView(context);
            if (pushLogWindowParams == null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                pushLogWindowParams = layoutParams;
                layoutParams.type = IAppResourceException.REASON_MGR_DB_ERROR;
                pushLogWindowParams.format = 1;
                pushLogWindowParams.gravity = 17;
            }
        }
    }

    public static void createPushDataWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (pushDataWindow == null) {
            pushDataWindow = new PushDataView(context);
            if (pushDataWindowParams == null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                pushDataWindowParams = layoutParams;
                layoutParams.type = IAppResourceException.REASON_MGR_DB_ERROR;
                pushDataWindowParams.format = 1;
                pushDataWindowParams.gravity = 17;
            }
            windowManager.addView(pushDataWindow, pushDataWindowParams);
        }
    }

    public static void createPushDiagramView(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (pushDiagramWindow == null) {
            pushDiagramWindow = new PushDiagramView(context);
            if (pushDiagramWindowParams == null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                pushDiagramWindowParams = layoutParams;
                layoutParams.type = IAppResourceException.REASON_MGR_DB_ERROR;
                pushDiagramWindowParams.format = 1;
                pushDiagramWindowParams.gravity = 17;
            }
        }
        windowManager.addView(pushDiagramWindow, pushDiagramWindowParams);
    }

    public static void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            getWindowManager(context).removeView(smallWindow);
            smallWindow = null;
        }
    }

    public static void removeBigWindow(Context context) {
        if (bigWindow != null) {
            getWindowManager(context).removeView(bigWindow);
            bigWindow = null;
        }
    }

    public static void removePushLogWindow(Context context) {
        if (pushLogWindow != null) {
            getWindowManager(context).removeView(pushLogWindow);
        }
    }

    public static void removePushDataWindow(Context context) {
        if (pushDataWindow != null) {
            getWindowManager(context).removeView(pushDataWindow);
            pushDataWindow = null;
        }
    }

    public static void removePushDiagramWindow(Context context) {
        if (pushDiagramWindow != null) {
            getWindowManager(context).removeView(pushDiagramWindow);
        }
    }

    public static void openBigWindow(Context context) {
        createBigWindow(context);
        Handler handler = mHandler;
        if (handler != null) {
            handler.post(mRunnable);
        }
    }

    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService("window");
        }
        return mWindowManager;
    }

    public static void showView(Context context) {
        mContext = context;
        if (permission(context)) {
            createSmallWindow(context);
            createPushLogView(context);
        }
    }

    public static void hideDebugView(Context context) {
        try {
            removePushLogWindow(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            removeBigWindow(context);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            removePushDataWindow(context);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            removeSmallWindow(context);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            removePushDiagramWindow(context);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        pushLogWindow = null;
        pushDiagramWindow = null;
        Handler handler = mHandler;
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }

    public static void recordFunction(final String text) {
        if (pushLogWindow != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.alivc.debug.DebugViewManager.1
                @Override // java.lang.Runnable
                public void run() {
                    DebugViewManager.pushLogWindow.updateData(text);
                }
            });
        }
    }

    public static boolean permission(Context context) {
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context)) {
            return true;
        }
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
        context.startActivity(intent);
        return false;
    }

    public static void setUpdateDebigListener(UpdateDebugInfo updateDebugListener) {
        mUpdateDebugInfo = updateDebugListener;
    }
}
