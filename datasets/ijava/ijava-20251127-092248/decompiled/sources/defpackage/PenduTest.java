package defpackage;

/* loaded from: ijava.jar:PenduTest.class */
class PenduTest extends HiddenTest {
    PenduTest() {
    }

    void test_test_newLettre_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "test_newLettre", new Class[0], program);
    }

    void test_newLettre_exists(Program program) {
        assertFunctionIsDefined(Lettre.class, "newLettre", new Class[]{Character.TYPE}, program);
    }

    void test_test_creerMot(Program program) {
        assertFunctionIsDefined(Void.TYPE, "test_creerMot", new Class[0], program);
    }

    void test_creerMot_exists(Program program) {
        assertFunctionIsDefined(Lettre[].class, "creerMot", new Class[]{String.class}, program);
    }
}
