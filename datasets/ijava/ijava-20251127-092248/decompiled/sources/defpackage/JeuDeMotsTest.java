package defpackage;

/* loaded from: ijava.jar:JeuDeMotsTest.class */
class JeuDeMotsTest extends HiddenTest {
    JeuDeMotsTest() {
    }

    void test_JeuDeMots_choixFichier_method_exists(Program program) {
        assertFunctionIsDefined(String.class, "choixFichier", new Class[]{String.class}, program);
    }

    void test_JeuDeMots_nbLignes_method_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nbLignes", new Class[]{String.class}, program);
    }

    void test_JeuDeMots_chargerCategorie_method_exists(Program program) {
        assertFunctionIsDefined(Categorie.class, "chargerCategorie", new Class[]{String.class}, program);
    }

    void test_JeuDeMots_affichageCategorie_method_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "affichageCategorie", new Class[]{Categorie.class}, program);
    }
}
