package defpackage;

/* loaded from: ijava.jar:JeuDeLaVieTest.class */
class JeuDeLaVieTest extends HiddenTest {
    final Class TAB_2D_BOOLEAN = new boolean[]{new boolean[0]}.getClass();

    /* JADX WARN: Multi-variable type inference failed */
    JeuDeLaVieTest() {
    }

    void test_afficher_exists(Program program) {
        assertFunctionIsDefined(String.class, "afficher", new Class[]{this.TAB_2D_BOOLEAN}, program);
    }

    void test_initialiser_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "initialiser", new Class[]{this.TAB_2D_BOOLEAN, Double.TYPE}, program);
    }

    void test_nombreDeVoisins_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "nombreDeVoisins", new Class[]{this.TAB_2D_BOOLEAN, Integer.TYPE, Integer.TYPE}, program);
    }

    void test_evolution_exists(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "evolution", new Class[]{Integer.TYPE, Boolean.TYPE}, program);
    }

    void test_nouvelleGeneration_exists(Program program) {
        assertFunctionIsDefined(this.TAB_2D_BOOLEAN, "nouvelleGeneration", new Class[]{this.TAB_2D_BOOLEAN}, program);
    }
}
