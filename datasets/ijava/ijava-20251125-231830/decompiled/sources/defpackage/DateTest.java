package defpackage;

/* loaded from: ijava.jar:DateTest.class */
class DateTest extends HiddenTest {
    DateTest() {
    }

    void test_Date_jour_exist() {
        assertFieldIsDefined(Integer.TYPE, "jour", this.studentClass);
    }

    void test_Date_mois_exist() {
        assertFieldIsDefined(Integer.TYPE, "mois", this.studentClass);
    }

    void test_Date_annee_exist() {
        assertFieldIsDefined(Integer.TYPE, "annee", this.studentClass);
    }
}
