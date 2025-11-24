package defpackage;

/* loaded from: ijava.jar:CTPSaE1v2Test.class */
class CTPSaE1v2Test extends HiddenTest {
    CTPSaE1v2Test() {
    }

    void test_colorCode_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "colorCode", new Class[]{new String[0].getClass(), String.class}, program);
    }

    void test_numberOfPixels_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "numberOfPixels", new Class[]{String.class}, program);
    }

    void test_convertOldToNew_exists(Program program) {
        assertFunctionIsDefined(new int[0].getClass(), "convertOldToNew", new Class[]{String.class, new String[0].getClass()}, program);
    }

    void test_convertNewToOld_exists(Program program) {
        assertFunctionIsDefined(String.class, "convertNewToOld", new Class[]{new int[0].getClass(), new String[0].getClass()}, program);
    }

    void test_shift_exists(Program program) {
        assertFunctionIsDefined(new String[0].getClass(), "shift", new Class[]{new String[0].getClass(), Boolean.TYPE}, program);
    }
}
