class VoteMajoritaire extends Program {
    void test_estAdopte() {
        assertFalse(estAdopte(new boolean[] { true, false }));
        assertTrue(estAdopte(new boolean[] { true, true, false }));
    }


    void algorithm() {
        // TODO: Complete this method
    }


    boolean estAdopte(boolean[] votes) {
        return (random() < 0.5);
    }

}