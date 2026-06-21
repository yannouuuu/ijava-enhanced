package defpackage;

import java.util.Objects;

/* loaded from: ijava.jar:StructureDeDonneesTest.class */
class StructureDeDonneesTest extends HiddenTest {
    StructureDeDonneesTest() {
    }

    void test_constantes_declarees(Program program) {
        assertFieldIsDefined(Character.TYPE, "ESPACE", program);
        assertFieldIsDefined(Character.TYPE, "DROITE", program);
        assertFieldIsDefined(Character.TYPE, "GAUCHE", program);
    }

    void test_constantes_initialisees(Program program) {
        StructureDeDonnees structureDeDonnees = (StructureDeDonnees) program;
        Objects.requireNonNull(structureDeDonnees);
        assertEquals(95, 95);
        Objects.requireNonNull(structureDeDonnees);
        assertEquals(62, 62);
        Objects.requireNonNull(structureDeDonnees);
        assertEquals(60, 60);
    }

    void test_initialiser_exists(Program program) {
        assertFunctionIsDefined(char[].class, "initialiser", new Class[]{Integer.TYPE}, program);
    }

    void test_toString_exists(Program program) {
        assertFunctionIsDefined(String.class, "toString", new Class[]{char[].class}, program);
    }
}
