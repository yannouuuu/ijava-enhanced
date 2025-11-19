package defpackage;

/* loaded from: ijava.jar:InitTableauTest.class */
class InitTableauTest extends HiddenTest {
    InitTableauTest() {
    }

    void test_creerTableau_no_parameters_exists(Program program) {
        assertFunctionIsDefined(int[].class, "creerTableau", new Class[0], program);
        assertFunctionIsDefined(Void.TYPE, "test_creerTableau_taille_fixe_10", new Class[0], program);
    }

    void test_creerTableau_int_parameter_exists(Program program) {
        assertFunctionIsDefined(int[].class, "creerTableau", new Class[]{Integer.TYPE}, program);
        assertFunctionIsDefined(Void.TYPE, "test_creerTableau_taille_en_parametre", new Class[0], program);
    }

    void test_creerTableauAleatoire_exists(Program program) {
        assertFunctionIsDefined(int[].class, "creerTableauAleatoire", new Class[]{Integer.TYPE}, program);
        assertFunctionIsDefined(Void.TYPE, "test_creerTableauAleatoire_valeurs_entre_0_et_20", new Class[0], program);
        assertFunctionIsDefined(Void.TYPE, "test_creerTableauAleatoire_toutes_les_valeurs_entre_0_et_20_sont_presentes", new Class[0], program);
    }
}
