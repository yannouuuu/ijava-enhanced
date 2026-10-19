package defpackage;

/* loaded from: ijava.jar:Semaine3.class */
class Semaine3 extends Program {
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;
    final String CODES = "NBrvb";
    final String COLORS = "000000000255255255255000000000255000000000255";
    final String IMAGE_PAS_TERRIBLE = "NNNNNNNNNbbbbbbNNbbbbbbNNbbrrbbNNbrrrrbNNvrrrrvNNvvvvvvNNNNNNNNN";

    Semaine3() {
    }

    int charToInt(char c) {
        return c - '0';
    }

    String toString(int i) {
        String str = "00" + i;
        return substring(str, length(str) - 3, length(str));
    }

    String color(int i, int i2, int i3) {
        return toString(i) + toString(i2) + toString(i3);
    }

    int primaryColorToInt(String str) {
        return (charToInt(charAt(str, 0)) * 100) + (charToInt(charAt(str, 1)) * 10) + charToInt(charAt(str, 2));
    }

    int primaryColorIndex(int i) {
        return i * 3;
    }

    int get(String str, int i) {
        int primaryColorIndex = primaryColorIndex(i);
        return primaryColorToInt(substring(str, primaryColorIndex, primaryColorIndex + 3));
    }

    String set(String str, int i, int i2) {
        int primaryColorIndex = primaryColorIndex(i);
        return (("" + substring(str, 0, primaryColorIndex)) + toString(i2)) + substring(str, primaryColorIndex + 3, length(str));
    }

    int size(String str) {
        return sqrt(length(str) / 9);
    }

    String get(String str, int i, int i2) {
        int size = (i * size(str) * 9) + (i2 * 9);
        return substring(str, size, size + 9);
    }

    String generate(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        String str = "";
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        int i11 = 0;
        while (true) {
            int i12 = i11;
            if (i12 < i * i) {
                if (i12 % i == 0) {
                    i8 = (i8 + i5) % 255;
                    i9 = (i9 + i6) % 255;
                    i10 = (i10 + i7) % 255;
                }
                str = str + color(i8, i9, i10);
                i11 = i12 + 1;
            } else {
                return str;
            }
        }
    }

    void show(String str) {
        size(str);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < size(str)) {
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    if (i4 < size(str)) {
                        String str2 = get(str, i2, i4);
                        print(rgb(get(str2, 0), get(str2, 1), get(str2, 2), false) + " ");
                        i3 = i4 + 1;
                    }
                }
                println("\u001b[0m");
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getColorIndex(String str, char c) {
        int i;
        int i2 = 0;
        while (true) {
            i = i2;
            if (i >= length(str) || charAt(str, i) == c) {
                break;
            }
            i2 = i + 1;
        }
        if (i == length(str)) {
            i = -1;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getColorForCode(String str, String str2, char c) {
        String str3 = "";
        int colorIndex = getColorIndex(str, c);
        if (colorIndex >= 0) {
            int i = colorIndex * 9;
            str3 = substring(str2, i, i + 9);
        }
        return str3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String convert(String str, String str2, String str3) {
        String str4 = "";
        int i = 0;
        String str5 = "on s'en moque :)";
        while (i < length(str3) && length(str5) >= 0) {
            str5 = getColorForCode(str, str2, charAt(str3, i));
            str4 = str4 + str5;
            i++;
        }
        if (length(str5) == 0) {
            str4 = (i - 1);
        }
        return str4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        show("255255255255255255255255255255255255255255255070180040000140030255255255255255255255255255255255255255255255255255255255255255070180040070180040255255255255255255255255255255255255255255255255255255238110000245235155238110000238110000245235155238110000255255255255255255255255255238110000238110000245235155238110000238110000245235155238110000238110000255255255238110000245235155000000000000000000238110000238110000000000000000000000245235155238110000238110000245235155000000000000000000238110000238110000000000000000000000245235155238110000238110000238110000000000000238110000000000000000000000238110000000000000238110000238110000238110000000000000000000000000000000238110000238110000000000000000000000000000000238110000255255255238110000245235155000000000000000000000000000000000000245235155238110000255255255255255255255255255238110000245235155238110000238110000245235155238110000255255255255255255");
        println();
        show(convert("NBrvb", "000000000255255255255000000000255000000000255", "BBBBBvvBBBBBBBvvBBBBBBojoojoBBBoojoojooBojNNooNNjoojNNooNNjoooNoNNoNoooNNNooNNNoBojNNNNjoBBBojoojoBB"));
        println();
    }
}
