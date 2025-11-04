package defpackage;

/* loaded from: ijava.jar:DemineurTest.class */
class DemineurTest extends HiddenTest {
    final Class TAB_2D_BOOLEAN = new boolean[]{new boolean[0]}.getClass();

    /* JADX WARN: Multi-variable type inference failed */
    DemineurTest() {
    }

    void test_afficher_exists(Program program) {
        assertFunctionIsDefined(String.class, "afficher", new Class[]{this.TAB_2D_BOOLEAN, this.TAB_2D_BOOLEAN}, program);
    }

    void test_initialiserChamps_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "initialiserChamps", new Class[]{this.TAB_2D_BOOLEAN, Double.TYPE}, program);
    }
}
