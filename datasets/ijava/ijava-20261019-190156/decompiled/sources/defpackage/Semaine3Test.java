package defpackage;

/* loaded from: ijava.jar:Semaine3Test.class */
class Semaine3Test extends HiddenTest {
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;
    final String CODES = "NBrvb";
    final String COLORS = "000000000255255255255000000000255000000000255";
    final String IMAGE_PAS_TERRIBLE = "NNNNNNNNNbbbbbbNNbbbbbbNNbbrrbbNNbrrrrbNNvrrrrvNNvvvvvvNNNNNNNNN";

    Semaine3Test() {
    }

    void test_getColorIndex_N(Program program) {
        assertEquals(0, ((Semaine3) program).getColorIndex("NBrvb", 'N'));
    }

    void test_getColorIndex_r(Program program) {
        assertEquals(2, ((Semaine3) program).getColorIndex("NBrvb", 'r'));
    }

    void test_getColorIndex_b(Program program) {
        assertEquals(4, ((Semaine3) program).getColorIndex("NBrvb", 'b'));
    }

    void test_getColorIndex_z(Program program) {
        assertEquals(-1, ((Semaine3) program).getColorIndex("NBrvb", 'z'));
    }

    void test_getColorForCode_N(Program program) {
        assertEquals("000000000", ((Semaine3) program).getColorForCode("NBrvb", "000000000255255255255000000000255000000000255", 'N'));
    }

    void test_getColorForCode_r(Program program) {
        assertEquals("255000000", ((Semaine3) program).getColorForCode("NBrvb", "000000000255255255255000000000255000000000255", 'r'));
    }

    void test_getColorForCode_v(Program program) {
        assertEquals("000255000", ((Semaine3) program).getColorForCode("NBrvb", "000000000255255255255000000000255000000000255", 'v'));
    }

    void test_getColorForCode_b(Program program) {
        assertEquals("000000255", ((Semaine3) program).getColorForCode("NBrvb", "000000000255255255255000000000255000000000255", 'b'));
    }

    void test_getColorForCode_z(Program program) {
        assertEquals("", ((Semaine3) program).getColorForCode("NBrvb", "000000000255255255255000000000255000000000255", 'z'));
    }

    void test_convert_NBBN(Program program) {
        assertEquals("000000000255255255255255255000000000", ((Semaine3) program).convert("NBrvb", "000000000255255255255000000000255000000000255", "NBBN"));
    }

    void test_convert_rvbN(Program program) {
        assertEquals("255000000000255000000000255000000000", ((Semaine3) program).convert("NBrvb", "000000000255255255255000000000255000000000255", "rvbN"));
    }
}
