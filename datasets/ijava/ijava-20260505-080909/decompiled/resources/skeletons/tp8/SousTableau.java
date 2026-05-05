class SousTableau extends Program {

    void testSousTableau () {
            assertArrayEquals(new String[]{"a", "b", "c"},
                sousTableau(new String[]{"a", "b", "c", "d", "e"}, 0, 3));
            assertArrayEquals(new String[]{"a", "b"},
                sousTableau(new String[]{"a", "b"}, 0, 5));
            assertArrayEquals(new String[]{},
                sousTableau(new String[]{"a", "b", "c"}, 0, 0));
            assertArrayEquals(new String[]{"a", "b"},
                sousTableau(new String[]{"a", "b"}, 0, -5));

            // L’assertion suivante sert à tester qu’on retourne bien une copie du tableau
            // et non le tableau lui-même
            String[] tab = new String[]{"a", "b"};
            assertNotEquals(tab, sousTableau(tab,  2, 1));
            assertNotEquals(tab, sousTableau(tab, -1, 2));
            assertNotEquals(tab, sousTableau(tab,  1, 7));
        }

        void algorithm() {
            // ne rien mettre ici !
        }

}
