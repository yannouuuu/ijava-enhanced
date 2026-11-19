package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:MenuTest.class */
public class MenuTest extends HiddenTest {
    void _test(Program program, int i, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Veuillez entrer votre choix: "));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Vous avez choisi: \"" + str + "\"\n"));
        program.algorithm();
    }

    void testChoix1(Program program) {
        _test(program, 1, "Ouvrir un document existant.");
    }

    void testChoix2(Program program) {
        _test(program, 2, "Créer un nouveau document.");
    }

    void testChoix3(Program program) {
        _test(program, 3, "Enregistrer le document courant.");
    }

    void testChoix4(Program program) {
        _test(program, 4, "Quitter ce magnifique logiciel.");
    }

    void testChoix5(Program program) {
        _test(program, 5, "Un mauvais chiffre !");
    }
}
