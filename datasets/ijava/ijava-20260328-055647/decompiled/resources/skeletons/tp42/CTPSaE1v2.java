class CTPSaE1v2 extends Program {

    // Q1 : Définition de la fonction colorCode (4 pt)

            // Q2 : Définition de la fonction numberOfPixels (2 pt)

            // Q3 : int[] convertOldToNew(String image, String[] palette) (4 pts)

            // Q4 : String convertNewToOld(int[] image, String[] palette) (4 pts)

            // Q5 : String[] shift(String[] palette, boolean right) (3 pts)

            // Q6 : void show(int[] image, String[] palette) (3 pts)

            void algorithm() {
                final String IMAGE_3x3 = "255000000"+"000255000"+"000000255"+
                                        "000000000"+"255000000"+"000255000"+
                                        "255255255"+"000000000"+"255000000";
                final String[] PALETTE = {
                    "255000000", // rouge
                    "000255000", // vert
                    "000000255", // bleu
                    "000000000", // noir
                    "255255255"  // blanc
                };
                //show(convertOldToNew(IMAGE, PALETTE), PALETTE);
            }

}
