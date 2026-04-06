class Occurrences extends Program {

    void test_nbFois () {
            String[] tab = new String[]{"je", "tu", "tu", "elle" , "je" , "je"};
            // "je" apparait 3 fois dans tab
            assertEquals(3, nbFois(tab, "je"));
            assertEquals(2, nbFois(tab, "tu"));
            assertEquals(0, nbFois(tab, "il"));
        }

        void algorithm() {
            // ne rien écrire ici !
        }

}
