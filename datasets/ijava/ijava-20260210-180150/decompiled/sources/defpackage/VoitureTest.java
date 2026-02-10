package defpackage;

/* loaded from: ijava.jar:VoitureTest.class */
class VoitureTest extends HiddenTest {
    VoitureTest() {
    }

    void test_Voiture_immatriculation_field_exists() {
        assertFieldIsDefined(String.class, "immatriculation", this.studentClass);
    }

    void test_Voiture_gabarit_field_exists() {
        assertFieldIsDefined(Gabarit.class, "gabarit", this.studentClass);
    }
}
