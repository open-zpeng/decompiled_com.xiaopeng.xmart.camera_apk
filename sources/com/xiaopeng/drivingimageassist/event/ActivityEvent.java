package com.xiaopeng.drivingimageassist.event;

import android.content.ComponentName;
/* loaded from: classes.dex */
public class ActivityEvent {
    private ComponentName mComponentName;

    public ActivityEvent(ComponentName componentName) {
        this.mComponentName = componentName;
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }
}
