package vendor.xpeng.imgproc.V1_0;

import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class NotifyCallbackMsgType {
    public static final int DONE = 2;
    public static final int ERROR = 1;
    public static final int OTHERS = 64;
    public static final int PROGRESS = 4;
    public static final int RESERVED = 256;

    public static final String toString(int i) {
        return i == 1 ? "ERROR" : i == 2 ? "DONE" : i == 4 ? "PROGRESS" : i == 64 ? "OTHERS" : i == 256 ? "RESERVED" : "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("ERROR");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("DONE");
            i2 |= 2;
        }
        if ((i & 4) == 4) {
            arrayList.add("PROGRESS");
            i2 |= 4;
        }
        if ((i & 64) == 64) {
            arrayList.add("OTHERS");
            i2 |= 64;
        }
        if ((i & 256) == 256) {
            arrayList.add("RESERVED");
            i2 |= 256;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
