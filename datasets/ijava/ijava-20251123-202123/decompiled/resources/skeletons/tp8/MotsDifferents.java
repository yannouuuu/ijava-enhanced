class MotsDifferents extends Program {

    void test_motsDifferents () {
            String[] mots = new String[]{"je", "tu", "tu", "elle", "je", "je"};
            String[] motsDiff = motsDifferents(mots);
            for (int idx = 0; idx < length(mots); idx = idx+1){
                assertEquals(1, nbFois(motsDiff,mots[idx]));
                //on s’assure que chaque mot apparaît une et une seule fois dans motsDiff
            }
            //on vérifie que les mots sont bien à la bonne place
            assertEquals("je",  motsDiff[0]);
            assertEquals("tu",  motsDiff[1]);
            assertEquals("elle",motsDiff[2]);
            for (int idx = 3; idx < length(motsDiff); idx = idx+1){
                assertEquals("", motsDiff[idx]);
            }
        }

        void algorithm() {
            // ne rien mettre ici pour l'instant ...
        }

}
