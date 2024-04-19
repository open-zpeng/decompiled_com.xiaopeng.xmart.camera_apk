package com.xiaopeng.drivingimageassist.event;
/* loaded from: classes.dex */
public class GearLevelEvent {
    int gearLevel;

    public static boolean isRLevel(int level) {
        return level == 3;
    }

    public int getGearLevel() {
        return this.gearLevel;
    }

    public GearLevelEvent(int gearLevel) {
        this.gearLevel = gearLevel;
    }
}
