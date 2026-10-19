package defpackage;

/* loaded from: ijava.jar:SousChaineTest.class */
class SousChaineTest extends HiddenTest {
    SousChaineTest() {
    }

    void testContient_blablabla_lab(Program program) {
        assertEquals(true, ((SousChaine) program).contient("blablabla", "lab"));
    }

    void testContient_blablabla_bal(Program program) {
        assertEquals(false, ((SousChaine) program).contient("blablabla", "bal"));
    }

    void testContient_abc_vide(Program program) {
        assertEquals(true, ((SousChaine) program).contient("abc", ""));
    }

    void testContient_vide_vide(Program program) {
        assertEquals(true, ((SousChaine) program).contient("", ""));
    }

    void testContient_abcdef_defg(Program program) {
        assertEquals(false, ((SousChaine) program).contient("abcdef", "defg"));
    }

    void testContient_abc_abcdef(Program program) {
        assertEquals(false, ((SousChaine) program).contient("abc", "abcdef"));
    }
}
