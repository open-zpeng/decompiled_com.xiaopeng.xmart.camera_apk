package com.alivc.live.pusher;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import com.xiaopeng.libconfig.settings.SettingsUtil;
/* loaded from: classes.dex */
public class a {
    private Context a;
    private AudioManager b;
    private BluetoothHeadset d;
    private boolean e = false;
    private boolean f = false;
    private boolean g = false;
    private InterfaceC0023a h = null;
    private BluetoothProfile.ServiceListener i = new BluetoothProfile.ServiceListener() { // from class: com.alivc.live.pusher.a.1
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d("BluetoothHeadsetUtils", "Profile listener onServiceConnected");
            a.this.d = (BluetoothHeadset) bluetoothProfile;
            if (a.this.d.getConnectedDevices().size() > 0) {
                a.this.c();
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.d("BluetoothHeadsetUtils", "Profile listener onServiceDisconnected");
            if (a.this.d != null) {
                a.this.d();
            }
        }
    };
    private BroadcastReceiver j = new BroadcastReceiver() { // from class: com.alivc.live.pusher.a.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                Log.d("BluetoothHeadsetUtils", "Action = " + action + " State = " + intExtra);
                if (intExtra == 2) {
                    Log.d("BluetoothHeadsetUtils", "Headset connected");
                    if (a.this.d != null) {
                        a.this.c();
                    } else {
                        a.this.c.getProfileProxy(a.this.a, a.this.i, 1);
                    }
                } else if (intExtra == 0) {
                    Log.d("BluetoothHeadsetUtils", "Headset disconnected");
                    if (a.this.d != null) {
                        a.this.d();
                    }
                }
            } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
                int intExtra2 = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 10);
                if (intExtra2 == 12) {
                    Log.d("BluetoothHeadsetUtils", "Headset audio connected");
                    a.this.e = true;
                    if (a.this.h != null) {
                        a.this.h.onBlueTooth(true);
                    }
                } else if (intExtra2 == 10) {
                    Log.d("BluetoothHeadsetUtils", "Headset audio disconnected");
                    a.this.e = false;
                    if (a.this.h != null) {
                        a.this.h.onBlueTooth(false);
                    }
                }
            }
        }
    };
    private BluetoothAdapter c = BluetoothAdapter.getDefaultAdapter();

    /* renamed from: com.alivc.live.pusher.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public interface InterfaceC0023a {
        void onBlueTooth(boolean z);
    }

    public a(Context context) {
        this.a = context;
        this.b = (AudioManager) this.a.getSystemService("audio");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        this.b.setMode(3);
        this.b.startBluetoothSco();
        this.b.setBluetoothScoOn(true);
        InterfaceC0023a interfaceC0023a = this.h;
        if (interfaceC0023a != null) {
            interfaceC0023a.onBlueTooth(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.b.setMode(0);
        this.b.stopBluetoothSco();
        this.b.setBluetoothScoOn(false);
        InterfaceC0023a interfaceC0023a = this.h;
        if (interfaceC0023a != null) {
            interfaceC0023a.onBlueTooth(false);
        }
    }

    public void a() {
        if (this.f) {
            if (this.d != null) {
                d();
                this.c.closeProfileProxy(1, this.d);
                this.d = null;
            }
            if (this.g) {
                try {
                    this.a.unregisterReceiver(this.j);
                } catch (Exception unused) {
                    LogUtil.d(SettingsUtil.PAGE_BLUETOOTH, "unregisterReceiver exception");
                }
                this.g = false;
            }
            this.f = false;
        }
    }

    public void a(InterfaceC0023a interfaceC0023a) {
        this.h = interfaceC0023a;
        if (this.f || this.c == null || !this.b.isBluetoothScoAvailableOffCall()) {
            return;
        }
        this.a.registerReceiver(this.j, new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"));
        this.a.registerReceiver(this.j, new IntentFilter("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED"));
        this.g = true;
        if (this.c.isEnabled()) {
            this.c.getProfileProxy(this.a, this.i, 1);
        }
        this.f = true;
    }

    public boolean b() {
        return this.e;
    }
}
