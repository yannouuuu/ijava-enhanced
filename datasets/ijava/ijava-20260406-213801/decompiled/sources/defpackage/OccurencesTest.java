package defpackage;

/* loaded from: ijava.jar:OccurencesTest.class */
class OccurencesTest extends HiddenTest {
    OccurencesTest() {
    }

    void test_nbOccurrences_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nbOccurrences", new Class[]{String.class, Character.TYPE}, program);
    }

    void test_nbOccurencesTerminale_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nbOccurencesTerminale", new Class[]{String.class, Character.TYPE}, program);
    }
}
