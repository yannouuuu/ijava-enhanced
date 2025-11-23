package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:MajMinTest.class */
class MajMinTest extends HiddenTest {
    MajMinTest() {
    }

    void testaIsAandZisz(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Entrez une lettre en minuscule : "));
        provideInput(program, 'a');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("La lettre a en majuscule donne : A\nEntrez une lettre en majuscule : "));
        provideInput(program, 'Z');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("La lettre Z en minuscule donne : z\n"));
        program.algorithm();
    }
}
