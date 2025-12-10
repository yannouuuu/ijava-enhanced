package defpackage;

/* loaded from: ijava.jar:PresidentTest.class */
class PresidentTest extends HiddenTest {
    PresidentTest() {
    }

    void test_President_nom_field_exists() {
        assertFieldIsDefined(String.class, "nom", this.studentClass);
    }

    void test_President_parti_field_exists() {
        assertFieldIsDefined(String.class, "parti", this.studentClass);
    }

    void test_President_debut_field_exists() {
        assertFieldIsDefined(Date.class, "debut", this.studentClass);
    }

    void test_President_fin_field_exists() {
        assertFieldIsDefined(Date.class, "fin", this.studentClass);
    }
}
