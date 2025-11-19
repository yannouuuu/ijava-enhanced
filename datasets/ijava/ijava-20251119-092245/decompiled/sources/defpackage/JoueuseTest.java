package defpackage;

/* loaded from: ijava.jar:JoueuseTest.class */
class JoueuseTest extends HiddenTest {
    JoueuseTest() {
    }

    void test_Joueuse_caractere_field_exists() {
        assertFieldIsDefined(String.class, "nom", this.studentClass);
    }

    void test_Joueuse_decouvert_field_exists() {
        assertFieldIsDefined(boolean[].class, "parties", this.studentClass);
    }
}
