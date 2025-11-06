package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:PleinTest.class */
class PleinTest extends HiddenTest {
    PleinTest() {
    }

    void test_1_e(Program program) {
        expectPrompt(program);
        provideInput(program, 1);
        expectPrompt(program);
        provideInput(program, 'e');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("e\n"));
        program.algorithm();
    }

    void test_5_o(Program program) {
        expectPrompt(program);
        provideInput(program, 5);
        expectPrompt(program);
        provideInput(program, 'o');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("o\noo\nooo\noooo\nooooo\n"));
        program.algorithm();
    }
}
