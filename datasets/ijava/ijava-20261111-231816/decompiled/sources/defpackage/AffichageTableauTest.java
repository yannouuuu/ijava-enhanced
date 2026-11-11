package defpackage;

/* loaded from: ijava.jar:AffichageTableauTest.class */
public class AffichageTableauTest extends HiddenTest {
    void test_toString_tab_int_exists(Program program) {
        assertFunctionIsDefined(String.class, "toString", new Class[]{int[].class}, program);
    }

    void test_toString_tab_string_exists(Program program) {
        assertFunctionIsDefined(String.class, "toString", new Class[]{String[].class}, program);
    }
}
