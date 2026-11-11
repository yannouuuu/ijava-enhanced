class Occurrences extends Program {

    void test_nbFois () {
        String[] mots = new String[]{"je", "tu", "tu", "elle" , "je" , "je"};
        // "je" apparait 3 fois dans tab
        assertEquals(3, nbFois(mots, "je"));
        assertEquals(2, nbFois(mots, "tu"));
        assertEquals(0, nbFois(mots, "il"));
    }


    
    void algorithm() {
        // ne rien écrire ici !
    }

}