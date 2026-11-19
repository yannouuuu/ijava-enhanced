package defpackage;

/* loaded from: ijava.jar:OccurrencesTest.class */
class OccurrencesTest extends HiddenTest {
    OccurrencesTest() {
    }

    void test_nbFois_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nbFois", new Class[]{new String[0].getClass(), String.class}, program);
    }
}
