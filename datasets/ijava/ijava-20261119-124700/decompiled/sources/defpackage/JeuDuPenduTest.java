package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:JeuDuPenduTest.class */
class JeuDuPenduTest extends HiddenTest {
    JeuDuPenduTest() {
    }

    void test_programme_win(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 5 erreurs: * * * * * * * * *"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'o');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 5 erreurs: * * o * * * * * *"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'a');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 5 erreurs: * * o * * a * * *"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'e');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 5 erreurs: * * o * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'i');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 4 erreurs: * * o * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'u');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 3 erreurs: * * o * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'm');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 3 erreurs: * * o * * a m m e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'p');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 3 erreurs: p * o * * a m m e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'r');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 3 erreurs: p r o * r a m m e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'g');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Vous avez gagné ! Il fallait trouver: programme\n"));
        program.algorithm();
    }

    void test_programme_loose(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 5 erreurs: * * * * * * * * *"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'p');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 5 erreurs: p * * * * * * * *"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'p');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 4 erreurs: p * * * * * * * *"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'e');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 4 erreurs: p * * * * * * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'i');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 3 erreurs: p * * * * * * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'a');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 3 erreurs: p * * * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'u');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 2 erreurs: p * * * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'o');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 2 erreurs: p * o * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 's');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 1 erreurs: p * o * * a * * e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'm');
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Il vous reste 1 erreurs: p * o * * a m m e"), StudentInteractionSequence.OutputCondition.terminatesWith("Entrez un caractère: "));
        provideInput(program, 'x');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Vous avez perdu ! Il fallait trouver: programme\n"));
        program.algorithm();
    }
}
