package org.h2.security;

import org.h2.util.Bits;

/* loaded from: ijava.jar:org/h2/security/AES.class */
public class AES implements BlockCipher {
    private static final int[] RCON = new int[10];
    private static final int[] FS = new int[256];
    private static final int[] FT0 = new int[256];
    private static final int[] FT1 = new int[256];
    private static final int[] FT2 = new int[256];
    private static final int[] FT3 = new int[256];
    private static final int[] RS = new int[256];
    private static final int[] RT0 = new int[256];
    private static final int[] RT1 = new int[256];
    private static final int[] RT2 = new int[256];
    private static final int[] RT3 = new int[256];
    private final int[] encKey = new int[44];
    private final int[] decKey = new int[44];

    static {
        int[] iArr = new int[256];
        int[] iArr2 = new int[256];
        int i = 0;
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i >= 256) {
                break;
            }
            iArr[i] = i3;
            iArr2[i3] = i;
            i++;
            i2 = i3 ^ xtime(i3);
        }
        int i4 = 0;
        int i5 = 1;
        while (true) {
            int i6 = i5;
            if (i4 >= 10) {
                break;
            }
            RCON[i4] = i6 << 24;
            i4++;
            i5 = xtime(i6);
        }
        FS[0] = 99;
        RS[99] = 0;
        for (int i7 = 1; i7 < 256; i7++) {
            int i8 = iArr[255 - iArr2[i7]];
            int i9 = ((i8 << 1) | (i8 >> 7)) & 255;
            int i10 = i8 ^ i9;
            int i11 = ((i9 << 1) | (i9 >> 7)) & 255;
            int i12 = i10 ^ i11;
            int i13 = ((i11 << 1) | (i11 >> 7)) & 255;
            int i14 = (i12 ^ i13) ^ ((((i13 << 1) | (i13 >> 7)) & 255) ^ 99);
            FS[i7] = i14 & 255;
            RS[i14] = i7 & 255;
        }
        for (int i15 = 0; i15 < 256; i15++) {
            int i16 = FS[i15];
            int xtime = xtime(i16);
            FT0[i15] = (((i16 ^ xtime) ^ (i16 << 8)) ^ (i16 << 16)) ^ (xtime << 24);
            FT1[i15] = rot8(FT0[i15]);
            FT2[i15] = rot8(FT1[i15]);
            FT3[i15] = rot8(FT2[i15]);
            int i17 = RS[i15];
            RT0[i15] = ((mul(iArr, iArr2, 11, i17) ^ (mul(iArr, iArr2, 13, i17) << 8)) ^ (mul(iArr, iArr2, 9, i17) << 16)) ^ (mul(iArr, iArr2, 14, i17) << 24);
            RT1[i15] = rot8(RT0[i15]);
            RT2[i15] = rot8(RT1[i15]);
            RT3[i15] = rot8(RT2[i15]);
        }
    }

    private static int rot8(int i) {
        return (i >>> 8) | (i << 24);
    }

    private static int xtime(int i) {
        return ((i << 1) ^ ((i & 128) != 0 ? 27 : 0)) & 255;
    }

    private static int mul(int[] iArr, int[] iArr2, int i, int i2) {
        if (i == 0 || i2 == 0) {
            return 0;
        }
        return iArr[(iArr2[i] + iArr2[i2]) % 255];
    }

    private static int getDec(int i) {
        return ((RT0[FS[(i >> 24) & 255]] ^ RT1[FS[(i >> 16) & 255]]) ^ RT2[FS[(i >> 8) & 255]]) ^ RT3[FS[i & 255]];
    }

    @Override // org.h2.security.BlockCipher
    public void setKey(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            int i3 = i;
            int i4 = i + 1;
            int i5 = i4 + 1;
            int i6 = ((bArr[i3] & 255) << 24) | ((bArr[i4] & 255) << 16);
            int i7 = i5 + 1;
            int i8 = i6 | ((bArr[i5] & 255) << 8);
            i = i7 + 1;
            int i9 = i8 | (bArr[i7] & 255);
            this.decKey[i2] = i9;
            this.encKey[i2] = i9;
        }
        int i10 = 0;
        int i11 = 0;
        while (i11 < 10) {
            this.encKey[i10 + 4] = ((((this.encKey[i10] ^ RCON[i11]) ^ (FS[(this.encKey[i10 + 3] >> 16) & 255] << 24)) ^ (FS[(this.encKey[i10 + 3] >> 8) & 255] << 16)) ^ (FS[this.encKey[i10 + 3] & 255] << 8)) ^ FS[(this.encKey[i10 + 3] >> 24) & 255];
            this.encKey[i10 + 5] = this.encKey[i10 + 1] ^ this.encKey[i10 + 4];
            this.encKey[i10 + 6] = this.encKey[i10 + 2] ^ this.encKey[i10 + 5];
            this.encKey[i10 + 7] = this.encKey[i10 + 3] ^ this.encKey[i10 + 6];
            i11++;
            i10 += 4;
        }
        int i12 = 0 + 1;
        int i13 = i10;
        int i14 = i10 + 1;
        this.decKey[0] = this.encKey[i13];
        int i15 = i12 + 1;
        int i16 = i14 + 1;
        this.decKey[i12] = this.encKey[i14];
        int i17 = i15 + 1;
        int i18 = i16 + 1;
        this.decKey[i15] = this.encKey[i16];
        int i19 = i17 + 1;
        int i20 = i18 + 1;
        this.decKey[i17] = this.encKey[i18];
        for (int i21 = 1; i21 < 10; i21++) {
            int i22 = i20 - 8;
            int i23 = i19;
            int i24 = i19 + 1;
            int i25 = i22 + 1;
            this.decKey[i23] = getDec(this.encKey[i22]);
            int i26 = i24 + 1;
            int i27 = i25 + 1;
            this.decKey[i24] = getDec(this.encKey[i25]);
            int i28 = i26 + 1;
            int i29 = i27 + 1;
            this.decKey[i26] = getDec(this.encKey[i27]);
            i19 = i28 + 1;
            i20 = i29 + 1;
            this.decKey[i28] = getDec(this.encKey[i29]);
        }
        int i30 = i20 - 8;
        int i31 = i19;
        int i32 = i19 + 1;
        int i33 = i30 + 1;
        this.decKey[i31] = this.encKey[i30];
        int i34 = i32 + 1;
        int i35 = i33 + 1;
        this.decKey[i32] = this.encKey[i33];
        this.decKey[i34] = this.encKey[i35];
        this.decKey[i34 + 1] = this.encKey[i35 + 1];
    }

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
        int[] iArr = this.encKey;
        int i2 = Bits.INT_VH_BE.get(bArr, i) ^ iArr[0];
        int i3 = Bits.INT_VH_BE.get(bArr, i + 4) ^ iArr[1];
        int i4 = Bits.INT_VH_BE.get(bArr, i + 8) ^ iArr[2];
        int i5 = Bits.INT_VH_BE.get(bArr, i + 12) ^ iArr[3];
        int i6 = (((FT0[(i2 >> 24) & 255] ^ FT1[(i3 >> 16) & 255]) ^ FT2[(i4 >> 8) & 255]) ^ FT3[i5 & 255]) ^ iArr[4];
        int i7 = (((FT0[(i3 >> 24) & 255] ^ FT1[(i4 >> 16) & 255]) ^ FT2[(i5 >> 8) & 255]) ^ FT3[i2 & 255]) ^ iArr[5];
        int i8 = (((FT0[(i4 >> 24) & 255] ^ FT1[(i5 >> 16) & 255]) ^ FT2[(i2 >> 8) & 255]) ^ FT3[i3 & 255]) ^ iArr[6];
        int i9 = (((FT0[(i5 >> 24) & 255] ^ FT1[(i2 >> 16) & 255]) ^ FT2[(i3 >> 8) & 255]) ^ FT3[i4 & 255]) ^ iArr[7];
        int i10 = (((FT0[(i6 >> 24) & 255] ^ FT1[(i7 >> 16) & 255]) ^ FT2[(i8 >> 8) & 255]) ^ FT3[i9 & 255]) ^ iArr[8];
        int i11 = (((FT0[(i7 >> 24) & 255] ^ FT1[(i8 >> 16) & 255]) ^ FT2[(i9 >> 8) & 255]) ^ FT3[i6 & 255]) ^ iArr[9];
        int i12 = (((FT0[(i8 >> 24) & 255] ^ FT1[(i9 >> 16) & 255]) ^ FT2[(i6 >> 8) & 255]) ^ FT3[i7 & 255]) ^ iArr[10];
        int i13 = (((FT0[(i9 >> 24) & 255] ^ FT1[(i6 >> 16) & 255]) ^ FT2[(i7 >> 8) & 255]) ^ FT3[i8 & 255]) ^ iArr[11];
        int i14 = (((FT0[(i10 >> 24) & 255] ^ FT1[(i11 >> 16) & 255]) ^ FT2[(i12 >> 8) & 255]) ^ FT3[i13 & 255]) ^ iArr[12];
        int i15 = (((FT0[(i11 >> 24) & 255] ^ FT1[(i12 >> 16) & 255]) ^ FT2[(i13 >> 8) & 255]) ^ FT3[i10 & 255]) ^ iArr[13];
        int i16 = (((FT0[(i12 >> 24) & 255] ^ FT1[(i13 >> 16) & 255]) ^ FT2[(i10 >> 8) & 255]) ^ FT3[i11 & 255]) ^ iArr[14];
        int i17 = (((FT0[(i13 >> 24) & 255] ^ FT1[(i10 >> 16) & 255]) ^ FT2[(i11 >> 8) & 255]) ^ FT3[i12 & 255]) ^ iArr[15];
        int i18 = (((FT0[(i14 >> 24) & 255] ^ FT1[(i15 >> 16) & 255]) ^ FT2[(i16 >> 8) & 255]) ^ FT3[i17 & 255]) ^ iArr[16];
        int i19 = (((FT0[(i15 >> 24) & 255] ^ FT1[(i16 >> 16) & 255]) ^ FT2[(i17 >> 8) & 255]) ^ FT3[i14 & 255]) ^ iArr[17];
        int i20 = (((FT0[(i16 >> 24) & 255] ^ FT1[(i17 >> 16) & 255]) ^ FT2[(i14 >> 8) & 255]) ^ FT3[i15 & 255]) ^ iArr[18];
        int i21 = (((FT0[(i17 >> 24) & 255] ^ FT1[(i14 >> 16) & 255]) ^ FT2[(i15 >> 8) & 255]) ^ FT3[i16 & 255]) ^ iArr[19];
        int i22 = (((FT0[(i18 >> 24) & 255] ^ FT1[(i19 >> 16) & 255]) ^ FT2[(i20 >> 8) & 255]) ^ FT3[i21 & 255]) ^ iArr[20];
        int i23 = (((FT0[(i19 >> 24) & 255] ^ FT1[(i20 >> 16) & 255]) ^ FT2[(i21 >> 8) & 255]) ^ FT3[i18 & 255]) ^ iArr[21];
        int i24 = (((FT0[(i20 >> 24) & 255] ^ FT1[(i21 >> 16) & 255]) ^ FT2[(i18 >> 8) & 255]) ^ FT3[i19 & 255]) ^ iArr[22];
        int i25 = (((FT0[(i21 >> 24) & 255] ^ FT1[(i18 >> 16) & 255]) ^ FT2[(i19 >> 8) & 255]) ^ FT3[i20 & 255]) ^ iArr[23];
        int i26 = (((FT0[(i22 >> 24) & 255] ^ FT1[(i23 >> 16) & 255]) ^ FT2[(i24 >> 8) & 255]) ^ FT3[i25 & 255]) ^ iArr[24];
        int i27 = (((FT0[(i23 >> 24) & 255] ^ FT1[(i24 >> 16) & 255]) ^ FT2[(i25 >> 8) & 255]) ^ FT3[i22 & 255]) ^ iArr[25];
        int i28 = (((FT0[(i24 >> 24) & 255] ^ FT1[(i25 >> 16) & 255]) ^ FT2[(i22 >> 8) & 255]) ^ FT3[i23 & 255]) ^ iArr[26];
        int i29 = (((FT0[(i25 >> 24) & 255] ^ FT1[(i22 >> 16) & 255]) ^ FT2[(i23 >> 8) & 255]) ^ FT3[i24 & 255]) ^ iArr[27];
        int i30 = (((FT0[(i26 >> 24) & 255] ^ FT1[(i27 >> 16) & 255]) ^ FT2[(i28 >> 8) & 255]) ^ FT3[i29 & 255]) ^ iArr[28];
        int i31 = (((FT0[(i27 >> 24) & 255] ^ FT1[(i28 >> 16) & 255]) ^ FT2[(i29 >> 8) & 255]) ^ FT3[i26 & 255]) ^ iArr[29];
        int i32 = (((FT0[(i28 >> 24) & 255] ^ FT1[(i29 >> 16) & 255]) ^ FT2[(i26 >> 8) & 255]) ^ FT3[i27 & 255]) ^ iArr[30];
        int i33 = (((FT0[(i29 >> 24) & 255] ^ FT1[(i26 >> 16) & 255]) ^ FT2[(i27 >> 8) & 255]) ^ FT3[i28 & 255]) ^ iArr[31];
        int i34 = (((FT0[(i30 >> 24) & 255] ^ FT1[(i31 >> 16) & 255]) ^ FT2[(i32 >> 8) & 255]) ^ FT3[i33 & 255]) ^ iArr[32];
        int i35 = (((FT0[(i31 >> 24) & 255] ^ FT1[(i32 >> 16) & 255]) ^ FT2[(i33 >> 8) & 255]) ^ FT3[i30 & 255]) ^ iArr[33];
        int i36 = (((FT0[(i32 >> 24) & 255] ^ FT1[(i33 >> 16) & 255]) ^ FT2[(i30 >> 8) & 255]) ^ FT3[i31 & 255]) ^ iArr[34];
        int i37 = (((FT0[(i33 >> 24) & 255] ^ FT1[(i30 >> 16) & 255]) ^ FT2[(i31 >> 8) & 255]) ^ FT3[i32 & 255]) ^ iArr[35];
        int i38 = (((FT0[(i34 >> 24) & 255] ^ FT1[(i35 >> 16) & 255]) ^ FT2[(i36 >> 8) & 255]) ^ FT3[i37 & 255]) ^ iArr[36];
        int i39 = (((FT0[(i35 >> 24) & 255] ^ FT1[(i36 >> 16) & 255]) ^ FT2[(i37 >> 8) & 255]) ^ FT3[i34 & 255]) ^ iArr[37];
        int i40 = (((FT0[(i36 >> 24) & 255] ^ FT1[(i37 >> 16) & 255]) ^ FT2[(i34 >> 8) & 255]) ^ FT3[i35 & 255]) ^ iArr[38];
        int i41 = (((FT0[(i37 >> 24) & 255] ^ FT1[(i34 >> 16) & 255]) ^ FT2[(i35 >> 8) & 255]) ^ FT3[i36 & 255]) ^ iArr[39];
        int i42 = ((((FS[(i38 >> 24) & 255] << 24) | (FS[(i39 >> 16) & 255] << 16)) | (FS[(i40 >> 8) & 255] << 8)) | FS[i41 & 255]) ^ iArr[40];
        int i43 = ((((FS[(i39 >> 24) & 255] << 24) | (FS[(i40 >> 16) & 255] << 16)) | (FS[(i41 >> 8) & 255] << 8)) | FS[i38 & 255]) ^ iArr[41];
        int i44 = ((((FS[(i40 >> 24) & 255] << 24) | (FS[(i41 >> 16) & 255] << 16)) | (FS[(i38 >> 8) & 255] << 8)) | FS[i39 & 255]) ^ iArr[42];
        int i45 = ((((FS[(i41 >> 24) & 255] << 24) | (FS[(i38 >> 16) & 255] << 16)) | (FS[(i39 >> 8) & 255] << 8)) | FS[i40 & 255]) ^ iArr[43];
        Bits.INT_VH_BE.set(bArr2, i, i42);
        Bits.INT_VH_BE.set(bArr2, i + 4, i43);
        Bits.INT_VH_BE.set(bArr2, i + 8, i44);
        Bits.INT_VH_BE.set(bArr2, i + 12, i45);
    }

    private void decryptBlock(byte[] bArr, byte[] bArr2, int i) {
        int[] iArr = this.decKey;
        int i2 = Bits.INT_VH_BE.get(bArr, i) ^ iArr[0];
        int i3 = Bits.INT_VH_BE.get(bArr, i + 4) ^ iArr[1];
        int i4 = Bits.INT_VH_BE.get(bArr, i + 8) ^ iArr[2];
        int i5 = Bits.INT_VH_BE.get(bArr, i + 12) ^ iArr[3];
        int i6 = (((RT0[(i2 >> 24) & 255] ^ RT1[(i5 >> 16) & 255]) ^ RT2[(i4 >> 8) & 255]) ^ RT3[i3 & 255]) ^ iArr[4];
        int i7 = (((RT0[(i3 >> 24) & 255] ^ RT1[(i2 >> 16) & 255]) ^ RT2[(i5 >> 8) & 255]) ^ RT3[i4 & 255]) ^ iArr[5];
        int i8 = (((RT0[(i4 >> 24) & 255] ^ RT1[(i3 >> 16) & 255]) ^ RT2[(i2 >> 8) & 255]) ^ RT3[i5 & 255]) ^ iArr[6];
        int i9 = (((RT0[(i5 >> 24) & 255] ^ RT1[(i4 >> 16) & 255]) ^ RT2[(i3 >> 8) & 255]) ^ RT3[i2 & 255]) ^ iArr[7];
        int i10 = (((RT0[(i6 >> 24) & 255] ^ RT1[(i9 >> 16) & 255]) ^ RT2[(i8 >> 8) & 255]) ^ RT3[i7 & 255]) ^ iArr[8];
        int i11 = (((RT0[(i7 >> 24) & 255] ^ RT1[(i6 >> 16) & 255]) ^ RT2[(i9 >> 8) & 255]) ^ RT3[i8 & 255]) ^ iArr[9];
        int i12 = (((RT0[(i8 >> 24) & 255] ^ RT1[(i7 >> 16) & 255]) ^ RT2[(i6 >> 8) & 255]) ^ RT3[i9 & 255]) ^ iArr[10];
        int i13 = (((RT0[(i9 >> 24) & 255] ^ RT1[(i8 >> 16) & 255]) ^ RT2[(i7 >> 8) & 255]) ^ RT3[i6 & 255]) ^ iArr[11];
        int i14 = (((RT0[(i10 >> 24) & 255] ^ RT1[(i13 >> 16) & 255]) ^ RT2[(i12 >> 8) & 255]) ^ RT3[i11 & 255]) ^ iArr[12];
        int i15 = (((RT0[(i11 >> 24) & 255] ^ RT1[(i10 >> 16) & 255]) ^ RT2[(i13 >> 8) & 255]) ^ RT3[i12 & 255]) ^ iArr[13];
        int i16 = (((RT0[(i12 >> 24) & 255] ^ RT1[(i11 >> 16) & 255]) ^ RT2[(i10 >> 8) & 255]) ^ RT3[i13 & 255]) ^ iArr[14];
        int i17 = (((RT0[(i13 >> 24) & 255] ^ RT1[(i12 >> 16) & 255]) ^ RT2[(i11 >> 8) & 255]) ^ RT3[i10 & 255]) ^ iArr[15];
        int i18 = (((RT0[(i14 >> 24) & 255] ^ RT1[(i17 >> 16) & 255]) ^ RT2[(i16 >> 8) & 255]) ^ RT3[i15 & 255]) ^ iArr[16];
        int i19 = (((RT0[(i15 >> 24) & 255] ^ RT1[(i14 >> 16) & 255]) ^ RT2[(i17 >> 8) & 255]) ^ RT3[i16 & 255]) ^ iArr[17];
        int i20 = (((RT0[(i16 >> 24) & 255] ^ RT1[(i15 >> 16) & 255]) ^ RT2[(i14 >> 8) & 255]) ^ RT3[i17 & 255]) ^ iArr[18];
        int i21 = (((RT0[(i17 >> 24) & 255] ^ RT1[(i16 >> 16) & 255]) ^ RT2[(i15 >> 8) & 255]) ^ RT3[i14 & 255]) ^ iArr[19];
        int i22 = (((RT0[(i18 >> 24) & 255] ^ RT1[(i21 >> 16) & 255]) ^ RT2[(i20 >> 8) & 255]) ^ RT3[i19 & 255]) ^ iArr[20];
        int i23 = (((RT0[(i19 >> 24) & 255] ^ RT1[(i18 >> 16) & 255]) ^ RT2[(i21 >> 8) & 255]) ^ RT3[i20 & 255]) ^ iArr[21];
        int i24 = (((RT0[(i20 >> 24) & 255] ^ RT1[(i19 >> 16) & 255]) ^ RT2[(i18 >> 8) & 255]) ^ RT3[i21 & 255]) ^ iArr[22];
        int i25 = (((RT0[(i21 >> 24) & 255] ^ RT1[(i20 >> 16) & 255]) ^ RT2[(i19 >> 8) & 255]) ^ RT3[i18 & 255]) ^ iArr[23];
        int i26 = (((RT0[(i22 >> 24) & 255] ^ RT1[(i25 >> 16) & 255]) ^ RT2[(i24 >> 8) & 255]) ^ RT3[i23 & 255]) ^ iArr[24];
        int i27 = (((RT0[(i23 >> 24) & 255] ^ RT1[(i22 >> 16) & 255]) ^ RT2[(i25 >> 8) & 255]) ^ RT3[i24 & 255]) ^ iArr[25];
        int i28 = (((RT0[(i24 >> 24) & 255] ^ RT1[(i23 >> 16) & 255]) ^ RT2[(i22 >> 8) & 255]) ^ RT3[i25 & 255]) ^ iArr[26];
        int i29 = (((RT0[(i25 >> 24) & 255] ^ RT1[(i24 >> 16) & 255]) ^ RT2[(i23 >> 8) & 255]) ^ RT3[i22 & 255]) ^ iArr[27];
        int i30 = (((RT0[(i26 >> 24) & 255] ^ RT1[(i29 >> 16) & 255]) ^ RT2[(i28 >> 8) & 255]) ^ RT3[i27 & 255]) ^ iArr[28];
        int i31 = (((RT0[(i27 >> 24) & 255] ^ RT1[(i26 >> 16) & 255]) ^ RT2[(i29 >> 8) & 255]) ^ RT3[i28 & 255]) ^ iArr[29];
        int i32 = (((RT0[(i28 >> 24) & 255] ^ RT1[(i27 >> 16) & 255]) ^ RT2[(i26 >> 8) & 255]) ^ RT3[i29 & 255]) ^ iArr[30];
        int i33 = (((RT0[(i29 >> 24) & 255] ^ RT1[(i28 >> 16) & 255]) ^ RT2[(i27 >> 8) & 255]) ^ RT3[i26 & 255]) ^ iArr[31];
        int i34 = (((RT0[(i30 >> 24) & 255] ^ RT1[(i33 >> 16) & 255]) ^ RT2[(i32 >> 8) & 255]) ^ RT3[i31 & 255]) ^ iArr[32];
        int i35 = (((RT0[(i31 >> 24) & 255] ^ RT1[(i30 >> 16) & 255]) ^ RT2[(i33 >> 8) & 255]) ^ RT3[i32 & 255]) ^ iArr[33];
        int i36 = (((RT0[(i32 >> 24) & 255] ^ RT1[(i31 >> 16) & 255]) ^ RT2[(i30 >> 8) & 255]) ^ RT3[i33 & 255]) ^ iArr[34];
        int i37 = (((RT0[(i33 >> 24) & 255] ^ RT1[(i32 >> 16) & 255]) ^ RT2[(i31 >> 8) & 255]) ^ RT3[i30 & 255]) ^ iArr[35];
        int i38 = (((RT0[(i34 >> 24) & 255] ^ RT1[(i37 >> 16) & 255]) ^ RT2[(i36 >> 8) & 255]) ^ RT3[i35 & 255]) ^ iArr[36];
        int i39 = (((RT0[(i35 >> 24) & 255] ^ RT1[(i34 >> 16) & 255]) ^ RT2[(i37 >> 8) & 255]) ^ RT3[i36 & 255]) ^ iArr[37];
        int i40 = (((RT0[(i36 >> 24) & 255] ^ RT1[(i35 >> 16) & 255]) ^ RT2[(i34 >> 8) & 255]) ^ RT3[i37 & 255]) ^ iArr[38];
        int i41 = (((RT0[(i37 >> 24) & 255] ^ RT1[(i36 >> 16) & 255]) ^ RT2[(i35 >> 8) & 255]) ^ RT3[i34 & 255]) ^ iArr[39];
        int i42 = ((((RS[(i38 >> 24) & 255] << 24) | (RS[(i41 >> 16) & 255] << 16)) | (RS[(i40 >> 8) & 255] << 8)) | RS[i39 & 255]) ^ iArr[40];
        int i43 = ((((RS[(i39 >> 24) & 255] << 24) | (RS[(i38 >> 16) & 255] << 16)) | (RS[(i41 >> 8) & 255] << 8)) | RS[i40 & 255]) ^ iArr[41];
        int i44 = ((((RS[(i40 >> 24) & 255] << 24) | (RS[(i39 >> 16) & 255] << 16)) | (RS[(i38 >> 8) & 255] << 8)) | RS[i41 & 255]) ^ iArr[42];
        int i45 = ((((RS[(i41 >> 24) & 255] << 24) | (RS[(i40 >> 16) & 255] << 16)) | (RS[(i39 >> 8) & 255] << 8)) | RS[i38 & 255]) ^ iArr[43];
        Bits.INT_VH_BE.set(bArr2, i, i42);
        Bits.INT_VH_BE.set(bArr2, i + 4, i43);
        Bits.INT_VH_BE.set(bArr2, i + 8, i44);
        Bits.INT_VH_BE.set(bArr2, i + 12, i45);
    }

    @Override // org.h2.security.BlockCipher
    public int getKeyLength() {
        return 16;
    }
}
