package defpackage;

import ijava2.clitools.StudentInteractionSequence;
import java.util.Objects;

/* loaded from: ijava.jar:SauteMoutonTest.class */
class SauteMoutonTest extends HiddenTest {
    SauteMoutonTest() {
    }

    void test_appliquer_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "appliquer", new Class[]{char[].class, int[].class}, program);
    }

    void test_corriger_exists(Program program) {
        assertFunctionIsDefined(Integer.TYPE, "corriger", new Class[]{Integer.TYPE, Integer.TYPE}, program);
    }

    void test_saisir_exists(Program program) {
        assertFunctionIsDefined(int[].class, "saisir", new Class[]{char[].class}, program);
    }

    void test_saisir_negatif_erreur_2_saut(Program program) {
        assertArrayEquals(new int[]{1, 3}, try_saisir(program, -1, 2));
    }

    void test_saisir_1_erreur_2_saut_droite(Program program) {
        assertArrayEquals(new int[]{1, 3}, try_saisir(program, 1, 2));
    }

    void test_saisir_1_erreur_3_avancer_droite(Program program) {
        assertArrayEquals(new int[]{2, 3}, try_saisir(program, 1, 3));
    }

    void test_saisir_8_erreur_6_saut_gauche(Program program) {
        assertArrayEquals(new int[]{5, 3}, try_saisir(program, 8, 6));
    }

    void test_saisir_7_erreur_6_saut_gauche(Program program) {
        assertArrayEquals(new int[]{5, 3}, try_saisir(program, 7, 6));
    }

    void test_saisir_7_erreur_5_avancer_gauche(Program program) {
        assertArrayEquals(new int[]{4, 3}, try_saisir(program, 7, 5));
    }

    int[] try_saisir(Program program, int i, int i2) {
        SauteMouton sauteMouton = (SauteMouton) program;
        Objects.requireNonNull(sauteMouton);
        Objects.requireNonNull(sauteMouton);
        Objects.requireNonNull(sauteMouton);
        Objects.requireNonNull(sauteMouton);
        Objects.requireNonNull(sauteMouton);
        Objects.requireNonNull(sauteMouton);
        Objects.requireNonNull(sauteMouton);
        char[] cArr = {'>', '>', '>', '_', '<', '<', '<'};
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith(">>>_<<<\nPosition du mouton"));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith(">>>_<<<\nPosition du mouton"));
        provideInput(program, Integer.valueOf(i2));
        return sauteMouton.saisir(cArr);
    }

    void test_bloque_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "bloque", new Class[]{char[].class}, program);
    }

    void test_gagne_exists(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "gagne", new Class[]{char[].class}, program);
    }
}
