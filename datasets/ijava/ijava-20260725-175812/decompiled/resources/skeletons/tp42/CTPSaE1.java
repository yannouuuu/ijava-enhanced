class CTPSaE1 extends Program {

    // Afin d'alléger la lecture du code et des tests, on définit quelques constantes pour les couleurs de base représentée en hexadécimal
        final String HEX_RED   = "FF0000";
        final String HEX_GREEN = "00FF00";
        final String HEX_BLUE  = "0000FF";
        final String HEX_WHITE = "FFFFFF";
        final String HEX_BLACK = "000000";

        // Q1 (3) : char numberToHex(int digit)

    /*
        void test_toHexChar() {
            assertEquals('0', numberToHex(0));
            assertEquals('5', numberToHex(5));
            assertEquals('9', numberToHex(9));
            assertEquals('A', numberToHex(10));
            assertEquals('C', numberToHex(12));
            assertEquals('F', numberToHex(15));
        }
    */

        // Q2 (2): String toHex(int primaryColor)

    /*    void test_toHexString() {
            assertEquals("00", intToHex(0));
            assertEquals("05", intToHex(5));
            assertEquals("0A", intToHex(10));
            assertEquals("1F", intToHex(31));
            assertEquals("7F", intToHex(127));
            assertEquals("FF", intToHex(255));
        }
    *

        // Q3 (2) : String colorToHex(int red, int green, int blue)

        final String HEX_RED   = "FF0000";
        final String HEX_GREEN = "00FF00";
        final String HEX_BLUE  = "0000FF";
        final String HEX_WHITE = "FFFFFF";
        final String HEX_BLACK = "000000";

    /*
        void test_colorToHex() {
            assertEquals(HEX_RED,   colorToHex(255, 0, 0));     // rouge pur
            assertEquals(HEX_GREEN, colorToHex(0, 255, 0));     // vert pur
            assertEquals(HEX_BLUE,  colorToHex(0, 0, 255));     // bleu pur
            assertEquals(HEX_WHITE, colorToHex(255, 255, 255)); // blanc
            assertEquals(HEX_BLACK, colorToHex(0, 0, 0));       // noir
            assertEquals("7F7F7F", colorToHex(127, 127, 127));  // gris moyen
            assertEquals("123ABC", colorToHex(18, 58, 188));    // couleur quelconque
        }
    */

        // Q4 (2) : int size(String[] image)

        int size(String[] image) {
            return 0;
        }

    /*
        void test_size() {
            // 2x2 rouge et vert :
            // ROUGE, VERT,
            // VERT,  ROUGE
            // {"FF0000", "00FF00",
            //  "00FF00", "FF0000"}
            assertEquals(2, size(new String[]{
                HEX_RED, HEX_GREEN,
                HEX_RED, HEX_GREEN}));
            // 3x3 noir et bleu :
            // NOIR, BLEU, NOIR,
            // BLEU, NOIR, BLEU,
            // NOIR, BLEU, NOIR
            // "0000000000FF0000000000FF0000000000FF0000000000FF000000"
            assertEquals(3, size(new String[]{
                HEX_BLACK, HEX_BLUE,  HEX_BLACK,
                HEX_BLUE,  HEX_BLACK, HEX_BLUE,
                HEX_BLACK, HEX_BLUE,  HEX_BLACK}));
            // 4x4 blanc : 16 fois WHITE
            // "FFFFFFFFFFFFFFFFFFFFFFF....FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
            assertEquals(4, size(new String[]{
                HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE,
                HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE,
                HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE,
                HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE}));
        }
    */

        // Q5 (2): String get(String[] image, int line, int column)

        String get(String[] image, int line, int column) {
            return "";
        }

    /*
        void test_get() {
            // image2x2 = "FF0000" "00FF00"
            //            "00FF00" "FF0000";
            String[] image2x2 = new String[]{  HEX_RED, HEX_GREEN,
                                             HEX_GREEN,   HEX_RED};
            assertEquals(HEX_RED,   get(image2x2, 0, 0)); // rouge
            assertEquals(HEX_GREEN, get(image2x2, 0, 1)); // vert
            assertEquals(HEX_GREEN, get(image2x2, 1, 0)); // vert
            assertEquals(HEX_RED,   get(image2x2, 1, 1)); // rouge
            // image3x3 = "000000" "0000FF" "000000"
            //            "0000FF" "000000" "0000FF"
            //            "000000" "0000FF" "000000"
            String[] image3x3 = new String[]{HEX_BLACK, HEX_BLUE,  HEX_BLACK,
                                             HEX_BLUE,  HEX_GREEN, HEX_BLUE,
                                             HEX_BLACK, HEX_BLUE,  HEX_BLACK};
            assertEquals(HEX_BLACK, get(image3x3, 0, 0)); // noir
            assertEquals(HEX_BLUE,  get(image3x3, 0, 1)); // bleu
            assertEquals(HEX_BLACK, get(image3x3, 0, 2)); // noir
            assertEquals(HEX_BLUE,  get(image3x3, 1, 0)); // bleu
            assertEquals(HEX_GREEN, get(image3x3, 1, 1)); // vert
            assertEquals(HEX_BLUE,  get(image3x3, 1, 2)); // bleu
            assertEquals(HEX_BLACK, get(image3x3, 2, 0)); // noir
            assertEquals(HEX_BLUE,  get(image3x3, 2, 1)); // bleu
            assertEquals(HEX_BLACK, get(image3x3, 2, 2)); // noir
        }
    */

        //Q6 (4) : String[] generate(int size, int r, int g, int b, int stepR, int stepG, int stepB)

    /*
        void test_generate_5_200_255_155_moins20_moins30_moins15() {
            String[] generatedImage = new String[]{
                "B4E18C", "B4E18C", "B4E18C", "B4E18C", "B4E18C",
                "A0C37D", "A0C37D", "A0C37D", "A0C37D", "A0C37D",
                "8CA56E", "8CA56E", "8CA56E", "8CA56E", "8CA56E",
                "78875F", "78875F", "78875F", "78875F", "78875F",
                "646950", "646950", "646950", "646950", "646950"};
            assertArrayEquals(generatedImage, generate(5, 200, 255, 155, -20, -30, -15));
        }
    */

        //Q7 (4) : String[] miroir(String[] image)

    /*
        void test_miroir() {
            String[] image2x2 = new String[]{
                HEX_RED,   HEX_GREEN,
                HEX_GREEN, HEX_RED};
            String[] mirrored2x2 = new String[]{
                HEX_GREEN, HEX_RED,
                HEX_RED,   HEX_GREEN};
            assertArrayEquals(mirrored2x2, miroir(image2x2));

            String[] image3x3 = new String[]{
                HEX_BLACK,  HEX_BLUE, HEX_GREEN,
                HEX_BLACK,  HEX_BLUE,  HEX_BLUE,
                HEX_BLACK, HEX_BLACK, HEX_BLACK};
            String[] mirrored3x3 = new String[]{
                HEX_GREEN,  HEX_BLUE, HEX_BLACK,
                 HEX_BLUE,  HEX_BLUE, HEX_BLACK,
                HEX_BLACK, HEX_BLACK, HEX_BLACK};
            assertArrayEquals(mirrored3x3, miroir(image3x3));
        }
    */

        void algorithm() {
            String[] image = new String[]{};
            //image = generate(5, 200, 255, 155, -20, -30, -15);
            for (int idx = 0; idx < length(image); idx = idx + 1) {
                print(image[idx]);
            }
            println();
            //show(image);
            //println();
            //image  = new String[]{
            //    HEX_RED,   HEX_GREEN,
            //    HEX_GREEN, HEX_RED};
            //show(image);
            //println();
            //show(miroir(image));
            //println();
        }

        /*
        NE SURTOUT PAS TOUCHER AU CODE CI-DESSOUS !!!!
        CE CODE EST FOURNI POUR QUE VOUS PUISSIEZ VISUALISER VOS IMAGES DANS LA CONSOLE.
        IL RE-IMPLEMENTE LA FONCTION SHOW POUR S'ADAPTER A LA NOUVELLE REPRESENTATION HEXADECIIMALE DES COULEURS.
        */
        final int RED    = 0;
        final int GREEN  = 1;
        final int BLUE   = 2;
        // Fonction get modifiée pour extraire la valeur décimale d'une couleur primaire à partir d'une chaîne hexadécimale utilisée par la fonction show
        int get(String color, int primaryColor){
            String r="";
            if(primaryColor == RED){
                r = substring(color,0,2);
            } else if(primaryColor == GREEN){
                r = substring(color,2,4);
            } else {
                r = substring(color,4,6);
            }
            int res = 0;
            for (int idx=0; idx<2; idx=idx+1){
                char c = charAt(r, idx);
                int val = 0;
                if('0' <= c && c <= '9'){
                    val = (int) (c - '0');
                } else {
                    val = 10 + (int) (c - 'A');
                }
                res = res * 16 + val;
            }
            return res;
        }
        // Affiche une image codée en hexadécimal
        void show(String[] image) {
            int imageSize = size(image);
            for (int line = 0; line < size(image); line = line + 1) {
                for (int column = 0; column < size(image); column = column + 1) {
                    String color = get(image, line, column);
                    String ansiColor = rgb(get(color, RED),
                                           get(color, GREEN),
                                           get(color, BLUE),
                                        false);
                    print(ansiColor +' '+RESET);
                }
                println();
            }
        }

}
