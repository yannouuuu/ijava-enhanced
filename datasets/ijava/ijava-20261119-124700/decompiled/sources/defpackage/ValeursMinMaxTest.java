package defpackage;

/* loaded from: ijava.jar:ValeursMinMaxTest.class */
class ValeursMinMaxTest extends HiddenTest {
    ValeursMinMaxTest() {
    }

    void test_minMax_exists(Program program) {
        assertFunctionIsDefined(int[].class, "minMax", new Class[]{int[].class}, program);
        assertFunctionIsDefined(Void.TYPE, "test_minMax", new Class[0], program);
    }

    void test_indicesMinMax_exists(Program program) {
        assertFunctionIsDefined(int[].class, "indicesMinMax", new Class[]{double[].class}, program);
        assertFunctionIsDefined(Void.TYPE, "test_indicesMinMax", new Class[0], program);
    }
}
