package defpackage;

/* loaded from: ijava.jar:FrequencesTest.class */
class FrequencesTest extends HiddenTest {
    FrequencesTest() {
    }

    void test_frequences_exists(Program program) {
        Class<?> cls = new String[0].getClass();
        assertFunctionIsDefined(new int[0].getClass(), "frequences", new Class[]{cls, cls}, program);
    }

    void test_frequences(Program program) {
        assertArrayEquals(new int[]{2, 0, 3}, ((Frequences) program).frequences(new String[]{"a", "b", "c"}, new String[]{"a", "a", "c", "c", "c"}));
    }
}
