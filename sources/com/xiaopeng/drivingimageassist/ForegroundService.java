package com.xiaopeng.drivingimageassist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
/* loaded from: classes.dex */
public class ForegroundService extends Service {
    private static final String CHANNEL_ID = "XPENG";
    private static final String CHANNEL_NAME = "ForegroundService";
    private static final String CONTENT = "ForegroundService is running";
    private static final String TITLE = "Xmart";

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.i(CHANNEL_NAME, "onCreate");
        startNotification();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(CHANNEL_NAME, "onStartCommand");
        return 1;
    }

    private void startNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, 3);
        ((NotificationManager) getApplicationContext().getSystemService("notification")).createNotificationChannel(notificationChannel);
        startForeground(163, new NotificationCompat.Builder(this, notificationChannel.getId()).setSmallIcon(17301674).setContentTitle(TITLE).setContentText(CONTENT).setPriority(3).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
    }
}
