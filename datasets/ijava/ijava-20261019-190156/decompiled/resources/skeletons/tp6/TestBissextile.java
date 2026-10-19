class TestBissextile extends Program {

    boolean bissextile(int annee) {
        // TODO: Complete this method
    }


    void test_bissextile_true() { // 2000, 2012, 2024, 1600
        assertEquals(true, bissextile(2000));
        assertEquals(true, bissextile(2012));
        assertEquals(true, bissextile(2024));
        assertEquals(true, bissextile(1600));
    }
    @Student(Student.Mode.STUB)
    void test_bissextile_false() { // 2013, 2006, 1999, 1000
        assertEquals(false, bissextile(2013));
        assertEquals(false, bissextile(2006));
        assertEquals(false, bissextile(1999));
        assertEquals(false, bissextile(1000));
    }
    @Student(Student.Mode.STUB)
    void algorithm() {
        // TODO: Complete this method
    }


}