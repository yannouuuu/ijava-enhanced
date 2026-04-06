package defpackage;

/* loaded from: ijava.jar:CTPPreTDTest.class */
class CTPPreTDTest extends HiddenTest {
    final Class TAB_2D_INT = new int[]{new int[0]}.getClass();

    /* JADX WARN: Multi-variable type inference failed */
    CTPPreTDTest() {
    }

    void test_creer_exists(Program program) {
        assertFunctionIsDefined(this.TAB_2D_INT, "creer", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}, program);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void test_creer_2_0_32_64(Program program) {
        assertArrayEquals(new int[]{new int[]{0, 0, 0, 0}, new int[]{32, 32, 32, 32}, new int[]{64, 64, 64, 64}}, ((CTPPreTD) program).creer(2, 0, 32, 64));
    }

    void test_toString_exists(Program program) {
        assertFunctionIsDefined(String.class, "toString", new Class[]{Integer.TYPE}, program);
    }

    void test_toString_0(Program program) {
        assertEquals("000", ((CTPPreTD) program).toString(0));
    }

    void test_toString_32(Program program) {
        assertEquals("032", ((CTPPreTD) program).toString(32));
    }

    void test_toString_128(Program program) {
        assertEquals("128", ((CTPPreTD) program).toString(128));
    }

    void test_debug_exists(Program program) {
        assertFunctionIsDefined(String.class, "debug", new Class[]{this.TAB_2D_INT}, program);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [int[], int[][]] */
    void test_debug_2_0_32_64(Program program) {
        assertEquals("(000,032,064) (000,032,064) \n(000,032,064) (000,032,064) \n", ((CTPPreTD) program).debug((int[][]) new int[]{new int[]{0, 0, 0, 0}, new int[]{32, 32, 32, 32}, new int[]{64, 64, 64, 64}}));
    }

    void test_show_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "show", new Class[]{this.TAB_2D_INT}, program);
    }
}
