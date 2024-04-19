package com.alivc.live.pusher;
/* loaded from: classes.dex */
public enum AlivcVideoEncodeGopEnum {
    GOP_ONE(1),
    GOP_TWO(2),
    GOP_THREE(3),
    GOP_FOUR(4),
    GOP_FIVE(5);
    
    private int mGop;

    AlivcVideoEncodeGopEnum(int i) {
        this.mGop = 3;
        this.mGop = i;
    }

    public int getGop() {
        return this.mGop;
    }
}
