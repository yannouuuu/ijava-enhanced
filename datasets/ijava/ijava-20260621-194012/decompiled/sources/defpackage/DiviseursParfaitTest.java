package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DiviseursParfaitTest.class */
class DiviseursParfaitTest extends HiddenTest {
    DiviseursParfaitTest() {
    }

    void test_6(Program program) {
        provideInput(program, 6);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Diviseurs : 6 3 2 1.\nNombre parfait !\n"));
        program.algorithm();
    }

    void test_7(Program program) {
        provideInput(program, 7);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Diviseurs : 7 1.\n"));
        program.algorithm();
    }
}
