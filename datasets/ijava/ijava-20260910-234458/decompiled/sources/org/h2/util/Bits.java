package org.h2.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.UUID;

/* loaded from: ijava.jar:org/h2/util/Bits.class */
public final class Bits {
    public static final VarHandle INT_VH_BE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN);
    public static final VarHandle INT_VH_LE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    public static final VarHandle LONG_VH_BE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.BIG_ENDIAN);
    public static final VarHandle LONG_VH_LE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    public static final VarHandle DOUBLE_VH_BE = MethodHandles.byteArrayViewVarHandle(double[].class, ByteOrder.BIG_ENDIAN);
    public static final VarHandle DOUBLE_VH_LE = MethodHandles.byteArrayViewVarHandle(double[].class, ByteOrder.LITTLE_ENDIAN);

    public static byte[] uuidToBytes(long j, long j2) {
        byte[] bArr = new byte[16];
        LONG_VH_BE.set(bArr, 0, j);
        LONG_VH_BE.set(bArr, 8, j2);
        return bArr;
    }

    public static byte[] uuidToBytes(UUID uuid) {
        return uuidToBytes(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    private Bits() {
    }
}
