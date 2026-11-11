package defpackage;

/* loaded from: ijava.jar:SousTableauTest.class */
class SousTableauTest extends HiddenTest {
    SousTableauTest() {
    }

    void test_sousTableau_exists(Program program) {
        Class<?> cls = new String[0].getClass();
        assertFunctionIsDefined(cls, "sousTableau", new Class[]{cls, Integer.TYPE, Integer.TYPE}, program);
    }
}
