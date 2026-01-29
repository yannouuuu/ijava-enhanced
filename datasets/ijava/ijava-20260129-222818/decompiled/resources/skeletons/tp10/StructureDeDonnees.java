class StructureDeDonnees extends Program {

    // définition des constantes pour les moutons et l'espace

        // initialisation d'une prairie en situation initiale
        // à compléter ...

        // test d'initialisation d'une prairie en situation initiale
        /*void test_initialiser_7() {
            assertArrayEquals(
                new char[]{DROITE, DROITE, DROITE, ESPACE, GAUCHE, GAUCHE, GAUCHE},
                initialiser(7));
        }
        void test_initialiser_3() {
            assertArrayEquals(
                new char[]{DROITE, ESPACE, GAUCHE},
                initialiser(3));
        }
        void test_initialiser_2() {
            assertArrayEquals(
                new char[]{DROITE, ESPACE, GAUCHE},
                initialiser(2));
        }*/

        // visualisation d'une prairie
        // à compléter ...

        // test de la visualisation d'une prairie
        /*void test_toString_vide() {
            char[] prairie_vide = new char[]{ESPACE, ESPACE, ESPACE, ESPACE, ESPACE};
            assertEquals("_____", toString(prairie_vide));
        }
        void test_toString_prairie_initiale() {
            char[] prairie_initiale = new char[]{
                DROITE, DROITE, DROITE, ESPACE, GAUCHE, GAUCHE, GAUCHE};
            assertEquals(">>>_<<<", toString(prairie_initiale));
        }
        void test_toString_prairie_inexistante() {
            char[] test_toString_prairie_inexistante = new char[]{};
            assertEquals("", toString(test_toString_prairie_inexistante));
        }*/

        void algorithm () {
            // A décommenter une fois les tests au vert !
            //char[] prairie = initialiser(7);
            //println(toString(prairie));
        }

}
