package defpackage;

/* loaded from: ijava.jar:Semaine4.class */
class Semaine4 extends Program {
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;
    int lineTurtle = 0;
    int columnTurtle = 0;
    boolean penDown = true;
    String colorPen = color(255, 0, 0);
    String currentImage = "";
    final int[] NORTH = {-1, 0};
    final int[] EAST = {0, 1};
    final int[] SOUTH = {1, 0};
    final int[] WEST = {0, -1};

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

    /* JADX INFO: Access modifiers changed from: package-private */
    public String get(String str, int i, int i2) {
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
                println();
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void togglePen() {
        this.penDown = !this.penDown;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean within(int i, int i2, int i3) {
        return i3 >= 0 && i3 <= i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inside(int i, int i2, int i3) {
        return within(0, i - 1, i2) && within(0, i - 1, i3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentImage(String str) {
        this.currentImage = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String set(String str, int i, int i2, String str2) {
        int size = (i * size(str) * 9) + (i2 * 9);
        return (("" + substring(str, 0, size)) + str2) + substring(str, size + 9, length(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trace() {
        if (this.penDown) {
            this.currentImage = set(this.currentImage, this.lineTurtle, this.columnTurtle, this.colorPen);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean go(int i, int i2) {
        boolean z = false;
        if (inside(size(this.currentImage), i, i2)) {
            this.lineTurtle = i;
            this.columnTurtle = i2;
            z = true;
            trace();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean move(int[] iArr) {
        boolean z = false;
        int i = this.lineTurtle + iArr[0];
        int i2 = this.columnTurtle + iArr[1];
        if (inside(size(this.currentImage), i, i2)) {
            this.lineTurtle = i;
            this.columnTurtle = i2;
            z = true;
            trace();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    @inject("// Semaine 1 : couleurs\n// On représente une couleur par trois couleurs primaires\n// correspondant au trois intensité de rouge (red), vert (green) et bleu (blue)\n// Ces intensités varient entre 0 et 255.\n// Ainsi, une couleur peut-être représenté par : \"255044138\"\n// pour red = 255, green = 044 et blue = 138 (le 0 est important!)\nfinal int RED    = 0;\nfinal int GREEN  = 1;\nfinal int BLUE   = 2;\n\nint charToInt(char digit) {\n    return (int) (digit - '0');\n}\n\nString toString(int primaryColor) {\n    String valeur = \"00\"+primaryColor;\n    return substring(valeur, length(valeur)-3, length(valeur));\n}\n\nString color(int red, int green, int blue) {\n    return toString(red) + toString(green) + toString(blue);\n}\n\nint primaryColorToInt(String primaryColor) {\n    return charToInt(charAt(primaryColor, 0)) * 100 +\n           charToInt(charAt(primaryColor, 1)) *  10 +\n           charToInt(charAt(primaryColor, 2));\n}\n\nint primaryColorIndex(int primaryColor) {\n    return primaryColor * 3;\n}\n\nint get(String color, int primaryColor) {\n    int indiceDebut = primaryColorIndex(primaryColor);\n    return primaryColorToInt(substring(color, indiceDebut, indiceDebut + 3));\n}\n\nString set(String color, int primaryColor, int valeur) {\n    String newColor = \"\";\n    int idxStart = primaryColorIndex(primaryColor);\n    // on copie ce qui est avant l'indice de primaryColor (éventuellement rien)\n    newColor = newColor + substring(color, 0, idxStart);\n    // on accumule la nouvelle valeur en la normalisant avant\n    newColor = newColor + toString(valeur);\n    // on copie tout ce qui est après la composte\n    newColor = newColor + substring(color, idxStart+3, length(color));\n    return newColor;\n}\n\n// Semaine 2 : images\n// On souhaite maintenant représenter une grille carrée de pixels de couleurs.\n// Le choix de la grille carré évite d'avoir à encoder les dimensions de l'image.\n// Pour cela, on propose d'utiliser une simple chaîne de caractères contenant\n// tous les pixels les uns à la suite des autres.\n\nint size(String image) {\n    return sqrt(length(image)/9);\n}\n\n// on sait que la grille est carré, donc on récupère d'abord sa taille\n// avec size, on peut calculer l'indice de début du pixel nous intéressant\n// en multipliant par 9 l'indice de colonne (vu qu'un pixel fait 9 caractères) et\n// en multipliant l'indice de ligne par la taille d'une ligne dans l'image.\nString get(String image, int line, int column) {\n    int imageSize = size(image);\n    int idxStart  = line * imageSize * 9 + column * 9;\n    return substring(image, idxStart, idxStart + 9);\n}\n\nString generate(int size, int r, int g, int b, int stepR, int stepG, int stepB) {\n    String image = \"\";\n    int red = r, green = g, blue = b;\n    for (int line = 0; line < size * size; line = line + 1) {\n        if (line % size == 0) {\n            red   = (red   + stepR) % 255;\n            green = (green + stepG) % 255;\n            blue  = (blue  + stepB) % 255;\n        }\n        image = image + color(red, green, blue);\n    }\n    return image;\n}\n\nvoid show(String image) {\n    int imageSize = size(image);\n    for (int line = 0; line < size(image); line = line + 1) {\n        for (int column = 0; column < size(image); column = column + 1) {\n            String color = get(image, line, column);\n            String ansiColor = rgb(get(color, RED),\n                                   get(color, GREEN),\n                                get(color, BLUE),\n                                false);\n            print(ansiColor + ' ' + RESET);\n        }\n        println();\n    }\n}\n\nvoid algorithm() {\n    String image = generate(5, 200, 255, 155, -20, -30, -15);\n    println(rgb(255,125,75, false) + image + RESET);\n    show(image);\n    image = generate(10, 0, 0, 0, 25, 40, 55);\n    println(rgb(255,125,75, true) + image + RESET);\n    show(image);\n}\n")
    public void algorithm() {
        setCurrentImage(generate(5, 128, 128, 128, 0, 0, 0));
        this.penDown = true;
        this.colorPen = color(0, 0, 0);
        go(3, 3);
        show(this.currentImage);
        move(this.NORTH);
        show(this.currentImage);
        move(this.SOUTH);
        show(this.currentImage);
        move(this.EAST);
        show(this.currentImage);
        move(this.WEST);
        show(this.currentImage);
    }
}
