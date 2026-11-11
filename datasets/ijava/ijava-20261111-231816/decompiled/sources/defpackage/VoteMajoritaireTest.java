package defpackage;

/* loaded from: ijava.jar:VoteMajoritaireTest.class */
class VoteMajoritaireTest extends HiddenTest {
    VoteMajoritaireTest() {
    }

    void test_estAdopte_exists(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "estAdopte", new Class[]{boolean[].class}, program);
    }
}
