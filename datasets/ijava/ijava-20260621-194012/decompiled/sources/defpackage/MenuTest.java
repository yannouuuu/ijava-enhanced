package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:MenuTest.class */
public class MenuTest extends HiddenTest {
    void _test(Program program, int i, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Bienvenue dans le SuperLogicielDeLanTroisMille\n\n1. Ouvrir un document existant.\n2. Créer un nouveau document.\n3. Enregistrer le document courant.\n4. Quitter ce magnifique logiciel.\n\nVeuillez entrer votre choix:"));
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
}
