class SauteMouton extends Program {

    // définition de la fonction appliquer
        // à compléter ici

        // test de l'application d'un coup
        /*void test_appliquer_avancer_droite() {
            char[] prairie = new char[]{DROITE, ESPACE, GAUCHE};
            appliquer(prairie, new int[]{0, 1});
            assertArrayEquals(new char[]{ESPACE, DROITE, GAUCHE}, prairie);
        }
        void test_appliquer_avancer_gauche() {
            char[] prairie = new char[]{DROITE, ESPACE, GAUCHE};
            appliquer(prairie, new int[]{2, 1});
            assertArrayEquals(new char[]{DROITE, GAUCHE, ESPACE}, prairie);
        }*/

        // définition de la fonction saisir
        // à compléter ici

        // définition de la fonction bloque
        // à compléter ici

        void test_bloque() {
            assertFalse(bloque(new char[]{ESPACE, GAUCHE, DROITE, GAUCHE, DROITE}));
            assertFalse(bloque(new char[]{DROITE, GAUCHE, DROITE, GAUCHE, ESPACE}));
            assertTrue (bloque(new char[]{DROITE, DROITE, GAUCHE, GAUCHE, ESPACE}));
            assertTrue (bloque(new char[]{ESPACE, DROITE, DROITE, GAUCHE, GAUCHE}));
            assertTrue (bloque(new char[]{GAUCHE, GAUCHE, ESPACE, DROITE, DROITE}));
        }

        // définition de la fonction gagne
        // à compléter ici

        void test_gagne() {
            assertTrue(
                gagne(new char[]{GAUCHE, GAUCHE, GAUCHE, ESPACE, DROITE, DROITE, DROITE}));
            assertFalse(
                gagne(new char[]{GAUCHE, GAUCHE, GAUCHE, DROITE, ESPACE, DROITE, DROITE}));
        }

        void algorithm() {
            char[] prairie = initialiser(7);
            println(toString(prairie));
            //int[] coup = saisir(prairie);
            //appliquer(prairie, coup);
            //println(toString(prairie));
        }

}
