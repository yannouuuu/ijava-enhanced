class Frequences extends Program {

    void test_frequences_je_tu_elle() {
            String[] motsCles = new String[]{"je", "tu", "elle"};
            int[]    freq     = new int[]{3, 2, 1};
            String[] mots     = new String[]{"je", "tu", "tu", "elle", "je", "je"};
            assertArrayEquals(freq, frequences(motsCles, mots));
        }

        void algorithm() {
        }

}
