package org.h2.security;

import org.h2.util.Bits;

/* loaded from: ijava.jar:org/h2/security/Fog.class */
public class Fog implements BlockCipher {
    private int key;

    @Override // org.h2.security.BlockCipher
    public void encrypt(byte[] bArr, int i, int i2) {
        for (int i3 = i; i3 < i + i2; i3 += 16) {
            encryptBlock(bArr, bArr, i3);
        }
    }

    @Override // org.h2.security.BlockCipher
    public void decrypt(byte[] bArr, int i, int i2) {
        for (int i3 = i; i3 < i + i2; i3 += 16) {
            decryptBlock(bArr, bArr, i3);
        }
    }

    private void encryptBlock(byte[] bArr, byte[] bArr2, int i) {
        int i2 = Bits.INT_VH_BE.get(bArr, i);
        int i3 = Bits.INT_VH_BE.get(bArr, i + 4);
        int i4 = Bits.INT_VH_BE.get(bArr, i + 8);
        int i5 = Bits.INT_VH_BE.get(bArr, i + 12);
        int i6 = this.key;
        int rotateLeft = Integer.rotateLeft(i2 ^ i6, i3);
        int rotateLeft2 = Integer.rotateLeft(i4 ^ i6, i3);
        int rotateLeft3 = Integer.rotateLeft(i3 ^ i6, rotateLeft);
        int rotateLeft4 = Integer.rotateLeft(i5 ^ i6, rotateLeft);
        Bits.INT_VH_BE.set(bArr2, i, rotateLeft);
        Bits.INT_VH_BE.set(bArr2, i + 4, rotateLeft3);
        Bits.INT_VH_BE.set(bArr2, i + 8, rotateLeft2);
        Bits.INT_VH_BE.set(bArr2, i + 12, rotateLeft4);
    }

    private void decryptBlock(byte[] bArr, byte[] bArr2, int i) {
        int i2 = Bits.INT_VH_BE.get(bArr, i);
        int i3 = Bits.INT_VH_BE.get(bArr, i + 4);
        int i4 = Bits.INT_VH_BE.get(bArr, i + 8);
        int i5 = Bits.INT_VH_BE.get(bArr, i + 12);
        int i6 = this.key;
        int rotateRight = Integer.rotateRight(i3, i2) ^ i6;
        int rotateRight2 = Integer.rotateRight(i5, i2) ^ i6;
        int rotateRight3 = Integer.rotateRight(i2, rotateRight) ^ i6;
        int rotateRight4 = Integer.rotateRight(i4, rotateRight) ^ i6;
        Bits.INT_VH_BE.set(bArr2, i, rotateRight3);
        Bits.INT_VH_BE.set(bArr2, i + 4, rotateRight);
        Bits.INT_VH_BE.set(bArr2, i + 8, rotateRight4);
        Bits.INT_VH_BE.set(bArr2, i + 12, rotateRight2);
    }

    @Override // org.h2.security.BlockCipher
    public int getKeyLength() {
        return 16;
    }

    @Override // org.h2.security.BlockCipher
    public void setKey(byte[] bArr) {
        this.key = (int) Bits.LONG_VH_BE.get(bArr, 0);
    }
}
