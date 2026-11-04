package defpackage;

/* loaded from: ijava.jar:MasqueChaineTest.class */
class MasqueChaineTest extends HiddenTest {
    MasqueChaineTest() {
    }

    void test_masque_HelloWorld_o(Program program) {
        assertEquals("....o..o.....", ((MasqueChaine) program).masque("Hello world !", 'o'));
    }

    void test_masque_HelloWorld_a(Program program) {
        assertEquals(".............", ((MasqueChaine) program).masque("Hello world !", 'a'));
    }

    void test_masque_vide_a(Program program) {
        assertEquals("", ((MasqueChaine) program).masque("", 'a'));
    }
}
