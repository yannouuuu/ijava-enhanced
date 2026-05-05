package defpackage;

/* loaded from: ijava.jar:ParcTest.class */
class ParcTest extends HiddenTest {
    ParcTest() {
    }

    void test_Parc_vehicules_field_exists() {
        assertFieldIsDefined(Voiture[].class, "vehicules", this.studentClass);
    }

    void test_Parc_nbVehicules_field_exists() {
        assertFieldIsDefined(Boolean.TYPE, "nbVehicules", this.studentClass);
    }
}
