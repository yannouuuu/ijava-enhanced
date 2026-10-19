package defpackage;

/* loaded from: ijava.jar:TestBissextileTest.class */
class TestBissextileTest extends HiddenTest {
    TestBissextileTest() {
    }

    void test_test_bissextile_true_exists(Program program) {
        assertEquals(true, functionIsDefined("test_bissextile_true", program));
    }

    void test_test_bissextile_false_exists(Program program) {
        assertEquals(true, functionIsDefined("test_bissextile_false", program));
    }
}
