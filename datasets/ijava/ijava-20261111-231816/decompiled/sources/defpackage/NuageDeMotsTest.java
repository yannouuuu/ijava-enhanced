package defpackage;

/* loaded from: ijava.jar:NuageDeMotsTest.class */
class NuageDeMotsTest extends HiddenTest {
    NuageDeMotsTest() {
    }

    void test_algorithm_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "algorithm", new Class[0], program);
    }
}
