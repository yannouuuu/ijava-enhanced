package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:PGCDTest.class */
class PGCDTest extends HiddenTest {
    PGCDTest() {
    }

    void test_80_70_donne_10(Program program) {
        provideInput(program, 80);
        provideInput(program, 70);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Le pgcd est 10\n"));
        program.algorithm();
    }

    void test_5_3_donne_1(Program program) {
        provideInput(program, 5);
        provideInput(program, 3);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Le pgcd est 1\n"));
        program.algorithm();
    }
}
