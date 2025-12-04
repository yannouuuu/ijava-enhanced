package defpackage;

/* loaded from: ijava.jar:EmailTest.class */
class EmailTest extends HiddenTest {
    EmailTest() {
    }

    void test_estPeutEtreUnEmail_exists(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "estPeutEtreUnEmail", new Class[]{String.class}, program);
    }
}
