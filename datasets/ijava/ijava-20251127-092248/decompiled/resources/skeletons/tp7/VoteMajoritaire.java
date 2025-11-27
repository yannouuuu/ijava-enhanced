class VoteMajoritaire extends Program {

    void test_estAdopte(){
        assertFalse(estAdopte(new boolean[]{true,false}));
        assertTrue(estAdopte(new boolean[]{true,true,false}));
    }

    void algorithm() {
    }

}
