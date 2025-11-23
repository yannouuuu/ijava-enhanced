package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:CaractereSuivantTest.class */
class CaractereSuivantTest extends HiddenTest {
    CaractereSuivantTest() {
    }

    void testAdonneB(Program program) {
        expectPrompt(program);
        provideInput(program, 'a');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Le caractère après a est b\n"));
        program.algorithm();
    }

    void testZdonneCrochet(Program program) {
        expectPrompt(program);
        provideInput(program, 'Z');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Le caractère après Z est [\n"));
        program.algorithm();
    }

    void testYdonneZ(Program program) {
        expectPrompt(program);
        provideInput(program, 'y');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Le caractère après y est z\n"));
        program.algorithm();
    }
}
