package defpackage;

/* loaded from: ijava.jar:MiseAJourPresidentsTest.class */
class MiseAJourPresidentsTest extends HiddenTest {
    MiseAJourPresidentsTest() {
    }

    void test_Date_newDate_exists(Program program) {
        assertFunctionIsDefined(Date.class, "newDate", new Class[]{String.class}, program);
    }

    void test_Date_toString_exists(Program program) {
        assertFunctionIsDefined(String.class, "toString", new Class[]{Date.class}, program);
    }

    void test_President_newPresident_exists(Program program) {
        assertFunctionIsDefined(President.class, "newPresident", new Class[]{String.class, String.class, Date.class, Date.class}, program);
    }

    void test_President_toString_exists(Program program) {
        assertFunctionIsDefined(String.class, "toString", new Class[]{President.class}, program);
    }

    void test_President_load_exists(Program program) {
        assertFunctionIsDefined(President[].class, "load", new Class[]{String.class, Integer.TYPE}, program);
    }

    void test_President_nombreDe_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nombreDe", new Class[]{President[].class, String.class}, program);
    }

    void test_President_regneMaximal_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "regneMaximal", new Class[]{President[].class, String.class}, program);
    }
}
