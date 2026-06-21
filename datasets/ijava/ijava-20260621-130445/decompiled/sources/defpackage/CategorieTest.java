package defpackage;

/* loaded from: ijava.jar:CategorieTest.class */
class CategorieTest extends HiddenTest {
    CategorieTest() {
    }

    void test_Categorie_theme_field_exists() {
        assertFieldIsDefined(Theme.class, "theme", this.studentClass);
    }

    void test_Categorie_sujet_field_exists() {
        assertFieldIsDefined(String.class, "sujet", this.studentClass);
    }

    void test_Categorie_mots_field_exists() {
        assertFieldIsDefined(String[].class, "mots", this.studentClass);
    }
}
