package defpackage;

/* loaded from: ijava.jar:LettresCrushDefiTest.class */
class LettresCrushDefiTest extends HiddenTest {
    LettresCrushDefiTest() {
    }

    void test_solutionExiste_exists(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "solutionExiste", new Class[]{String.class}, program);
    }

    void test_test_solutionExiste_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "test_solutionExiste", new Class[0], program);
    }
}
