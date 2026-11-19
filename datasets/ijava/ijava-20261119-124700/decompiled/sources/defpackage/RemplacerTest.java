package defpackage;

/* loaded from: ijava.jar:RemplacerTest.class */
class RemplacerTest extends HiddenTest {
    RemplacerTest() {
    }

    void test_copieEnRemplacant_exists(Program program) {
        assertEquals(true, functionIsDefined("copieEnRemplacant", new Class[]{String.class, String.class, String.class}, program));
    }
}
