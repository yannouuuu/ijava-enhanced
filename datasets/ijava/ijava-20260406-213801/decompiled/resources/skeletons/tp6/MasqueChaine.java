class MasqueChaine extends Program {

    void testMasque () {
            assertEquals("a...a...a....",    masque("au bal masqué",    'a'));
            assertEquals("................", masque("Tonari no Totoro", 'u'));
            assertEquals(".o......o..o.o.o", masque("Tonari no Totoro", 'o'));
            assertEquals("",                 masque("",                 'z'));
        }
        void algorithm() {
        }

}
