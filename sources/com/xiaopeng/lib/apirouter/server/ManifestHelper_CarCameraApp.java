package com.xiaopeng.lib.apirouter.server;

import android.os.IBinder;
import android.util.Pair;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ManifestHelper_CarCameraApp implements IManifestHelper {
    public HashMap<String, Pair<IBinder, String>> mapping = new HashMap<>();

    @Override // com.xiaopeng.lib.apirouter.server.IManifestHelper
    public HashMap<String, Pair<IBinder, String>> getMapping() {
        Pair<IBinder, String> pair = new Pair<>(new SpeechOverAllObserverCarCamera_Stub(), SpeechOverAllObserverCarCamera_Manifest.toJsonManifest());
        for (String str : SpeechOverAllObserverCarCamera_Manifest.getKey()) {
            this.mapping.put(str, pair);
        }
        Pair<IBinder, String> pair2 = new Pair<>(new CameraAppVuiObserver_Stub(), CameraAppVuiObserver_Manifest.toJsonManifest());
        for (String str2 : CameraAppVuiObserver_Manifest.getKey()) {
            this.mapping.put(str2, pair2);
        }
        return this.mapping;
    }
}
