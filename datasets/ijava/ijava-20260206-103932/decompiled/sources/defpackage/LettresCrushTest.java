package defpackage;

/* loaded from: ijava.jar:LettresCrushTest.class */
class LettresCrushTest extends HiddenTest {
    LettresCrushTest() {
    }

    void test_explorer_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "explorer", new Class[]{String.class, Integer.TYPE, Character.TYPE, Integer.TYPE}, program);
    }

    void test_test_explorer_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "test_explorer", new Class[0], program);
    }
}
