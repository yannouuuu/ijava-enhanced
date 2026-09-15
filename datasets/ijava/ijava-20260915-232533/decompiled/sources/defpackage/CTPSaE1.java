package defpackage;

/* loaded from: ijava.jar:CTPSaE1.class */
class CTPSaE1 extends Program {
    final String HEX_RED = "FF0000";
    final String HEX_GREEN = "00FF00";
    final String HEX_BLUE = "0000FF";
    final String HEX_WHITE = "FFFFFF";
    final String HEX_BLACK = "000000";
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;

    CTPSaE1() {
    }

    char numberToHex(int i) {
        char c = (char) (48 + i);
        if (i >= 10 && i <= 15) {
            c = (char) (65 + (i - 10));
        }
        return c;
    }

    void test_toHexChar() {
        assertEquals(48, (int) numberToHex(0));
        assertEquals(53, (int) numberToHex(5));
        assertEquals(57, (int) numberToHex(9));
        assertEquals(65, (int) numberToHex(10));
        assertEquals(67, (int) numberToHex(12));
        assertEquals(70, (int) numberToHex(15));
    }

    String intToHex(int i) {
        return numberToHex(i / 16) + numberToHex(i % 16);
    }

    void test_toHexString() {
        assertEquals("00", intToHex(0));
        assertEquals("05", intToHex(5));
        assertEquals("0A", intToHex(10));
        assertEquals("1F", intToHex(31));
        assertEquals("7F", intToHex(127));
        assertEquals("FF", intToHex(255));
    }

    String colorToHex(int i, int i2, int i3) {
        return intToHex(i) + intToHex(i2) + intToHex(i3);
    }

    void test_colorToHex() {
        assertEquals("FF0000", colorToHex(255, 0, 0));
        assertEquals("00FF00", colorToHex(0, 255, 0));
        assertEquals("0000FF", colorToHex(0, 0, 255));
        assertEquals("FFFFFF", colorToHex(255, 255, 255));
        assertEquals("000000", colorToHex(0, 0, 0));
        assertEquals("7F7F7F", colorToHex(127, 127, 127));
        assertEquals("123ABC", colorToHex(18, 58, 188));
    }

    int size(String[] strArr) {
        return sqrt(length(strArr));
    }

    void test_size() {
        assertEquals(2, size(new String[]{"FF0000", "00FF00", "FF0000", "00FF00"}));
        assertEquals(3, size(new String[]{"000000", "0000FF", "000000", "0000FF", "000000", "0000FF", "000000", "0000FF", "000000"}));
        assertEquals(4, size(new String[]{"FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"}));
    }

    String get(String[] strArr, int i, int i2) {
        return strArr[(i * size(strArr)) + i2];
    }

    void test_get() {
        String[] strArr = {"FF0000", "00FF00", "00FF00", "FF0000"};
        assertEquals("FF0000", get(strArr, 0, 0));
        assertEquals("00FF00", get(strArr, 0, 1));
        assertEquals("00FF00", get(strArr, 1, 0));
        assertEquals("FF0000", get(strArr, 1, 1));
        String[] strArr2 = {"000000", "0000FF", "000000", "0000FF", "00FF00", "0000FF", "000000", "0000FF", "000000"};
        assertEquals("000000", get(strArr2, 0, 0));
        assertEquals("0000FF", get(strArr2, 0, 1));
        assertEquals("000000", get(strArr2, 0, 2));
        assertEquals("0000FF", get(strArr2, 1, 0));
        assertEquals("00FF00", get(strArr2, 1, 1));
        assertEquals("0000FF", get(strArr2, 1, 2));
        assertEquals("000000", get(strArr2, 2, 0));
        assertEquals("0000FF", get(strArr2, 2, 1));
        assertEquals("000000", get(strArr2, 2, 2));
    }

