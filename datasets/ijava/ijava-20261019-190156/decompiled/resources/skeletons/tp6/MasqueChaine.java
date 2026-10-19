class MasqueChaine extends Program {

    String masque(String phrase, char lettre) {
        // TODO: Complete this method
    }


    void test_masque() {
        assertEquals("a...a...a....", masque("au bal masqué", 'a'));
        assertEquals("................", masque("Tonari no Totoro", 'u'));
        assertEquals(".o......o..o.o.o", masque("Tonari no Totoro", 'o'));
        assertEquals("", masque("", 'z'));
    }


    void algorithm() {
        // TODO: Complete this method
    }

}