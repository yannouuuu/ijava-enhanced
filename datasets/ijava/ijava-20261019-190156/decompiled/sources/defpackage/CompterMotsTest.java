package defpackage;

/* loaded from: ijava.jar:CompterMotsTest.class */
class CompterMotsTest extends HiddenTest {
    CompterMotsTest() {
    }

    void test_espace_debut_et_fin_lab(Program program) {
        assertEquals(3, ((CompterMots) program).nombreDeMots(" bla bla bla "));
    }

    void test_espace_debut_mais_pas_fin(Program program) {
        assertEquals(3, ((CompterMots) program).nombreDeMots(" bla bla bla"));
    }

    void test_espace_fin_mais_pas_debut(Program program) {
        assertEquals(3, ((CompterMots) program).nombreDeMots("bla bla bla "));
    }

    void test_pas_d_espace_du_tout(Program program) {
        assertEquals(1, ((CompterMots) program).nombreDeMots("bla"));
    }

    void test_chaine_vide(Program program) {
        assertEquals(0, ((CompterMots) program).nombreDeMots(""));
    }
}
