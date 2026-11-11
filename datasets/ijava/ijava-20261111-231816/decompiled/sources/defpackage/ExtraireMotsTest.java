package defpackage;

/* loaded from: ijava.jar:ExtraireMotsTest.class */
class ExtraireMotsTest extends HiddenTest {
    ExtraireMotsTest() {
    }

    void test_extraireMots_exists(Program program) {
        assertFunctionIsDefined(new String[0].getClass(), "extraireMots", new Class[]{String.class}, program);
    }
}
