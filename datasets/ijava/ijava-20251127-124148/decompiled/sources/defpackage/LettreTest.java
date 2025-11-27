package defpackage;

/* loaded from: ijava.jar:LettreTest.class */
class LettreTest extends HiddenTest {
    LettreTest() {
    }

    void test_Lettre_caractere_field_exists() {
        assertFieldIsDefined(Character.TYPE, "caractere", this.studentClass);
    }

    void test_Lettre_decouvert_field_exists() {
        assertFieldIsDefined(Boolean.TYPE, "decouvert", this.studentClass);
    }
}
