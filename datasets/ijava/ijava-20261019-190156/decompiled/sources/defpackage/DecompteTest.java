package defpackage;

/* loaded from: ijava.jar:DecompteTest.class */
class DecompteTest extends HiddenTest {
    DecompteTest() {
    }

    void test_function_genereNombresPairs1_exists(Program program) {
        assertEquals(true, functionIsDefined("genereNombresPairs1", new Class[]{Integer.TYPE}, program));
    }

    void test_function_genereNombresPairs2_exists(Program program) {
        assertEquals(true, functionIsDefined("genereNombresPairs2", new Class[]{Integer.TYPE}, program));
    }

    void test_function_genereNombresPairs3_exists(Program program) {
        assertEquals(true, functionIsDefined("genereNombresPairs3", new Class[]{Integer.TYPE}, program));
    }
}