    String[] generate(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        String[] strArr = new String[i * i];
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
                strArr[i12] = colorToHex(i8, i9, i10);
                i11 = i12 + 1;
            } else {
                return strArr;
            }
        }
    }

    void test_generate_5_200_255_155_moins20_moins30_moins15() {
        assertArrayEquals(new String[]{"B4E18C", "B4E18C", "B4E18C", "B4E18C", "B4E18C", "A0C37D", "A0C37D", "A0C37D", "A0C37D", "A0C37D", "8CA56E", "8CA56E", "8CA56E", "8CA56E", "8CA56E", "78875F", "78875F", "78875F", "78875F", "78875F", "646950", "646950", "646950", "646950", "646950"}, generate(5, 200, 255, 155, -20, -30, -15));
    }

    String[] miroir(String[] strArr) {
        int size = size(strArr);
        String[] strArr2 = new String[length(strArr)];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < size) {
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    if (i4 < size) {
                        strArr2[(i2 * size) + ((size - 1) - i4)] = get(strArr, i2, i4);
                        i3 = i4 + 1;
                    }
                }
                i = i2 + 1;
            } else {
                return strArr2;
            }
        }
    }

    void test_miroir() {
        assertArrayEquals(new String[]{"00FF00", "FF0000", "FF0000", "00FF00"}, miroir(new String[]{"FF0000", "00FF00", "00FF00", "FF0000"}));
        assertArrayEquals(new String[]{"00FF00", "0000FF", "000000", "0000FF", "0000FF", "000000", "000000", "000000", "000000"}, miroir(new String[]{"000000", "0000FF", "00FF00", "000000", "0000FF", "0000FF", "000000", "000000", "000000"}));
    }

    int get(String str, int i) {
        String substring;
        int i2;
        if (i == 0) {
            substring = substring(str, 0, 2);
        } else if (i == 1) {
            substring = substring(str, 2, 4);
        } else {
            substring = substring(str, 4, 6);
        }
        int i3 = 0;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < 2) {
                char charAt = charAt(substring, i5);
                if ('0' <= charAt && charAt <= '9') {
                    i2 = charAt - '0';
                } else {
                    i2 = 10 + (charAt - 'A');
                }
                i3 = (i3 * 16) + i2;
                i4 = i5 + 1;
            } else {
                return i3;
            }
        }
    }

    void show(String[] strArr) {
        size(strArr);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < size(strArr)) {
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    if (i4 < size(strArr)) {
                        String str = get(strArr, i2, i4);
                        print(rgb(get(str, 0), get(str, 1), get(str, 2), false) + " \u001b[0m");
                        i3 = i4 + 1;
                    }
                }
                println();
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    @inject("    // Afin d'alléger la lecture du code et des tests, on définit quelques constantes pour les couleurs de base représentée en hexadécimal\n    final String HEX_RED   = \"FF0000\";\n    final String HEX_GREEN = \"00FF00\";\n    final String HEX_BLUE  = \"0000FF\";\n    final String HEX_WHITE = \"FFFFFF\";\n    final String HEX_BLACK = \"000000\";\n\n    // Q1 (3) : char numberToHex(int digit)\n\n/*\n    void test_toHexChar() {\n        assertEquals('0', numberToHex(0));\n        assertEquals('5', numberToHex(5));\n        assertEquals('9', numberToHex(9));\n        assertEquals('A', numberToHex(10));\n        assertEquals('C', numberToHex(12));\n        assertEquals('F', numberToHex(15));\n    }\n*/\n\n    // Q2 (2): String toHex(int primaryColor)\n\n/*    void test_toHexString() {\n        assertEquals(\"00\", intToHex(0));\n        assertEquals(\"05\", intToHex(5));\n        assertEquals(\"0A\", intToHex(10));\n        assertEquals(\"1F\", intToHex(31));\n        assertEquals(\"7F\", intToHex(127));\n        assertEquals(\"FF\", intToHex(255));\n    }\n*\n\n    // Q3 (2) : String colorToHex(int red, int green, int blue)\n\n    final String HEX_RED   = \"FF0000\";\n    final String HEX_GREEN = \"00FF00\";\n    final String HEX_BLUE  = \"0000FF\";\n    final String HEX_WHITE = \"FFFFFF\";\n    final String HEX_BLACK = \"000000\";\n\n/*\n    void test_colorToHex() {\n        assertEquals(HEX_RED,   colorToHex(255, 0, 0));     // rouge pur\n        assertEquals(HEX_GREEN, colorToHex(0, 255, 0));     // vert pur\n        assertEquals(HEX_BLUE,  colorToHex(0, 0, 255));     // bleu pur\n        assertEquals(HEX_WHITE, colorToHex(255, 255, 255)); // blanc\n        assertEquals(HEX_BLACK, colorToHex(0, 0, 0));       // noir\n        assertEquals(\"7F7F7F\", colorToHex(127, 127, 127));  // gris moyen\n        assertEquals(\"123ABC\", colorToHex(18, 58, 188));    // couleur quelconque\n    }\n*/\n\n    // Q4 (2) : int size(String[] image)\n\n    int size(String[] image) {\n        return 0;\n    }\n\n/*\n    void test_size() {\n        // 2x2 rouge et vert :\n        // ROUGE, VERT,\n        // VERT,  ROUGE\n        // {\"FF0000\", \"00FF00\",\n        //  \"00FF00\", \"FF0000\"}\n        assertEquals(2, size(new String[]{\n            HEX_RED, HEX_GREEN,\n            HEX_RED, HEX_GREEN}));\n        // 3x3 noir et bleu :\n        // NOIR, BLEU, NOIR,\n        // BLEU, NOIR, BLEU,\n        // NOIR, BLEU, NOIR\n        // \"0000000000FF0000000000FF0000000000FF0000000000FF000000\"\n        assertEquals(3, size(new String[]{\n            HEX_BLACK, HEX_BLUE,  HEX_BLACK,\n            HEX_BLUE,  HEX_BLACK, HEX_BLUE,\n            HEX_BLACK, HEX_BLUE,  HEX_BLACK}));\n        // 4x4 blanc : 16 fois WHITE\n        // \"FFFFFFFFFFFFFFFFFFFFFFF....FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\"\n        assertEquals(4, size(new String[]{\n            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE,\n            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE,\n            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE,\n            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE}));\n    }\n*/\n\n    // Q5 (2): String get(String[] image, int line, int column)\n\n    String get(String[] image, int line, int column) {\n        return \"\";\n    }\n\n/*\n    void test_get() {\n        // image2x2 = \"FF0000\" \"00FF00\"\n        //            \"00FF00\" \"FF0000\";\n        String[] image2x2 = new String[]{  HEX_RED, HEX_GREEN,\n                                         HEX_GREEN,   HEX_RED};\n        assertEquals(HEX_RED,   get(image2x2, 0, 0)); // rouge\n        assertEquals(HEX_GREEN, get(image2x2, 0, 1)); // vert\n        assertEquals(HEX_GREEN, get(image2x2, 1, 0)); // vert\n        assertEquals(HEX_RED,   get(image2x2, 1, 1)); // rouge\n        // image3x3 = \"000000\" \"0000FF\" \"000000\"\n        //            \"0000FF\" \"000000\" \"0000FF\"\n        //            \"000000\" \"0000FF\" \"000000\"\n        String[] image3x3 = new String[]{HEX_BLACK, HEX_BLUE,  HEX_BLACK,\n                                         HEX_BLUE,  HEX_GREEN, HEX_BLUE,\n                                         HEX_BLACK, HEX_BLUE,  HEX_BLACK};\n        assertEquals(HEX_BLACK, get(image3x3, 0, 0)); // noir\n        assertEquals(HEX_BLUE,  get(image3x3, 0, 1)); // bleu\n        assertEquals(HEX_BLACK, get(image3x3, 0, 2)); // noir\n        assertEquals(HEX_BLUE,  get(image3x3, 1, 0)); // bleu\n        assertEquals(HEX_GREEN, get(image3x3, 1, 1)); // vert\n        assertEquals(HEX_BLUE,  get(image3x3, 1, 2)); // bleu\n        assertEquals(HEX_BLACK, get(image3x3, 2, 0)); // noir\n        assertEquals(HEX_BLUE,  get(image3x3, 2, 1)); // bleu\n        assertEquals(HEX_BLACK, get(image3x3, 2, 2)); // noir\n    }\n*/\n\n    //Q6 (4) : String[] generate(int size, int r, int g, int b, int stepR, int stepG, int stepB)\n\n/*\n    void test_generate_5_200_255_155_moins20_moins30_moins15() {\n        String[] generatedImage = new String[]{\n            \"B4E18C\", \"B4E18C\", \"B4E18C\", \"B4E18C\", \"B4E18C\",\n            \"A0C37D\", \"A0C37D\", \"A0C37D\", \"A0C37D\", \"A0C37D\",\n            \"8CA56E\", \"8CA56E\", \"8CA56E\", \"8CA56E\", \"8CA56E\",\n            \"78875F\", \"78875F\", \"78875F\", \"78875F\", \"78875F\",\n            \"646950\", \"646950\", \"646950\", \"646950\", \"646950\"};\n        assertArrayEquals(generatedImage, generate(5, 200, 255, 155, -20, -30, -15));\n    }\n*/\n\n    //Q7 (4) : String[] miroir(String[] image)\n\n/*\n    void test_miroir() {\n        String[] image2x2 = new String[]{\n            HEX_RED,   HEX_GREEN,\n            HEX_GREEN, HEX_RED};\n        String[] mirrored2x2 = new String[]{\n            HEX_GREEN, HEX_RED,\n            HEX_RED,   HEX_GREEN};\n        assertArrayEquals(mirrored2x2, miroir(image2x2));\n\n        String[] image3x3 = new String[]{\n            HEX_BLACK,  HEX_BLUE, HEX_GREEN,\n            HEX_BLACK,  HEX_BLUE,  HEX_BLUE,\n            HEX_BLACK, HEX_BLACK, HEX_BLACK};\n        String[] mirrored3x3 = new String[]{\n            HEX_GREEN,  HEX_BLUE, HEX_BLACK,\n             HEX_BLUE,  HEX_BLUE, HEX_BLACK,\n            HEX_BLACK, HEX_BLACK, HEX_BLACK};\n        assertArrayEquals(mirrored3x3, miroir(image3x3));\n    }\n*/\n\n    void algorithm() {\n        String[] image = new String[]{};\n        //image = generate(5, 200, 255, 155, -20, -30, -15);\n        for (int idx = 0; idx < length(image); idx = idx + 1) {\n            print(image[idx]);\n        }\n        println();\n        //show(image);\n        //println();\n        //image  = new String[]{\n        //    HEX_RED,   HEX_GREEN,\n        //    HEX_GREEN, HEX_RED};\n        //show(image);\n        //println();\n        //show(miroir(image));\n        //println();\n    }\n\n    /*\n    NE SURTOUT PAS TOUCHER AU CODE CI-DESSOUS !!!!\n    CE CODE EST FOURNI POUR QUE VOUS PUISSIEZ VISUALISER VOS IMAGES DANS LA CONSOLE.\n    IL RE-IMPLEMENTE LA FONCTION SHOW POUR S'ADAPTER A LA NOUVELLE REPRESENTATION HEXADECIIMALE DES COULEURS.\n    */\n    final int RED    = 0;\n    final int GREEN  = 1;\n    final int BLUE   = 2;\n    // Fonction get modifiée pour extraire la valeur décimale d'une couleur primaire à partir d'une chaîne hexadécimale utilisée par la fonction show\n    int get(String color, int primaryColor){\n        String r=\"\";\n        if(primaryColor == RED){\n            r = substring(color,0,2);\n        } else if(primaryColor == GREEN){\n            r = substring(color,2,4);\n        } else {\n            r = substring(color,4,6);\n        }\n        int res = 0;\n        for (int idx=0; idx<2; idx=idx+1){\n            char c = charAt(r, idx);\n            int val = 0;\n            if('0' <= c && c <= '9'){\n                val = (int) (c - '0');\n            } else {\n                val = 10 + (int) (c - 'A');\n            }\n            res = res * 16 + val;\n        }\n        return res;\n    }\n    // Affiche une image codée en hexadécimal\n    void show(String[] image) {\n        int imageSize = size(image);\n        for (int line = 0; line < size(image); line = line + 1) {\n            for (int column = 0; column < size(image); column = column + 1) {\n                String color = get(image, line, column);\n                String ansiColor = rgb(get(color, RED),\n                                       get(color, GREEN),\n                                       get(color, BLUE),\n                                    false);\n                print(ansiColor +' '+RESET);\n            }\n            println();\n        }\n    }\n")
    public void algorithm() {
        String[] strArr = new String[0];
        String[] generate = generate(5, 200, 255, 155, -20, -30, -15);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < length(generate)) {
                print(generate[i2]);
                i = i2 + 1;
            } else {
                println();
                show(generate);
                println();
                String[] strArr2 = {"FF0000", "00FF00", "00FF00", "FF0000"};
                show(strArr2);
                println();
                show(miroir(strArr2));
                println();
                return;
            }
        }
    }
}
