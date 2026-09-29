package defpackage;

/* loaded from: ijava.jar:Semaine4.class */
class Semaine4 extends Program {
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;

    Semaine4() {
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

    int colorForLetter(String str) {
        return 0;
    }

    int colorIndex(String str, char c) {
        int i;
        int i2 = 0;
        while (true) {
            i = i2;
            if (i >= length(str) || charAt(str, i) == c) {
                break;
            }
            i2 = i + 10;
        }
        if (i > length(str)) {
            i = -1;
        }
        return i;
    }

    int getColorForLetter(String str, char c) {
        return 0;
    }

    String generate(String str, String str2) {
        return "";
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
    @Override // defpackage.Program
    @inject("// Semaine 1 : couleurs\n// On représente une couleur par trois couleurs primaires\n// correspondant au trois intensité de rouge (red), vert (green) et bleu (blue)\n// Ces intensités varient entre 0 et 255.\n// Ainsi, une couleur peut-être représenté par : \"255044138\"\n// pour red = 255, green = 044 et blue = 138 (le 0 est important!)\nfinal int RED    = 0;\nfinal int GREEN  = 1;\nfinal int BLUE   = 2;\n\nint charToInt(char digit) {\n    return (int) (digit - '0');\n}\n\nString toString(int primaryColor) {\n    String valeur = \"00\"+primaryColor;\n    return substring(valeur, length(valeur)-3, length(valeur));\n}\n\nString color(int red, int green, int blue) {\n    return toString(red) + toString(green) + toString(blue);\n}\n\nint primaryColorToInt(String primaryColor) {\n    return charToInt(charAt(primaryColor, 0)) * 100 +\n           charToInt(charAt(primaryColor, 1)) *  10 +\n           charToInt(charAt(primaryColor, 2));\n}\n\nint primaryColorIndex(int primaryColor) {\n    return primaryColor * 3;\n}\n\nint get(String color, int primaryColor) {\n    int indiceDebut = primaryColorIndex(primaryColor);\n    return primaryColorToInt(substring(color, indiceDebut, indiceDebut + 3));\n}\n\nString set(String color, int primaryColor, int valeur) {\n    String newColor = \"\";\n    int idxStart = primaryColorIndex(primaryColor);\n    // on copie ce qui est avant l'indice de primaryColor (éventuellement rien)\n    newColor = newColor + substring(color, 0, idxStart);\n    // on accumule la nouvelle valeur en la normalisant avant\n    newColor = newColor + toString(valeur);\n    // on copie tout ce qui est après la composte\n    newColor = newColor + substring(color, idxStart+3, length(color));\n    return newColor;\n}\n\nvoid algorithm() {\n    /*\n    String image = generate(5, 200, 255, 155, -20, -30, -15);\n    println(rgb(255,125,75, false) + image + RESET);\n    show(image);\n    image = generate(10, 0, 0, 0, 25, 40, 55);\n    println(rgb(255,125,75, true) + image + RESET);\n    show(image);\n    */\n}\n")
    public void algorithm() {
    }
}
