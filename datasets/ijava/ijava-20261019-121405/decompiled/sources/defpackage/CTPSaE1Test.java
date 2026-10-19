package defpackage;

/* loaded from: ijava.jar:CTPSaE1Test.class */
class CTPSaE1Test extends HiddenTest {
    CTPSaE1Test() {
    }

    void test_numberToHex_exists(Program program) {
        assertFunctionIsDefined(Character.TYPE, "numberToHex", new Class[]{Integer.TYPE}, program);
    }

    void test_intToHex_exists(Program program) {
        assertFunctionIsDefined(String.class, "intToHex", new Class[]{Integer.TYPE}, program);
    }

    void test_colorToHex_exists(Program program) {
        assertFunctionIsDefined(String.class, "colorToHex", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}, program);
    }

    void test_size_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "size", new Class[]{new String[0].getClass()}, program);
    }

    void test_get_exists(Program program) {
        assertFunctionIsDefined(String.class, "get", new Class[]{new String[0].getClass(), Integer.TYPE, Integer.TYPE}, program);
    }

    void test_generate_exists(Program program) {
        assertFunctionIsDefined(new String[0].getClass(), "generate", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}, program);
    }
}
