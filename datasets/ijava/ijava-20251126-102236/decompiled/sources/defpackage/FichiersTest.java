package defpackage;

/* loaded from: ijava.jar:FichiersTest.class */
public class FichiersTest extends HiddenTest {
    void test_choixFichier_exists(Program program) {
        assertFunctionIsDefined(String.class, "choixFichier", new Class[]{String.class}, program);
    }

    void test_nbLignes_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nbLignes", new Class[]{String.class}, program);
    }

    void test_test_nbLignes_animaux_txt_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "test_nbLignes_animaux_txt", new Class[0], program);
    }

    void test_dump_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "dump", new Class[]{String.class}, program);
    }
}
