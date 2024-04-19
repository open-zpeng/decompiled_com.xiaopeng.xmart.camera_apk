package vendor.xpeng.imgproc.V1_0;

import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class Status {
    public static final byte FAILURE = 1;
    public static final byte SUCCESS = 0;
    public static final byte UNSUPPORTED = 2;

    public static final String toString(byte b) {
        return b == 0 ? "SUCCESS" : b == 1 ? "FAILURE" : b == 2 ? "UNSUPPORTED" : "0x" + Integer.toHexString(Byte.toUnsignedInt(b));
    }

    public static final String dumpBitfield(byte b) {
        byte b2;
        ArrayList arrayList = new ArrayList();
        arrayList.add("SUCCESS");
        if ((b & 1) == 1) {
            arrayList.add("FAILURE");
            b2 = (byte) 1;
        } else {
            b2 = 0;
        }
        if ((b & 2) == 2) {
            arrayList.add("UNSUPPORTED");
            b2 = (byte) (b2 | 2);
        }
        if (b != b2) {
            arrayList.add("0x" + Integer.toHexString(Byte.toUnsignedInt((byte) (b & (~b2)))));
        }
        return String.join(" | ", arrayList);
    }
}
